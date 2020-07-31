package edu.fhm.cs.ss.schafkopf.ai.sets.simpledeterministic;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAIFactory;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;

/**
 * A Factory returning only Instances of {@link SimpleDeterministicSpecializedAI}.
 */
public class SimpleDeterministicSpecializedAIFactory implements ISpecializedAIFactory {

	@Override
	public ISpecializedAI getAI(final GameType gameType) {

		return new SimpleDeterministicSpecializedAI();
	}

}
