package edu.fhm.cs.ss.schafkopf.ai.sets.random;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAIFactory;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;

/**
 * A Factory returning only Instances of {@link RandomSpecializedAI}.
 */
public class RandomSpecializedAIFactory implements ISpecializedAIFactory {

	@Override
	public ISpecializedAI getAI(final GameType gameType) {

		if (gameType == null) {
			return null;
		}
		return new RandomSpecializedAI();
	}

}
