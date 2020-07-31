package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.fhm.cs.ss.schafkopf.model.Card;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IStackHandler;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This utility class implements the methods concerning creation and access to a stack.<br>
 * <br>
 *
 * It is a SINGLETON, because the indicated cards should be only created once.
 *
 * @author Sebastian Stumpf
 *
 */
public final class StackHandler implements IStackHandler {

	/**
	 * All available cards.
	 */
	private static final List<ICard> ALL_CARDS = getAllCards();
	/**
	 * All cards indicated by color and value for quick access.
	 */
	private static final Map<CardColor, Map<CardValue, ICard>> CARDS_BY_COLOR_AND_VALUE = indicateCardsByColorAndValue();
	/**
	 * All cards indicated by their source for quick access.
	 */
	private static final Map<String, ICard> CARDS_BY_ID = indicateCardsById();
	/**
	 * Singleton instance of stack controller.
	 */
	private static final IStackHandler SINGLETON = new StackHandler();

	/**
	 * Private constructor used to create SINGLETON.
	 */
	private StackHandler() {

	}

	/**
	 * @return The stack handler instance.
	 */
	public static IStackHandler getInstance() {

		return SINGLETON;
	}

	/**
	 * Generate a list of all cards. Used for initialization of static values.
	 *
	 * @return list of all cards.
	 */
	private static List<ICard> getAllCards() {

		final List<ICard> retVal = new ArrayList<ICard>();
		for (final CardValue value : CardValue.values()) {
			for (final CardColor color : CardColor.values()) {
				retVal.add(new Card(color, value));
			}
		}
		return retVal;
	}

	/**
	 * Generate a cards map indicated by color and value. Used for initialization of static values.
	 *
	 * @return map of all cards indicated by color and value.
	 */
	private static Map<CardColor, Map<CardValue, ICard>> indicateCardsByColorAndValue() {

		final Map<CardColor, Map<CardValue, ICard>> sortedCards = new EnumMap<CardColor, Map<CardValue, ICard>>(CardColor.class);
		for (final CardColor color : CardColor.values()) {
			sortedCards.put(color, new EnumMap<CardValue, ICard>(CardValue.class));
		}
		for (final ICard card : ALL_CARDS) {
			sortedCards.get(card.getColor()).put(card.getValue(), card);
		}
		return sortedCards;
	}

	/**
	 * Generate a map of all cards, indicated by their id. Used for initialization of static values.
	 *
	 * @return map of all cards indicated by id.
	 */
	private static Map<String, ICard> indicateCardsById() {

		final Map<String, ICard> cards = new HashMap<String, ICard>();
		for (final ICard card : ALL_CARDS) {
			cards.put(card.getId(), card);
		}
		return cards;
	}

	@Override
	public ICard getCardByColorAndValue(final CardColor color, final CardValue value) {

		if (color == null || value == null) {
			throw new IllegalArgumentException();
		}
		return CARDS_BY_COLOR_AND_VALUE.get(color).get(value);
	}

	@Override
	public ICard getCardById(final String id) {

		return CARDS_BY_ID.get(id);
	}

	@Override
	public List<ICard> getPredefinedStack(final String[] firstHand, final String[] secondHand, final String[] thirdHand, final String[] fourthHand) {

		if (firstHand == null || firstHand.length != IRestrictedGameData.MAX_HAND_SIZE || secondHand == null || secondHand.length != IRestrictedGameData.MAX_HAND_SIZE || thirdHand == null
				|| thirdHand.length != IRestrictedGameData.MAX_HAND_SIZE || fourthHand == null || fourthHand.length != IRestrictedGameData.MAX_HAND_SIZE) {
			throw new IllegalArgumentException();
		}

		// check if any card is contained twice or more
		final Set<String> checkSet = new HashSet<String>();
		checkSet.addAll(Arrays.asList(firstHand));
		checkSet.addAll(Arrays.asList(secondHand));
		checkSet.addAll(Arrays.asList(thirdHand));
		checkSet.addAll(Arrays.asList(fourthHand));
		if (checkSet.size() != IStackHandler.NUMBER_OF_CARDS) {
			return null;
		}

		// rearrange cards and replace shortcuts by cards
		final List<ICard> stack = new ArrayList<ICard>();
		for (int i = 7; i > 3; --i) {
			final ICard card = getCardById(firstHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 7; i > 3; --i) {
			final ICard card = getCardById(secondHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 7; i > 3; --i) {
			final ICard card = getCardById(thirdHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 7; i > 3; --i) {
			final ICard card = getCardById(fourthHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}

		for (int i = 3; i >= 0; --i) {
			final ICard card = getCardById(firstHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 3; i >= 0; --i) {
			final ICard card = getCardById(secondHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 3; i >= 0; --i) {
			final ICard card = getCardById(thirdHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		for (int i = 3; i >= 0; --i) {
			final ICard card = getCardById(fourthHand[i]);
			if (card == null) {
				return null;
			}
			stack.add(card);
		}
		return stack;
	}

	@Override
	public List<ICard> getShuffledStack() {

		final List<ICard> retVal = new ArrayList<ICard>(ALL_CARDS);
		Collections.shuffle(retVal);
		return retVal;
	}

	@Override
	public List<ICard> getSortedCards(final GameType type, final CardColor trump) {

		final List<ICard> retVal = new ArrayList<ICard>(ALL_CARDS);
		Collections.sort(retVal, new CardComparator(type, trump));
		return retVal;
	}

}
