package edu.fhm.cs.ss.schafkopf.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPlayerData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * This data class holds the complete information about a player. It extends the restricted information provided by {@link RestrictedPlayerData} with elements,
 * that are not available for all player views.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class PlayerData extends RestrictedPlayerData implements IPlayerData {
	/**
	 * The player's chosen game. null if no game chosen yet.
	 */
	private IBasicGameData chosenGame;
	/**
	 * The player's current hand.
	 */
	private List<ICard> currentHand;
	/**
	 * The player's initial hand.
	 */
	private List<ICard> initialHand;

	/**
	 * Instantiate a player data instance with the given parameters.
	 * 
	 * @param settings
	 *            the game settings, name and start money are set to the values from there.
	 * @param position
	 *            the players position.
	 */
	public PlayerData(final IGameSettings settings, final PlayerPosition position) {

		this(settings.getName(position), settings.getStartMoney(), position);

	}

	/**
	 * Copy constructor copy.
	 * 
	 * @param playerData
	 *            the player data to copy.
	 */
	public PlayerData(final IPlayerData playerData) {

		// super initialization
		super(playerData);

		// own elements
		this.currentHand = playerData.getCurrentHand() == null ? null : new ArrayList<ICard>(playerData.getCurrentHand());
		this.initialHand = playerData.getInitialHand() == null ? null : new ArrayList<ICard>(playerData.getInitialHand());
	}

	/**
	 * Instantiate player data with the given arguments.
	 * 
	 * @param name
	 *            the players name.
	 * @param credit
	 *            the players money.
	 * @param position
	 *            the players position.
	 */
	public PlayerData(final String name, final int credit, final PlayerPosition position) {

		// super initialization
		super();
		super.setAcceptingNextGameStart(false);
		super.setCredit(credit);
		super.setName(name);
		super.setPlayedCard(null);
		super.setPoints(0);
		super.setPosition(position);
		super.setRaising(false);
		super.setStriking(false);
		super.setStrikingBack(false);
		super.setWonCards(new HashSet<ICard>());
		// own elements
		this.chosenGame = null;
		this.currentHand = new ArrayList<ICard>();
		this.initialHand = new ArrayList<ICard>();
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerData other = (PlayerData) obj;
		if (currentHand == null) {
			if (other.currentHand != null) {
				return false;
			}
		} else if (!currentHand.equals(other.currentHand)) {
			return false;
		}
		if (initialHand == null) {
			if (other.initialHand != null) {
				return false;
			}
		} else if (!initialHand.equals(other.initialHand)) {
			return false;
		}
		return true;
	}

	@Override
	public IBasicGameData getChosenGame() {

		return chosenGame;
	}

	@Override
	public List<ICard> getCurrentHand() {

		return currentHand;
	}

	@Override
	public List<ICard> getInitialHand() {

		return initialHand;
	}

	@Override
	public int getSizeOfHand() {

		if (this.currentHand == null) {
			return 0;
		}
		return this.currentHand.size();
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (currentHand == null ? 0 : currentHand.hashCode());
		result = prime * result + (initialHand == null ? 0 : initialHand.hashCode());
		return result;
	}

	@Override
	public void setChosenGame(final IBasicGameData chosenGame) {

		this.chosenGame = chosenGame;
	}

	@Override
	public void setCurrentHand(final List<ICard> currentHand) {

		this.currentHand = currentHand;
	}

	@Override
	public void setInitialHand(final List<ICard> initialHand) {

		this.initialHand = initialHand;
	}

}
