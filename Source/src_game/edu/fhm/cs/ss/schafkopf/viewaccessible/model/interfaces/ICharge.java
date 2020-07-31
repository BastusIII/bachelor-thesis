package edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.StockIndex;

/**
 * The charge information in a Schafkopf game.<br>
 * <br>
 *
 * The defined methods offer information about the value of a current game.
 *
 * @author Sebastian Stumpf
 *
 */
public interface ICharge {

	@Override
	boolean equals(Object obj);

	/**
	 * The basic value is the value for the game in cent, without any additional charge.
	 *
	 * @return the basic value for this game.
	 */
	int getBasic();

	/**
	 * The bounty is the high cards in a row per team at the end of the game.
	 *
	 * @return the bounty for this game, the number of high cards in row.
	 */
	int getBounty();

	/**
	 * The charge in an exclusive game is multiplied by 2.
	 *
	 * @return the exclusive multiplier. 1 for exclusive game's, 0 for other ones.
	 */
	int getExclusiveMultiplier();

	/**
	 * For each players raise, this multiplier is growing by 1.
	 *
	 * @return the initial multiplier, the number the players have raised.
	 */
	int getInitialMultiplier();

	/**
	 * The player team is schneider if it has less than 30 points, schwarz in no cards won.
	 *
	 * @return the schneider value for this game, 0 if none, 1 if schneider, 2 if schwarz.
	 */
	int getSchneider();

	/**
	 * @return the stock index, giving information about what to do with the stock.
	 */
	StockIndex getStockIndex();

	/**
	 *
	 * @return the value of the stock costs for a single player.
	 */
	int getStockValue();

	/**
	 * The opponent players can strike once per team and the player can strike back once. The maximum her is thus 2.
	 *
	 * @return the number of times the players stroke.
	 */
	int getStrikeMultiplier();

	/**
	 * @return he total charge with all bonuses calculated, for the current game.
	 */
	int getTotalCharge();

	@Override
	int hashCode();

	/**
	 * @param basic
	 *            the basic value for this game.
	 */
	void setBasic(int basic);

	/**
	 * @param bounty
	 *            the bounty for this game.
	 */
	void setBounty(int bounty);

	/**
	 * @param exclusiveMultiplier
	 *            this game's exclusive multiplier.
	 */
	void setExclusiveMultiplier(int exclusiveMultiplier);

	/**
	 *
	 * @param initialMultiplier
	 *            this game's initial multiplier.
	 */
	void setInitialMultiplier(int initialMultiplier);

	/**
	 * @param schneider
	 *            this game's schneider value.
	 */
	void setSchneider(int schneider);

	/**
	 * @param stockIndex
	 *            this game's stock index.
	 */
	void setStockIndex(StockIndex stockIndex);

	/**
	 * @param stockValue
	 *            this game's stock value.
	 */
	void setStockValue(int stockValue);

	/**
	 * @param retourMultiplier
	 *            this game's strike multiplier.
	 */
	void setStrikeMultiplier(int retourMultiplier);

	/**
	 * @param totalCharge
	 *            this game's total charge.
	 */
	void setTotalCharge(int totalCharge);
}