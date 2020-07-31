package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * This is a container class for various action data.<br>
 * <br>
 * 
 * Only the methods, that fit to the actions {@link ActionType} must return valid values.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IActionData {

	@Override
	boolean equals(Object obj);

	/**
	 * @return the action's type.
	 */
	ActionType getActionType();

	/**
	 * @return the boolean value of the STRIKE and STRIKEBACK actions. If action type is any other type, always false.
	 */
	boolean getBoolean();

	/**
	 * @return the chosen game, if action type is CHOOSE, else null.
	 */
	IBasicGameData getChosenGame();

	/**
	 * @return the executing player position.
	 */
	PlayerPosition getExecutingPlayerPosition();

	/**
	 * @return the played card, if action type is PLAY, else null.
	 */
	ICard getPlayedCard();

	/**
	 * @return the validation code, the action was executed, or refused with.
	 */
	ActionValidationCode getValidationCode();

	@Override
	int hashCode();
}
