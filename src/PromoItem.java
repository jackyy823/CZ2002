import java.util.ArrayList;
import java.util.Scanner;

/**
 * Promo item that extends attributes from menu item <br>
 * Contains a arrayList of menu item ID
 */
public class PromoItem extends MenuItem {
	ArrayList<Integer> itemIDs;
	Scanner sc = new Scanner(System.in);

	/**
	 * @param menuIndex ID of Promo Set
	 * @param name Name of Promo Set
	 * @param description Description of Promo Set
	 * @param e Promo type
	 * @param price Price of the Promo Set
	 * @param itemIDs ArrayList of Menu item ID that are included in this Promo Set
	 */
	public PromoItem(int menuIndex, String name, String description, ItemType e, float price, ArrayList<Integer> itemIDs) {
		super(menuIndex, name, description, e, price);
		this.itemIDs = itemIDs;
	}

	/**
	 * @param num Add Menu item ids into the ArrayList
	 */
	public ArrayList<Integer> additem(int num) {
		System.out.println("Please enter the ids of the items you would like to add:");
		for (int i=0; i<num; i++) {
			int id = sc.nextInt();
			itemIDs.add(id);
		}
		return itemIDs;
	}

	/**
	 * @param num Remove Menu item ids from the ArrayList
	 */
	public ArrayList<Integer> removeitem(int num) {
		System.out.println("Please enter the ids of the items you would like to remove:");
		for (int i=0; i<num; i++) {
			int id = sc.nextInt();
			itemIDs.remove(id);
		}
		return itemIDs;
	}

	/**
	 * @param itemIDs Set ArrayList of menu item ID
	 */
	public void setItemIDs(ArrayList<Integer> itemIDs) {
		this.itemIDs = itemIDs;
	}

	/**
	 * @return ArrayList of menu item ID
	 */
	public ArrayList<Integer> getItemIDs() {
		return this.itemIDs;
	}
}