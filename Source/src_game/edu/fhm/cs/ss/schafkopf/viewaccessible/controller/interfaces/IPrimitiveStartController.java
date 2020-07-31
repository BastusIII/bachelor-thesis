package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces;

/**
 * This is a navigation interface that offers methods to start the different application's views with various options.<br>
 * <br>
 *
 * This controller should be set as the upper controller for the started views to be able to navigate back.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPrimitiveStartController extends IPrimitiveController {

	/**
	 * Start the change settings view.
	 *
	 */
	void changeSettings();

	/**
	 * Start the interactive player view with a newly initialized game.
	 *
	 * The game will be played vs. 3 AI views.
	 */
	void newGame();

	/**
	 * Start the interactive player view with a game based on the last persisted game data.
	 *
	 * The game will be played vs. 3 AI views.
	 *
	 * @return true, if the game could be loaded and was started, else false.
	 */
	boolean resumeGame();

}