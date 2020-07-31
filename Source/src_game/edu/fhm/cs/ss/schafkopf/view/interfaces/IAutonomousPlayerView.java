package edu.fhm.cs.ss.schafkopf.view.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;

/**
 * An autonomous view is acting by itself in a game.<br>
 * <br>
 *
 * It chooses its actions if it is expected to act and forwards them to the game controller for execution. Chosen actions should be valid.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IAutonomousPlayerView extends IPlayerView {

	/**
	 * @return the view's AI.
	 */
	IAI getAi();

	/**
	 * @param ai
	 *            the view's AI.
	 */
	void setAi(IAI ai);

}
