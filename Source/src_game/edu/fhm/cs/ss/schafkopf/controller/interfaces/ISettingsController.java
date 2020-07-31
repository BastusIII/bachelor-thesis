package edu.fhm.cs.ss.schafkopf.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveSettingsController;

/**
 * This interface extends the methods from {@link IPrimitiveSettingsController} by methods not available for views.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ISettingsController extends IPrimitiveSettingsController, IController {

	/**
	 * @return the the settings data instance.
	 */
	IGameSettings getSettings();

	/**
	 * @param settings
	 *            the settings data instance to set.
	 */
	void setSettings(IGameSettings settings);

}
