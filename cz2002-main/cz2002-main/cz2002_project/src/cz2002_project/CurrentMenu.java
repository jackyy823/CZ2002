package cz2002_project;
import cz2002_project.Menu;
import cz2002_project.Menu_item;
import cz2002_project.Promo_set;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class CurrentMenu {
	public Menu[] menu;
	public int local_item_id=1;
	public boolean successful;
	public int nr_items_on_menu=0;
	public final int max_nr_items_on_menu=250;
	public ArrayList<List<String>> data;
	public ReadWrite rw;

	public CurrentMenu()
	{
		this.menu = new Menu[this.max_nr_items_on_menu];
		rw = new ReadWrite();
		data = rw.readCsv("Menu.csv");
		for (int i=1; i<data.size(); i++) {
			Menu.menu_type e = Menu.menu_type.valueOf(data.get(i).get(3));
			this.menu[i-1] = new Menu(data.get(i).get(1), Float.parseFloat(data.get(i).get(4)), e, data.get(i).get(2), Integer.parseInt(data.get(i).get(0)));
			//System.out.println(this.menu[i].item_id);
			this.nr_items_on_menu++;
		}
		// sort the items by their enum so that they are in the correct order when being printed
		this._re_sort_items(); // just incase the file had an error 
		
		
		// read the menu items from the csv file and instantiate the objects accordingly
		// read the promo items from the csv file and instantiate the objects accordingly
		
	}
	
	public boolean add_item_to_menu(String name, float price, int enum_int, String description) throws IOException {
		// check validation of input types
		for(int i=0; i<nr_items_on_menu; i++){
			if(this.menu[i].name.equals(name) && this.menu[i].type ==  Menu.menu_type.values()[enum_int]){
				System.out.println("Item is already on the menu.");
				return false;
			}
		}
		// return false if failed and print the reason why

		// find next viable item_id
		this.local_item_id = _get_next_item_id();
		// instantiate object
		this.menu[this.nr_items_on_menu] = new Menu_item(name, price, enum_int, description, this.local_item_id);

		List<String> list = new ArrayList<>();
		list.add(String.valueOf(this.menu[this.nr_items_on_menu].get_item_id()));
		list.add(String.valueOf(this.menu[this.nr_items_on_menu].get_name()));
		list.add(String.valueOf(this.menu[this.nr_items_on_menu].get_description()));
		list.add(String.valueOf(this.menu[this.nr_items_on_menu].getType()));
		list.add(String.valueOf(this.menu[this.nr_items_on_menu].get_price()));

		this.nr_items_on_menu++;
		// sort the items by their enum so that they are in the correct order when being printed
		this._re_sort_items();

		data.add(list);

		rw.outputCsv(data, "Menu.csv");
		return true;
	}
	
	public boolean add_promo_item_to_menu(String name, float price, int[] item_ids, String description)
	{		
		// check if the individual item exists
		for(int i = 0; i<item_ids.length; i++){
			//System.out.println(item_ids[i]);
			//System.out.println(this._does_item_exist(item_ids[i]));
			if(this._does_item_exist(item_ids[i]) == false){
				System.out.println("The selected item to be added does not exist.");
				return false;
			}
		}
		
		// check validation of input types 
		for(int i=0; i<nr_items_on_menu; i++){
			if(this.menu[i].name.equals(name) && this.menu[i].type ==  Menu.menu_type.PROMO_SET){
				System.out.println("Promo item is already on the menu.");
				return false;
			}
		}
		// return false if failed and print the reason why
		
		// find the next viable item_id 
		this.local_item_id = _get_next_item_id();
		// instantiate object
		this.menu[this.nr_items_on_menu] = new Promo_set(name, price, description, this.local_item_id);
		this.successful=true; 
		for (int i=1; i<item_ids.length; i++)
		{
			if (this._does_item_exist(item_ids[i]))
				this.menu[this.nr_items_on_menu].add_promo_item(this._return_item_by_id(item_ids[i]));
			else 
			{
				// item does not exist
				this.menu[this.nr_items_on_menu]=null;
				System.out.println("One of the items you tried to add does not exist. Please make sure to only enter correct item ids.");
				return false;
			}
		}

		this.nr_items_on_menu++;
		
		// sort the items by their enum so that they are in the correct order when being printed
		this._re_sort_items();

		// add to file (/ override the current file 
		// TODO
		return true;
	}

	public boolean remove_item_from_menu(int item_id) throws IOException {
		// check
		if(this._does_item_exist(item_id) == false){
			System.out.println("Item is not on the menu.");
			return false;
		}
		// return false if failed and print the reason why

		// remove from list
		this.successful=false; 
		for (int i=0; i<this.menu.length; i++)
		{
			if (this.menu[i].get_item_id() == item_id)
			{
				this.menu[i] = null;
				this.successful=true;
				data.remove(i);
				break;
			}
		}
		
		if (this.successful==false)
		{
			System.out.println("The item_id does not exist.");
			return this.successful;
		}
		
		// remove from file (override, should be easier)
		this._re_sort_items();
		this.nr_items_on_menu--;
		rw.outputCsv(data, "Menu.csv");
		
		return this.successful;
	}

	public boolean add_items_to_promo(int item_id, int[] item_ids)
	{
		// check if the item exists
		if (this._does_item_exist(item_id) == false)
		{
			System.out.println("The selected promo set does not exist.");
			return false;
		}
		
		// maybe check if the item_id is actually a promo
		if(this.menu[item_id].type != Menu.menu_type.PROMO_SET){
			System.out.println("The item selected is not a promo set.");
			return false;
		}
		
		// check if the individual item exists
		for(int i = 0; i<item_ids.length; i++){
			if(this._does_item_exist(item_ids[i]) == false){
				System.out.println("The selected item to be added does not exist.");
				return false;
			}
		}
		
		// add the items to the promo
		this.successful=true; 
		this.local_item_id = _get_item_index(item_id);
		for (int i=0; i<item_ids.length; i++)
		{
			if (this._does_item_exist(item_ids[i]))
				this.menu[this.local_item_id].add_promo_item(this._return_item_by_id(item_ids[i]));
			else 
			{
				// item does not exist
				this.menu[this.nr_items_on_menu]=null;
				System.out.println("One of the items you tried to add does not exist. Please make sure to only enter correct item ids.");
				return false;
			}
		}
		// add to file (/ override the current file 
		// TODO
		return true;
	}
	
	public boolean remove_items_from_promo(int item_id, int[] item_ids)
	{
		// check if the item exists
		if (this._does_item_exist(item_id) == false)
		{
			System.out.println("The selected promo does not exist.");
			return false;
		}
		this.successful=true; 
		this.local_item_id = _get_item_index(item_id);
		for (int i=0; i<item_ids.length; i++)
		{
			if (this._does_item_exist(item_ids[i]))
				this.menu[this.local_item_id].remove_promo_item(item_ids[i]);
			else 
			{
				// item does not exist
				this.menu[this.nr_items_on_menu]=null;
				System.out.println("One of the items you tried to remove does not exist. Please make sure to only enter correct item ids. All other items will be removed");
				return false;
			}
		}

		// add to file (/ override the current file 
		// TODO
		return true;
	}
	
	public boolean edit_name_of_item_on_menu(int item_id, String name) throws IOException {
		// check if item exists
		if (this._does_item_exist(item_id) == false)
		{
			System.out.println("The selected item/promo does not exist.");
			return false;
		}
		
		
		// edit the name of the relevant item_id item
		this.menu[this._get_item_index(item_id)].set_name(name);
		data.get(this._get_item_index(item_id)).set(1, name);
		
		// add to file (/ override the current file
		rw.outputCsv(data, "Menu.csv");
		return true; 
	}
	
	
	public boolean edit_price_of_item_on_menu(int item_id, float price) throws IOException {
		// check if item exists
		if (this._does_item_exist(item_id) == false)
		{
			System.out.println("The selected item/promo does not exist.");
			return false;
		}
		
		// edit the price of the relevant item_id item
		this.menu[this._get_item_index(item_id)].set_price(price);
		data.get(this._get_item_index(item_id)).set(4, String.valueOf(price));
		
		// add to file (/ override the current file
		rw.outputCsv(data, "Menu.csv");
		return true;
	}
	
	public boolean edit_description_of_item_on_menu(int item_id, String description) throws IOException {
		// check if item exists
		if (this._does_item_exist(item_id) == false)
		{
			System.out.println("The selected item/promo does not exist.");
			return false;
		}
				
				
		// edit the description of the relevant item_id item
		this.menu[this._get_item_index(item_id)].set_description(description);
		data.get(this._get_item_index(item_id)).set(2, description);
		
		// add to file (/ override the current file
		rw.outputCsv(data, "Menu.csv");
		return true;
	}
	
	
	public void list_enum_types()
	{
		System.out.println("trying to print enums");
		// actually print the string (1) - ENUM...
		int temp_int=0;
		for (Menu.menu_type temp: Menu.menu_type.values())
		{
			System.out.printf("\t(%d) - %s \n", temp_int, temp.toString());
			temp_int++;
		}
	}
	
	public Menu.menu_type select_enum(int enum_choice)
	{
		// TODO why is this here? I don't think it is needed (note to myself)
		return Menu.menu_type.DESSERT; // change later
	}
	
	
	public void list_all_items_with_id()
	{
		// string print all items with the relevant ids
		// include headers as well
		// TODO pretty string printing of all items with the id (id) - name - ....
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		//System.out.println("okok");
		//System.out.println(this.nr_items_on_menu);
		for (int i=0; i<this.nr_items_on_menu; i++){
			//System.out.println(this.menu[0]);
			if (this.menu[i].type != Menu.menu_type.PROMO_SET) {
				System.out.print("   " + this.menu[i].item_id);
				System.out.printf("%-70s", " ~ " + this.menu[i].name);
				System.out.printf("%-8s%n",
						new DecimalFormat("$###,##0.00").format(this.menu[i].price));
				System.out.print("   " + this.menu[i].type + "  ");
				System.out.printf("%-60s%n", "\"" + this.menu[i].description + "\"");
				System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
				System.out.println();
			}
		}
		//System.out.println("This should print all current menu items.");
	}
	
	public void list_all_promos_with_id()
	{
		// string print all promos with the relevant ids
		// include headers as well
		// TODO pretty string printing of all promos with the id (id) - name - ....
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		for (int i=0; i<this.nr_items_on_menu; i++){
			if (this.menu[i].type == Menu.menu_type.PROMO_SET) {
				System.out.print("   " + this.menu[i].item_id);
				System.out.printf("%-70s", " ~ " + this.menu[i].name);
				System.out.printf("%-8s%n",
						new DecimalFormat("$###,##0.00").format(this.menu[i].price));
				System.out.print("   " + this.menu[i].type + "  ");
				System.out.printf("%-60s%n", "\"" + this.menu[i].description + "\"");
				System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
				System.out.println();
			}
		}
		System.out.println("This should print all current promos.");
	}
	
	public void list_all_items_and_promos()
	{
		// string print everything according to type
		// include headers as well
		// TODO pretty string printing of all promos & items with the id (id) - name - ....
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		for (int i=0; i<this.nr_items_on_menu; i++){
			System.out.print("   " + this.menu[i].item_id);
			System.out.printf("%-70s", " ~ " + this.menu[i].name);
			System.out.printf("%-8s%n",
					new DecimalFormat("$###,##0.00").format(this.menu[i].price));
			System.out.print("   " + this.menu[i].type + "  ");
			System.out.printf("%-60s%n", "\"" + this.menu[i].description + "\"");
			System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
			System.out.println();
		}
		System.out.println("This should print all current menu items and promos.");
	}
	
	public void print_specific_item(int item_id)
	{
		int index = this._get_item_index(item_id);
		// print all relevant information for one item
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		System.out.print("   " + this.menu[index].item_id);
		System.out.printf("%-70s", " ~ " + this.menu[index].name);
		System.out.printf("%-8s%n",
				new DecimalFormat("$###,##0.00").format(this.menu[index].price));
		System.out.print("   " + this.menu[index].type + "  ");
		System.out.printf("%-60s%n", "\"" + this.menu[index].description + "\"");
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		System.out.println("This should print one specific item.");
	}
	
	public void print_specific_promo(int item_id)
	{
		int index = this._get_item_index(item_id);
		// print all relevant information for one promo
		System.out.print("   " + this.menu[index].item_id);
		System.out.printf("%-70s", " ~ " + this.menu[index].name);
		System.out.printf("%-8s%n",
				new DecimalFormat("$###,##0.00").format(this.menu[index].price));
		System.out.print("   " + this.menu[index].type + "  ");
		System.out.printf("%-60s%n", "\"" + this.menu[index].description + "\"");
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
		System.out.println();
		System.out.println("This should print one specific promo.");
	}
	
	private int _get_next_item_id()
	{
		this.local_item_id=0;
		for (int i=0; i<this.menu.length; i++)
		{
			if (this.menu[i] == null) break;
			if (this.menu[i].item_id > this.local_item_id) this.local_item_id=this.menu[i].item_id;
		}
		return this.local_item_id+1;
	}
	
	private boolean _does_item_exist(int item_id)
	{
		for (int i=0; i<this.menu.length; i++)
			if (this.menu[i]!=null && this.menu[i].get_item_id() == item_id)
				return true;
		return false;
	}
	private int _get_item_index(int item_id)
	{
		for (int i=0; i<this.menu.length; i++) if (this.menu[i].get_item_id() == item_id) return i;
		return -1;
	}
	
	private Menu _return_item_by_id(int item_id)
	{
		// this function is only called after _does_item_exist, so no need to cover the non-existant case
		for (int i=0; i<this.menu.length; i++) if (this.menu[i].get_item_id() == item_id) return this.menu[i];
		return this.menu[0]; // this is a dummy to prevent the syntax error
	}
	
	private void _re_sort_items()
	{
		// iterate over all items on the Menu, re-index them and adjust them based on their type
		int local_id=1;
		for (Menu.menu_type temp: Menu.menu_type.values())
		{
			for (int i=0; i<this.menu.length; i++)
			{
				// first of all, re-index them based on enum, in the second loop, re-arrange them by index (using a basic bubble sort algorithm
				if (this.menu[i]!=null && this.menu[i].get_menu_type() == temp)
				{
					// change the id of this item
					this.menu[i].set_item_id(local_id);
					local_id++;
				}
			}
		}
		// now sort the items in the list "menu" by their ids
		for (int i=0; i<this.menu.length; i++)
		{
			for (int j=0; j<this.menu.length-1; j++)
			{
				if (this.menu[j+1]==null) continue;
				if (this.menu[j]==null || this.menu[j].get_item_id() > this.menu[j+1].get_item_id())
				{
					// swap items at index j+1 & j
					Menu temp_item = this.menu[j];
					this.menu[j] = this.menu[j+1];
					this.menu[j+1] = temp_item;
				}
			}
		}
	}
}

















