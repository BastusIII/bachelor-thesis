package edu.fhm.cs.ss.schafkopf.test.controller;

import edu.fhm.cs.ss.schafkopf.ai.sets.random.RandomAI;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.AutonomousPlayerView;

/**
 * The test controller offers methods that will start the various tests and options, with a minimum of parameters.<br>
 * <br>
 *
 * The test calls are as simple as possible, most of the used settings can be edited in {@link TestSettings}. All the long tests are blocking method calls they
 * do not return before all the started test games have finished.
 *
 * @author Sebastian Stumpf
 *
 */
public interface ITestController {

	/**
	 * This test checks the loaded test cases for determination.<br>
	 * They will be replayed with exact the same preconditions, by the same player views and AI's. Afterwards, the action list of the replay and the action list
	 * of the loaded test case are compared. They have to be equal, otherwise this test will print a warning to the console, that will display more information
	 * about the compared test cases, like the action list of both and a simple overview of players cards in each round. The replays, determination errors
	 * occurred in, are persisted in {@link TestSettings#DETERMINISM_ERROR_REPLAY_RELATIVE_FOLDER_PATH}.
	 */
	void aiDeterminationTest();

	/**
	 * This test will generate a lot of games, the exact number is defined in {@link TestSettings#CUSTOM_STRESSTEST_GAMES}, and start them with the AI Views and
	 * AIs defined in {@link TestSettings#AUTONOMOUS_VIEW} and {@link TestSettings#AI}. All occurring errors are displayed to the console. <br>
	 * The test can be used to check if the tested AI's are causing problems, especially deadlocks. If the {@link #stressTestRandomAi()} runs through without
	 * causing problems - this means there are no errors in the controller logic - and this test causes errors, this is a hint on malfunctions in the tested
	 * views or AIs.
	 */
	void customStressTest();

	/**
	 * This is not a test, but an option to generate test cases that can be loaded and used for a {@link #aiDeterminationTest()} afterwards.<br>
	 * The loaded stack feed - which one can be edited in {@link TestSettings#STACK_FEED_TO_LOAD} - will be used to generate stacks for the test games. One test
	 * game is started for each stack found in the stack feed.<br>
	 * The generated test cases are persisted in the given folder, and the prefix prepended to their name. This test may cause general errors, if problems with
	 * saving occur.<br>
	 * The generated test cases are validated on the fly, if validation errors occur during the processing of this method, it will be cancelled. Invalid test
	 * cases are not persisted, the errors have to be solved first.<br>
	 * The path, the given directory will be created in, is defined in {@link TestSettings#TESTCASE_FOLDER_RELATIVE_PATH}.
	 *
	 * @param dirName
	 *            the directory name, the generated tests should be saved in. If the directory does not exist, it will be created.
	 * @param fileprefix
	 *            the file prefix for the generated test cases.
	 */
	void generateTestCasesWithCustomStacks(String dirName, String fileprefix);

	/**
	 * This method is working in the same way as {@link #generateTestCasesWithCustomStacks(String, String)}, but uses random stacks for the generated test
	 * games. The number of games is given as a parameter.
	 *
	 * @see #generateTestCasesWithCustomStacks(String, String)
	 * @param dirName
	 *            the directory name, the generated tests should be saved in. If the directory does not exist, it will be created.
	 * @param number
	 *            the number of created test games.
	 * @param fileprefix
	 *            the file prefix for the generated test cases.
	 */
	void generateTestCasesWithRandomStacks(String dirName, int number, String fileprefix);

	/**
	 * This is also an option and loads all test cases found in the given directory. These can then be used for the {@link #aiDeterminationTest()}.<br>
	 * The path, the given directory has to be in, is defined in {@link TestSettings#TESTCASE_FOLDER_RELATIVE_PATH}.
	 *
	 * @param dirName
	 *            the directory name the tests should be loaded from.
	 */
	void loadAllTestCases(String dirName);

	/**
	 * Package validation checks the application for invalid imports.<br>
	 * Invalid imports are those, that access to packages, they do not have access to. The imports that will be checked can be defined in
	 * {@link TestSettings#PREFIX_IMPORT_TO_CHECK}, the allowed import suffixes coming directly after the prefix in {@link TestSettings#ALLOWED_IMPORT_SUFFIXES}
	 * .<br>
	 * The directory that will be tested is defined in {@link TestSettings#RESTRICTED_FILES_FOLDER_RELATIVE_PATH}. All files in that directory will be checked.
	 */
	void packageAccessValidation();

	/**
	 * This is an interactive test, the user has the possibility to play a test game against 3 AI enemies.<br>
	 * The used AI view and AI are the same as in {@link #customStressTest()} and defined in the settings. The game will be validated while played, errors are
	 * printed to the console after it is finished.<br>
	 * The game will be started with the stack from the loaded stack feed at the given index.
	 *
	 * @param index
	 *            the index of the stack in the stack feed.
	 */
	void playAndValidateInteractiveGameVsAiWithCustomStack(int index);

	/**
	 * This test is basically the same as {@link #playAndValidateInteractiveGameVsAiWithCustomStack(int)}, but with a random stack.
	 *
	 * @see #playAndValidateInteractiveGameVsAiWithCustomStack(int)
	 */
	void playAndValidateInteractiveGameVsAiWithRandomStack();

	/**
	 * Prints all filenames of the loaded test cases to the console.
	 */
	void printLoadedTestCaseFilenames();

	/**
	 * Prints a persisted test case on the console, step by step. This is especially useful, if all information of a failing determination replay from the
	 * {@link #aiDeterminationTest()} should be displayed.
	 *
	 * @param dirName
	 *            the directory name.
	 * @param testCaseFilename
	 *            the test case filename.
	 */
	void printTestCase(String dirName, String testCaseFilename);

	/**
	 * This test is basically the same as {@link #customStressTest()}, but the AI's used for validation are tested on the current implementation and do not
	 * cause errors.<br>
	 * The used AI view is {@link AutonomousPlayerView} and the AI a {@link RandomAI}. The AI is instantiated with a variety of forbidden game types and accept
	 * states, to assure a high coverage of different test games.<br>
	 * If errors in this test occur, the reason is a invalid change in the game controllers logic. Values are manipulated in a way, it wasn't planned. This
	 * should be fixed if it happened unconsciously.<br>
	 * Only if the developer is really sure he added a new feature, that does not cause problems and needs to be the game data changed the way it is, he can
	 * update the validation test that causes the error.<br>
	 * The number of test can be edited in {@link TestSettings#STRESSTEST_GAMES}.
	 *
	 * Errors in this test means that the current implementation of the game logic contains errors.
	 */
	void stressTestRandomAi();

	/**
	 * This is a callback method for the test game controller, all the test games are started with. It should not be called by the test application, only by the
	 * test game controller.
	 *
	 * @param testValidationInfo
	 *            the test validation info containing the test data and the error messages.
	 */
	void testFeedBack(ITestValidationInfo testValidationInfo);

}