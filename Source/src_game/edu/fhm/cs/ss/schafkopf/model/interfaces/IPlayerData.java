package edu.fhm.cs.ss.schafkopf.model.interfaces;

import java.util.List;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedPlayerData;

/**
 * This interface extends {@link IRestrictedPlayerData} by full access methods on a player data instance only accessible for the connected player view.
 *
 * Other views must no have access to this interface, because they would get too much information about the game otherwise.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IPlayerData extends IRestrictedPlayerData {

	@Override
	boolean equals(Object obj);

	/**
	 * @return the players chosen game with all information set. If not yet chosen, null.
	 */
	IBasicGameData getChosenGame();

	/**
	 * The players current hand contains all the cards the player currently has.
	 *
	 * Every card he plays is removed. Cards he gets in the initial round are added.
	 *
	 * @return the players current hand.
	 */
	List<ICard> getCurrentHand();

	@Override
	int hashCode();

	/**
	 *
	 * @param chosenGame
	 *            the players chosen game.
	 */
	void setChosenGame(IBasicGameData chosenGame);

	/**
	 * Contains all cards the player had from the beginning of the current game.
	 *
	 * Cards he gets are added. This List is cleared when a new game starts.
	 *
	 * @return the players initial hand.
	 */
	List<ICard> getInitialHand();

	/**
	 * @param currentHand
	 *            the players current hand to set. Should not be null.
	 */
	void setCurrentHand(List<ICard> currentHand);

	/**
	 * @param initialHand
	 *            the players initial hand to set. Should not be null.
	 */
	void setInitialHand(List<ICard> initialHand);
}