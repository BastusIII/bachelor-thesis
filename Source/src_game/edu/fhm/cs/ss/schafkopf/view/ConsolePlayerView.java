package edu.fhm.cs.ss.schafkopf.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import edu.fhm.cs.ss.schafkopf.view.baseclasses.BasePlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IInteractivePlayerView;
import edu.fhm.cs.ss.schafkopf.view.utilities.Interpreter;
import edu.fhm.cs.ss.schafkopf.view.utilities.SharedViewRessources;
// TODO: ERROR on purpose for testing -> invalid import
// import edu.fhm.cs.ss.schafkopf.model.game.GameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveGameController;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * The console player view is an interactive view with a console interface. It interacts with a {@link IPrimitiveGameController}.<br>
 * <br>
 *
 * Game data received in an update is displayed in the console and all user inputs are tried to interpret as actions. Successfully interpreted actions are
 * forwarded to the controller without validating them here. If the action was invalid and thus not executed by the controller, the returned
 * {@link ActionValidationCode} is used for a formatted output of the reason for the controllers refuse.<br>
 * This implementation uses 3 Threads. One for displaying incoming game data. One to scan user input and one to interpret the last scanned user input. To scan
 * and interpret user input the producer consumer pattern is used. all Threads will terminate, if {@link BasePlayerView#isStopped()} returns true.
 *
 * @author Sebastian Stumpf
 *
 */
public class ConsolePlayerView extends BasePlayerView implements IInteractivePlayerView {

	/**
	 * This Thread consumes commands provided by the {@link ReaderThread}.
	 *
	 * @author Sebastian Stumpf
	 *
	 */
	private class ConsumerThread extends Thread {

		@Override
		public void run() {

			while (!isStopped()) {
				try {
					synchronized (consumeMonitor) {
						consumeMonitor.wait();
					}
				} catch (final InterruptedException e) {
					if (isStopped()) {
						break;
					}
				}
				synchronized (displayMonitor) {
					if (currentCommand.startsWith("command")) {
						displayAvailableCommands();
					}
					if (currentCommand.startsWith("back")) {
						getGameController().back();
					} else {
						currentAction = interpreter.interpretString(currentCommand);
						if (currentAction == null) {
							System.out.println("Anweisung konnte nicht interpretiert werden.");
						} else {
							switch (getGameController().handleGameAction(currentAction)) {
								case REQUIRED_DATA_CORRUPT:
									System.err.println("ERROR: Anweisung wurde fehlerhaft interpretiert.");
									break;
								case CARD_NOTALLOWED:
									System.out.println("Diese Karte darf nicht gespielt werden.");
									break;
								case CHOOSE_COLORNOTALLOWED:
									System.out.println("Die gewählte Farbe ist nicht erlaubt.");
									break;
								case CHOOSE_TYPENOTALLOWED:
									System.out.println("Der gewählte Spieltyp ist nicht erlaubt.");
									break;
								case GET_HANDFULL:
									System.out.println("Bei voller Hand bekommt man keine Karten mehr.");
									break;
								case RAISE_ALREADYRAISED:
									System.out.println("Man darf nur einmal legen.");
									break;
								case RAISE_TOOMUCHCARDS:
									System.out.println("Mit voller Hand darf man nicht mehr legen.");
									break;
								case STATE_WRONG:
									System.out.println("Dieser Zug darf bei aktuellem Spielstatus nicht gemacht werden.");
									break;
								case STRIKEBACK_ALREADYSTRUCKBACK:
									System.out.println("Man darf nur einmal Retour geben.");
									break;
								case STRIKEBACK_NOTPLAYER:
									System.out.println("Als Nichtspieler darf man kein Retour geben.");
									break;
								case STRIKE_ALREADYSTRUCK:
									System.out.println("Man darf nur einmal spritzen.");
									break;
								case STRIKE_NOTOPPONENT:
									System.out.println("Als Spieler darf man nicht spritzen.");
									break;
								case TURN_NOTONTURN:
									System.out.println("Nicht an der Reihe.");
									break;
								case ID_INVALID:
									System.out.println("ERROR: Ungültige Id.");
									break;
								case STARTNEXT_ALREADYACCEPTING:
									System.out.println("Dein Wunsch neuzustarten wurde schon registriert.");
									break;
								default:
									break;
							}
							currentAction = null;
						}
					}
				}
				synchronized (readMonitor) {
					readMonitor.notifyAll();
				}
			}
		}
	}

	/**
	 * This Thread displays the game data objects from the {@link #playerUtilsQueue}.
	 *
	 * @author Sebastian Stumpf
	 *
	 */
	private class DisplayGameThread extends Thread {
		@Override
		public void run() {

			boolean firstRun = true;
			while (!isStopped()) {
				IRestrictedPlayerUtils playerUtils;
				try {
					synchronized (updateutilsMonitor) {
						while (playerUtilsQueue.isEmpty()) {
							updateutilsMonitor.wait();
						}
						playerUtils = playerUtilsQueue.poll();
						synchronized (displayMonitor) {
							displayGame(firstRun, playerUtils);
							firstRun = false;
						}
					}
				} catch (final InterruptedException e) {
					if (isStopped()) {
						break;
					}
				}

			}
		}
	}

	/**
	 * This Thread reads commands from the command line and notifies the {@link ConsumerThread} if a new command war read.
	 *
	 * @author Sebastian Stumpf
	 *
	 */
	private class ReaderThread extends Thread {

		@Override
		public void run() {

			while (!isStopped()) {
				try {
					currentCommand = scanner.nextLine();
					synchronized (consumeMonitor) {
						consumeMonitor.notifyAll();
					}
					synchronized (readMonitor) {
						readMonitor.wait();
					}
				} catch (final InterruptedException | IndexOutOfBoundsException e) {
					if (isStopped()) {
						break;
					}
				}
			}
		}
	}

	/** Monitor used to synchronize and notify the consumer thread. */
	private final Object consumeMonitor;
	/** Holds the last interpreted action. */
	private IAction currentAction;
	/** Holds the last read command. */
	private String currentCommand;
	/** Monitor used to synchronize access to the playerUtilsQueue. */
	private final Object updateutilsMonitor;
	/**
	 * Monitor used to display on the console. So the consumer thread cann not display error messages into a current displaying of the game data and thus
	 * destroy the layout.
	 */
	private final Object displayMonitor;
	/** The interpreter provides the utility to interprete strings as actions. */
	private final Interpreter interpreter;
	/** Monitor used to synchronize and update the read thread. */
	private final Object readMonitor;
	/** The scanner for console inputs. */
	private final Scanner scanner;
	/** The incoming player utilities are stored in this queue until the display thread consumes them. */
	private final Queue<IRestrictedPlayerUtils> playerUtilsQueue;

	/**
	 * Default constructor initializing the controller with null.
	 */
	public ConsolePlayerView() {

		this(null);
	}

	/**
	 * Instantiate a console player with a given controller.
	 *
	 * @param controller
	 *            the controller to interact with.
	 */
	public ConsolePlayerView(final IPrimitiveGameController controller) {

		super(controller);
		this.scanner = SharedViewRessources.getScanner();
		updateutilsMonitor = new Object();
		displayMonitor = new Object();
		readMonitor = new Object();
		consumeMonitor = new Object();
		this.playerUtilsQueue = new LinkedList<IRestrictedPlayerUtils>();
		this.interpreter = new Interpreter();
	}

	@Override
	public void start() {

		registerThreads(new ConsumerThread(), new ReaderThread(), new DisplayGameThread());
		// provide interpreter with player id
		interpreter.setPlayerId(getPlayerId());
		super.start();
	}

	@Override
	public void update() {

		// we get a gameDataChanged call, if the gameData has changed.
	}

	@Override
	public void updateGameData(final IRestrictedPlayerUtils restrictedplayerUtils) {

		synchronized (updateutilsMonitor) {
			playerUtilsQueue.add(restrictedplayerUtils);
			updateutilsMonitor.notifyAll();
		}

	}

	/**
	 * Simulates a clear console.
	 */
	private void clearConsole() {

		for (int i = 0; i < 50; ++i) {
			System.out.println();
		}
	}

	/**
	 * Display commands.
	 */
	private void displayAvailableCommands() {

		System.out.println("Format: [Aktion],[Argumente...]");
		System.out.println("Verfügbare Aktionen:");
		System.out.println("\tplay,[Farbe],[Wert] -> Karte spielen");
		System.out.println("\t\t[Farbe] := schelln, herz, gras, eichel (oder s,h,g,e)");
		System.out.println("\t\t[Wert] := 7-10, unter, ober, koenig, sau (oder 7-9,u,o,k,s)");
		System.out.println("\tchoose,[Spieltyp],[Farbe] -> Spiel wählen");
		System.out.println("\t\t[Spieltyp] := pass, wenz, wenztout, farbwenz, farbwenztout, solo, solotout, sauspiel, si");
		System.out.println("\traise[boolean] -> Spielwert verdoppeln mit max 4 Karten in der Hand");
		System.out.println("\t\t[boolean] := f -> nein, andere oder kein Wert -> ja");
		System.out.println("\tstrike -> Stoß oder retour");
		System.out.println("\tget -> 4 Karten vom Stapel ausgeteilt bekommen");
		System.out.println("\tnext game -> nächstes Spiel starten");
		System.out.println("\tback -> zurück zum Startbildschirm navigieren");
	}

	/**
	 * Display further game information.
	 *
	 * @param playerUtils
	 *            the player utils, used for displaying.
	 */
	private void displayFurtherGameInformation(final IRestrictedPlayerUtils playerUtils) {

		// game status
		System.out.format("%-32s%-32s%n", "Status:", playerUtils.getRestrictedGameData().getGameState());

		// last action
		System.out.format("%-32s%-32s%n", "Letzte Aktion:", interpretActionData(playerUtils));

		// lead player and gametype

		System.out.format("%-32s%-32s%n", "Spiel:", interpretCurrentChosenGame(playerUtils));

		// player on turn
		System.out.format("%-32s%-32s%n", "Am Zug:", playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(playerUtils.getRestrictedGameData().getPlayerOnTurnPosition()));

		// round number
		System.out.format("%-32s%-32s%n", "Runde:", playerUtils.getRestrictedGameData().getRoundNumber());

		// stock
		System.out.format("%-32s%-32s%n", "Stock:", playerUtils.getRestrictedGameData().getStock());

		// game multiplier
		System.out.format("%-32s%-32s%n", "Multiplikator:", playerUtils.getTotalMultiplier());

		// base game value
		System.out.format("%-32s%-32s%n", "Basis Spielwert:", playerUtils.getRestrictedGameData().getCharge().getBasic());

		// high cards in row (Laufende)
		System.out.format("%-32s%-32s%n", "Laufende des Spielers:", playerUtils.calculateHighCardsInRow());

		// last rounds played cards and winner

		System.out.format("%-32s%-32s%n", "Letzter Stich", interpretLastStich(playerUtils));

		// player team
		System.out.format("%-32s%-32s%n", "Spieler Team:", interpretTeam(playerUtils, Team.PLAYER_TEAM));

		// non player team
		System.out.format("%-32s%-32s%n", "Nichtspieler Team:", interpretTeam(playerUtils, Team.OPPONENT_TEAM));

		// result

		// teams high cards are set at the end of a game
		System.out.format("%-32s%-32s%n", "Laufende der Teams:", playerUtils.getRestrictedGameData().getCharge().getBounty());
		// calculated game value
		System.out.format("%-32s%-32s%n", "Spielwert:", playerUtils.getRestrictedGameData().getCharge().getTotalCharge());
		// points of one party below 30
		System.out.format("%-32s%-32s%n", "Schneider:", playerUtils.getRestrictedGameData().getCharge().getSchneider() == 0 ? "nein"
				: playerUtils.getRestrictedGameData().getCharge().getSchneider() == 1 ? "ja" : "schwarz");
		// winner team
		System.out.format("%-32s%-32s%n", "Gewinner Team:", interpretTeam(playerUtils, playerUtils.getRestrictedGameData().getWinnerTeam()));
	}

	/**
	 * Display the whole game.
	 *
	 * @param playerUtils
	 *            the player utils, used for displaying.
	 * @param firstRun
	 *            if true, an other string will displayed to the console.
	 */
	private void displayGame(final boolean firstRun, final IRestrictedPlayerUtils playerUtils) {

		clearConsole();
		printHorizontalLine();
		displayPlayers(playerUtils);
		displayTable(playerUtils);
		printHorizontalLine();
		displayFurtherGameInformation(playerUtils);
		printHorizontalLine();
		if (firstRun) {
			System.out.println();
			if (playerUtils.getRestrictedGameData().isResumed()) {
				System.out.println("Und weiter gehts.");
			} else {
				System.out.println("Es geht los. Lass dir erstmal Karten geben (\"get\").");
			}
			System.out.println("Um weitere Anweisungen anzuzeigen, bitte \"commands\" eingeben.");
		}
	}

	/**
	 * Display a player by a given data.
	 *
	 * @param playerUtils
	 *            the player utils, used for displaying.
	 * @param data
	 *            the player data that will be displayed.
	 */
	private void displayPlayer(final IRestrictedPlayerUtils playerUtils, final IRestrictedPlayerData data) {

		System.out.format("%-32s%-32s%n", "Position:", data.getPosition());
		System.out.format("%-32s%-32s%n", "Name:", data.getName());
		System.out.format("%-32s%-32s%n", "gelegt/contra/retour:", (data.isRaising() ? "+/" : "-/") + (data.isStriking() ? "+/" : "-/") + (data.isStrikingBack() ? "+/" : "-/"));
		System.out.format("%-32s%-32s%n", "Punkte:", data.getPoints());
		System.out.format("%-32s%-32s%n", "Geld:", data.getCredit());
		System.out.format("%-32s%-32s%n", "Gespielte Karte: ", data.getPlayedCard() == null ? "keine" : data.getPlayedCard());
		final StringBuilder builder = new StringBuilder();
		if (data.getPosition().equals(playerUtils.getPovPlayerData().getPosition())) {
			for (final ICard card : playerUtils.getPovPlayerData().getCurrentHand()) {
				builder.append("[" + card + "] ");
			}
			if (builder.length() == 0) {
				builder.append("leer");

			}
		} else {
			for (int i = 0; i < data.getSizeOfHand(); ++i) {
				builder.append("[???] ");
			}
			if (builder.length() == 0) {
				builder.append("leer");
			}
		}
		System.out.format("%-32s%-32s%n", "Karten: ", builder.toString());
	}

	/**
	 * Display all players.
	 *
	 * @param playerUtils
	 *            the player utils, used for displaying.
	 */
	private void displayPlayers(final IRestrictedPlayerUtils playerUtils) {

		for (final IRestrictedPlayerData data : playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().values()) {
			// the input player will always be displayed at the bottom
			if (!data.getPosition().equals(playerUtils.getPovPlayerData().getPosition())) {
				displayPlayer(playerUtils, data);
				printHorizontalLine();
			}
		}
		displayPlayer(playerUtils, playerUtils.getPovPlayerData());
		printHorizontalLine();
	}

	/**
	 * Display the cards on the table.
	 *
	 * @param playerUtils
	 *            the player utils, used for displaying.
	 */
	private void displayTable(final IRestrictedPlayerUtils playerUtils) {

		final StringBuilder builder = new StringBuilder();
		for (final IRestrictedPlayerData player : playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().values()) {
			builder.append("[" + (player.getPlayedCard() == null ? "-" : player.getPlayedCard()) + "] ");
		}
		System.out.format("%-32s%-32s%n", "Karten auf dem Tisch:", builder.toString());
	}

	/**
	 * Generate a string that formats the current action data.
	 *
	 * @param playerUtils
	 *            the player utils, used for generating the string.
	 * @return the generated string.
	 */
	private String interpretActionData(final IRestrictedPlayerUtils playerUtils) {

		final StringBuilder builder = new StringBuilder();
		final IActionData lastExecutedAction = playerUtils.getRestrictedGameData().getLastExecutedAction();
		if (lastExecutedAction == null) {
			builder.append("-");
		} else {
			final PlayerPosition position = lastExecutedAction.getExecutingPlayerPosition();
			final ActionType type = lastExecutedAction.getActionType();
			final boolean bool = lastExecutedAction.getBoolean();
			final ICard card = lastExecutedAction.getPlayedCard();
			final IBasicGameData chosenGame = lastExecutedAction.getChosenGame();
			if (position != null) {
				builder.append(playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(lastExecutedAction.getExecutingPlayerPosition()).getName());
				builder.append(" ");
			}
			if (type != null) {
				switch (type) {
					case CHOOSE:
						builder.append("will ");
						if (type.equals(GameType.PASS)) {
							builder.append("passen.");
						} else {
							builder.append(chosenGame.getGameType());
							builder.append(" spielen.");
						}
						break;
					case GET:
						builder.append("bekommt Karten.");
						break;
					case PLAY:
						builder.append("spielt ");
						builder.append(card.getName());
						builder.append(".");
						break;
					case RAISE:
						builder.append("legt.");
						break;
					case STRIKE:
						if (bool) {
							builder.append("spritzt.");
						}
						break;
					case STRIKEBACK:
						if (bool) {
							builder.append("gibt Retour.");
						}
						break;
					case NEXTGAMEACCEPTED:
						builder.append("würde gerne das nächste Spiel beginnen.");
						break;
					case NEXTGAMESTARTED:
						builder.append("startet das nächste Spiel.");
						break;
					default:
						break;

				}
			}
		}
		return builder.toString();
	}

	/**
	 * Generate a string that formats the current played game.
	 *
	 * @param playerUtils
	 *            the player utils, used for generating the string.
	 * @return the generated string.
	 */
	private String interpretCurrentChosenGame(final IRestrictedPlayerUtils playerUtils) {

		String currentGame;
		if (playerUtils.getRestrictedGameData().getGameType() == null) {
			currentGame = "-";
		} else if (playerUtils.getRestrictedGameData().getGameType().equals(GameType.PASS) && playerUtils.getRestrictedGameData().getGameState().equals(GameState.FINISHED)) {
			currentGame = "Alle passen";
		} else if (playerUtils.getRestrictedGameData().getGameType().equals(GameType.SAUSPIEL)) {
			currentGame = playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(playerUtils.getRestrictedGameData().getLeadPlayerPosition()) + " spielt mit "
					+ playerUtils.getRestrictedGameData().getColor();
		} else {
			currentGame = playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(playerUtils.getRestrictedGameData().getLeadPlayerPosition()) + " spielt"
					+ (playerUtils.getRestrictedGameData().getColor() == null ? "" : " " + playerUtils.getRestrictedGameData().getColor()) + " " + playerUtils.getRestrictedGameData().getGameType();
		}
		return currentGame;
	}

	/**
	 * Generate a string that formats las round's played cards.
	 *
	 * @param playerUtils
	 *            the player utils, used for generating the string.
	 * @return the generated string.
	 */
	private String interpretLastStich(final IRestrictedPlayerUtils playerUtils) {

		final StringBuilder builder = new StringBuilder();
		if (playerUtils.getRestrictedGameData().getLastRoundsPlayedCards() == null) {
			builder.append("-");
		} else {
			for (final ICard card : playerUtils.getRestrictedGameData().getLastRoundsPlayedCards().values()) {
				builder.append("[" + (card == null ? "-" : card) + "] ");
			}
			if (builder.length() > 0) {
				builder.append(" -> geht an " + playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(playerUtils.getRestrictedGameData().getLastRoundsWinner()).getName());
			} else {
				builder.append("-");
			}
		}
		return builder.toString();
	}

	/**
	 * Generate a string that formats a team.
	 *
	 * @param playerUtils
	 *            the player utils, used for generating the string.
	 * @param team
	 *            the team.
	 * @return the generated string.
	 */
	private String interpretTeam(final IRestrictedPlayerUtils playerUtils, final Team team) {

		if (team == null) {
			return "-";
		}
		final Collection<PlayerPosition> playersPositions = playerUtils.getRestrictedGameData().getTeam(team);
		final Collection<IRestrictedPlayerData> players = new ArrayList<IRestrictedPlayerData>();
		for (final PlayerPosition playerPosition : playersPositions) {
			players.add(playerUtils.getRestrictedGameData().getRestrictedPlayerDatas().get(playerPosition));
		}
		return players.isEmpty() ? "-" : players.toString();
	}

	/**
	 * Print a horizontal line on the console.
	 */
	private void printHorizontalLine() {

		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 120; ++i) {
			builder.append("-");
		}
		System.out.println(builder.toString());
	}

}
