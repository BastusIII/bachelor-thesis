package edu.fhm.cs.ss.schafkopf.ai.sets.gametypedependent;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAIFactory;
import edu.fhm.cs.ss.schafkopf.ai.sets.random.RandomSpecializedAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;

/**
 * A factory, that returns specialized AI Instances from this set for the given gameType.
 * 
 * This Class is part of a set to demonstrate the structure of an AI-Set, that contains specialized AI's for every GameType.
 * 
 * @deprecated NOT IMPLEMENTED.
 * 
 * @author Sebastian Stumpf
 * 
 */
@Deprecated
public class GameTypeDependantSpecializedAIFactory implements ISpecializedAIFactory {

	@Override
	public ISpecializedAI getAI(final GameType gameType) {

		if (gameType == null) {
			return null;
		}

		switch (gameType) {
			case FARBWENZ:
				return new FarbwenzAI(false);
			case FARBWENZ_TOUT:
				return new FarbwenzAI(true);
			case PASS:
				return null;
			case SAUSPIEL:
				return new SauspielAI();
			case SI:
				return new RandomSpecializedAI();
			case SOLO:
				return new SoloAI(false);
			case SOLO_TOUT:
				return new SoloAI(true);
			case WENZ:
				return new WenzAI(false);
			case WENZ_TOUT:
				return new WenzAI(true);
			default:
				return null;
		}
	}

}
