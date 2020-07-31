package edu.fhm.cs.ss.schafkopf.model.utilities.interfaces;

import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * Extends {@link IPrimitivePlayerUtils} by high level read methods, that are player data dependent, but can be called with any player data data as a parameter.<br>
 * <br>
 *
 * The dependency on a single {@link IPlayerData}-reference is abolished and all methods can be called with any player data data. The offered methods should not
 * be available for the player views.
 *
 * The point-of-view player data used in {@link IPrimitivePlayerUtils} can be set here.
 *
 * @author Sebastian Stumpf
 */
public interface IPlayerUtils extends IRestrictedPlayerUtils {

	/**
	 * Calculate high cards in row for a given game data for one or more playerDatas (a team).
	 *
	 * @param playerDatas
	 *            the players data.
	 * @param gameData
	 *            the game data.
	 * @return the players common high cards in row.
	 */
	int calculateHighCardsInRow(IBasicGameData gameData, Collection<IPlayerData> playerDatas);

	/**
	 * @see #calculateHighCardsInRow(IBasicGameData, Collection)
	 * @param gameData
	 *            the game data.
	 * @param playerDatas
	 *            the players datas.
	 * @return the high cards in row.
	 */
	int calculateHighCardsInRow(IBasicGameData gameData, IPlayerData... playerDatas);

	/**
	 * Counts the cards of a given color in a players hand for the given game, excluding the high trump cards.
	 *
	 * @param playerData
	 *            the player data.
	 * @param color
	 *            the color that should be counted.
	 * @return the number of cards equal to the given color.
	 */
	int countColor(final IPlayerData playerData, final CardColor color);

	/**
	 * Counts the cards of a given color in a players hand for the given gameType, excluding the high trump cards.
	 *
	 * @param playerData
	 *            the player data.
	 * @param gameType
	 *            the gameType.
	 * @param color
	 *            the color that should be counted.
	 * @return the number of cards equal to the given color.
	 */
	int countColor(final IPlayerData playerData, final GameType gameType, final CardColor color);

	/**
	 * Counts the cards of a given color in a players hand for the given game, excluding the high trump cards.
	 *
	 * @param playerData
	 *            the player data.
	 * @param game
	 *            the game.
	 * @param color
	 *            the color that should be counted.
	 * @return the number of cards equal to the given color.
	 */
	int countColor(final IPlayerData playerData, final IBasicGameData game, final CardColor color);

	/**
	 * Counts all trump cards in a players hand for a given game, including the high trump cards.
	 *
	 * @param playerData
	 *            the player data.
	 * @param game
	 *            the game.
	 * @return the number of trump Cards in the players hand.
	 */
	int countTrump(final IPlayerData playerData, final IBasicGameData game);

	/**
	 * Get all cards of a given color in a players hand for the current game, excluding the high trump cards..
	 *
	 * @param playerData
	 *            the player data.
	 * @param color
	 *            the card color.
	 * @return a collection containing all cards of the given color.
	 */
	Collection<ICard> getAllColorCards(final IPlayerData playerData, final CardColor color);

	/**
	 * Get all cards of a given color in a players hand for the given game, excluding the high trump cards.
	 *
	 * @param playerData
	 *            the player data.
	 * @param game
	 *            the game.
	 * @param color
	 *            the color.
	 * @return a collection containing all cards of the given color.
	 */
	Collection<ICard> getAllColorCards(final IPlayerData playerData, final IBasicGameData game, final CardColor color);

	/**
	 * Get all trump cards in a players hand for the current game.
	 *
	 * @param playerData
	 *            the player data.
	 * @return a collection containing all trump cards.
	 */
	Collection<ICard> getAllTrumpCards(final IPlayerData playerData);

	/**
	 * Get all trump cards in a players hand for the given game.
	 *
	 * @param playerData
	 *            the player data.
	 * @param game
	 *            the game.
	 * @return a collection containing all trump cards.
	 */
	Collection<ICard> getAllTrumpCards(final IPlayerData playerData, final IBasicGameData game);

	/**
	 * Calculate the cards, a given playerData is allowed to play dependent of the current game.
	 *
	 * @param playerData
	 *            the player data.
	 *
	 * @return a collection containing the available cards.
	 */
	Collection<ICard> getAvailableCards(final IPlayerData playerData);

	/**
	 * Calculate the available colors that can be chosen for the given game type.<br>
	 * Note: If GameType is SAUSPIEL, the returned colors are the mates that can be chosen and not the trump colors.
	 *
	 * @param playerData
	 *            the player data.
	 * @param gameType
	 *            the game type.
	 * @return a collection of available colors.
	 */
	Collection<CardColor> getAvailableColors(final IPlayerData playerData, final GameType gameType);

	/**
	 * Calculate the available gameTypes for a player data dependent of the current game type.
	 *
	 * @param playerData
	 *            the player data.
	 * @return a collection of available game types.
	 */
	Collection<GameType> getAvailableGameTypes(final IPlayerData playerData);

	/**
	 * Return a validation code that describes if - or why not - the chosen game is allowed to play.
	 *
	 * @param playerData
	 *            the player data.
	 * @param chosenGame
	 *            the chosen game.
	 * @return the validation code.
	 */
	ActionValidationCode getChooseGameValidationCode(IPlayerData playerData, IBasicGameData chosenGame);

	/**
	 * Return a validation code that describes if - or why not - the player is allowed to get cards.
	 *
	 * @param playerData
	 *            the player data.
	 *
	 * @return the validation code.
	 */
	ActionValidationCode getGetCardsValidationCode(IPlayerData playerData);

	/**
	 * Return a validation code that describes if - or why not - card is allowed to play.
	 *
	 * @param playerData
	 *            the player data.
	 * @param card
	 *            card to play.
	 * @return the validation code.
	 */
	ActionValidationCode getPlayCardValidationCode(IPlayerData playerData, ICard card);

	/**
	 * Return a validation code that describes if - or why not - the player is allowed to raise.
	 *
	 * @param playerData
	 *            the player data.
	 * @return the validation code.
	 */
	ActionValidationCode getRaiseValidationCode(IPlayerData playerData);

	/**
	 * Return a validation code that describes if - or why not - the player is allowed to strike.
	 *
	 * @param playerData
	 *            the player data.
	 * @return the validation code.
	 */
	ActionValidationCode getStrikeBackValidationCode(IPlayerData playerData);

	/**
	 * Return a validation code that describes if - or why not - the player is allowed to strike back.
	 *
	 * @param playerData
	 *            the player data.
	 * @return the validation code.
	 */
	ActionValidationCode getStrikeValidationCode(IPlayerData playerData);

	/**
	 * Check if a player data has a SI.
	 *
	 * @param playerData
	 *            the player data.
	 * @return true if the player data has a SI (All OBER and all UNTER).
	 */
	boolean hasSi(final IPlayerData playerData);

	/**
	 * Calculate if a player data is allowed to run away in the current game, which means the player data is the mate and has more than 3 cards of the mate
	 * color. The return value of this method is only valid, if the player data is the mate playerData, in a SAUSPIEL.
	 *
	 * @return true if the player data is allowed to run away, false if not or the game is not a SAUSPIEL.
	 */
	boolean isAllowedToRunAway(final IPlayerData playerData);

	/**
	 * Calculates if a game type - color combination is available for a player data.
	 *
	 * @param playerData
	 *            the player data.
	 * @param gameType
	 *            the gameType.
	 * @param color
	 *            the color.
	 * @return true if available.
	 */
	boolean isColorAllowedToChoose(IPlayerData playerData, GameType gameType, CardColor color);

	/**
	 * @return if the player is expected to act.
	 */
	boolean isExpectedToAct(IPlayerData playerData);

	/**
	 * Calculates if a game type is available for a player data dependent of the gameDatas current type.
	 *
	 * @param playerData
	 *            the player data
	 * @param newType
	 *            the game type the player data wants to play.
	 * @return if new type is allowed to play.
	 */
	boolean isGameTypeAllowedToChoose(final IPlayerData playerData, final GameType newType);

	/**
	 * @param restrictedPlayerData
	 *            the player data.
	 * @return true if the player's hand is complete.
	 *
	 */
	boolean isHandComplete(IPlayerData restrictedPlayerData);

	/**
	 * Calculate if a player data is the mate playerData of the current game.
	 *
	 * @param playerData
	 *            the player data.
	 * @return true if mate.
	 */
	boolean isLeadPlayerMate(final IPlayerData playerData);

	/**
	 * @param restrictedPlayerData
	 *            the player data.
	 * @return if the player is on turn.
	 */
	boolean isOnTurn(IPlayerData restrictedPlayerData);

	/**
	 * Checks if a player data has a given card.
	 *
	 * @param playerData
	 *            the player data.
	 * @param color
	 *            the card's color.
	 * @param value
	 *            the card's value.
	 * @return true if the players Hand contains the card.
	 */
	boolean ownsCard(final IPlayerData playerData, final CardColor color, final CardValue value);

	/**
	 * @param povPlayerData
	 *            the point-of-view player data to set.
	 */
	void setPovPlayerData(IPlayerData povPlayerData);
}
