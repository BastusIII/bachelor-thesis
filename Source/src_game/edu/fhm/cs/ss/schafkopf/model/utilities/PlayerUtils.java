package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import edu.fhm.cs.ss.schafkopf.model.BasicGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPlayerUtils;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.ActionValidationCode;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardValue;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.Team;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This utility wrapper extends {@link GameUtils} by high level player dependent methods. <br>
 * <br>
 *
 * That means the information is drawn from the game data in combination with a player data. The methods return different values dependent on the player data.
 * For example if a player may play a card in the current game depends on his current hand.
 *
 * @author Sebastian Stumpf
 *
 */
public class PlayerUtils extends GameUtils implements IPlayerUtils {

	/** The point-of-view player data that is used for calculations if no player data is given in the parameter of a method. */
	private IPlayerData povPlayerData;

	/**
	 * Instantiate the player utilities with the given game data, the point of view player data is set to null.
	 *
	 * @param gameData
	 *            the game data instance. Should not be null.
	 *
	 */
	public PlayerUtils(final IGameData gameData) {

		super(gameData);
		povPlayerData = null;
	}

	/**
	 * Instantiates the player utilities with the given game data and the point-of-view player data defined by the game data's point-of-view position.
	 *
	 * @throws ClassCastException
	 *             id the playerData at the position is not an instance of {@link IPlayerData}.
	 * @throws IllegalArgumentException
	 *             if the player point of view position in the game data is not set.
	 *
	 * @param restrictedGameData
	 *            the game data instance. Should not be null.
	 */
	public PlayerUtils(final IRestrictedGameData restrictedGameData) {

		super(restrictedGameData);
		if (restrictedGameData.getPointOfViewPosition() == null) {
			throw new IllegalArgumentException("Point of view position is null.");
		}
		// set the point-of-view player data to the one defined by the point-of-view position in the given game data
		povPlayerData = (IPlayerData) restrictedGameData.getRestrictedPlayerDatas().get(restrictedGameData.getPointOfViewPosition());
	}

	/**
	 * Instantiates the player utilities with the given game data and the point-of-view player data set to that on the given position.
	 *
	 * @param pointOfViewPosition
	 *            the point-of-view position. If null, {@link #povPlayerData} is not set.
	 * @param gameData
	 *            the game data instance. Should not be null.
	 */
	public PlayerUtils(final PlayerPosition pointOfViewPosition, final IGameData gameData) {

		super(gameData);
		if (pointOfViewPosition == null) {
			povPlayerData = null;
		} else {
			this.povPlayerData = gameData.getPlayerDatas().get(pointOfViewPosition);
		}
	}

	@Override
	public int calculateHighCardsInRow() {

		return calculateHighCardsInRow(getRestrictedGameData(), povPlayerData);
	}

	@Override
	public int calculateHighCardsInRow(final IBasicGameData gameData) {

		return calculateHighCardsInRow(gameData, povPlayerData);
	}

	@Override
	public int calculateHighCardsInRow(final IBasicGameData gameData, final Collection<IPlayerData> players) {

		if (gameData == null || gameData.getGameType() == null || players == null) {
			return 0;
		}

		if (gameData.getGameType().equals(GameType.PASS)) {
			return 0;
		}
		final ArrayList<ICard> cards = new ArrayList<ICard>();
		for (final IPlayerData data : players) {
			if (data.getInitialHand() != null) {
				cards.addAll(data.getInitialHand());
			}
		}
		Collections.sort(cards, new CardComparator(gameData.getGameType(), getTrumpColor(gameData)));
		final List<ICard> sortedStack = StackHandler.getInstance().getSortedCards(gameData.getGameType(), getTrumpColor(gameData));
		int retVal = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (!cards.get(i).equals(sortedStack.get(i))) {
				retVal = i;
				break;
			}
		}
		// in wenz games the high cards in row have to be at least 2
		if (gameData.getGameType().equals(GameType.WENZ) || gameData.getGameType().equals(GameType.WENZ_TOUT)) {
			retVal = retVal < IRestrictedGameData.MIN_HIGHCARDS_WENZ ? retVal : 0;
		}
		// in other games the high cards in row have to be at least 3
		else {
			retVal = retVal < IRestrictedGameData.MIN_HIGHCARDS_BASIC ? retVal : 0;
		}
		return retVal;
	}

	@Override
	public int calculateHighCardsInRow(final IBasicGameData gameData, final IPlayerData... players) {

		if (players == null) {
			throw new IllegalArgumentException();
		}
		return calculateHighCardsInRow(gameData, new ArrayList<IPlayerData>(Arrays.asList(players)));
	}

	@Override
	public int countColor(final CardColor color) {

		return countColor(getRestrictedGameData(), color);

	}

	@Override
	public int countColor(final IBasicGameData game, final CardColor color) {

		return countColor(povPlayerData, game, color);

	}

	@Override
	public int countColor(final IPlayerData player, final CardColor color) {

		return countColor(player, getRestrictedGameData(), color);
	}

	@Override
	public int countColor(final IPlayerData player, final GameType gameType, final CardColor color) {

		if (player == null || gameType == null || color == null) {
			throw new IllegalArgumentException();
		}
		int retVal = 0;
		for (final ICard card : player.getCurrentHand()) {
			if (!isCardHighTrump(gameType, card) && card.getColor().equals(color)) {
				retVal++;
			}
		}
		return retVal;
	}

	@Override
	public int countColor(final IPlayerData player, final IBasicGameData game, final CardColor color) {

		if (game == null || game.getGameType() == null || color == null) {
			throw new IllegalArgumentException();
		}
		return countColor(player, game.getGameType(), color);
	}

	@Override
	public int countTrump() {

		return countTrump(getRestrictedGameData());

	}

	@Override
	public int countTrump(final IBasicGameData game) {

		return countTrump(povPlayerData, game);
	}

	@Override
	public int countTrump(final IPlayerData player, final IBasicGameData game) {

		if (player == null) {
			throw new IllegalArgumentException();
		}
		int retVal = 0;
		for (final ICard card : player.getCurrentHand()) {
			if (isCardTrump(game, card)) {
				retVal++;
			}
		}
		return retVal;
	}

	@Override
	public Collection<ICard> getAllColorCards(final CardColor color) {

		return getAllColorCards(getRestrictedGameData(), color);
	}

	@Override
	public Collection<ICard> getAllColorCards(final IBasicGameData game, final CardColor color) {

		return getAllColorCards(povPlayerData, game, color);

	}

	@Override
	public Collection<ICard> getAllColorCards(final IPlayerData player, final CardColor color) {

		return getAllColorCards(player, getRestrictedGameData(), color);

	}

	@Override
	public Collection<ICard> getAllColorCards(final IPlayerData player, final IBasicGameData game, final CardColor color) {

		if (player == null || color == null) {
			throw new IllegalArgumentException();
		}

		final Collection<ICard> retVal = new ArrayList<ICard>();
		if (player.getCurrentHand() != null) {
			for (final ICard card : player.getCurrentHand()) {
				if (card.getColor().equals(color) && !isCardHighTrump(game, card)) {
					retVal.add(card);
				}
			}
		}
		return retVal;
	}

	@Override
	public Collection<ICard> getAllTrumpCards() {

		return getAllTrumpCards(getRestrictedGameData());
	}

	@Override
	public Collection<ICard> getAllTrumpCards(final IBasicGameData game) {

		return getAllTrumpCards(povPlayerData, game);
	}

	@Override
	public Collection<ICard> getAllTrumpCards(final IPlayerData player) {

		return getAllTrumpCards(player, getRestrictedGameData());
	}

	@Override
	public Collection<ICard> getAllTrumpCards(final IPlayerData player, final IBasicGameData game) {

		if (player == null) {
			throw new IllegalArgumentException();
		}
		final Collection<ICard> retVal = new ArrayList<ICard>();
		if (player.getCurrentHand() != null) {
			for (final ICard card : player.getCurrentHand()) {
				if (isCardTrump(game, card)) {
					retVal.add(card);
				}
			}
		}
		return retVal;
	}

	@Override
	public Collection<ICard> getAvailableCards() {

		return getAvailableCards(povPlayerData);
	}

	@Override
	public Collection<ICard> getAvailableCards(final IPlayerData player) {

		if (player == null) {
			throw new IllegalArgumentException();
		}
		final Collection<ICard> availableCards = new ArrayList<ICard>();
		if (!getRestrictedGameData().getGameState().equals(GameState.PLAY)) {
			return availableCards;
		}
		try {
			final ICard firstCard = getFirstPlayedCard();
			if (player.getCurrentHand() != null) {
				// in a SAUSPIEL more rules have to be regarded
				if (getRestrictedGameData().getGameType().equals(GameType.SAUSPIEL)) {
					// player comes out
					if (firstCard == null) {
						if (isLeadPlayerMate(player)) {
							final boolean isAllowedToRunAway = isAllowedToRunAway(player);
							for (final ICard card : player.getCurrentHand()) {
								// player is allowed to play the card, if one of the
								// following is true:
								// players know each other | card is not of mate
								// color | player is allowed to run away | card is
								// mate sau
								if (getRestrictedGameData().getPlayerTeam().size() == 2 || !card.getColor().equals(getRestrictedGameData().getColor()) || isAllowedToRunAway
										|| card.getValue().equals(CardValue.SAU)) {
									availableCards.add(card);
								}
							}
						} else {
							availableCards.addAll(player.getCurrentHand());
						}
					}
					// player must serve a player before him
					else {
						final boolean matesUnknown = getRestrictedGameData().getPlayerTeam().size() < 2;
						// trump is played
						if (isCardTrump(firstCard)) {
							final Collection<ICard> trumpCards = getAllTrumpCards(player);
							// player has no trump, so he doesn't need to serve
							if (trumpCards.isEmpty()) {
								availableCards.addAll(player.getCurrentHand());
								// if the player is mate and the mate has not been
								// searched yet, he must not play the mate SAU
								// expect it is the last card on his hand
								if (matesUnknown && isLeadPlayerMate(player) && getPovPlayerData().getCurrentHand().size() > 1) {
									availableCards.remove(StackHandler.getInstance().getCardByColorAndValue(getRestrictedGameData().getColor(), CardValue.SAU));
								}
								availableCards.addAll(trumpCards);
							}
							// player has trump, so he must serve with trump
							else {
								availableCards.addAll(trumpCards);
							}
						}
						// color is played
						else {
							final Collection<ICard> colorCards = getAllColorCards(player, firstCard.getColor());
							// player has no color, so he can doesn't need to serve
							if (colorCards.isEmpty()) {
								availableCards.addAll(player.getCurrentHand());
								// if the player is mate and the mate has not been
								// searched yet, he must not play the mate SAU
								if (matesUnknown && isLeadPlayerMate(player) && getPovPlayerData().getCurrentHand().size() > 1) {
									availableCards.remove(StackHandler.getInstance().getCardByColorAndValue(getRestrictedGameData().getColor(), CardValue.SAU));
								}
							}
							// player has color and so has to serve
							else {
								// if mate is searched we have to serve with the
								// mate
								// sau
								if (isMateSearchedInThisRound() && isLeadPlayerMate(player)) {
									availableCards.add(StackHandler.getInstance().getCardByColorAndValue(getRestrictedGameData().getColor(), CardValue.SAU));
								} else {
									availableCards.addAll(colorCards);
								}
							}
						}
					}
				}
				// in a solo game rules over searching the mate need not be observed
				else {
					// player comes out
					if (firstCard == null) {
						availableCards.addAll(player.getCurrentHand());
					} else {
						// trump is played
						if (isCardTrump(firstCard)) {
							final Collection<ICard> trumpCards = getAllTrumpCards(player);
							// player has no trump and can play what he wants
							if (trumpCards.isEmpty()) {
								availableCards.addAll(player.getCurrentHand());
							}
							// player has trump, so he must serve with trump
							else {
								availableCards.addAll(trumpCards);
							}
						}
						// color is played
						else {
							final Collection<ICard> colorCards = getAllColorCards(player, firstCard.getColor());
							// player has no color and can play what he wants
							if (colorCards.isEmpty()) {
								availableCards.addAll(player.getCurrentHand());
							}
							// player has color and must serve
							else {
								availableCards.addAll(colorCards);
							}
						}
					}
				}
			}
			return availableCards;
		} catch (final NullPointerException e) {
			// seems game data is corrupt!! must noch happen, please check tests
			availableCards.clear();
			return availableCards;
		}
	}

	@Override
	public Collection<CardColor> getAvailableColors(final GameType gameType) {

		return getAvailableColors(povPlayerData, gameType);
	}

	@Override
	public Collection<CardColor> getAvailableColors(final IPlayerData player, final GameType gameType) {

		if (gameType == null) {
			throw new IllegalArgumentException();
		}

		final Collection<CardColor> availableColors = new HashSet<CardColor>();
		if (!getRestrictedGameData().getGameState().equals(GameState.CHOOSE)) {
			return availableColors;
		}
		switch (gameType) {
			case FARBWENZ:
			case FARBWENZ_TOUT:
			case SOLO:
			case SOLO_TOUT:
				for (final CardColor color : CardColor.values()) {
					// A player is only allowed to play SOLOs of a color he
					// owns.
					if (countColor(player, gameType, color) > 0) {
						availableColors.add(color);
					}
				}
				break;
			case SAUSPIEL:
				for (final CardColor color : CardColor.values()) {
					// To play a SAUSPIEL, the player need at least one card
					// of
					// the mate color and must not own the mate SAU. HERZ
					// must
					// never be mate color.
					if (!color.equals(CardColor.HERZ) && countColor(player, gameType, color) > 0 && !ownsCard(player, color, CardValue.SAU)) {
						availableColors.add(color);
					}
				}
				break;
			default:
				break;
		}
		return availableColors;
	}

	@Override
	public Collection<GameType> getAvailableGameTypes() {

		return getAvailableGameTypes(povPlayerData);
	}

	@Override
	public Collection<GameType> getAvailableGameTypes(final IPlayerData player) {

		final Collection<GameType> allowedTypes = new HashSet<GameType>();
		if (!getRestrictedGameData().getGameState().equals(GameState.CHOOSE)) {
			return allowedTypes;
		}
		for (final GameType newType : GameType.values()) {
			if (isGameTypeAllowedToChoose(player, newType)) {
				allowedTypes.add(newType);
			}
		}

		return allowedTypes;
	}

	@Override
	public ActionValidationCode getChooseGameValidationCode(final IPlayerData playerData, final IBasicGameData chosenGame) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.CHOOSE, true);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// the game type must not be played
		else if (!isGameTypeAllowedToChoose(playerData, chosenGame.getGameType())) {
			retVal = ActionValidationCode.CHOOSE_TYPENOTALLOWED;
		}
		// the color is not available for that type
		else if (!chosenGame.getGameType().equals(GameType.PASS) && !chosenGame.getGameType().equals(GameType.WENZ) && !chosenGame.getGameType().equals(GameType.WENZ_TOUT)
				&& !isColorAllowedToChoose(playerData, chosenGame.getGameType(), chosenGame.getColor())) {
			retVal = ActionValidationCode.CHOOSE_COLORNOTALLOWED;
		}

		return retVal;
	}

	@Override
	public ActionValidationCode getGetCardsValidationCode(final IPlayerData playerData) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.GET_RAISE, true);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// player hand is full
		if (isHandComplete(playerData)) {
			retVal = ActionValidationCode.GET_HANDFULL;
		}
		return retVal;
	}

	@Override
	public ActionValidationCode getPlayCardValidationCode(final IPlayerData playerData, final ICard card) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.PLAY, true);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// player is not allowed to play the card
		else if (!getAvailableCards(playerData).contains(card)) {
			retVal = ActionValidationCode.CARD_NOTALLOWED;
		}

		return retVal;
	}

	@Override
	public IPlayerData getPovPlayerData() {

		return povPlayerData;
	}

	@Override
	public ActionValidationCode getRaiseValidationCode(final IPlayerData playerData) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.GET_RAISE, false);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// already raised
		if (playerData.isRaising()) {
			retVal = ActionValidationCode.RAISE_ALREADYRAISED;
		}
		// too much cards
		else if (playerData.getCurrentHand().size() > IRestrictedGameData.MAX_RAISING_HAND_SIZE) {
			retVal = ActionValidationCode.RAISE_TOOMUCHCARDS;
		}
		return retVal;
	}

	@Override
	public ActionValidationCode getStrikeBackValidationCode(final IPlayerData playerData) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.STRIKEBACK, false);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// the back striking player must be the lead player
		else if (!getRestrictedGameData().getLeadPlayerPosition().equals(playerData.getPosition())) {
			retVal = ActionValidationCode.STRIKEBACK_NOTPLAYER;
		}
		return retVal;
	}

	@Override
	public ActionValidationCode getStrikeValidationCode(final IPlayerData playerData) {

		ActionValidationCode retVal = getBasicValidationCode(playerData, GameState.STRIKE, false);
		if (!retVal.equals(ActionValidationCode.VALIDATION_SUCCESS)) {
			return retVal;
		}
		// the striking player must not be mate or lead player
		else if (isLeadPlayerMate(playerData) || getRestrictedGameData().getLeadPlayerPosition().equals(playerData.getPosition())) {
			retVal = ActionValidationCode.STRIKE_NOTOPPONENT;
		}

		return retVal;
	}

	@Override
	public boolean hasSi() {

		return hasSi(povPlayerData);
	}

	@Override
	public boolean hasSi(final IPlayerData player) {

		if (player == null) {
			throw new IllegalArgumentException();
		}
		if (player.getInitialHand() == null) {
			return false;
		}
		for (final ICard card : player.getInitialHand()) {
			if (card.getValue() != CardValue.OBER || card.getValue() != CardValue.UNTER) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isAllowedToChooseGame(final GameType gameType, final CardColor cardColor) {

		return getChooseGameValidationCode(povPlayerData, new BasicGameData(gameType, cardColor)).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToChooseGame(final IBasicGameData chosenGame) {

		return getChooseGameValidationCode(povPlayerData, chosenGame).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToGetCards() {

		return getGetCardsValidationCode(povPlayerData).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToPlayCard(final ICard card) {

		return getPlayCardValidationCode(povPlayerData, card).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToRaise() {

		return getRaiseValidationCode(povPlayerData).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToRunAway() {

		return isAllowedToRunAway(povPlayerData);
	}

	@Override
	public boolean isAllowedToRunAway(final IPlayerData player) {

		return countColor(player, getRestrictedGameData().getColor()) > IRestrictedGameData.CARDS_NEEDED_TO_RUN_AWAY;
	}

	@Override
	public boolean isAllowedToStrike() {

		return getStrikeValidationCode(povPlayerData).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isAllowedToStrikeBack() {

		return getStrikeBackValidationCode(povPlayerData).equals(ActionValidationCode.VALIDATION_SUCCESS);
	}

	@Override
	public boolean isColorAllowedToChoose(final GameType gameType, final CardColor color) {

		return isColorAllowedToChoose(povPlayerData, gameType, color);
	}

	@Override
	public boolean isColorAllowedToChoose(final IPlayerData player, final GameType gameType, final CardColor color) {

		return getAvailableColors(player, gameType).contains(color);
	}

	@Override
	public boolean isExpectedToAct() {

		return isExpectedToAct(povPlayerData);
	}

	@Override
	public boolean isExpectedToAct(final IPlayerData playerData) {

		if (playerData == null) {
			throw new IllegalArgumentException();
		}
		// in a finished game the players should not act
		if (getRestrictedGameData().getGameState().equals(GameState.FINISHED)) {
			return false;
		}
		// players can strike if not on turn
		if (isAllowedToStrike()) {
			return true;
		}
		// players can strike back if not on turn
		if (isAllowedToStrikeBack()) {
			return true;
		}
		// if the the game is in any other state the players should only act
		// if on turn
		return isOnTurn(playerData);
	}

	@Override
	public boolean isGameTypeAllowedToChoose(final GameType newType) {

		return isGameTypeAllowedToChoose(povPlayerData, newType);
	}

	@Override
	public boolean isGameTypeAllowedToChoose(final IPlayerData player, final GameType gameType) {

		if (player == null || gameType == null) {
			throw new IllegalArgumentException();
		}
		if (!GameState.CHOOSE.equals(getRestrictedGameData().getGameState())) {
			return false;
		}

		// pass is always allowed
		if (gameType == GameType.PASS) {
			return true;
		}
		boolean available = false;
		// check if the new type dominates the current type
		if (gameType.dominates(getRestrictedGameData().getGameType())) {
			// check if the new type is allowed to be played dependent of
			// the players hand.
			if (gameType.equals(GameType.SAUSPIEL)) {
				// if player has at least one color and not he sau he is allowed
				// to play.
				available = !getAvailableColors(player, GameType.SAUSPIEL).isEmpty();
			} else if (gameType.equals(GameType.SI)) {
				available = hasSi(player);
			} else {
				available = true;
			}
		}
		return available;
	}

	@Override
	public boolean isHandComplete() {

		return isHandComplete(povPlayerData);
	}

	@Override
	public boolean isHandComplete(final IPlayerData playerData) {

		if (playerData == null) {
			throw new IllegalArgumentException();
		}
		return playerData.getCurrentHand().size() == IRestrictedGameData.MAX_HAND_SIZE;
	}

	@Override
	public boolean isLeadPlayerMate() {

		return isLeadPlayerMate(povPlayerData);
	}

	@Override
	public boolean isLeadPlayerMate(final IPlayerData playerData) {

		if (playerData == null) {
			throw new IllegalArgumentException();
		}
		// no partner game, no mates
		if (!getRestrictedGameData().getGameType().isPartnerGame) {
			return false;
		}
		// player isLeadPlayer;
		if (playerData.getPosition().equals(getRestrictedGameData().getLeadPlayerPosition())) {
			return false;
		}
		// player is in the opponent team
		if (getRestrictedGameData().getTeam(Team.OPPONENT_TEAM) != null && getRestrictedGameData().getTeam(Team.OPPONENT_TEAM).contains(playerData.getPosition())) {
			return false;
		}
		// player is in the player team and thus known as the lead player
		// mate
		if (getRestrictedGameData().getTeam(Team.PLAYER_TEAM) != null && getRestrictedGameData().getTeam(Team.PLAYER_TEAM).contains(playerData.getPosition())) {
			return true;
		}
		// if none of the top triggered, we have to calculate if the player is
		// the mate by checking his hand
		return ownsCard(playerData, getRestrictedGameData().getColor(), CardValue.SAU);
	}

	@Override
	public boolean isOnTurn() {

		return isOnTurn(povPlayerData);
	}

	@Override
	public boolean isOnTurn(final IPlayerData restrictedPlayerData) {

		if (restrictedPlayerData == null || restrictedPlayerData.getPosition() == null) {
			throw new IllegalArgumentException();
		}
		return restrictedPlayerData.getPosition().equals(getRestrictedGameData().getPlayerOnTurnPosition());
	}

	@Override
	public boolean ownsCard(final CardColor color, final CardValue value) {

		return ownsCard(povPlayerData, color, value);
	}

	@Override
	public boolean ownsCard(final IPlayerData player, final CardColor color, final CardValue value) {

		if (player == null || color == null || value == null) {
			throw new IllegalArgumentException();
		}
		if (player.getCurrentHand() == null) {
			return false;
		}
		return player.getCurrentHand().contains(StackHandler.getInstance().getCardByColorAndValue(color, value));
	}

	@Override
	public void setPovPlayerData(final IPlayerData povPlayerData) {

		this.povPlayerData = povPlayerData;
	}

	/**
	 * Basic action Validation.
	 *
	 * @param playerData
	 *            the player data.
	 * @param gameStatus
	 *            the game state.
	 * @param onTurnRequired
	 *            true, if the player be on turn to execute an action.
	 * @return the validation code for the basic validation.
	 */
	private ActionValidationCode getBasicValidationCode(final IPlayerData playerData, final GameState gameStatus, final boolean onTurnRequired) {

		if (playerData == null) {
			throw new IllegalArgumentException();
		}
		// wrong state
		if (!getRestrictedGameData().getGameState().equals(gameStatus)) {
			return ActionValidationCode.STATE_WRONG;
		}
		// not on turn
		if (onTurnRequired && !isOnTurn(playerData)) {
			return ActionValidationCode.TURN_NOTONTURN;
		}
		return ActionValidationCode.VALIDATION_SUCCESS;
	}
}
