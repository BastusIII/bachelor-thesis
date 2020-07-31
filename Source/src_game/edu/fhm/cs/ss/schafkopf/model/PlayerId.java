package edu.fhm.cs.ss.schafkopf.model;

import java.util.UUID;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPlayerId;

/**
 * This data class is holding the information to identify a player.<br>
 * <br>
 * 
 * As there are 4 different positions in a game and each player gets a combination of {@link UUID} and this position, the PlayerIds in a game are always and
 * definitely unique. This class is immutable.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class PlayerId implements IPlayerId {
	/** The players position. */
	private final PlayerPosition position;
	/** The generated UUID for this player. */
	private final UUID uuid;

	/**
	 * Instantiate a player id with the given parameters.
	 * 
	 * @param position
	 *            the player position.
	 * @param value
	 *            the generated UUID value.
	 */
	public PlayerId(final PlayerPosition position, final UUID value) {

		this.position = position;
		this.uuid = value;

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
		final PlayerId other = (PlayerId) obj;
		if (position != other.position) {
			return false;
		}
		if (uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!uuid.equals(other.uuid)) {
			return false;
		}
		return true;
	}

	@Override
	public PlayerPosition getPosition() {

		return position;
	}

	@Override
	public UUID getUuid() {

		return uuid;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (position == null ? 0 : position.hashCode());
		result = prime * result + (uuid == null ? 0 : uuid.hashCode());
		return result;
	}

}
