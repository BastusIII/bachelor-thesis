package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * The action validation defines, gives more information about how an action was validated or executed.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum ActionValidationCode {
	/** Card was not allowed to play. */
	CARD_NOTALLOWED,
	/** The color of the chosen game was not allowed. */
	CHOOSE_COLORNOTALLOWED,
	/** The type of the chosen game was not allowed. */
	CHOOSE_TYPENOTALLOWED,
	/** Player did not get cards, because his hand was full. */
	GET_HANDFULL,
	/** The players id was invalid. */
	ID_INVALID,
	/** The raise was not allowed, because the player already rose. */
	RAISE_ALREADYRAISED,
	/** Too much cards for a raise to be allowed. */
	RAISE_TOOMUCHCARDS,
	/** Some required Data was corrupt. This should not happen normally and is not the players fault, but a hint for errors in the controller or action logic. */
	REQUIRED_DATA_CORRUPT,
	/** The action was not allowed, because the game state did not fit. */
	STATE_WRONG,
	/** The strike was not allowed, because the player already stroke. */
	STRIKE_ALREADYSTRUCK,
	/** The strike was not allowed, because the player is not an opponent. */
	STRIKE_NOTOPPONENT,
	/** The strike back was not allowed, because the player already stroke back. */
	STRIKEBACK_ALREADYSTRUCKBACK,
	/** The strike back was not allowed, because the player is not the player. */
	STRIKEBACK_NOTPLAYER,
	/** Validation of the action was successful. The player views should not get this code. It is internally used in the controller. */
	VALIDATION_SUCCESS,
	/** The action was executed and there were changes in the game data. */
	EXECUTED_CHANGES,
	/**
	 * The action was accepted, but there were no changes in the game data. This could be, because it was stored in the action buffer maybe, to execute it
	 * later, when all preconditions are fullfilled.
	 */
	EXECUTED_NOCHANGES,
	/** The action was not allowed, because the player is not on turn. */
	TURN_NOTONTURN,
	/** The player is only allowed to accept the next game once. */
	STARTNEXT_ALREADYACCEPTING;
}
