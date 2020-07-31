package edu.fhm.cs.ss.schafkopf.view.baseclasses;

import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveGameController;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This base class is implementing the functionality, the different player views have in common.
 *
 * @author Sebastian Stumpf
 *
 */
public abstract class BasePlayerView extends BaseMultiThreadedView implements IPlayerView {

	/** Actions are submitted to the controller for execution. */
	private IPrimitiveGameController controller;
	/** The views player ID used to identify its actions at the controller. */
	private IPlayerId playerId;

	/**
	 * Instantiate the views and set its controller.
	 *
	 * @param controller
	 *            the views controller.
	 */
	protected BasePlayerView(final IPrimitiveGameController controller) {

		setGameController(controller);
	}

	@Override
	public IPrimitiveGameController getGameController() {

		return controller;
	}

	@Override
	public IPlayerId getPlayerId() {

		return this.playerId;
	}

	@Override
	public void setGameController(final IPrimitiveGameController gameController) {

		this.controller = gameController;
		// subscribing Player to controller
		if (gameController != null) {
			gameController.subscribePlayer(this);
		}
	}

	@Override
	public void setPlayerId(final IPlayerId playerId) {

		this.playerId = playerId;
	}
}