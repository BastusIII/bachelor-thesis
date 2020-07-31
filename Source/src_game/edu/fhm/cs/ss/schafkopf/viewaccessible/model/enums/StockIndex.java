package edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums;

/**
 * Stock index defines what to do with the stock at the end of a game.
 * 
 * @author Sebastian Stumpf
 * 
 */
public enum StockIndex {

	/** The player team lost and has to double up the stock. */
	DOUBLE,
	/** The stock can be ignored in this game. */
	IGNORE,
	/** All players passed and have to pay in the basic charge. */
	PAY_IN,
	/** The player team won and may distribute the stock between themselfes. */
	PAY_OUT;
}
