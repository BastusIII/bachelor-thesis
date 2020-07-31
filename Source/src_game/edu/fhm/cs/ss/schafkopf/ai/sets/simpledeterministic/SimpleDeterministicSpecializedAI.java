package edu.fhm.cs.ss.schafkopf.ai.sets.simpledeterministic;

import java.util.Collection;
import java.util.Comparator;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.model.utilities.CardComparator;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This specialized AI gives deterministic valid answers to specialized questions. The answers are simple and without higher intelligence.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class SimpleDeterministicSpecializedAI implements ISpecializedAI {

	@Override
	public ICard getBestCard(final IRestrictedPlayerUtils playerUtils) {

		final Collection<ICard> cards = playerUtils.getAvailableCards();
		final Comparator<ICard> comp = new CardComparator(playerUtils.getRestrictedGameData().getGameType(), playerUtils.getTrumpColor());
		ICard highest = null;
		for (final ICard card : cards) {
			if (comp.compare(highest, card) > 0) {
				highest = card;
			}
		}
		return highest;
	}

	@Override
	public boolean strike(final IRestrictedPlayerUtils playerUtils) {

		// Number of Ober
		int counter = 0;
		for (final ICard card : playerUtils.getPovPlayerData().getInitialHand()) {
			if (card.getValue().equals(CardValue.OBER)) {
				counter++;
			}
		}
		if (counter > 2) {
			return true;
		}
		return false;
	}

	@Override
	public boolean strikeBack(final IRestrictedPlayerUtils playerUtils) {

		// Number of Ober
		int counter = 0;
		for (final ICard card : playerUtils.getPovPlayerData().getInitialHand()) {
			if (card.getValue().equals(CardValue.OBER)) {
				counter++;
			}
		}
		if (counter > 2) {
			return true;
		}
		return false;
	}

}
