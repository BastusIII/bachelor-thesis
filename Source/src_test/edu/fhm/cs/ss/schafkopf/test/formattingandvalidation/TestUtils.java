package edu.fhm.cs.ss.schafkopf.test.formattingandvalidation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import edu.fhm.cs.ss.schafkopf.ai.sets.random.RandomAI;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.StackHandler;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.AutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IAutonomousPlayerView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IPlayerView;
import edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces.IAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This is a class with only static methods, it offers functionality needed in the test environment.<br>
 * <br>
 * The offered methods are combining validation calls or implementing other functionality, that has nothing to do with game data validation.
 *
 * @author Sebastian Stumpf
 *
 */
public class TestUtils {

	/**
	 * Returns a map of Players defined by the {@link TestSettings}.
	 *
	 * @param testValidationInfo
	 *            errors are set here.
	 * @param interactive
	 *            true, if a interactive player should be added.
	 *
	 * @return null if errors occurred, else the generated player map.
	 */
	public static Map<PlayerPosition, IPlayerView> getCustomTestPlayers(final ITestValidationInfo testValidationInfo, final boolean interactive) {

		final Map<PlayerPosition, IPlayerView> generatedPlayerViews = new EnumMap<>(PlayerPosition.class);
		PlayerPosition currentPosition = TestSettings.CONSOLE_PLAYER_POSITION;

		try {
			if (interactive) {
				generatedPlayerViews.put(currentPosition, TestSettings.INTERACTIVE_VIEW.getConstructor().newInstance());
				currentPosition = currentPosition.getNext();
			}
			do {
				final IAutonomousPlayerView aiPlayer = TestSettings.AUTONOMOUS_VIEW.getConstructor().newInstance();
				aiPlayer.setAi(TestSettings.AI.getConstructor(GameState.class).newInstance(TestSettings.ACCEPT_RESTART_STATE));
				generatedPlayerViews.put(currentPosition, aiPlayer);
				currentPosition = currentPosition.getNext();
			} while (!currentPosition.equals(TestSettings.FIRST_PLAYER_POSITION));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			generatedPlayerViews.clear();
			testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Generating Views from settings failed.\n" + e.getMessage());
		}

		return generatedPlayerViews;
	}

	/**
	 * Creates a map of players defined by the given parameters. This is used in the determination test to create the same players as in a test data for the
	 * replay.
	 *
	 * @param execInfo
	 *            the test validation info to append errors to.
	 * @param players
	 *            the player views classes.
	 * @param ais
	 *            the AI classes.
	 * @param acceptRestartState
	 *            the accept state.
	 * @return the map of generated players.
	 */
	public static Map<PlayerPosition, IPlayerView> getCustomTestPlayers(final ITestValidationInfo execInfo, final Map<PlayerPosition, Class<? extends IPlayerView>> players,
			final Map<PlayerPosition, Class<? extends IAI>> ais, final GameState acceptRestartState) {

		final Map<PlayerPosition, IPlayerView> generatedPlayerViews = new EnumMap<>(PlayerPosition.class);

		try {
			for (final PlayerPosition pos : players.keySet()) {
				final IAutonomousPlayerView aiPlayer = (IAutonomousPlayerView) players.get(pos).getConstructor().newInstance();
				aiPlayer.setAi(ais.get(pos).getConstructor(GameState.class).newInstance(acceptRestartState));
				generatedPlayerViews.put(pos, aiPlayer);
			}
		} catch (ClassCastException | NullPointerException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			execInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Could not create Players with " + players + " " + ais + "\nReason:" + e.getMessage());
			return null;
		}

		return generatedPlayerViews;
	}

	/**
	 * @return a variety of forbidden game type arrays.
	 */
	public static GameType[][] getForbiddenGameTypesArray() {

		// test with a variety of forbidden game types, the ones with higher
		// priority are forbidden more often. Si can be statistically never
		// played, so it is never forbidden here
		final GameType[] test1ForbiddenGameTypes = { GameType.FARBWENZ_TOUT, GameType.WENZ_TOUT, GameType.SOLO_TOUT, GameType.FARBWENZ, GameType.WENZ, GameType.SOLO };
		final GameType[] test2ForbiddenGameTypes = { GameType.FARBWENZ_TOUT, GameType.WENZ_TOUT, GameType.SOLO_TOUT, GameType.FARBWENZ, GameType.WENZ };
		final GameType[] test3ForbiddenGameTypes = { GameType.FARBWENZ_TOUT, GameType.WENZ_TOUT, GameType.SOLO_TOUT, GameType.FARBWENZ, GameType.SOLO };
		final GameType[] test4ForbiddenGameTypes = { GameType.FARBWENZ_TOUT, GameType.WENZ_TOUT, GameType.SOLO_TOUT, GameType.WENZ, GameType.SOLO };
		final GameType[] test5ForbiddenGameTypes = { GameType.FARBWENZ, GameType.WENZ, GameType.SOLO };
		final GameType[] test6ForbiddenGameTypes = { GameType.FARBWENZ_TOUT, GameType.WENZ_TOUT, GameType.SOLO_TOUT };
		final GameType[][] forbiddenGameTypes = { test1ForbiddenGameTypes, test2ForbiddenGameTypes, test3ForbiddenGameTypes, test4ForbiddenGameTypes, test5ForbiddenGameTypes, test6ForbiddenGameTypes };
		return forbiddenGameTypes;
	}

	/**
	 * Generate a map of player views initialized with {@link RandomAI}.
	 *
	 * @param restartState
	 *            the restart state of the AI's.
	 * @param randomAiForbiddenGameTypes
	 *            the array of forbidden game types that won't be chosen by the AI's.
	 * @return the map of random AI players.
	 */
	public static Map<PlayerPosition, IPlayerView> getRandomTestPlayers(final GameState restartState, final GameType[] randomAiForbiddenGameTypes) {

		final Map<PlayerPosition, IPlayerView> retVal = new EnumMap<PlayerPosition, IPlayerView>(PlayerPosition.class);
		for (final PlayerPosition position : PlayerPosition.values()) {
			retVal.put(position, new AutonomousPlayerView(null, new RandomAI(restartState, randomAiForbiddenGameTypes)));
		}
		return retVal;
	}

	/**
	 * Load the stacks from the given stack feed path.
	 *
	 * @param testValidationInfo
	 *            the test validation info to append errors to.
	 * @param stackFeedPath
	 *            the stack feed path.
	 * @return the list of loaded stacks.
	 */
	public static List<List<ICard>> loadStacks(final ITestValidationInfo testValidationInfo, final String stackFeedPath) {

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final List<List<ICard>> retVal = new ArrayList<>();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(stackFeedPath));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Stack Feed at " + stackFeedPath + " could not be loaded.");
			return null;
		}
		if (document == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Stack Feed at " + stackFeedPath + " could not be loaded.");
			return null;
		}
		removeWhitespaceNodes(document);
		final NodeList stackNodes = document.getElementsByTagName("stack");

		for (int i = 0; i < stackNodes.getLength(); ++i) {
			final NodeList handNodes = stackNodes.item(i).getChildNodes();
			List<ICard> loadedStack = null;
			try {
				loadedStack = StackHandler.getInstance().getPredefinedStack(handNodes.item(0).getTextContent().split(","), handNodes.item(1).getTextContent().split(","),
						handNodes.item(2).getTextContent().split(","), handNodes.item(3).getTextContent().split(","));
			} catch (final NullPointerException e) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Stack Feed at " + stackFeedPath + " content could not be read.");
				return null;
			}
			if (loadedStack == null || loadedStack.isEmpty()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Stack at index " + i + " could not be properly loaded.");
			} else {
				retVal.add(loadedStack);
			}

		}
		return retVal;
	}

	/**
	 * Validate the action execution on a current game state by forwarding it to the specialized method defined in {@link ActionValidationUtils} dependent of
	 * the last action's type.
	 *
	 * @param testValidationInfo
	 *            the test validation info to append errors to.
	 * @param previousState
	 *            the previous game data.
	 * @param currentState
	 *            the current game data.
	 */
	public static void validateActionExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		switch (currentState.getLastExecutedAction().getActionType()) {
			case CHOOSE:
				ActionValidationUtils.validateChooseExecution(testValidationInfo, previousState, currentState);
				break;
			case GET:
				ActionValidationUtils.validateGetExecution(testValidationInfo, previousState, currentState);
				break;
			case NEXTGAMEACCEPTED:
				ActionValidationUtils.validateNextGameAcceptedExecution(testValidationInfo, previousState, currentState);
				break;
			case NEXTGAMESTARTED:
				ActionValidationUtils.validateNextGameStartedExecution(testValidationInfo, previousState, currentState);
				break;
			case PLAY:
				ActionValidationUtils.validatePlayExecution(testValidationInfo, previousState, currentState);
				break;
			case RAISE:
				ActionValidationUtils.validateRaiseExecution(testValidationInfo, previousState, currentState);
				break;
			case STRIKE:
				ActionValidationUtils.validateStrikeExecution(testValidationInfo, previousState, currentState);
				break;
			case STRIKEBACK:
				ActionValidationUtils.validateStrikeBackExecution(testValidationInfo, previousState, currentState);
				break;
			default:
				break;

		}
		// return if validation was not successfull for previous checks
		if (testValidationInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION)) {
			testValidationInfo.appendInformation(
					TestValidationCode.ERROR_INVALID_ACTION_EXECUTION,
					"Upper invalid action executions found from \n\nprevious game state \n\n" + FormattingUtils.generateGameDataString(previousState) + "\nto current game state\n\n"
							+ FormattingUtils.generateGameDataString(currentState));
		}
	}

	/**
	 * Check a game state is valid.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateGameState(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		GameStateValidationUtils.validateCriticalValues(testValidationInfo, gameData);
		if (!testValidationInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_CRITICAL_INVALID_STATE)) {
			// non critical values are only checked, if no critical errors occur
			GameStateValidationUtils.validateNonCriticalValues(testValidationInfo, gameData);
		} else {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Critical invalid states occur in the gamedata, please solve before going on.");
		}
	}

	/**
	 * remove white space nodes from a loaded XML-document.
	 *
	 * @param document
	 *            the document.
	 */
	private static void removeWhitespaceNodes(final Document document) {

		final NodeList children = document.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			final Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
				document.removeChild(child);
			} else if (child instanceof Element) {
				removeWhitespaceNodes((Element) child);
			}
		}
	}

	/**
	 * Remove white space nodes from a given XML-element.
	 *
	 * @param element
	 *            the element.
	 */
	private static void removeWhitespaceNodes(final Element element) {

		final NodeList children = element.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			final Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
				element.removeChild(child);
			} else if (child instanceof Element) {
				removeWhitespaceNodes((Element) child);
			}
		}
	}
}
