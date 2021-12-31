package cz2002_project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CurrentOrders {
	public ArrayList<Order> current_orders;
	public int temp_id=0;
	public boolean return_code;
	public final int max_nr_orders=250;
	
	public CurrentOrders() {
		current_orders = new ArrayList<>();
		Path path = Paths.get("Current_Orders.csv");
		int i = 0;

		try (BufferedReader br = Files.newBufferedReader(path)) {
			String line = br.readLine();

			while (line != null) {
				String[] tokens = line.split(",");

				int n = tokens.length;

				int[] arr = new int[n-3];
				int j = 3;
				int k = 0;
				while (j < n) {
					arr[k] = Integer.parseInt(tokens[j]);
					j++; k++;
				}
				Order order = new Order(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), arr);
				this.current_orders.add(order);
				line = br.readLine();
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean add_new_order(int staff_id, int reservation_id, int[] item_ids) throws IOException {
		// get next available order id
		this.temp_id = _get_next_order_id();

		// add a new order to the order list
		this.current_orders.add(new Order(this.temp_id, staff_id, reservation_id, item_ids));

		writeToCsv();
		return true;
	}
	
	public boolean remove_order(int order_id) throws IOException {
		// remove an order from the current order list
		this.return_code = false;
		for (int i=0; i<this.current_orders.size(); i++)
		{
			if (this.current_orders.get(i).getOrder_id() == order_id) {
				// insert here
				this.current_orders.remove(i);
				this.return_code = true;
				break;
			}
		}
		if (return_code==false)
		{
			System.out.println("The entered order_id does not exist.");
		}
		
		// also removes it from file (just override with the current list)
		writeToCsv();
		return return_code;
	}
	
	public boolean add_items_to_current_order(int order_id, int[] item_ids) throws IOException {
		// add items to the current order
		this.return_code = true;

		for (int i=0; i<this.current_orders.size(); i++)
		{
			if (this.current_orders.get(i).getOrder_id() == order_id)
			{
				int j=0;
				for (j=0; j<this.current_orders.get(i).getItem_ids().length; j++) {
					if (this.current_orders.get(i).getItem_ids()[j] == -1) {
						// find the index to continue adding new items
						break;
					}
				}

				// copy into current_orders
				for (int k=0; k<item_ids.length; k++) {
					this.current_orders.get(i).getItem_ids()[j] = item_ids[k];
					j++;
				}

				break;
			}
		}

		// update the relevant file
		writeToCsv();

		return return_code;
	}
	
	public boolean remove_items_from_current_order(int order_id, int[] item_ids) throws IOException {
		// remove the items from the relevant order
		this.return_code = true;
		for (int i=0; i<this.current_orders.size(); i++)
		{
			if (this.current_orders.get(i) !=null && this.current_orders.get(i).getOrder_id() == order_id)
			{
				for (int ii=0; ii<item_ids.length; ii++)
				{
					this.return_code = (this.current_orders.get(i).remove_item(item_ids[ii]) && this.return_code);
					// if any of them are false, return_code will be false
				}

				break;
			}
		}
		writeToCsv();
		if (this.return_code==false) return return_code;

		// update the file accordingly (both the current order file and the sales report file

		return true;
	}
	
	public void remove_all_items_from_order_by_reservation_id(int reservation_id) throws IOException
	{
		for (int i=0; i<this.current_orders.size(); i++)
		{
			if (this.current_orders.get(i) !=null && this.current_orders.get(i).getReservation_id()==reservation_id)
			{
				// remove this specific order
				this.remove_order(this.current_orders.get(i).getOrder_id());
				
			}
		}
		
		// TODO update file accordingly
	}
	
	public void print_all_orders()
	{
		// print all current orders 
		for (int i=0; i<this.current_orders.size(); i++) {
			System.out.print("   Order ID: " + this.current_orders.get(i).getOrder_id());
			System.out.printf("%-70s%n", " Staff ID: " + this.current_orders.get(i).getStaff_id());
			System.out.println("   Reservation ID: " + this.current_orders.get(i).getReservation_id());
			for (int j=0; j<this.current_orders.get(i).getItem_ids().length; j++) {
				//System.out.println(this.current_orders.get(i).getItem_ids()[j]);
				if (this.current_orders.get(i).getItem_ids()[j] == -1) {
					continue;
				}
				if (this.current_orders.get(i).getItem_ids()[j] == 0) {
					continue;
				}
				System.out.println("Order Item ID: " + this.current_orders.get(i).getItem_ids()[j]);
			}
			System.out.println();
		}
		//System.out.println("This should print a list of all current orders :)");
	}
	
	private int _get_next_order_id()
	{
		// iterate over the list of orders and increment the largest one by 1
		this.temp_id = 0;
		for (int i=0; i<this.current_orders.size(); i++)
		{
			if (this.current_orders.get(i)==null)
				break;
			
			if (this.current_orders.get(i).order_id > this.temp_id)
			{
				this.temp_id = this.current_orders.get(i).order_id;
			}
		}
		return this.temp_id+1;
	}

	private void writeToCsv() throws IOException {
		Path path = Paths.get("Current_Orders.csv");
		FileWriter fw = new FileWriter(String.valueOf(path));

		for(Order order: this.current_orders) {
			try {
				fw.write(order.getOrder_id() + ",");
				fw.write(order.getReservation_id() + ",");
				fw.write(order.getStaff_id() + ",");
				for (int j=0; j<order.getItem_ids().length; j++) {
					fw.write(order.getItem_ids()[j] + ",");
				}
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