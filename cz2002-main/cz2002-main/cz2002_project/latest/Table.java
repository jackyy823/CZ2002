import java.util.ArrayList;

/**
 * Table class with attributes such as tableID, capacity, table's occupied status, availability status, and the slots when the table is booked.
 */
public class Table {
	private int tableID;
	private int capacity;
	private boolean occupied;
	private boolean availability;
	private ArrayList<String> slots;

	/**
	 * @param tableID ID of the table
	 * @param capacity Capacity of the table
	 * @param occupied Whether the table is currently occupied at the moment
	 * @param availability Whether the table is available for reservation
	 * @param slots The timings of when the table is reserved
	 */
	public Table(int tableID, int capacity, boolean occupied, boolean availability, ArrayList<String>slots){
		this.tableID = tableID;
		this.capacity = capacity;
		this.occupied = occupied;
		this.availability = availability;
		this.slots = slots;
	}

	/**
	 * @return Table ID
	 */
	public int getTableID() {
		return this.tableID;
	}

	/**
	 * @param tableID set Table ID
	 */
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	/**
	 * @return Capacity of table
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * @param capacity Set capacity of table
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return Occupied status of table
	 */
	public boolean getOccupied() {
		return this.occupied;
	}

	/**
	 * @param occupied Set occupied status of table
	 */
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	/**
	 * @return Availability status of table
	 */
	public boolean getAvailability() {
		return this.availability;
	}

	/**
	 * @param availability Set availability status of table
	 */
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	/**
	 * @return ArrayList of dates when the table is reserved
	 */
	public ArrayList<String> getSlots() {
		return this.slots;
	}

	/**
	 * @param slots Set arrayList of dates when the table is reserved
	 */
	public void setTimeslot(ArrayList<String> slots) {
		this.slots = slots;
	}
}