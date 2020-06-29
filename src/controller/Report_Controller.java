package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.AdminMain;
import application.InventoryMain;
import application.TradeListMain;
import dao.CompanyDAO;
import dao.TradeListDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CompanyModel;
import model.TradeListModel;

public class Report_Controller implements Initializable {
	// main 페이지의 버튼
	@FXML
	Button btnCompany;
	@FXML
	Button btnInventory;
	@FXML
	Button btnTradeList;
	@FXML
	Button btnReport;
	@FXML
	Button btnAdmin;
	@FXML
	Button btnLogout;

	// 거래내역 페이지의 버튼
	@FXML
	Label lblMonth;
	@FXML
	Label lblTotalSales;
	@FXML
	LineChart chartTotalSales;
	@FXML
	BarChart chartProduct;
	@FXML
	BarChart chartSell;
	@FXML
	Label lblWon;
	@FXML
	Label lblYear;
	@FXML
	ComboBox cmbYear;
	@FXML
	ComboBox cmbMonth;

	// 멤버 변수
	public Stage reportStage;// 리포트 stage
	private Stage newStage;// logout 용
	private ObservableList<TradeListModel> obsList;// 매출이 발생한 연도, 월
	private ObservableList<String> obsListYear;// combobox 초기화
	private ObservableList<String> obsListMonth;// combobox 초기화
	private ArrayList<TradeListModel> arrayList;
	private XYChart.Series series;//차트 시리즈 함수안에서 선언하면 클릭할 때마다 계속 생성함
//	private String month;
	
	// 기본 생성자
	public Report_Controller() {
		this.obsList = FXCollections.observableArrayList();
		this.obsListYear = FXCollections.observableArrayList();
		this.obsListMonth = FXCollections.observableArrayList();
		this.newStage = new Stage();
		this.arrayList = new ArrayList<TradeListModel>();
		this.series = new XYChart.Series();
//		this.month = String.valueOf(new Date().getMonth()+1);
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 초기 세팅 함수
		comboboxInit();// 콤보박스 초기값
//		chartInit();//차트 초기화
		cmbYear.setOnAction(event -> handleLineChart());// 연매출 라인차트 이벤트
		cmbMonth.setOnAction(event -> handleMonthSalesAction());// 월 매출

		// 화면 이동
//		btnLogout.setOnAction(event -> handleBtnLogoutAction());// 로그인 화면으로 이동
		btnAdmin.setOnAction(event -> handleBtnAdminAction());// 관리자 화면으로 이동
		btnCompany.setOnAction(event -> handleBtnCompanyAction());// 업체관리 화면으로 이동
		btnInventory.setOnAction(event -> handleBtnInventoryAction());// 재고관리 화면으로 이동
		btnTradeList.setOnAction(event -> handleBtnTradeAction());// 거래내역 화면으로 이동
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록
	
	

	// 콤보박스 초기값
	private void comboboxInit() {
		btnReport.setDefaultButton(true);// btnReport 버튼 표시를 위한 것
		arrayList = new TradeListDAO().reportCombocox();
		List listYear = new ArrayList<String>();// 중복값 제거용

		// arrayList에 있는 값을 year 와 month로 나눔
		if (arrayList.size() != 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsList.add(tm);
				String year = tm.getDate().substring(0, 4);
				listYear.add(year);
			}

			// 중복값 제거
			listYear = (List) listYear.stream().distinct().collect(Collectors.toList());

			// list에서 obslist로 변환
			for (int i = 0; i < listYear.size(); i++) {
				// arraylist 에서 date의 년도, 월을 구분하여 각각 obslist에 저장
				String s = (String) listYear.get(i);
				obsListYear.add(s);
			}

			// 값 저장
			cmbYear.setItems(obsListYear);

			// promptText 설정
			cmbYear.setPromptText("연도 선택");
			cmbMonth.setPromptText("월 선택");
			
			// 년도가 먼저 선택되게 하기 위한 장치
			cmbMonth.setDisable(true);
			
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}
	}

	// 연매출 라인차트 이벤트
	private void handleLineChart() {
		// 년도가 먼저 선택되게 하기 위한 장치
		cmbMonth.setDisable(false);

		// 선택한 연도의 text 받음
		String selectYear = cmbYear.getSelectionModel().getSelectedItem().toString();

		// 연도 라벨 : 선택한 연도로 변경
		lblYear.setText(selectYear);
		
		// 선택 연도 중 매출이 발생한 월만 콤보박스에 초기화
		obsListMonth.clear();
		for (int i = 0; i < obsList.size(); i ++) {
			TradeListModel tmMonth = obsList.get(i);
			String month = tmMonth.getDate().substring(0, 4);
			if (month.equals(selectYear)) {
				obsListMonth.add(tmMonth.getDate().substring(5, 7));
			}
		}
		cmbMonth.setItems(obsListMonth);

		// 라인차트 총매출, 날짜의 달
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxYearSales(selectYear);
		ObservableList obsListTrade = FXCollections.observableArrayList();
		series.setName("월별 매출 그래프");
		
		// 차트에 값 넣기
		for (int i = 0; i < arrayList.size(); i++) {
			TradeListModel tm = arrayList.get(i);
			String month = tm.getDate().substring(5, 7);
			int totalSales = Integer.parseInt(tm.getTotal_price());
			obsListTrade.add(new XYChart.Data(month, totalSales));
		}
		series.setData(obsListTrade);
		chartTotalSales.getData().add(series);
		
		// 차트 설정
		chartTotalSales.getXAxis().setTickLabelRotation(10);
	}

	// 월 매출
	private void handleMonthSalesAction() {
		// 월,년 받아오기
		String month = cmbMonth.getSelectionModel().getSelectedItem().toString();
		String year = cmbYear.getSelectionModel().getSelectedItem().toString();
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxMonthSales(year, month);
		
		// 라벨에 월 변경
		lblTotalSales.setText(String.format("%,d", Integer.parseInt(arrayList.get(0).getTotal_price())));
		lblTotalSales.setTextAlignment(TextAlignment.CENTER);
	
		//콤보박스 선택시 월 변경
		lblMonth.setText(month);
	}
//	//차트 초기화
//	private void chartInit() {
//		new TradeListDAO().
//	}
	
	// 재고 관리 화면으로 이동
	private void handleBtnInventoryAction() {
		try {
			new InventoryMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 업체 관리 화면으로 이동
	private void handleBtnCompanyAction() {
		try {
			new TradeListMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 거래내역 화면으로 이동
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 관리자 화면으로 이동
	private void handleBtnAdminAction() {
		try {
			new AdminMain().start(newStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 로그인 화면으로 이동
//	private void handleBtnLogoutAction() {
//		try {
//			new LoginMain().start(companyEditStage);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		primaryStage.close();
//	}
}