package cz2002_project;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz2002_project.Reservation;
import cz2002_project.Table;

public class Bookings_management {
	public int total_nr_bookings=0;
	private Reservation[] reservations;
	public Table[] tables;
	private final int max_nr_reservations=100;
	
	
	public Bookings_management()
	{
		this.reservations = new Reservation[this.max_nr_reservations];
		// TODO populate reservations from file
		this.reservations[0] = new Reservation(0, 5, "leon", "90853283", 1, false, 1634342400, 1634343400);
		this.total_nr_bookings++;
		
		
		
		
		// check how many tables in file
		this.tables = new Table[1]; // 1 should be the number read from file, and not fixed
		// the following line is only for testing and should be removed once the tables are read from the file
		this.tables[0] = new Table(10, 1);
		
		
		// read all tables from file and initialize object accordingly
		
	}
	
	public boolean create_new_reservation(int nr_pax, String name, String contact_nr, boolean is_member, int reservation_start_time_ts, int current_ts)
	{
		int reservation_end_time_ts = reservation_start_time_ts + (60*60);
		int[] reserved_table_id_list;
		boolean temp_already_in_use;
		// populate the above list with -1 values (in case the table_id 0 exists)
		reserved_table_id_list = new int[this.tables.length];
		for (int i=0; i<this.tables.length; i++) reserved_table_id_list[i] = -1;
		
		// returns true if successful and false otherwise
		
		// check whether the provided info is correct
		if (name.matches(".*\\d.*")){
			System.out.println("Please enter a valid name.");
			return false;
		}
		for (int i = 0; i < contact_nr.length(); ++i) {
			if (Character.isLetter(contact_nr.charAt(i))) {
				System.out.println("Please enter a valid number.");
				return false;
			}
		}
		// check whether the booking is actually into the future (i.e. compare to the current timestamp
		if (reservation_start_time_ts <= current_ts){
			System.out.println("The selected timeslot is invalid. Please select a future time.");
			return false;
		}
		
		// try to match it to one of the available tables
		
		// matching "algorithm" are two simple for loops hahahahahaah don't @ me
		for (int i=0; i<this.reservations.length; i++)
		{
			// first loop to iterate over the reservations by index
			
			// for all reservations in the selected timeframe, check which table_id they are on
			if (this.reservations[i]!=null && this.reservations[i].start_ts>(reservation_start_time_ts-(59*60)) && this.reservations[i].end_ts<(reservation_start_time_ts+((59+60)*60)))
				// this means that this specific reservation is booked within the timeframe of 59min before start and 1h 59min after start. I.e. during our time
				// this means we have to save the table id
				for (int ii=0; ii<reserved_table_id_list.length; ii++)
				{
					if (reserved_table_id_list[ii]==-1)
					{
						// change to the current table id
						reserved_table_id_list[ii] = this.reservations[i].table_id;
						break;
					}
				}
		}
		// now we have a list of all tables that are booked during the relevant time slot
		// try to match the attempted booking to a table, and then check whether the table is already booked
		for (int i=0; i<this.tables.length; i++)
		{
			if (this.tables[i].capacity >= nr_pax)
			{
				temp_already_in_use = false;
				for (int ii=0; ii<reserved_table_id_list.length; ii++)
				{
					if (reserved_table_id_list[ii] == this.tables[i].table_id)
					{
						// this means that the table is already in use
						temp_already_in_use = true;
						break;
					}
				}
				if (temp_already_in_use==false)
				{
					// this means that a suitable table is in fact available
					// hence, create the reservation
					for (int iii=0; iii<this.reservations.length; iii++)
					{
						if (this.reservations[iii] == null)
						{
							// add the reservation here and return
							this.reservations[iii] = new Reservation(
														_get_next_available_reservation_id(),
														nr_pax,
														name,
														contact_nr,
														this.tables[i].table_id,
														is_member,
														reservation_start_time_ts,
														reservation_end_time_ts);
							this.total_nr_bookings++;
							return true;
						}
					}
					// if the loop is exited, this means that all reservations are already populated. i.e. return error
					System.out.println("Even though a suitable table is available, the maximum number of reservations has been reached.");
					return false;
				}
			}
		}
		
		// if a match is found, create a reservation
		
		System.out.println("All tables with the correct capacity at this timeslot are already booked.");
		return false;
	}
	
	public boolean remove_current_reservation_by_id(int reservation_id)
	{
		// iterate over the current reservation ids and delete accordingly
		for (int i=0; i<this.max_nr_reservations; i++)
		{
			if (this.reservations[i]!=null && this.reservations[i].reservation_id == reservation_id)
			{
				// remove at index i
				this.reservations[i] = null;
				for (int ii=i; ii<this.max_nr_reservations-1; ii++)
				{
					this.reservations[ii]=this.reservations[ii+1];
				}
				return true;
				
			}
		}
		return false;
	}
	
	public void remove_all_timely_reservations(int start_ts, int end_ts)
	{
		for (int i=0; i<this.total_nr_bookings; i++)
		{
			if (
					this.reservations[i] != null && 
					this.reservations[i].end_ts>=start_ts && 
					this.reservations[i].end_ts<=end_ts
				)
			{
				// this means that this is the reservation we are looking for
				this.remove_current_reservation_by_id(this.reservations[i].reservation_id);
			}
		}
	}
	
	
	public void print_reservations()
	{
		// print all current reservations 
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		for (int i=0; i<this.total_nr_bookings; i++){
			if (this.reservations[i]==null) continue;
			System.out.printf("%-60s", "   Name of Customer: " + this.reservations[i].name);
			System.out.printf("%-10s%n", "Contact Number: " + this.reservations[i].contact_nr);
			System.out.printf("%-60s", "   Member: " + (this.reservations[i].is_member==true ? "Yes" : "No"));
			System.out.printf("%10s%n", "Reservation ID:" + this.reservations[i].reservation_id);
			System.out.printf("%-60s%n", "   Table Number: " + this.reservations[i].table_id);
			System.out.printf("%10s%n", "   Number of Pax: " + this.reservations[i].nr_pax);
			//System.out.printf("%-20s%n", "   Start Time: " + this.reservations[i].start_ts);
			//System.out.printf("%-20s%n", "   End Time: " + this.reservations[i].end_ts);
			System.out.printf("%s%n", "   Start Time: " + this._convert_ts_to_date_string(this.reservations[i].start_ts));
			System.out.printf("%s%n", "   End Time:   " + _convert_ts_to_date_string(this.reservations[i].end_ts));
			System.out.print("\n" + new String(new char[87]).replace("\0", "-"));
			System.out.println();
		}
	}
	
	public void print_availability()
	{
		// print the available time slots ( max 25 or so) optimally in some table format
		List<Integer> times = new ArrayList<>();
		int current_time = RRPSS.current_timestamp;
		int current_ts = 1634342400; // from 8am to 10pm on 16.10.21
		for(int i = 0; i < 15; i++) {
			times.add(current_ts+(i*60*60));
		}
		int nr_tables_empty_at_ts = 0;
		int nr_timeslots_available = 0;
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.print(String.format("%6s %18s %21s %32s %5s", "|     ", "  Timeslots available", "|    ", "Number of Tables available", "|"));
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		for (int i=0; i<times.size(); i++){
			if (times.get(i)>=current_time){
				for (int j=0; j<this.tables.length; j++){
					if (check_if_table_is_currently_being_served(this.tables[j].table_id, times.get(i))==false) {
						nr_tables_empty_at_ts++;
					}
				}
			}
			if (nr_tables_empty_at_ts>0){
				System.out.print(String.format("%6s %18s %16s %32s %5s", "|          ", _convert_ts_to_date_string(times.get(i)), "|    ", nr_tables_empty_at_ts, "|"));
				System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
				nr_timeslots_available++;
			}
		}
		if (nr_timeslots_available==0){
			System.out.print(String.format("%20s %43s %21s", "|                   ","There are no more available timeslots today.", "     |"));
			System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		}
		//System.out.println("This should print all available slots for reservations TODO");
	}
	
	public void print_available_tables()
	{
		// print the available tables (take time into consideration) optimally in some table format
		int current_ts = RRPSS.current_timestamp;
		int nr_tables_available = 0;
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.print(String.format("%6s %18s %21s %32s %6s", "|     ", "Table ID", "|    ", "Capacity of Tables available", "   |"));
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		for (int i=0; i<this.tables.length; i++){
			if (check_if_table_is_currently_being_served(this.tables[i].table_id, current_ts)==false) {
				System.out.print(String.format("%6s %18s %21s %32s %6s", "|     ", this.tables[i].table_id, "|    ", this.tables[i].capacity, "|"));
				System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
				nr_tables_available++;
			}
		}
		if (nr_tables_available==0){
			System.out.print(String.format("%20s %35s %30s", "|                   "," There are no tables available now.", "                        |"));
			System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		}
		//System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		//System.out.println("This should currently available tables TODO");
	}
	
	public boolean check_if_table_is_currently_being_served(int table_nr, int current_ts)
	{
		for (int i=0; i<this.total_nr_bookings; i++)
		{
			if (
					this.reservations[i] != null && 
					this.reservations[i].start_ts<=current_ts && 
					this.reservations[i].end_ts>=current_ts && 
					this.reservations[i].table_id==table_nr
				)
			{
				// this means that this is the reservation we are looking for
				return true;
			}
		}
		return false;
	}
	
	public int get_reservation_id_by_table_nr(int table_nr, int current_ts)
	{
		for (int i=0; i<this.total_nr_bookings; i++)
		{
			if (
					this.reservations[i] != null && 
					this.reservations[i].start_ts<=current_ts && 
					this.reservations[i].end_ts>=current_ts && 
					this.reservations[i].table_id==table_nr
				)
			{
				// this means that this is the reservation we are looking for
				return this.reservations[i].reservation_id;
			}
		}
		return -1; // this should not be reached as "check_if_table_is_currently_being_served" should be called first
		
	}
	
	
	private int _get_next_available_reservation_id()
	{
		int return_id=0;
		for (int i=0; i<this.reservations.length; i++)
		{
			if (this.reservations[i] != null)
			{
				if (this.reservations[i].reservation_id > return_id) return_id = this.reservations[i].reservation_id;
			}
		}
		return return_id+1;
	}
	
	
	private String _convert_ts_to_date_string(int current_ts)
	{
		// used for beautified printing
		String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current_ts * 1000L));
		return dateAsText;
	}

	
}
