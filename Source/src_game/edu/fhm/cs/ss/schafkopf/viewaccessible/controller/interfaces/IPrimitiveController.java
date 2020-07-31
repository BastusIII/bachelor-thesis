package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.view.interfaces.IView;

/**
 * This interface defines basic controller methods available for views.<br>
 * <br>
 * 
 * All controllers have to implement them.<br>
 * A controller has the duty to update all of its connected views about changed data.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPrimitiveController {

	/**
	 * Cleans up by terminating all connected views and navigate back to the upper controller by calling its start method.
	 */
	void back();

	/**
	 * Connect a view to this controller.
	 * 
	 * This view will get updates about data changes. It should be called in the views constructor.
	 * 
	 * @param view
	 *            the view to set as this controllers view.
	 */
	void setView(IView view);

}
