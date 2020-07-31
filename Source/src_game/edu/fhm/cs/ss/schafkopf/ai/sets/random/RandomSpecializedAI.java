package edu.fhm.cs.ss.schafkopf.ai.sets.random;

import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This specialized AI gives random, but valid answers to specialized questions.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class RandomSpecializedAI implements ISpecializedAI {

	@Override
	public ICard getBestCard(final IRestrictedPlayerUtils playerUtils) {

		final Collection<ICard> allowedCards = playerUtils.getAvailableCards();
		ICard chosenCard = null;
		if (allowedCards != null && !allowedCards.isEmpty()) {
			// TODO: ERROR on purpose for testing -> causes dead lock, very
			// unlikely chosen
			// if (Math.random() < 0.00001) {
			// return new Card(CardColor.EICHEL, CardValue.SAU);
			// }
			final int randomIndex = (int) (Math.random() * allowedCards.size());
			int counter = 0;
			for (final ICard card : allowedCards) {
				if (counter++ == randomIndex) {
					chosenCard = card;
					break;
				}
			}
		}
		return chosenCard;
	}

	@Override
	public boolean strike(final IRestrictedPlayerUtils playerUtils) {

		if (playerUtils.isAllowedToStrike()) {
			return (int) (Math.random() * 2) == 0;
		}
		return false;
	}

	@Override
	public boolean strikeBack(final IRestrictedPlayerUtils playerUtils) {

		if (playerUtils.isAllowedToStrikeBack()) {
			return (int) (Math.random() * 2) == 0;
		}
		return false;
	}
}
