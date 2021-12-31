package cz2002_project;
import cz2002_project.Staff;

public class StaffManagement {
	public int nr_employees=0;
	private int max_nr_employees = 250;
	private Staff[] staff_list;
	
	public StaffManagement()
	{
		// load all current employees from file 
		// TODO
		this.staff_list = new Staff[this.max_nr_employees];
		// dummy, remove once loading from file
		this.staff_list[0] = new Staff(1, "Leon", "Male", "Boss");
		this.nr_employees++;
	}
	
	public void list_all_employees()
	{
		// TODO beautify?
		for (int i=0; i<nr_employees; i++)
		{
			if (this.staff_list[i] != null)
			{
				System.out.printf("(%d) - %s %n", this.staff_list[i].staff_id, this.staff_list[i].get_name());
			}
		}
	}
	
	
	public boolean check_if_staff_id_exists(int staff_id_to_check)
	{
		for (int i=0; i<nr_employees; i++)
		{
			if (this.staff_list[i] != null && this.staff_list[i].staff_id==staff_id_to_check)
			{
				return true;
			}
		}
		return false;
	}
	
	public String return_staff_name_by_id(int staff_id)
	{
		for (int i=0; i<nr_employees; i++)
		{
			if (this.staff_list[i] != null && this.staff_list[i].staff_id==staff_id)
			{
				return this.staff_list[i].get_name();
			}
		}
		return ""; // should only be called after checked whether the id exists
	}
	
	public void print_employee_by_id(int staff_id)
	{
		for (int i=0; i<nr_employees; i++)
		{
			//System.out.println(staff_id);
			if (this.staff_list[i] != null && this.staff_list[i].staff_id==staff_id)
			{
				// print all employee information
				System.out.printf("%n(%d) - %s %s %s%n", 
						this.staff_list[i].staff_id, this.staff_list[i].get_title(), 
						this.staff_list[i].get_gender(), this.staff_list[i].get_name());
			}
		}
	}
	
	public boolean delete_emplyee(int staff_id)
	{
		// check if staff_id exists
		if (this._check_if_staff_id_exists(staff_id)==false)
		{
			System.out.println("The entered staff_id does not exist.");
			return false;
		}
		
		// delete employee from the list and left-shift everybody else 
		for (int i=0; i<max_nr_employees; i++)
		{
			if (this.staff_list[i]!=null && this.staff_list[i].staff_id==staff_id)
			{
				//System.out.println(staff_id);
				//System.out.println()
				// found the employee to delete
				this.staff_list[i] = null; // in case this was the very last element
				/*for (int j=i; j<max_nr_employees-1; j++)
				{
					this.staff_list[j] = this.staff_list[j+1];
				}*/
				return true;
			}
		}
		System.out.println("The entered staff_id does not exist.");
		return false;
	}
	
	public boolean add_new_employee(String name, String gender, String job_title)
	{
		// check the input types etc
		// TODO
		
		int staff_id = this._get_next_available_id();
		if (staff_id==-1)
		{
			// this means that employee roster is full
			System.out.println("The maximum number of employees allowed by the system is reached. Please contact your system admin.");
			return false;
		}
		
		for (int i=0; i<max_nr_employees; i++)
		{
			if (this.staff_list[i]==null)
				{
					this.staff_list[i] = new Staff(staff_id, name, gender, job_title);
					break;
				}
		}
		this.nr_employees++;
		return true;
		
	}
	
	public boolean change_employee_attribute(int staff_id, String new_input, String aspect)
	{
		for (int i=0; i<max_nr_employees; i++)
		{
			if (this.staff_list[i]!=null && this.staff_list[i].staff_id==staff_id) 
			{
				if (aspect=="name") this.staff_list[i].set_name(new_input);
				if (aspect=="gender") this.staff_list[i].set_gender(new_input);
				if (aspect=="job title") this.staff_list[i].set_job_title(new_input);
				return true;
			}
		}
		System.out.println("Staff if was now found."); 
		return false;
	}
	
	private int _get_next_available_id()
	{
		// check for the next available employee id and return it 
		int new_id = -1;
		for (int i=0; i<max_nr_employees; i++)
		{
			if (this.staff_list[i] != null && this.staff_list[i].staff_id > new_id) new_id=this.staff_list[i].staff_id;
		}
		if (new_id!=-1) return new_id+1;
		return new_id;
	}
	private boolean _check_if_staff_id_exists(int staff_id)
	{
		for (int i=0; i<max_nr_employees; i++)
		{
			if (this.staff_list[i]!=null && this.staff_list[i].staff_id==staff_id) return true;
		}
		return false;
	}
}
