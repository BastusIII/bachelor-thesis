package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import java.util.Collection;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * The player data all players are allowed to see.<br>
 * <br>
 *
 * Information about the content of the player's hand, for example is not available.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IRestrictedPlayerData {

	/**
	 * @param credit
	 *            the money to be added to the player's credit. Negative values will be subtracted.
	 */
	void addCredit(float credit);

	/**
	 * @param points
	 *            the points to be added to the player's points in a current game.
	 */
	void addPoints(int points);

	@Override
	boolean equals(Object obj);

	/**
	 * @return the player's current money.
	 */
	float getCredit();

	/**
	 * @return the player's name.
	 */
	String getName();

	/**
	 * @return the card the player has played in the current round.
	 */
	ICard getPlayedCard();

	/**
	 * @return the player's points.
	 */
	int getPoints();

	/**
	 * @return the player's position.
	 */
	PlayerPosition getPosition();

	/**
	 * @return the size of the player's hand.
	 */
	int getSizeOfHand();

	/**
	 * @return the player's won cards.
	 */
	Collection<ICard> getWonCards();

	@Override
	int hashCode();

	/**
	 * @return true, if the player accepts the next game start.
	 */
	boolean isAcceptingNextGameStart();

	/**
	 * @return true, if the player is raising.
	 */
	boolean isRaising();

	/**
	 * @return true, if the player is striking.
	 */
	boolean isStriking();

	/**
	 * @return true, if the player is striking back.
	 */
	boolean isStrikingBack();

	/**
	 * @param acceptingNextGameStart
	 *            true, if the player accepts the next game start.
	 */
	void setAcceptingNextGameStart(boolean acceptingNextGameStart);

	/**
	 * @param credit
	 *            the player's money.
	 */
	void setCredit(float credit);

	/**
	 * @param name
	 *            the player's name.
	 */
	void setName(String name);

	/**
	 * @param card
	 *            the card that is played in the current round. null, if no card is played yet.
	 */
	void setPlayedCard(ICard card);

	/**
	 * @param points
	 *            the player's points.
	 */
	void setPoints(int points);

	/**
	 * @param position
	 *            the player's position.
	 */
	void setPosition(PlayerPosition position);

	/**
	 * @param raising
	 *            true if the player is raising.
	 */
	void setRaising(boolean raising);

	/**
	 * @param sizeOfHand
	 *            the sizeOfHand to set
	 */
	void setSizeOfHand(int sizeOfHand);

	/**
	 * @param striking
	 *            true if the player is striking.
	 */
	void setStriking(boolean striking);

	/**
	 * @param strikingBack
	 *            true if the player is striking back.
	 */
	void setStrikingBack(boolean strikingBack);

	/**
	 * @param wonCards
	 *            the player's won cards.
	 */
	void setWonCards(Collection<ICard> wonCards);

	@Override
	String toString();
}
