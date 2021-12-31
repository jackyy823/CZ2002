/**
 * Represents a Menu Item in the Menu
 * @author
 * @version 1.0
 * @since 2021-11-13
 */
public class MenuItem {
    /**
     * Enum of Type of item in menu
     */
    public enum ItemType {
        MAIN,
        DESSERT,
        STARTER,
        DRINK,
        PROMO
    }

    /**
     * The price of menu item
     */
    private float price;
    /**
     * The description of menu item
     */
    private String description;
    /**
     * The name of menu item
     */
    private String name;
    /**
     * The index of menu item
     */
    private int menuIndex;
    /**
     * The type of menu item
     */
    public ItemType type = ItemType.MAIN;

    /**
     * Constructor of a menu item with the given index, name, description, item type and price
     * @param index
     * @param name
     * @param description
     * @param e
     * @param price
     */
    public MenuItem(int index, String name, String description, ItemType e, float price) {
        this.name = name;
        this.price = price;
        this.type = e;
        this.description = description;
        this.menuIndex = index;
    }

    /**
     * Get the price of menu item
     * @return the price of menu item
     */
    public float getPrice() {
        return this.price;
    }

    /**
     * Changes the price of menu item
     * @param price This is the new price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Get the description of menu item
     * @return the description of menu item
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Changes the description of menu item
     * @param description This is the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the name of menu item
     * @return the name of menu item
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes the name of menu item
     * @param name This is the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the index of menu item
     * @return index of item on menu
     */
    public int getMenuIndex() {
        return this.menuIndex;
    }

    /**
     * Changes the index of menu item
     * @param menuIndex This is the new index
     */
    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }


    /**
     * Get the type of menu item
     * @return type of item on menu
     */
    public ItemType getType() {
        return this.type;
    }

    /**
     * Get the type of menu item
     * @return type of item on menu
     */
    public ItemType get_menu_type() {
        return this.type;
    }

    /**
     * Changes the type of menu item
     * @param enum_int
     * Uses the enum_int to get type of menu item
     */
    public void set_menu_type(int enum_int) {
        this.type = MenuItem.ItemType.values()[enum_int];
    }

    /**
     * Printing all the types of item in menu
     */
    public static void list_enum_types() {
        //System.out.println("trying to print enums");
        // actually print the string (1) - ENUM...
        int temp_int = 0;
        for (MenuItem.ItemType temp : MenuItem.ItemType.values()) {
            System.out.printf("\t(%d) - %s \n", temp_int, temp.toString());
            temp_int++;
        }
    }

}
