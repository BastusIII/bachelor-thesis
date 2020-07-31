package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * The base action implements basic functionality, all actions have in common.<br>
 * <br>
 * 
 * In this case, this is only the handling of the player ID.
 * 
 * @author Sebastian Stumpf
 * 
 */
public abstract class BaseAction implements IAction {
	/**
	 * The player ID of the player that created this action.
	 */
	private final IPlayerId playerId;

	/**
	 * Instantiate an action with the given player ID.
	 * 
	 * @param playerId
	 *            the player ID.
	 */
	public BaseAction(final IPlayerId playerId) {

		this.playerId = playerId;
	}

	@Override
	public IPlayerId getPlayerId() {

		return playerId;
	}

	@Override
	public PlayerPosition getPosition() {

		return playerId.getPosition();
	}
}
