package edu.fhm.cs.ss.schafkopf.test.model;

/**
 * Test validation code defines codes, that give information about the execution of a test or test game.<br>
 * <br>
 * Each code has a priority, higher priority means more important.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum TestValidationCode {
	/** Execution was successfull. */
	SUCCESS(0),
	/** Warnings occurred, for example nondeterminism. */
	WARNING(1),
	/** The executed actions were invalid. */
	ERROR_INVALID_ACTION_EXECUTION(2),
	/** The game data was invalid, but the values are not critical. */
	ERROR_NON_CRITICAL_INVALID_STATE(3),
	/** A deadlock occurred. */
	ERROR_DEADLOCK(4),
	/** The game data was invalid, the values were critical. This can cause exceptions in the program progress. */
	ERROR_CRITICAL_INVALID_STATE(5),
	/** A general error occurred, for example a test file could not be loaded. General errors are not validation errors in a game. */
	ERROR_GENERAL(6);

	/**
	 * The priority of this value. Higher priority is more important.
	 */
	public final int priority;

	/**
	 * Private Constructor to set the priority.
	 * 
	 * @param priority
	 *            the priority.
	 */
	private TestValidationCode(final int priority) {

		this.priority = priority;
	}
}
