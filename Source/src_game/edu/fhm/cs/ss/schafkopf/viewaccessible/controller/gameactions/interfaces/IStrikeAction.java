package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces;

/**
 * With this action, a player can strike.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IStrikeAction extends IAction {

	/**
	 * @return true if the player is striking.
	 */
	boolean isStriking();
}
