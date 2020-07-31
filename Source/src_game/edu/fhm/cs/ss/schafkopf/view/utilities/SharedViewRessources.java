package edu.fhm.cs.ss.schafkopf.view.utilities;

import java.util.Scanner;

/**
 * Shared ressources, that are used by various views should be stored here.
 *
 * @author Sebastian Stumpf
 *
 */
public final class SharedViewRessources {
	/**
	 * The scanner on {@link System#in} all views are sharing.
	 */
	private static final Scanner SCANNER = new Scanner(System.in);

	/**
	 * @return the shared scanner on {@link System#in}.
	 */
	public static Scanner getScanner() {

		return SCANNER;
	}

}
