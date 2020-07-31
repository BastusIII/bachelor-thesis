package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

import edu.fhm.cs.ss.schafkopf.model.utilities.CardComparator;

/**
 * Color of a Card.<br>
 * <br>
 * 
 * This Enum has some public attributes that give more information about the color.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum CardColor {

	/** The color Eichel. */
	EICHEL("Eichel", "e", 3),
	/** The color Gras. */
	GRAS("Gras", "g", 2),
	/** The color Herz. */
	HERZ("Herz", "h", 1),
	/** The color Schelln. */
	SCHELLN("Schelln", "s", 0);

	/**
	 * The color's id is the first character of its name.
	 */
	public final String id;
	/**
	 * The color's name.
	 */
	public final String name;
	/**
	 * The color's number equivalent. Used in {@link CardComparator}.
	 */
	public final int number;

	/**
	 * Instantiates the color with given parameters.
	 * 
	 * @param name
	 *            the name.
	 * @param id
	 *            the id.
	 * @param number
	 *            the number.
	 */
	private CardColor(final String name, final String id, final int number) {

		this.id = id;
		this.name = name;
		this.number = number;
	}

	@Override
	public String toString() {

		return name;
	}
}
