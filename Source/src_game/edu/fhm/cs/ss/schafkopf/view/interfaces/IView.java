package edu.fhm.cs.ss.schafkopf.view.interfaces;

/**
 * Every view of this application has to implement this interface and provide the basic view methods.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IView {

	/**
	 * Called by controller, start displaying this view and initialize everything necessary. After the start user interactions can be received and forwarded to
	 * the controller.
	 */
	void start();

	/**
	 * Called by Controller, stop displaying this view and clean up everything necessary. User interaction should no longer be received.
	 */
	void stop();

	/**
	 * Called if the data has changed, so the view can display the changes.
	 */
	void update();
}
