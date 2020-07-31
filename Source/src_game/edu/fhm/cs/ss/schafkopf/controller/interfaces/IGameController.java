package edu.fhm.cs.ss.schafkopf.controller.interfaces;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IGameData;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveGameController;

/**
 * This interface extends the methods from {@link IPrimitiveGameController} by methods not available for views.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IGameController extends IPrimitiveGameController, IController {

	/**
	 * @return the full access game data instance.
	 */
	IGameData getGameData();

	/**
	 * Notify all subscribed player views.
	 */
	void notifyPlayers();

	/**
	 * @param gameData
	 *            the full access game data instance to set.
	 */
	void setGameData(IGameData gameData);
}
