package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.CompanyDAO;
import dao.TradeListDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CompanyModel;
import model.TradeListModel;

public class TradeList_Management_Controller implements Initializable {
	// main 페이지의 버튼
	@FXML
	Button btnCompany;
	@FXML
	Button btnInventory;
	@FXML
	Button btnTrade;
	@FXML
	Button btnReport;
	@FXML
	Button btnAdmin;
	@FXML
	Button btnLogout;

	// 거래내역 페이지의 버튼
	@FXML
	Button btnSearch;
	@FXML
	Button btnRegisteration;
	@FXML
	Button btnDelete;
	@FXML
	TextField txtSearch;
	@FXML
	TableView tlvTradeList;
	@FXML
	Button btnSellTab;
	@FXML
	Button btnPurchaseTab;
	@FXML
	Button btnReset;
	@FXML
	DatePicker dpkStart;

	// 멤버 변수
	public Stage tradeStage;// 업체관리 stage
	private String purchase = "주문";// tableView 하나로 구현하기 위해서 생산업체리스트와 소비업체 리스트를 선택에 따라 list up하기 위한 변수
	private String sell = "판매";
	private String nowPurchaseOrSell = null;// 현재 업체관리 tableView에 나타나고 있는 리스트가 소비업체인지/생산업체인지 확인하는 변수
	private ObservableList<TradeListModel> obsListTrade;

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 초기 세팅 함수
		tableViewColumnInit();// tableView 칼럼 초기화 함수
		tableViewtradeListInit(purchase);// '거래내역' 버튼 클릭 시 tableView에 생산업체 초기화
		searchAndDatePickerReset();// 리셋(초기화) 함수 (콤보박스, 검색창)
		setTextFormatEditRegistry();// 등록 및 수정 텍스트 포멧

		// 버튼 이벤트
		btnPurchaseTab.setOnAction(event -> tableViewtradeListInit(purchase)); // 생산업체 버튼 클릭 시 tableView에 생산업체 초기화
		btnSellTab.setOnAction(event -> tableViewtradeListInit(sell)); // 소비업체 버튼 클릭 시 tableView에 소비업체 초기화
		btnReset.setOnAction(event -> handelBtnSearchAction()); // 리셋(초기화) 버튼 이벤트
		dpkStart.setOnAction(event -> handleBtnDateSearchAction()); // 선택 날짜 기준 내림차순
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록

	// tableView 칼럼 초기값 입력 함수
	private void tableViewColumnInit() {
		TableColumn colDate = new TableColumn("거래 날짜");
		colDate.setMinWidth(150);
		colDate.setStyle("-fx-alignment: CENTER;");
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colCompany = new TableColumn("업체명");
		colCompany.setMinWidth(300);
		colCompany.setStyle("-fx-alignment: CENTER;");
		colCompany.setCellValueFactory(new PropertyValueFactory("companyName"));

		TableColumn colProductName = new TableColumn("제품명");
		colProductName.setMinWidth(300);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductNum = new TableColumn("제품코드");
		colProductNum.setMinWidth(120);
		colProductNum.setStyle("-fx-alignment: CENTER;");
		colProductNum.setCellValueFactory(new PropertyValueFactory("productNumber"));

		TableColumn colQuantity = new TableColumn("거래 수량");
		colQuantity.setMinWidth(90);
		colQuantity.setStyle("-fx-alignment: CENTER;");
		colQuantity.setCellValueFactory(new PropertyValueFactory("order_quantity"));

		TableColumn colPrice = new TableColumn("총금액");
		colPrice.setMinWidth(180);
		colPrice.setStyle("-fx-alignment: CENTER;");
		colPrice.setCellValueFactory(new PropertyValueFactory("total_price"));

		tlvTradeList.getColumns().addAll(colDate, colCompany, colProductName, colProductNum, colQuantity, colPrice);
	}

	// '업체관리' 버튼 클릭 시 tableView에 생산업체 초기화
	private void tableViewtradeListInit(String purchaseOrSell) {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().tradeListUp(purchaseOrSell);
		obsListTrade = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayList.size() != 0) {
			obsListTrade.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsListTrade.add(tm);
			}
			tlvTradeList.setItems(obsListTrade);
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}
		// 현재 tableView 에 보여지는 리스트가 소비업체인가, 생산업체인가
		TradeListModel tm = obsListTrade.get(0);
		nowPurchaseOrSell = tm.getPurchaseOrSell();

		// 검색 및 데이트피커 리셋
		searchAndDatePickerReset();

		// button 선택 default 적용
		if (purchaseOrSell == purchase) {
			btnPurchaseTab.setDefaultButton(true);
			btnSellTab.setDefaultButton(false);
		} else {
			btnSellTab.setDefaultButton(true);
			btnPurchaseTab.setDefaultButton(false);
		}
	}
	
	// 리셋 함수 (검색창)
	private void searchAndDatePickerReset() {
		txtSearch.clear();
		dpkStart.setValue(null);
	}

	// 등록 및 수정 텍스트 포멧
	private void setTextFormatEditRegistry() {
		// 사업자 등록번호 10자리로 제한

	}

	// 리셋(초기화) 버튼 이벤트
	private void handelBtnSearchAction() {
		searchAndDatePickerReset();
		tableViewtradeListInit(nowPurchaseOrSell);
	}

	// 선택 날짜 기준 내림차순
	private void handleBtnDateSearchAction() {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().FindTradeListDate(dpkStart.getValue().toString(),
				nowPurchaseOrSell);
		obsListTrade = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayList.size() != 0) {
			obsListTrade.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsListTrade.add(tm);
			}
			tlvTradeList.setItems(obsListTrade);
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}

		// 현재 tableView 에 보여지는 리스트가 판매인가, 주문인가
		TradeListModel tm = obsListTrade.get(0);
		nowPurchaseOrSell = tm.getPurchaseOrSell();
	}
}