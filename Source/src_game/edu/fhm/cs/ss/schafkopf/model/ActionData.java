package edu.fhm.cs.ss.schafkopf.model;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This data class holds the information about an executed or refused action.
 *
 * @author Sebastian Stumpf
 *
 */
public class ActionData implements IActionData {
	/** The code, the action was executed or refused with. */
	private final ActionValidationCode executionCode;
	/** The chosen game, if set depends on the action's type. */
	private final IBasicGameData chosenGame;
	/** The played card, if set depends on the action's type. */
	private final ICard playedCard;
	/** The boolean value, if set depends on the action's type. */
	private final boolean bool;
	/** The position of the player that created this action. */
	private final PlayerPosition executingPlayerPosition;
	/** The action's type. */
	private final ActionType actionType;

	/**
	 * Instantiate the action with the given parameters. The action validation code is set to {@link ActionValidationCode#EXECUTED_CHANGES}.
	 *
	 * @param actionType
	 *            the actions type.
	 * @param executingPlayerPosition
	 *            the executing player position.
	 * @param chosenGame
	 *            the chosen game.
	 * @param playedCard
	 *            the played card.
	 * @param bool
	 *            the boolean value.
	 */
	public ActionData(final ActionType actionType, final PlayerPosition executingPlayerPosition, final IBasicGameData chosenGame, final ICard playedCard, final boolean bool) {

		this(ActionValidationCode.EXECUTED_CHANGES, actionType, executingPlayerPosition, chosenGame, playedCard, bool);
	}

	/**
	 * Instantiate the action with the given parameters.
	 *
	 * @param executionCode
	 *            the code the action was executed or refused with.
	 * @param actionType
	 *            the actions type.
	 * @param executingPlayerPosition
	 *            the executing player position.
	 * @param chosenGame
	 *            the chosen game.
	 * @param playedCard
	 *            the played card.
	 * @param bool
	 *            the boolean value.
	 */
	public ActionData(final ActionValidationCode executionCode, final ActionType actionType, final PlayerPosition executingPlayerPosition, final IBasicGameData chosenGame, final ICard playedCard,
			final boolean bool) {

		super();
		this.executionCode = executionCode;
		this.chosenGame = chosenGame;
		this.playedCard = playedCard;
		this.bool = bool;
		this.executingPlayerPosition = executingPlayerPosition;
		this.actionType = actionType;
	}

	/**
	 * Copy constructor.
	 *
	 * @param lastExecutedAction
	 *            the action data to copy.
	 */
	public ActionData(final IActionData lastExecutedAction) {

		super();
		this.executionCode = lastExecutedAction.getValidationCode();
		this.chosenGame = lastExecutedAction.getChosenGame();
		this.playedCard = lastExecutedAction.getPlayedCard();
		this.bool = lastExecutedAction.getBoolean();
		this.executingPlayerPosition = lastExecutedAction.getExecutingPlayerPosition();
		this.actionType = lastExecutedAction.getActionType();
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ActionData other = (ActionData) obj;
		if (actionType != other.actionType) {
			return false;
		}
		if (bool != other.bool) {
			return false;
		}
		if (chosenGame == null) {
			if (other.chosenGame != null) {
				return false;
			}
		} else if (!chosenGame.equals(other.chosenGame)) {
			return false;
		}
		if (executingPlayerPosition != other.executingPlayerPosition) {
			return false;
		}
		if (executionCode != other.executionCode) {
			return false;
		}
		if (playedCard == null) {
			if (other.playedCard != null) {
				return false;
			}
		} else if (!playedCard.equals(other.playedCard)) {
			return false;
		}
		return true;
	}

	@Override
	public ActionType getActionType() {

		return actionType;
	}

	@Override
	public boolean getBoolean() {

		return bool;
	}

	@Override
	public IBasicGameData getChosenGame() {

		return chosenGame;
	}

	@Override
	public PlayerPosition getExecutingPlayerPosition() {

		return executingPlayerPosition;
	}

	@Override
	public ICard getPlayedCard() {

		return playedCard;
	}

	@Override
	public ActionValidationCode getValidationCode() {

		return executionCode;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (actionType == null ? 0 : actionType.hashCode());
		result = prime * result + (bool ? 1231 : 1237);
		result = prime * result + (chosenGame == null ? 0 : chosenGame.hashCode());
		result = prime * result + (executingPlayerPosition == null ? 0 : executingPlayerPosition.hashCode());
		result = prime * result + (executionCode == null ? 0 : executionCode.hashCode());
		result = prime * result + (playedCard == null ? 0 : playedCard.hashCode());
		return result;
	}

}
