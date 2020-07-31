package edu.fhm.cs.ss.schafkopf.model.utilities.interfaces;

import java.util.List;

import edu.fhm.cs.ss.schafkopf.model.utilities.CardComparator;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This interface offers methods to get a random or predefined stack and fast access to cards by various indices.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IStackHandler {

	/**
	 * The number of cards in a stack.
	 */
	static final int NUMBER_OF_CARDS = 32;

	/**
	 * Returns the card identified by the given color and value.
	 *
	 * @param color
	 *            the cards color.
	 * @param value
	 *            the cards value.
	 * @return the card.
	 */
	ICard getCardByColorAndValue(CardColor color, CardValue value);

	/**
	 * Returns a card identified by its source name.
	 *
	 * @param source
	 *            the source.
	 * @return the card.
	 */
	ICard getCardById(String source);

	/**
	 * Get a stack so every player gets the card defined in the parameters.
	 *
	 * @param firstHand
	 *            the first players hand.
	 * @param secondhand
	 *            the second players hand.
	 * @param thirdHand
	 *            the third players hand.
	 * @param fourthHand
	 *            the fourth players hand.
	 * @return the stack.
	 */
	List<ICard> getPredefinedStack(String[] firstHand, String[] secondhand, String[] thirdHand, String[] fourthHand);

	/**
	 * @return a complete, shuffled stack.
	 */
	List<ICard> getShuffledStack();

	/**
	 * Returns a full stack sorted by a given game type and color with the {@link CardComparator}.
	 *
	 * @param type
	 *            the game type.
	 * @param trump
	 *            the trump color.
	 * @return the sorted cards. Highest card first.
	 */
	List<ICard> getSortedCards(GameType type, CardColor trump);

}