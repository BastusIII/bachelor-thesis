package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces;

/**
 * With this action, a player can strike back.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IStrikeBackAction extends IAction {

	/**
	 * @return true if the player is striking.
	 */
	boolean isStrikingBack();
}
