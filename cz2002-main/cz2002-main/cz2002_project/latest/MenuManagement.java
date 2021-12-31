import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * MenuManagement class that implements the interface from OPInterface
 */
public class MenuManagement implements OPInterface {
    private Scanner sc = new Scanner(System.in);
    private ArrayList<MenuItem> Menu = new ArrayList<MenuItem>();
    private ArrayList<PromoItem> PromoMenu = new ArrayList<PromoItem>();
    private int globalIndex = 1;

    /**
     * Constructor for MenuManagement
     */
    public MenuManagement() {
        if (!read()) {
            System.out.println("Corrupted file.");
        }
    }

    /**
     * @return true if read from Menu.txt and Promo.txt successfully, false otherwise
     */
    public boolean read() {
        Path path = Paths.get("Menu.txt");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(",");
                MenuItem.ItemType e = MenuItem.ItemType.valueOf(tokens[3]);
                MenuItem menuItem = new MenuItem(Integer.parseInt(tokens[0]), tokens[1], tokens[2], e, Float.parseFloat(tokens[4]));
                globalIndex++;
                this.Menu.add(menuItem);
                line = br.readLine();
            }

        } catch (IOException e) {
            return false;
        }

        path = Paths.get("Promo.txt");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(",");
                int n = tokens.length;
                int j = 5;

                MenuItem.ItemType e = MenuItem.ItemType.valueOf(tokens[3]);
                ArrayList<Integer> itemIDs = new ArrayList<>();

                while (j < n) {
                    itemIDs.add(Integer.parseInt(tokens[j]));
                    j++;
                }

                PromoItem promoItem = new PromoItem(Integer.parseInt(tokens[0]), tokens[1], tokens[2], e, Float.parseFloat(tokens[4]), itemIDs);
                globalIndex++;
                this.PromoMenu.add(promoItem);

                line = br.readLine();
            }

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * @return true after adding a new Menu item or Promo set into data file, false otherwise
     * @throws IOException
     */
    public boolean add() throws IOException {
        System.out.println("Would you like to add a: ");
        System.out.println("(1) - Menu Item ");
        System.out.println("(2) - Promo Set ");
        int input = sc.nextInt();
        if (input == 1) {
            System.out.println("Name of Menu Item you would like to add: ");
            String name = sc.next();
            System.out.println("Price of Menu Item you would like to add: ");
            float price = sc.nextFloat();
            System.out.println("Type of Menu Item you would like to add: ");
            MenuItem.list_enum_types();
            int enum_int = sc.nextInt();
            System.out.println("Description of Menu Item you would like to add: ");
            sc.nextLine();
            String description = sc.nextLine();

            for (int i = 0; i < Menu.size(); i++) {
                if (this.Menu.get(i).getName().equals(name) && this.Menu.get(i).type == MenuItem.ItemType.values()[enum_int]) {
                    System.out.println("Item is already on the menu.");
                    return false;
                }
            }

            this.Menu.add(new MenuItem(globalIndex, name, description, MenuItem.ItemType.values()[enum_int], price));
            globalIndex++;
        } else {
            System.out.println("Name of Promo Set you would like to add: ");
            sc.nextLine();
            String name = sc.nextLine();
            for (int i = 0; i < PromoMenu.size(); i++) {
                if (this.PromoMenu.get(i).getName().equals(name)) {
                    System.out.println("Promo set is already on the menu.");
                    return false;
                }
            }

            System.out.println("Price of Promo Set you would like to add: ");
            float price = sc.nextFloat();
            System.out.println("Description of Promo Set you would like to add: ");
            String description = sc.next();

            ArrayList<Integer> arr = new ArrayList<>();
            System.out.println("How many items would you like to add into Promo Set?");
            int num = sc.nextInt();
            System.out.println("Please enter the item ids: ");
            for (int j = 0; j < num; j++) {
                int id = sc.nextInt();
                arr.add(id);
            }

            this.PromoMenu.add(new PromoItem(globalIndex, name, description, MenuItem.ItemType.values()[4], price, arr));
            globalIndex++;
        }

        // sort the items by their enum so that they are in the correct order when being printed
        // this.sort();
        write();
        return true;
    }


    /**
     * @param id Menu Item or Promo set id to be removed from Menu or PromoMenu
     * @return true after removing menu item or promo set from data file, false otherwise
     * @throws IOException
     */
    public boolean remove(int id) throws IOException {
        // remove an order from the current order list
        boolean changed = false;
        for (MenuItem menuItem : Menu) {
            if (menuItem.getMenuIndex() == id) {
                Menu.remove(id - PromoMenu.size() - 1);
                changed = true;
                break;
            }
        }
        int i = 0;
        for (PromoItem promoItem : PromoMenu) {
            if (promoItem.getMenuIndex() == id) {
                PromoMenu.remove(i);
                changed = true;
                break;
            }
            i++;
        }

        // also removes it from file (just override with the current list)
        write();
        return changed;
    }

    /**
     * @param id Menu Item id to be edited
     * @return true if Menu item's details changed successfully, false otherwise
     * @throws IOException
     */
    public boolean edit(int id) throws IOException {
        boolean changed = false;
        MenuItem menuItem = Menu.get(id - PromoMenu.size() - 1);

        System.out.println("Which aspect of the above menu item would you like to edit?");
        System.out.println("\t(1) - Name");
        System.out.println("\t(2) - Price");
        System.out.println("\t(3) - Type");
        System.out.println("\t(4) - Description");
        System.out.println("\t(5) - Nothing (return)");
        int input = sc.nextInt();
        sc.nextLine();
        switch (input) {
            case 1:
                System.out.println("New name: ");
                String name = sc.next();
                menuItem.setName(name);
                Menu.set(id - PromoMenu.size() - 1, menuItem);
                changed = true;
                break;
            case 2:
                System.out.println("New Price: ");
                float price = sc.nextFloat();
                menuItem.setPrice(price);
                Menu.set(id - PromoMenu.size() - 1, menuItem);
                changed = true;
                break;
            case 3:
                System.out.println("New Type: ");
                MenuItem.list_enum_types();
                int enum_int = sc.nextInt();
                menuItem.set_menu_type(enum_int);
                Menu.set(id - PromoMenu.size() - 1, menuItem);
                changed = true;
                break;
            case 4:
                System.out.println("New Description: ");
                String description = sc.nextLine();
                menuItem.setDescription(description);
                Menu.set(id - PromoMenu.size() - 1, menuItem);
                changed = true;
                break;
            case 5:
                System.out.println("Nothing changed.");
                break;
            default:
                System.out.println("Invalid input!");
                break;
        }
        write();
        return changed;
    }

    /**
     * display all menu items and each of their details
     */
    public void display() {
        System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
        System.out.println();
        for (MenuItem menuItem : Menu) {
            System.out.print("   " + menuItem.getMenuIndex());
            System.out.printf("%-70s", " ~ " + menuItem.getName());
            System.out.printf("%-8s%n",
                    new DecimalFormat("$###,##0.00").format(menuItem.getPrice()));
            System.out.print("   " + menuItem.get_menu_type() + "  ");
            System.out.printf("%-60s%n", "\"" + menuItem.getDescription() + "\"");
            System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
            System.out.println();
        }
        for (PromoItem promoItem : PromoMenu) {
            System.out.print("   " + promoItem.getMenuIndex());
            System.out.printf("%-70s", " ~ " + promoItem.getName());
            System.out.printf("%-8s%n",
                    new DecimalFormat("$###,##0.00").format(promoItem.getPrice()));
            System.out.print("   " + promoItem.get_menu_type() + "  ");
            String list_of_names = "";
            for (int i = 0; i < promoItem.getItemIDs().size(); i++) {
                String item = Menu.get(promoItem.getItemIDs().get(i) - 1).getName();
                list_of_names += item;
                if (i != promoItem.getItemIDs().size() - 1) {
                    list_of_names += ", ";
                }
            }
            System.out.printf("%-60s%n", "\"" + list_of_names + "\"");
            System.out.println("\n" + new String(new char[87]).replace("\0", "-"));
            System.out.println();
        }
    }

    /**
     * Sorting Menu Items based on Enum Item Types
     */
    public void sort() {
        int local_id = 1;
        for (MenuItem.ItemType temp : MenuItem.ItemType.values()) {
            for (int i = 0; i < this.Menu.size(); i++) {
                // first of all, re-index them based on enum, in the second loop, re-arrange them by index (using a basic bubble sort algorithm
                if (this.Menu.get(i) != null && this.Menu.get(i).get_menu_type() == temp) {
                    // change the id of this item
                    this.Menu.get(i).setMenuIndex(local_id);
                    local_id++;
                }
            }
        }
        // now sort the items in the list "menu" by their ids
        for (int i = 0; i < this.Menu.size(); i++) {
            for (int j = 0; j < this.Menu.size() - 1; j++) {
                if (this.Menu.get(j + 1) == null) continue;
                if (this.Menu.get(j) == null || this.Menu.get(j).getMenuIndex() > this.Menu.get(j + 1).getMenuIndex()) {
                    // swap items at index j+1 & j
                    MenuItem temp_item = this.Menu.get(j);
                    this.Menu.set(j, this.Menu.get(j + 1));
                    this.Menu.set(j + 1, temp_item);
                }
            }
        }
    }

    /**
     * @param id Menu Item id to be added from Promo Set
     * @return true after adding item to Promo set successfully, false otherwise
     * @throws IOException
     */
    public boolean addItemToPromo(int id) throws IOException {
        if (!checkPromoSet(id)) {
            return false;
        }
        int i = 0;
        for (PromoItem promoItem : PromoMenu) {
            if (promoItem.getMenuIndex() == id) {
                break;
            }
            i++;
        }
        System.out.println("How many items would you like to add?");
        int num = sc.nextInt();
        ArrayList<Integer> arr = PromoMenu.get(i).additem(num);
        PromoMenu.get(i).setItemIDs(arr);
        write();
        return true;
    }

    /**
     * @param id Menu Item id to be removed from Promo Set
     * @return true after removing item from Promo set successfully, false otherwise
     * @throws IOException
     */
    public boolean removeItemFromPromo(int id) throws IOException {
        if (!checkPromoSet(id)) {
            return false;
        }
        int j = 0;
        for (PromoItem promoItem : PromoMenu) {
            if (promoItem.getMenuIndex() == id) {
                break;
            }
            j++;
        }
        System.out.println("These are the items in this Promo Set: ");
        for (int i = 0; i < PromoMenu.get(j).getItemIDs().size(); i++) {
            String names = "";
            names += String.valueOf(i);
            names += ". ";
            String item = Menu.get(PromoMenu.get(j).getItemIDs().get(i) - 1).getName();
            names += item;
            System.out.println(names);
        }
        ArrayList<Integer> arr = PromoMenu.get(j).getItemIDs();
        System.out.println("How many items would you like to remove?");
        int num = sc.nextInt();
        for (int i = 0; i < num; i++) {
            System.out.println("Please select which item you would like to remove: ");
            int choice = sc.nextInt();
            arr.remove(choice);
        }
        PromoMenu.get(j).setItemIDs(arr);
        write();
        return true;
    }

    /**
     * @param id Promo Set id to be selected
     * @return true if Promo Set is part of PromoMenu, false otherwise
     */
    public boolean checkPromoSet(int id) {
        // check if unique id is part of promo csv
        for (PromoItem promoItem : PromoMenu) {
            if (promoItem.getMenuIndex() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param id Menu Item id to be selected
     * @return MenuItem and the details of the Menu item chosen
     */
    public MenuItem get(int id) {
        return Menu.get(id - 1);
    }

    public int getMenuSize() {
        return this.Menu.size();
    }

    public void displayInterface() throws IOException {

        int id;
        while (true) {
            System.out.println("\t(1) - CREATE a new Menu item");
            System.out.println("\t(2) - UPDATE a current Menu item");
            System.out.println("\t(3) - DELETE a current Menu item");
            System.out.println("\t(4) - ADD a new Promo item to existing Promo set");
            System.out.println("\t(5) - REMOVE a Promo item from Promo set");
            System.out.println("\t(6) - DISPLAY menu");
            System.out.println("\t(7) - Quit");

            int input = sc.nextInt();
            switch (input) {
                case 1:
                    if (add()) {
                        System.out.println("Added successfully!");
                    } else {
                        System.out.println("Not added!");
                    }
                    break;

                case 2:
                    System.out.println("Please select an item you would like to edit: ");
                    id = sc.nextInt();
                    if (edit(id)) {
                        System.out.println("Changes made successfully!");
                    } else {
                        System.out.println("Changes were not made.");
                    }
                    break;

                case 3:
                    System.out.println("Please select an item id to delete: ");
                    id = sc.nextInt();
                    if (remove(id)) {
                        System.out.println("Deleted successfully!");
                    } else {
                        System.out.println("Item not deleted.");
                    }
                    break;

                case 4:
                    System.out.println("Please enter the promo set id: ");
                    id = sc.nextInt();
                    if (addItemToPromo(id)) {
                        System.out.println("Added item successfully to promo set!");
                    } else {
                        System.out.println("Item not added.");
                    }
                    break;

                case 5:
                    System.out.println("Please enter the promo set id: ");
                    id = sc.nextInt();
                    if (removeItemFromPromo(id)) {
                        System.out.println("Item successfully removed from promo set!");
                    } else {
                        System.out.println("Item not removed.");
                    }
                    break;

                case 6:
                    display();
                    break;

                case 7:
                    return;

                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }

    }

    /**
     * writes to Menu.txt and Promo.txt file
     * @throws IOException
     */
    public void write() throws IOException {
        Path path = Paths.get("Menu.txt");
        Path path1 = Paths.get("Promo.txt");
        FileWriter fw = new FileWriter(String.valueOf(path));
        FileWriter fw1 = new FileWriter(String.valueOf(path1));

        for (MenuItem menuItem : this.Menu) {
            try {
                fw.write(menuItem.getMenuIndex() + ",");
                fw.write(menuItem.getName() + ",");
                fw.write(menuItem.getDescription() + ",");
                fw.write(menuItem.get_menu_type() + ",");
                fw.write(menuItem.getPrice() + ",");
                fw.write(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (PromoItem promoItem : this.PromoMenu) {
            try {
                fw1.write(promoItem.getMenuIndex() + ",");
                fw1.write(promoItem.getName() + ",");
                fw1.write(promoItem.getDescription() + ",");
                fw1.write(promoItem.get_menu_type() + ",");
                fw1.write(promoItem.getPrice() + ",");
                for (int i = 0; i < promoItem.getItemIDs().size(); i++) {
                    fw1.write(promoItem.getItemIDs().get(i) + ",");
                }
                fw1.write(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fw.close();
            fw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
