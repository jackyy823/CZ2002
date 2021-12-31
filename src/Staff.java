/**
 * Staff class with attributes such as StaffID, name, job title, and gender
 */
public class Staff {
	private int staffID;
	private String name;
	private String jobTitle;
	private String gender;

	/**
	 * @param staffID ID of staff
	 * @param name Staff's name
	 * @param jobTitle Staff's job title
	 * @param gender Gender of staff
	 */
	public Staff (int staffID, String name, String jobTitle, String gender) {
		this.staffID = staffID;
		this.name = name;
		this.jobTitle = jobTitle;
		this.gender = gender;
	}

	/**
	 * @return ID of staff
	 */
	public int getStaffID() {
		return this.staffID;
	}

	/**
	 * @param staffID set staff ID
	 */
	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	/**
	 * @return Name of staff
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name set name for staff
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Job title of staff
	 */
	public String getJobTitle() {
		return this.jobTitle;
	}

	/**
	 * @param jobTitle set job title of staff
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return Gender of staff
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * @param gender Set gender of staff
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

}