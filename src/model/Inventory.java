package model;

public class Inventory {
	private String productNumber;
	private String companyName;
	private String product;
	private int stock;
	private int purchase;
	private int sell;
	private String type;
	private String color;
	private String size;
	private String companyNumber;
	
	public Inventory(String productNumber, String product, int stock, int purchase, int sell,
			String type, String size, String color) {
		super();
		this.productNumber = productNumber;
		this.product = product;
		this.stock = stock;
		this.purchase = purchase;
		this.sell = sell;
		this.type = type;
		this.color = color;
		this.size = size;
	}
	
	public Inventory(String productNumber, String product, int stock, int purchase, 
			int sell, String type, String size, String color, String companyName) {
		super();
		this.productNumber = productNumber;
		this.product = product;
		this.stock = stock;
		this.purchase = purchase;
		this.sell = sell;
		this.type = type;
		this.size = size;
		this.color = color;
		this.companyName = companyName;
	}
	
	public Inventory(String productNumber, String product, int stock, int purchase, 
			int sell, String type, String size, String color, String companyName, String companyNumber) {
		super();
		this.productNumber = productNumber;
		this.product = product;
		this.stock = stock;
		this.purchase = purchase;
		this.sell = sell;
		this.type = type;
		this.size = size;
		this.color = color;
		this.companyName = companyName;
		this.companyNumber = companyNumber;
	}
	
	public Inventory(String type, String size) {
		this.type = type;
		this.size = size;
		}
	
	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getPurchase() {
		return purchase;
	}

	public void setPurchase(int purchase) {
		this.purchase = purchase;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
}