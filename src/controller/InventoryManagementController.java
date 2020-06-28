package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Phaser;

import dao.CompanyDAO;
import dao.InventoryDAO;
import javafx.stage.Stage;
import model.CompanyModel;
import model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class InventoryManagementController implements Initializable{
	@FXML private Tab tabInventory;
	@FXML private Tab tabPurchase;
	@FXML private Tab tabSell;
	
	@FXML private Button btnInventory;
	@FXML private Button btnTrade;
	@FXML private Button btnReport;
	@FXML private Button btnAdmin;
	@FXML private Button btnCompany;
	@FXML private Button btnLogout;
	@FXML private Button btnOk;
	@FXML private Button btnEdit;
	@FXML private Button btnDelete;
	@FXML private Button btnSearch1;
	@FXML private Button btnSearch2;
	@FXML private Button btnSearch3;
	@FXML private Button btnReset1;
	@FXML private Button btnReset2;
	@FXML private Button btnReset3;
	@FXML private Button btnCal1;
	@FXML private Button btnCal2;
	@FXML private Button btnPurchase;
	@FXML private Button btnSell;

	@FXML private TextField txtSearch1;
	@FXML private TextField txtCompany1;
	@FXML private TextField txtProduct1;
	@FXML private TextField txtProductNumber1;
	@FXML private TextField txtStock1;
	@FXML private TextField txtPurchase1;
	@FXML private TextField txtSell1;
	@FXML private TextField txtType1;
	@FXML private TextField txtColor1;
	@FXML private TextField txtSize1;
	
	@FXML private TextField txtSearch2;
	@FXML private TextField txtCompany2;
	@FXML private TextField txtProduct2;
	@FXML private TextField txtProductNumber2;
	@FXML private TextField txtStock2;
	@FXML private TextField txtPurchase2;
	@FXML private TextField txtType2;
	@FXML private TextField txtColor2;
	@FXML private TextField txtSize2;
	@FXML private TextField txtOrder1;
	@FXML private TextField txtTotalPurchase;
	
	@FXML private TextField txtSearch3;
	@FXML private TextField txtCompany3;
	@FXML private TextField txtProduct3;
	@FXML private TextField txtProductNumber3;
	@FXML private TextField txtStock3;
	@FXML private TextField txtSell2;
	@FXML private TextField txtType3;
	@FXML private TextField txtColor3;
	@FXML private TextField txtSize3;
	@FXML private TextField txtOrder2;
	@FXML private TextField txtTotalSell;
	
	//재고 탭 콤보박스
	@FXML private ComboBox cmbCompany1;
	@FXML private ComboBox cmbType1;
	@FXML private ComboBox cmbSize1;
	@FXML private ComboBox cmbColor1;
	@FXML private ComboBox cmbType4;
	@FXML private ComboBox cmbColor4;
	@FXML private ComboBox cmbSize4;
	@FXML private ComboBox cmbCompany4;
	
	//주문 탭 콤보박스
	@FXML private ComboBox cmbCompany2;
	@FXML private ComboBox cmbType2;
	@FXML private ComboBox cmbSize2;
	@FXML private ComboBox cmbColor2;
	
	//판매 탭 콤보박스
	@FXML private ComboBox cmbCompany3;
	@FXML private ComboBox cmbType3;
	@FXML private ComboBox cmbSize3;
	@FXML private ComboBox cmbColor3;
	
	
	
	@FXML private TableView tvInventory1;
	@FXML private TableView tvInventory2;
	@FXML private TableView tvInventory3;
	
	public Stage stage;
	private ObservableList<Inventory> obList;
	private ObservableList<CompanyModel> obList1;
	private int tvInventoryIndex;
	private String purchase = "주문";
	
	public InventoryManagementController() {
		this.stage = null;
		this.obList = FXCollections.observableArrayList();
		this.obList1 = FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//탭 이동시 리스트 초기화
		tabInventory.setOnSelectionChanged(e-> selectTabInventoryAction());
		tabPurchase.setOnSelectionChanged(e-> selectTabPurchaseAction());
		tabSell.setOnSelectionChanged(e-> selectTabSellAction());
		
		//재고 리스트 정렬 (타입,색상,사이즈)
		cmbType1.setOnAction(e-> tvInventory1TypeSort());
		cmbColor1.setOnAction(e-> tvInventory1ColorSort());
		cmbSize1.setOnAction(e-> tvInventory1SizeSort());
		
		//주문 리스트 정렬(타입,색상,사이즈)
		cmbType2.setOnAction(e-> tvInventory2TypeSort());
		cmbColor2.setOnAction(e-> tvInventory2ColorSort());
		cmbSize2.setOnAction(e-> tvInventory2SizeSort());
		
		//판매 리스트 정렬(타입,색상,사이즈)
		cmbType3.setOnAction(e-> tvInventory3TypeSort());
		cmbColor3.setOnAction(e-> tvInventory3ColorSort());
		cmbSize3.setOnAction(e-> tvInventory3SizeSort());
		
		companyComboBoxList(); //업체 선택
		typeComboBoxList(); //마스크 타입 선택
		sizeComboBoxList(); //마스크 사이즈 선택
		colorComboBoxList(); //마스크 색상 선택
		
		tvInventory1Column(); //재고 리스트 컬럼
		tvInventory2Column(); //주문 리스트 컬럼
		tvInventory3Column(); //판매 리스트 컬럼
		
		totalLoadList(); //전체 재고 리스트
		
		//목록 클릭시 이벤트
		tvInventory1.setOnMouseClicked(e-> handleTvinventory1MouseClicked(e)); 
		tvInventory2.setOnMouseClicked(e-> handleTvinventory2MouseClicked(e)); 
		tvInventory3.setOnMouseClicked(e-> handleTvinventory3MouseClicked(e)); 
		
		btnOk.setOnAction(e-> handleBtnOkAction(e)); //등록 버튼
		btnEdit.setOnAction(e-> handleBtnEditAction(e)); //수정 버튼
		btnDelete.setOnAction(e-> handleBtnDeleteAction(e)); //삭제버튼
		btnSearch1.setOnAction(e-> handleBtnSearch1Action(e)); //재고 화면 검색버튼
		btnSearch2.setOnAction(e-> handleBtnSearch2Action(e)); //주문 화면 검색버튼
		btnSearch3.setOnAction(e-> handleBtnSearch3Action(e)); //판매 화면 검색버튼
		
		btnCal1.setOnAction(e-> handleBtnCal1Action(e)); //구매총액 계산 버튼
		btnPurchase.setOnAction(e-> handleBtnPurchaseAction(e)); //구매 버튼
		btnCal2.setOnAction(e-> handleBtnCal2Action(e)); //판매총액 계산 버튼
		btnSell.setOnAction(e-> handleBtnSellAction(e)); //판매 버튼
		
		btnReset1.setOnAction(e-> handleBtnReset1Action(e)); // 재고 화면 초기화 버튼
		btnReset2.setOnAction(e-> handleBtnReset2Action(e)); // 주문 화면 초기화 버튼
		btnReset3.setOnAction(e-> handleBtnReset3Action(e)); // 판매 화면 초기화 버튼
		
	}//initialize

	//탭 선택시 리스트 초기화 이벤트
	private void selectTabInventoryAction() {
		txtSearch1.clear();
		cmbCompany1.getSelectionModel().clearSelection();
		cmbType1.getSelectionModel().clearSelection();
		cmbColor1.getSelectionModel().clearSelection();
		cmbSize1.getSelectionModel().clearSelection();
		
		txtProduct1.clear();
		txtProductNumber1.clear();
		txtStock1.clear();
		txtPurchase1.clear();
		txtSell1.clear();
		
		cmbType4.getSelectionModel().clearSelection();
		cmbColor4.getSelectionModel().clearSelection();
		cmbSize4.getSelectionModel().clearSelection();
		cmbCompany4.getSelectionModel().clearSelection();
		
		obList.clear();
		totalLoadList();
	}
	private void selectTabPurchaseAction() {
		txtSearch2.clear();
		cmbCompany2.getSelectionModel().clearSelection();
		cmbType2.getSelectionModel().clearSelection();
		cmbColor2.getSelectionModel().clearSelection();
		cmbSize2.getSelectionModel().clearSelection();
		
		txtCompany2.clear();
		txtProduct2.clear();
		txtProductNumber2.clear();
		txtStock2.clear();
		txtPurchase2.clear();
		txtType2.clear();
		txtColor2.clear();
		txtSize2.clear();
		txtOrder1.clear();
		txtTotalPurchase.clear();
		
		obList.clear();
		totalLoadList();
	}
	private void selectTabSellAction() {
		txtSearch3.clear();
		cmbCompany3.getSelectionModel().clearSelection();
		cmbType3.getSelectionModel().clearSelection();
		cmbColor3.getSelectionModel().clearSelection();
		cmbSize3.getSelectionModel().clearSelection();
		
		txtCompany3.clear();
		txtProduct3.clear();
		txtProductNumber3.clear();
		txtStock3.clear();
		txtSell2.clear();
		txtType3.clear();
		txtColor3.clear();
		txtSize3.clear();
		txtOrder2.clear();
		txtTotalSell.clear();
		
		obList.clear();
		totalLoadList();
		
	}
	
	//판매 버튼 이벤트
	private void handleBtnSellAction(ActionEvent e) {
		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory iv = obList.get(tvInventoryIndex);
		
		int currentStock = iv.getStock();
		int sOrder = Integer.parseInt(txtOrder2.getText());
		
		int editStock = (currentStock - sOrder);
		
		iv.setStock(editStock);
		
		int returnValue = inventoryDAO.getProductSell(iv);
		
		if (returnValue != 0) {
			obList.set(tvInventoryIndex, iv);
			inventoryDAO.getTotalLoadList();
		}
	}
	//구매 버튼 이벤트
	private void handleBtnPurchaseAction(ActionEvent e) {
		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory iv = obList.get(tvInventoryIndex);
		
		int currentStock = iv.getStock();
		int pOrder = Integer.parseInt(txtOrder1.getText());
		
		int editStock = (currentStock + pOrder);
		
		iv.setStock(editStock);
		
		int returnValue = inventoryDAO.getProductPurchase(iv);
		
		if (returnValue != 0) {
			obList.set(tvInventoryIndex, iv);
			inventoryDAO.getTotalLoadList();
		}
	}

	//판매 총액 계산 이벤트
	private void handleBtnCal2Action(ActionEvent e) {
		
		int order = Integer.parseInt(txtOrder2.getText());
		int purchase = Integer.parseInt(txtSell2.getText());
		
		int totalPrice = order * purchase;
		
		txtTotalSell.setText(String.valueOf(totalPrice));
	}
	//구매 총액 계산 이벤트
	private void handleBtnCal1Action(ActionEvent e) {
		
		int order = Integer.parseInt(txtOrder1.getText());
		int purchase = Integer.parseInt(txtPurchase2.getText());
		
		int totalPrice = order * purchase;
		
		txtTotalPurchase.setText(String.valueOf(totalPrice));
	}

	//재고 화면 리스트 정렬
	//콤보박스로 리스트 정렬(타입)
	private void tvInventory1TypeSort() {
	      String type = null;
	      try {
	    	  type = cmbType1.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrTypeList = new InventoryDAO().getTypeSort(type);
	      if (arrTypeList == null) {
	         System.out.println(arrTypeList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrTypeList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	//콤보박스로 리스트 정렬(색상)
	private void tvInventory1ColorSort() {
		 String color = null;
	      try {
	    	  color = cmbColor1.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrColorList = new InventoryDAO().getColorSort(color);
	      if (arrColorList == null) {
	         System.out.println(arrColorList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrColorList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	//콤보박스로 리스트 정렬(크기)
	private void tvInventory1SizeSort() {
		 String size = null;
	      try {
	    	  size = cmbSize1.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrSizeList = new InventoryDAO().getSizeSort(size);
	      if (arrSizeList == null) {
	         System.out.println(arrSizeList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrSizeList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	
	//주문 화면 리스트 정렬
	//타입 정렬
	private void tvInventory2TypeSort() {
			String type = null;
		      try {
		    	  type = cmbType2.getSelectionModel().getSelectedItem().toString();
		      }catch(NullPointerException e) {}
		      ArrayList<Inventory> arrTypeList = new InventoryDAO().getTypeSort(type);
		      if (arrTypeList == null) {
		         System.out.println(arrTypeList + ", 값이 null 입니다.");
		      } else {
		    	  obList.clear();
		    	 for(Inventory iv : arrTypeList) {
		    		 obList.add(iv);
		    	 }
		      }
		}
	//색상 정렬
	private void tvInventory2ColorSort() {
		 String color = null;
	      try {
	    	  color = cmbColor2.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrColorList = new InventoryDAO().getColorSort(color);
	      if (arrColorList == null) {
	         System.out.println(arrColorList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrColorList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	//크기 정렬
	private void tvInventory2SizeSort() {
		 String size = null;
	      try {
	    	  size = cmbSize2.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrSizeList = new InventoryDAO().getSizeSort(size);
	      if (arrSizeList == null) {
	         System.out.println(arrSizeList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrSizeList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }

	//판매 화면 리스트 정렬
	//타입 정렬
	private void tvInventory3TypeSort() {
		String type = null;
	      try {
	    	  type = cmbType3.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrTypeList = new InventoryDAO().getTypeSort(type);
	      if (arrTypeList == null) {
	         System.out.println(arrTypeList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrTypeList) {
	    		 obList.add(iv);
	    	 }
	      }
	}
	//색상 정렬
	private void tvInventory3ColorSort() {
		 String color = null;
	      try {
	    	  color = cmbColor3.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrColorList = new InventoryDAO().getColorSort(color);
	      if (arrColorList == null) {
	         System.out.println(arrColorList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrColorList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	//크기 정렬
	private void tvInventory3SizeSort() {
		 String size = null;
	      try {
	    	  size = cmbSize3.getSelectionModel().getSelectedItem().toString();
	      }catch(NullPointerException e) {}
	      ArrayList<Inventory> arrSizeList = new InventoryDAO().getSizeSort(size);
	      if (arrSizeList == null) {
	         System.out.println(arrSizeList + ", 값이 null 입니다.");
	      } else {
	    	  obList.clear();
	    	 for(Inventory iv : arrSizeList) {
	    		 obList.add(iv);
	    	 }
	      }
	   }
	
	
	//재고 화면 검색 버튼
	private void handleBtnSearch1Action(ActionEvent e) {
		try {
		InventoryDAO inventoryDAO = new InventoryDAO();
		if(txtSearch1.getText().trim().equals("")) {
			throw new Exception();
		}
		ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch1.getText().trim());
		
		if(arrList.size() != 0) {
			obList.clear();
			for (int i = 0; i < arrList.size(); i++) {
				Inventory inven= arrList.get(i);
				obList.add(inven);
			}
		}
	}catch (Exception event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Search Error");
		alert.setHeaderText("제품 번호를 입력하시오");
		alert.setContentText("제품 번호 입력");
		alert.showAndWait();
		}
	}
	//주문 화면 검색 버튼
	private void handleBtnSearch2Action(ActionEvent e) {
		try {
		InventoryDAO inventoryDAO = new InventoryDAO();
		if(txtSearch2.getText().trim().equals("")) {
			throw new Exception();
		}
		ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch2.getText().trim());
		
		if(arrList.size() != 0) {
			obList.clear();
			for (int i = 0; i < arrList.size(); i++) {
				Inventory inven= arrList.get(i);
				obList.add(inven);
			}
		}
	}catch (Exception event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Search Error");
		alert.setHeaderText("제품 번호를 입력하시오");
		alert.setContentText("제품 번호 입력");
		alert.showAndWait();
		}
	}
	//판매 화면 검색 버튼
	private void handleBtnSearch3Action(ActionEvent e) {
		try {
		InventoryDAO inventoryDAO = new InventoryDAO();
		if(txtSearch3.getText().trim().equals("")) {
			throw new Exception();
		}
		ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch3.getText().trim());
		
		if(arrList.size() != 0) {
			obList.clear();
			for (int i = 0; i < arrList.size(); i++) {
				Inventory inven= arrList.get(i);
				obList.add(inven);
			}
		}
	}catch (Exception event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Search Error");
		alert.setHeaderText("제품 번호를 입력하시오");
		alert.setContentText("제품 번호 입력");
		alert.showAndWait();
		}
	}

	
	//등록 버튼 이벤트
	private void handleBtnOkAction(ActionEvent e) {
		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory iv = null;
		
	try {
		iv = new Inventory(txtProductNumber1.getText(),txtProduct1.getText(),
				Integer.parseInt(txtStock1.getText()),Integer.parseInt(txtPurchase1.getText()),
				Integer.parseInt(txtSell1.getText()),
				cmbType4.getSelectionModel().getSelectedItem().toString()
				,cmbColor4.getSelectionModel().getSelectedItem().toString()
				,cmbSize4.getSelectionModel().getSelectedItem().toString());
	}catch(Exception e1) {}
	int returnValue = inventoryDAO.getInventoryRegist(iv);
	if (returnValue != 0) {
		obList.clear();
		totalLoadList();
		}
	}
	//수정 버튼 이벤트
	private void handleBtnEditAction(ActionEvent e) {
		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory inven = obList.get(tvInventoryIndex);
		
		inven.setProduct(txtProduct1.getText());
		inven.setPurchase(Integer.parseInt(txtPurchase1.getText()));
		inven.setSell(Integer.parseInt(txtSell1.getText()));
		inven.setType(cmbType4.getSelectionModel().getSelectedItem().toString());
		inven.setSize(cmbSize4.getSelectionModel().getSelectedItem().toString());
		inven.setColor(cmbColor4.getSelectionModel().getSelectedItem().toString());
		
		int returnValue = inventoryDAO.getInventoryUpdate(inven);
		
		if (returnValue != 0) {
			obList.set(tvInventoryIndex, inven);
		}
	}

	
	//재고 리스트에서 목록 선택시 이벤트
	private void handleTvinventory1MouseClicked(MouseEvent e) {
		tvInventoryIndex = tvInventory1.getSelectionModel().getSelectedIndex();
	
		try {
		Inventory inventory = obList.get(tvInventoryIndex);
		
		txtProductNumber1.setText(String.valueOf(inventory.getProductNumber()));
		txtProduct1.setText(inventory.getProduct());
		txtStock1.setText(String.valueOf(inventory.getStock()));
		txtPurchase1.setText(String.valueOf(inventory.getPurchase()));
		txtSell1.setText(String.valueOf(inventory.getSell()));
		cmbCompany4.getSelectionModel().select(inventory.getCompanyName());
		cmbType4.getSelectionModel().select(inventory.getType());
		cmbColor4.getSelectionModel().select(inventory.getColor());
		cmbSize4.getSelectionModel().select(inventory.getSize());
		
		}
		catch(Exception e1){}
		
	}
	
	private void handleTvinventory2MouseClicked(MouseEvent e) {
		tvInventoryIndex = tvInventory2.getSelectionModel().getSelectedIndex();
		
		try {
			Inventory inventory = obList.get(tvInventoryIndex);
			
			txtProductNumber2.setText(String.valueOf(inventory.getProductNumber()));
			txtProduct2.setText(inventory.getProduct());
			txtStock2.setText(String.valueOf(inventory.getStock()));
			txtPurchase2.setText(String.valueOf(inventory.getPurchase()));
			txtType2.setText(inventory.getType());
			txtColor2.setText(inventory.getColor());
			txtSize2.setText(inventory.getSize());
			
		}
		catch(Exception e1){}
	}
	
	private void handleTvinventory3MouseClicked(MouseEvent e) {
		tvInventoryIndex = tvInventory3.getSelectionModel().getSelectedIndex();
		
		try {
		Inventory inventory = obList.get(tvInventoryIndex);
		
		txtProductNumber3.setText(String.valueOf(inventory.getProductNumber()));
		txtProduct3.setText(inventory.getProduct());
		txtStock3.setText(String.valueOf(inventory.getStock()));
		txtSell2.setText(String.valueOf(inventory.getSell()));
		txtType3.setText(inventory.getType());
		txtColor3.setText(inventory.getColor());
		txtSize3.setText(inventory.getSize());
		}
		catch(Exception e1){}
	}

	
	//재고 리스트, DB 에서 해당 목록 삭제
	private void handleBtnDeleteAction(ActionEvent e) {

		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory inventory = obList.get(tvInventoryIndex);
		String no = inventory.getProductNumber();

		int returnValue = inventoryDAO.getInventoryDelete(no);
		
		obList.remove(tvInventoryIndex);
		
		txtCompany1.clear();
		txtProduct1.clear();
		txtProductNumber1.clear();
		txtStock1.clear();
		txtPurchase1.clear();
		txtSell1.clear();
		cmbType4.getSelectionModel().clearSelection();
		cmbColor4.getSelectionModel().clearSelection();
		cmbSize4.getSelectionModel().clearSelection();
		
	}

	//DB에서 재고 가져오기
	private void totalLoadList() {
		InventoryDAO inventoryDAO = new InventoryDAO();
		ArrayList<Inventory> arrayList = inventoryDAO.getTotalLoadList();
		if (arrayList == null) {
			return;
		}
		obList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			Inventory iv = arrayList.get(i);
			obList.add(iv);
		}
	}
	
	
	//검색 조건 및 리스트 초기화버튼
	private void handleBtnReset1Action(ActionEvent e) {
				
		txtSearch1.clear();
		cmbCompany1.getSelectionModel().clearSelection();
		cmbType1.getSelectionModel().clearSelection();
		cmbColor1.getSelectionModel().clearSelection();
		cmbSize1.getSelectionModel().clearSelection();
		
		txtProduct1.clear();
		txtProductNumber1.clear();
		txtStock1.clear();
		txtPurchase1.clear();
		txtSell1.clear();
		
		cmbCompany4.getPromptText();
		cmbType4.getSelectionModel().clearSelection();
		cmbColor4.getSelectionModel().clearSelection();
		cmbSize4.getSelectionModel().clearSelection();
		
		obList.clear();
		totalLoadList();
		
	}
	
	private void handleBtnReset2Action(ActionEvent e) {
		txtSearch2.clear();
		cmbCompany2.getSelectionModel().clearSelection();
		cmbType2.getSelectionModel().clearSelection();
		cmbColor2.getSelectionModel().clearSelection();
		cmbSize2.getSelectionModel().clearSelection();
		
		txtCompany2.clear();
		txtProduct2.clear();
		txtProductNumber2.clear();
		txtStock2.clear();
		txtPurchase2.clear();
		txtType2.clear();
		txtColor2.clear();
		txtSize2.clear();
		txtOrder1.clear();
		txtTotalPurchase.clear();
		
		obList.clear();
		totalLoadList();
	}
	
	private void handleBtnReset3Action(ActionEvent e) {
		txtSearch3.clear();
		cmbCompany3.getSelectionModel().clearSelection();
		cmbType3.getSelectionModel().clearSelection();
		cmbColor3.getSelectionModel().clearSelection();
		cmbSize3.getSelectionModel().clearSelection();
		
		txtCompany3.clear();
		txtProduct3.clear();
		txtProductNumber3.clear();
		txtStock3.clear();
		txtSell2.clear();
		txtType3.clear();
		txtColor3.clear();
		txtSize3.clear();
		txtOrder2.clear();
		txtTotalSell.clear();
		
		obList.clear();
		totalLoadList();
	}

	
	//콤보박스 리스트
	private void colorComboBoxList() {
		ObservableList<String> colorList=FXCollections.observableArrayList();
		colorList.addAll("흰색","검정색");
		cmbColor1.setPromptText("색상 선택");
		cmbColor1.setItems(colorList);
		cmbColor2.setPromptText("색상 선택");
		cmbColor2.setItems(colorList);
		cmbColor3.setPromptText("색상 선택");
		cmbColor3.setItems(colorList);
		cmbColor4.setPromptText("색상");
		cmbColor4.setItems(colorList);
	}

	private void sizeComboBoxList() {
		ObservableList<String> sizeList=FXCollections.observableArrayList();
		sizeList.addAll("어린이용","성인용");
		cmbSize1.setPromptText("사이즈 선택");
		cmbSize1.setItems(sizeList);
		cmbSize2.setPromptText("사이즈 선택");
		cmbSize2.setItems(sizeList);
		cmbSize3.setPromptText("사이즈 선택");
		cmbSize3.setItems(sizeList);
		cmbSize4.setPromptText("사이즈");
		cmbSize4.setItems(sizeList);
	}

	private void typeComboBoxList() {	
		ObservableList<String> typeList=FXCollections.observableArrayList();
		typeList.addAll("KF80","KF94","덴탈 마스크","면 마스크");
		cmbType1.setPromptText("종류 선택");
		cmbType1.setItems(typeList);
		cmbType2.setPromptText("종류 선택");
		cmbType2.setItems(typeList);
		cmbType3.setPromptText("종류 선택");
		cmbType3.setItems(typeList);
		cmbType4.setPromptText("종류");
		cmbType4.setItems(typeList);
	}

	private void companyComboBoxList() {
		CompanyDAO companyDAO = new CompanyDAO();
		
		ArrayList<CompanyModel> arrayList = companyDAO.companyListUp(purchase);
		if(arrayList == null) {
			return;
		}
		for(int i = 0; i<arrayList.size(); i++) {
			CompanyModel com = arrayList.get(i);
			obList1.add(com);
		}
		cmbCompany1.setItems(obList1);
		
		cmbCompany1.setPromptText("업체 선택");
		cmbCompany2.setPromptText("업체 선택");
		cmbCompany3.setPromptText("업체 선택");
		cmbCompany4.setPromptText("업체 선택");

	}
	
	
	//재고 리스트 테이블
	private void tvInventory1Column() {
		TableColumn colPnumber=new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));
		
		TableColumn colCompany=new TableColumn("업체명");
		colCompany.setPrefWidth(160.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		
		TableColumn colProduct=new TableColumn("제품명");
		colProduct.setPrefWidth(220.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
		
		TableColumn colStock=new TableColumn("재고");
		colStock.setMaxWidth(40);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		
		TableColumn colPurchase=new TableColumn("구입가");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));
		
		TableColumn colSell=new TableColumn("판매가");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));
		
		TableColumn colType=new TableColumn("종류");
		colType.setMaxWidth(90);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		TableColumn colSize=new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		
		TableColumn colColor=new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		
		tvInventory1.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, 
				colPurchase, colSell, colType, colSize, colColor);
		
		tvInventory1.setItems(obList);
		
	}
	//구매화면 리스트
	private void tvInventory2Column() {
		TableColumn colPnumber=new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));
		
		TableColumn colCompany=new TableColumn("업체명");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
		
		TableColumn colProduct=new TableColumn("제품명");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
		
		TableColumn colStock=new TableColumn("재고");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		
		TableColumn colPurchase=new TableColumn("구입가");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));
		
		TableColumn colType=new TableColumn("종류");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		TableColumn colSize=new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		
		TableColumn colColor=new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		
		tvInventory2.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, 
				colPurchase, colType, colSize, colColor);
		
		tvInventory2.setItems(obList);
		
	}
	//판매화면 리스트
	private void tvInventory3Column() {
		TableColumn colPnumber=new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));
		
		TableColumn colCompany=new TableColumn("업체명");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
		
		TableColumn colProduct=new TableColumn("제품명");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
		
		TableColumn colStock=new TableColumn("재고");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		
		TableColumn colSell=new TableColumn("판매가");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));
		
		TableColumn colType=new TableColumn("종류");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		TableColumn colSize=new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		
		TableColumn colColor=new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		
		tvInventory3.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, 
				colSell, colType, colSize, colColor);
		
		tvInventory3.setItems(obList);
		
	}
	
}
