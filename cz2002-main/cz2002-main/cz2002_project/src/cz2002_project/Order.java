package cz2002_project;

public class Order {
	public int order_id;
	public int staff_id;
	public int reservation_id;
	public int[] item_ids;
	
	
	// Constructor class Declaration
	public Order(int order_id, int staff_id, int reservation_id, int[] input_item_ids)
	{
		this.order_id = order_id;
		this.staff_id = staff_id;
		this.reservation_id = reservation_id;
		this.item_ids = new int[input_item_ids.length + 25]; // incase more orders are added
		for (int i=0; i<this.item_ids.length; i++) this.item_ids[i]=-1;
		for (int i=0; i<input_item_ids.length; i++) this.item_ids[i]=input_item_ids[i];
		/*
		for (int i=0; i<input_item_ids.length; i++) System.out.println(input_item_ids[i]);
		for (int i=0; i<this.item_ids.length; i++)
		{
			if (i < input_item_ids.length)
				this.item_ids[i] = input_item_ids[i];
			else this.item_ids[1] = -1; // value to indicate an empty slot (incase menu items have index 0)
				
		}*/
	}
	
	public boolean add_item(int item_id)
	{
		for (int i=0; i<this.item_ids.length; i++)
		{
			if (this.item_ids[i] == -1)
			{
				this.item_ids[i] = item_id;
				return true;
			}
		}
		System.out.println("Too many items are already in the current order.");
		return false;
	}
	
	public boolean remove_item(int item_id)
	{
		for (int i=0; i<this.item_ids.length; i++)
		{
			if (this.item_ids[i] == item_id)
			{
				this.item_ids[i] = -1;
				return true;
			}
		}
		return false;
	}

	public int getOrder_id() {
		return order_id;
	}

	public int getStaff_id() {
		return staff_id;
	}

	public int getReservation_id() {
		return reservation_id;
	}

	public int[] getItem_ids() {
		return item_ids;
	}
}
