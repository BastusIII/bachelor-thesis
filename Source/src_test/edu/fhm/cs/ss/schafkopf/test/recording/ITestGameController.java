package edu.fhm.cs.ss.schafkopf.test.recording;

import edu.fhm.cs.ss.schafkopf.controller.interfaces.IGameController;
import edu.fhm.cs.ss.schafkopf.test.model.ITestValidationInfo;

/**
 * The test game controller extends the {@link IGameController} by testing functionality. The game progress must be recorded, validated and deadlocks detected.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface ITestGameController extends IGameController {

	/**
	 * Callback method for the {@link IGameRecorder} to deliver the created test information.<br>
	 * If this method is called, the test game controller must terminate with all of its views and components.
	 * 
	 * @param testValidationInfo
	 *            the validation info.
	 */
	void testFeedBack(ITestValidationInfo testValidationInfo);

}