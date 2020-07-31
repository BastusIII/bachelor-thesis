package edu.fhm.cs.ss.schafkopf.ai.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This interface provides methods, that give answers to game dependent questions about the next move.
 * 
 * They need a current game with gameType and if needed color set, otherwise they cannot give proper answers.<br>
 * Important: These answers must be valid on the given playerUtil's gameData.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ISpecializedAI {
	/**
	 * Calculate the best card for the next move.
	 * 
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return the best card to be played in the next move.
	 */
	ICard getBestCard(IRestrictedPlayerUtils playerUtils);

	/**
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return true if the player should strike, else false.
	 */
	boolean strike(IRestrictedPlayerUtils playerUtils);

	/**
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return true if the player should strike back, else false.
	 */
	boolean strikeBack(IRestrictedPlayerUtils playerUtils);

}
