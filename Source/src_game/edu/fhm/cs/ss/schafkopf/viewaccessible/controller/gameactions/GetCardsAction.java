package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IGetCardsAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This action implements the execution and validation of a player asking for cards.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class GetCardsAction extends BaseAction implements IGetCardsAction {
	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 */
	public GetCardsAction(final IPlayerId playerId) {

		super(playerId);
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			utils.dealCardsFromStack(utils.getPovPlayerData(), IRestrictedGameData.DRAWN_CARDS_PER_ACTION);
			// set next player on turn
			utils.getGameData().setPlayerOnTurnPosition(utils.getGameData().getPlayerOnTurnPosition().getNext());

			// all cards are handed out -> players can start choosing their game
			if (utils.getGameData().getStack().isEmpty()) {
				utils.getGameData().setGameState(GameState.CHOOSE);
			}
			utils.getGameData().setLastExecutedAction(new ActionData(ActionType.GET, utils.getPovPlayerData().getPosition(), null, null, false));
			retVal = ActionValidationCode.EXECUTED_CHANGES;
		}

		return retVal;

	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getGetCardsValidationCode(playerUtils.getPovPlayerData());
	}
}
