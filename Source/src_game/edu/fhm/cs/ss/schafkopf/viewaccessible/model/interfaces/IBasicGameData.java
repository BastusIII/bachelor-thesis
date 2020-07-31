package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.CardColor;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameType;

/**
 * This interface defines the color and the type of a game.<br>
 * <br>
 * 
 * It is used to define a players chosen game. Only these values are needed for that.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IBasicGameData {

	@Override
	boolean equals(Object obj);

	/**
	 * 
	 * @return the card color.
	 */
	CardColor getColor();

	/**
	 * 
	 * @return the game type.
	 */
	GameType getGameType();

	@Override
	int hashCode();

	/**
	 * @param color
	 *            the card color to set.
	 */
	void setColor(CardColor color);

	/**
	 * 
	 * @param gameType
	 *            the game type to set.
	 */
	void setGameType(GameType gameType);
}