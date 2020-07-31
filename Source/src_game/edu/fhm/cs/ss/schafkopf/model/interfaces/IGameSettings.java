package edu.fhm.cs.ss.schafkopf.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPrimitiveGameSettings;

/**
 * This interface extends {@link IPrimitiveGameSettings} by write access methods and data not available for the setting view.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IGameSettings extends IPersistenceObject, IPersistableObject, IPrimitiveGameSettings {
	/**
	 * Default value for the basic charge used to create a new data instance.
	 */
	int DEFVAL_BASIC_CHARGE = 10;
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	String DEFVAL_NAME_BOTTOM = "Bottom Player";
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	String DEFVAL_NAME_LEFT = "Left Player";
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	String DEFVAL_NAME_RIGHT = "Right Player";
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	String DEFVAL_NAME_TOP = "Top Player";
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	int DEFVAL_SOLO_MULTIPLIER = 5;
	/**
	 * @see #DEFVAL_NAME_BOTTOM
	 */
	int DEFVAL_START_MONEY = 1000;
	/**
	 * Filename of the persistence object.
	 */
	String FILENAME = "GAME_SETTINGS";

	/**
	 * Adopt all settings from a given settings instance to this instance.
	 *
	 * @param toAdopt
	 *            the settings to adopt.
	 */
	void adoptSettings(IGameSettings toAdopt);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	/**
	 *
	 * @param charge
	 *            the basic money value to set.
	 */
	void setBasicCharge(int charge);

	/**
	 *
	 * @param position
	 *            the players position.
	 * @param name
	 *            the name to set.
	 */
	void setName(PlayerPosition position, String name);

	/**
	 *
	 * @param nameBottom
	 *            the bottom player name to set.
	 */
	void setNameBottom(String nameBottom);

	/**
	 *
	 * @param nameLeft
	 *            the left player name to set.
	 */
	void setNameLeft(String nameLeft);

	/**
	 *
	 * @param nameRight
	 *            the right player name to set.
	 */
	void setNameRight(String nameRight);

	/**
	 *
	 * @param nameTop
	 *            the top player name to set.
	 */
	void setNameTop(String nameTop);

	/**
	 *
	 * @param multiplier
	 *            the solo game multiplier to set.
	 */
	void setSoloMultiplier(int multiplier);

	/**
	 *
	 * @param startMoney
	 *            the players'start money to set.
	 */
	void setStartMoney(int startMoney);

}