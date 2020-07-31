package edu.fhm.cs.ss.schafkopf.test.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This data class holds all the information needed to validate a test game, to replay it under the same preconditions, or to print further information.
 *
 * @author Sebastian Stumpf
 *
 */
public class TestGameData implements ITestGameData {
	/**
	 * The file name.
	 */
	private final String fileName;
	/**
	 * The accept state of the AI's.
	 */
	private final GameState acceptRestartState;
	/**
	 * The initial stack.
	 */
	private final List<ICard> initialStack;
	/**
	 * The used player view classes.
	 */
	private final Map<PlayerPosition, Class<? extends IPlayerView>> players = new EnumMap<>(PlayerPosition.class);
	/**
	 * The used AI classes.
	 */
	private final Map<PlayerPosition, Class<? extends IAI>> ais = new EnumMap<>(PlayerPosition.class);
	/**
	 * The list of game data states, as the occurred during the game.
	 */
	private final List<IGameData> gamesList = new ArrayList<IGameData>();
	/**
	 * The list of all actions.
	 */
	private final List<IActionData> actionList = new ArrayList<IActionData>();
	/**
	 * The list of all deterministic actions.
	 */
	private final List<IActionData> deterministicActionlist = new ArrayList<IActionData>();
	/**
	 * The list of all raise actions.
	 */
	private final List<IActionData> raiseActionlist = new ArrayList<IActionData>();

	/**
	 * Instantiate the test data with the given parameters.
	 *
	 * @param fileName
	 *            the file name.
	 * @param acceptRestartState
	 *            the accept state.
	 * @param initialStack
	 *            the initial stack.
	 */
	public TestGameData(final String fileName, final GameState acceptRestartState, final ArrayList<ICard> initialStack) {

		this.fileName = fileName;
		this.acceptRestartState = acceptRestartState;
		this.initialStack = initialStack;
	}

	@Override
	public GameState getAcceptRestartState() {

		return acceptRestartState;
	}

	@Override
	public List<IActionData> getActionList() {

		return actionList;
	}

	@Override
	public Map<PlayerPosition, Class<? extends IAI>> getAis() {

		return ais;
	}

	@Override
	public List<IActionData> getDeterministicActionlist() {

		return deterministicActionlist;
	}

	@Override
	public String getFileName() {

		return fileName;
	}

	@Override
	public List<IGameData> getGamesList() {

		return gamesList;
	}

	@Override
	public List<ICard> getInitialStack() {

		return initialStack;
	}

	@Override
	public Map<PlayerPosition, Class<? extends IPlayerView>> getPlayers() {

		return players;
	}

	@Override
	public List<IActionData> getRaiseActionlist() {

		return raiseActionlist;
	}

	@Override
	public String toString() {

		return fileName;
	}
}
