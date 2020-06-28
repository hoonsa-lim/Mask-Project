package model;

public class TradeListModel {
	private int no;
	private int order_quantity;
	private String total_price;
	private String date;
	private String purchaseOrSell;
	
	//join 용 업체(업체명, 제품명, 제품번호)도 담을 수 있는 변수
	private String companyName;
	private String productName;
	private String productNumber;
	
	//기본 생성자
	public TradeListModel(int no, int order_quantity, String total_price, String date, String purchaseOrSell) {
		super();
		this.no = no;
		this.order_quantity = order_quantity;
		this.total_price = total_price;
		this.date = date;
		this.purchaseOrSell = purchaseOrSell;
	}
	
	
	
	//오버로딩(추가 : 업체명, 제품명, 제품번호)
	public TradeListModel(int no, int order_quantity, String total_price, String date, String purchaseOrSell,
			String companyName, String productName, String productNumber) {
		super();
		this.no = no;
		this.order_quantity = order_quantity;
		this.total_price = total_price;
		this.date = date;
		this.purchaseOrSell = purchaseOrSell;
		this.companyName = companyName;
		this.productName = productName;
		this.productNumber = productNumber;
	}

	
	//getter,setter
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getOrder_quantity() {
		return order_quantity;
	}

	public void setOrder_quantity(int order_quantity) {
		this.order_quantity = order_quantity;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPurchaseOrSell() {
		return purchaseOrSell;
	}

	public void setPurchaseOrSell(String purchaseOrSell) {
		this.purchaseOrSell = purchaseOrSell;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	
}
