package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

import edu.fhm.cs.ss.schafkopf.model.utilities.CardComparator;

/**
 * Value of a Card.<br>
 * <br>
 * 
 * This Enum has some public attributes that give more information about the value.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum CardValue {

	/** The value 8. */
	ACHTER(0, "Achter", "8", 8),
	/** The value König. */
	KÖNIG(4, "König", "k", 13),
	/** The value 9. */
	NEUNER(0, "Neuner", "9", 9),
	/** The value Ober. */
	OBER(3, "Ober", "o", 12),
	/** The value Sau. */
	SAU(11, "Sau", "s", 14),
	/** The value 7. */
	SIEBENER(0, "Siebener", "7", 7),
	/**  */
	UNTER(2, "Unter", "u", 11),
	/** The value 10. */
	ZEHNER(10, "Zehner", "1", 10);

	/**
	 * The value's id, this is its first character.
	 */
	public final String id;
	/**
	 * The value's name.
	 */
	public final String name;

	/**
	 * The value's number equivalent. Used in {@link CardComparator}.
	 */
	public final int number;
	/**
	 * Values points.
	 */
	public final int points;

	/**
	 * Private Constructor.
	 * 
	 * @param points
	 *            the points.
	 * @param name
	 *            the name.
	 * @param id
	 *            the id.
	 * @param number
	 *            the value's number equivalent.
	 */
	private CardValue(final int points, final String name, final String id, final int number) {

		this.id = id;
		this.points = points;
		this.name = name;
		this.number = number;
	}
}
