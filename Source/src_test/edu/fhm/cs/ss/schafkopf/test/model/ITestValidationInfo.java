package edu.fhm.cs.ss.schafkopf.test.model;

/**
 * Test validation info offers methods to append test information and get the formatted information. Test data can be connected optionally.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ITestValidationInfo {
	/**
	 * Formats and appends information to the info output. This method also updates the total validation code, if the given code has a higher priority.
	 * 
	 * @param testValidationCode
	 *            the validation code.
	 * @param furtherInformation
	 *            the information to append besides the validation code.
	 */
	void appendInformation(TestValidationCode testValidationCode, String furtherInformation);

	/**
	 * @return the validation infos output string.
	 */
	String getFurtherInformation();

	/**
	 * @return the test case.
	 */
	ITestGameData getTestCase();

	/**
	 * @return the total execution code.
	 */
	TestValidationCode getTotalValidationCode();

	/**
	 * @param testCase
	 *            the test case to set
	 */
	void setTestCase(ITestGameData testCase);

}