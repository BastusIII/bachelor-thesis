package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * Types of an action.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum ActionType {

	/** Get cards action. */
	GET,
	/** Play card action. */
	PLAY,
	/** Raise action. */
	RAISE,
	/** Strike action. */
	STRIKE,
	/** Choose game action. */
	CHOOSE,
	/** Strike back action. */
	STRIKEBACK,
	/** Accept next game action. */
	NEXTGAMEACCEPTED,
	/** Next game started. Not an action more a hint on the final execution of a accept next game action. */
	NEXTGAMESTARTED;
}
