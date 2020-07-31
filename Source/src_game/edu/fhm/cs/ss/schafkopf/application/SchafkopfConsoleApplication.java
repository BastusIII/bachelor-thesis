package edu.fhm.cs.ss.schafkopf.application;

import edu.fhm.cs.ss.schafkopf.controller.StartController;
import edu.fhm.cs.ss.schafkopf.controller.interfaces.IStartController;
import edu.fhm.cs.ss.schafkopf.model.utilities.XMLFilePersistenceHandler;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;
import edu.fhm.cs.ss.schafkopf.view.ConsoleStartView;

/**
 * The entry point for a Schafkopf console game.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class SchafkopfConsoleApplication {
	/**
	 * The main method, to start a Schafkopf game.
	 * 
	 * @param args
	 *            are not used.
	 */
	public static void main(final String... args) {

		System.out.println("Willkommen zu Schafkopf");
		System.out.println();

		// persistence handler used in controllers
		final IPersistenceHandler persistenceHandler = new XMLFilePersistenceHandler("console_player_", "data", "persistence");

		// initialize start controller and view.
		final IStartController consoleStartController = new StartController(persistenceHandler);
		consoleStartController.setView(new ConsoleStartView(consoleStartController));
		consoleStartController.start();
	}
}
