package edu.fhm.cs.ss.schafkopf.view;

import java.util.Scanner;

import edu.fhm.cs.ss.schafkopf.view.baseclasses.BaseMultiThreadedView;
import edu.fhm.cs.ss.schafkopf.view.interfaces.ISettingsView;
import edu.fhm.cs.ss.schafkopf.view.utilities.SharedViewRessources;
import edu.fhm.cs.ss.schafkopf.viewaccessible.controller.interfaces.IPrimitiveSettingsController;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.PlayerPosition;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.interfaces.IPrimitiveGameSettings;

/**
 * The settings view is an interactive view, that interacts with a {@link IPrimitiveSettingsController} and displays {@link IPrimitiveGameSettings}.<br>
 * <br>
 * 
 * The display and interaction task are each taken over by one thread.
 * 
 * @author Sebastian Stumpf
 * 
 */
public class ConsoleSettingsView extends BaseMultiThreadedView implements ISettingsView {
	/**
	 * Display the updated settings if {@link #update()} is called.
	 * 
	 * @author Sebastian Stumpf
	 * 
	 */
	private class DisplaySettingsThread extends Thread {
		@Override
		public void run() {

			while (!isStopped()) {
				try {
					synchronized (displaySettingsmonitor) {
						displaySettingsmonitor.wait();
						if (settings != null) {
							displaySettings();
						}
					}

				} catch (final InterruptedException e) {
					// interrupted by stop call -> thread will terminate
				}
			}
		}
	}

	/**
	 * Interact with the user. Simple Interaction menu is offered for that purpose.
	 * 
	 * @author Sebastian Stumpf
	 * 
	 */
	private class InteractionThread extends Thread {
		@Override
		public void run() {

			System.out.println();
			System.out.printf("%-32s%-32s%n", "Info", "Einstellungen werden auf neue Spiele angewendet, nicht auf laufende.");
			boolean validInput;
			boolean back = false;
			while (!isStopped()) {
				synchronized (displaySettingsmonitor) {
					System.out.println();
					System.out.println("Bitte wählen:");
					System.out.printf("%-32s%-32s%n", "1", "Einstellungen anzeigen");
					System.out.printf("%-32s%-32s%n", "2", "Einstellung ändern");
					System.out.printf("%-32s%-32s%n", "3", "Einstellungen speichern");
					System.out.printf("%-32s%-32s%n", "4", "Zurück zum Startbildschirm");
				}

				try {
					switch (Integer.parseInt(scanner.nextLine())) {
						case 1:
							synchronized (displaySettingsmonitor) {
								displaySettingsmonitor.notifyAll();
							}
							validInput = true;
							break;
						case 2:
							changeSetting();
							validInput = true;
							break;
						case 3:
							synchronized (displaySettingsmonitor) {
								if (controller.saveSettings()) {
									System.out.println("Einstellungen gespeichert.");
								} else {
									System.out.println("Fehler beim speichern.");
								}
							}
							validInput = true;
							break;
						case 4:
							validInput = true;
							back = true;
							controller.back();
							break;
						default:
							validInput = false;
					}
				} catch (final NumberFormatException e) {
					validInput = false;
				}
				if (!validInput) {
					System.out.println("Falsche Eingabe, bitte 1/2/3/4 eingeben.");
				}
				if (back) {
					try {
						synchronized (this) {
							// wait for stop call causing interrupt
							this.wait();
						}
					} catch (final InterruptedException e) {
					}
				}
			}
		}
	}

	/** The settings controller reference. */
	private final IPrimitiveSettingsController controller;
	/** The scanner to read user commands from the command line. */
	private final Scanner scanner;
	/** A monitor to display the settings an menu as one block. */
	private final Object displaySettingsmonitor;
	/** The settings data. */
	private final IPrimitiveGameSettings settings;

	/**
	 * Instantiate the view with a given controller.
	 * 
	 * @param controller
	 *            the settings controller to interact with.
	 */
	public ConsoleSettingsView(final IPrimitiveSettingsController controller) {

		super();
		this.controller = controller;
		this.settings = controller.getPrimitiveSettings();
		this.scanner = SharedViewRessources.getScanner();
		this.displaySettingsmonitor = new Object();
		// set me as the controllers view
		controller.setView(this);
	}

	@Override
	public void start() {

		registerThreads(new DisplaySettingsThread(), new InteractionThread());
		super.start();
	}

	@Override
	public void update() {

		synchronized (displaySettingsmonitor) {
			displaySettingsmonitor.notifyAll();
		}
	}

	/**
	 * Display submenu for changing a setting.
	 */
	private void changeSetting() {

		System.out.println();
		System.out.println("Bitte wählen:");
		System.out.printf("%-32s%-32s%n", "1", "Name unterer Spieler | " + settings.getNameBottom());
		System.out.printf("%-32s%-32s%n", "2", "Name linker Spieler | " + settings.getNameLeft());
		System.out.printf("%-32s%-32s%n", "3", "Name oberer Spieler | " + settings.getNameTop());
		System.out.printf("%-32s%-32s%n", "4", "Name rechter Spieler | " + settings.getNameRight());
		System.out.printf("%-32s%-32s%n", "5", "Solo Multiplikator | " + settings.getSoloMultiplier());
		System.out.printf("%-32s%-32s%n", "6", "Basis Tarif | " + settings.getBasicCharge());
		System.out.printf("%-32s%-32s%n", "7", "Startgeld | " + settings.getStartMoney());
		System.out.printf("%-32s%-32s%n", "8", "Doch nichts");
		boolean validInput = false;
		boolean back = false;
		while (!back && !validInput) {
			try {
				switch (Integer.parseInt(scanner.nextLine())) {
					case 1:
						while (!validInput) {
							validInput = setName(PlayerPosition.BOTTOM);
						}
						break;
					case 2:
						while (!validInput) {
							validInput = setName(PlayerPosition.LEFT);
						}
						break;
					case 3:
						while (!validInput) {
							validInput = setName(PlayerPosition.TOP);
						}
						break;
					case 4:
						while (!validInput) {
							validInput = setName(PlayerPosition.RIGHT);
						}
						break;
					case 5:
						while (!validInput) {
							System.out.println("Bitte Wert eingeben: ");
							int value = 0;
							try {
								value = Integer.parseInt(scanner.nextLine());
								if (controller.changeSoloMultiplier(value)) {
									validInput = true;
								} else {
									System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl zwischen " + IPrimitiveGameSettings.MULTIPLIER_MIN_VALUE + " und "
											+ IPrimitiveGameSettings.MULTIPLIER_MAX_VALUE + " eingeben.");
								}
							} catch (final NumberFormatException e) {
								System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl eingeben.");
							}
						}
						break;
					case 6:
						while (!validInput) {
							System.out.println("Bitte Wert eingeben: ");
							int value = 0;
							try {
								value = Integer.parseInt(scanner.nextLine());
								if (controller.changeBasicCharge(value)) {
									validInput = true;
								} else {
									System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl zwischen " + IPrimitiveGameSettings.CHARGE_MIN_VALUE + " und "
											+ IPrimitiveGameSettings.CHARGE_MAX_VALUE + " eingeben.");
								}
							} catch (final NumberFormatException e) {
								System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl eingeben.");
							}
						}
						break;
					case 7:
						while (!validInput) {
							System.out.println("Bitte Wert eingeben: ");
							int value = 0;
							try {
								value = Integer.parseInt(scanner.nextLine());
								if (controller.changeSoloMultiplier(value)) {
									validInput = true;
								} else {
									System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl zwischen " + IPrimitiveGameSettings.MONEY_MIN_VALUE + " und "
											+ IPrimitiveGameSettings.MONEY_MAX_VALUE + " eingeben.");
								}
							} catch (final NumberFormatException e) {
								System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte eine Zahl eingeben.");
							}
						}
						break;
					case 8:
						validInput = true;
						back = true;
						break;
					default:
						validInput = false;
				}
			} catch (final NumberFormatException e) {
				validInput = false;
			}

			if (!validInput) {
				System.out.println("Falsche Eingabe, bitte 1/2/3/4/5/6/7/8 eingeben.");
			}
		}
	}

	/**
	 * Display the formatted settings on the console.
	 */
	private void displaySettings() {

		System.out.println();
		System.out.println("Einstellungen:");
		System.out.printf("%-32s%-32s%n", "Name unterer Spieler", settings.getNameBottom());
		System.out.printf("%-32s%-32s%n", "Name linker Spieler", settings.getNameLeft());
		System.out.printf("%-32s%-32s%n", "Name oberer Spieler", settings.getNameTop());
		System.out.printf("%-32s%-32s%n", "Name rechter Spieler", settings.getNameRight());
		System.out.printf("%-32s%-32s%n", "Solo Multiplikator", settings.getSoloMultiplier());
		System.out.printf("%-32s%-32s%n", "Basis Tarif", settings.getBasicCharge());
		System.out.printf("%-32s%-32s%n", "Startgeld", settings.getStartMoney());
	}

	/**
	 * Subsubmenu to set a name.
	 * 
	 * @param position
	 *            to position to change the name for.
	 * @return true if the name was valid.
	 */
	private boolean setName(final PlayerPosition position) {

		System.out.println("Bitte Wert eingeben: ");
		final String value = scanner.nextLine();
		if (controller.changePlayerName(position, value)) {
			return true;
		} else {
			System.out.println("Dieser Wert ist hier nicht erlaubt. Bitte mindestens " + IPrimitiveGameSettings.NAME_MIN_LENGTH + " und maximal " + IPrimitiveGameSettings.NAME_MAX_LENGTH
					+ " Zeichen eingeben");
			return false;
		}
	}

}
