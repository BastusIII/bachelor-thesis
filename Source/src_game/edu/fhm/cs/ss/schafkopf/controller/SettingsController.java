package edu.fhm.cs.ss.schafkopf.controller;

import edu.fhm.cs.ss.schafkopf.controller.interfaces.IController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.ISettingsController;
import edu.fhm.cs.ss.schafkopf.model.GameSettings;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.interfaces.ISettingsView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPrimitiveGameSettings;

/**
 * This controller has the task to manipulate a settings data according to incoming interactions from a connected {@link ISettingsView} and update it when the
 * data has changed. <br>
 *
 * Interactions are validated based on the limits defined in {@link IGameSettings}. Only valid interactions are executed.
 *
 * @author Sebastian Stumpf
 *
 */
public class SettingsController extends BaseController implements ISettingsController {

	/** the settings data. */
	private IGameSettings settings;

	/**
	 * Instantiate the controller with only a persistence handler, other values are set to null.
	 *
	 * @param persistenceHandler
	 *            the persistence handler.
	 */
	public SettingsController(final IPersistenceHandler persistenceHandler) {

		this(persistenceHandler, null);
	}

	/**
	 * Instantiate the controller with the given parameters.
	 *
	 * @param persistenceHandler
	 *            the persistence handler.
	 * @param upperController
	 *            the settings data.
	 */
	public SettingsController(final IPersistenceHandler persistenceHandler, final IController upperController) {

		super(persistenceHandler, upperController);
		if (isPersisting()) {
			this.settings = (IGameSettings) getPersistenceHandler().load(IGameSettings.FILENAME);
			if (settings == null) {
				// create new default settings instance
				settings = new GameSettings();
			}
		} else {
			settings = new GameSettings();
		}
	}

	@Override
	public boolean changeBasicCharge(final int value) {

		if (value < IPrimitiveGameSettings.CHARGE_MIN_VALUE || value > IPrimitiveGameSettings.CHARGE_MAX_VALUE) {
			return false;
		}
		settings.setBasicCharge(value);
		getView().update();
		return true;
	}

	@Override
	public boolean changePlayerName(final PlayerPosition position, final String name) {

		if (name == null || name.length() < IPrimitiveGameSettings.NAME_MIN_LENGTH || name.length() > IPrimitiveGameSettings.NAME_MAX_LENGTH) {
			return false;
		}
		if (position == null) {
			return false;
		}
		settings.setName(position, name);
		getView().update();
		return true;
	}

	@Override
	public boolean changeSoloMultiplier(final int value) {

		if (value < IPrimitiveGameSettings.MULTIPLIER_MIN_VALUE || value > IPrimitiveGameSettings.MULTIPLIER_MAX_VALUE) {
			return false;
		}
		settings.setSoloMultiplier(value);
		getView().update();
		return true;
	}

	@Override
	public boolean changeStartMoney(final int value) {

		if (value < IPrimitiveGameSettings.MONEY_MIN_VALUE || value > IPrimitiveGameSettings.MONEY_MAX_VALUE) {
			return false;
		}
		settings.setStartMoney(value);
		getView().update();
		return true;
	}

	@Override
	public IPrimitiveGameSettings getPrimitiveSettings() {

		return settings;
	}

	@Override
	public IGameSettings getSettings() {

		return settings;
	}

	@Override
	public boolean saveSettings() {

		if (isPersisting()) {
			return getPersistenceHandler().persist(settings, settings.getFilename());
		} else {
			return false;
		}

	}

	@Override
	public void setSettings(final IGameSettings settings) {

		this.settings = settings;

	}

}
