package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IStrikeAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This action implements the execution and validation of a player requesting to strike.
 *
 * @author Sebastian Stumpf
 *
 */
public class StrikeAction extends BaseAction implements IStrikeAction {

	/**
	 * True if the player is striking.
	 */
	private final boolean striking;

	/**
	 * Instantiate the action with the given parameters.
	 *
	 * @param playerId
	 *            the player ID.
	 * @param striking
	 *            true if the player wants to strike.
	 */
	public StrikeAction(final IPlayerId playerId, final boolean striking) {

		super(playerId);
		this.striking = striking;
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			gameData.getActionBuffer().put(getPosition(), new ActionData(retVal, ActionType.STRIKE, utils.getPovPlayerData().getPosition(), null, null, striking));

			// all strike action received
			if (utils.getGameData().getGameType().isPartnerGame && gameData.getActionBuffer().size() == 2 || gameData.getActionBuffer().size() == 3) {
				// get strike action with highest priority

				PlayerPosition position = gameData.getLeadPlayerPosition();
				IActionData strikeToExecute = null;
				do {
					position = position.getNext();
					if (gameData.getActionBuffer().get(position) != null && gameData.getActionBuffer().get(position).getBoolean()) {
						strikeToExecute = gameData.getActionBuffer().get(position);
						break;
					}
				} while (position != gameData.getLeadPlayerPosition());

				// somebody is striking so execute the strike
				if (strikeToExecute != null) {
					gameData.setGameState(GameState.STRIKEBACK);
					gameData.getPlayerDatas().get(strikeToExecute.getExecutingPlayerPosition()).setStriking(true);
					gameData.getCharge().setStrikeMultiplier(utils.getGameData().getCharge().getStrikeMultiplier() + 1);
					// to prevent releasing too much information, only if player
					// wants
					// to strike, his action is saved,
					gameData.setLastExecutedAction(new ActionData(ActionType.STRIKE, strikeToExecute.getExecutingPlayerPosition(), null, null, true));
				} else {
					gameData.setGameState(GameState.PLAY);
				}
				gameData.getActionBuffer().clear();
				retVal = ActionValidationCode.EXECUTED_CHANGES;
			}
		}

		return retVal;

	}

	@Override
	public boolean isStriking() {

		return striking;
	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getStrikeValidationCode(playerUtils.getPovPlayerData());
	}

}
