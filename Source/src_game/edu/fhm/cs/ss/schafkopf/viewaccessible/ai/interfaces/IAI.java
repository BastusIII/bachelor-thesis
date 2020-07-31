package edu.fhm.cs.ss.schafkopf.viewaccessible.ai.interfaces;

import edu.fhm.cs.ss.schafkopf.ai.interfaces.IGeneralAI;
import edu.fhm.cs.ss.schafkopf.ai.interfaces.ISpecializedAI;
import edu.fhm.cs.ss.schafkopf.viewaccessible.model.enums.GameState;

/**
 * This interface offers all methods an AI player needs to calculate to make his next best action.<br>
 * <br>
 * 
 * For testing purposes, implementing classes must must have a Constructor({@link GameState}) that defines the state the AI is accepting a restart.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IAI extends ISpecializedAI, IGeneralAI {

}
