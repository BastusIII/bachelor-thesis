package edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces;

import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IGameUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * Extends {@link IGameUtils} by high level read methods, that depend on a player data.
 *
 * For example, the information if a card may be played depends on the player datas current hand.<br>
 * This player data has to be defined for this utilities implementation. only the information depending on the defined player data will be returned.
 *
 * All methods are available for player views, but the few can only get the offered information for the player data instance, it is connected to. It must be
 * ensured, that no other player views get access to their opponents information.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IRestrictedPlayerUtils extends IGameUtils {

	/**
	 * Calculates the number of high cards in row in the current game.
	 *
	 * @return the number if high cards in row.
	 */
	int calculateHighCardsInRow();

	/**
	 * Calculates the number of high cards in row for a given game.
	 *
	 * @param gameData
	 *            the game.
	 * @return the number if high cards in row.
	 */
	int calculateHighCardsInRow(IBasicGameData gameData);

	/**
	 * Counts the cards of a given color in the player's hand for the current game, excluding the high trump cards. High trump cards are excluded.
	 *
	 * @param color
	 *            the color to count.
	 * @return the number of cards equal to the given color.
	 */
	int countColor(final CardColor color);

	/**
	 * Counts the color for a given game.
	 *
	 * @see #countColor(CardColor)
	 *
	 * @param game
	 *            the game.
	 * @param color
	 *            the color to count.
	 * @return the number of cards equal to the given color.
	 */
	int countColor(final IBasicGameData game, final CardColor color);

	/**
	 * Counts all trump cards in the player's hand for the current game, including the high trump cards.
	 *
	 * @return the number of trump Cards in the player's hand.
	 */
	int countTrump();

	/**
	 * Counts trump for a given game.
	 *
	 * @see #countTrump()
	 *
	 * @param game
	 *            the game.
	 * @return the number of trump Cards in the player's hand.
	 */
	int countTrump(final IBasicGameData game);

	/**
	 * Get all cards of a given color in the player's hand for the current game, excluding the high trump cards.
	 *
	 * @param color
	 *            the color.
	 * @return a collection containing all cards of the given color.
	 */
	Collection<ICard> getAllColorCards(final CardColor color);

	/**
	 * Get all cards of a given color for a given game.
	 *
	 * @see #getAllColorCards(CardColor)
	 *
	 * @param game
	 *            the game.
	 * @param color
	 *            the color.
	 * @return a collection containing all cards of the given color.
	 */
	Collection<ICard> getAllColorCards(final IBasicGameData game, final CardColor color);

	/**
	 * Get all cards of a given color in the player's hand for the current game, excluding the high trump cards.
	 *
	 * @return a collection containing all cards of the given color.
	 */
	Collection<ICard> getAllTrumpCards();

	/**
	 * Get all trump cards for the given game.
	 *
	 * @see #getAllColorCards(CardColor)
	 *
	 * @param game
	 *            the game.
	 * @return a collection containing all trump cards.
	 */
	Collection<ICard> getAllTrumpCards(final IBasicGameData game);

	/**
	 * Calculate the cards, the player is allowed to play dependent of the current game.
	 *
	 * @return a collection containing the available cards.
	 */
	Collection<ICard> getAvailableCards();

	/**
	 * Calculate the available trump / mate colors for a given game type of the player.<br>
	 * Note: If GameType is SAUSPIEL, the returned colors are the mates that can be chosen and not the trump colors.
	 *
	 * @param gameType
	 *            the game type.
	 * @return a collection of available colors.
	 */
	Collection<CardColor> getAvailableColors(final GameType gameType);

	/**
	 * Calculate the available gameTypes for the player dependent of the current game type.
	 *
	 * @return a collection of available game types.
	 */
	Collection<GameType> getAvailableGameTypes();

	/**
	 * @return the point-of-view player data this utilities offer information about.
	 */
	IPlayerData getPovPlayerData();

	/**
	 * Check if the player has a SI.
	 *
	 * @return true if the player has a SI (All OBER and all UNTER).
	 */
	boolean hasSi();

	/**
	 * @param gameType
	 *            the game type.
	 * @param cardColor
	 *            the card color.
	 * @return true if the player is allowed to choose the game defined by the parameters.
	 */
	boolean isAllowedToChooseGame(GameType gameType, CardColor cardColor);

	/**
	 * @param chosenGame
	 *            the chosen game.
	 * @return true if the player is allowed to choose the game defined by the parameters.
	 */
	boolean isAllowedToChooseGame(IBasicGameData chosenGame);

	/**
	 * @return true if the player is allowed get cards.
	 */
	boolean isAllowedToGetCards();

	/**
	 * @param card
	 *            the card to play.
	 * @return true if the player is allowed to play the given card.
	 */
	boolean isAllowedToPlayCard(ICard card);

	/**
	 * @return true if the player is allowed to raise.
	 */
	boolean isAllowedToRaise();

	/**
	 * Calculate if the player is allowed to run away in the current game, which means the player is the mate and has more than 3 cards of the mate color. The
	 * return value of this method is only valid, if the player is the mate player, in a SAUSPIEL.
	 *
	 * @return true if the player is allowed to run away, false if not or the game is not a SAUSPIEL.
	 */
	boolean isAllowedToRunAway();

	/**
	 * @return true if the player is allowed to strike.
	 */
	boolean isAllowedToStrike();

	/**
	 * @return true if the player is allowed to strike back.
	 */
	boolean isAllowedToStrikeBack();

	/**
	 * Calculates if a game type-color combination is available for the player.
	 *
	 * @param gameType
	 *            the game type.
	 * @param color
	 *            the color.
	 * @return true if available.
	 */
	boolean isColorAllowedToChoose(GameType gameType, CardColor color);

	/**
	 * If a player is expected to act, but does not make an action, the game will stagnate.
	 *
	 * @return true if the player is expected to make an action.
	 */
	boolean isExpectedToAct();

	/**
	 * Calculates if a game type is available for the player dependent of the gameDatas current type.
	 *
	 * @param newType
	 *            the game type the player wants to play.
	 * @return if new type is allowed to play.
	 */
	boolean isGameTypeAllowedToChoose(final GameType newType);

	/**
	 * @return true if the player's hand is complete.
	 */
	boolean isHandComplete();

	/**
	 * Calculate if the player is the mate player of the current game.
	 *
	 * @return true if mate.
	 */
	boolean isLeadPlayerMate();

	/**
	 * @return true if the player is on turn.
	 */
	boolean isOnTurn();

	/**
	 * Checks if the the player has a given card in his hand.
	 *
	 * @param color
	 *            the card's color.
	 * @param value
	 *            the card's value.
	 * @return true if the player's hand contains the card.
	 */
	boolean ownsCard(final CardColor color, final CardValue value);
}
