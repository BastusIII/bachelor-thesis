package edu.fhm.cs.ss.schafkopf.model.utilities.interfaces;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistenceObject;

/**
 * This interface offers methods to load from and save to the persistence layer.<br>
 * <br>
 * 
 * The implementations can define the layer they are persisting to.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPersistenceHandler {

	/**
	 * Get the prefix of this handler. The filename of loading and saving methods is appended to this prefix.
	 * 
	 * @return prefix the prefix.
	 */
	String getPrefix();

	/**
	 * Load a Object with the given filename.
	 * 
	 * @param filename
	 *            the filename.
	 * @return the object if it was found or null.
	 */
	Object load(String filename);

	/**
	 * Persist a persistence object under its defined filename.
	 * 
	 * @param persistenceObject
	 *            the persistence object.
	 * @return true if the object was successfully persisted.
	 */
	boolean persist(IPersistenceObject persistenceObject);

	/**
	 * Persist an object with the given filename.
	 * 
	 * @param object
	 *            the object.
	 * @param filename
	 *            the filename
	 * @return true if the object was successfully persisted.
	 */
	boolean persist(Object object, String filename);

	/**
	 * Set the prefix of this handler. The filename of loading and saving methods is appended to this prefix.
	 * 
	 * @param prefix
	 *            the prefix.
	 */
	void setPrefix(String prefix);
}
