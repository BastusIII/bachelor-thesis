package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * The settings for the game Schafkopf.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPrimitiveGameSettings {

	/** Limit for the setting's basic charge value. */
	int CHARGE_MAX_VALUE = 10000;
	/** @see #CHARGE_MAX_VALUE */
	int CHARGE_MIN_VALUE = 1;
	/** @see #CHARGE_MAX_VALUE */
	int MONEY_MAX_VALUE = 100000;
	/** @see #CHARGE_MAX_VALUE */
	int MONEY_MIN_VALUE = 1;
	/** @see #CHARGE_MAX_VALUE */
	int MULTIPLIER_MAX_VALUE = 10;
	/** @see #CHARGE_MAX_VALUE */
	int MULTIPLIER_MIN_VALUE = 2;
	/** @see #CHARGE_MAX_VALUE */
	int NAME_MAX_LENGTH = 15;
	/** @see #CHARGE_MAX_VALUE */
	int NAME_MIN_LENGTH = 1;

	@Override
	boolean equals(Object obj);

	/**
	 * This value is used to calculate all the charges. It is the counting value. For example, the charge for a solo is the solo multiplier * basicCharge.
	 *
	 * @return the basic charge value.
	 */
	int getBasicCharge();

	/**
	 *
	 * @param position
	 *            the player's position.
	 * @return the name of the player at the given position.
	 */
	String getName(PlayerPosition position);

	/**
	 *
	 * @return the bottom player name.
	 */
	String getNameBottom();

	/**
	 *
	 * @return the left player name.
	 */
	String getNameLeft();

	/**
	 *
	 * @return the right player name.
	 */
	String getNameRight();

	/**
	 *
	 * @return the top player name.
	 */
	String getNameTop();

	/**
	 *
	 * @return the solo game multiplier.
	 */
	int getSoloMultiplier();

	/**
	 *
	 * @return the players' start money.
	 */
	int getStartMoney();

	@Override
	int hashCode();
}
