import java.io.IOException;

/**
 * Interface for manager classes
 */
public interface OPInterface {

	/**
	 * Add new object
	 * @throws IOException
	 */
	boolean add() throws IOException;

	/**
	 * Remove object
	 * @param id ID of object
	 * @throws IOException
	 */
	boolean remove(int id) throws IOException;

	/**
	 * Edit existing object
	 * @param id ID of object
	 * @throws IOException
	 */
	boolean edit(int id) throws IOException;

	/**
	 * Display all objects
	 */
	void display();

	/**
	 * Write to data files
	 * @throws IOException
	 */
	void write() throws IOException;
}
