package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * The possible player positions.<br>
 * <br>
 *
 * Offers functionality to get the next and previous position to a given position.
 *
 * @author Sebastian Stumpf
 *
 */
public enum PlayerPosition {
	// order is important so the order of ENUM map values containing player
	// positions as keys is fitting
	/** The bottom position. */
	BOTTOM,
	/** The left position. */
	LEFT,
	/** The top position. */
	TOP,
	/** The right position. */
	RIGHT;

	/**
	 * Get the next (clockwise) position.
	 *
	 * @return the next position.
	 */
	public PlayerPosition getNext() {

		switch (this) {
			case BOTTOM:
				return LEFT;
			case LEFT:
				return TOP;
			case TOP:
				return RIGHT;
			case RIGHT:
				return BOTTOM;
			default:
				// must not happen
				return null;
		}
	}

	/**
	 * Get the previous (counter-clockwise) PlayerPosition.
	 *
	 * @return the previous position.
	 */
	public PlayerPosition getPrevious() {

		switch (this) {
			case BOTTOM:
				return RIGHT;
			case LEFT:
				return BOTTOM;
			case TOP:
				return LEFT;
			case RIGHT:
				return TOP;
			default:
				// must not happen
				return null;
		}
	}

}
