package edu.fhm.cs.ss.schafkopf.test.settings;

import edu.fhm.cs.ss.schafkopf.ai.sets.simpledeterministic.SimpleDeterministicAI;
import edu.fhm.cs.ss.schafkopf.view.AutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.ConsolePlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IAutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IInteractivePlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * This is a static class, holding the settings used in the test environment.
 *
 * @author Sebastian Stumpf
 *
 */
public final class TestSettings {

	// folder and data paths
	/** The file separator for this system. */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/** The relative path of the data folder. All test information should be stored here. */
	public static final String TEST_FOLDER_RELATIVE_PATH = "data" + FILE_SEPARATOR + "testing";
	/** The relative path of the stack feed folder. All stack feeds should be stored here. */
	public static final String STACK_FEED_FOLDER_RELATIVE_PATH = TEST_FOLDER_RELATIVE_PATH + FILE_SEPARATOR + "stackfeeds";
	/** The relative path of the test case folder. All persisted test cases should be stored here. */
	public static final String TESTCASE_FOLDER_RELATIVE_PATH = TEST_FOLDER_RELATIVE_PATH + FILE_SEPARATOR + "testcases";
	/** The relative path of the error replays folder. The replayed test cases with errors in the determination test will be stored here. */
	public static final String DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH = TESTCASE_FOLDER_RELATIVE_PATH + FILE_SEPARATOR + "error_replay";
	/** The relative path of the game settings for test games. They are used when starting new test games. */
	public static final String GAME_SETTINGS_RELATIVE_PATH = TEST_FOLDER_RELATIVE_PATH + FILE_SEPARATOR + "TEST_GAME_SETTINGS";
	/** The stack feed that is loaded when starting the test application in the test controller. */
	public static final String STACK_FEED_TO_LOAD = "STACK_FEED.xml";

	// settings for new test cases
	/** The position of the first player in test games. */
	public static final PlayerPosition FIRST_PLAYER_POSITION = PlayerPosition.BOTTOM;
	/** The position of the console player in test games. */
	public static final PlayerPosition CONSOLE_PLAYER_POSITION = PlayerPosition.BOTTOM;
	/** The accept state of created AI's. */
	public static final GameState ACCEPT_RESTART_STATE = GameState.FINISHED;
	/** The created interactive views. */
	public static final Class<? extends IInteractivePlayerView> INTERACTIVE_VIEW = ConsolePlayerView.class;
	/** The created autonomous views. */
	public static final Class<? extends IAutonomousPlayerView> AUTONOMOUS_VIEW = AutonomousPlayerView.class;
	/** The AI, autonomous views are initialized with. */
	public static final Class<? extends IAI> AI = SimpleDeterministicAI.class;

	// settings for automated tests
	/** The timeout used to detect deadlocks in tests without interactive views. */
	public static final long TIMEOUT = 2500;
	/** The number of stress test games created. */
	public static final int STRESSTEST_GAMES = 12000;
	/** The number of custom stress test games created. */
	public static final int CUSTOM_STRESSTEST_GAMES = 5000;

	// settings for package validation
	/** The folder, the files will be checked in. */
	public static final String RESTRICTED_FILES_FOLDER_RELATIVE_PATH = "src_game" + FILE_SEPARATOR + "edu" + FILE_SEPARATOR + "fhm" + FILE_SEPARATOR + "cs" + FILE_SEPARATOR + "ss" + FILE_SEPARATOR
			+ "schafkopf" + FILE_SEPARATOR + "view";
	/** The prefix that will be further examined in the package validation test. */
	public static final String PREFIX_IMPORT_TO_CHECK = "edu.fhm.cs.ss.schafkopf";
	/** The allowed suffixes for the files to come directly after the defined prefix. */
	public static final String[] ALLOWED_IMPORT_SUFFIXES = { "view_accessible", "view" };

}
