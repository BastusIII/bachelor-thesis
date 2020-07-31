package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStrikeBackAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This action implements the execution and validation of a player requesting to strike back.
 *
 * @author Sebastian Stumpf
 *
 */
public class StrikeBackAction extends BaseAction implements IStrikeBackAction {

	/**
	 * True if the player is striking back.
	 */
	private final boolean strikingBack;

	/**
	 * Instantiate the action with the given parameters.
	 *
	 * @param playerId
	 *            the player ID.
	 * @param strikingBack
	 *            true, if the player wants to strike back.
	 */
	public StrikeBackAction(final IPlayerId playerId, final boolean strikingBack) {

		super(playerId);
		this.strikingBack = strikingBack;
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			if (strikingBack) {
				utils.getPovPlayerData().setStrikingBack(strikingBack);
				utils.getGameData().getCharge().setStrikeMultiplier(utils.getGameData().getCharge().getStrikeMultiplier() + 1);
				// to prevent releasing too much information, only if player
				// wants
				// to strike, his action is saved,
				utils.getGameData().setLastExecutedAction(new ActionData(ActionType.STRIKEBACK, utils.getPovPlayerData().getPosition(), null, null, strikingBack));
			}
			retVal = ActionValidationCode.EXECUTED_CHANGES;
			// because only the player is allowed to strike back, after one
			// incoming
			// strike back, the game is resumed and status set to play
			utils.getGameData().setGameState(GameState.PLAY);
		}
		return retVal;

	}

	@Override
	public boolean isStrikingBack() {

		return strikingBack;
	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getStrikeBackValidationCode(playerUtils.getPovPlayerData());
	}

}
