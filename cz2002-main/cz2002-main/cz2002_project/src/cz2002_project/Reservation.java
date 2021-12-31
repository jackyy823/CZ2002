package cz2002_project;


public class Reservation {
	public int reservation_id;
	public int nr_pax;
	public int table_id;
	public String name;
	public String contact_nr;
	public boolean is_member;
	public int start_ts;
	public int end_ts;
	
	// Declaration of Constructor Class
	public Reservation(int reservation_id, int nr_pax, String name, String contact_nr, int table_id, boolean is_member, int start_ts, int end_ts)
	{
		this.reservation_id=reservation_id;
		this.nr_pax=nr_pax;
		this.name=name;
		this.contact_nr=contact_nr;
		this.is_member=is_member;
		this.table_id=table_id;
		this.start_ts=start_ts;
		this.end_ts=end_ts;
	}
}
