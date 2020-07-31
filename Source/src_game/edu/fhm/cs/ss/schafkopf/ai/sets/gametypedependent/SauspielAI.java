package edu.fhm.cs.ss.schafkopf.ai.sets.gametypedependent;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * A specialized AI for Sauspiel.
 * 
 * This Class is part of a set to demonstrate the structure of an AI-Set, that contains specialized AI's for every GameType.
 * 
 * @deprecated NOT IMPLEMENTED.
 * 
 * @author Sebastian Stumpf
 * 
 */
@Deprecated
public class SauspielAI implements ISpecializedAI {

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
