package edu.fhm.cs.ss.schafkopf.test.application;

import java.util.Scanner;

import edu.fhm.cs.ss.schafkopf.test.controller.ITestController;
import edu.fhm.cs.ss.schafkopf.test.controller.TestController;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;
import edu.fhm.cs.ss.schafkopf.view.utilities.SharedViewRessources;

/**
 * The entry point for the test environmentt.<br>
 * <br>
 *
 * The user is able to start all tests from here. Errors will be logged to the console. A menu is offered to interact with the {@link TestController}.
 *
 * @author Sebastian Stumpf
 */
public class TestApplication {

	/**
	 * A string to format the output.
	 */
	public static final String FORMAT_STRING = "%-32s%-32s%n";

	/**
	 * The main method for the test environment.
	 *
	 * @param args
	 *            unused.
	 */
	public static void main(final String... args) {

		System.out.println("Schafkopf Testumfeld");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "", "");
		System.out.format(FORMAT_STRING, "Option", "Information");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Package Access Validation", "Dieser Test stellt sicher, dass keine Zugriffsverletzungen von den View - Packages");
		System.out.format(FORMAT_STRING, "-------------------------------", "aus erfolgen.");
		System.out.format(FORMAT_STRING, "", "Einstellungen in welchem Package die Views liegen und auf welche Packages nicht zugegriffen werden darf,");
		System.out.format(FORMAT_STRING, "", "k�nnen in den TestSettings angepasst werden.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Stress Test", "Dieser Test stellt sicher, dass die aktuelle Implementierung von Logik und Model stabil l�uft");
		System.out.format(FORMAT_STRING, "-------------------------------", "und keine Ver�nderungen vorgenommen wurden, die unvorhergesehene Modifikationen zur Folge haben.");
		System.out.format(FORMAT_STRING, "", "Dazu wird eine gr��ere Anzahl an Spielen erstellt, und �berpr�ft, ob das Spiel in einem Deadlock");
		System.out.format(FORMAT_STRING, "", "endet und Spieldaten nur auf vorhergesehene Weise manipuliert werden.");
		System.out.format(FORMAT_STRING, "", "Dieser Stresstest wird immer mit Random AI Spielrn durchgef�hrt, diese sind auf der aktuellen Code");
		System.out.format(FORMAT_STRING, "", "Basis getestet und laufen stabil, so dass Fehler in den KI Spielern ausgeschlossen werden k�nnen.");
		System.out.format(FORMAT_STRING, "", "Es werden Spiele mit variablen verbotenen Spielen (z.B. KI spielt nur Sauspiel/Pass) und variablem");
		System.out.format(FORMAT_STRING, "", "Neustart Akzeptanz Status gespielt.");
		System.out.format(FORMAT_STRING, "", "Die Einstellungen (Timeout/Anzahl der Spiele) k�nnen ggf. in den TestSettings angepasst werden.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Custom AI Stress Test", "Hier werden vorgegebene AI Player Implementierungen auf ihre Stabilit�t getestet.");
		System.out.format(FORMAT_STRING, "-------------------------------", "Wenn neue KI's/Views implementiert werden, kann mit diesem Test �berpr�ft werden ob diese stabil laufen.");
		System.out.format(FORMAT_STRING, "", "Der Ablauf ist der selbe wie im Stress Test, jedoch k�nnen die verwendeten AI Player bestimmt werden.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Neue Test Cases erstellen", "Erstellt eine Anzahl von Testf�llen mit vorgegebenen AI Playern und speichert diese ab.");
		System.out.format(FORMAT_STRING, "(Random Stacks)", "Jedes Spiel verwendet einen zuf�lligen Stack.");
		System.out.format(FORMAT_STRING, "-------------------------------", "Diese Option ist dazu gedacht, neue Test Cases zu erstellen, auf die zu einem beliebigen sp�teren");
		System.out.format(FORMAT_STRING, "", "Zeitpunkt der Determination Test angewendet werden kann. Nur falls alle erstellten Testf�lle");
		System.out.format(FORMAT_STRING, "", "ohne Fehler durchlaufen, wird abgespeichert");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Neue Test Cases erstellen", "Erstellt Testf�lle mit vorgegebenen AI Playern und speichert diese ab.");
		System.out.format(FORMAT_STRING, "(Custom Stacks)", "Es wird ein Spiel f�r jeden im Stack Feed gefundenen Stack erstellt.");
		System.out.format(FORMAT_STRING, "-------------------------------", "Diese Option ist dazu gedacht, neue Test Cases zu erstellen, auf die zu einem beliebigen sp�teren");
		System.out.format(FORMAT_STRING, "", "Zeitpunkt der Determination Test angewendet werden kann. Nur falls alle erstellten Testf�lle");
		System.out.format(FORMAT_STRING, "", "ohne Fehler durchlaufen, wird abgespeichert");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Load Tests", "Es k�nnen abgespeicherte Tests aus einem vorgegebenen Ordner geladen werden,");
		System.out.format(FORMAT_STRING, "-------------------------------", "um auf diese den Determination Test anzuwenden.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Determination Test", "Hier wird f�r die aktuell geladenen Testf�lle �berpr�ft, ob diese bei erneutem Spieldurchlauf ");
		System.out.format(FORMAT_STRING, "-------------------------------", "deterministisch handeln.");
		System.out.format(FORMAT_STRING, "", "Falls Abweichungen gefunden werden, werden diese ausgegeben.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Interactive Game (Random Stack)", "Es wird ein interaktives Spiel erstellt, in dem der Spieler selbst gegen seine KI spielen kann.");
		System.out.format(FORMAT_STRING, "-------------------------------", "Es wird ein zuf�lliger Stack verwendet.");
		System.out.format(FORMAT_STRING, "", "Gefundene Fehler im Spielablauf werden im Anschluss ausgegegeben.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Interactive Game (Custom Stack)", "Es wird ein interaktives Spiel erstellt, in dem der Spieler selbst gegen seine KI spielen kann.");
		System.out.format(FORMAT_STRING, "-------------------------------", "Es wird der Stack aus dem Stack Feed mit dem vorgegebenen Index gespielt.");
		System.out.format(FORMAT_STRING, "", "Gefundene Fehler im Spielablauf werden im Anschluss ausgegegeben.");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Print Test Case", "Die Spielst�nde eines vorgegebenen Test Cases werden nacheinander ausgegeben.");
		System.out.format(FORMAT_STRING, "-------------------------------", "");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.format("%-32s%n", "Geladene Einstellungen");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.format(FORMAT_STRING, "Stack Feed", TestSettings.STACK_FEED_FOLDER_RELATIVE_PATH + TestSettings.FILE_SEPARATOR + TestSettings.STACK_FEED_TO_LOAD);
		System.out.format(FORMAT_STRING, "Test Case Folder", TestSettings.TEST_FOLDER_RELATIVE_PATH);
		System.out.format(FORMAT_STRING, "AI Accept Restart State", TestSettings.ACCEPT_RESTART_STATE);
		System.out.format(FORMAT_STRING, "Interactive Player", TestSettings.INTERACTIVE_VIEW);
		System.out.format(FORMAT_STRING, "Autonomous Player", TestSettings.AUTONOMOUS_VIEW);
		System.out.format(FORMAT_STRING, "Custom AI", TestSettings.AI);
		System.out.format(FORMAT_STRING, "Stress Test Timeout", TestSettings.TIMEOUT);
		System.out.format(FORMAT_STRING, "Stress Test Games", TestSettings.STRESSTEST_GAMES);
		System.out.format(FORMAT_STRING, "Custom Stress Test Games", TestSettings.CUSTOM_STRESSTEST_GAMES);
		System.out.format("%-32s", "View access packages");
		int counter = 0;
		for (final String suffix : TestSettings.ALLOWED_IMPORT_SUFFIXES) {
			System.out.format(suffix);
			if (counter != TestSettings.ALLOWED_IMPORT_SUFFIXES.length - 1) {
				System.out.print(", ");
			}
			counter++;
		}
		System.out.println();
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		final ITestController testController = new TestController();
		final Scanner scanner = SharedViewRessources.getScanner();

		boolean success = false;
		int number = 1;
		String folder = null;
		String prefix = null;
		String name = null;
		int choice = 0;
		while (choice != -1) {
			System.out.println();
			System.out.println();
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.format(FORMAT_STRING, "1", "Package Access Validation");
			System.out.format(FORMAT_STRING, "2", "Stress Test");
			System.out.format(FORMAT_STRING, "3", "Custom Stress Test");
			System.out.format(FORMAT_STRING, "4", "Neue Test Cases erstellen (Random Stacks)");
			System.out.format(FORMAT_STRING, "5", "Neue Test Cases erstellen (Custom Stacks)");
			System.out.format(FORMAT_STRING, "6", "Tests Cases laden");
			System.out.format(FORMAT_STRING, "7", "Determination Test mit geladenen Test Cases");
			System.out.format(FORMAT_STRING, "8", "Interaktives Spiel (Random Stack)");
			System.out.format(FORMAT_STRING, "9", "Interaktives Spiel (Custom Stack)");
			System.out.format(FORMAT_STRING, "10", "Test Case ausgeben");
			System.out.format(FORMAT_STRING, "0", "Ende");
			System.out.format("%-32s", "Eingabe:");
			try {
				choice = Integer.parseInt(scanner.nextLine());
				switch (choice) {
					case 1:
						testController.packageAccessValidation();
						break;
					case 2:
						testController.stressTestRandomAi();
						break;
					case 3:
						testController.customStressTest();
						break;
					case 4:
						success = false;
						while (!success) {
							try {
								System.out.format("%-32s%n", "Anzahl der Test Cases ( ~ 100, werden alle abgespeichert, > Werte -> > Dauer und > Speicherplatz)");
								System.out.format("%-32s", "Eingabe:");
								number = Integer.parseInt(scanner.nextLine());
								success = true;
							} catch (final NumberFormatException e) {
								System.out.println("Falsche Eingabe.");
							}
						}
						System.out.format("%-32s%n", "Ordner");
						System.out.format("%-32s", "Eingabe:");
						folder = scanner.nextLine();
						System.out.format("%-32s%n", "Test Case Prefix");
						System.out.format("%-32s", "Eingabe:");
						prefix = scanner.nextLine();
						testController.generateTestCasesWithRandomStacks(folder, number, prefix);
						break;
					case 5:
						System.out.format("%-32s%n", "Ordner");
						System.out.format("%-32s", "Eingabe:");
						folder = scanner.nextLine();
						System.out.format("%-32s%n", "Test Case Prefix");
						System.out.format("%-32s", "Eingabe:");
						prefix = scanner.nextLine();
						testController.generateTestCasesWithCustomStacks(folder, prefix);
						break;
					case 6:
						System.out.format("%-32s%n", "Ordner");
						System.out.format("%-32s", "Eingabe:");
						folder = scanner.nextLine();
						testController.loadAllTestCases(folder);
						break;
					case 7:
						testController.aiDeterminationTest();
						break;
					case 8:
						testController.playAndValidateInteractiveGameVsAiWithRandomStack();
						break;
					case 9:
						success = false;
						while (!success) {
							try {
								System.out.format("%-32s%n", "Index im Stack Feed");
								System.out.format("%-32s", "Eingabe:");
								number = Integer.parseInt(scanner.nextLine());
								success = true;
							} catch (final NumberFormatException e) {
								System.out.println("Falsche Eingabe.");
							}
						}
						testController.playAndValidateInteractiveGameVsAiWithCustomStack(number);
						break;
					case 10:
						System.out.format("%-32s%n", "Ordner");
						System.out.format("%-32s", "Eingabe:");
						folder = scanner.nextLine();
						System.out.format("%-32s%n", "Test Case Name");
						System.out.format("%-32s", "Eingabe:");
						name = scanner.nextLine();
						testController.printTestCase(folder, name);
						break;
					case 0:
						choice = -1;
						break;
					default:
						System.out.println("Falsche Eingabe.");
						break;
				}

			} catch (final NumberFormatException e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		scanner.close();
	}
}
