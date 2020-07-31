package edu.fhm.cs.ss.schafkopf.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveController;

/**
 * This interface extends the methods from {@link IPrimitiveController} by basic methods not available for views.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IController extends IPrimitiveController {

	/**
	 * @return this controllers persistence handler.
	 */
	IPersistenceHandler getPersistenceHandler();

	/**
	 * 
	 * @return the upper controller, used for navigating back.
	 */
	IController getUpperController();

	/**
	 * 
	 * @return this controllers view.
	 */
	IView getView();

	/**
	 * @return true if this controller is persisting, else false.
	 */
	boolean isPersisting();

	/**
	 * @param upperController
	 *            the controller to set as this controllers upper controller.
	 */
	void setUpperController(IController upperController);

	/**
	 * Start all the views connected to this controller.
	 */
	void start();
}
