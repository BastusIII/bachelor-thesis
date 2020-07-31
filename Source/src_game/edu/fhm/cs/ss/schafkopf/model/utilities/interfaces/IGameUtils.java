package edu.fhm.cs.ss.schafkopf.model.utilities.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * A collection of high level read only functions on a GameData Object.<br>
 * <br>
 *
 * This interface offers game information that is visible for all players or can be remembered. For example it is not possible to see other players hands, but
 * you will get the cards each player has won, because this can be remembered.<br>
 * <br>
 *
 * The offered functions combine simple access functions to get information about the data or manipulate it in a more complex way.<br>
 * If methods are called on a corrupted game data instance, or with invalid Arguments, correct functionality is not guaranteed and {@link RuntimeException} may
 * be thrown.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IGameUtils {

	/**
	 * Calculate the points of the given cards.
	 *
	 * @param cards
	 *            a collection of cards.
	 * @return the points of the given cards.
	 */
	int calculatePointsOfCards(Collection<ICard> cards);

	/**
	 * Returns true if the second card dominates the first for the current game.
	 *
	 * @param first
	 *            the first card.
	 * @param second
	 *            the second card.
	 * @return true if second > first.
	 */
	boolean dominates(ICard first, ICard second);

	/**
	 * @return the cards on the table, this is a map filled with the players' played cards.
	 */
	Map<PlayerPosition, ICard> getCardsOnTable();

	/**
	 * @return the first card played this round. Null, if no Card is played yet.
	 */
	ICard getFirstPlayedCard();

	/**
	 * @return the restricted player data.
	 */
	IRestrictedGameData getRestrictedGameData();

	/**
	 * Note: calculates the team points only as far as the teams are known in the current game progress.
	 *
	 * @param team
	 *            the team.
	 * @return the calculated team points
	 */
	int getTeamsPoints(Team team);

	/**
	 * Sums up the won cards of a team as far as it is known.
	 *
	 * @param team
	 *            the team.
	 * @return all won cards of the given team.
	 */
	ArrayList<ICard> getTeamsWonCards(Team team);

	/**
	 *
	 * @return the total multiplier, e.g. pow(2, sum of all multipliers).
	 */
	int getTotalMultiplier();

	/**
	 * @return the games trump color.
	 */
	CardColor getTrumpColor();

	/**
	 * Get the trump color for a given game.
	 *
	 * @param game
	 *            the game.
	 * @return the trump color.
	 */
	CardColor getTrumpColor(final IBasicGameData game);

	/**
	 * @return true if all players have played their card in this round.
	 */
	boolean haveAllPlayersPlayedCard();

	/**
	 * Calculates if a given card is a high trump card for the given gameType.
	 *
	 * @param gameType
	 *            the gameType.
	 * @param card
	 *            the card.
	 * @return true if the card is a trump card.
	 */
	boolean isCardHighTrump(final GameType gameType, final ICard card);

	/**
	 * Calculates if a given card is a high trump card for the given game.
	 *
	 * @param game
	 *            the game.
	 * @param card
	 *            the card.
	 * @return true if the card is a trump card.
	 */
	boolean isCardHighTrump(final IBasicGameData game, final ICard card);

	/**
	 * Calculates if a given card is a high trump card for the current game.
	 *
	 * @param card
	 *            the card.
	 * @return true if the card is a trump card.
	 */
	boolean isCardHighTrump(final ICard card);

	/**
	 * Calculates if a given card is a trump card for the given game.
	 *
	 * @param game
	 *            the game.
	 * @param card
	 *            the card.
	 * @return true if the card is a trump card.
	 */
	boolean isCardTrump(final IBasicGameData game, final ICard card);

	/**
	 * Calculates if a given card is a trump card for the current game.
	 *
	 * @param card
	 *            the card.
	 * @return true if the card is a trump card.
	 */
	boolean isCardTrump(final ICard card);

	/**
	 * Calculates if the mate is searched in the current round by checking the cards on the table.
	 *
	 * @return true if searched else false.
	 */
	boolean isMateSearchedInThisRound();
}
