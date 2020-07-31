package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;

/**
 * Extends {@link IBasicGameData} by values, that define a Schafkopf game and are available for all players views.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IRestrictedGameData extends IBasicGameData {
	/**
	 * The cards needed to run away.
	 */
	int CARDS_NEEDED_TO_RUN_AWAY = 3;
	/**
	 * Number of cards drawn with a draw action.
	 */
	int DRAWN_CARDS_PER_ACTION = 4;
	/**
	 * The maximum size of a hand.
	 */
	int MAX_HAND_SIZE = 8;
	/**
	 * The number of cards rising is allowed with.
	 */
	int MAX_RAISING_HAND_SIZE = 4;
	/**
	 * The minimal cards needed to get bounty for a basic game.
	 */
	int MIN_HIGHCARDS_BASIC = 3;
	/**
	 * The minimal cards needed to get bounty for a wenz game.
	 */
	int MIN_HIGHCARDS_WENZ = 2;
	/**
	 * The number of players.
	 */
	int NUMBER_OF_PLAYERS = 4;
	/**
	 * The rounds of a game.
	 */
	int ROUNDS_PER_GAME = 8;
	/**
	 * The trump color in a sauspiel.
	 */
	CardColor SAUSPIEL_TRUMP_COLOR = CardColor.HERZ;

	@Override
	boolean equals(Object obj);

	/**
	 * @return the game charge object.
	 */
	ICharge getCharge();

	/**
	 * @return the game's settings.
	 */
	IGameSettings getGameSettings();

	/**
	 * @return position of the player who comes/came out in the current game.
	 */
	PlayerPosition getGamesFirstPlayerPosition();

	/**
	 * @return the status of the game.
	 */
	GameState getGameState();

	/**
	 * @return the string representation of the last executed action.
	 */
	IActionData getLastExecutedAction();

	/**
	 * @return the cards of the last round, null if first round.
	 */
	Map<PlayerPosition, ICard> getLastRoundsPlayedCards();

	/**
	 * @return the position of the last round's winner.
	 */
	PlayerPosition getLastRoundsWinner();

	/**
	 * @return the lead player position.
	 */
	PlayerPosition getLeadPlayerPosition();

	/**
	 * @return the opponent team.
	 */
	List<PlayerPosition> getOpponentTeam();

	/**
	 * @return position of the player on turn in the current game.
	 */
	PlayerPosition getPlayerOnTurnPosition();

	/**
	 * @return the player team.
	 */
	List<PlayerPosition> getPlayerTeam();

	/**
	 * @return the player position that has full access on its player data.
	 */
	PlayerPosition getPointOfViewPosition();

	/**
	 * @return the restricted player datas of the currently playing players.
	 */
	Map<PlayerPosition, ? extends IRestrictedPlayerData> getRestrictedPlayerDatas();

	/**
	 * @return the round number of the current game.
	 */
	int getRoundNumber();

	/**
	 * @return position of the player who comes/came out in the current round.
	 */
	PlayerPosition getRoundsFirstPlayerPosition();

	/**
	 * @return the stock.
	 */
	int getStock();

	/**
	 * @param team
	 *            the team identifier.
	 * @return the team identified by the given team enum.
	 */
	List<PlayerPosition> getTeam(Team team);

	/**
	 * @return the winner team.
	 */
	Team getWinnerTeam();

	@Override
	public int hashCode();

	/**
	 * Increments the round number by 1.
	 */
	void incrementRoundNumber();

	/**
	 * @return true, if the game was resumed.
	 */
	boolean isResumed();

	/**
	 * @param charge
	 *            the game's charge data.
	 */
	void setCharge(ICharge charge);

	/**
	 * @param gameSettings
	 *            the game's settings.
	 */
	void setGameSettings(IGameSettings gameSettings);

	/**
	 * @param gamesFirstPlayerPosition
	 *            the player that was first in the game.
	 */
	void setGamesFirstPlayerPosition(PlayerPosition gamesFirstPlayerPosition);

	/**
	 * @param gameState
	 *            the state the game is in.
	 */
	void setGameState(GameState gameState);

	/**
	 * @param actionData
	 *            the action that was executed last.
	 */
	void setLastExecutedAction(IActionData actionData);

	/**
	 * @param lastRoundsPlayedCards
	 *            the cards played in the last round.
	 */
	void setLastRoundsPlayedCards(Map<PlayerPosition, ICard> lastRoundsPlayedCards);

	/**
	 * @param lastRoundsWinner
	 *            the winner of the last rounds played cards.
	 */
	void setLastRoundsWinner(PlayerPosition lastRoundsWinner);

	/**
	 * @param leadPlayerPosition
	 *            the lead player position.
	 */
	void setLeadPlayerPosition(PlayerPosition leadPlayerPosition);

	/**
	 * @param opponentTeam
	 *            the opponent team.
	 */
	void setOpponentTeam(List<PlayerPosition> opponentTeam);

	/**
	 * @param playerOnTurnPosition
	 *            the player currently on turn.
	 */
	void setPlayerOnTurnPosition(PlayerPosition playerOnTurnPosition);

	/**
	 * @param playerTeam
	 *            the player team.
	 */
	void setPlayerTeam(List<PlayerPosition> playerTeam);

	/**
	 * @param resumed
	 *            set true if the game was resumed.
	 */
	void setResumed(boolean resumed);

	/**
	 * @param roundNumber
	 *            the round number.
	 */
	void setRoundNumber(int roundNumber);

	/**
	 * @param roundsFirstPlayerPosition
	 *            the rounds first player position.
	 */
	void setRoundsFirstPlayerPosition(PlayerPosition roundsFirstPlayerPosition);

	/**
	 * @param stock
	 *            the game's stock.
	 */
	void setStock(int stock);

	/**
	 * @param winnerTeam
	 *            the winner team.
	 */
	void setWinnerTeam(Team winnerTeam);
}
