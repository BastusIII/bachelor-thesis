package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import java.util.UUID;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * The player ID is used by the controller to identify a player view.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPlayerId {
	/**
	 * @return the players position.
	 */
	PlayerPosition getPosition();

	/**
	 * @return the players {@link UUID}.
	 */
	UUID getUuid();

}