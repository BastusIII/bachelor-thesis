package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;

/**
 * A Card in the game Schafkopf.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ICard {
	@Override
	boolean equals(Object obj);

	/**
	 * 
	 * @return the card's color.
	 */
	CardColor getColor();

	/**
	 * 
	 * @return the card's image representation's source.
	 */
	String getId();

	/**
	 * 
	 * @return the card's name.
	 */
	String getName();

	/**
	 * 
	 * @return the card's points.
	 */
	int getPoints();

	/**
	 * 
	 * @return the card's value.
	 */
	CardValue getValue();

	@Override
	int hashCode();

}