package edu.fhm.cs.ss.schafkopf.test.recording;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.controller.GameController;
import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.test.controller.ITestController;
import edu.fhm.cs.ss.schafkopf.test.controller.TestController;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IInteractivePlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IChooseGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IGetCardsAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IPlayCardAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IRaiseAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStartNextGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStrikeAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStrikeBackAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * The test game controller extends {@link GameController} by the functionality to record and validate a test game.<br>
 * <br>
 *
 * For this purpose, a {@link GameRecorder} is created an will be provided with all changes that occur on the game data during the game progress.
 *
 * @author Sebastian Stumpf
 *
 */
public class TestGameController extends GameController implements ITestGameController {

	/**
	 * A monitor used to synchronize the action handling.
	 */
	private final Object gameMonitor = new Object();
	/**
	 * The created game recorder, that will be provided with the information about the game progress.
	 */
	private final IGameRecorder gameRecorder;
	/**
	 * If the game is finished, all incoming actions will be refused.
	 */
	private boolean gameFinished = false;
	/**
	 * The test controller, that started this test game an will be given feedback to.
	 */
	private final ITestController testController;

	/**
	 * Instantiate the test game controller with the given parameters.
	 *
	 * @param controller
	 *            the test controller that will be given feedback to.
	 * @param gameData
	 *            the game data used to initialize the game.
	 * @param stack
	 *            the stack. if this is null, the game data will be initialized with a random stack.
	 * @param players
	 *            the players that will play the game. These have to be four players!
	 * @param firstPlayerPosition
	 *            the position of the first player used to initialize the game data.
	 * @param testGameDataFilename
	 *            the filename of the created test game data.
	 */
	public TestGameController(final ITestController controller, final GameData gameData, final List<ICard> stack, final Map<PlayerPosition, IPlayerView> players,
			final PlayerPosition firstPlayerPosition, final String testGameDataFilename) {

		super(null, gameData, null);
		this.testController = controller;
		// check if we have an interactive player in the player map.
		boolean interactive = false;
		for (final IPlayerView playerView : players.values()) {
			if (playerView instanceof IInteractivePlayerView) {
				interactive = true;
				break;
			}
		}
		// initializing the game
		new FullAccessGameUtils(getGameData()).initializeGameData(firstPlayerPosition, stack);
		// need to give a copy of the stack instance to the observer, so it is not changed by reference by the player actions.
		this.gameRecorder = new GameRecorder(interactive, this, testGameDataFilename, new ArrayList<ICard>(gameData.getStack()));
		// subscribe players and set their game controller.
		for (final PlayerPosition position : players.keySet()) {
			players.get(position).setGameController(this);
		}
		this.gameRecorder.startWatching();
	}

	/**
	 * This method is overridden and now also sets the {@link #gameFinished} flag. So no more actions are processed. See the interface documentation for more
	 * information.
	 */
	@Override
	public void back() {

		// finish game instantly
		synchronized (gameMonitor) {
			super.back();
			this.gameFinished = true;
		}
		// inform the observer that the game was finished calling back
		gameRecorder.gameFinished();
	}

	/**
	 * This method now also provides the game recorder with the action and game data information. See the interface documentation for more information.
	 */
	@Override
	public ActionValidationCode handleGameAction(final IAction action) {

		try {
			synchronized (gameMonitor) {
				final ActionValidationCode retVal = super.handleGameAction(action);
				IActionData handledAction;
				if (action instanceof IChooseGameAction) {
					handledAction = new ActionData(retVal, ActionType.CHOOSE, action.getPosition(), ((IChooseGameAction) action).getChosenGame(), null, false);
				} else if (action instanceof IGetCardsAction) {
					handledAction = new ActionData(retVal, ActionType.GET, action.getPosition(), null, null, false);
				} else if (action instanceof IPlayCardAction) {
					handledAction = new ActionData(retVal, ActionType.PLAY, action.getPosition(), null, ((IPlayCardAction) action).getChosenCard(), false);
				} else if (action instanceof IRaiseAction) {
					handledAction = new ActionData(retVal, ActionType.RAISE, action.getPosition(), null, null, false);
				} else if (action instanceof IStartNextGameAction) {
					handledAction = new ActionData(retVal, ActionType.NEXTGAMEACCEPTED, action.getPosition(), null, null, false);
				} else if (action instanceof IStrikeAction) {
					handledAction = new ActionData(retVal, ActionType.STRIKE, action.getPosition(), null, null, ((IStrikeAction) action).isStriking());
				} else if (action instanceof IStrikeBackAction) {
					handledAction = new ActionData(retVal, ActionType.STRIKEBACK, action.getPosition(), null, null, ((IStrikeBackAction) action).isStrikingBack());
				} else {
					handledAction = null;
				}
				// the unrestricted observer gets only updates if the game is not
				// yet finished to avoid concurrent modification errors
				if (!gameFinished) {
					// inform the observer that an action was processed
					gameRecorder.actionHandled(handledAction);
					if (retVal == ActionValidationCode.EXECUTED_CHANGES) {
						// inform the observer that the data has changed
						gameRecorder.gameChanged(getGameData());
						if (getGameData() != null && getGameData().getLastExecutedAction() != null && ActionType.NEXTGAMESTARTED.equals(getGameData().getLastExecutedAction().getActionType())) {
							gameFinished = true;
							super.back();
							// inform the observer that the game was finished calling back
							gameRecorder.gameFinished();
						}
					}
				}
				return retVal;
			}
		} catch (final RuntimeException e) {
			// we dont want any exceptions to be thrown here in the test environment, errors are tracked down by the validationUtils.
			return ActionValidationCode.REQUIRED_DATA_CORRUPT;
		}
	}

	/**
	 * This method now also provides the player with each successfully subscribed player. See the interface documentation for more information.
	 */
	@Override
	public boolean subscribePlayer(final IPlayerView playerView) {

		if (super.subscribePlayer(playerView)) {
			// inform the observer that a new player has subscribed
			gameRecorder.playerSubscribed(playerView);
			return true;
		}
		return false;
	}

	/**
	 * Feedback from the game recorder is forwarded to {@link TestController#testFeedBack(ITestValidationInfo)}, the finished flag set to true and all views
	 * terminated by calling {@link #back()}. See the interface documentation for more information.
	 */
	@Override
	public void testFeedBack(final ITestValidationInfo testValidationInfo) {

		// exit his game controller by calling back could be that the game is in a deadlock and not finished properly, so call back here
		synchronized (gameMonitor) {
			this.back();
			this.gameFinished = true;
		}
		// hand over the test feedback to the test controller
		testController.testFeedBack(testValidationInfo);
	}
}
