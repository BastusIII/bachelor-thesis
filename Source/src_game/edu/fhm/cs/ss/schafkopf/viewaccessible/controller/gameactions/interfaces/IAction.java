package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces;

import edu.fhm.cs.ss.schafkopf.model.GameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This interface has to be implemented by all actions and offers a validation and an execution method.<br>
 * <br>
 * 
 * So an action can be validated an executed, without having to know exactly what action it is. All the execution and validation logic has to be implemented in
 * the concrete classes. Every action must also offer an ID, so the controller can check if the action is coming from a valid player.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IAction {

	/**
	 * Execute the action. Call {@link #validate()} before to check if execution is allowed. If the validation code is not
	 * {@link ActionValidationCode#VALIDATION_SUCCESS}, the action is not executed. The position of the player data is taken from the player data id. In the
	 * given gameData, the point of view must be defined.
	 * 
	 * @param gameData
	 *            a full access {@link GameData} instance, the action is validated and executed on.
	 * @return the validation code.
	 */
	ActionValidationCode execute(IGameData gameData);

	/**
	 * @return the player id of the player that generated this action.
	 */
	IPlayerId getPlayerId();

	/**
	 * 
	 * @return the position of this actions playerId.
	 */
	PlayerPosition getPosition();

	/**
	 * Validate the action on the current game data.
	 * 
	 * @param playerUtils
	 *            the player utils used for validation.
	 * 
	 * @return true if the action is valid and can be executed.
	 */
	ActionValidationCode validate(IPlayerUtils playerUtils);
}
