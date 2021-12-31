package cz2002_project;

public class Menu {
	public enum menu_type
	{
		STARTER,
		MAIN,
		DESSERT,
		DRINK,
		PROMO_SET
	}
	
	public float price=0;
	public String description="";
	public String name;
	public menu_type type=menu_type.MAIN;
	public int item_id;
	
	
	// Constructor declaration method
	public Menu(String name, float price, menu_type type, String description, int item_id)
	{
		this.name = name;
		this.price = price;
		this.type = type;
		this.description = description;
		this.item_id = item_id;
	}
	
	public void add_promo_item(Menu item)
	{
		// dummy, not actually used but necessary for the Promo_set class
	}
	public void remove_promo_item(int id)
	{
		// dummy, not actually used but necessary for the Promo_set class
	}
	
	public void set_name(String name)
	{
		this.name=name;
	}

	public int get_item_id() { return this.item_id; }

	public String get_name()
	{
		return this.name;
	}
	
	public float get_price()
	{
		return this.price;
	}
	
	public void set_price(float price)
	{
		this.price=price; 
	}
	
	public String get_description()
	{
		return this.description;
	}
	
	public void set_description(String description)
	{
		this.description=description;
	}

	public void set_item_id (int item_id) { this.item_id = item_id; }

	public menu_type getType() {
		return this.type;
	}
	
	public menu_type get_menu_type()
	{
		return this.type;
	}
	
	}
