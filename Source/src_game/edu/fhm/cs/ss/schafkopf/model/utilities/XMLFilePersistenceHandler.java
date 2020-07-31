package edu.fhm.cs.ss.schafkopf.model.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

import edu.fhm.cs.ss.schafkopf.model.interfaces.IPersistenceObject;
import edu.fhm.cs.ss.schafkopf.model.utilities.interfaces.IPersistenceHandler;

/**
 * This persistence handler loads and stores data as and from XML files.
 *
 * The library XStream is used to serialize the data as an XML and to store and load it from the file system. The persistence handler is created with relative
 * path, used as a base path all the files are saved in and loaded from..
 *
 * @author Sebastian Stumpf
 *
 */
public class XMLFilePersistenceHandler implements IPersistenceHandler {

	/**
	 * The file separator for this system.
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * The relative path, the data is saved to and loaded from.
	 */
	private final String destinationFolder;
	/**
	 * the persistence handler's prefix.
	 */
	private String prefix;

	/**
	 * Instantiate the persistence handler with the given parameters.
	 *
	 * @param prefix
	 *            the prefix the filenames are appended to. If the prefix is null, it will be ignored.
	 * @param destinationFolderHierachrchy
	 *            the hierarchy of the destination folder path.
	 */
	public XMLFilePersistenceHandler(final String prefix, final String... destinationFolderHierachrchy) {

		final StringBuilder builder = new StringBuilder();

		if (destinationFolderHierachrchy != null) {
			for (final String element : destinationFolderHierachrchy) {
				builder.append(element);
				builder.append(FILE_SEPARATOR);
			}
		}

		this.destinationFolder = builder.toString();
		this.prefix = prefix == null ? "" : prefix;
	}

	@Override
	public String getPrefix() {

		return prefix;
	}

	@Override
	public Object load(final String filename) {

		if (filename == null) {
			return null;
		}
		final XStream xstream = new XStream();
		Object object = null;
		try {
			final Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(destinationFolder + prefix + filename)));
			object = xstream.fromXML(reader);
			reader.close();
		} catch (final IOException ex) {
			return null;
		}
		return object;
	}

	@Override
	public boolean persist(final IPersistenceObject persistenceObject) {

		return persist(persistenceObject, persistenceObject.getFilename());

	}

	@Override
	public boolean persist(final Object object, final String filename) {

		if (filename == null) {
			return false;
		}
		final XStream xstream = new XStream();
		final String xml = xstream.toXML(object);
		try {
			final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFolder + prefix + filename)));
			writer.write(xml);
			writer.close();
		} catch (final IOException ex) {
			return false;
		}
		return true;
	}

	@Override
	public void setPrefix(final String prefix) {

		this.prefix = prefix;

	}
}
