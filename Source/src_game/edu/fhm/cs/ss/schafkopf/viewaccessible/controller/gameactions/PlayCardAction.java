package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions;

import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.ActionData;
import edu.fhm.cs.ss.schafkopf.model.Card;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.FullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.StackHandler;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces.IPlayCardAction;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This action implements the execution and validation of a player playing a card.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class PlayCardAction extends BaseAction implements IPlayCardAction {

	/**
	 * The card the player wants to play.
	 */
	private final ICard chosenCard;

	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 * @param chosenCardValue
	 *            the card's value.
	 * @param chosenCardColor
	 *            the card's color.
	 */
	public PlayCardAction(final IPlayerId playerId, final CardColor chosenCardColor, final CardValue chosenCardValue) {

		this(playerId, new Card(chosenCardColor, chosenCardValue));
	}

	/**
	 * Instantiate the action with the given parameters.
	 * 
	 * @param playerId
	 *            the player ID.
	 * @param chosenCard
	 *            the chosen card.
	 */
	public PlayCardAction(final IPlayerId playerId, final ICard chosenCard) {

		super(playerId);
		this.chosenCard = chosenCard;
	}

	@Override
	public ActionValidationCode execute(final IGameData gameData) {

		final IFullAccessGameUtils utils = new FullAccessGameUtils(getPosition(), gameData);

		ActionValidationCode retVal = validate(utils);
		if (retVal == ActionValidationCode.VALIDATION_SUCCESS) {
			utils.getPovPlayerData().getCurrentHand().remove(getChosenCard());
			utils.getPovPlayerData().setPlayedCard(getChosenCard());

			final boolean allPlayersPlayedCard = utils.haveAllPlayersPlayedCard();
			final Map<PlayerPosition, ICard> cardsOnTable = utils.getCardsOnTable();

			// mate sau manipulations
			if (utils.getGameData().getGameType().equals(GameType.SAUSPIEL)) {
				final ICard mateSau = StackHandler.getInstance().getCardByColorAndValue(utils.getGameData().getColor(), CardValue.SAU);
				// mate manipulations have only to be done if the mates do not
				// already know each other
				if (gameData.getPlayerTeam().size() < 2) {
					// MateSau was played
					if (getChosenCard().equals(mateSau)) {
						utils.getGameData().getPlayerTeam().add(utils.getPovPlayerData().getPosition());
						utils.fillOpponentTeam();
					}
					// mate ran away
					else if (allPlayersPlayedCard && utils.isMateSearchedInThisRound() && !cardsOnTable.containsValue(mateSau)) {
						utils.getGameData().getPlayerTeam().add(utils.getGameData().getRoundsFirstPlayerPosition());
						utils.fillOpponentTeam();
					}
				}
			}

			// end round manipulations if all players played their card
			if (allPlayersPlayedCard) {
				final IPlayerData winner = utils.getGameData().getPlayerDatas().get(utils.calculateWinnerCardPosition(cardsOnTable));
				final int points = utils.calculatePointsOfCards(cardsOnTable.values());
				winner.getWonCards().addAll(cardsOnTable.values());
				winner.addPoints(points);
				utils.getGameData().setRoundsFirstPlayerPosition(winner.getPosition());
				utils.getGameData().setPlayerOnTurnPosition(winner.getPosition());
				utils.getGameData().setLastRoundsPlayedCards(cardsOnTable);
				utils.getGameData().setLastRoundsWinner(winner.getPosition());
				utils.clearPlayedCards();
				utils.getGameData().incrementRoundNumber();
				// TODO: ERROR on purpose for testing -> causes invalid state,
				// very unlikely chosen
				// if (Math.random() < 0.0001) {
				// winner.getWonCards().remove(cardsOnTable.get(getPosition()));
				// }
			} else {
				utils.getGameData().setPlayerOnTurnPosition(utils.getGameData().getPlayerOnTurnPosition().getNext());
			}

			// end game manipulations if this is the last round
			if (utils.getGameData().getRoundNumber() == IRestrictedGameData.ROUNDS_PER_GAME) {
				utils.endGameManipulations();
			}

			// if this is the first card that was played in this game, then the
			// game
			// status is set to strike
			if (utils.getGameData().getRoundNumber() == 0 && utils.getCardsOnTable().size() == 1) {
				utils.getGameData().setGameState(GameState.STRIKE);
			}

			utils.getGameData().setLastExecutedAction(new ActionData(ActionType.PLAY, utils.getPovPlayerData().getPosition(), null, getChosenCard(), false));

			retVal = ActionValidationCode.EXECUTED_CHANGES;
		}

		return retVal;

	}

	@Override
	public ICard getChosenCard() {

		return this.chosenCard;
	}

	@Override
	public ActionValidationCode validate(final IPlayerUtils playerUtils) {

		return playerUtils.getPlayCardValidationCode(playerUtils.getPovPlayerData(), chosenCard);
	}
}
