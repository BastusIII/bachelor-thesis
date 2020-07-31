package edu.fhm.cs.ss.schafkopf.model.interfaces;

/**
 * This interface is used to declare a class as persistable object. It has to offer a {@link IPersistenceObject}, with all its data that has to be persisted.
 * 
 * @author Sebastian Stumpf
 * 
 */
public interface IPersistableObject {

	/**
	 * Convert this object into its {@link IPersistenceObject} version.
	 * 
	 * @return the persistence object with all necessary content for the persistence.
	 */
	IPersistenceObject getPersistenceObject();
}
