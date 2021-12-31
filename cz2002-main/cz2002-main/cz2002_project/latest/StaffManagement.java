import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * StaffManagement class that implements the interface from OPInterface
 */
public class StaffManagement implements OPInterface {
	private Scanner sc = new Scanner(System.in);
	private ArrayList<Staff> StaffList = new ArrayList<>();

	/**
	 * Constructor for StaffManagement
	 */
	public StaffManagement(){
		if(!read()){
			System.out.println("Corrupted file.");
		}
	}

	/**
	 * @return true if read from Staff.txt successfully, false otherwise
	 */
	public boolean read(){
		Path path = Paths.get("Staff.txt");

		try (BufferedReader br = Files.newBufferedReader(path)) {
			String line = br.readLine();

			while (line != null) {
				String[] tokens = line.split(",");
				Staff staff = new Staff(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3]);
				StaffList.add(staff);

				line = br.readLine();
			}
		}catch (IOException e){
			return false;
		}
		return true;
	}

	/**
	 * @return true after adding a new staff into data file
	 * @throws IOException
	 */
	public boolean add() throws IOException {
		int index = StaffList.size() + 1;
		System.out.println("Name of staff: ");
		String name = sc.next();
		System.out.println("Job title of staff: ");
		String jobTitle = sc.next();
		System.out.println("Gender of staff: ");
		String gender = sc.next();

		Staff staff = new Staff(index, name, jobTitle, gender);
		StaffList.add(staff);

		write();

		return true;
	}


	/**
	 * @param id id of staff to be deleted
	 * @return true after removing staff from data file
	 * @throws IOException
	 */
	public boolean remove(int id) throws IOException {
		StaffList.remove(id);
		write();

		return true;
	}

	/**
	 * @param id id of staff's details to be changed
	 * @return true if staff's details changed successfully, false otherwise
	 * @throws IOException
	 */
	public boolean edit(int id) throws IOException {
		boolean changed = false;
		Staff staff = StaffList.get(id);

		System.out.println("Which aspect of the above staff would you like to edit?");
		System.out.println("\t(1) - Name");
		System.out.println("\t(2) - Job Title");
		System.out.println("\t(3) - Nothing (return)");

		int input = sc.nextInt();
		switch(input) {
			case 1:
				System.out.println("New name: ");
				String name = sc.next();
				staff.setName(name);
				StaffList.set(id, staff);
				changed = true;
				break;

			case 2:
				System.out.println("New job title: ");
				String jobTitle = sc.next();
				staff.setJobTitle(jobTitle);
				StaffList.set(id, staff);
				changed = true;
				break;

			case 3:
				System.out.println("Nothing changed.");
				break;

			default:
				System.out.println("Invalid input!");
				break;
		}
		write();
		return changed;
	}

	/**
	 * display all staffs and each of their details
	 */
	public void display() {
		for(Staff staff: StaffList) {
			System.out.println("Staff ID: " + staff.getStaffID());
			System.out.println("Name: " + staff.getName());
			System.out.println("Job title: " + staff.getJobTitle());
			System.out.println("Gender: " + staff.getGender());
			System.out.println();
		}
	}

	public void sort() {}

	/**
	 * @param id staff id to be selected
	 * @return details of the staff chosen
	 */
	public Staff get(int id) {
		return StaffList.get(id);
	}

	/**
	 * @return total number of staff
	 */
	public int getStaffSize() {
		return StaffList.size();
	}

	/**
	 * Displays the menu for user to select the next course of action
	 */
	public void displayInterface() {
		int user_choice = 0;
		int staff_id;
		while (true) {
			System.out.println("\t(1) - ADD a new staff");
			System.out.println("\t(2) - EDIT a current staff");
			System.out.println("\t(3) - DELETE a current staff");
			System.out.println("\t(4) - VIEW all the staff");
			System.out.println("\t(5) - Return");
			user_choice = sc.nextInt();

			switch (user_choice)
			{
				case 1:
					// ADD a new staff
					boolean added =false;
					try{
						added = add();
					}catch(IOException e){
						System.out.println("File corrupted.");
					}

					if(!added){
						System.out.println("The new staff was not added. Please refer to error message above.");
					}
					else{
						System.out.println("A new staff added to the system successfully.");
					}
					break;

				case 2:
					// Edit a current staff
					System.out.println("Please enter the ID of the staff you would like to modify:");
					staff_id = sc.nextInt();
					if(staff_id < 0 || staff_id > StaffList.size()){
						System.out.println("The ID you've entered is invalid.");
						break;
					}
					boolean edited =false;
					try{
						edited = edit(staff_id-1);
					}catch(IOException e){
						System.out.println("File corrupted.");
					}

					if(edited){
						System.out.println("The operation was successful.");
					}else{
						System.out.println("The operation was unsuccessful, please refer to the error above.");
					}
					break;

				case 3:
					// Delete a current staff
					System.out.println("Please enter the ID of the staff you would like to delete:");
					staff_id = sc.nextInt();
					if(staff_id < 0 || staff_id > StaffList.size()){
						System.out.println("The ID you've entered is invalid.");
						break;
					}
					boolean removed = false;
					try{
						removed = remove(staff_id-1);
					}catch(IOException e){
						System.out.println("File corrupted.");
					}

					if(removed){
						System.out.println("A staff was removed from the system successfully.");
					}else{
						System.out.println("The staff was not removed. Please refer to error message above.");
					}
					break;

				case 4:
					// VIEW all staff
					display();
					break;
				
				case 5:
					return;

				default:
					System.out.println("Invalid input, please select one of the options above.");
					break;
			}
		}
	}

	/**
	 * writes to Staff data file
	 * @throws IOException
	 */
	public void write() throws IOException {
		Path path = Paths.get("Staff.txt");
		FileWriter fw = new FileWriter(String.valueOf(path));

		for(Staff staff: StaffList) {
			try {
				fw.write(staff.getStaffID() + ",");
				fw.write(staff.getName() + ",");
				fw.write(staff.getJobTitle() + ",");
				fw.write(staff.getGender() + ",");
				fw.write(System.lineSeparator());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
