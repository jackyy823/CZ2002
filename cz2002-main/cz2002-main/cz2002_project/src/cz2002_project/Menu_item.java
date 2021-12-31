package cz2002_project;
import cz2002_project.Menu;

public class Menu_item extends Menu{

	public Menu_item(String name, float price, int enum_int, String description, int item_id) {
		// convert enum_int to actual enum
		super(name, price, Menu.menu_type.values()[enum_int], description, item_id);
	}

}
