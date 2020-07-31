package edu.fhm.cs.ss.schafkopf.model.interfaces;

import java.util.List;
import java.util.Map;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IActionData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IRestrictedGameData;

/**
 * This interface extends {@link IRestrictedGameData} by full access methods on a game data instance not accessible for the player views.
 *
 * The declared methods would give the player views too much information about the game. Players must never be able to see the other players hands for example.
 *
 * @author Sebastian Stumpf
 *
 */
public interface IGameData extends IRestrictedGameData, IPersistableObject, IPersistenceObject {

	/**
	 * Filename of the persistence object.
	 */
	final String FILENAME = "CURRENT_GAME";

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	/**
	 * @return the action buffer used to buffer actions not executed instantly.
	 */
	Map<PlayerPosition, IActionData> getActionBuffer();

	/**
	 * @return the map of full access player data, indicated by their position.
	 */
	Map<PlayerPosition, IPlayerData> getPlayerDatas();

	/**
	 * @return the games card stack as an ArrayList.
	 */
	List<ICard> getStack();

	/**
	 * @param actionBuffer
	 *            the action buffer to set. It should never be set to null.
	 */
	void setActionBuffer(Map<PlayerPosition, IActionData> actionBuffer);

	/**
	 * @param playerDatas
	 *            the map of player data to set. It should never be set to null.
	 */
	void setPlayerDatas(Map<PlayerPosition, IPlayerData> playerDatas);

	/**
	 * @param stack
	 *            the game stack to set. It should never be set to null.
	 */
	void setStack(List<ICard> stack);
}