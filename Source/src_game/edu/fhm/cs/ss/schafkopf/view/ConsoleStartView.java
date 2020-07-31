package edu.fhm.cs.ss.schafkopf.view;

import java.util.Scanner;

import edu.fhm.cs.ss.schafkopf.view.baseclasses.BaseMultiThreadedView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.IStartView;
import edu.fhm.cs.ss.schafkopf.view.utilities.SharedViewRessources;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveStartController;

/**
 *
 * The settings view is an interactive view, that interacts with a {@link IPrimitiveStartController} but with no data to display.<br>
 * <br>
 *
 * It starts the different views of the application. The user interaction menu is running in a own thread.
 *
 * @author Sebastian Stumpf
 *
 */

public class ConsoleStartView extends BaseMultiThreadedView implements IStartView {

	/**
	 * Interact with the user via a console menu.
	 *
	 * @author Sebastian Stumpf
	 *
	 */
	private class DisplayStartThread extends Thread {
		@Override
		public void run() {

			boolean validInput;
			boolean pause;
			while (!isStopped()) {
				System.out.println();
				System.out.println("Bitte wählen:");
				System.out.printf("%-32s%-32s%n", "1", "Neues Spiel starten");
				System.out.printf("%-32s%-32s%n", "2", "Laufendes Spiel fortführen");
				System.out.printf("%-32s%-32s%n", "3", "Einstellungen");
				System.out.printf("%-32s%-32s%n", "4", "Anwendung beenden");
				try {
					switch (Integer.parseInt(scanner.nextLine())) {
						case 1:
							controller.newGame();
							validInput = true;
							pause = true;
							break;
						case 2:
							if (!controller.resumeGame()) {
								System.out.println("Es ist kein Spiel gespeichert.");
							}
							validInput = true;
							pause = false;
							break;
						case 3:
							controller.changeSettings();
							validInput = true;
							pause = true;
							break;
						case 4:
							scanner.close();
							validInput = true;
							pause = false;
							System.out.println("Bis zum nächsten mal.");
							controller.back();
							scanner.close();
							break;
						default:
							validInput = false;
							pause = false;
							break;
					}
				} catch (final NumberFormatException e) {
					validInput = false;
					pause = false;
				}
				if (!validInput) {
					System.out.println("Falsche Eingabe, bitte 1/2/3/4 eingeben.");
				}
				if (pause) {
					try {
						synchronized (this) {
							// wait for stop call from created view causing interrupt
							this.wait();
						}

					} catch (final InterruptedException e) {
					}
				}
			}
		}
	}

	/**
	 * The controller reference.
	 */
	private final IPrimitiveStartController controller;
	/**
	 * The scanner to read from the command line.
	 */
	private final Scanner scanner;

	/**
	 * Instantiate the view with a given controller.
	 *
	 * @param controller
	 *            the start controller to interact with.
	 */
	public ConsoleStartView(final IPrimitiveStartController controller) {

		super();
		this.controller = controller;
		this.scanner = SharedViewRessources.getScanner();
		// set me as the controller view
		controller.setView(this);
	}

	@Override
	public void start() {

		registerThreads(new DisplayStartThread());
		super.start();
	}

	@Override
	public void update() {

		// there is not data to be changed, so this method is empty.

	}
}
