package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPrimitiveGameSettings;

/**
 * This interface offers methods that can be called by the settings view to interact with the controller to change settings.<br>
 * <br>
 *
 * Interactions are validated before executing them on the data.<br>
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPrimitiveSettingsController extends IPrimitiveController {

	/**
	 * Change the basic charge.
	 *
	 * @param value
	 *            the value to set.
	 * @return true if validation was successful and the interaction executed.
	 */
	boolean changeBasicCharge(int value);

	/**
	 * Change a players name.
	 *
	 * @param position
	 *            the players position.
	 * @param name
	 *            the name to set.
	 * @return true if validation was successful and the interaction executed.
	 */
	boolean changePlayerName(PlayerPosition position, String name);

	/**
	 * Change the solo multiplier.
	 *
	 * The solo multiplier defines the value the basic charge is multiplied with in case of a solo.
	 *
	 * @param value
	 *            the value to set.
	 * @return true if validation was successful and the interaction executed.
	 */
	boolean changeSoloMultiplier(int value);

	/**
	 * Change each players start money..
	 *
	 * Starting a new game each player will have that amount of money.
	 *
	 * @param value
	 *            the value to set.
	 * @return true if validation was successful and the interaction executed.
	 */
	boolean changeStartMoney(int value);

	/**
	 * For views to be able to get the current data without having to wait for an update.
	 *
	 * @return return the read only interface of this controllers data.
	 */
	IPrimitiveGameSettings getPrimitiveSettings();

	/**
	 * Persist the current settings.
	 *
	 * Path and persistence handler are defined in the controller.
	 *
	 * @return true if the settings were successfully saved.
	 */
	boolean saveSettings();

}