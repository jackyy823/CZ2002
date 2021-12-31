import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Reservation class
 */
public class Reservation {

	private int reservationID;
	private int nrPax;
	private int tableID;
	private String name;
	private String contact;
	private Date date;
	private int startTime;

	/**
	 * @param reservationID ID for reservation
	 * @param name name of customer who is making the reservation
	 * @param contact contact number of customer
	 * @param nrPax number of pax for the reservation
	 * @param tableID table ID assigned to the reservation
	 * @param date date of reservation
	 * @param startTime time of reservation
	 */
	public Reservation(int reservationID,String name,String contact,int nrPax,int tableID,Date date,int startTime){
		this.reservationID = reservationID;
		this.nrPax = nrPax;
		this.tableID = tableID;
		this.name = name;
		this.contact = contact;
		this.date = date;
		this.startTime = startTime;
	}

	/**
	 * @return Reservation ID
	 */
	public int getReservationID() {
		return this.reservationID;
	}

	/**
	 * @param reservationID Set reservation ID
	 */
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	/**
	 * @return Number of pax for the reservation
	 */
	public int getNrPax() {
		return this.nrPax;
	}

	/**
	 * @param nrPax Set number of pax for the reservation
	 */
	public void setNrPax(int nrPax) {
		this.nrPax = nrPax;
	}

	/**
	 * @return The table ID that was assigned
	 */
	public int getTableID() {
		return this.tableID;
	}

	/**
	 * @param tableID Assign a table ID for the reservation
	 */
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	/**
	 * @return Name of customer who made the reservation
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name Set name of customer who is making reservation
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Contact number of customer
	 */
	public String getContact() {
		return this.contact;
	}

	/**
	 * @param contact Set contact number of customer
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return Time of the reservation
	 */
	public int getStartTime() {
		return this.startTime;
	}

	/**
	 * @param startTime Set time of the reservation
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return Date of reservation
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date Set date of reservation
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Print all reservations that were made
	 */
	public void printReservation(){
		Date date = getDate();
		DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = df1.format(date);

		System.out.println("reservationID:" + reservationID);
		System.out.println("nrPax: " + nrPax);
		System.out.println("tableID: " + tableID);
		System.out.println("name: " + name);
		System.out.println("contact: " + contact);
		System.out.println("date: " + strDate);
		System.out.println("startTime: " + getStartTime());
		System.out.println("----------------------------------------");
		System.out.println();
	}
}