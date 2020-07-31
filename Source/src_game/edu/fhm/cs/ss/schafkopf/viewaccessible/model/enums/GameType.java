package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * Type of a Schafkopf game.<br>
 * <br>
 * 
 * This Enum has some public attributes that give more information about the value. A function is offered, to check if a game type dominates an other game type.
 * *
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum GameType {

	/** The game type Farbwenz. */
	FARBWENZ("Farbwenz", 1, true, true, false, false),
	/** The game type Farbwenz tout. */
	FARBWENZ_TOUT("Farbwenz Tout", 3, true, true, false, true),
	/** The game type Pass, if a player does not want to play a game, he can pass. */
	PASS("Pass", -1, false, false, false, false),
	/** The game type Sauspiel. */
	SAUSPIEL("Sauspiel", 0, false, true, true, false),
	/** The game type Si. */
	SI("Si", 5, false, false, false, false),
	/** The game type Solo. */
	SOLO("Solo", 2, false, true, false, false),
	/** The game type Solo tout. */
	SOLO_TOUT("Solo Tout", 4, false, true, false, true),
	/** The game type Wenz. */
	WENZ("Wenz", 1, true, false, false, false),
	/** The game type Wenz tour. */
	WENZ_TOUT("Wenz Tout", 3, true, false, false, true);

	/**
	 * Indicates if the game is an exclusive game like a tout.
	 */
	public final boolean isExclusive;
	/**
	 * Indicates if the game is a partner game.
	 */
	public final boolean isPartnerGame;
	/**
	 * Indicates if the game is a Farbwenz or Wenz.
	 */
	public final boolean isWenz;
	/**
	 * The name of the game.
	 */
	public final String name;
	/**
	 * Indicates if the game needs a color. Wenz and Si and passes don't need colors.
	 */
	public final boolean needsColor;
	/**
	 * The weight of the game, used to calculate which games dominate the others.
	 */
	public final int weight;

	/**
	 * Private Constructor used to set the game types attributes.
	 * 
	 * @param name
	 *            the name.
	 * @param weight
	 *            the weight.
	 * @param isWenz
	 *            true if Wenz or Farbwenz.
	 * @param needsColor
	 *            true if game type needs color.
	 * @param isPartnerGame
	 *            true if game type is a partner game.
	 * @param isExclusive
	 *            true if game type is exclusive.
	 */
	private GameType(final String name, final int weight, final boolean isWenz, final boolean needsColor, final boolean isPartnerGame, final boolean isExclusive) {

		this.weight = weight;
		this.isWenz = isWenz;
		this.name = name;
		this.needsColor = needsColor;
		this.isPartnerGame = isPartnerGame;
		this.isExclusive = isExclusive;
	}

	/**
	 * Compares two game types.
	 * 
	 * Typical use: "If the guy before me said he wants to play game type a, am I now allowed to play game type b?" Then use: b.dominates(a) to check if you may
	 * play. Pay attention, pass is always allowed to play but does not dominate any game type.
	 * 
	 * @param gameType
	 *            the old game type.
	 * @return true if this dominates the given game type.
	 */
	public boolean dominates(final GameType gameType) {

		if (gameType == null) {
			return true;
		}
		return this.weight > gameType.weight;
	}

	@Override
	public String toString() {

		return this.name;
	}
}
