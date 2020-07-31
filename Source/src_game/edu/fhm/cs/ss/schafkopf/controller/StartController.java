package edu.fhm.cs.ss.schafkopf.controller;

import edu.fhm.cs.ss.schafkopf.ai.sets.random.RandomAI;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.IController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.IGameController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.ISettingsController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.IStartController;
import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.GameSettings;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.AutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.ConsolePlayerView;
import edu.fhm.cs.ss.schafkopf.view.ConsoleSettingsView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IStartView;

/**
 * The Start controller has to interact with the {@link IStartView} and navigate to the other views of this application according to its interactions.<br>
 * <br>
 *
 * Behaviour of this implementation:<br>
 * - {@link #changeSettings()} load stored settings with the persistence handler, if none are stored create new settings. Then start the settings view. Upper
 * controller is set to this controller.<br>
 * - {@link #newGame()} create new game data instance, one interactive (currently {@link ConsolePlayerView}) view and 3 Autonomous Views (currently
 * {@link AutonomousPlayerView} + {@link RandomAI}) and start a new game with these. <br>
 * - {@link #resumeGame()} same as {@link #newGame()} but with loading the stored game data instance before and starting the game with this. If no stored data
 * is found, return false and don't start a game.
 *
 * @author Sebastian Stumpf
 *
 */
public class StartController extends BaseController implements IStartController {
	/**
	 * Instantiate the controller with only a persistence handler, other values are set to null.
	 *
	 * @param persistenceHandler
	 *            the persistence handler.
	 */
	public StartController(final IPersistenceHandler persistenceHandler) {

		this(persistenceHandler, null);
	}

	/**
	 * Instantiate the controller with the given parameters.
	 *
	 * @param persistenceHandler
	 *            the persistence handler.
	 * @param upperController
	 *            the upper controller. As the start view is the entry view, there should be none.
	 *
	 */
	public StartController(final IPersistenceHandler persistenceHandler, final IController upperController) {

		super(persistenceHandler, upperController);
	}

	@Override
	public void changeSettings() {

		getView().stop();
		final ISettingsController settingsController = new SettingsController(getPersistenceHandler(), this);
		new ConsoleSettingsView(settingsController);
		settingsController.start();
	}

	@Override
	public void newGame() {

		IGameSettings settings;
		if (isPersisting()) {
			settings = (IGameSettings) getPersistenceHandler().load(IGameSettings.FILENAME);
			if (settings == null) {
				settings = new GameSettings();
			}
		} else {
			settings = new GameSettings();
		}

		final IGameData gameData = new GameData(settings, false);

		final IGameController gameController = new GameController(getPersistenceHandler(), gameData, this);

		// console player is subscribed first, so he is always at bottom
		final IPlayerView consolePlayer = new ConsolePlayerView(gameController);
		// create the ai players at the remaining free positions
		new AutonomousPlayerView(gameController, new RandomAI(null));
		new AutonomousPlayerView(gameController, new RandomAI(null));
		new AutonomousPlayerView(gameController, new RandomAI(null));

		// initialize the gameData with the console players position
		final FullAccessGameUtils utils = new FullAccessGameUtils(gameData);
		utils.initializeGameData(consolePlayer.getPlayerId().getPosition());

		// persist the created game
		getPersistenceHandler().persist(gameData.getPersistenceObject());

		// stop the view connected to this controller
		getView().stop();
		// start the game controller, this starts all player views
		gameController.start();

	}

	@Override
	public boolean resumeGame() {

		if (!isPersisting()) {
			return false;
		}
		final GameData gameData = (GameData) getPersistenceHandler().load(IGameData.FILENAME);
		if (gameData == null) {
			// return false, if no game could be loaded
			return false;
		}
		// set the game to resumed.
		gameData.setResumed(true);
		// create game controller with the loaded game data
		final IGameController gameController = new GameController(getPersistenceHandler(), gameData, this);

		// console player is subscribed first, so he is always at bottom
		new ConsolePlayerView(gameController);
		// create the ai players at the remaining free positions
		new AutonomousPlayerView(gameController, new RandomAI(null));
		new AutonomousPlayerView(gameController, new RandomAI(null));
		new AutonomousPlayerView(gameController, new RandomAI(null));

		// game must not be initialized here, because we do not want to reset the loaded gamedata

		getView().stop();
		gameController.start();
		return true;

	}
}
