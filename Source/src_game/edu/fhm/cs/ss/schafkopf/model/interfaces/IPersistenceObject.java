package edu.fhm.cs.ss.schafkopf.model.interfaces;

import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;

/**
 * This interface is used to declare a class as persistence object. Persistence objects contain all and only that information, that should be persisted.
 * 
 * It is used by the {@link IPersistenceHandler} to load and store objects. A method to convert a this persistence object to its {@link IPersistableObject}
 * version must be implemented.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPersistenceObject {

	/**
	 * @return the objects filename it is persisted with.
	 */
	String getFilename();

	/**
	 * Convert this persistence object to its {@link IPersistableObject} version.
	 * 
	 * @return the persistable object.
	 */
	IPersistableObject getPersistableObject();
}