package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Phaser;

import application.CompanyMain;
import application.InventoryMain;
import application.LoginMain;
import application.ReportMain;
import application.TradeListMain;
import dao.CompanyDAO;
import dao.InventoryDAO;
import dao.TradeListDAO;
import javafx.stage.Stage;
import model.CompanyModel;
import model.Inventory;
import model.TradeListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class InventoryManagementController implements Initializable {
	@FXML
	private Tab tabInventory;
	@FXML
	private Tab tabPurchase;
	@FXML
	private Tab tabSell;

	@FXML
	private Button btnInventory;
	@FXML
	private Button btnTrade;
	@FXML
	private Button btnReport;
	@FXML
	private Button btnCompany;
	@FXML
	private Button btnLogout;

	@FXML
	private Button btnOk;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnSearch1;
	@FXML
	private Button btnSearch2;
	@FXML
	private Button btnSearch3;
	@FXML
	private Button btnReset1;
	@FXML
	private Button btnReset2;
	@FXML
	private Button btnReset3;
	@FXML
	private Button btnCal1;
	@FXML
	private Button btnCal2;
	@FXML
	private Button btnPurchase;
	@FXML
	private Button btnSell;

	@FXML
	private TextField txtSearch1;
	@FXML
	private TextField txtCompany1;
	@FXML
	private TextField txtProduct1;
	@FXML
	private TextField txtProductNumber1;
	@FXML
	private TextField txtStock1;
	@FXML
	private TextField txtPurchase1;
	@FXML
	private TextField txtSell1;
	@FXML
	private TextField txtType1;
	@FXML
	private TextField txtColor1;
	@FXML
	private TextField txtSize1;

	@FXML
	private TextField txtSearch2;
	@FXML
	private TextField txtCompany2;
	@FXML
	private TextField txtProduct2;
	@FXML
	private TextField txtProductNumber2;
	@FXML
	private TextField txtStock2;
	@FXML
	private TextField txtPurchase2;
	@FXML
	private TextField txtType2;
	@FXML
	private TextField txtColor2;
	@FXML
	private TextField txtSize2;
	@FXML
	private TextField txtOrder1;
	@FXML
	private TextField txtTotalPurchase;

	@FXML
	private TextField txtSearch3;
	@FXML
	private TextField txtCompany3;
	@FXML
	private TextField txtProduct3;
	@FXML
	private TextField txtProductNumber3;
	@FXML
	private TextField txtStock3;
	@FXML
	private TextField txtSell2;
	@FXML
	private TextField txtType3;
	@FXML
	private TextField txtColor3;
	@FXML
	private TextField txtSize3;
	@FXML
	private TextField txtOrder2;
	@FXML
	private TextField txtTotalSell;

	// 재고 탭 콤보박스
	@FXML
	private ComboBox cmbCompany1;
	@FXML
	private ComboBox cmbType4;
	@FXML
	private ComboBox cmbColor4;
	@FXML
	private ComboBox cmbSize4;
	@FXML
	private ComboBox cmbCompany4;

	// 주문 탭 콤보박스
	@FXML
	private ComboBox cmbCompany2;
	@FXML
	private ComboBox cmbType2;
	@FXML
	private ComboBox cmbSize2;
	@FXML
	private ComboBox cmbColor2;

	// 판매 탭 콤보박스
	@FXML
	private ComboBox cmbCompany3;
	@FXML
	private ComboBox cmbType3;
	@FXML
	private ComboBox cmbSize3;
	@FXML
	private ComboBox cmbColor3;
	@FXML
	private ComboBox cmbCompany5;

	@FXML
	private TableView tvInventory1;
	@FXML
	private TableView tvInventory2;
	@FXML
	private TableView tvInventory3;
	@FXML
	private RadioButton rdoRegister;
	@FXML
	private RadioButton rdoEdit;

	public Stage inventoryStage;
	private ObservableList<Inventory> obList;
	private ObservableList<Inventory> obList2;
	private ObservableList<String> obList1;
	private ObservableList<String> obList3;
	private ObservableList<CompanyModel> obList4;
	private ObservableList<CompanyModel> obList5;
	private int tvInventoryIndex = -1;
	private int cmbselect = 0;
	private String production = "생산";
	private String production2 = "소비";
	private String purchase = "주문";
	private String sell = "판매";
	private ArrayList<Inventory> arrayList = null;
	private ArrayList<CompanyModel> arrayList2 = null;// 콤보박스에 들어간 소비업체 객체정보 기억

	public InventoryManagementController() {
		this.inventoryStage = null;
		this.obList = FXCollections.observableArrayList();
		this.obList1 = FXCollections.observableArrayList();
		this.obList2 = FXCollections.observableArrayList();
		this.obList3 = FXCollections.observableArrayList();
		this.obList5 = FXCollections.observableArrayList();
		this.arrayList = new ArrayList<Inventory>();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 다른 화면으로 이동 버튼
		btnCompany.setOnAction(e -> handleBtnCompanyAction());
		btnTrade.setOnAction(e -> handleBtnTradeAction());
		btnReport.setOnAction(e -> handleBtnReportAction());
		btnLogout.setOnAction(e -> handleBtnLogoutAction());

		// 탭 이동시 리스트 초기화
		tabInventory.setOnSelectionChanged(e -> selectTabInventoryAction());
		tabPurchase.setOnSelectionChanged(e -> selectTabPurchaseAction());
		tabSell.setOnSelectionChanged(e -> selectTabSellAction());

		tvInventory1Column(); // 재고 리스트 컬럼
		tvInventory2Column(); // 주문 리스트 컬럼
		tvInventory3Column(); // 판매 리스트 컬럼

		totalLoadList(); // 전체 재고 리스트

		registerFormat();// 등록 text 포맷
		companyComboBoxList();
		typeComboBoxList();
		colorComboBoxList();
		sizeComboBoxList();

		radioButtonGroup();// 등록 수정 group

		cmbCompany4.setOnAction(e -> cmbselect = cmbCompany4.getSelectionModel().getSelectedIndex());

		// 목록 클릭시 이벤트
		tvInventory1.setOnMouseClicked(e -> handleTvinventory1MouseClicked(e));
		tvInventory2.setOnMouseClicked(e -> handleTvinventory2MouseClicked(e));
		tvInventory3.setOnMouseClicked(e -> handleTvinventory3MouseClicked(e));

		btnOk.setOnAction(e -> handleBtnOkAction(e)); // 등록 버튼
		btnEdit.setOnAction(e -> handleBtnEditAction(e)); // 수정 버튼
		btnDelete.setOnAction(e -> handleBtnDeleteAction(e)); // 삭제버튼

		btnSearch1.setOnAction(e -> handleBtnSearch1Action(e)); // 재고 화면 검색버튼
		btnSearch2.setOnAction(e -> handleBtnSearch2Action(e)); // 주문 화면 검색버튼
		btnSearch3.setOnAction(e -> handleBtnSearch3Action(e)); // 판매 화면 검색버튼

		btnCal1.setOnAction(e -> handleBtnCal1Action(e)); // 구매총액 계산 버튼
		btnPurchase.setOnAction(e -> handleBtnPurchaseAction(e)); // 구매 버튼
		btnCal2.setOnAction(e -> handleBtnCal2Action(e)); // 판매총액 계산 버튼
		btnSell.setOnAction(e -> handleBtnSellAction(e)); // 판매 버튼

		btnReset1.setOnAction(e -> handleBtnReset1Action(e)); // 재고 화면 초기화 버튼
		btnReset2.setOnAction(e -> handleBtnReset2Action(e)); // 주문 화면 초기화 버튼
		btnReset3.setOnAction(e -> handleBtnReset3Action(e)); // 판매 화면 초기화 버튼

		rdoRegister.setOnAction(e -> handleRdoRegisterAction());// 등록 rdoButton 눌렀을 때 이벤트
		rdoEdit.setOnAction(e -> handleRdoEditAction());// 수정 rdoButton 눌렀을 때 이벤트
	}// initialize

	// toggleGroup init
	private void radioButtonGroup() {
		ToggleGroup tg = new ToggleGroup();
		rdoRegister.setToggleGroup(tg);
		rdoEdit.setToggleGroup(tg);

		// default 설정
		rdoRegister.setSelected(true);
		btnEdit.setVisible(false);
	}

	// 수정 rdoButton 눌렀을 때 이벤트
	private void handleRdoEditAction() {
		btnEdit.setVisible(true);
		btnOk.setVisible(false);
	}

	// 등록 rdoButton 눌렀을 때 이벤트
	private void handleRdoRegisterAction() {
		btnEdit.setVisible(false);
		btnOk.setVisible(true);
		
		//클리어
		tvInventoryIndex = -1;

		txtSearch1.clear();

		txtProduct1.clear();
		txtProductNumber1.clear();
		txtStock1.clear();
		txtPurchase1.clear();
		txtSell1.clear();

		cmbCompany4.getSelectionModel().clearSelection();
		cmbType4.getSelectionModel().clearSelection();
		cmbColor4.getSelectionModel().clearSelection();
		cmbSize4.getSelectionModel().clearSelection();
	}

	// 제품 등록시 입력 포멧 설정
	private void registerFormat() {
		// 제품번호 7자리로 한정
		DecimalFormat cmNumber = new DecimalFormat("#######");
		txtProductNumber1.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 8) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
		// 제품명 최대 45자리
		txtProduct1.setOnKeyTyped(event -> {
			int maxCharacters = 44;
			if (txtProduct1.getText().length() > maxCharacters)
				event.consume();
		});
		// 재고 최대 5자리
		DecimalFormat stockFormat = new DecimalFormat("#####");
		txtStock1.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = stockFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 6) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
		// 구입가 최대 4자리
		DecimalFormat txtPurchase1Format = new DecimalFormat("####");
		txtPurchase1.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = txtPurchase1Format.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 5) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
		// 판매가 최대 4자리
		DecimalFormat txtSell1Format = new DecimalFormat("####");
		txtSell1.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = txtSell1Format.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 5) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
	}

	private void sizeComboBoxList() {
		cmbSize4.setItems(FXCollections.observableArrayList("성인용", "어린이용"));
		cmbSize4.setPromptText("사이즈 선택");
	}

	private void colorComboBoxList() {
		cmbColor4.setItems(FXCollections.observableArrayList("흰색", "검정색"));
		cmbColor4.setPromptText("색상 선택");
	}

	private void typeComboBoxList() {
		cmbType4.setItems(FXCollections.observableArrayList("KF80", "KF94", "덴탈 마스크", "면 마스크"));
		cmbType4.setPromptText("종류 선택");
	}

	// 보고서 화면으로 이동 버튼
	private void handleBtnReportAction() {
		try {
			new ReportMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 로그인 화면으로 이동 버튼
	private void handleBtnLogoutAction() {
		try {
			new LoginMain().start(new Stage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		inventoryStage.close();
	}

	// 거래 내역 화면으로 이동 버튼
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 업체 관리 화면으로 이동 버튼
	private void handleBtnCompanyAction() {
		try {
			new CompanyMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 탭 선택시 리스트 초기화 이벤트
	private void selectTabInventoryAction() {
		txtSearch1.clear();

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
		cmbCompany5.getSelectionModel().clearSelection();

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

	// 판매 버튼 이벤트
	private void handleBtnSellAction(ActionEvent e) {
		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			TradeListDAO tradelistDAO = new TradeListDAO();
			Inventory iv = obList.get(tvInventoryIndex);

			int returnValue = 0;
			int currentStock = iv.getStock();
			int sOrder = Integer.parseInt(txtOrder2.getText());

			if (currentStock < 10) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("에러");
				alert.setHeaderText("재고가 부족합니다");
				alert.setContentText("재고 주문 필요");
				alert.showAndWait();
				return;
			}

			Calendar cal = Calendar.getInstance();
			int second = cal.get(Calendar.YEAR);
			int minute = cal.get(Calendar.MONTH) + 1;
			int hour = cal.get(Calendar.DAY_OF_MONTH);

			// 거래가 이뤼지는 시스템 날짜를 받는 문
			String s = String.valueOf(second) + "-" + String.valueOf(minute) + "-" + String.valueOf(hour);

			// 콤보박스에서 선택된 소비업체의 업체번호를 받아오기 위한 문
			CompanyModel c = arrayList2.get(cmbCompany5.getSelectionModel().getSelectedIndex());

			int returnValue2 = tradelistDAO.registrationPurchaseOrSell(Integer.parseInt(txtOrder2.getText()),
					txtTotalSell.getText(), s, sell, c.getCompany_number(), iv.getProductNumber());

			int editStock = (currentStock - sOrder);

			iv.setStock(editStock);
			returnValue = inventoryDAO.getProductSell(iv);

			if (returnValue != 0 && returnValue2 != 0) {
				obList.set(tvInventoryIndex, iv);
				inventoryDAO.getTotalLoadList();
			}
			handleBtnReset1Action(e);
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setHeaderText("재고 선택 오류");
			alert.setContentText("판매할 재고를 선택하시오");
			alert.showAndWait();
		}
	}

	// 구매 버튼 이벤트
	private void handleBtnPurchaseAction(ActionEvent e) {
		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			TradeListDAO tradelistDAO = new TradeListDAO();

			Calendar cal = Calendar.getInstance();
			int second = cal.get(Calendar.YEAR);
			int minute = cal.get(Calendar.MONTH) + 1;
			int hour = cal.get(Calendar.DAY_OF_MONTH);

			String s = String.valueOf(second) + "-" + String.valueOf(minute) + "-" + String.valueOf(hour);

			Inventory inven = arrayList.get(tvInventoryIndex);

			int returnValue2 = tradelistDAO.registrationPurchaseOrSell(Integer.parseInt(txtOrder1.getText()),
					txtTotalPurchase.getText(), s, purchase, inven.getCompanyNumber(), inven.getProductNumber());

			Inventory iv = obList.get(tvInventoryIndex);

			int currentStock = iv.getStock();
			int pOrder = Integer.parseInt(txtOrder1.getText());
			int editStock = (currentStock + pOrder);

			iv.setStock(editStock);

			int returnValue = inventoryDAO.getProductPurchase(iv);

			if (returnValue != 0 && returnValue2 != 0) {
				obList.set(tvInventoryIndex, iv);
				inventoryDAO.getTotalLoadList();
			}

			handleBtnReset1Action(e);
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setHeaderText("재고 선택 오류");
			alert.setContentText("구매할 재고를 선택하시오");
			alert.showAndWait();
		}
	}

	// 판매 총액 계산 이벤트
	private void handleBtnCal2Action(ActionEvent e) {
		try {
			int order = Integer.parseInt(txtOrder2.getText());
			int purchase = Integer.parseInt(txtSell2.getText());

			int totalPrice = order * purchase;

			txtTotalSell.setText(String.valueOf(totalPrice));
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("재고 입력 오류");
			alert.setContentText("판매 수량을 입력 하시오");
			alert.showAndWait();
		}
	}

	// 구매 총액 계산 이벤트
	private void handleBtnCal1Action(ActionEvent e) {
		try {
			int order = Integer.parseInt(txtOrder1.getText());
			int purchase = Integer.parseInt(txtPurchase2.getText());

			int totalPrice = order * purchase;

			txtTotalPurchase.setText(String.valueOf(totalPrice));
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("재고 입력 오류");
			alert.setContentText("구매 수량을 입력 하시오");
			alert.showAndWait();
		}
	}

	// 재고 화면 검색 버튼
	private void handleBtnSearch1Action(ActionEvent e) {
		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			if (txtSearch1.getText().trim().equals("")) {
				throw new Exception();
			}
			ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch1.getText().trim());

			if (arrList.size() != 0) {
				obList.clear();
				for (int i = 0; i < arrList.size(); i++) {
					Inventory inven = arrList.get(i);
					obList.add(inven);
				}
			}
		} catch (Exception event) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Search Error");
			alert.setHeaderText("검색할 제품 번호를 입력하시오");
			alert.setContentText("제품 번호를 입력하시오");
			alert.showAndWait();
		}
	}

	// 주문 화면 검색 버튼
	private void handleBtnSearch2Action(ActionEvent e) {
		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			if (txtSearch2.getText().trim().equals("")) {
				throw new Exception();
			}
			ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch2.getText().trim());

			if (arrList.size() != 0) {
				obList.clear();
				for (int i = 0; i < arrList.size(); i++) {
					Inventory inven = arrList.get(i);
					obList.add(inven);
				}
			}
		} catch (Exception event) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Search Error");
			alert.setHeaderText("검색할 제품 번호를 입력하시오");
			alert.setContentText("제품 번호를 입력하시오");
			alert.showAndWait();
		}
	}

	// 판매 화면 검색 버튼
	private void handleBtnSearch3Action(ActionEvent e) {
		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			if (txtSearch3.getText().trim().equals("")) {
				throw new Exception();
			}
			ArrayList<Inventory> arrList = inventoryDAO.getProductSearch(txtSearch3.getText().trim());

			if (arrList.size() != 0) {
				obList.clear();
				for (int i = 0; i < arrList.size(); i++) {
					Inventory inven = arrList.get(i);
					obList.add(inven);
				}
			}
		} catch (Exception event) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Search Error");
			alert.setHeaderText("검색할 제품 번호를 입력하시오");
			alert.setContentText("제품 번호를 입력하시오");
			alert.showAndWait();
		}
	}

	// 등록 버튼 이벤트
	private void handleBtnOkAction(ActionEvent e) {
		InventoryDAO inventoryDAO = new InventoryDAO();
		Inventory iv = null;

		try {
			iv = new Inventory(txtProductNumber1.getText(), txtProduct1.getText(),
					Integer.parseInt(txtStock1.getText()), Integer.parseInt(txtPurchase1.getText()),
					Integer.parseInt(txtSell1.getText()), cmbType4.getSelectionModel().getSelectedItem().toString(),
					cmbSize4.getSelectionModel().getSelectedItem().toString(),
					cmbColor4.getSelectionModel().getSelectedItem().toString(),
					cmbCompany4.getSelectionModel().getSelectedItem().toString(),
					String.valueOf(obList5.get(cmbselect).getCompany_number()));

			handleBtnReset1Action(e);
		} catch (Exception e1) {
		}

		int returnValue = inventoryDAO.getInventoryRegist(iv);

		if (returnValue != 0) {
			obList.clear();
			totalLoadList();
		}
	}

	// 수정 버튼 이벤트
	private void handleBtnEditAction(ActionEvent e) {
		try {
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
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setHeaderText("재고 선택 오류");
			alert.setContentText("수정할 재고를 선택하시오");
			alert.showAndWait();
		}
	}

	// 재고 리스트에서 목록 선택시 이벤트
	private void handleTvinventory1MouseClicked(MouseEvent e) {
		if (rdoEdit.isSelected()) {
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

			} catch (Exception e1) {
			}
		} else {
			return;
		}

	}

	private void handleTvinventory2MouseClicked(MouseEvent e) {
		tvInventoryIndex = tvInventory2.getSelectionModel().getSelectedIndex();

		try {
			Inventory inventory = obList.get(tvInventoryIndex);

			txtProductNumber2.setText(String.valueOf(inventory.getProductNumber()));
			txtCompany2.setText(inventory.getCompanyName());
			txtProduct2.setText(inventory.getProduct());
			txtStock2.setText(String.valueOf(inventory.getStock()));
			txtPurchase2.setText(String.valueOf(inventory.getPurchase()));
			txtType2.setText(inventory.getType());
			txtColor2.setText(inventory.getColor());
			txtSize2.setText(inventory.getSize());

		} catch (Exception e1) {
		}
	}

	private void handleTvinventory3MouseClicked(MouseEvent e) {
		tvInventoryIndex = tvInventory3.getSelectionModel().getSelectedIndex();

		try {
			Inventory inventory = obList.get(tvInventoryIndex);

			txtProductNumber3.setText(String.valueOf(inventory.getProductNumber()));
			txtCompany3.setText(inventory.getCompanyName());
			txtProduct3.setText(inventory.getProduct());
			txtStock3.setText(String.valueOf(inventory.getStock()));
			txtSell2.setText(String.valueOf(inventory.getSell()));
			txtType3.setText(inventory.getType());
			txtColor3.setText(inventory.getColor());
			txtSize3.setText(inventory.getSize());
		} catch (Exception e1) {
		}
	}

	// 재고 리스트, DB 에서 해당 목록 삭제
	private void handleBtnDeleteAction(ActionEvent e) {

		try {
			InventoryDAO inventoryDAO = new InventoryDAO();
			Inventory inventory = obList.get(tvInventoryIndex);
			String no = inventory.getProductNumber();

			int returnValue = inventoryDAO.getInventoryDelete(no);

			obList.remove(tvInventoryIndex);

			txtProduct1.clear();
			txtProductNumber1.clear();
			txtStock1.clear();
			txtPurchase1.clear();
			txtSell1.clear();
			cmbType4.getSelectionModel().clearSelection();
			cmbColor4.getSelectionModel().clearSelection();
			cmbSize4.getSelectionModel().clearSelection();
			cmbCompany4.getSelectionModel().clearSelection();
		} catch (ArrayIndexOutOfBoundsException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setHeaderText("재고 선택 오류");
			alert.setContentText("삭제할 재고를 선택하시오");
			alert.showAndWait();
		}

		obList.clear();
		totalLoadList();
	}

	// DB에서 재고 가져오기
	private void totalLoadList() {
		btnInventory.setDefaultButton(true);// inventory 버튼 표시를 위한 것
		InventoryDAO inventoryDAO = new InventoryDAO();
		arrayList = inventoryDAO.getTotalLoadList();
		if (arrayList == null) {
			return;
		}
		obList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			Inventory iv = arrayList.get(i);
			obList.add(iv);
		}
	}

	// 검색 조건 및 리스트 초기화버튼 (재고 화면)
	private void handleBtnReset1Action(ActionEvent e) {
		tvInventoryIndex = -1;

		txtSearch1.clear();

		txtProduct1.clear();
		txtProductNumber1.clear();
		txtStock1.clear();
		txtPurchase1.clear();
		txtSell1.clear();

		cmbCompany4.getSelectionModel().clearSelection();
		cmbType4.getSelectionModel().clearSelection();
		cmbColor4.getSelectionModel().clearSelection();
		cmbSize4.getSelectionModel().clearSelection();

		obList.clear();
		totalLoadList();
		handleRdoRegisterAction();
		rdoRegister.setSelected(true);
	}

	// 검색 조건 및 리스트 초기화버튼 (주문 화면)
	private void handleBtnReset2Action(ActionEvent e) {
		tvInventoryIndex = -1;

		txtSearch2.clear();

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

	// 검색 조건 및 리스트 초기화버튼 (판매 화면)
	private void handleBtnReset3Action(ActionEvent e) {
		tvInventoryIndex = -1;

		txtSearch3.clear();

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

	// 콤보박스 리스트
	// 업체
	private void companyComboBoxList() {
		CompanyDAO companyDAO = new CompanyDAO();

		ArrayList<CompanyModel> arrayList = companyDAO.inventoryStageCompanyComboboxListUp(production);
		String cName = null;
		if (arrayList == null) {
			return;
		}

		for (int i = 0; i < arrayList.size(); i++) {
			CompanyModel com = arrayList.get(i);
			obList5.add(com);

			obList1.add(com.getCompany_name());
		}

		arrayList2 = companyDAO.inventoryStageCompanyComboboxListUp(production2);
		String cName2 = null;
		if (arrayList2 == null) {
			return;
		}
		for (int i = 0; i < arrayList2.size(); i++) {
			CompanyModel com = arrayList2.get(i);
			cName2 = com.getCompany_name();
			obList3.add(cName2);
		}

		cmbCompany4.setPromptText("업체 선택");
		cmbCompany4.setItems(obList1);

		cmbCompany5.setPromptText("판매처 선택");
		cmbCompany5.setItems(obList3);
	}

	// 재고 리스트 테이블
	private void tvInventory1Column() {
		TableColumn colPnumber = new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("업체명");
		colCompany.setPrefWidth(140.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("제품명");
		colProduct.setPrefWidth(180.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("재고");
		colStock.setMaxWidth(100);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colPurchase = new TableColumn("구입가");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));

		TableColumn colSell = new TableColumn("판매가");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));

		TableColumn colType = new TableColumn("종류");
		colType.setMaxWidth(90);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory1.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colPurchase, colSell, colType,
				colSize, colColor);

		tvInventory1.setItems(obList);

	}

	// 구매화면 리스트
	private void tvInventory2Column() {
		TableColumn colPnumber = new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("업체명");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("제품명");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("재고");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colPurchase = new TableColumn("구입가");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));

		TableColumn colType = new TableColumn("종류");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory2.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colPurchase, colType, colSize,
				colColor);

		tvInventory2.setItems(obList);

	}

	// 판매화면 리스트
	private void tvInventory3Column() {
		TableColumn colPnumber = new TableColumn("제품코드");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("업체명");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("제품명");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("재고");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colSell = new TableColumn("판매가");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));

		TableColumn colType = new TableColumn("종류");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("사이즈");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("색상");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory3.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colSell, colType, colSize,
				colColor);

		tvInventory3.setItems(obList);

	}

}
