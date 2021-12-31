package cz2002_project;
import cz2002_project.Menu;
import cz2002_project.Menu_item;

public class Promo_set extends Menu{
	public Menu[] items;
	public int nr_item=0;
	public int max_nr_items=10;
	
	public Promo_set(String name, float price, String description, int item_id) {
		super(name, price, Menu.menu_type.PROMO_SET, description, item_id);
		
		// max promo items is 10 (for now)
		this.items = new Menu[this.max_nr_items];
	}
	
	public void add_promo_item(Menu new_item)
	{
		this.items[this.nr_item] = new_item;
		this.nr_item++;
	}
	
	public void list_all_items()
	{
		// iterate over the list of items and print them with the relevant index
		for (int i=0; i<this.nr_item; i++)
		{
			System.out.printf("Index: %d %t Item_name: %s%n", i, this.items[i].name);
		}
	}
	
	public void remove_promo_item(int item_id)
	{
		// remove the item with item_id from the list 
		for (int i=0; i<this.max_nr_items; i++)
		{
			if (this.items[i].item_id == item_id)
			{
				
				// in case this is the last item 
				this.items[i] = null;
				this.nr_item--;
				
				// left-shift everything and break
				for (int ii=i; ii<this.max_nr_items-1; ii++)
				{
					this.items[ii] = this.items[ii+1];
				}
				break;
			}
		}
	}
	
	

}
