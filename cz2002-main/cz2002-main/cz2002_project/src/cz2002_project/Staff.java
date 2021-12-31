package cz2002_project;

/**
 * Staff details such as id, gender, and job title
 */
public class Staff {
	public int staff_id;
	private String name;
	private String gender;
	private String job_title;

	/**
	 * @param staff_id id of staff
	 * @param gender gender of staff
	 * @param job_title job title of staff
	 */
	public Staff(int staff_id, String name, String gender, String job_title)
	{
		this.staff_id=staff_id;
		this.name = name;
		this.gender=gender;
		this.job_title=job_title;
	}
	
	public String get_name()
	{
		return this.name;
	}
	public void set_name(String new_name)
	{
		this.name=new_name;
	}
	
	public String get_gender()
	{
		return this.gender;
	}
	public void set_gender(String new_gender)
	{
		this.gender=new_gender;
	}
	
	public String get_title()
	{
		return this.job_title;
	}
	public void set_job_title(String new_job_title)
	{
		this.job_title = new_job_title;
	}

}
