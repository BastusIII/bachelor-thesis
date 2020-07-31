package edu.fhm.cs.ss.schafkopf.view.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveGameController;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.utilities.interfaces.IRestrictedPlayerUtils;

/**
 * A player view is a view that can subscribe at the game controller and is able to forward actions to it.<br>
 * <br>
 *
 * Player views must offer a default constructor, this is especially important for the test environment to be able to create them. Subscribing to a connected
 * {@link IPrimitiveGameController} can be handled in the constructor or in the method that sets this game controller.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPlayerView extends IView {
	/**
	 * @return the game controller the view is interacting with.
	 */
	IPrimitiveGameController getGameController();

	/**
	 * @return the views player id is important for the controller to know that this view is actually subscribed.
	 */
	IPlayerId getPlayerId();

	/**
	 * @param controller
	 *            the game controller the view is interacting with. Subscribing to the controller can be handled here.
	 */
	void setGameController(IPrimitiveGameController controller);

	/**
	 * @param playerId
	 *            the views player ID.
	 */
	void setPlayerId(IPlayerId playerId);

	/**
	 * Called by game controller, hands over the current game data object. The player should display the game data and - if expected to - forward a proper
	 * action back to the controller.
	 *
	 * @param restrictedPlayerutils
	 *            these utilities contain the current game data, restricted to the point-of-view of this view.
	 */
	void updateGameData(IRestrictedPlayerUtils restrictedPlayerutils);
}
