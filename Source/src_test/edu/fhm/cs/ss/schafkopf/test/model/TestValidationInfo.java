package edu.fhm.cs.ss.schafkopf.test.model;

/**
 * This class holds the execution info and formats appended information.
 *
 * @author Sebastian Stumpf
 *
 */
public class TestValidationInfo implements ITestValidationInfo {

	/**
	 * The total validation code.
	 */
	private TestValidationCode totalValidationCode;
	/**
	 * The further information about the execution.
	 */
	private final StringBuilder furtherInformation;
	/**
	 * The optional test game data.
	 */
	private ITestGameData testGameData;

	/**
	 * Instantiate a test validation info with default values. The total validation code is set to the lowest priority code, {@link TestValidationCode#SUCCESS}.
	 */
	public TestValidationInfo() {

		super();
		this.totalValidationCode = TestValidationCode.SUCCESS;
		this.furtherInformation = new StringBuilder();
		this.testGameData = null;
	}

	@Override
	public void appendInformation(final TestValidationCode testValidationCode, final String furtherInformation) {

		synchronized (this.furtherInformation) {
			if (testValidationCode.priority > this.totalValidationCode.priority) {
				this.totalValidationCode = testValidationCode;
			}
			this.furtherInformation.append(testValidationCode);
			this.furtherInformation.append(": ");
			this.furtherInformation.append(furtherInformation);
			this.furtherInformation.append("\n");
		}
	}

	@Override
	public String getFurtherInformation() {

		synchronized (furtherInformation) {
			return furtherInformation.toString();
		}
	}

	@Override
	public ITestGameData getTestCase() {

		return testGameData;
	}

	@Override
	public TestValidationCode getTotalValidationCode() {

		return totalValidationCode;
	}

	@Override
	public void setTestCase(final ITestGameData testCase) {

		this.testGameData = testCase;
	}
}
