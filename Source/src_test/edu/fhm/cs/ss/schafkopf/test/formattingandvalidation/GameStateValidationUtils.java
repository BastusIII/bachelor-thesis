package edu.fhm.cs.ss.schafkopf.test.formattingandvalidation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.StockIndex;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This is a class with only static validation methods, specialized to detect invalid game states.<br>
 * <br>
 *
 * Each method needs the game data to validate as parameter.
 *
 * @author Sebastian Stumpf
 *
 */
public class GameStateValidationUtils {
	/**
	 * Validate the critical game state values.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateCriticalValues(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		if (gameData == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Game data must never be null.");
			return;
		}
		if (!gameData.equals(new GameData(gameData))) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Copy constructor is not working properly, gamestate != copy(gamestate).");
		}
		// general checks independent of other values
		if (gameData.getActionBuffer() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Action buffer must never be null.");
		}
		if (gameData.getGamesFirstPlayerPosition() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Games first player must never be null.");
		}
		if (gameData.getGameState() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Gamestate must never be null.");
		}
		if (gameData.getLastExecutedAction() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Last executed action must never be null.");
		}
		if (gameData.getPlayerOnTurnPosition() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player on turn position must never be null.");
		}
		if (gameData.getRoundsFirstPlayerPosition() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player on turn position must never be null.");
		}
		if (gameData.getOpponentTeam() == null || gameData.getOpponentTeam().size() > 3) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Opponent team must never be null or size > 3.");
		}
		if (gameData.getPlayerTeam() == null || gameData.getPlayerTeam().size() > 2) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player team must never be null or size > 2.");
		}
		if (gameData.getRoundNumber() < -1 || gameData.getRoundNumber() > 8) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Round Number must never be < 0 or > 8");
		}
		if (gameData.getStack() == null || gameData.getStack().size() > 32) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Stack must never be null or size > 32.");
		}
		if (gameData.getStock() < 0 || gameData.getStock() % 4 != 0 || gameData.getStock() % gameData.getGameSettings().getBasicCharge() != 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE,
					"Stock must never be < 0 and must be a multiple of 4 and a multiple of the basic charge defined in settings.");
		}
		// validation of last rounds played cards
		if (gameData.getLastRoundsPlayedCards() == null || gameData.getLastRoundsPlayedCards().size() > 4) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Last round played cards must not be null or size > 4.");
		}
		// player data
		if (gameData.getPlayerDatas() == null || gameData.getPlayerDatas().size() != 4) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data map must not be null or size != 4.");
		} else {
			for (final IPlayerData playerData : gameData.getPlayerDatas().values()) {
				if (playerData == null) {
					testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data must never be null.");
				} else {
					if (playerData.getCurrentHand() == null || playerData.getCurrentHand().size() > 8) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> current hand must never be null or size > 8.");
					}
					if (playerData.getInitialHand() == null) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> initial hand must never be null or size > 8.");
					}
					if (playerData.getName() == null) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> name must never be null.");
					}
					if (playerData.getPoints() < 0) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> points must never be < 0.");
					}
					if (playerData.getPosition() == null) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> position must never be null.");
					}
					if (playerData.getSizeOfHand() < 0) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> size of hand must never return a negative value.");
					}
					if (playerData.getWonCards() == null || playerData.getWonCards().size() > 32) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> won cards must never be null or size > 32.");
					}
					if (playerData.isStriking() && playerData.isStrikingBack()) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Player data -> strike and strike back must not be true both.");
					}
				}
			}
		}
		// validation of settings
		if (gameData.getGameSettings() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings must never be null.");
		} else {
			if (gameData.getGameSettings().getFilename() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> filename must never be null.");
			}
			if (gameData.getGameSettings().getBasicCharge() <= 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> basic charge must never <= 0.");
			}
			if (gameData.getGameSettings().getNameBottom() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> name bottom must never be null.");
			}
			if (gameData.getGameSettings().getNameLeft() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> name left must never be null.");
			}
			if (gameData.getGameSettings().getNameRight() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> name right must never be null.");
			}
			if (gameData.getGameSettings().getNameTop() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> name top must never be null.");
			}
			if (gameData.getGameSettings().getSoloMultiplier() <= 1) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> solo multiplier must never be <= 1.");
			}
			if (gameData.getGameSettings().getStartMoney() <= 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Settings -> start money must never be <= 0");
			}
		}

		// validation of charge
		if (gameData.getCharge() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge must never be null.");
		} else {
			if (gameData.getCharge().getBasic() < 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> basic must never be < 0.");
			}
			// maximum high cards in row is 14 in a partner game
			if (gameData.getCharge().getBounty() < 0 || gameData.getCharge().getBounty() > 14) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> bounty must never be < 0 or > 14.");
			}
			// maximum initial multiplier is 2^1 -> 1
			if (gameData.getCharge().getExclusiveMultiplier() < 0 || gameData.getCharge().getExclusiveMultiplier() > 1) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> exclusive multiplier must never be < 0 or > 1.");
			}
			// maximum initial multiplier is 2^4 -> 4
			if (gameData.getCharge().getInitialMultiplier() < 0 || gameData.getCharge().getInitialMultiplier() > 4) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> exclusive multiplier must never be < 0 or > 4.");
			}
			// no schneider = 0, schneider = 1 , schneider schwarz = 2
			if (gameData.getCharge().getSchneider() < 0 || gameData.getCharge().getSchneider() > 2) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> exclusive multiplier must never be < 0 or > 2.");
			}
			if (gameData.getCharge().getStockIndex() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> stock index must never be null.");
			}
			// stock value that has to be paid in a game per player is always a
			// multiple of the basis value in the game data settings
			if (gameData.getCharge().getStockValue() < 0 || gameData.getCharge().getStockValue() % gameData.getGameSettings().getBasicCharge() != 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> stock value must never be < 0 and must be a of the basic charge defined in settings.");
			}
			// strike multiplier max is one strike and one strikeback, so 2^2 ->
			// 2
			if (gameData.getCharge().getStrikeMultiplier() < 0 || gameData.getCharge().getStrikeMultiplier() > 2) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> strike multiplier must never be < 0 or > 2.");
			}
			// strike multiplier max is one strike and one strikeback, so 2*2 =
			// 4
			if (gameData.getCharge().getTotalCharge() < 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Charge -> total charge must never be < 0.");
			}
		}

		// validation of executed action
		if (gameData.getLastExecutedAction() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Last executed action must never be null.");
		} else {
			if (gameData.getLastExecutedAction().getExecutingPlayerPosition() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action -> position must never be null.");
			}
			if (gameData.getLastExecutedAction().getValidationCode() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action -> validation code must never be null.");
			}
			if (gameData.getLastExecutedAction().getActionType() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action -> action type must never be null.");
			} else {
				if (gameData.getLastExecutedAction().getActionType().equals(ActionType.CHOOSE)) {
					if (gameData.getLastExecutedAction().getChosenGame() == null) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action | choose -> chosen game must be null.");
					} else {
						if (gameData.getLastExecutedAction().getChosenGame().getGameType() == null) {
							testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action | choose | gametype must not be null.");
						} else {
							switch (gameData.getLastExecutedAction().getChosenGame().getGameType()) {
								case FARBWENZ:
								case FARBWENZ_TOUT:
								case SOLO:
								case SOLO_TOUT:
								case SAUSPIEL:
									if (gameData.getLastExecutedAction().getChosenGame().getGameType() == null) {
										testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action | choose | color must not be null for gametype "
												+ gameData.getLastExecutedAction().getChosenGame().getGameType() + ".");
									}
									break;
								default:
									break;
							}
						}
					}
				} else if (gameData.getLastExecutedAction().getActionType().equals(ActionType.PLAY)) {
					if (gameData.getLastExecutedAction().getPlayedCard() == null) {
						testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Executed action | play | card must not be null.");
					}
				}
			}
		}

		// return if validation was not successfull for previous checks
		if (testValidationInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_CRITICAL_INVALID_STATE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Upper critical invalid states found in \n" + FormattingUtils.generateGameDataString(gameData));
		}
	}

	/**
	 * Validate the non critical game state values.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateNonCriticalValues(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		switch (gameData.getGameState()) {
			case CHOOSE:
				if (!gameData.getStack().isEmpty()) {
					testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Stack must be empty in game state " + gameData.getGameState() + ".");
				}
				validateNonCriticalValuesGameNotStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameNotFinished(testValidationInfo, gameData);
				break;
			case GET_RAISE:
				if (gameData.getStack().isEmpty()) {
					testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Stack must not be empty in game state " + gameData.getGameState() + ".");
				}
				if (gameData.getColor() != null) {
					testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Color must be null in game state " + gameData.getGameState() + ".");
				}
				if (gameData.getLeadPlayerPosition() != null && !gameData.getGameType().equals(GameType.PASS)) {
					testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Lead player position must be null in game state " + gameData.getGameState() + ".");
				}
				validateNonCriticalValuesGameNotStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameNotFinished(testValidationInfo, gameData);
				break;
			case PLAY:
				validateNonCriticalValuesGameStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameNotFinished(testValidationInfo, gameData);
				break;
			case STRIKE:
				validateNonCriticalValuesGameStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameNotFinished(testValidationInfo, gameData);
				break;
			case STRIKEBACK:
				validateNonCriticalValuesGameStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameNotFinished(testValidationInfo, gameData);
				break;
			case FINISHED:
				validateNonCriticalValuesGameStarted(testValidationInfo, gameData);
				validateNonCriticalValuesGameFinished(testValidationInfo, gameData);
				break;
			default:
				break;
		}

		// player and opponent team must not have values in common
		if (!Collections.disjoint(gameData.getPlayerTeam(), gameData.getOpponentTeam())) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Player and opponent team must not have values in common.");
		}

		// this is a critical error when the game has been started.
		if (gameData.getGameType() == null && gameData.getRoundNumber() >= 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_CRITICAL_INVALID_STATE, "Game type must never be null in game state " + gameData.getGameState() + ".");
		}

		// return if validation was not successfull for previous checks
		if (testValidationInfo.getTotalValidationCode().equals(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE,
					"Upper non critical invalid states found in \n" + FormattingUtils.generateGameDataString(gameData));
		}
	}

	/**
	 * Validate the non critical game state values for a finished game.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateNonCriticalValuesGameFinished(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		if (gameData.getGameType().equals(GameType.PASS)) {
			if (gameData.getCharge().getTotalCharge() != 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> total charge must be 0 in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
			if (!gameData.getCharge().getStockIndex().equals(StockIndex.PAY_IN)) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> stock index must be pay in in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
			if (gameData.getCharge().getStockValue() == 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> stock value must not be 0 in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
			if (!gameData.getPlayerTeam().isEmpty() || !gameData.getOpponentTeam().isEmpty()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE,
						"Teams must be empty in game state " + gameData.getGameState() + " for game type" + gameData.getGameType());
			}
			if (gameData.getWinnerTeam() != null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> winner team must be null in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
		} else {
			if (gameData.getCharge().getTotalCharge() == 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> total charge must not be 0 in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
			if (gameData.getWinnerTeam() == null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> winner team must not be null in game state " + gameData.getGameState()
						+ " for game type " + gameData.getGameType());
			}
		}
		// partner game
		if (gameData.getGameType().isPartnerGame && gameData.getCharge().getBasic() != gameData.getGameSettings().getBasicCharge()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> basic must be = basic charge as defined in the game settings for game type "
					+ gameData.getGameType() + ".");
		}
		// exclusive
		if (gameData.getGameType().isExclusive && gameData.getCharge().getExclusiveMultiplier() != 1) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> exclusive multiplier must be = 1 for game type " + gameData.getGameType() + ".");
		}
		// solo
		if (!gameData.getGameType().isPartnerGame && !gameData.getGameType().equals(GameType.PASS)
				&& gameData.getCharge().getBasic() != gameData.getGameSettings().getBasicCharge() * gameData.getGameSettings().getSoloMultiplier()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE,
					"Charge -> basic must be = basic charge * solo multiplier as defined in the game settings for game type " + gameData.getGameType() + ".");
		}
	}

	/**
	 * Validate the non critical game state values for a not yet finished game.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateNonCriticalValuesGameNotFinished(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		if (gameData.getCharge().getBounty() > 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> bounty must be = 0 in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getCharge().getSchneider() > 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> schneider must be = 0 in game state " + gameData.getGameState() + ".");
		}
		if (!gameData.getCharge().getStockIndex().equals(StockIndex.IGNORE)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE,
					"Charge -> stock index must be ignore, because this is default in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getCharge().getStockValue() > 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> stock value must be < 0 in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getCharge().getTotalCharge() > 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> total charge must be < 0 in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getWinnerTeam() != null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Winner team must be null in game state " + gameData.getGameState() + ".");
		}
	}

	/**
	 * Validate the non critical game state values for a not yet started game.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateNonCriticalValuesGameNotStarted(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		if (!gameData.getGamesFirstPlayerPosition().equals(gameData.getRoundsFirstPlayerPosition())) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Games first and rounds first player position must be equal when no cards have been played yet.");
		}
		if (!gameData.getLastRoundsPlayedCards().isEmpty()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Last rounds played cards must be empty in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getLastRoundsWinner() != null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Last rounds winner must be null in game state " + gameData.getGameState() + ".");
		}
		if (!gameData.getOpponentTeam().isEmpty() || !gameData.getPlayerTeam().isEmpty()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Teams must be empty in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getRoundNumber() != -1) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Round number must be -1 in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getCharge().getStrikeMultiplier() > 0) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Charge -> strike multiplier must be > 0 in game state " + gameData.getGameState() + ".");
		}
		for (final IPlayerData playerData : gameData.getPlayerDatas().values()) {
			if (playerData.getPlayedCard() != null) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Player -> played card must be null in game state " + gameData.getGameState() + ".");
			}
			if (playerData.getPoints() > 0) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Player -> points must be 0 in game state " + gameData.getGameState() + ".");
			}
			if (!playerData.getWonCards().isEmpty()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Player -> won cards must be empty in game state " + gameData.getGameState() + ".");
			}
			if (playerData.isStriking() || playerData.isStrikingBack()) {
				testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Player -> must not be striking or striking back in game state " + gameData.getGameState()
						+ ".");
			}
		}

		// check if no card is contained twice and the number of all cards is 32
		final List<ICard> allPlayedCards = new LinkedList<ICard>();
		final List<ICard> allRemainingCards = new LinkedList<ICard>();
		allRemainingCards.addAll(gameData.getStack());
		for (final IPlayerData playerData : gameData.getPlayerDatas().values()) {
			allPlayedCards.addAll(playerData.getCurrentHand());
		}
		if (!Collections.disjoint(allPlayedCards, allRemainingCards)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "A card is contained in both the played cards and the remaining cards.");
		}
		allPlayedCards.addAll(allRemainingCards);
		if (allPlayedCards.size() != 32) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "The total number of cards is not  = 32, but " + allPlayedCards.size() + ".");
		}
	}

	/**
	 * Validate the non critical game state values for a started game.
	 *
	 * @param testValidationInfo
	 *            errors will be appended.
	 * @param gameData
	 *            the game data to check.
	 */
	public static void validateNonCriticalValuesGameStarted(final ITestValidationInfo testValidationInfo, final IGameData gameData) {

		if (!gameData.getStack().isEmpty()) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "The stack must not contain cards in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getGameType().equals(GameType.PASS) && !gameData.getGameState().equals(GameState.FINISHED)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "The game type pass is not allowed in game state " + gameData.getGameState() + ".");
		}
		if (gameData.getGameType().needsColor && gameData.getColor() == null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "The game type " + gameData.getGameType() + " needs a color.");
		} else if (!gameData.getGameType().needsColor && gameData.getColor() != null) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "For game type " + gameData.getGameType() + " color must not be set.");
		}

		// color -> game type dependency when game started
		if (gameData.getGameType().equals(GameType.SAUSPIEL) && gameData.getColor().equals(CardColor.HERZ)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "Card Color must not be herz for game type " + gameData.getGameType() + ".");
		}
		// check if no card is contained twice and the number of all cards is 32
		final List<ICard> allPlayedCards = new LinkedList<ICard>();
		final List<ICard> allRemainingCards = new LinkedList<ICard>();
		// when the game started
		for (final IPlayerData playerData : gameData.getPlayerDatas().values()) {
			allPlayedCards.addAll(playerData.getWonCards());
			if (playerData.getPlayedCard() != null) {
				allPlayedCards.add(playerData.getPlayedCard());
			}
			allRemainingCards.addAll(playerData.getCurrentHand());
		}
		if (!Collections.disjoint(allPlayedCards, allRemainingCards)) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "A card is contained in both the played cards and the remaining cards.");
		}
		allPlayedCards.addAll(allRemainingCards);
		if (allPlayedCards.size() != 32) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_NON_CRITICAL_INVALID_STATE, "The total number of cards is not  = 32, but " + allPlayedCards.size() + ".");
		}
	}
}
