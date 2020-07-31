package edu.fhm.cs.ss.schafkopf.test.recording;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.test.controller.ITestController;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;

/**
 * The game controller is watching a test game and storing every information it gets from the {@link ITestGameController}. It also validates the game progress.<br>
 * <br>
 *
 * It can be seen as a passive view that is only getting updates, but - during a valid game - not interacting with the controller.<br>
 * If validation errors or a deadlock occurs in the game progress, the recorder cancels the game by calling
 * {@link ITestController#testFeedBack(edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo)}. This method is also called, if the game is successfully
 * finished.<br>
 * Implementing classes should detect deadlocks, that is a game with autonomous player views that takes mor time than defined in {@link TestSettings#TIMEOUT}.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IGameRecorder {
	/**
	 * Record the given game data by storing a deep copy of it. The incoming data is also validated and the game progress cancelled, if an invalid state was
	 * found.
	 *
	 * @param gameData
	 *            the game data from the current game progress.
	 */
	void gameChanged(IGameData gameData);

	/**
	 * Record the given action data. The action is not validated.
	 *
	 * @param action
	 *            the action data.
	 */
	void actionHandled(IActionData action);

	/**
	 * This method is called when a recorded game has successfully finished. In this case, the feedback information is gathered and created and sent to the
	 * {@link ITestGameController}. After this method is called, no more records or validations are made and all started threads must terminate.
	 */
	void gameFinished();

	/**
	 * Record the given player as a subscribed player.
	 *
	 * @param player
	 *            the player views.
	 */
	void playerSubscribed(IPlayerView player);

	/**
	 * Start detecting deadlocks.
	 */
	void startWatching();
}
