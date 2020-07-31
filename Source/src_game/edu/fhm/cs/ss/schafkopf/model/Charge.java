package edu.fhm.cs.ss.schafkopf.model;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.StockIndex;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICharge;

/**
 * This data class holds the information about a game's charge.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class Charge implements ICharge {

	/**
	 * The basic charge for the current gameType.
	 */
	private int basic;
	/**
	 * Team high cards in a row.
	 */
	private int bounty;
	/**
	 * Multiplier for exclusive games (Tout).
	 */
	private int exclusiveMultiplier;
	/**
	 * Each player can increase the multiplier after his first 4 cards. It is doubled for each increase.
	 */
	private int initialMultiplier;
	/**
	 * If a party has struck a lead player, the game multiplier is doubled for each strike. A game can be struck up to 2 times.
	 */
	private int strikeMultiplier;
	/**
	 * Player team points < 29 || opponent team points < 30 -> 1. One team points = 0 -> 2. If exclusive multiplier active, schneider is deactivated.
	 */
	private int schneider;
	/**
	 * What has been done with the stock.
	 */
	private StockIndex stockIndex;
	/**
	 * The value that was paid out of or into the stock.
	 */
	private int stockValue;
	/**
	 * The total game value is calculated by adding the basic, bounty*basic and schneider*basic values and multiply all with the total game multiplier. In
	 * exclusive games, the schneider value is always 0. The stock is doubled if the player team loses or paid out if the player team wins.
	 */
	private int totalCharge;

	/**
	 * Default constructor instantiates all values with default values.
	 */
	public Charge() {

		super();
		this.stockValue = 0;
		this.basic = 0;
		this.bounty = 0;
		this.schneider = 0;
		this.exclusiveMultiplier = 0;
		this.initialMultiplier = 0;
		this.strikeMultiplier = 0;
		this.totalCharge = 0;
		this.stockIndex = StockIndex.IGNORE;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param charge
	 *            the charge to copy.
	 */
	public Charge(final ICharge charge) {

		super();
		this.stockValue = charge.getStockValue();
		this.basic = charge.getBasic();
		this.bounty = charge.getBounty();
		this.schneider = charge.getSchneider();
		this.exclusiveMultiplier = charge.getExclusiveMultiplier();
		this.initialMultiplier = charge.getInitialMultiplier();
		this.strikeMultiplier = charge.getStrikeMultiplier();
		this.totalCharge = charge.getTotalCharge();
		this.stockIndex = charge.getStockIndex();
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
		final Charge other = (Charge) obj;
		if (basic != other.basic) {
			return false;
		}
		if (bounty != other.bounty) {
			return false;
		}
		if (exclusiveMultiplier != other.exclusiveMultiplier) {
			return false;
		}
		if (initialMultiplier != other.initialMultiplier) {
			return false;
		}
		if (schneider != other.schneider) {
			return false;
		}
		if (stockIndex != other.stockIndex) {
			return false;
		}
		if (stockValue != other.stockValue) {
			return false;
		}
		if (strikeMultiplier != other.strikeMultiplier) {
			return false;
		}
		if (totalCharge != other.totalCharge) {
			return false;
		}
		return true;
	}

	@Override
	public int getBasic() {

		return basic;
	}

	@Override
	public int getBounty() {

		return bounty;
	}

	@Override
	public int getExclusiveMultiplier() {

		return exclusiveMultiplier;
	}

	@Override
	public int getInitialMultiplier() {

		return initialMultiplier;
	}

	@Override
	public int getSchneider() {

		return schneider;
	}

	@Override
	public StockIndex getStockIndex() {

		return stockIndex;
	}

	@Override
	public int getStockValue() {

		return stockValue;
	}

	@Override
	public int getStrikeMultiplier() {

		return strikeMultiplier;
	}

	@Override
	public int getTotalCharge() {

		return totalCharge;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + basic;
		result = prime * result + bounty;
		result = prime * result + exclusiveMultiplier;
		result = prime * result + initialMultiplier;
		result = prime * result + schneider;
		result = prime * result + (stockIndex == null ? 0 : stockIndex.hashCode());
		result = prime * result + stockValue;
		result = prime * result + strikeMultiplier;
		result = prime * result + totalCharge;
		return result;
	}

	@Override
	public void setBasic(final int basic) {

		this.basic = basic;
	}

	@Override
	public void setBounty(final int bounty) {

		this.bounty = bounty;
	}

	@Override
	public void setExclusiveMultiplier(final int exclusiveMultiplier) {

		this.exclusiveMultiplier = exclusiveMultiplier;
	}

	@Override
	public void setInitialMultiplier(final int initialMultiplier) {

		this.initialMultiplier = initialMultiplier;
	}

	@Override
	public void setSchneider(final int schneider) {

		this.schneider = schneider;
	}

	@Override
	public void setStockIndex(final StockIndex stockIndex) {

		this.stockIndex = stockIndex;
	}

	@Override
	public void setStockValue(final int stockValue) {

		this.stockValue = stockValue;
	}

	@Override
	public void setStrikeMultiplier(final int retourMultiplier) {

		this.strikeMultiplier = retourMultiplier;
	}

	@Override
	public void setTotalCharge(final int totalCharge) {

		this.totalCharge = totalCharge;
	}

}
