package edu.fhm.cs.ss.schafkopf.test.formattingandvalidation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.test.model.ITestGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This class contains utility methods to generate strings from data elements of a game state.
 *
 * @author Sebastian Stumpf
 *
 */
public class FormattingUtils {
	/**
	 * Append all actions in the collection up to a limit to the returned string.
	 *
	 * @param collection
	 *            the action collection.
	 * @param limit
	 *            the limit of generated actions.
	 * @return the actions string.
	 */
	public static String generateActionsString(final Collection<IActionData> collection, final int limit) {

		if (collection == null) {
			return "null";
		}
		final StringBuilder builder = new StringBuilder();
		int counter = 0;
		for (final IActionData data : collection) {
			if (counter++ == limit) {
				break;
			}
			builder.append("[ ");
			builder.append(generateSingleActionString(data));
			builder.append(" ]");
			if (counter < collection.size() && counter < limit) {
				builder.append(" / ");
			}
		}
		return builder.toString();
	}

	/**
	 * Generate a string of cards from a given map. The first appended element is the the card at the given first position.
	 *
	 * @param cards
	 *            the cards map.
	 * @param first
	 *            the first position.
	 * @return the cards string.
	 */
	public static String generateCardsStringInOrder(final Map<PlayerPosition, ICard> cards, final PlayerPosition first) {

		final StringBuilder builder = new StringBuilder();

		PlayerPosition currentPosition = first;

		if (cards != null) {
			do {
				if (cards.get(currentPosition) != null) {
					builder.append("[ ");
					builder.append(currentPosition);
					builder.append(" : ");
					builder.append(cards.get(currentPosition));
					builder.append(" ]");
				}
				currentPosition = currentPosition.getNext();
			} while (currentPosition != first);
		}
		return builder.toString();
	}

	/**
	 * Generate a deadlock String from a given test case.
	 *
	 * This is displaying the last game state and a simple representation of the cards played the rounds before. Also an action string in reverse order is
	 * appended.
	 *
	 * @param testCase
	 *            the test case to display.
	 * @return the deadlock string.
	 * @throws NullPointerException
	 *             if the test case game states contain critical invalid values.
	 */
	public static String generateDeadLockErrorString(final ITestGameData testCase) {

		final StringBuilder builder = new StringBuilder();
		builder.append("Last incoming actions\n");
		builder.append(generateReverseActionsString(testCase.getActionList(), 1000));
		builder.append("\n\n");
		builder.append("Simple Game process\n\n");
		builder.append(generateSimpleGameProgressString(testCase.getGamesList()));
		builder.append("Last game state\n");
		builder.append(testCase.getGamesList().isEmpty() ? " Game not yet started.\n" : generateGameDataString(testCase.getGamesList().get(testCase.getGamesList().size() - 1)));
		return builder.toString();
	}

	/**
	 * String Representation of a game data object with all informative values.
	 *
	 * @param gameData
	 *            the game data.
	 * @return the game data string.
	 */
	public static String generateGameDataString(final IGameData gameData) {

		final StringBuilder builder = new StringBuilder();
		// basic information
		builder.append(String.format("%-32s%-32s%n", "Status", gameData.getGameState()));
		builder.append(String.format("%-32s", "Stack"));
		if (gameData.getStack() == null) {
			builder.append("null\n");
			builder.append("--------------------------------\n");
		} else {
			int counter = 0;
			builder.append("[");
			final Iterator<ICard> iterator = gameData.getStack().iterator();
			while (iterator.hasNext()) {
				builder.append(iterator.next() + (iterator.hasNext() ? ", " : ""));
				if (++counter % 8 == 0 && iterator.hasNext()) {
					builder.append(String.format("%n%-32s", ""));
				}
			}
			builder.append("]\n");
		}
		builder.append(String.format("%-32s%-32s%n", "Runde", gameData.getRoundNumber()));
		builder.append(String.format("%-32s%-32s%n", "Wiederaufgenommen", gameData.isResumed()));
		builder.append(String.format("%-32s%-32s%n", "Typ", gameData.getGameType()));
		builder.append(String.format("%-32s%-32s%n", "Farbe", gameData.getColor()));
		builder.append(String.format("%-32s%-32s%n", "Spiel, erster Spieler", gameData.getGamesFirstPlayerPosition()));
		builder.append(String.format("%-32s%-32s%n", "Runde, erster Spieler", gameData.getRoundsFirstPlayerPosition()));
		builder.append(String.format("%-32s%-32s%n", "Am Zug", gameData.getPlayerOnTurnPosition()));
		builder.append(String.format("%-32s%-32s%n", "Spieler", gameData.getLeadPlayerPosition()));
		builder.append(String.format("%-32s%-32s%n", "Spieler Team", gameData.getPlayerTeam()));
		builder.append(String.format("%-32s%-32s%n", "Gegner Team", gameData.getOpponentTeam()));
		builder.append(String.format("%-32s", "Letze Aktion"));
		builder.append(generateSingleActionString(gameData.getLastExecutedAction()));
		builder.append("\n");
		builder.append(String.format("%-32s", "Action Puffer"));
		if (gameData.getActionBuffer() == null) {
			builder.append("null");
		} else {
			builder.append(generateActionsString(gameData.getActionBuffer().values(), 4));
		}
		builder.append("\n");

		// last rounds information
		builder.append(String.format("%-32s%-32s%n", "Karten, letzte Runde", gameData.getLastRoundsPlayedCards()));
		builder.append(String.format("%-32s%-32s%n", "Gewinner, letzte Runde", gameData.getLastRoundsWinner()));

		// charge information and game finished information
		builder.append(String.format("%-32s%-32s%n", "Gewinner Team", gameData.getWinnerTeam()));
		builder.append(String.format("%-32s%-32s%n", "Stock", gameData.getStock()));
		builder.append("--------------------------------\n");
		builder.append(String.format("%-32s", "Spielwert Informationen"));
		if (gameData.getCharge() == null) {
			builder.append("null\n");
		} else {
			builder.append("\n");
			builder.append("--------------------------------\n");
			builder.append(String.format("%-32s%-32s%n", "Basis Spielwert", gameData.getCharge().getBasic()));
			builder.append(String.format("%-32s%-32s%n", "Laufende Bonus", gameData.getCharge().getBounty()));
			builder.append(String.format("%-32s%-32s%n", "Exklusiver Multiplikator", gameData.getCharge().getExclusiveMultiplier()));
			builder.append(String.format("%-32s%-32s%n", "Legen Multiplikator", gameData.getCharge().getInitialMultiplier()));
			builder.append(String.format("%-32s%-32s%n", "Spritz/Retour Multiplikator", gameData.getCharge().getStrikeMultiplier()));
			builder.append(String.format("%-32s%-32s%n", "Schneider Bonus", gameData.getCharge().getSchneider()));
			builder.append(String.format("%-32s%-32s%n", "Stock Index", gameData.getCharge().getStockIndex()));
			builder.append(String.format("%-32s%-32s%n", "Stock Wert", gameData.getCharge().getStockValue()));
			builder.append(String.format("%-32s%-32s%n", "End Spielwert", gameData.getCharge().getTotalCharge()));
		}

		// player information
		builder.append("--------------------------------\n");
		builder.append(String.format("%-32s", "Spieler Informationen"));
		if (gameData.getPlayerDatas() == null) {
			builder.append("null\n");
			builder.append("--------------------------------\n");
		} else {
			if (gameData.getPlayerDatas().isEmpty()) {
				builder.append(gameData.getPlayerDatas());
				builder.append("\n");
				builder.append("--------------------------------\n");
			} else {
				builder.append("\n");
				builder.append("--------------------------------\n");
				for (final IPlayerData data : gameData.getPlayerDatas().values()) {
					builder.append(String.format("%-32s%-32s%n", "Name", data.getName()));
					builder.append(String.format("%-32s%-32s%n", "Position", data.getPosition()));
					builder.append(String.format("%-32s%-32s%n", "Gewähltes Spiel", data.getChosenGame() == null ? "null" : data.getChosenGame().getGameType() + " | "
							+ data.getChosenGame().getColor()));
					builder.append(String.format("%-32s%-32s%n", "Geld", data.getCredit()));
					builder.append(String.format("%-32s%-32s%n", "Aktuelle Hand", data.getCurrentHand()));
					builder.append(String.format("%-32s%-32s%n", "Start Hand", data.getInitialHand()));
					builder.append(String.format("%-32s%-32s%n", "Gespielte Karte", data.getPlayedCard()));
					builder.append(String.format("%-32s%-32s%n", "Punkte", data.getPoints()));
					builder.append(String.format("%-32s%-32s%n", "Gewonnene Karten", data.getWonCards()));
					builder.append(String.format("%-32s%-32s%n", "Legt/Spritz/Retour/Neustart",
							data.isRaising() + "/" + data.isStriking() + "/" + data.isStrikingBack() + "/" + data.isAcceptingNextGameStart()));
					builder.append("--------------------------------\n");
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Generate a string representation of players hands.
	 *
	 * @param players
	 *            the map of players.
	 * @return the players hands string.
	 */
	public static String generatePlayersHandsString(final Map<PlayerPosition, IPlayerData> players) {

		final StringBuilder builder = new StringBuilder();
		if (players == null) {
			builder.append("null\n");
		} else {
			for (final PlayerPosition pos : players.keySet()) {
				builder.append(pos);
				builder.append(" Karten: ");
				builder.append(players.get(pos).getCurrentHand());
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	/**
	 * Generate an action string in reverse order up to a given limit.
	 *
	 * The last element in the given list is displayed first.
	 *
	 * @param actions
	 *            the action list.
	 * @param limit
	 *            the limit.
	 * @return
	 */
	public static String generateReverseActionsString(final List<IActionData> actions, final int limit) {

		if (actions == null) {
			return "null";
		}
		// Generate an iterator. Start just after the last element.
		final ListIterator<IActionData> iterator = actions.listIterator(actions.size());
		final StringBuilder builder = new StringBuilder();
		int counter = 0;
		// Iterate in reverse.
		while (iterator.hasPrevious()) {
			if (counter++ == limit) {
				break;
			}
			final IActionData action = iterator.previous();
			builder.append("[ ");
			builder.append(generateSingleActionString(action));
			builder.append(" ]");
			if (iterator.hasPrevious() && counter < limit) {
				builder.append(" / ");
			}
		}

		return builder.toString();
	}

	/**
	 * Generate a simple game progress string from a list of games.
	 *
	 * @param gamesList
	 *            the list of games.
	 * @return the string.
	 */
	public static String generateSimpleGameProgressString(final List<IGameData> gamesList) {

		final StringBuilder builder = new StringBuilder();
		final Iterator<IGameData> iterator = gamesList.iterator();
		boolean firstRoundProceeded = false;
		PlayerPosition currentRoundFirstPlayer = null;
		PlayerPosition lastRoundsFirstPlayer = null;
		while (iterator.hasNext()) {
			final IGameData gameData = iterator.next();
			if (gameData.getGameState().equals(GameState.GET_RAISE) || gameData.getGameState().equals(GameState.CHOOSE) || gameData.getGameState().equals(GameState.STRIKE)
					|| gameData.getGameState().equals(GameState.STRIKEBACK)) {
				continue;
			}
			final FullAccessGameUtils utils = new FullAccessGameUtils(gameData);
			// first round, no card played
			if (!firstRoundProceeded) {
				currentRoundFirstPlayer = gameData.getRoundsFirstPlayerPosition();
				builder.append(gameData.getLeadPlayerPosition());
				builder.append(" spielt ");
				builder.append(gameData.getColor());
				builder.append(" ");
				builder.append(gameData.getGameType());
				builder.append("\n\n");
				firstRoundProceeded = true;
			}
			// card on table are null so information has to be drawn from last
			// rounds played cards.
			else if (utils.getCardsOnTable().size() == 0 && gameData.getRoundNumber() > 0) {
				lastRoundsFirstPlayer = currentRoundFirstPlayer;
				currentRoundFirstPlayer = gameData.getRoundsFirstPlayerPosition();
				builder.append("Runde ");
				builder.append(gameData.getRoundNumber() - 1);
				builder.append(": ");
				builder.append(generateCardsStringInOrder(gameData.getLastRoundsPlayedCards(), lastRoundsFirstPlayer));
				builder.append("\n");
				builder.append(generatePlayersHandsString(gameData.getPlayerDatas()));
				builder.append("\n");
			} else {
				builder.append("Runde ");
				builder.append(gameData.getRoundNumber());
				builder.append(": ");
				builder.append(generateCardsStringInOrder(utils.getCardsOnTable(), gameData.getRoundsFirstPlayerPosition()));
				builder.append("\n");
				builder.append(generatePlayersHandsString(gameData.getPlayerDatas()));
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	/**
	 * Generate a formatted String for a given action.
	 *
	 * @param action
	 *            the action.
	 * @return the string.
	 */
	public static String generateSingleActionString(final IActionData action) {

		if (action == null) {
			return "null";
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(action.getValidationCode() + " | ");
		builder.append(action.getExecutingPlayerPosition() + " | ");
		builder.append(action.getActionType() + " | ");
		builder.append(action.getChosenGame() == null ? "null | " : action.getChosenGame().getGameType() + " | " + action.getChosenGame().getColor() + " | ");
		builder.append(action.getPlayedCard() == null ? "null | " : action.getPlayedCard() + " | ");
		builder.append(action.getBoolean());
		return builder.toString();
	}
}
