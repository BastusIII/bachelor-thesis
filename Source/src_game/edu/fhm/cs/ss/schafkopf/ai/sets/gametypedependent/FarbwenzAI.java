package edu.fhm.cs.ss.schafkopf.ai.sets.gametypedependent;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * A specialized AI for Farbwenz.
 *
 * This Class is part of a set to demonstrate the structure of an AI-Set, that contains specialized AI's for every GameType.
 *
 * @deprecated NOT IMPLEMENTED.
 *
 * @author Sebastian Stumpf
 *
 */
@Deprecated
public class FarbwenzAI implements ISpecializedAI {
	/**
	 * True if the game is a tout.
	 */
	private final boolean tout;

	/**
	 * Instantiate the AI with the given parameters.
	 *
	 * @param tout
	 *            true is the game is a tout.
	 */
	public FarbwenzAI(final boolean tout) {

		this.tout = tout;
	}

	@Override
	public ICard getBestCard(final IRestrictedPlayerUtils playerUtils) {

		return null;
	}

	@Override
	public boolean strike(final IRestrictedPlayerUtils playerUtils) {

		return false;
	}

	@Override
	public boolean strikeBack(final IRestrictedPlayerUtils playerUtils) {

		return false;
	}

}
