package edu.fhm.cs.ss.schafkopf.ai.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;

/**
 * This factory provides one method to create them all. The specialized AI Instances based on the given gameType.
 * 
 * So you can implement one AI for each gameType, that does not have to contain the logic for any other gameType.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ISpecializedAIFactory {

	/**
	 * Get a specialized AI that fits to to the given gamType.
	 * 
	 * @param gameType
	 *            the gameType the AI is created for.
	 * 
	 * @return the AI or null if the AI could not be initialized, for example if the given gameType was null. That should not happen.
	 */
	ISpecializedAI getAI(final GameType gameType);

}
