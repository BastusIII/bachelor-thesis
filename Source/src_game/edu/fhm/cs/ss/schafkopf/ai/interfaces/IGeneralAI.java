package edu.fhm.cs.ss.schafkopf.ai.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * This interface provides methods, that give answers to general questions about the next move and don't need a running game to be answered.
 * 
 * Important: These answers must be valid on the given playerUtil's gameData.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IGeneralAI {

	/**
	 * 
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return true, if the player should accept the next game's start, else false.
	 */
	boolean acceptRestart(IRestrictedPlayerUtils playerUtils);

	/**
	 * Returns the game that fits best to the players current hand and is allowed to play.
	 * 
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return the best game to play.
	 */
	IBasicGameData getBestGame(IRestrictedPlayerUtils playerUtils);

	/**
	 * Answers the question, if the player should raise.
	 * 
	 * @param playerUtils
	 *            the playerUtils, the calculations are based on.
	 * 
	 * @return true if he should, else false.
	 */
	boolean raise(IRestrictedPlayerUtils playerUtils);
}
