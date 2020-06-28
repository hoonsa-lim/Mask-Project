package model;

public class CompanyModel {
	private String company_number;
	private String company_name;
	private String manager;
	private String manager_phone;
	private String contract;
	private String address;
	private String production_consumption;
	
	public CompanyModel(String company_number, String company_name, String manager, String manager_phone,
			String contract, String address, String production_consumption) {
		super();
		this.company_number = company_number;
		this.company_name = company_name;
		this.manager = manager;
		this.manager_phone = manager_phone;
		this.contract = contract;
		this.address = address;
		this.production_consumption = production_consumption;
	}

	public String getCompany_number() {
		return company_number;
	}

	public void setCompany_number(String company_number) {
		this.company_number = company_number;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager_phone() {
		return manager_phone;
	}

	public void setManager_phone(String manager_phone) {
		this.manager_phone = manager_phone;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProduction_consumption() {
		return production_consumption;
	}

	public void setProduction_consumption(String production_consumption) {
		this.production_consumption = production_consumption;
	}
}
