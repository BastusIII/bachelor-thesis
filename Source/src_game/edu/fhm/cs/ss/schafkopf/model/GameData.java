package edu.fhm.cs.ss.schafkopf.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistableObject;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistenceObject;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;

/**
 * This data class holds the complete information about a game. It extends the restricted information provided by {@link RestrictedGameData} with elements, that
 * are not available for all player views.
 *
 * @author Sebastian Stumpf
 *
 */
public class GameData extends RestrictedGameData implements IGameData {

	/**
	 * The full access player datas.
	 */
	private Map<PlayerPosition, IPlayerData> playerDatas;
	/**
	 * The remaining cards in the stack.
	 */
	private List<ICard> stack;
	/**
	 * Buffer for actions if they cannot be executed immediately.
	 */
	private Map<PlayerPosition, IActionData> actionBuffer;

	/**
	 * Copy Constructor.
	 *
	 * @param gameData
	 *            the game data to copy.
	 */
	public GameData(final IGameData gameData) {

		// initializing super object
		super();
		super.setCharge(gameData.getCharge() == null ? null : new Charge(gameData.getCharge()));
		super.setColor(gameData.getColor());
		super.setGamesFirstPlayerPosition(gameData.getGamesFirstPlayerPosition());
		super.setGameState(gameData.getGameState());
		super.setGameType(gameData.getGameType());
		super.setLastExecutedAction(gameData.getLastExecutedAction() == null ? null : new ActionData(gameData.getLastExecutedAction()));
		super.setLastRoundsPlayedCards(gameData.getLastRoundsPlayedCards() == null ? null : new EnumMap<>(gameData.getLastRoundsPlayedCards()));
		super.setLastRoundsWinner(gameData.getLastRoundsWinner());
		super.setLeadPlayerPosition(gameData.getLeadPlayerPosition());
		super.setOpponentTeam(gameData.getOpponentTeam() == null ? null : new ArrayList<PlayerPosition>(gameData.getOpponentTeam()));
		super.setPlayerOnTurnPosition(gameData.getPlayerOnTurnPosition());
		super.setPlayerTeam(gameData.getPlayerTeam() == null ? null : new ArrayList<PlayerPosition>(gameData.getPlayerTeam()));
		super.setResumed(gameData.isResumed());
		super.setRoundNumber(gameData.getRoundNumber());
		super.setRoundsFirstPlayerPosition(gameData.getRoundsFirstPlayerPosition());
		super.setStock(gameData.getStock());
		super.setWinnerTeam(gameData.getWinnerTeam());
		super.setGameSettings(gameData.getGameSettings() == null ? null : new GameSettings(gameData.getGameSettings()));

		// own elements
		this.actionBuffer = gameData.getActionBuffer() == null ? null : new EnumMap<PlayerPosition, IActionData>(PlayerPosition.class);
		if (actionBuffer != null) {
			for (final PlayerPosition position : gameData.getActionBuffer().keySet()) {
				this.actionBuffer.put(position, new ActionData(gameData.getActionBuffer().get(position)));
			}
		}

		this.playerDatas = gameData.getPlayerDatas() == null ? null : new EnumMap<PlayerPosition, IPlayerData>(PlayerPosition.class);
		if (playerDatas != null) {
			for (final PlayerPosition position : gameData.getPlayerDatas().keySet()) {
				this.playerDatas.put(position, new PlayerData(gameData.getPlayerDatas().get(position)));
			}
		}

		this.stack = gameData.getStack() == null ? null : new ArrayList<ICard>(gameData.getStack());
	}

	/**
	 * Instantiate a game data with default values and the values, provided by the given settings.
	 *
	 * @param settings
	 *            the settings.
	 * @param isResumed
	 *            true, if the game data was resumed and loaded from persistence.
	 */
	public GameData(final IGameSettings settings, final boolean isResumed) {

		// initializing super object
		super();
		super.setResumed(isResumed);
		super.setGameState(GameState.GET_RAISE);
		super.setPlayerOnTurnPosition(null);
		super.setLeadPlayerPosition(null);
		super.setRoundsFirstPlayerPosition(null);
		super.setGamesFirstPlayerPosition(null);
		super.setRoundNumber(0);
		super.setColor(null);
		super.setLastExecutedAction(null);
		super.setLastRoundsPlayedCards(null);
		super.setLastRoundsWinner(null);
		super.setGameType(null);
		super.setCharge(new Charge());
		super.setPlayerTeam(new ArrayList<PlayerPosition>());
		super.setOpponentTeam(new ArrayList<PlayerPosition>());
		super.setWinnerTeam(null);
		super.setStock(0);
		super.setLastRoundsPlayedCards(new EnumMap<PlayerPosition, ICard>(PlayerPosition.class));
		super.setGameSettings(settings == null ? new GameSettings() : settings);

		// own elements
		this.actionBuffer = new EnumMap<PlayerPosition, IActionData>(PlayerPosition.class);
		this.stack = null;
		this.playerDatas = new EnumMap<PlayerPosition, IPlayerData>(PlayerPosition.class);
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
		final GameData other = (GameData) obj;
		if (actionBuffer == null) {
			if (other.actionBuffer != null) {
				return false;
			}
		} else if (!actionBuffer.equals(other.actionBuffer)) {
			return false;
		}
		if (playerDatas == null) {
			if (other.playerDatas != null) {
				return false;
			}
		} else if (!playerDatas.equals(other.playerDatas)) {
			return false;
		}
		if (stack == null) {
			if (other.stack != null) {
				return false;
			}
		} else if (!stack.equals(other.stack)) {
			return false;
		}
		return true;
	}

	@Override
	public Map<PlayerPosition, IActionData> getActionBuffer() {

		return actionBuffer;
	}

	@Override
	public String getFilename() {

		return FILENAME;
	}

	@Override
	public IPersistableObject getPersistableObject() {

		return this;
	}

	@Override
	public IPersistenceObject getPersistenceObject() {

		return this;
	}

	@Override
	public Map<PlayerPosition, IPlayerData> getPlayerDatas() {

		return playerDatas;
	}

	@Override
	public Map<PlayerPosition, ? extends IRestrictedPlayerData> getRestrictedPlayerDatas() {

		return playerDatas;
	}

	@Override
	public List<ICard> getStack() {

		return stack;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (actionBuffer == null ? 0 : actionBuffer.hashCode());
		result = prime * result + (playerDatas == null ? 0 : playerDatas.hashCode());
		result = prime * result + (stack == null ? 0 : stack.hashCode());
		return result;
	}

	@Override
	public void setActionBuffer(final Map<PlayerPosition, IActionData> actionBuffer) {

		this.actionBuffer = actionBuffer;
	}

	@Override
	public void setPlayerDatas(final Map<PlayerPosition, IPlayerData> playerDatas) {

		this.playerDatas = playerDatas;

	}

	@Override
	public void setStack(final List<ICard> stack) {

		this.stack = stack;

	}
}