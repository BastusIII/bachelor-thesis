package edu.fhm.cs.ss.schafkopf.model;

import java.util.Collection;
import java.util.HashSet;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;

/**
 * This data class holds the restricted information about a player.<br>
 * <br>
 *
 * The information not visible to all players is not contained.
 *
 * @author Sebastian Stumpf
 *
 */
public class RestrictedPlayerData implements IRestrictedPlayerData {

	/**
	 * The number of cards in the player's current hand.
	 */
	private int sizeOfHand;
	/**
	 * Players money.
	 */
	private float credit;
	/**
	 * Players name.
	 */
	private String name;
	/**
	 * Players played card. null if no card is played in the current round yet..
	 */
	private ICard playedCard;
	/**
	 * Players points.
	 */
	private int points;
	/**
	 * Players position.
	 */
	private PlayerPosition position;
	/**
	 * Player has raised.
	 */
	private boolean raising;
	/**
	 * Player struck.
	 */
	private boolean striking;
	/**
	 * Player struck back.
	 */
	private boolean strikingBack;
	/**
	 * Player accepts next game start.
	 */
	private boolean acceptingNextGameStart;
	/**
	 * Player's won cards.
	 */
	private Collection<ICard> wonCards;

	/**
	 * Default constructor. Size of hand is set to 0.
	 */
	public RestrictedPlayerData() {

		super();
		sizeOfHand = 0;
	}

	/**
	 * Constructor that makes a deep copy of the necessary fields from playerData, restricted to information visible to all players.
	 *
	 * @param playerData
	 *            the player data containing the values to copy.
	 */
	public RestrictedPlayerData(final IPlayerData playerData) {

		super();
		this.acceptingNextGameStart = playerData.isAcceptingNextGameStart();
		this.credit = playerData.getCredit();
		this.sizeOfHand = playerData.getSizeOfHand();
		this.name = playerData.getName();
		this.playedCard = playerData.getPlayedCard();
		this.points = playerData.getPoints();
		this.position = playerData.getPosition();
		this.raising = playerData.isRaising();
		this.striking = playerData.isStriking();
		this.strikingBack = playerData.isStrikingBack();
		this.wonCards = playerData.getWonCards() == null ? null : new HashSet<ICard>(playerData.getWonCards());
	}

	@Override
	public void addCredit(final float credit) {

		this.credit += credit;
	}

	@Override
	public void addPoints(final int points) {

		this.points += points;
	}

	/**
	 * Pay attention, size of hand is not consistently set in the copy constructor, thus do not check it in equals. See interface documentation for more
	 * details.
	 */
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
		final RestrictedPlayerData other = (RestrictedPlayerData) obj;
		if (acceptingNextGameStart != other.acceptingNextGameStart) {
			return false;
		}
		if (Float.floatToIntBits(credit) != Float.floatToIntBits(other.credit)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (playedCard == null) {
			if (other.playedCard != null) {
				return false;
			}
		} else if (!playedCard.equals(other.playedCard)) {
			return false;
		}
		if (points != other.points) {
			return false;
		}
		if (position != other.position) {
			return false;
		}
		if (raising != other.raising) {
			return false;
		}
		if (striking != other.striking) {
			return false;
		}
		if (strikingBack != other.strikingBack) {
			return false;
		}
		if (wonCards == null) {
			if (other.wonCards != null) {
				return false;
			}
		} else if (!wonCards.equals(other.wonCards)) {
			return false;
		}
		return true;
	}

	@Override
	public float getCredit() {

		return credit;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public ICard getPlayedCard() {

		return playedCard;
	}

	@Override
	public int getPoints() {

		return points;
	}

	@Override
	public PlayerPosition getPosition() {

		return position;
	}

	@Override
	public int getSizeOfHand() {

		return this.sizeOfHand;
	}

	@Override
	public Collection<ICard> getWonCards() {

		return wonCards;
	}

	/**
	 * Pay attention, size of hand is not consistently set in the copy constructor, thus do not use it in hash code. See interface documentation for more
	 * details.
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (acceptingNextGameStart ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(credit);
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (playedCard == null ? 0 : playedCard.hashCode());
		result = prime * result + points;
		result = prime * result + (position == null ? 0 : position.hashCode());
		result = prime * result + (raising ? 1231 : 1237);
		result = prime * result + (striking ? 1231 : 1237);
		result = prime * result + (strikingBack ? 1231 : 1237);
		result = prime * result + (wonCards == null ? 0 : wonCards.hashCode());
		return result;
	}

	@Override
	public boolean isAcceptingNextGameStart() {

		return acceptingNextGameStart;
	}

	@Override
	public boolean isRaising() {

		return raising;
	}

	@Override
	public boolean isStriking() {

		return striking;
	}

	@Override
	public boolean isStrikingBack() {

		return strikingBack;
	}

	@Override
	public void setAcceptingNextGameStart(final boolean acceptingNextGameStart) {

		this.acceptingNextGameStart = acceptingNextGameStart;
	}

	@Override
	public void setCredit(final float credit) {

		this.credit = credit;
	}

	@Override
	public void setName(final String name) {

		this.name = name;
	}

	@Override
	public void setPlayedCard(final ICard card) {

		this.playedCard = card;
	}

	@Override
	public void setPoints(final int points) {

		this.points = points;
	}

	@Override
	public void setPosition(final PlayerPosition position) {

		this.position = position;
	}

	@Override
	public void setRaising(final boolean raising) {

		this.raising = raising;
	}

	@Override
	public void setSizeOfHand(final int sizeOfHand) {

		this.sizeOfHand = sizeOfHand;
	}

	@Override
	public void setStriking(final boolean striking) {

		this.striking = striking;
	}

	@Override
	public void setStrikingBack(final boolean strikingBack) {

		this.strikingBack = strikingBack;
	}

	@Override
	public void setWonCards(final Collection<ICard> wonCards) {

		this.wonCards = wonCards;
	}

	@Override
	public String toString() {

		return getName();
	}
}
