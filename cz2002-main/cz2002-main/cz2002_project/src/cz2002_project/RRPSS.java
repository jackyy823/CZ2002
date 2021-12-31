package cz2002_project;
import cz2002_project.CurrentMenu;
import cz2002_project.Bookings_management;
import cz2002_project.CurrentOrders;
import cz2002_project.Staff;

import java.io.IOException;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RRPSS {
	static CurrentMenu current_menu = new CurrentMenu();
	static CurrentOrders current_orders = new CurrentOrders();
	static Bookings_management bookings_management = new Bookings_management();
	static StaffManagement staff_management = new StaffManagement();
	
	static Scanner sc = new Scanner(System.in);  
	static int user_choice;
	static int user_choice_item;
	static int user_choice_nr_promo_items;
	static int user_input_number_pax;
	static int user_input_staff_id ;
	static int user_input_reservation_id;
	
	static float user_input_price;
	
	static char user_binary_choice;
	
	static String user_input_name;
	static String user_input_promo_name;
	static String user_input_description;
	static String user_input_contact_nr;
	
	static boolean user_input_boolean;
	static boolean user_return;
	static int[] item_ids;
	
	
	static int current_timestamp=1634342400; // 8am 16.10.21
	
	
	public static void main(String[] args) throws IOException 
	{
		
		// initial promot to get the staff id 
		while (true)
		{
			System.out.printf("%n Welcome to RRPSS. Please select your Staff-ID to continue:%n");
			RRPSS.staff_management.list_all_employees();
			
			RRPSS.user_input_staff_id = RRPSS.sc.nextInt();
			if (RRPSS.staff_management.check_if_staff_id_exists(RRPSS.user_input_staff_id))
			{
				System.out.printf("%n Welcome %s %n%n", RRPSS.staff_management.return_staff_name_by_id(RRPSS.user_input_staff_id));
				break;
			}
			else System.out.printf("%n The entered Staff-ID is invalid, please choose one from the list below!%n");
		}
		
		// allow the user to interact
		while (true)
		{
			System.out.printf("%nWelcome to the RRPSS main interface, it is currently %s; please select an option:%n", RRPSS._ts_to_date_string(RRPSS.current_timestamp));
			System.out.println("\t(1) - Access the Admin interface (create/edit/remove items and promos & print the Sales report)");
			System.out.println("\t(2) - Access the Reservation interface (make/delete reservations)");
			System.out.println("\t(3) - Access the Order interface (make a new order)");
			System.out.println("\t(4) - Step through time (+10min)");
			System.out.println("\t(5) - Exit");
			user_choice = sc.nextInt();
			
			switch (user_choice)
			{
				case 1:
					// Access the Admin interface (create/edit/remove items and promos & print the Sales report)
					admin_interface();
					break;
										
				case 2:
					// Access the Reservation interface (make/delete reservations)
					reservation_interface();
					break;
					
				case 3:
					// Access the Order interface (make a new order)
					order_interface();
					break;
					
				case 4:
					// step through time add 10min to the current timestamp
					RRPSS.current_timestamp += (10*60);
					// additionally, check if any of the current reservations has expired 
					RRPSS.bookings_management.remove_all_timely_reservations(current_timestamp-(25*60), current_timestamp-(20*60));
					break;
					
				case 5:
					return;
					
				default:
					System.out.println("Invalid input, please enter a valid option.");
					break;
			}
		}
	}
	
	private static String _ts_to_date_string(int current_ts)
	{
		String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current_ts * 1000L));
		return dateAsText;
	}
	
	private static int _convert_times_to_ts(String local_year_input, String local_month_input, String local_day_input, String local_hour_input)
	{
		//Date myDate = new Date(local_year_input, local_month_input, local_day_input, local_hour_input, "00", "00");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");//("yyyy-MM-dd hh:mm:ss.SSS");
	    Date parsedDate;
		try {
			parsedDate = dateFormat.parse(local_year_input+"-"+local_month_input+"-"+local_day_input+" "+local_hour_input);
			return (int) Math.floorDiv(parsedDate.getTime(), 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		//return (int) Math.floorDiv(parsedDate.getTime(), 1000);
	}
	
	public static void order_interface() throws IOException {
		// Add/Remove order item/s to/from order
		// View order
		// Create order
		while (true)
		{
			System.out.println("Welcome to the order interface, please select an option:");
			System.out.println("\t(1) - CREATE a new order");
			System.out.println("\t(2) - VIEW a current order");
			System.out.println("\t(3) - ADD items to an order");
			System.out.println("\t(4) - REMOVE items from an order");
			System.out.println("\t(5) - Create the bill");
			System.out.println("\t(6) - Exit");
			RRPSS.user_choice = RRPSS.sc.nextInt();
			
			switch (RRPSS.user_choice)
			{
				case 1:
					// CREATE a new order
					RRPSS.create_a_new_order();
					break;
					
				case 2:
					// VIEW a current order
					RRPSS.view_current_orders();
					break;
					
				case 3:
					// ADD items to an order
					RRPSS.add_items_to_an_order();
					break;
					
				case 4:
					// REMOVE items from an order
					RRPSS.remove_items_from_an_order();
					break;
					
				case 5:
					// Generate the bill
					RRPSS.create_the_bill();
					break;
					
				case 6:
					// exit the interface
					return;
					
				default:
					// invalid input
					System.out.println("Invalid input, please select one of the options above.");
					break;
			}
		}	
	}
	
	private static void create_the_bill()
	{
		System.out.println("Please enter the Table nr for whom you would like to create a bill:");
		int table_nr = RRPSS.sc.nextInt();
		if (RRPSS.bookings_management.check_if_table_is_currently_being_served(table_nr, RRPSS.current_timestamp)==false)
		{
			// invalid input
			System.out.println("The entered table_nr is not currently being served. Please try again.");
			return;
		}
		int reservation_id = RRPSS.bookings_management.get_reservation_id_by_table_nr(table_nr, RRPSS.current_timestamp);
		
		
		// TODO use reservation_id to look up all relevant information and print it
		
		// TODO update the past sales data
		
		// close the current reservation
		RRPSS.bookings_management.remove_current_reservation_by_id(reservation_id);
		
		// remove all items from order 
		
	}
	
	private static void create_a_new_order() throws IOException {
		// create a new order
		//System.out.println("Please enter your staff_id:");
		//RRPSS.user_input_staff_id = RRPSS.sc.nextInt();
		
		System.out.println("Please enter the reservation_id:");
		RRPSS.user_input_reservation_id = RRPSS.sc.nextInt();
		
		System.out.println("Please enter the number of items to be orderd (promos count as one item):");
		RRPSS.user_choice_item = RRPSS.sc.nextInt();
		
		RRPSS.item_ids = new int[RRPSS.user_choice_item];
		
		// print the menu
		RRPSS.current_menu.list_all_items_and_promos();
		System.out.println("Please enter the IDs of the items/promos you would like to order:");
		
		// prompt user to enter item ids
		for (int i=0; i<RRPSS.item_ids.length; i++) RRPSS.item_ids[i]=-1;
		
		
		for (int i=0; i<RRPSS.user_choice_item; i++)
		{
			RRPSS.item_ids[i] = RRPSS.sc.nextInt();
		}
		
		// check if all items were in fact added
		/*for (int i=0; i<RRPSS.user_choice_item; i++)
		{
			System.out.println(RRPSS.item_ids[i]);
			//RRPSS.item_ids[i] = RRPSS.sc.nextInt();
		}*/
		
		if (RRPSS.current_orders.add_new_order(RRPSS.user_input_staff_id, RRPSS.user_input_reservation_id, RRPSS.item_ids))
			System.out.println("The order has been created successfully!");
		else System.out.println("The order has not been created. Please refer to the above error message and try again.");
	}
	
	
	private static void view_current_orders()
	{
		// print all current orders
		RRPSS.current_orders.print_all_orders();
	}
	
	
	private static void add_items_to_an_order() throws IOException {
		// add new items to a current order 
		RRPSS.current_orders.print_all_orders();
		System.out.println("Please enter the ID of the order you would like to edit:");
		RRPSS.user_choice = RRPSS.sc.nextInt();
		System.out.println("How many items would you like to add to the selected order");
		RRPSS.user_choice_item = RRPSS.sc.nextInt();
		RRPSS.item_ids = new int[RRPSS.user_choice_item];
		// print the menu
		RRPSS.current_menu.list_all_items_and_promos();
		System.out.println("Please enter the IDs of the items/promos you would like to add to the order:");
				
		// prompt user to enter item ids
		for (int i=0; i<RRPSS.user_choice_item; i++)
		{
			RRPSS.item_ids[i] = RRPSS.sc.nextInt();
		}
		
		if (RRPSS.current_orders.add_items_to_current_order(RRPSS.user_choice, RRPSS.item_ids))
			System.out.println("The order has been updated successfully!");
		else System.out.println("The order has not been updated. Please refer to the above error message and try again.");
	}
	
	
	private static void remove_items_from_an_order() throws IOException {
		// remove items from a current order 
		RRPSS.current_orders.print_all_orders();
		System.out.println("Please enter the ID of the order you would like to edit:");
		RRPSS.user_choice = RRPSS.sc.nextInt();
		System.out.println("How many items would you like to remove from the selected order");
		RRPSS.user_choice_item = RRPSS.sc.nextInt();
		RRPSS.item_ids = new int[RRPSS.user_choice_item];
		// print the menu
		RRPSS.current_menu.list_all_items_and_promos();
		System.out.println("Please enter the IDs of the items/promos you would like to remove from the order:");
				
		// prompt user to enter item ids
		for (int i=0; i<RRPSS.user_choice_item; i++)
		{
			RRPSS.item_ids[i] = RRPSS.sc.nextInt();
		}
		
		if (RRPSS.current_orders.remove_items_from_current_order(RRPSS.user_choice, RRPSS.item_ids))
			System.out.println("The order has been updated successfully!");
		else System.out.println("The order has not been updated. Please refer to the above error message and try again.");
	}
	
	
	public static void reservation_interface()
	{
		// for checking, making and removing reservations
		while (true)
		{
			System.out.println("Welcome to the reservation interface, please select an option:");
			System.out.println("\t(1) - CREATE a new reservation");
			System.out.println("\t(2) - CHECK current reservations");
			System.out.println("\t(3) - REMOVE a reservation");
			System.out.println("\t(4) - CHECK table availability");
			System.out.println("\t(5) - Exit");
			RRPSS.user_choice = RRPSS.sc.nextInt();
			
			switch (RRPSS.user_choice)
			{
				case 1:
					// CREATE a new reservation
					RRPSS.create_new_reservation();
					break;
					
				case 2:
					// CHECK current reservations
					RRPSS.check_current_reservations();
					break;
					
				case 3:
					// REMOVE a reservation
					RRPSS.remove_a_current_reservation();
					break;
					
				case 4:
					// CHECK table availability
					RRPSS.check_table_availability();
					break;
					
				case 5:
					// exit the interface
					return;
					
				default:
					// invalid input
					System.out.println("Invalid input, please select one of the options above.");
					break;
			}
			
			
		}
	}
	
	private static void create_new_reservation()
	{
		String local_year_input, local_month_input, local_day_input, local_hour_input;
		// get all relevant information from the user 
		System.out.println("Please enter a Name for the reservation: ");
		RRPSS.user_input_name = RRPSS.sc.next();
		
		System.out.println("Please enter the number of pax:");
		RRPSS.user_input_number_pax = RRPSS.sc.nextInt();
		
		System.out.println("Please enter your contact number:");
		RRPSS.user_input_contact_nr = RRPSS.sc.next();
		
		System.out.println("Are you a member (y/n)?");
		RRPSS.user_input_boolean = (RRPSS.sc.next().charAt(0)=='y' 	|| RRPSS.sc.next().charAt(0)=='Y');
		
		// get the user input for time 
		System.out.println("Please enter the year (yyyy):");
		RRPSS.sc.nextLine();
		local_year_input = RRPSS.sc.nextLine();
		
		System.out.println("Please enter the month (mm):");
		//RRPSS.sc.nextLine();
		local_month_input = RRPSS.sc.nextLine();
		
		System.out.println("Please enter the day (dd):");
		//RRPSS.sc.nextLine();
		local_day_input = RRPSS.sc.nextLine();
		
		System.out.println("Please enter the hour (hh):");
		//RRPSS.sc.nextLine();
		local_hour_input = RRPSS.sc.nextLine();
		
		
		
		// pass all information to the bookings management
		if (RRPSS.bookings_management.create_new_reservation(
					RRPSS.user_input_number_pax,
					RRPSS.user_input_name,
					RRPSS.user_input_contact_nr,
					RRPSS.user_input_boolean,
					RRPSS._convert_times_to_ts(
							local_year_input,
							local_month_input,
							local_day_input,
							local_hour_input
					),
					RRPSS.current_timestamp
					)
				)
			System.out.println("The reservation has been made successfully!");
		else System.out.println("There reservations has NOT been made successfully. Please refer to the above error.");
	}
	
	private static void check_current_reservations()
	{
		// simply print all current reservations (maybe add time later on)
		RRPSS.bookings_management.print_reservations();
	}
	
	private static void remove_a_current_reservation()
	{
		// print all current reservations, and remove one based on reservation id
		RRPSS.bookings_management.print_reservations();
		System.out.println("Please enter the ID of the reservation you would like to remove.");
		RRPSS.user_choice = RRPSS.sc.nextInt();
		
		if (RRPSS.bookings_management.remove_current_reservation_by_id(RRPSS.user_choice))
			System.out.println("The reservation has been removed successfully!");
		else System.out.println("The reservations has NOT been removed successfully. Please refer to the above error.");
	}
	
	private static void check_table_availability()
	{
		// print the available table times 
		RRPSS.bookings_management.print_available_tables();
	}
	
	
	public static void admin_interface() throws IOException {
		// for adding and removing things from the menu and print sales revenue
		while (true)
		{
			System.out.println("Welcome to the admin interface, please select an option:");
			System.out.println("\t(1) - CREATE a new Menu item");
			System.out.println("\t(2) - UPDATE a current Menu item");
			System.out.println("\t(3) - DELETE a current Menu item");
			System.out.println("\t(4) - CREATE a new Promo item");
			System.out.println("\t(5) - UPDATE a current Promo item");
			System.out.println("\t(6) - DELETE a current Promo item");
			System.out.println("\t(7) - ADD a new Employee");
			System.out.println("\t(8) - EDIT a current Employee");
			System.out.println("\t(9) - DELETE a current Employee");
			System.out.println("\t(10) - Print Sales report");
			System.out.println("\t(11) - RETURN to main interface");
			
			RRPSS.user_choice = RRPSS.sc.nextInt();
			
			switch(RRPSS.user_choice)
			{
				case 1:
					// CREATE a new Menu item
					RRPSS.add_item_to_menu();
					break;
					
				case 2:
					// UPDATE a current Menu item
					RRPSS.edit_item_on_menu();
					break;
					
				case 3:
					// DELETE a current Menu item
					RRPSS.remove_item_from_menu();
					break;
					
				case 4:
					// CREATE a new Promo item
					RRPSS.add_promo_item_to_menu();
					break;
					
				case 5:
					// UPDATE a current Promo item
					RRPSS.update_promo_item_on_menu();
					break;
					
				case 6:
					// DELETE a current Promo item
					RRPSS.remove_promo_item_from_menu();
					break;
					
				case 7:
					// ADD a new Employee
					RRPSS.add_new_employee();
					break;
					
				case 8:
					// EDIT a current Employee
					RRPSS.edit_current_employee();
					break;
					
				case 9:
					// DELETE a current Employee
					RRPSS.remove_current_employee();
					break;
					
				case 10:
					// Print Sales report
					break;
					
				case 11:
					return;
					
				default:
					System.out.println("Invalid input, please select one of the options above.");
					break;
			}
		}
	}
	
	
	private static void add_item_to_menu() throws IOException {
		System.out.println("Item Name:");
		//RRPSS.user_input_name = RRPSS.sc.next();
		RRPSS.sc.nextLine();
		RRPSS.user_input_name = RRPSS.sc.nextLine(); // this will allow the user to enter multiple words at once
		
		System.out.println("Item Price:");
		RRPSS.user_input_price = RRPSS.sc.nextFloat();
		
		// select the item type from enum
		RRPSS.current_menu.list_enum_types();
		RRPSS.user_choice = RRPSS.sc.nextInt();
		
		System.out.println("Item Description:");
		RRPSS.sc.nextLine();
		RRPSS.user_input_description = RRPSS.sc.nextLine();
		
		// pass all elements into the CurrentMenu function
		if (RRPSS.current_menu.add_item_to_menu(RRPSS.user_input_name, RRPSS.user_input_price, RRPSS.user_choice, RRPSS.user_input_description))
			System.out.println("The item has been added successfully!");
		else System.out.println("The item has NOT been added. See the error above. Please Try again.");
	}
	
	private static void add_promo_item_to_menu()
	{
		System.out.println("Promo Name:");
		RRPSS.sc.nextLine();
		RRPSS.user_input_promo_name = RRPSS.sc.nextLine();
		
		System.out.println("Promo Price:");
		RRPSS.user_input_price = RRPSS.sc.nextFloat();
		
		// select promo items
		System.out.println("How many items are included in this Promo:");
		RRPSS.user_choice_nr_promo_items = RRPSS.sc.nextInt();
		RRPSS.item_ids = new int[RRPSS.user_choice_nr_promo_items];
		
		// iterate over the number of items and add them
		// first print all of the items with ids
		RRPSS.current_menu.list_all_items_with_id();
		System.out.println("Please list the IDs of all items you would like to include: ");
		for (int i=0; i<RRPSS.user_choice_nr_promo_items; i++)
		{
			RRPSS.item_ids[i] = RRPSS.sc.nextInt();
		}
		
		System.out.println("Promo Description:");
		RRPSS.sc.nextLine();
		RRPSS.user_input_description = RRPSS.sc.nextLine();
		
		// pass all elements into the CurrentMenu class
		if (RRPSS.current_menu.add_promo_item_to_menu(RRPSS.user_input_promo_name, RRPSS.user_input_price, RRPSS.item_ids, RRPSS.user_input_description))
			System.out.println("The promo has been added successfully!");
		else System.out.println("The promo has NOT been added. See the error above and please Try again.");
	}
	
	private static void add_new_employee()
	{
		
		System.out.println("Employee Name: ");
		RRPSS.sc.nextLine();
		String name = RRPSS.sc.nextLine();
		
		System.out.println("Employee Gender: ");
		//RRPSS.sc.nextLine();
		String gender = RRPSS.sc.nextLine();
		
		System.out.println("Employee Job Title: ");
		//RRPSS.sc.nextLine();
		String job_title = RRPSS.sc.nextLine();
		
		// pass all information to the StaffManagement class for error checking
		if (RRPSS.staff_management.add_new_employee(name, gender, job_title))
			System.out.println("The employee has been added successfully!");
		else System.out.println("The employee has NOT been added. See the error above and please Try again.");
	}
	
	
	private static void remove_item_from_menu() throws IOException {
		// user only has to select an index
		System.out.println("Please select an item id to delete:");
		RRPSS.current_menu.list_all_items_with_id();
		RRPSS.user_choice = RRPSS.sc.nextInt();
		if (RRPSS.current_menu.remove_item_from_menu(RRPSS.user_choice))
			System.out.println("The item has been removed successfully!");
		else System.out.println("The item has NOT been removed. Please make sure you enter a valid ID and try again.");
	}
	
	private static void remove_promo_item_from_menu() throws IOException {
		// user only has to select an index
		System.out.println("Please select a Promo id to delete:");
		RRPSS.current_menu.list_all_promos_with_id();
		RRPSS.user_choice = RRPSS.sc.nextInt();
		if (RRPSS.current_menu.remove_item_from_menu(RRPSS.user_choice))
			System.out.println("The Promo has been removed successfully!");
		else System.out.println("The Promo has NOT been removed. Please make sure you enter a valid ID and try again.");
	}
	
	private static void remove_current_employee() throws IOException 
	{
		// user only has to select an index
		System.out.println("Please select a Staff id to delete:");
		RRPSS.staff_management.list_all_employees();
		int user_choice = RRPSS.sc.nextInt();
		if (RRPSS.staff_management.delete_emplyee(user_choice))
			System.out.println("The Employee has been removed successfully!");
		else System.out.println("The Employee has NOT been removed. Please make sure you enter a valid ID and try again.");
	}
	
	private static void edit_item_on_menu() throws IOException {
		// user select item to be edited 
		System.out.println("Please select an item you would like to edit: ");
		RRPSS.current_menu.list_all_items_with_id();
		RRPSS.user_choice_item = RRPSS.sc.nextInt();
		// TODO validate user input
		System.out.println("You selected the following item.");
		RRPSS.current_menu.print_specific_item(RRPSS.user_choice_item);
		
		System.out.println("Which aspect of the above item would you like to edit?");
		System.out.println("\t(1) - Name");
		System.out.println("\t(2) - Price");
		System.out.println("\t(3) - Description");
		System.out.println("\t(4) - Noting (return)");
		RRPSS.user_choice = RRPSS.sc.nextInt();
		switch(RRPSS.user_choice)
		{
			case 1:
				// change the Name
				System.out.println("Please enter the new Name:");
				//RRPSS.user_input_name = RRPSS.sc.next();
				RRPSS.sc.nextLine();
				RRPSS.user_input_name = RRPSS.sc.nextLine();
				RRPSS.user_return = RRPSS.current_menu.edit_name_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_name);
				break;
				
			case 2:	
				// change the price
				System.out.println("Please enter the new Price:");
				RRPSS.user_input_price = RRPSS.sc.nextFloat();
				RRPSS.user_return = RRPSS.current_menu.edit_price_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_price);
				break;
				
			case 3:
				// change the Description
				System.out.println("Please enter the new Description:");
				RRPSS.sc.nextLine();
				RRPSS.user_input_description = RRPSS.sc.nextLine();
				RRPSS.user_return = RRPSS.current_menu.edit_description_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_description);
				break;
				
			case 4:
				return;
				
			default:
				System.out.println("Invalid input!");
				RRPSS.user_return = false;
				break;
		}
		if (RRPSS.user_return)
			System.out.println("The changes have been made successfully!");
		else System.out.println("The changes were NOT saved. Please referr to the above error message and try again.");
	}
	
	
	private static void edit_current_employee() throws IOException
	{
		boolean user_return = false;
		System.out.println("Please select an Employee you would like to edit:");
		RRPSS.staff_management.list_all_employees();
		int user_choice_staff_id = RRPSS.sc.nextInt();
		
		if (staff_management.check_if_staff_id_exists(user_choice_staff_id)==false)
		{
			System.out.println("The employee id selected does not exists. Please try again!"); return;
		}
		
		// TODO validate user input
		System.out.println("You selected the following employee: ");
		RRPSS.staff_management.print_employee_by_id(user_choice_staff_id);
		
		System.out.println("Which aspect of the above employee would you like to edit?");
		System.out.println("\t(1) - Name");
		System.out.println("\t(2) - Job Title");
		System.out.println("\t(3) - Gender");
		System.out.println("\t(4) - Nothing (return)");
		int user_o_choice = RRPSS.sc.nextInt();
		
		switch(user_o_choice)
		{
			case 1:
				// change the Name
				System.out.println("Please enter the new Name:");
				RRPSS.sc.nextLine();
				String user_input_name = RRPSS.sc.nextLine();
				user_return = RRPSS.staff_management.change_employee_attribute(user_choice_staff_id, user_input_name, "name");
				break;
				
			case 2:	
				// Change the Job Title
				System.out.println("Please enter the new Job Title:");
				RRPSS.sc.nextLine();
				String user_input_job_title = RRPSS.sc.nextLine();
				user_return = RRPSS.staff_management.change_employee_attribute(user_choice_staff_id, user_input_job_title, "job title");
				break;
				
			case 3:
				// change the Gender
				System.out.println("Please enter the new Gender:");
				RRPSS.sc.nextLine();
				String user_input_gender = RRPSS.sc.nextLine();
				user_return = RRPSS.staff_management.change_employee_attribute(user_choice_staff_id, user_input_gender, "gender");
				break;
				
			case 4:
				return;
				
			default:
				System.out.println("Invalid input!");
				user_return = false;
				break;
		}
		if (user_return)
			System.out.println("The changes have been made successfully!");
		else System.out.println("The changes were NOT saved. Please referr to the above error message and try again.");
	}
	
	
	
	private static void update_promo_item_on_menu() throws IOException {
		// user select promo to be edited 
		System.out.println("Please select a Promo you would like to edit: ");
		RRPSS.current_menu.list_all_promos_with_id();
		RRPSS.user_choice_item = RRPSS.sc.nextInt();
		System.out.println("You selected the following promo.");
		RRPSS.current_menu.print_specific_promo(RRPSS.user_choice_item);
		
		System.out.println("Which aspect of the above promo would you like to edit?");
		System.out.println("\t(1) - Name");
		System.out.println("\t(2) - Price");
		System.out.println("\t(3) - Items");
		System.out.println("\t(4) - Description");
		System.out.println("\t(5) - Exit");
		RRPSS.user_choice = RRPSS.sc.nextInt();
		RRPSS.user_return = false; // by default
		switch(RRPSS.user_choice) 
		{
			case 1:
				// Change the name
				System.out.println("Please enter the new Name:");
				//RRPSS.user_input_name = RRPSS.sc.next();
				RRPSS.sc.nextLine();
				RRPSS.user_input_name = RRPSS.sc.nextLine();
				RRPSS.user_return = RRPSS.current_menu.edit_name_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_name);
				break;
				
			case 2:
				// change the price
				System.out.println("Please enter the new Price:");
				RRPSS.user_input_price = RRPSS.sc.nextFloat();
				RRPSS.user_return = RRPSS.current_menu.edit_price_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_price);
				break;
				
			case 3:
				// change the items
				System.out.println("Would you like to add new items (y/n)?");
				if (RRPSS.sc.next().charAt(0)=='y')
				{
					// collect indices to add
					System.out.println("How many items would you like to add?");
					RRPSS.user_choice_nr_promo_items = RRPSS.sc.nextInt();
					// iterate over the list and collect indices 
					RRPSS.current_menu.list_all_items_with_id();
					RRPSS.item_ids = new int[RRPSS.user_choice_nr_promo_items];
					System.out.println("Please enter the ids of the items you would like to add.");
					for (int i=0; i<RRPSS.user_choice_nr_promo_items; i++)
					{
						RRPSS.item_ids[i] = RRPSS.sc.nextInt();
					}
					RRPSS.user_return = RRPSS.current_menu.add_items_to_promo(RRPSS.user_choice_item, RRPSS.item_ids);
				}
				
				System.out.println("Would you like to delete some of the current items (y/n)?");
				if (RRPSS.sc.next().charAt(0)=='y')
				{
					// collect indices to add
					System.out.println("How many items would you like to delete?");
					RRPSS.user_choice_nr_promo_items = RRPSS.sc.nextInt();
					// iterate over the list and collect indices 
					System.out.println("Please enter the ids of the items you would like to remove.");
					RRPSS.item_ids = new int[RRPSS.user_choice_nr_promo_items];
					for (int i=0; i<RRPSS.user_choice_nr_promo_items; i++)
					{
						RRPSS.item_ids[i] = RRPSS.sc.nextInt();
					}
					RRPSS.user_return = RRPSS.current_menu.remove_items_from_promo(RRPSS.user_choice_item, RRPSS.item_ids);
				}
				break;
				
			case 4:
				// change the description
				System.out.println("Please enter the new Description:");
				RRPSS.sc.nextLine();
				RRPSS.user_input_description = RRPSS.sc.nextLine();
				RRPSS.user_return = RRPSS.current_menu.edit_description_of_item_on_menu(RRPSS.user_choice_item, RRPSS.user_input_description);
				break;
				
			case 5:
				return;
				
			default:
				System.out.println("Invalid input!");
				RRPSS.user_return = false;
				break;
		}
		
		if (RRPSS.user_return)
			System.out.println("The changes have been made successfully!");
		else System.out.println("The changes were NOT saved. Please referr to the above error message and try again.");
		
	}
	
	
	
	
}







