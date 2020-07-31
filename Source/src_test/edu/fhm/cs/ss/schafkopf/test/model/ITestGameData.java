package edu.fhm.cs.ss.schafkopf.test.model;

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
 * Test data is offering methods for all the information needed to validate a test game, to replay it under the same preconditions, or to print further
 * information.
 *
 * @author Sebastian Stumpf
 *
 */
public interface ITestGameData {

	/**
	 * @return the accept state, the AI instances were initialized with.
	 */
	GameState getAcceptRestartState();

	/**
	 * @return the list of all actions, executed or refused.
	 */
	List<IActionData> getActionList();

	/**
	 * @return the AI classes, the autonomous player views were initialized with.
	 */
	Map<PlayerPosition, Class<? extends IAI>> getAis();

	/**
	 * @return all executed actions that are important for the determinism test.
	 */
	List<IActionData> getDeterministicActionlist();

	/**
	 * @return the filename of the test case.
	 */
	String getFileName();

	/**
	 * @return the list of game data states, as the occurred during the game.
	 */
	List<IGameData> getGamesList();

	/**
	 * @return the stack the game was initialized with.
	 */
	List<ICard> getInitialStack();

	/**
	 * @return the player view classes, the game was started with.
	 */
	Map<PlayerPosition, Class<? extends IPlayerView>> getPlayers();

	/**
	 * @return the list of raise actions, important for the determinism test.
	 */
	List<IActionData> getRaiseActionlist();

}