package edu.fhm.cs.ss.schafkopf.viewaccessible.controller.gameactions.interfaces;

import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.ICard;

/**
 * With this action a player can play a card in a game.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPlayCardAction extends IAction {

	/**
	 * @return the card the player wants to play.
	 */
	ICard getChosenCard();

}