import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


/**
 * Order class with attributes such as orderID, staffID, tableID, itemList, date, hour, membership, paid and cost
 */
public class Order {

	private int orderID;
	private int staffID;
	private int tableID;
	private ArrayList<Integer> itemList;
	private Date date;
	private int hour;
	private boolean membership;
	private boolean paid;
	private float cost;
	private static Scanner sc = new Scanner(System.in);

	/**
	 * @param order is the orders ID
	 * @param staff is the staffs ID
	 * @param table is the tables ID
	 * @param orderdate is the date of the order
	 * @param orderhour is the hour of the order
	 * @member indicates if it is ordered by a member
	 * @payment indicates whether the order has already been paid for
	 */
	public Order(int order, int staff, int table, Date orderdate, int orderhour, boolean member, boolean payment){
		orderID = order;
		staffID = staff;
		tableID = table;
		date = orderdate;
		hour = orderhour;
		membership = member;
		paid = payment;
		itemList = new ArrayList<Integer>();
	}

	/**
	 * @return whether the order is by a member
	 */
	public boolean getMembership(){
		return this.membership;
	}

	/**
	 * @param member update the membership status of the order
	 */
	public void setMembership(boolean member){
		this.membership = member;
	}

	/**
	 * @param setPaid update the payment status of the order
	 */
	public void setPaid(boolean pay){
		this.paid = pay;
	}

	/**
	 * @return whether the order has been paid for
	 */
	public boolean getPaid(){
		return this.paid;
	}

	/**
	 * @return the date of the order
	 */
	public Date getDate(){
		return this.date;
	}

	/**
	 * @return the time of the order
	 */
	public int getTime(){
		return this.hour;
	}

	/**
	 * @return the total price of the order
	 */
	public float getPrice(){
		return this.cost;
	}

	/**
	 * @return the order ID
	 */
	public int getOrderID() {
		return this.orderID;
	}

	/**
	 * @param orderID update the orderID
	 */
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the staff ID
	 */
	public int getStaffID() {
		return this.staffID;
	}

	/**
	 * @param staffID update the staffID
	 */
	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	/**
	 * @return the table ID
	 */
	public int getTableID() {
		return this.tableID;
	}

	/**
	 * @param set the table id
	 */
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	/**
	 * @return the item list
	 */
	public ArrayList<Integer> getItemList() {
		return itemList;
	}

	/**
	* Print the orders information
	*/
	public void printOrder(){
		System.out.print("   Order ID: " + getOrderID());
		System.out.printf("   Staff ID: " + getStaffID());
		System.out.println("   Table ID: " + getTableID());
		System.out.print(("   Membership: "+getMembership()));
		System.out.println(("   Paid: "+getPaid()));
		System.out.print("   Order Item ID: ");
		for (int j=0; j<itemList.size(); j++) {
			System.out.print(itemList.get(j)+", ");
		}
		System.out.println();
		System.out.println();
	}

	/**
	 * @param MenuMgrf object for looking up the entered IDs
	 * @return true if the items were added successfully
	 */
	public boolean addItems(MenuManagement MenuMgr) {
		int item_id = 0;

		while(true){
			System.out.println("Please enter the ID of the item to be added: (-1 to exit)");
			item_id = sc.nextInt();
			if(item_id == -1){
				return true;
			}
			else if((item_id < 1 || item_id >= MenuMgr.getMenuSize()+1)){
				System.out.println("Please enter a valid item id.");
				return false;
			}
			else{
				itemList.add(item_id);
			}
		}
	}

	/**
	 * @param aList
	 * @return true if the items were added successfully
	 */
	public boolean addItems(ArrayList<Integer> aList) {
		for(Integer item: aList){
			itemList.add(item);
		}
		return true;
	}

	/**
	 * @param MenuMgrf object for looking up the entered IDs
	 * @return true if the items were removed successfully
	 */
	public boolean removeItems(MenuManagement MenuMgr) {
		System.out.println("Please enter the number of items to be removed");
		int num_items = sc.nextInt();
		int item_id = -1;

		for(int i=0; i<num_items; i++){
			System.out.println("Please enter the ID of "+i+" item to be removed");
			item_id = sc.nextInt();
			if(itemList.size()<=0){
				System.out.println("There are no items on the order.");
				return false;
			}
			if(item_id < 1 || item_id >= MenuMgr.getMenuSize()+1){
				System.out.println("Please enter a valid item id.");
				i--;
			}
			else{
				itemList.add(item_id);
			}
		}
		return true;
	}
}
