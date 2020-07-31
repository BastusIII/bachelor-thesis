package edu.fhm.cs.ss.schafkopf.model;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameSettings;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistableObject;
import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistenceObject;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;

/**
 * This data class holds the information about a games settings.
 *
 * @author Sebastian Stumpf
 *
 */
public class GameSettings implements IGameSettings, IPersistenceObject {

	/**
	 * Cost of a basic game.
	 */
	private int basicCharge;
	/**
	 * Name of the bottom player.
	 */
	private String nameBottom;
	/**
	 * Name of the left player.
	 */
	private String nameLeft;
	/**
	 * Name of the right player.
	 */
	private String nameRight;
	/**
	 * Name of the top player.
	 */
	private String nameTop;
	/**
	 * Solo multiplier.
	 */
	private int soloMultiplier;
	/**
	 * Start money of the players.
	 */
	private int startMoney;

	/**
	 * Instantiates settings with default values.
	 */
	public GameSettings() {

		this.nameBottom = DEFVAL_NAME_BOTTOM;
		this.nameLeft = DEFVAL_NAME_LEFT;
		this.nameTop = DEFVAL_NAME_TOP;
		this.nameRight = DEFVAL_NAME_RIGHT;
		this.soloMultiplier = DEFVAL_SOLO_MULTIPLIER;
		this.basicCharge = DEFVAL_BASIC_CHARGE;
		this.startMoney = DEFVAL_START_MONEY;
	}

	/**
	 * Copy Constructor.
	 *
	 * @param toCopy
	 *            the settings to copy,
	 */
	public GameSettings(final IGameSettings toCopy) {

		adoptSettings(toCopy);
	}

	@Override
	public void adoptSettings(final IGameSettings toAdopt) {

		this.nameBottom = toAdopt.getNameBottom();
		this.nameLeft = toAdopt.getNameLeft();
		this.nameTop = toAdopt.getNameTop();
		this.nameRight = toAdopt.getNameRight();
		this.soloMultiplier = toAdopt.getSoloMultiplier();
		this.basicCharge = toAdopt.getBasicCharge();
		this.startMoney = toAdopt.getStartMoney();
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
		final GameSettings other = (GameSettings) obj;
		if (basicCharge != other.basicCharge) {
			return false;
		}
		if (nameBottom == null) {
			if (other.nameBottom != null) {
				return false;
			}
		} else if (!nameBottom.equals(other.nameBottom)) {
			return false;
		}
		if (nameLeft == null) {
			if (other.nameLeft != null) {
				return false;
			}
		} else if (!nameLeft.equals(other.nameLeft)) {
			return false;
		}
		if (nameRight == null) {
			if (other.nameRight != null) {
				return false;
			}
		} else if (!nameRight.equals(other.nameRight)) {
			return false;
		}
		if (nameTop == null) {
			if (other.nameTop != null) {
				return false;
			}
		} else if (!nameTop.equals(other.nameTop)) {
			return false;
		}
		if (soloMultiplier != other.soloMultiplier) {
			return false;
		}
		if (startMoney != other.startMoney) {
			return false;
		}
		return true;
	}

	@Override
	public int getBasicCharge() {

		return basicCharge;
	}

	@Override
	public String getFilename() {

		return FILENAME;
	}

	@Override
	public String getName(final PlayerPosition position) {

		switch (position) {
			case BOTTOM:
				return getNameBottom();
			case LEFT:
				return getNameLeft();
			case RIGHT:
				return getNameRight();
			case TOP:
				return getNameTop();
			default:
				return "";
		}
	}

	@Override
	public String getNameBottom() {

		return nameBottom;
	}

	@Override
	public String getNameLeft() {

		return nameLeft;
	}

	@Override
	public String getNameRight() {

		return nameRight;
	}

	@Override
	public String getNameTop() {

		return nameTop;
	}

	@Override
	public IPersistableObject getPersistableObject() {

		// all content is saved
		return this;
	}

	@Override
	public IPersistenceObject getPersistenceObject() {

		// all content is saved
		return this;
	}

	@Override
	public int getSoloMultiplier() {

		return soloMultiplier;
	}

	@Override
	public int getStartMoney() {

		return startMoney;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + basicCharge;
		result = prime * result + (nameBottom == null ? 0 : nameBottom.hashCode());
		result = prime * result + (nameLeft == null ? 0 : nameLeft.hashCode());
		result = prime * result + (nameRight == null ? 0 : nameRight.hashCode());
		result = prime * result + (nameTop == null ? 0 : nameTop.hashCode());
		result = prime * result + soloMultiplier;
		result = prime * result + startMoney;
		return result;
	}

	@Override
	public void setBasicCharge(final int charge) {

		this.basicCharge = charge;

	}

	@Override
	public void setName(final PlayerPosition position, final String name) {

		switch (position) {
			case BOTTOM:
				setNameBottom(name);
				break;
			case LEFT:
				setNameLeft(name);
				break;
			case RIGHT:
				setNameRight(name);
				break;
			case TOP:
				setNameTop(name);
				break;
			default:
				break;
		}

	}

	@Override
	public void setNameBottom(final String nameBottom) {

		this.nameBottom = nameBottom;
	}

	@Override
	public void setNameLeft(final String nameLeft) {

		this.nameLeft = nameLeft;
	}

	@Override
	public void setNameRight(final String nameRight) {

		this.nameRight = nameRight;
	}

	@Override
	public void setNameTop(final String nameTop) {

		this.nameTop = nameTop;
	}

	@Override
	public void setSoloMultiplier(final int multiplier) {

		this.soloMultiplier = multiplier;

	}

	@Override
	public void setStartMoney(final int startMoney) {

		this.startMoney = startMoney;
	}
}
