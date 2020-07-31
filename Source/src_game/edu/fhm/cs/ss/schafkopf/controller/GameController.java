package edu.fhm.cs.ss.schafkopf.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.fhm.cs.ss.schafkopf.controller.interfaces.IController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.IGameController;
import edu.fhm.cs.ss.schafkopf.model.PlayerData;
import edu.fhm.cs.ss.schafkopf.model.PlayerId;
import edu.fhm.cs.ss.schafkopf.model.RestrictedGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.PlayerUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * The game controller has the task to interact with {@link IPlayerView}s and manipulate the game data according to their actions and update them if the game
 * data has changed.<br>
 * <br>
 * 
 * Behavior of this implementation:<br>
 * - {@link #back()}: causes all subscribed player views to stop.<br>
 * - {@link #handleGameAction(IAction)}: executed if the actions ID is one of the subscribed player IDs and the validation was successful, otherwise refused.<br>
 * - {@link #subscribePlayer(IPlayerView)} up to 4 player views can subscribe. More subscription calls are refused.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class GameController extends BaseController implements IGameController {

	/** The first subscribing player gets this position. */
	public static final PlayerPosition FIRST_SUBSCRIBER_POSITION = PlayerPosition.BOTTOM;
	/** An instance of gameData. There is only one instance in the whole game. */
	private IGameData originalGameData;
	/** Stores the position the next subscribing player views will get. Incremented with each subscribing player. */
	private PlayerPosition nextSubscriberPosition;
	/** True, if this controller is started. If this attribute is false, the controller will not react to incoming actions. */
	private boolean started;
	/** The subscribed player views. */
	private final Map<IPlayerId, IPlayerView> playerViews;

	/**
	 * Instantiate the controller with only a persistence handler. Game data and upper controller is set to null.
	 * 
	 * @param persistenceHandler
	 *            the persistence handler.
	 */
	public GameController(final IPersistenceHandler persistenceHandler) {

		this(persistenceHandler, null, null);
	}

	/**
	 * Instantiate the controller with the given parameters.
	 * 
	 * @param persistenceHandler
	 *            the persistence handler.
	 * @param gameData
	 *            the game data instance.
	 * @param upperController
	 *            the uppser controller used to navigate back.
	 */
	public GameController(final IPersistenceHandler persistenceHandler, final IGameData gameData, final IController upperController) {

		super(persistenceHandler, upperController);
		this.playerViews = new HashMap<IPlayerId, IPlayerView>();
		this.nextSubscriberPosition = FIRST_SUBSCRIBER_POSITION;
		this.started = false;
		this.originalGameData = gameData;
	}

	@Override
	public void back() {

		super.back();
		// back finished all player views
		synchronized (playerViews) {
			started = false;
			for (final IPlayerView view : playerViews.values()) {
				view.stop();
			}
			// clear views
			playerViews.clear();
			// reset subscriber position
			nextSubscriberPosition = FIRST_SUBSCRIBER_POSITION;
		}
	}

	@Override
	public IGameData getGameData() {

		return originalGameData;
	}

	@Override
	public ActionValidationCode handleGameAction(final IAction action) {

		// TODO: ERROR on purpose, gameData = null -> critical invalid state
		// if (Math.random() < 0.0001) {
		// originalGameData.setPlayerDatas(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setCharge(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setLastExecutedAction(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setOpponentTeam(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setPlayerTeam(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setStack(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData.setWinnerTeam(null);
		// }
		// if (Math.random() < 0.0001) {
		// originalGameData = null;
		// }

		// TODO: ERROR on purpose -> uncritical invalid state
		// if (Math.random() < 0.0001) {
		// originalGameData.getCharge().setBasic(0);
		// originalGameData.setColor(null);
		// }

		try {
			// players can only hand in actions, if the game is started.
			if (!started) {
				return ActionValidationCode.TURN_NOTONTURN;
			}
			// check if the player id is correct
			if (!playerViews.containsKey(action.getPlayerId())) {
				return ActionValidationCode.ID_INVALID;
			}
			ActionValidationCode retVal;
			synchronized (originalGameData) {
				retVal = action.execute(originalGameData);
				if (retVal == ActionValidationCode.EXECUTED_CHANGES) {
					notifyPlayers();
					if (isPersisting()) {
						getPersistenceHandler().persist(originalGameData.getPersistenceObject());
					}
				}
			}
			return retVal;
		} catch (final NullPointerException e) {
			return ActionValidationCode.REQUIRED_DATA_CORRUPT;
		}
	}

	@Override
	public void notifyPlayers() {

		synchronized (playerViews) {
			if (started) {
				for (final IPlayerId playerId : playerViews.keySet()) {
					playerViews.get(playerId).updateGameData(new PlayerUtils(new RestrictedGameData(playerId.getPosition(), originalGameData)));
				}
			}
		}
	}

	@Override
	public void setGameData(final IGameData gameData) {

		this.originalGameData = gameData;
	}

	@Override
	public void start() {

		// game can only be started if all players are full
		if (playerViews.size() == IRestrictedGameData.NUMBER_OF_PLAYERS) {
			synchronized (playerViews) {
				for (final IView view : playerViews.values()) {
					view.start();
				}
				started = true;
			}
			notifyPlayers();
		}
	}

	@Override
	public boolean subscribePlayer(final IPlayerView playerView) {

		synchronized (playerViews) {
			if (playerView == null || playerViews.size() == IRestrictedGameData.NUMBER_OF_PLAYERS || originalGameData == null) {
				return false;
			}
			IPlayerId playerId = null;

			// generate player id
			playerId = new PlayerId(nextSubscriberPosition, UUID.randomUUID());
			// create player data on the subscribed position if there is no player data set in game yet
			if (originalGameData.getPlayerDatas().get(playerId.getPosition()) == null) {
				originalGameData.getPlayerDatas().put(playerId.getPosition(), new PlayerData(originalGameData.getGameSettings(), playerId.getPosition()));
			}
			// increment position for next subscriber
			nextSubscriberPosition = nextSubscriberPosition.getNext();
			// add playerView to subscribed views
			playerViews.put(playerId, playerView);
			// provide player with player id
			playerView.setPlayerId(playerId);

			return true;
		}

	}
}
