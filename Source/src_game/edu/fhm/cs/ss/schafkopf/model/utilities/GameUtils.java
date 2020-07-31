package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IGameUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;

/**
 * This utility wrapper implements high level read methods available for all players.
 *
 * @author Sebastian Stumpf
 *
 */
public class GameUtils implements IGameUtils {

	/**
	 * The restricted game data instance the information is drawn from.
	 */
	private final IRestrictedGameData restrictedGameData;

	/**
	 * Instantiate the game utils with the given parameter.
	 *
	 * @param gameData
	 *            the game data instance the information is drawn from. Not null.
	 */
	public GameUtils(final IRestrictedGameData gameData) {

		super();
		if (gameData == null) {
			throw new IllegalArgumentException();
		}
		this.restrictedGameData = gameData;
	}

	@Override
	public int calculatePointsOfCards(final Collection<ICard> cards) {

		if (cards == null) {
			throw new IllegalArgumentException();
		}
		int points = 0;
		for (final ICard card : cards) {
			points += card.getValue().points;
		}
		return points;
	}

	@Override
	public boolean dominates(final ICard first, final ICard second) {

		final CardComparator comparator = new CardComparator(restrictedGameData.getGameType(), getTrumpColor());
		// because the card comparator does not have the functionality to check that only a card from the same color can dominate the other, we have to do that
		// here
		if (comparator.compare(first, second) > 0) {
			// in that case the card comparator is right
			if (first.getColor().equals(getTrumpColor())) {
				return true;
			} else {
				if (second.getColor().equals(getTrumpColor())) {
					return true;
				} else {
					// the second card can only dominate another color card if it is from the same color
					return first.getColor().equals(second.getColor());
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public Map<PlayerPosition, ICard> getCardsOnTable() {

		final Map<PlayerPosition, ICard> table = new EnumMap<PlayerPosition, ICard>(PlayerPosition.class);
		for (final IRestrictedPlayerData player : restrictedGameData.getRestrictedPlayerDatas().values()) {
			if (player.getPlayedCard() != null) {
				table.put(player.getPosition(), player.getPlayedCard());
			}
		}
		return table;
	}

	@Override
	public ICard getFirstPlayedCard() {

		return getCardsOnTable().get(restrictedGameData.getRoundsFirstPlayerPosition());
	}

	@Override
	public IRestrictedGameData getRestrictedGameData() {

		return restrictedGameData;
	}

	@Override
	public int getTeamsPoints(final Team team) {

		if (team == null) {
			throw new IllegalArgumentException();
		}

		int points = 0;
		for (final PlayerPosition position : restrictedGameData.getTeam(team)) {
			points += restrictedGameData.getRestrictedPlayerDatas().get(position).getPoints();
		}
		return points;
	}

	@Override
	public ArrayList<ICard> getTeamsWonCards(final Team team) {

		if (team == null) {
			throw new IllegalArgumentException();
		}
		final ArrayList<ICard> retVal = new ArrayList<ICard>();

		for (final PlayerPosition position : restrictedGameData.getTeam(team)) {
			retVal.addAll(restrictedGameData.getRestrictedPlayerDatas().get(position).getWonCards());
		}
		return retVal;
	}

	@Override
	public int getTotalMultiplier() {

		return (int) Math
				.pow(2, restrictedGameData.getCharge().getExclusiveMultiplier() + restrictedGameData.getCharge().getInitialMultiplier() + restrictedGameData.getCharge().getStrikeMultiplier());
	}

	@Override
	public CardColor getTrumpColor() {

		return restrictedGameData.getGameType().equals(GameType.SAUSPIEL) ? IRestrictedGameData.SAUSPIEL_TRUMP_COLOR : restrictedGameData.getColor();
	}

	@Override
	public CardColor getTrumpColor(final IBasicGameData game) {

		if (game == null || game.getGameType() == null) {
			throw new IllegalArgumentException();
		}
		return game.getGameType().equals(GameType.SAUSPIEL) ? IRestrictedGameData.SAUSPIEL_TRUMP_COLOR : game.getColor();
	}

	@Override
	public boolean haveAllPlayersPlayedCard() {

		for (final IRestrictedPlayerData player : restrictedGameData.getRestrictedPlayerDatas().values()) {
			if (player.getPlayedCard() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isCardHighTrump(final GameType gameType, final ICard card) {

		if (gameType == null || card == null) {
			throw new IllegalArgumentException();
		}
		switch (gameType) {
			case SOLO:
			case SOLO_TOUT:
			case SAUSPIEL:
				return card.getValue().equals(CardValue.OBER) || card.getValue().equals(CardValue.UNTER);
			case WENZ:
			case WENZ_TOUT:
			case FARBWENZ:
			case FARBWENZ_TOUT:
				return card.getValue().equals(CardValue.UNTER);
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean isCardHighTrump(final IBasicGameData game, final ICard card) {

		if (game == null) {
			throw new IllegalArgumentException();
		}
		return isCardHighTrump(game.getGameType(), card);
	}

	@Override
	public boolean isCardHighTrump(final ICard card) {

		return isCardHighTrump(restrictedGameData, card);
	}

	@Override
	public boolean isCardTrump(final IBasicGameData game, final ICard card) {

		return isCardHighTrump(game, card) || card.getColor().equals(getTrumpColor(game));
	}

	@Override
	public boolean isCardTrump(final ICard card) {

		return isCardTrump(restrictedGameData, card);
	}

	@Override
	public boolean isMateSearchedInThisRound() {

		final ICard firstCard = getFirstPlayedCard();
		// mate is not searched if we do not play a sauspiel, there is no
		// card
		// played yet or the mates already know
		if (!restrictedGameData.getGameType().equals(GameType.SAUSPIEL) || firstCard == null || isCardTrump(firstCard) || restrictedGameData.getTeam(Team.PLAYER_TEAM).size() == 2) {
			return false;
		}
		// if the color fits, return true, if the mate sau was not played
		// itself
		if (firstCard.getColor().equals(restrictedGameData.getColor())) {
			return !firstCard.getValue().equals(CardValue.SAU);
		}
		return false;
	}
}
