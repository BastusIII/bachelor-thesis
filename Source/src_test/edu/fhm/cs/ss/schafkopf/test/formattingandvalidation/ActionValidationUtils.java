package edu.fhm.cs.ss.schafkopf.test.formattingandvalidation;

import java.util.EnumMap;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This is a class with only static validation methods, specialized to detect errors concerning action validation and execution.<br>
 * <br>
 * Thus each method needs two game states, the previous and the current, to validate if the changes in the game data were correct and if the action was allowed
 * to be executed on the previous state.
 *
 * @author Sebastian Stumpf
 *
 */
public class ActionValidationUtils {

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Choose action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Choose action.
	 */
	public static void validateChooseExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final IActionData executedAction = currentState.getLastExecutedAction();
		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		final IBasicGameData chosenGame = currentState.getLastExecutedAction().getChosenGame();

		// invalid execution states
		if (!previousState.getGameState().equals(GameState.CHOOSE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (currentState.getGameState().equals(GameState.STRIKEBACK) || currentState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		if (executingPosition != previousPlayerData.getPosition()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not on turn.");
		}

		// invalid required changes
		if (!chosenGame.getGameType().equals(currentPlayerData.getChosenGame().getGameType())) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
					+ " invalid change : chosen game type in executed action data must be set to restricted version of the players chosen game type when the choose round is not finished yet.");
		}

		if (chosenGame.getGameType().dominates(previousState.getGameType())) {
			if (!currentState.getGameType().equals(currentState.getPlayerDatas().get(currentState.getLeadPlayerPosition()).getChosenGame().getGameType())) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
						+ " invalid change : current game type must be set to the actions gametype.");
			}

		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setPlayerOnTurnPosition(previousState.getPlayerOnTurnPosition());
		consistentGameData.setRoundNumber(previousState.getRoundNumber());
		consistentGameData.setGameType(previousState.getGameType());
		consistentGameData.setColor(previousState.getColor());
		consistentGameData.setLeadPlayerPosition(previousState.getLeadPlayerPosition());
		consistentGameData.setCharge(previousState.getCharge());
		// choose may sort cards
		consistentGameData.setPlayerDatas(previousState.getPlayerDatas());
		consistentGameData.setPlayerTeam(previousState.getPlayerTeam());
		consistentGameData.setOpponentTeam(previousState.getOpponentTeam());
		// if all players pass the game is finished charge/stock is changed
		if (currentState.getGameState().equals(GameState.FINISHED)) {
			consistentGameData.setStock(previousState.getStock());
		}
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}

	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Get action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Get action.
	 */
	public static void validateGetExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final IActionData executedAction = currentState.getLastExecutedAction();
		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		// invalid execution states
		if (!previousState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (currentState.getGameState().equals(GameState.FINISHED) || currentState.getGameState().equals(GameState.STRIKEBACK) || currentState.getGameState().equals(GameState.PLAY)
				|| currentState.getGameState().equals(GameState.STRIKE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		if (previousPlayerData.getCurrentHand().size() > IRestrictedGameData.MAX_HAND_SIZE - IRestrictedGameData.DRAWN_CARDS_PER_ACTION) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : hand size > "
					+ (IRestrictedGameData.MAX_HAND_SIZE - IRestrictedGameData.DRAWN_CARDS_PER_ACTION) + ".");
		}
		if (executingPosition != previousPlayerData.getPosition()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not on turn.");
		}

		// invalid required changes
		if (currentPlayerData.getCurrentHand().size() != previousPlayerData.getCurrentHand().size() + IRestrictedGameData.DRAWN_CARDS_PER_ACTION) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : hand size must increase by "
					+ IRestrictedGameData.DRAWN_CARDS_PER_ACTION + ".");
		}
		if (currentState.getStack().size() != previousState.getStack().size() - IRestrictedGameData.DRAWN_CARDS_PER_ACTION) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : stack size must decrease by "
					+ IRestrictedGameData.DRAWN_CARDS_PER_ACTION + ".");
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setPlayerOnTurnPosition(previousState.getPlayerOnTurnPosition());
		consistentGameData.setStack(previousState.getStack());
		consistentGameData.getPlayerDatas().put(executingPosition, previousPlayerData);
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a NextGameAccepted action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a NextGameAccepted action.
	 */
	public static void validateNextGameAcceptedExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final IActionData executedAction = currentState.getLastExecutedAction();
		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		// invalid execution states -> none

		// invalid following states -> none

		// invalid preconditions
		// strike and strike back can be jumped over and thus not be represented as the last executed action though changes occur
		if (previousState.getGameState().equals(GameState.STRIKE) || previousState.getGameState().equals(GameState.STRIKEBACK)) {
			if (!currentState.getLastExecutedAction().equals(executedAction)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
						+ " invalid precondition : action must not change if strike or strike back was not executed.");
			}
		} else {
			if (previousPlayerData.isAcceptingNextGameStart()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : already accepting next game start.");
			}
		}

		// invalid required changes
		if (!currentPlayerData.isAcceptingNextGameStart()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : player must be set to striking back.");
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.getPlayerDatas().get(executingPosition).setAcceptingNextGameStart(previousPlayerData.isAcceptingNextGameStart());
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a NextGameStarted action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a NextGameStarted action.
	 */
	public static void validateNextGameStartedExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IActionData executedAction = currentState.getLastExecutedAction();

		// invalid execution states -> none

		// invalid following states -> none

		// invalid preconditions
		for (final IPlayerData playerData : previousState.getPlayerDatas().values()) {
			if (!playerData.isAcceptingNextGameStart() && playerData.getPosition() != executingPosition) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not all players accept restart.");
			}
		}

		// invalid required changes
		if (!currentState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not all players accept restart.");
		}

		// invalid changes of consistent values
		// next game start initialized nearly everything, is not checked here

	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Play action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Play action.
	 */
	public static void validatePlayExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final IActionData executedAction = currentState.getLastExecutedAction();
		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		final ICard playedCard = currentState.getLastExecutedAction().getPlayedCard();
		final Map<PlayerPosition, ICard> previousCardsOnTable = new EnumMap<PlayerPosition, ICard>(PlayerPosition.class);
		final Map<PlayerPosition, ICard> currentCardsOnTable = new EnumMap<PlayerPosition, ICard>(PlayerPosition.class);
		for (final IPlayerData playerData : previousState.getPlayerDatas().values()) {
			if (playerData.getPlayedCard() != null) {
				previousCardsOnTable.put(playerData.getPosition(), playerData.getPlayedCard());
			}
		}
		for (final IPlayerData playerData : currentState.getPlayerDatas().values()) {
			if (playerData.getPlayedCard() != null) {
				currentCardsOnTable.put(playerData.getPosition(), playerData.getPlayedCard());
			}
		}
		final boolean roundFinished = previousCardsOnTable.size() == 3;

		// invalid execution states
		if (previousState.getGameState().equals(GameState.FINISHED) || previousState.getGameState().equals(GameState.CHOOSE) || previousState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (currentState.getGameState().equals(GameState.CHOOSE) || currentState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		if (previousState.getGameState().equals(GameState.STRIKE) || previousState.getGameState().equals(GameState.STRIKEBACK)) {
			if (!currentState.getLastExecutedAction().equals(executedAction)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
						+ " invalid precondition : action must not change if strike or strike back was not executed.");
			}
		} else {
			if (executingPosition != previousPlayerData.getPosition()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not on turn.");
			}
			if (previousPlayerData.getPlayedCard() != null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : already played card.");
			}
			if (previousPlayerData.getCurrentHand().isEmpty()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : current hand must not be empty.");
			}
			if (!previousPlayerData.getCurrentHand().contains(playedCard)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
						+ " invalid precondition : current hand must contain played card.");
			}
		}
		// TODO: the logic of serving could be checked here, but this is going quite into detail

		// invalid required changes
		if (roundFinished) {
			if (!currentCardsOnTable.isEmpty()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : all played cards must be resettet.");
			}
			if (currentState.getRoundNumber() != previousState.getRoundNumber() + 1) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : round number must increment.");
			}
			// add the played card to the stich
			previousCardsOnTable.put(executingPosition, playedCard);
			if (!currentState.getLastRoundsPlayedCards().equals(previousCardsOnTable)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : last rounds played cards are invalid.");
			}
			if (!currentState.getPlayerDatas().get(currentState.getLastRoundsWinner()).getWonCards().containsAll(previousCardsOnTable.values())) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType()
						+ " invalid change : the winner of the last round must get all the played cards to hin on cards collection.");
			}
		} else {
			if (!currentState.getPlayerOnTurnPosition().equals(previousState.getPlayerOnTurnPosition().getNext()) && previousState.getGameState().equals(GameState.PLAY)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : player on turn position must increase.");
			}
			if (!playedCard.equals(currentPlayerData.getPlayedCard())) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : played card must be set to player.");
			}
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setOpponentTeam(previousState.getOpponentTeam());
		consistentGameData.setPlayerOnTurnPosition(previousState.getPlayerOnTurnPosition());
		consistentGameData.setPlayerTeam(previousState.getPlayerTeam());
		if (currentState.getGameState().equals(GameState.FINISHED)) {
			consistentGameData.setCharge(previousState.getCharge());
			consistentGameData.setWinnerTeam(previousState.getWinnerTeam());
			consistentGameData.setStock(previousState.getStock());
		}
		if (roundFinished) {
			consistentGameData.setRoundNumber(previousState.getRoundNumber());
			consistentGameData.setRoundsFirstPlayerPosition(previousState.getRoundsFirstPlayerPosition());
			consistentGameData.setLastRoundsPlayedCards(previousState.getLastRoundsPlayedCards());
			consistentGameData.setLastRoundsWinner(previousState.getLastRoundsWinner());
			consistentGameData.setPlayerDatas(previousState.getPlayerDatas());
		} else {
			consistentGameData.getPlayerDatas().put(executingPosition, previousPlayerData);
		}
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Raise action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Raise action.
	 */
	public static void validateRaiseExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final IActionData executedAction = currentState.getLastExecutedAction();
		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		// invalid execution states
		if (!previousState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (!currentState.getGameState().equals(GameState.GET_RAISE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		if (previousPlayerData.getCurrentHand().size() > IRestrictedGameData.MAX_RAISING_HAND_SIZE) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : hand size > "
					+ IRestrictedGameData.MAX_RAISING_HAND_SIZE + ".");
		}
		if (previousPlayerData.isRaising()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : already raising.");
		}

		// invalid required changes
		if (currentPlayerData.isRaising() != true) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : player must be set to raising.");
		}
		if (currentState.getCharge().getInitialMultiplier() != previousState.getCharge().getInitialMultiplier() + 1) {
			testValidationInfo
					.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : raise multiplier in charge must increase by 1.");
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setCharge(previousState.getCharge());
		consistentGameData.getPlayerDatas().put(executingPosition, previousPlayerData);
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Strike Back action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Strike Back action.
	 */
	public static void validateStrikeBackExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IActionData executedAction = currentState.getLastExecutedAction();

		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		// invalid execution states
		if (!(previousState.getGameState().equals(GameState.STRIKE) || previousState.getGameState().equals(GameState.PLAY) || previousState.getGameState().equals(GameState.STRIKEBACK))) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (currentState.getGameState().equals(GameState.FINISHED) || currentState.getGameState().equals(GameState.GET_RAISE) || currentState.getGameState().equals(GameState.STRIKE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		if (previousPlayerData.isStrikingBack()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : already striking back.");
		}
		if (!executingPosition.equals(previousState.getLeadPlayerPosition())) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : not lead player.");
		}
		if (!executedAction.getBoolean()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : actions boolean not true.");
		}

		// invalid required changes
		if (currentPlayerData.isStrikingBack() != executedAction.getBoolean()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : player must be set to striking back.");
		}
		if (currentState.getCharge().getStrikeMultiplier() != 2) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : strike multiplier in charge must be 2.");
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.setCharge(previousState.getCharge());
		consistentGameData.getPlayerDatas().put(executingPosition, previousPlayerData);
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}

	/**
	 * Validate if last executed action in currentState was properly executed.
	 *
	 * This method expects the last executed action set in currentGameState to be a Strike action.
	 *
	 * @param testValidationInfo
	 *            the execution information to append errors to.
	 * @param previousState
	 *            the previous game state the action was executed on.
	 * @param currentState
	 *            the current game state.
	 * @throws NullPointerException
	 *             if the game states are critically invalid or the last executed action is not a Strike action.
	 */
	public static void validateStrikeExecution(final ITestValidationInfo testValidationInfo, final IGameData previousState, final IGameData currentState) {

		final PlayerPosition executingPosition = currentState.getLastExecutedAction().getExecutingPlayerPosition();
		final IActionData executedAction = currentState.getLastExecutedAction();

		final IPlayerData previousPlayerData = previousState.getPlayerDatas().get(executingPosition);
		final IPlayerData currentPlayerData = currentState.getPlayerDatas().get(executingPosition);

		// invalid execution states
		if (!(previousState.getGameState().equals(GameState.STRIKE) || previousState.getGameState().equals(GameState.PLAY) || previousState.getGameState().equals(GameState.STRIKEBACK))) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid execution state: " + previousState.getGameState() + ".");
		}

		// invalid following states
		if (currentState.getGameState().equals(GameState.FINISHED) || currentState.getGameState().equals(GameState.GET_RAISE) || currentState.getGameState().equals(GameState.STRIKE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid following state : " + currentState.getGameState() + ".");
		}

		// invalid preconditions
		// strike and strike back can be jumped over and thus not be represented as the last executed action though changes occur
		if (!(previousState.getGameState().equals(GameState.STRIKE) || previousState.getGameState().equals(GameState.STRIKEBACK))) {
			if (previousPlayerData.isAcceptingNextGameStart()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : already striking.");
			}
		}
		if (currentState.getPlayerTeam().contains(executingPosition)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : lead player must not strike back.");
		}
		if (!executedAction.getBoolean()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid precondition : actions boolean not true.");
		}

		// invalid required changes
		if (currentPlayerData.isStriking() != executedAction.getBoolean()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : player must be set to striking.");
		}
		if (currentState.getCharge().getStrikeMultiplier() != 1) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : strike multiplier in charge must be 1.");
		}

		// invalid changes of consistent values
		// set the data that can change from the previous state to a copy of the current change and then check if both are equal
		final IGameData consistentGameData = new GameData(currentState);
		consistentGameData.setActionBuffer(previousState.getActionBuffer());
		consistentGameData.setLastExecutedAction(previousState.getLastExecutedAction());
		consistentGameData.setGameState(previousState.getGameState());
		consistentGameData.setCharge(previousState.getCharge());
		consistentGameData.getPlayerDatas().put(executingPosition, previousPlayerData);
		if (!previousState.equals(consistentGameData)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_INVALID_ACTION_EXECUTION, executedAction.getActionType() + " invalid change : consistent values in game data have changed.");
		}
	}
}
