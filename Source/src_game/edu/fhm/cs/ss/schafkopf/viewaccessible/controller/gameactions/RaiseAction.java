package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IRaiseAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This action implements the execution and validation of a player wanting to raise.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class RaiseAction extends BaseAction implements IRaiseAction {
	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 */
	public RaiseAction(final IPlayerId playerId) {

		super(playerId);
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			utils.getPovPlayerData().setRaising(true);
			utils.getGameData().getCharge().setInitialMultiplier(utils.getGameData().getCharge().getInitialMultiplier() + 1);
			utils.getGameData().setLastExecutedAction(new ActionData(ActionType.RAISE, utils.getPovPlayerData().getPosition(), null, null, false));
			retVal = ActionValidationCode.EXECUTED_CHANGES;
		}

		return retVal;

	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getRaiseValidationCode(playerUtils.getPovPlayerData());
	}
}
