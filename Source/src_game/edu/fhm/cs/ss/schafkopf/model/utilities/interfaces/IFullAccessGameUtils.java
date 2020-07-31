package edu.fhm.cs.ss.schafkopf.model.utilities.interfaces;

import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * The interfaces extends {@link IPlayerUtils} by a collection of high level functions to manipulate a game data object. Also contains some read functionality,
 * that is not available for player views.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IFullAccessGameUtils extends IPlayerUtils {

	/**
	 * Calculates the basic charge dependent of the current game type.
	 *
	 * @return the basic charge.
	 */
	int calculateBasicChargeForCurrentGame();

	/**
	 * Calculate the winner of the cards of a given stich.
	 *
	 * @param stich
	 *            the stich to calculate the winner for.
	 * @return the winner position.
	 */
	PlayerPosition calculateWinnerCardPosition(Map<PlayerPosition, ICard> stich);

	/**
	 * Reset all cards played this round.
	 */
	void clearPlayedCards();

	/**
	 * Draws a given number of cards from the stack and adds them to the given player data. If there are less cards available than should be drawn, all
	 * remaining cards are drawn.
	 *
	 * @param playerData
	 *            the player data to draw cards for.
	 * @param number
	 *            the number that should be drawn from the stack.
	 */
	void dealCardsFromStack(final IPlayerData playerData, int number);

	/**
	 * A complex combination of write functions that is needed to set all the required data, if the game is finished.
	 *
	 * Charge information, game state, and other values will be changed.
	 */
	void endGameManipulations();

	/**
	 * Fills the opponent team with all players not contained in the player team. Thus it should only be called if the player team is properly filled.
	 */
	void fillOpponentTeam();

	/**
	 * @return the full access game data instance.
	 */
	IGameData getGameData();

	/**
	 * Check if all player have chosen a game.
	 *
	 * @return true if all players chose a game.
	 */
	boolean haveAllPlayersChosen();

	/**
	 * Initialize the gameData (with charge and all players data) and set the games first player to the given position. The stack is also filled with a new set
	 * of a shuffled card deck. The stack used for initialization is randomly shuffled.
	 *
	 * @param firstPlayerPosition
	 *            the games first player.
	 */
	void initializeGameData(final PlayerPosition firstPlayerPosition);

	/**
	 * Initializes the game data with a given stack.
	 *
	 * @see #initializeGameData(PlayerPosition)
	 *
	 * @param firstPlayerPosition
	 *            the games first player.
	 * @param stack
	 *            the stack.
	 */
	void initializeGameData(PlayerPosition firstPlayerPosition, List<ICard> stack);

	/**
	 * Initialize the players data for a new game.
	 *
	 * @param playerData
	 *            the player to initialize.
	 */
	void initializePlayerData(final IPlayerData playerData);

	/**
	 * A complex combination of write functions that is needed to set all the required data, if the initial round is over an the game starts.
	 *
	 * Game state, round number, teams and other values will be changed according to the set lead player and his chosen game.
	 */
	void startGameManipulations();
}
