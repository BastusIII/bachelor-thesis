package edu.fhm.cs.ss.schafkopf.controller;

import edu.fhm.cs.ss.schafkopf.controller.interfaces.IController;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IView;

/**
 * This base controller implements the basic functionality, all controllers have in common.<br>
 * <br>
 * 
 * Navigation and the {@link #start()}-method is based on a single connected view. If more views should be supported, {@link #back()} and {@link #start()} has
 * to be overwritten. The persistence handler is also stored here.
 * 
 * @author Sebastian Stumpf
 * 
 */
public abstract class BaseController implements IController {
	/** The upper controller used to navigate back to the upper view. */
	private IController upperController;
	/** The persistence handler used to store and load objects. */
	private final IPersistenceHandler persistenceHandler;
	/** This controller is connected to a single view. */
	private IView view;

	/**
	 * Instantiate the controller with given parameters. For use in extending classes.
	 * 
	 * @param persistenceHandler
	 *            the persistence handler.
	 * @param upperController
	 *            the upper controller used to navigate back.
	 */
	public BaseController(final IPersistenceHandler persistenceHandler, final IController upperController) {

		super();
		this.persistenceHandler = persistenceHandler;
		this.upperController = upperController;
	}

	@Override
	public void back() {

		if (this.view != null) {
			this.view.stop();
		}
		if (upperController != null) {
			upperController.start();
		}
	}

	@Override
	public IPersistenceHandler getPersistenceHandler() {

		return persistenceHandler;
	}

	@Override
	public IController getUpperController() {

		return upperController;
	}

	@Override
	public IView getView() {

		return view;
	}

	@Override
	public boolean isPersisting() {

		return this.persistenceHandler != null;
	}

	@Override
	public void setUpperController(final IController upperController) {

		this.upperController = upperController;
	}

	@Override
	public void setView(final IView view) {

		this.view = view;

	}

	@Override
	public void start() {

		if (this.view != null) {
			this.view.start();
		}
	}
}
