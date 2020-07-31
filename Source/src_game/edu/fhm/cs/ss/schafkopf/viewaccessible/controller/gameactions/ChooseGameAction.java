package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.BasicGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IChooseGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This action implements the execution and validation of a player choosing a game.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class ChooseGameAction extends BaseAction implements IChooseGameAction {

	/**
	 * The players chosen game.
	 */
	private final IBasicGameData chosenGame;

	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 * @param chosenGameType
	 *            the game type of the chosen game.
	 * @param chosenCardColor
	 *            the color of the chosen game.
	 */
	public ChooseGameAction(final IPlayerId playerId, final GameType chosenGameType, final CardColor chosenCardColor) {

		this(playerId, new BasicGameData(chosenGameType, chosenCardColor));
	}

	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 * @param chosenGame
	 *            the chosen game.
	 */
	public ChooseGameAction(final IPlayerId playerId, final IBasicGameData chosenGame) {

		super(playerId);
		this.chosenGame = chosenGame;
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);
		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			utils.getPovPlayerData().setChosenGame(getChosenGame());
			if (getChosenGame().getGameType().dominates(gameData.getGameType())) {
				utils.getGameData().setGameType(getChosenGame().getGameType());
				utils.getGameData().setLeadPlayerPosition(utils.getPovPlayerData().getPosition());
			}
			if (utils.haveAllPlayersChosen()) {
				utils.startGameManipulations();
			}
			// set next player position
			utils.getGameData().setPlayerOnTurnPosition(utils.getGameData().getPlayerOnTurnPosition().getNext());
			// set the color to null because this information must not be seen by all players
			final IBasicGameData restrictedChosenGame = new BasicGameData(getChosenGame().getGameType(), null);
			utils.getGameData().setLastExecutedAction(new ActionData(ActionType.CHOOSE, utils.getPovPlayerData().getPosition(), restrictedChosenGame, null, false));
			retVal = ActionValidationCode.EXECUTED_CHANGES;
		}

		return retVal;

	}

	@Override
	public IBasicGameData getChosenGame() {

		return chosenGame;
	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getChooseGameValidationCode(playerUtils.getPovPlayerData(), chosenGame);
	}
}
