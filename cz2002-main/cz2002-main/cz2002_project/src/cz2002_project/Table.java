package cz2002_project;

/**
 * Table class that contains table id and each table's capacity
 */
public class Table {
	public static int nr_of_tables=0;
	public int table_id;
	public int capacity;

	/**
	 * @param capacity
	 * max number of pax per table
	 * @param table_id
	 * id of table
	 */
	public Table(int capacity, int table_id)
	{
		this.capacity=capacity;
		this.table_id=table_id;
		Table.nr_of_tables++;
	}
}
