package model;

public class AdminModel {
	private int employee_number;
	private String employee_name;
	private String phone;
	private int password;
	private String employment;
	private String question;
	private String answer;
	private String admin;
	
	public AdminModel(int employee_number, String employee_name, String phone, int password, String employment,
			String question, String answer, String admin) {
		super();
		this.employee_number = employee_number;
		this.employee_name = employee_name;
		this.phone = phone;
		this.password = password;
		this.employment = employment;
		this.question = question;
		this.answer = answer;
		this.admin = admin;
	}
	
	public int getEmployee_number() {
		return employee_number;
	}
	public void setEmployee_number(int employee_number) {
		this.employee_number = employee_number;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
}
