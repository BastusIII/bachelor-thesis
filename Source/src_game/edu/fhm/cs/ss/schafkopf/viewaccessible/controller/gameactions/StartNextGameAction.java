package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStartNextGameAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This action implements the execution and validation of a player accepting the next game start.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class StartNextGameAction extends BaseAction implements IStartNextGameAction {
	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 */
	public StartNextGameAction(final IPlayerId playerId) {

		super(playerId);
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			utils.getPovPlayerData().setAcceptingNextGameStart(true);
			boolean startNextGame = true;
			for (final IPlayerData playerData : gameData.getPlayerDatas().values()) {
				if (!playerData.isAcceptingNextGameStart()) {
					startNextGame = false;
					break;
				}
			}
			if (startNextGame) {
				utils.initializeGameData(gameData.getGamesFirstPlayerPosition().getNext());
				utils.getGameData().setLastExecutedAction(new ActionData(ActionType.NEXTGAMESTARTED, utils.getPovPlayerData().getPosition(), null, null, false));
			} else {
				utils.getGameData().setLastExecutedAction(new ActionData(ActionType.NEXTGAMEACCEPTED, utils.getPovPlayerData().getPosition(), null, null, false));
			}
			retVal = ActionValidationCode.EXECUTED_CHANGES;

		}

		return retVal;

	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		// if the player is already accepting the restart, the action is not
		// executed again.
		return playerUtils.getPovPlayerData().isAcceptingNextGameStart() ? ActionValidationCode.STARTNEXT_ALREADYACCEPTING : ActionValidationCode.VALIDATION_SUCCESS;
	}
}
