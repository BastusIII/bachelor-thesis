package edu.fhm.cs.ss.schafkopf.test.recording;

import java.util.ArrayList;

import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.test.formattingandvalidation.FormattingUtils;
import edu.fhm.cs.ss.schafkopf.test.formattingandvalidation.TestUtils;
import edu.fhm.cs.ss.schafkopf.test.model.ITestGameData;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestGameData;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IAutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * The game recorder implements the functionality to record and validate a test game.<br>
 * <br>
 *
 * For recording, the update methods store all the received data in a {@link TestGameData} instance. Validation is done on each incoming game data from the
 * current game progress. Deadlocks are detected by a watcher thread, each game has a timeout, defined in {@link TestSettings#TIMEOUT} as the maximum duration
 * it is allowed to take. If a game need longer, the watcher will detect a timeout.<br>
 * If a deadlock or any validation errors are found in the current test game, or if the game finished successfully, the {@link TestGameController}'s
 * {@link TestGameController#testFeedBack(ITestValidationInfo)} is called and the created {@link TestValidationInfo} returned.
 *
 * @author Sebastian Stumpf
 *
 */
public class GameRecorder implements IGameRecorder {
	/**
	 * The watcher thread, that detects deadlocks. before terminating, this thread will call the {@link TestGameController#testFeedBack(ITestValidationInfo)}
	 * method. In interactive games, there is no timeout.
	 *
	 * @author Sebastian Stumpf
	 *
	 */
	private class WatcherThread extends Thread {
		@Override
		public void run() {

			try {
				synchronized (updateMonitor) {
					if (!interactive) {
						// if we are automatically testing the game has only
						// limited
						// time
						// to finish before the timeout occurs
						updateMonitor.wait(TestSettings.TIMEOUT);
					} else {
						// if we are interactive testing the game has unlimited
						// time
						// to finish

						updateMonitor.wait();
					}
					if (!finished) {
						if (validState) {
							// if we are in invalid state, dont generate the dead
							// lock string, because that could cause exceptions
							// then.
							testValidationInfo.appendInformation(TestValidationCode.ERROR_DEADLOCK,
									testGameData + " ran into a deadlock!\nLast game progress: \n\n" + FormattingUtils.generateDeadLockErrorString(testGameData));
						}
					}
					if (interactive) {
						// give the interactive view a little time to display its last game state
						try {
							updateMonitor.wait(100);
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (final InterruptedException e) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_DEADLOCK, "Watcher Thread for " + testGameData + " was interrupted!");
			}
			testValidationInfo.setTestCase(testGameData);
			controller.testFeedBack(testValidationInfo);
		}
	}

	/**
	 * Monitor for the update methods to ensure they are all in the proper order.
	 */
	private final Object updateMonitor = new Object();
	/**
	 * If the game was finished, this is true.
	 */
	private boolean finished;
	/**
	 * If the game is interactive, there is no deadlock timeout.
	 */
	private final boolean interactive;
	/**
	 * The data instance, all the received information is stored in.
	 */
	private final ITestGameData testGameData;
	/**
	 * The controller to interact with, when an error is found or the game finished.
	 */
	private final ITestGameController controller;
	/**
	 * True, if an invalid state is found. Then no more validations are done.
	 */
	private boolean validState;
	/**
	 * The test validation info, the validation information and test game data is appended to.
	 */
	private final ITestValidationInfo testValidationInfo;

	/**
	 * Instantiate the recorder with the given parameters.
	 *
	 * @param interactive
	 *            true, if this is an interactive game and the timeout should be ignored.
	 * @param controller
	 *            the test game controller to interact with.
	 * @param testDataFilename
	 *            the filename, the test game data should be created with.
	 * @param initialStack
	 *            the initial stack that will be stored to the test game data.
	 */
	public GameRecorder(final boolean interactive, final ITestGameController controller, final String testDataFilename, final ArrayList<ICard> initialStack) {

		this.finished = false;
		this.validState = true;
		this.interactive = interactive;
		this.controller = controller;
		this.testValidationInfo = new TestValidationInfo();
		this.testGameData = new TestGameData(testDataFilename, TestSettings.ACCEPT_RESTART_STATE, initialStack);
	}

	@Override
	public void actionHandled(final IActionData action) {

		synchronized (updateMonitor) {
			// no more updates if an invalid state is found
			if (validState) {
				testGameData.getActionList().add(action);
			}
		}
	}

	@Override
	public void gameChanged(final IGameData gameData) {

		synchronized (updateMonitor) {
			// no more updates if an invalid state is found
			if (validState) {
				// validate basic gamedata values
				TestUtils.validateGameState(testValidationInfo, gameData);
				if (!testValidationInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
					this.validState = false;
					// if an error occurs, we cancel the game.
					updateMonitor.notifyAll();
				} else {
					if (!testGameData.getGamesList().isEmpty()) {
						// validate action execution
						TestUtils.validateActionExecution(testValidationInfo, testGameData.getGamesList().get(testGameData.getGamesList().size() - 1), gameData);
					}
					if (gameData.getLastExecutedAction().getActionType().equals(ActionType.RAISE)) {
						testGameData.getRaiseActionlist().add(gameData.getLastExecutedAction());
					} else if (!(gameData.getLastExecutedAction().getActionType().equals(ActionType.NEXTGAMEACCEPTED) || gameData.getLastExecutedAction().getActionType().equals(ActionType.GET) || gameData
							.getLastExecutedAction().getActionType().equals(ActionType.NEXTGAMESTARTED))) {
						testGameData.getDeterministicActionlist().add(gameData.getLastExecutedAction());
					}
				}
				testGameData.getGamesList().add(new GameData(gameData));
			}
		}
	}

	@Override
	public void gameFinished() {

		synchronized (updateMonitor) {
			this.finished = true;
			updateMonitor.notifyAll();
		}
	}

	@Override
	public void playerSubscribed(final IPlayerView player) {

		synchronized (updateMonitor) {
			testGameData.getPlayers().put(player.getPlayerId().getPosition(), player.getClass());
			if (player instanceof IAutonomousPlayerView) {
				testGameData.getAis().put(player.getPlayerId().getPosition(), ((IAutonomousPlayerView) player).getAi().getClass());
			}
		}
	}

	@Override
	public void startWatching() {

		new WatcherThread().start();
	}
}
