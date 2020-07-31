package edu.fhm.cs.ss.schafkopf.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICharge;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;

/**
 * This data class holds the restricted information about a game.<br>
 * <br>
 *
 * It is restricted to a point-of-view position, only the player at this position can see his complete player data. Besides, not all information about the game
 * is visible. For example the stack.
 *
 * @author Sebastian Stumpf
 *
 */
public class RestrictedGameData extends BasicGameData implements IRestrictedGameData {

	/**
	 * The game's charge.
	 */
	private ICharge charge;
	/**
	 * The Position of the player that makes/made the first draw of the current game.
	 */
	private PlayerPosition gamesFirstPlayerPosition;
	/**
	 * The game's settings.
	 */
	private IGameSettings settings;
	/**
	 * The game's state.
	 */
	private GameState gameState;
	/**
	 * The game's last executed action.
	 */
	private IActionData lastExecutedAction;
	/**
	 * The cards that were played the round before.
	 */
	private Map<PlayerPosition, ICard> lastRoundsPlayedCards;
	/**
	 * The winner of the last round's played cards.
	 */
	private PlayerPosition lastRoundsWinner;
	/**
	 * Position of the lead player who said he wanted to play.
	 */
	private PlayerPosition leadPlayerPosition;
	/**
	 * The opponent teams positions.
	 */
	private List<PlayerPosition> opponentTeam;
	/**
	 * Position of the player currently on turn.
	 */
	private PlayerPosition playerOnTurnPosition;
	/**
	 * The player teams positions.
	 */
	private List<PlayerPosition> playerTeam;
	/**
	 * Players datas.
	 */
	private final Map<PlayerPosition, IRestrictedPlayerData> restrictedPlayerDatas;
	/**
	 * The game is resumed.
	 */
	private boolean resumed;
	/**
	 * Number of the round, from 0-7.
	 */
	private int roundNumber;
	/**
	 * The Position of the player that makes/made the first draw of the current round.
	 */
	private PlayerPosition roundsFirstPlayerPosition;
	/**
	 * The value that gets paid to the winner in the next game.
	 */
	private int stock;
	/**
	 * The unrestricted player position. The player data on this position is not restricted.
	 */
	private final PlayerPosition pointOfViewPosition;
	/**
	 * The winner team.
	 */
	private Team winnerTeam;

	/**
	 * Default constructor, settings all values to null or default values.
	 */
	public RestrictedGameData() {

		super();
		this.pointOfViewPosition = null;
		this.restrictedPlayerDatas = null;
	}

	/**
	 * Constructor that makes a deep copy of the necessary fields from gameData, restricted to the given point-of-view position.
	 *
	 * @param pointOfViewPosition
	 *            the point-of-view position.
	 * @param gameData
	 *            the game containing the values to copy.
	 */
	public RestrictedGameData(final PlayerPosition pointOfViewPosition, final IGameData gameData) {

		super(gameData);
		this.pointOfViewPosition = pointOfViewPosition;
		this.resumed = gameData.isResumed();
		this.gameState = gameData.getGameState();
		this.playerOnTurnPosition = gameData.getPlayerOnTurnPosition();
		this.leadPlayerPosition = gameData.getLeadPlayerPosition();
		this.roundsFirstPlayerPosition = gameData.getRoundsFirstPlayerPosition();
		this.gamesFirstPlayerPosition = gameData.getGamesFirstPlayerPosition();
		this.roundNumber = gameData.getRoundNumber();
		Map<PlayerPosition, IRestrictedPlayerData> playerDatas = null;
		if (gameData.getPlayerDatas() != null) {
			playerDatas = new EnumMap<PlayerPosition, IRestrictedPlayerData>(PlayerPosition.class);
			// restrict the player data except the one on the player point of
			// view.
			for (final PlayerPosition position : gameData.getPlayerDatas().keySet()) {
				if (position.equals(pointOfViewPosition)) {
					playerDatas.put(position, new PlayerData(gameData.getPlayerDatas().get(position)));
				} else {
					playerDatas.put(position, new RestrictedPlayerData(gameData.getPlayerDatas().get(position)));
				}
			}
		}
		this.restrictedPlayerDatas = playerDatas;
		this.lastExecutedAction = gameData.getLastExecutedAction() == null ? null : new ActionData(gameData.getLastExecutedAction());
		this.lastRoundsPlayedCards = gameData.getLastRoundsPlayedCards() == null ? null : new EnumMap<>(gameData.getLastRoundsPlayedCards());
		this.lastRoundsWinner = gameData.getLastRoundsWinner();
		this.charge = gameData.getCharge() == null ? null : new Charge(gameData.getCharge());
		this.playerTeam = gameData.getPlayerTeam() == null ? null : new ArrayList<PlayerPosition>(gameData.getPlayerTeam());
		this.opponentTeam = gameData.getOpponentTeam() == null ? null : new ArrayList<PlayerPosition>(gameData.getOpponentTeam());
		this.winnerTeam = gameData.getWinnerTeam();
		this.stock = gameData.getStock();
		this.settings = gameData.getGameSettings() == null ? null : new GameSettings(gameData.getGameSettings());
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RestrictedGameData other = (RestrictedGameData) obj;
		if (charge == null) {
			if (other.charge != null) {
				return false;
			}
		} else if (!charge.equals(other.charge)) {
			return false;
		}
		if (gameState != other.gameState) {
			return false;
		}
		if (gamesFirstPlayerPosition != other.gamesFirstPlayerPosition) {
			return false;
		}
		if (lastExecutedAction == null) {
			if (other.lastExecutedAction != null) {
				return false;
			}
		} else if (!lastExecutedAction.equals(other.lastExecutedAction)) {
			return false;
		}
		if (lastRoundsPlayedCards == null) {
			if (other.lastRoundsPlayedCards != null) {
				return false;
			}
		} else if (!lastRoundsPlayedCards.equals(other.lastRoundsPlayedCards)) {
			return false;
		}
		if (lastRoundsWinner != other.lastRoundsWinner) {
			return false;
		}
		if (leadPlayerPosition != other.leadPlayerPosition) {
			return false;
		}
		if (opponentTeam == null) {
			if (other.opponentTeam != null) {
				return false;
			}
		} else if (!opponentTeam.equals(other.opponentTeam)) {
			return false;
		}
		if (playerOnTurnPosition != other.playerOnTurnPosition) {
			return false;
		}
		if (playerTeam == null) {
			if (other.playerTeam != null) {
				return false;
			}
		} else if (!playerTeam.equals(other.playerTeam)) {
			return false;
		}
		if (pointOfViewPosition != other.pointOfViewPosition) {
			return false;
		}
		if (restrictedPlayerDatas == null) {
			if (other.restrictedPlayerDatas != null) {
				return false;
			}
		} else if (!restrictedPlayerDatas.equals(other.restrictedPlayerDatas)) {
			return false;
		}
		if (settings == null) {
			if (other.settings != null) {
				return false;
			}
		} else if (!settings.equals(other.settings)) {
			return false;
		}
		if (resumed != other.resumed) {
			return false;
		}
		if (roundNumber != other.roundNumber) {
			return false;
		}
		if (roundsFirstPlayerPosition != other.roundsFirstPlayerPosition) {
			return false;
		}
		if (stock != other.stock) {
			return false;
		}
		if (winnerTeam != other.winnerTeam) {
			return false;
		}

		return true;
	}

	@Override
	public ICharge getCharge() {

		return charge;
	}

	@Override
	public IGameSettings getGameSettings() {

		return settings;
	}

	@Override
	public PlayerPosition getGamesFirstPlayerPosition() {

		return gamesFirstPlayerPosition;
	}

	@Override
	public GameState getGameState() {

		return gameState;
	}

	@Override
	public IActionData getLastExecutedAction() {

		return lastExecutedAction;
	}

	@Override
	public Map<PlayerPosition, ICard> getLastRoundsPlayedCards() {

		return lastRoundsPlayedCards;
	}

	@Override
	public PlayerPosition getLastRoundsWinner() {

		return lastRoundsWinner;
	}

	@Override
	public PlayerPosition getLeadPlayerPosition() {

		return leadPlayerPosition;
	}

	@Override
	public List<PlayerPosition> getOpponentTeam() {

		return opponentTeam;
	}

	@Override
	public PlayerPosition getPlayerOnTurnPosition() {

		return playerOnTurnPosition;
	}

	@Override
	public List<PlayerPosition> getPlayerTeam() {

		return playerTeam;
	}

	@Override
	public PlayerPosition getPointOfViewPosition() {

		return pointOfViewPosition;
	}

	@Override
	public Map<PlayerPosition, ? extends IRestrictedPlayerData> getRestrictedPlayerDatas() {

		return restrictedPlayerDatas;
	}

	@Override
	public int getRoundNumber() {

		return roundNumber;
	}

	@Override
	public PlayerPosition getRoundsFirstPlayerPosition() {

		return roundsFirstPlayerPosition;
	}

	@Override
	public int getStock() {

		return stock;
	}

	@Override
	public List<PlayerPosition> getTeam(final Team team) {

		if (Team.OPPONENT_TEAM.equals(team)) {
			return this.opponentTeam;
		} else if (Team.PLAYER_TEAM.equals(team)) {
			return this.playerTeam;
		} else {
			return new ArrayList<PlayerPosition>();
		}

	}

	@Override
	public Team getWinnerTeam() {

		return winnerTeam;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (charge == null ? 0 : charge.hashCode());
		result = prime * result + (gameState == null ? 0 : gameState.hashCode());
		result = prime * result + (gamesFirstPlayerPosition == null ? 0 : gamesFirstPlayerPosition.hashCode());
		result = prime * result + (lastExecutedAction == null ? 0 : lastExecutedAction.hashCode());
		result = prime * result + (lastRoundsPlayedCards == null ? 0 : lastRoundsPlayedCards.hashCode());
		result = prime * result + (lastRoundsWinner == null ? 0 : lastRoundsWinner.hashCode());
		result = prime * result + (leadPlayerPosition == null ? 0 : leadPlayerPosition.hashCode());
		result = prime * result + (opponentTeam == null ? 0 : opponentTeam.hashCode());
		result = prime * result + (playerOnTurnPosition == null ? 0 : playerOnTurnPosition.hashCode());
		result = prime * result + (playerTeam == null ? 0 : playerTeam.hashCode());
		result = prime * result + (pointOfViewPosition == null ? 0 : pointOfViewPosition.hashCode());
		result = prime * result + (restrictedPlayerDatas == null ? 0 : restrictedPlayerDatas.hashCode());
		result = prime * result + (settings == null ? 0 : settings.hashCode());
		result = prime * result + (resumed ? 1231 : 1237);
		result = prime * result + roundNumber;
		result = prime * result + (roundsFirstPlayerPosition == null ? 0 : roundsFirstPlayerPosition.hashCode());
		result = prime * result + stock;
		result = prime * result + (winnerTeam == null ? 0 : winnerTeam.hashCode());
		return result;
	}

	@Override
	public void incrementRoundNumber() {

		this.roundNumber++;
	}

	@Override
	public boolean isResumed() {

		return resumed;
	}

	@Override
	public void setCharge(final ICharge charge) {

		this.charge = charge;
	}

	@Override
	public void setGameSettings(final IGameSettings gameSettings) {

		this.settings = gameSettings;
	}

	@Override
	public void setGamesFirstPlayerPosition(final PlayerPosition gamesFirstPlayerPosition) {

		this.gamesFirstPlayerPosition = gamesFirstPlayerPosition;
	}

	@Override
	public void setGameState(final GameState gameState) {

		this.gameState = gameState;
	}

	@Override
	public void setLastExecutedAction(final IActionData lastExecutedAction) {

		this.lastExecutedAction = lastExecutedAction;
	}

	@Override
	public void setLastRoundsPlayedCards(final Map<PlayerPosition, ICard> lastRoundsPlayedCards) {

		this.lastRoundsPlayedCards = lastRoundsPlayedCards;
	}

	@Override
	public void setLastRoundsWinner(final PlayerPosition lastRoundsWinner) {

		this.lastRoundsWinner = lastRoundsWinner;
	}

	@Override
	public void setLeadPlayerPosition(final PlayerPosition leadPlayerPosition) {

		this.leadPlayerPosition = leadPlayerPosition;
	}

	@Override
	public void setOpponentTeam(final List<PlayerPosition> opponentTeam) {

		this.opponentTeam = opponentTeam;
	}

	@Override
	public void setPlayerOnTurnPosition(final PlayerPosition playerOnTurnPosition) {

		this.playerOnTurnPosition = playerOnTurnPosition;
	}

	@Override
	public void setPlayerTeam(final List<PlayerPosition> playerTeam) {

		this.playerTeam = playerTeam;
	}

	@Override
	public void setResumed(final boolean resumed) {

		this.resumed = resumed;
	}

	@Override
	public void setRoundNumber(final int roundNumber) {

		this.roundNumber = roundNumber;
	}

	@Override
	public void setRoundsFirstPlayerPosition(final PlayerPosition roundsFirstPlayerPosition) {

		this.roundsFirstPlayerPosition = roundsFirstPlayerPosition;
	}

	@Override
	public void setStock(final int stock) {

		this.stock = stock;
	}

	@Override
	public void setWinnerTeam(final Team winnerTeam) {

		this.winnerTeam = winnerTeam;
	}
}