package edu.fhm.cs.ss.schafkopf.model;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This data class holds the information about a card.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class Card implements ICard {

	/**
	 * The card's color.
	 */
	private final CardColor color;
	/**
	 * The card's id is generated from color and value id.
	 */
	private final String id;
	/**
	 * The card's name.
	 */
	private final String name;
	/**
	 * The card's points.You get them if you win this card. The points are generated from the card's value.
	 */
	private final int points;
	/**
	 * The cards value.
	 */
	private final CardValue value;

	/**
	 * Instantiate a card with the given parameters.
	 * 
	 * @param color
	 *            The card's color.
	 * @param value
	 *            The card's value.
	 */
	public Card(final CardColor color, final CardValue value) {

		this.color = color;
		this.value = value;
		name = color.name + " " + value.name;
		points = value.points;
		this.id = color.id + value.id;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Card other = (Card) obj;
		if (color != other.color) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (points != other.points) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (value != other.value) {
			return false;
		}
		return true;
	}

	@Override
	public CardColor getColor() {

		return color;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public int getPoints() {

		return points;
	}

	@Override
	public CardValue getValue() {

		return value;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (color == null ? 0 : color.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + points;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}

	@Override
	public String toString() {

		return this.name;
	}

}
