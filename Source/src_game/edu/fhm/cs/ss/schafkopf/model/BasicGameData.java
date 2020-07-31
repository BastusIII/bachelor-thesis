package edu.fhm.cs.ss.schafkopf.model;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;

/**
 * This data class holds the information about a games color and type.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class BasicGameData implements IBasicGameData {

	/** The game's color. */
	private CardColor color;
	/** The game's type. */
	private GameType gameType;

	/**
	 * Default constructor, all attributes are set to null.
	 */
	public BasicGameData() {

		this.color = null;
		this.gameType = null;
	}

	/**
	 * Instantiate the basic game data with the given parameters.
	 * 
	 * @param gameType
	 *            the game type.
	 * @param color
	 *            the card color.
	 */
	public BasicGameData(final GameType gameType, final CardColor color) {

		super();
		this.gameType = gameType;
		this.color = color;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param game
	 *            basic game data to copy.
	 */
	public BasicGameData(final IBasicGameData game) {

		this(game.getGameType(), game.getColor());
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
		final BasicGameData other = (BasicGameData) obj;
		if (color != other.color) {
			return false;
		}
		if (gameType != other.gameType) {
			return false;
		}
		return true;
	}

	@Override
	public CardColor getColor() {

		return color;
	}

	@Override
	public GameType getGameType() {

		return gameType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (color == null ? 0 : color.hashCode());
		result = prime * result + (gameType == null ? 0 : gameType.hashCode());
		return result;
	}

	@Override
	public void setColor(final CardColor color) {

		this.color = color;

	}

	@Override
	public void setGameType(final GameType gameType) {

		this.gameType = gameType;

	}
}
