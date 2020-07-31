package edu.fhm.cs.ss.schafkopf.test.formattingandvalidation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationCode;
import edu.fhm.cs.ss.schafkopf.test.model.TestValidationInfo;
import edu.fhm.cs.ss.schafkopf.test.settings.TestSettings;

/**
 * This class is implementing the functionality of package validation.<br>
 * <br>
 * 
 * The files that are checked, the imports to check and the allowed imports can all be edited via:<br>
 * {@link TestSettings#RESTRICTED_FILES_FOLDER_RELATIVE_PATH} | {@link TestSettings#PREFIX_IMPORT_TO_CHECK} | {@link TestSettings#ALLOWED_IMPORT_SUFFIXES}
 * 
 */
public class PackageValidation {
	/**
	 * The test validation info to append errors to.
	 */
	private ITestValidationInfo testValidationInfo;

	/**
	 * This starts the test.
	 * 
	 * @return the test validation info about the test.
	 */
	public ITestValidationInfo run() {

		testValidationInfo = new TestValidationInfo();
		final File[] files = new File(TestSettings.RESTRICTED_FILES_FOLDER_RELATIVE_PATH).listFiles();
		checkFiles(files);
		return testValidationInfo;
	}

	/**
	 * This method is checking the imports in a given file.
	 * 
	 * @param file
	 *            the file to check.
	 */
	private void checkAccessViolations(final File file) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			String packageName = "";
			int lineCounter = 0;
			while ((line = reader.readLine()) != null) {
				lineCounter++;
				// get package name
				if (line.startsWith("package")) {
					packageName = line.split("\\s")[1].replaceAll(";", "");
					continue;
				}
				line = line.replaceAll("\\s", "");
				// check imports
				if (line.startsWith("import" + TestSettings.PREFIX_IMPORT_TO_CHECK)) {
					boolean violation = true;
					// check if import continues with allowed suffix
					for (final String suffix : TestSettings.ALLOWED_IMPORT_SUFFIXES) {
						if (line.startsWith("import" + TestSettings.PREFIX_IMPORT_TO_CHECK + "." + suffix)) {
							violation = false;
							break;
						}
					}
					if (violation) {
						testValidationInfo.appendInformation(TestValidationCode.WARNING,
								"package: " + packageName + "." + file.getName() + ", line: " + lineCounter + ",  invalid import: " + line.replaceAll("import|;", ""));
					}
				}
				// break when the imports are all checked and the class file
				// definition begins.
				else if (line.startsWith("public")) {
					break;
				}
			}
			reader.close();

		} catch (final IOException e) {
			testValidationInfo.appendInformation(TestValidationCode.ERROR_GENERAL, "Cannot read file" + file.getName());
		}
	}

	/**
	 * This method runs through all given files and calls {@link #checkAccessViolations(File)} for each file that is not a directory.
	 * 
	 * @param files
	 *            the files to iterate through.
	 */
	private void checkFiles(final File[] files) {

		for (final File file : files) {
			if (file.isDirectory()) {
				checkFiles(file.listFiles()); // Calls same method again.
			} else {
				checkAccessViolations(file);
			}
		}
	}
}
