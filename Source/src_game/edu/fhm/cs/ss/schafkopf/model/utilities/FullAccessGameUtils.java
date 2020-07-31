package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IFullAccessGameUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.StockIndex;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICharge;

/**
 * This utility wrapper extends the {@link PlayerUtils} by high level write methods and those not available for player views and not depending on a player data.<br>
 * <br>
 *
 * @author Sebastian Stumpf
 *
 */
public class FullAccessGameUtils extends PlayerUtils implements IFullAccessGameUtils {

	/**
	 * Instantiates the full access utilities with the given game data. Using this Constructor will cause the methods in {@link PlayerUtils} that depend on a
	 * point-of-view player data to throw null pointer exceptions. Use the correspondent methods with the player data as parameter.
	 *
	 * @param gameData
	 *            the game data.
	 */
	public FullAccessGameUtils(final IGameData gameData) {

		super(gameData);
	}

	/**
	 * Instantiates the full access utilities with the given game data and the point-of-view player data set to that on the given position. So the
	 * {@link PlayerUtils} methods using the point-of-view player data will work.
	 *
	 * @param position
	 *            the point-of-view player position.
	 * @param gameData
	 *            the game data.
	 */
	public FullAccessGameUtils(final PlayerPosition position, final IGameData gameData) {

		super(position, gameData);

	}

	@Override
	public int calculateBasicChargeForCurrentGame() {

		int retVal = 0;
		if (getGameData().getGameType().equals(GameType.PASS)) {
			return retVal;
		} else if (getGameData().getGameType().isPartnerGame) {
			retVal = getGameData().getGameSettings().getBasicCharge();
		} else {
			retVal = getGameData().getGameSettings().getBasicCharge() * getGameData().getGameSettings().getSoloMultiplier();
		}
		return retVal;

	}

	@Override
	public PlayerPosition calculateWinnerCardPosition(final Map<PlayerPosition, ICard> stich) {

		final PlayerPosition firstPlayerPosition = getGameData().getRoundsFirstPlayerPosition();
		ICard winnerCard = stich.get(firstPlayerPosition);
		PlayerPosition winnerCardPosition = firstPlayerPosition;
		for (PlayerPosition position = firstPlayerPosition.getNext(); !position.equals(firstPlayerPosition); position = position.getNext()) {
			if (dominates(winnerCard, stich.get(position))) {
				winnerCard = stich.get(position);
				winnerCardPosition = position;
			}
		}
		return winnerCardPosition;

	}

	@Override
	public void clearPlayedCards() {

		for (final IPlayerData player : getGameData().getPlayerDatas().values()) {
			player.setPlayedCard(null);
		}

	}

	@Override
	public void dealCardsFromStack(final IPlayerData playerData, final int number) {

		if (playerData == null || number < 0) {
			throw new IllegalArgumentException();
		}
		// have to instantiate a new List here, because sublist is just a view
		// on the original list
		final List<ICard> drawnCards = new ArrayList<ICard>();
		if (getGameData().getStack().size() < number) {
			drawnCards.addAll(getGameData().getStack().subList(0, getGameData().getStack().size()));
		} else {
			drawnCards.addAll(getGameData().getStack().subList(0, number));
		}
		getGameData().getStack().removeAll(drawnCards);
		if (playerData.getCurrentHand() == null) {
			playerData.setCurrentHand(drawnCards);
		} else {
			playerData.getCurrentHand().addAll(drawnCards);
		}
		if (playerData.getInitialHand() == null) {
			playerData.setInitialHand(drawnCards);
		} else {
			playerData.getInitialHand().addAll(drawnCards);
		}
		// sort players current hand by sauspiel, herz
		final CardComparator comp = new CardComparator(GameType.SAUSPIEL, CardColor.HERZ);
		Collections.sort(playerData.getInitialHand(), comp);
		Collections.sort(playerData.getCurrentHand(), comp);
	}

	@Override
	public void endGameManipulations() {

		calculateAndFillWinner();
		calculateAndFillCharge();
		distributeCharge();
		getGameData().setGameState(GameState.FINISHED);
	}

	@Override
	public void fillOpponentTeam() {

		for (final IPlayerData player : getGameData().getPlayerDatas().values()) {
			if (!getGameData().getTeam(Team.PLAYER_TEAM).contains(player.getPosition())) {
				getGameData().getTeam(Team.OPPONENT_TEAM).add(player.getPosition());
			}
		}
	}

	@Override
	public IGameData getGameData() {

		return (IGameData) getRestrictedGameData();
	}

	@Override
	public boolean haveAllPlayersChosen() {

		for (final IPlayerData player : getGameData().getPlayerDatas().values()) {
			if (player.getChosenGame() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initializeGameData(final PlayerPosition firstPlayerPosition) {

		initializeGameData(firstPlayerPosition, null);
	}

	@Override
	public void initializeGameData(final PlayerPosition firstPlayerPosition, final List<ICard> stack) {

		// initialize game data for next game
		getGameData().setPlayerOnTurnPosition(firstPlayerPosition);
		getGameData().setGamesFirstPlayerPosition(firstPlayerPosition);
		getGameData().setRoundsFirstPlayerPosition(firstPlayerPosition);
		getGameData().setLeadPlayerPosition(null);
		getGameData().setColor(null);
		getGameData().setRoundNumber(-1);
		getGameData().setGameType(null);
		getGameData().setLastExecutedAction(null);
		getGameData().setLastRoundsWinner(null);
		getGameData().setGameState(GameState.GET_RAISE);
		getGameData().setWinnerTeam(null);
		if (getGameData().getOpponentTeam() == null) {
			getGameData().setOpponentTeam(new ArrayList<PlayerPosition>());
		} else {
			getGameData().getOpponentTeam().clear();
		}
		if (getGameData().getPlayerTeam() == null) {
			getGameData().setPlayerTeam(new ArrayList<PlayerPosition>());
		} else {
			getGameData().getPlayerTeam().clear();
		}
		// initialize charge
		getGameData().getCharge().setBasic(0);
		getGameData().getCharge().setBounty(0);
		getGameData().getCharge().setExclusiveMultiplier(0);
		getGameData().getCharge().setInitialMultiplier(0);
		getGameData().getCharge().setStrikeMultiplier(0);
		getGameData().getCharge().setSchneider(0);
		getGameData().getCharge().setTotalCharge(0);
		getGameData().getCharge().setStockIndex(StockIndex.IGNORE);
		getGameData().getCharge().setStockValue(0);
		getGameData().getActionBuffer().clear();
		if (getGameData().getLastRoundsPlayedCards() == null) {
			getGameData().setLastRoundsPlayedCards(new EnumMap<PlayerPosition, ICard>(PlayerPosition.class));
		} else {
			getGameData().getLastRoundsPlayedCards().clear();
		}
		// initialize all player data
		for (final IPlayerData player : getGameData().getPlayerDatas().values()) {
			initializePlayerData(player);
		}

		if (stack == null) {
			// initialize stack
			getGameData().setStack(StackHandler.getInstance().getShuffledStack());
		} else {
			getGameData().setStack(stack);
		}
	}

	@Override
	public void initializePlayerData(final IPlayerData playerData) {

		playerData.setAcceptingNextGameStart(false);
		playerData.setChosenGame(null);
		playerData.getCurrentHand().clear();
		playerData.getInitialHand().clear();
		playerData.setPlayedCard(null);
		playerData.setPoints(0);
		playerData.setRaising(false);
		playerData.setStriking(false);
		playerData.setStrikingBack(false);
		playerData.getWonCards().clear();
	}

	@Override
	public void startGameManipulations() {

		if (getGameData().getGameType().equals(GameType.PASS)) {
			endGameManipulations();
		} else {
			// set the color
			getGameData().setColor(getGameData().getPlayerDatas().get(getGameData().getLeadPlayerPosition()).getChosenGame().getColor());
			// set the unrestricted game type
			getGameData().setGameType(getGameData().getPlayerDatas().get(getGameData().getLeadPlayerPosition()).getChosenGame().getGameType());
			// set the basic charge value for the the chosen gametype
			getGameData().getCharge().setBasic(calculateBasicChargeForCurrentGame());
			// set exclusive muliplier if the game is exclusive
			if (getGameData().getGameType().isExclusive) {
				getGameData().getCharge().setExclusiveMultiplier(1);
			}
			// set teams as far as known
			getGameData().getTeam(Team.PLAYER_TEAM).add(getGameData().getLeadPlayerPosition());
			if (!getGameData().getGameType().isPartnerGame) {
				// in a solo game, the partners are known from the beginning
				fillOpponentTeam();
			}
			final CardComparator comparator = new CardComparator(getGameData().getGameType(), getTrumpColor());
			for (final IPlayerData playerData : getGameData().getPlayerDatas().values()) {
				Collections.sort(playerData.getCurrentHand(), comparator);
			}
			getGameData().incrementRoundNumber();
			getGameData().setGameState(GameState.PLAY);
		}
	}

	/**
	 * Calculate the bounty for the current game and fill it to the game data's charge.
	 */
	private void calculateAndFillBounty() {

		final ArrayList<IPlayerData> playerTeam = new ArrayList<IPlayerData>();
		final ArrayList<IPlayerData> opponentTeam = new ArrayList<IPlayerData>();
		// get the player data for the teams
		for (final PlayerPosition position : getGameData().getTeam(Team.PLAYER_TEAM)) {
			playerTeam.add(getGameData().getPlayerDatas().get(position));
		}
		for (final PlayerPosition position : getGameData().getTeam(Team.OPPONENT_TEAM)) {
			opponentTeam.add(getGameData().getPlayerDatas().get(position));
		}
		// calculate the high cards of both teams
		final int playerTeamsHighCards = calculateHighCardsInRow(getGameData(), playerTeam);
		final int opponentTeamsHighCards = calculateHighCardsInRow(getGameData(), opponentTeam);
		// fill charge with higher value
		getGameData().getCharge().setBounty(playerTeamsHighCards > opponentTeamsHighCards ? playerTeamsHighCards : opponentTeamsHighCards);
	}

	/**
	 * Calculate the charge for the current game and fill it according to the calculations.
	 */
	private void calculateAndFillCharge() {

		calculateAndFillStock();
		calculateAndFillBounty();
		calculateAndFillSchneider();
		calculateAndFillTotalCharge();
	}

	/**
	 * Calculate the Schneider-value for the current game and fill it to the game data's charge.
	 */
	private void calculateAndFillSchneider() {

		if (getGameData().getGameType().equals(GameType.PASS)) {
			return;
		}
		final int winnersPoints = getTeamsPoints(getGameData().getWinnerTeam());
		// loser team has won no cards
		if (getTeamsWonCards(getGameData().getWinnerTeam()).size() == 32) {
			getGameData().getCharge().setSchneider(2);
		}
		// winner is player team -> winner needs 91 points for schneider
		else if (getGameData().getWinnerTeam().equals(Team.PLAYER_TEAM) && winnersPoints > 91 || getGameData().getWinnerTeam().equals(Team.OPPONENT_TEAM) && winnersPoints > 90) {
			getGameData().getCharge().setSchneider(1);
		}
	}

	/**
	 * Calculate the stock for the current game and fill it to the game data's charge.
	 */
	private void calculateAndFillStock() {

		// all players passed
		if (getGameData().getGameType().equals(GameType.PASS)) {
			// each player has to pay basic value in to the stock
			getGameData().getCharge().setStockIndex(StockIndex.PAY_IN);
			getGameData().getCharge().setStockValue(getGameData().getGameSettings().getBasicCharge());
		}
		// stock is not empty and we play a partner game
		else if (getGameData().getStock() > 0 && getGameData().getGameType().isPartnerGame) {
			// if player wins the stock is paid out
			if (getGameData().getWinnerTeam().equals(Team.PLAYER_TEAM)) {
				getGameData().getCharge().setStockIndex(StockIndex.PAY_OUT);
			}
			// if the opponent team wins, the players have to double the
			// stock
			else {
				getGameData().getCharge().setStockIndex(StockIndex.PAY_IN);
			}
			getGameData().getCharge().setStockValue(getGameData().getStock() / 2);
		}
	}

	/**
	 * Calculate the total charge for the current game and fill it to the game data's charge.
	 */
	private void calculateAndFillTotalCharge() {

		// the total multiplier is 2 ^ the sum of all added multipliers
		final int totalMultiplicator = getTotalMultiplier();
		// in an exclusive game, schneider is ignored
		final int chargeWithoutMultiplier = getGameData().getCharge().getBasic()
				+ (getGameData().getCharge().getBounty() + (getGameData().getCharge().getExclusiveMultiplier() == 0 ? getGameData().getCharge().getSchneider() : 0))
				* getGameData().getGameSettings().getBasicCharge();
		getGameData().getCharge().setTotalCharge(chargeWithoutMultiplier * totalMultiplicator);
	}

	/**
	 * Calculate the winner team for the current game and fill it to the game data's winner team attribute.
	 */
	private void calculateAndFillWinner() {

		if (getGameData().getGameType().equals(GameType.PASS)) {
			return;
		}
		if (getGameData().getGameType().isExclusive) {
			getGameData().setWinnerTeam(getTeamsPoints(Team.OPPONENT_TEAM) == 0 ? Team.PLAYER_TEAM : Team.OPPONENT_TEAM);
		} else if (getTeamsPoints(Team.PLAYER_TEAM) > getTeamsPoints(Team.OPPONENT_TEAM)) {
			getGameData().setWinnerTeam(Team.PLAYER_TEAM);
		} else {
			getGameData().setWinnerTeam(Team.OPPONENT_TEAM);
		}
	}

	/**
	 * Distribute the calculated charge among the players and the stock.
	 */
	private void distributeCharge() {

		final ICharge charge = getGameData().getCharge();
		for (final IPlayerData player : getGameData().getPlayerDatas().values()) {
			final boolean winner = getGameData().getTeam(getGameData().getWinnerTeam()).contains(player.getPosition());
			// manipulate stock values
			if (charge.getStockIndex().equals(StockIndex.PAY_IN)) {
				player.addCredit(-charge.getStockValue());
				getGameData().setStock(getGameData().getStock() + charge.getStockValue());
			} else if (charge.getStockIndex().equals(StockIndex.PAY_OUT) && winner) {
				player.addCredit(charge.getStockValue());
				getGameData().setStock(getGameData().getStock() - charge.getStockValue());
			} else if (charge.getStockIndex().equals(StockIndex.DOUBLE) && !winner) {
				player.addCredit(-charge.getStockValue());
				getGameData().setStock(getGameData().getStock() + charge.getStockValue());
			}
			// a solo player wins and looses 3 times the total charge
			final int soloPlayerMultiplier = player.getPosition().equals(getGameData().getLeadPlayerPosition()) && getGameData().getTeam(Team.PLAYER_TEAM).size() == 1 ? 3 : 1;
			// manipulate charge values
			if (winner) {
				player.addCredit(soloPlayerMultiplier * charge.getTotalCharge());
			} else {
				player.addCredit(soloPlayerMultiplier * -charge.getTotalCharge());
			}
		}
	}
}
