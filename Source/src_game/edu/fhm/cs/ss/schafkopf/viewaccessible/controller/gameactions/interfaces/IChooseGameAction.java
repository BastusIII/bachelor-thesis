package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IBasicGameData;

/**
 * With this action a player can choose the game he wants to play.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IChooseGameAction extends IAction {
	/**
	 * @return the player's chosen game.
	 */
	IBasicGameData getChosenGame();

}
