package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * State of a Schafkopf game.<br>
 * <br>
 * 
 * It is used by players to check what actions they have to make.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum GameState {
	/**
	 * Players can choose their game.
	 */
	CHOOSE,
	/**
	 * Players can play cards.
	 */
	PLAY,
	/**
	 * Players can strike.
	 */
	STRIKE,
	/**
	 * Players can strike back.
	 */
	STRIKEBACK,
	/**
	 * Player can get cards, or raise.
	 */
	GET_RAISE,
	/**
	 * Game is finished.
	 */
	FINISHED;
}
