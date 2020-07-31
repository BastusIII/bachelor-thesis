package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;

/**
 * This interface offers methods that can be called by the player views to interact with the controller on a current game.<br>
 * <br>
 *
 * Interactions are validated before executing them on the data.<br>
 * Further optional functionality, like replacing/removing idling players (watcher thread for example) could also be implemented here.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPrimitiveGameController extends IPrimitiveController {

	/**
	 * Execute an action on the game data. If the action is invalid, that means not {@link ActionValidationCode#VALIDATION_SUCCESS} or
	 * {@link ActionValidationCode#SUCCESS_UNCHANGED}, the action is not executed.
	 *
	 * @param action
	 *            the action.
	 * @return the action validation code.
	 */
	ActionValidationCode handleGameAction(IAction action);

	/**
	 * Subscribe the view as a player to the controller.
	 *
	 * @param playerView
	 *            the player view.
	 * @return true if the player views was successfully subscribed.
	 */
	boolean subscribePlayer(IPlayerView playerView);

}