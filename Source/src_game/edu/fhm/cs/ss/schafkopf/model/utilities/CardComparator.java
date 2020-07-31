package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.util.Comparator;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * A Comparator that compares two cards dependent of the game type and trump color.
 *
 * @author Sebastian Stumpf
 */
public class CardComparator implements Comparator<ICard> {
	/** Priority of color. */
	public static final int MULTI_COLOR = 10;
	/** Priority of Ober values. */
	public static final int MULTI_OBER = 10000;
	/** Priority of trump. */
	public static final int MULTI_TRUMP = 100;
	/** Priority of Unter values. */
	public static final int MULTI_UNTER = 1000;

	/**
	 * The game type to compare with.
	 */
	private GameType gameType;
	/**
	 * The trump color to compare with.
	 */
	private CardColor trump;

	/**
	 * Default Constructor initializes type and color with null.
	 */
	public CardComparator() {

		this.gameType = null;
		this.trump = null;
	}

	/**
	 * Instantiate a card comparator with the given parameters.
	 *
	 * @param type
	 *            the game type used for comparison.
	 * @param trump
	 *            the trump color used for comparison.
	 */
	public CardComparator(final GameType type, final CardColor trump) {

		this.gameType = type;
		this.trump = trump;
	}

	/**
	 * Compares two cards dependent of the card comparators game type and trump color.
	 *
	 * @param first
	 *            the first card.
	 * @param second
	 *            the second card.
	 * @return negative value if the first card is higher than the second, 0 if equal, 1 if the second is higher.
	 */
	@Override
	public int compare(final ICard first, final ICard second) {

		if (first == null) {
			if (second == null) {
				return 0;
			} else {
				return 1;
			}
		}
		if (second == null) {
			return -1;
		}
		if (first.equals(second)) {
			return 0;
		}

		final int firstColorMulti;
		final int firstValueMulti;
		final int secondColorMulti;
		final int secondValueMulti;

		if (gameType == null) {
			firstColorMulti = MULTI_COLOR;
			firstValueMulti = first.getValue().equals(CardValue.OBER) ? MULTI_OBER : first.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
			secondColorMulti = MULTI_COLOR;
			secondValueMulti = second.getValue().equals(CardValue.OBER) ? MULTI_OBER : second.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
		} else {
			switch (gameType) {
				case PASS:
					firstColorMulti = MULTI_COLOR;
					firstValueMulti = 1;
					secondColorMulti = MULTI_COLOR;
					secondValueMulti = 1;
					break;
				case SI:
					firstColorMulti = MULTI_COLOR;
					firstValueMulti = first.getValue().equals(CardValue.OBER) ? MULTI_OBER : first.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					secondColorMulti = MULTI_COLOR;
					secondValueMulti = second.getValue().equals(CardValue.OBER) ? MULTI_OBER : second.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					break;
				case WENZ:
				case WENZ_TOUT:
					firstColorMulti = MULTI_COLOR;
					firstValueMulti = first.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					secondColorMulti = MULTI_COLOR;
					secondValueMulti = second.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					break;
				case FARBWENZ:
				case FARBWENZ_TOUT:
					firstColorMulti = !first.getValue().equals(CardValue.UNTER) && first.getColor().equals(trump) ? MULTI_TRUMP : MULTI_COLOR;
					firstValueMulti = first.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					secondColorMulti = !second.getValue().equals(CardValue.UNTER) && second.getColor().equals(trump) ? MULTI_TRUMP : MULTI_COLOR;
					secondValueMulti = second.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					break;
				default:
					firstColorMulti = !first.getValue().equals(CardValue.OBER) && !first.getValue().equals(CardValue.UNTER) && first.getColor().equals(trump) ? MULTI_TRUMP : MULTI_COLOR;
					firstValueMulti = first.getValue().equals(CardValue.OBER) ? MULTI_OBER : first.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					secondColorMulti = !second.getValue().equals(CardValue.OBER) && !second.getValue().equals(CardValue.UNTER) && second.getColor().equals(trump) ? MULTI_TRUMP : MULTI_COLOR;
					secondValueMulti = second.getValue().equals(CardValue.OBER) ? MULTI_OBER : second.getValue().equals(CardValue.UNTER) ? MULTI_UNTER : 1;
					break;

			}
		}
		final int cmpValueFirst = firstColorMulti * first.getColor().number + firstValueMulti * first.getValue().number;
		final int cmpValueSecond = secondColorMulti * second.getColor().number + secondValueMulti * second.getValue().number;
		return cmpValueSecond - cmpValueFirst;
	}

	/**
	 * @return the comparator's game type.
	 */
	public GameType getGameType() {

		return gameType;
	}

	/**
	 * @return the comparator's trump color.
	 */
	public CardColor getTrump() {

		return trump;
	}

	/**
	 * @param gameType
	 *            the comparator's game type.
	 */
	public void setGameType(final GameType gameType) {

		this.gameType = gameType;
	}

	/**
	 * @param trump
	 *            the comparator's trump color.
	 */
	public void setTrump(final CardColor trump) {

		this.trump = trump;
	}

}
