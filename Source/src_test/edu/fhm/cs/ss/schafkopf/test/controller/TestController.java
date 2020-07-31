package edu.fhm.cs.ss.schafkopf.test.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.utilities.XMLFilePersistenceHandler;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.test.formattingandvalidation.FormattingUtils;
import edu.fhm.cs.ss.schafkopf.test.formattingandvalidation.PackageValidation;
import edu.fhm.cs.ss.schafkopf.test.formattingandvalidation.TestUtils;
import edu.fhm.cs.ss.schafkopf.test.model.ITestGameData;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestGameData;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.recording.ITestGameController;
import edu.fhm.cs.ss.schafkopf.test.recording.TestGameController;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This class implements the tests and options the user directly interacts with in the test environment.
 *
 * @author Sebastian Stumpf
 *
 */
public class TestController implements ITestController {
	/**
	 * Maximum of displayed actions.
	 */
	public static final int MAX_ACTIONS_DISPLAYED = 1000;
	/**
	 * Maximum of displayed created files.
	 */
	public static final int MAX_CREATED_FILES_DISPLAYED = 20;
	/**
	 * Monitor to synchronize feedbacks from the test game controller.
	 */
	private final Object feedbackMonitor;
	/**
	 * The test game settings. The settings to load can be edited in {@link TestSettings#GAME_SETTINGS_RELATIVE_PATH}.
	 */
	private final IGameSettings testGameSettings;
	/**
	 * Counter for feedbacks, used in {@link #waitForFeedbacks(ITestValidationInfo, boolean, long)} to block until all test games returned.
	 */
	private int feedBackCounter;
	/**
	 * The expected feedbacks, compared with {@link #feedBackCounter} to check if all feedbacks returned.
	 */
	private int expectedFeedBacks;
	/**
	 * The list of currently loaded test cases.
	 */
	private final List<TestGameData> loadedTestCaseList;
	/**
	 * The list of loaded custom stacks from the stack feed in {@link TestSettings#STACK_FEED_FOLDER_RELATIVE_PATH}.
	 */
	private List<List<ICard>> loadedStacks;
	/**
	 * The list of feedbacks. Each feedback returned from test game controller via {@link #testFeedBack(ITestValidationInfo)} is stored here.
	 */
	private final List<ITestValidationInfo> feedBackValidationInfoList;
	/**
	 * The total feedback code to be able to quickly see, if a test was successful. Edited in {@link #testFeedBack(ITestValidationInfo)}.
	 */
	private TestValidationCode totalFeedBackValidationCode;

	/**
	 * Instantiates a test controller with default values and empty lists.
	 */
	public TestController() {

		this.testGameSettings = (IGameSettings) new XMLFilePersistenceHandler("", "").load(TestSettings.GAME_SETTINGS_RELATIVE_PATH);
		this.loadedTestCaseList = new ArrayList<TestGameData>();
		this.feedBackValidationInfoList = new ArrayList<ITestValidationInfo>();
		this.loadedStacks = new ArrayList<List<ICard>>();
		this.feedbackMonitor = new Object();
		this.feedBackCounter = 0;
		this.expectedFeedBacks = 0;
	}

	@Override
	public void aiDeterminationTest() {

		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();

		final File file = new File(TestSettings.DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH);
		if (!file.isDirectory()) {
			if (!file.mkdir()) {
				execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Could not create directory at " + TestSettings.DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH);
				System.err.println(execInfo.getFurtherInformation());
				return;
			}
		}

		if (loadedTestCaseList.isEmpty()) {
			execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "No test cases loaded.");
			System.err.println(execInfo.getFurtherInformation());
			return;
		}
		for (final ITestGameData testCase : loadedTestCaseList) {
			this.feedBackValidationInfoList.clear();
			final Map<PlayerPosition, IPlayerView> players = TestUtils.getCustomTestPlayers(execInfo, testCase.getPlayers(), testCase.getAis(), testCase.getAcceptRestartState());
			if (execInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_GENERAL)) {
				break;
			}
			// creating players successful
			this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
			final List<ICard> stack = testCase.getInitialStack();
			final PlayerPosition firstPlayerPos = testCase.getGamesList().get(0).getGamesFirstPlayerPosition();

			// create a new test case replaying the loaded one and the compare these
			createSingleTestcase(stack, players, testCase + "_determinism_replay", firstPlayerPos);
			expectedFeedBacks = 1;
			waitForFeedbacks(execInfo, false, TestSettings.TIMEOUT);
			if (execInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_GENERAL)) {

			} else {
				if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
					break;
				}
				final ITestGameData replayTestCase = feedBackValidationInfoList.get(0).getTestCase();
				if (!replayTestCase.getDeterministicActionlist().equals(testCase.getDeterministicActionlist()) || !replayTestCase.getRaiseActionlist().containsAll(testCase.getRaiseActionlist())
						|| !testCase.getRaiseActionlist().containsAll(replayTestCase.getRaiseActionlist())) {
					execInfo.appendInformation(
							TestValidationCode.WARNING,
							"AI is not acting deterministic or its logic has been changed in " + testCase + "." + "\nold executed actions: "
									+ FormattingUtils.generateActionsString(testCase.getDeterministicActionlist(), MAX_ACTIONS_DISPLAYED) + "\nnew executed actions: "
									+ FormattingUtils.generateActionsString(replayTestCase.getDeterministicActionlist(), MAX_ACTIONS_DISPLAYED) + "\nold raises: "
									+ FormattingUtils.generateActionsString(testCase.getRaiseActionlist(), MAX_ACTIONS_DISPLAYED) + "\nnew raises: "
									+ FormattingUtils.generateActionsString(replayTestCase.getRaiseActionlist(), MAX_ACTIONS_DISPLAYED) + "\nold game progress\n"
									+ FormattingUtils.generateSimpleGameProgressString(testCase.getGamesList()) + "\nnew game progress\n"
									+ FormattingUtils.generateSimpleGameProgressString(replayTestCase.getGamesList()));
					// test cases with errors are persisted.
					persistTestCase(TestSettings.DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH, replayTestCase);
				}
			}
		}
		switch (execInfo.getTotalValidationCode()) {

			case ERROR_GENERAL:
				System.err.println("An unexpected error occurred, to go on please solve the problems first.");
				System.err.println(execInfo.getFurtherInformation());
				break;
			case ERROR_CRITICAL_INVALID_STATE:
			case ERROR_DEADLOCK:
			case ERROR_INVALID_ACTION_EXECUTION:
			case ERROR_NON_CRITICAL_INVALID_STATE:
				break;
			case WARNING:
				System.out.println("The determinism test finished with errors. You can have a look at the replays in " + TestSettings.DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH + ".");
				System.err.println(execInfo.getFurtherInformation());
				break;
			case SUCCESS:
				System.out.println("AI determination test successfully finished.");
				break;
			default:
				break;
		}
	}

	@Override
	public void customStressTest() {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();

		generateCustomTest(execInfo, null, TestSettings.CUSTOM_STRESSTEST_GAMES, false, "custom_stress_test");
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println(execInfo.getFurtherInformation());
		} else if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
			printErrorsInCurrentFeedbacks();
		} else {
			System.out.println("The stress test with custom ai players successfully finished.");
		}
	}

	@Override
	public void generateTestCasesWithCustomStacks(final String dirName, final String fileprefix) {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();
		loadStackList(execInfo);
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println(execInfo.getFurtherInformation());
			return;
		}
		if (dirName != null && !dirName.isEmpty()) {
			final File file = new File(TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + dirName);
			if (!file.isDirectory()) {
				if (!file.mkdir()) {
					execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Could not create directory at " + TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + dirName);
					System.err.println(execInfo.getFurtherInformation());
					return;
				}
			}
		}
		int index = 0;
		for (final List<ICard> stack : loadedStacks) {
			generateCustomTest(execInfo, stack, 1, false, fileprefix + "_testcase_custom_stack_game_" + index);
			index++;
			if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				break;
			}
		}
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println("Following errors occured generating the games, please solve errors before going on.");
			System.err.println(execInfo.getFurtherInformation());
		} else if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
			System.err.println("Following errors in testcases, please solve errors before going on.");
			printErrorsInCurrentFeedbacks();
		} else {
			persistTestCasesFromFeedbackList(dirName, execInfo);
			if (execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				if (feedBackValidationInfoList.size() > MAX_CREATED_FILES_DISPLAYED) {
					System.out.println("Successfully generated and persisted " + feedBackValidationInfoList.size() + " test cases.");
				} else {
					System.out.println("Successfully generated and persisted following test cases:");
					for (final ITestValidationInfo execInfo2 : feedBackValidationInfoList) {
						System.out.println(execInfo2.getTestCase());
					}
				}
			} else {
				System.err.println(execInfo.getFurtherInformation());
			}
		}
	}

	@Override
	public void generateTestCasesWithRandomStacks(final String dirName, final int number, final String fileprefix) {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();

		if (dirName != null && !dirName.isEmpty()) {
			final File file = new File(TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + dirName);
			if (!file.isDirectory()) {
				if (!file.mkdir()) {
					execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Could not create directory at " + TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + dirName);
					System.err.println(execInfo.getFurtherInformation());
					return;
				}
			}
		}

		generateCustomTest(execInfo, null, number, false, fileprefix + "_testcase_random_stack_game");
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println("Following errors occured generating the games, please solve errors before going on.");
			System.err.println(execInfo.getFurtherInformation());
		} else if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
			System.err.println("Following errors in testcases, please solve errors before going on.");
			printErrorsInCurrentFeedbacks();
		} else {
			persistTestCasesFromFeedbackList(dirName, execInfo);
			if (execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				if (feedBackValidationInfoList.size() > MAX_CREATED_FILES_DISPLAYED) {
					System.out.println("Successfully generated and persisted " + feedBackValidationInfoList.size() + " test cases.");
				} else {
					System.out.println("Successfully generated and persisted following test cases:");
					for (final ITestValidationInfo execInfo2 : feedBackValidationInfoList) {
						System.out.println(execInfo2.getTestCase());
					}
				}
			} else {
				System.err.println(execInfo.getFurtherInformation());
			}
		}
	}

	@Override
	public void loadAllTestCases(final String dirName) {

		loadedTestCaseList.clear();
		final ITestValidationInfo execInfo = new TestValidationInfo();

		final File[] files = new File(TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + (dirName != null && !dirName.isEmpty() ? TestSettings.FILE_SEPARATOR + dirName : "")).listFiles();
		for (final File file : files) {
			if (!file.isDirectory()) {
				addTestCaseToList(execInfo, dirName, file.getName());
			}
		}

		if (execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			if (loadedTestCaseList.size() <= MAX_CREATED_FILES_DISPLAYED) {
				System.out.println("Successfully loaded following test cases:");
				printLoadedTestCaseFilenames();
			} else {
				System.out.println("Successfully loaded " + loadedTestCaseList.size() + " test cases.");
			}
		} else {
			System.err.println(execInfo.getFurtherInformation());
		}
	}

	@Override
	public void packageAccessValidation() {

		final ITestValidationInfo testValidationInfo = new PackageValidation().run();
		if (testValidationInfo.getTotalValidationCode() == TestValidationCode.SUCCESS) {
			System.out.println("Package access validation test successfully finished.");
		} else {
			System.err.println(testValidationInfo.getFurtherInformation());
		}
	}

	@Override
	public void playAndValidateInteractiveGameVsAiWithCustomStack(final int index) {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();
		loadStackList(execInfo);
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println(execInfo.getFurtherInformation());
			return;
		}
		try {
			generateCustomTest(execInfo, loadedStacks.get(index), 1, true, "interactive_game_vs_ki");
			if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				System.err.println(execInfo.getFurtherInformation());
			} else if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
				printErrorsInCurrentFeedbacks();
			} else {
				System.out.println("Interactive game vs AI sucessfully finished.");
			}
		} catch (final IndexOutOfBoundsException e) {
			execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "There is no stack loaded at index " + index);
			System.err.println(execInfo.getFurtherInformation());
		}
	}

	@Override
	public void playAndValidateInteractiveGameVsAiWithRandomStack() {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();

		generateCustomTest(execInfo, null, 1, true, "interactive_game_vs_ki");
		if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			System.err.println(execInfo.getFurtherInformation());
		} else if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
			printErrorsInCurrentFeedbacks();
		} else {
			System.out.println("Interactive game vs AI sucessfully finished.");
		}
	}

	@Override
	public void printLoadedTestCaseFilenames() {

		for (final ITestGameData testcase : loadedTestCaseList) {
			System.out.println(testcase.getFileName());
		}
	}

	@Override
	public void printTestCase(final String dirName, final String testCaseFilename) {

		final ITestValidationInfo execInfo = new TestValidationInfo();
		final ITestGameData loaded = loadTestCase(execInfo, dirName, testCaseFilename);
		if (execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			for (final IGameData gameData : loaded.getGamesList()) {
				System.out.println(FormattingUtils.generateGameDataString(gameData));
			}
		} else {
			System.err.println(execInfo.getFurtherInformation());
		}
	}

	@Override
	public void stressTestRandomAi() {

		// reset variables
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;
		this.feedBackValidationInfoList.clear();

		final ITestValidationInfo execInfo = new TestValidationInfo();

		// test with variety of all restart game states once and then only with
		// finish a second time
		final GameState[] allAcceptableRestartStates = { GameState.FINISHED, GameState.CHOOSE, GameState.GET_RAISE, GameState.PLAY, GameState.STRIKE };
		// variety of forbidden game type arrays
		final GameType[][] forbiddenGameTypes = TestUtils.getForbiddenGameTypesArray();

		final int runsPerInnerLoop = TestSettings.STRESSTEST_GAMES / forbiddenGameTypes.length / 2;
		expectedFeedBacks = runsPerInnerLoop * forbiddenGameTypes.length * 2;
		for (int i = 0; i < forbiddenGameTypes.length; ++i) {
			// twice the games defined in the settings are generated per loop.
			for (int j = 0; j < runsPerInnerLoop; ++j) {
				// test a -> random acceptState
				createSingleTestcase(null, TestUtils.getRandomTestPlayers(allAcceptableRestartStates[(int) (Math.random() * allAcceptableRestartStates.length)], forbiddenGameTypes[i]), "stress_test_"
						+ i + "_a_" + j, TestSettings.FIRST_PLAYER_POSITION);
				// test b gameState.FINISHED accept state, games always run
				// through
				createSingleTestcase(null, TestUtils.getRandomTestPlayers(GameState.FINISHED, forbiddenGameTypes[i]), "stress_test_" + i + "_b_" + j, TestSettings.FIRST_PLAYER_POSITION);
			}
		}
		// by waiting twice the time of a automatical test timeout,
		// it is guaranteed, that all the unrestrictedGameObservers
		// have time to call the testCaseFeedback method.
		waitForFeedbacks(execInfo, false, TestSettings.TIMEOUT * 2);
		if (!totalFeedBackValidationCode.equals(TestValidationCode.SUCCESS)) {
			printErrorsInCurrentFeedbacks();
		} else {
			System.out.println("The stress test with random ai players successfully finished.");
		}
	}

	@Override
	public void testFeedBack(final ITestValidationInfo testValidationInfo) {

		synchronized (feedbackMonitor) {
			// testCase was not completed successfully
			feedBackCounter++;
			if (totalFeedBackValidationCode.priority < testValidationInfo.getTotalValidationCode().priority) {
				totalFeedBackValidationCode = testValidationInfo.getTotalValidationCode();
			}
			feedBackValidationInfoList.add(testValidationInfo);
			feedbackMonitor.notifyAll();
		}
	}

	/**
	 * Load a single test case
	 *
	 * @param execInfo
	 *            the execution info to append errors to.
	 * @param dirName
	 *            the directory name.
	 * @param testCaseFilename
	 *            the test case file name.
	 */
	private void addTestCaseToList(final ITestValidationInfo execInfo, final String dirName, final String testCaseFilename) {

		final TestGameData loaded = loadTestCase(execInfo, dirName, testCaseFilename);
		if (execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
			loadedTestCaseList.add(loaded);
		}

	}

	/**
	 * Creates a test with the given parameters.
	 *
	 * @param stack
	 *            the stack.
	 * @param players
	 *            the players.
	 * @param testCaseFilename
	 *            the test case filename.
	 * @param firstPlayerPosition
	 *            the game's first player position.
	 */
	private void createSingleTestcase(final List<ICard> stack, final Map<PlayerPosition, IPlayerView> players, final String testCaseFilename, final PlayerPosition firstPlayerPosition) {

		final ITestGameController gameController = new TestGameController(this, new GameData(testGameSettings, false), stack, players, firstPlayerPosition, testCaseFilename);
		gameController.start();
	}

	/**
	 * Generates a custom test, that can be started with various options defined by the parameters.
	 *
	 * @param execInfo
	 *            the execution info, the errors are appended to.
	 * @param stack
	 *            the stack. If it is null, the test game will be initialized with a random stack.
	 * @param runs
	 *            the number of test games to start.
	 * @param interactive
	 *            true if interactive. Interactive games have no timeout, so the user has unlimited time for his actions.
	 * @param fileNamePrefix
	 *            the file name prefix the test games are created with.
	 */
	private void generateCustomTest(final ITestValidationInfo execInfo, final List<ICard> stack, final int runs, final boolean interactive, final String fileNamePrefix) {

		// reset current execution code
		this.totalFeedBackValidationCode = TestValidationCode.SUCCESS;

		// for interactive games always one feedback is expected, because there is never running more than one game at the same time.
		expectedFeedBacks = runs;
		for (int i = 0; i < runs; ++i) {
			final Map<PlayerPosition, IPlayerView> customPlayers = TestUtils.getCustomTestPlayers(execInfo, interactive);
			if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				break;
			}
			createSingleTestcase(stack, customPlayers, fileNamePrefix + (runs > 1 ? "_" + i : ""), TestSettings.FIRST_PLAYER_POSITION);
			if (interactive) {
				expectedFeedBacks = 1;
				waitForFeedbacks(execInfo, interactive, 0);
			}
		}
		// by waiting twice the time of a automatical test timeout,
		// it is guaranteed, that all the unrestrictedGameObservers
		// have time to call the testCaseFeedback method.
		if (!interactive) {
			waitForFeedbacks(execInfo, interactive, TestSettings.TIMEOUT * 2);
		}
	}

	/**
	 * Fills the stack list with stacks from the stackfeed.<br>
	 *
	 * The feed is loaded from {@link TestSettings#STACK_FEED_FOLDER_RELATIVE_PATH} + {@link TestSettings#STACK_FEED_TO_LOAD}
	 *
	 * @param execInfo
	 *            the execution info, the errors are appended to.
	 */
	private void loadStackList(final ITestValidationInfo execInfo) {

		loadedStacks.clear();
		loadedStacks = TestUtils.loadStacks(execInfo, TestSettings.STACK_FEED_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + TestSettings.STACK_FEED_TO_LOAD);
	}

	/**
	 * Loads a single test case from the given directory with the given name. The given folder is searched in {@link TestSettings#TESTCASE_FOLDER_RELATIVE_PATH}
	 * .
	 *
	 * @param execInfo
	 *            the execution info, the errors are appended to.
	 * @param dirName
	 *            the directory name.
	 * @param testCaseFilename
	 *            the name of the test case to load.
	 * @return The loaded test data.
	 */
	private TestGameData loadTestCase(final ITestValidationInfo execInfo, final String dirName, final String testCaseFilename) {

		TestGameData loadedTestCase = null;
		final IPersistenceHandler persistenceHandler = new XMLFilePersistenceHandler("", TestSettings.TESTCASE_FOLDER_RELATIVE_PATH
				+ (dirName != null && !dirName.isEmpty() ? TestSettings.FILE_SEPARATOR + dirName : ""));
		try {
			loadedTestCase = (TestGameData) persistenceHandler.load(testCaseFilename);
		} catch (final ClassCastException e) {
			loadedTestCase = null;
		}
		if (loadedTestCase == null) {
			execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Testcase " + testCaseFilename + " could not be loaded.");
		}
		return loadedTestCase;
	}

	/**
	 * Persist a test case in the given path.
	 *
	 * @param path
	 *            the path to save the test case in.
	 * @param testCase
	 *            the test case.
	 * @return true if persisting was successful.
	 */
	private boolean persistTestCase(final String path, final ITestGameData testCase) {

		if (testCase == null) {
			return false;
		}
		return new XMLFilePersistenceHandler("", path).persist(testCase, testCase.getFileName());
	}

	/**
	 * Persist all test cases from feedback list.<br>
	 * If a test case could not be persisted, an error message is printed to the console. The given folder has to be in
	 * {@link TestSettings#TESTCASE_FOLDER_RELATIVE_PATH}.
	 *
	 * @param folderName
	 *            the folder to save the test case in, it has to exist.
	 * @param execInfo
	 *            the execution info to append errors to.
	 */
	private void persistTestCasesFromFeedbackList(final String folderName, final ITestValidationInfo execInfo) {

		synchronized (feedbackMonitor) {
			for (final ITestValidationInfo info : feedBackValidationInfoList) {
				if (!persistTestCase(TestSettings.TESTCASE_FOLDER_RELATIVE_PATH + (folderName != null && !folderName.isEmpty() ? TestSettings.FILE_SEPARATOR + folderName : ""), info.getTestCase())) {
					execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Could not save test case " + info.getTestCase() + " in " + TestSettings.TESTCASE_FOLDER_RELATIVE_PATH);
				}
			}
		}
	}

	/**
	 * Print the errors of all the feedbacks in the feedback list.
	 */
	private void printErrorsInCurrentFeedbacks() {

		for (final ITestValidationInfo execInfo : feedBackValidationInfoList) {
			if (!execInfo.getTotalValidationCode().equals(TestValidationCode.SUCCESS)) {
				System.out.println("\n\nErrors in: " + execInfo.getTestCase());
				System.err.println(execInfo.getFurtherInformation());
			}
		}
	}

	/**
	 * This is a blocking method, waiting for all the feedbacks from started test games to return.
	 *
	 * @param execInfo
	 *            the execution info to append errors to.
	 * @param interactive
	 *            if the game is interactive this method has no time out.
	 * @param timeOut
	 *            the time out is the maximum time between feedbacks, before an interrupted error message is appended to the execution ifor and the method
	 *            returns.
	 * @return true if all feedbacks returned without errors, else false.
	 */
	private boolean waitForFeedbacks(final ITestValidationInfo execInfo, final boolean interactive, final long timeOut) {

		try {
			// to check if the feedbackcounter still changes
			int previousFeedBacks = 0;
			synchronized (feedbackMonitor) {
				while (feedBackCounter < expectedFeedBacks) {
					if (interactive) {
						feedbackMonitor.wait();
					} else {
						feedbackMonitor.wait(timeOut);
					}
					// break if timed out -> feedBackCounter did not change
					if (previousFeedBacks == feedBackCounter) {
						break;
					}
					previousFeedBacks = feedBackCounter;
				}
				if (feedBackCounter < expectedFeedBacks) {
					execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Not all started tests finished. This should not happen, seems something is wrong in the test environment.");
					feedBackCounter = 0;
					return false;
				}
				feedBackCounter = 0;
			}
		} catch (final InterruptedException e) {
			execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Waiting for feedbacks was interrupted. This should not happen, seems something is wrong in the test environment.");
			return false;
		}
		expectedFeedBacks = 0;
		return true;
	}
}
