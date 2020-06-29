package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.InventoryMain;
import application.TradeListMain;
import dao.CompanyDAO;
import dao.TradeListDAO;
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
	public ObservableList<TradeListModel> obsList;// combobox init
	public ObservableList<String> obsListYear;// combobox init
	public ObservableList<String> obsListMonth;// combobox init

	public Report_Controller() {
		this.obsList = FXCollections.observableArrayList();
		this.obsListYear = FXCollections.observableArrayList();
		this.obsListMonth = FXCollections.observableArrayList();

	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 초기 세팅 함수
		comboboxInit();// 콤보박스 초기값
		cmbYear.setOnAction(event -> handleLineChart());// 연매출 라인차트 이벤트
		cmbMonth.setOnAction(event -> handleMonthSalesAction());// 월 매출

		// 버튼 이벤트
		btnInventory.setOnAction(event -> handleBtnInventoryAction());
		btnCompany.setOnAction(event -> handleBtnCompanyAction());
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록
	// 콤보박스 초기값
	private void comboboxInit() {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocox();
		List listYear = new ArrayList<String>();// 중복값 제거용
		List listMonth = new ArrayList<String>();// 중복값 제거용

		// arrayList에 있는 값을 year 와 month로 나눔
		if (arrayList.size() != 0) {
			obsList.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				String year = tm.getDate().substring(0, 4);
				String month = tm.getDate().substring(5, 7);
				listYear.add(year);
				listMonth.add(month);
			}

			// 중복값 제거
			listYear = (List) listYear.stream().distinct().collect(Collectors.toList());
			listMonth = (List) listMonth.stream().distinct().collect(Collectors.toList());

			// list에서 obslist로 변환
			for (int i = 0; i < listYear.size(); i++) {
				// arraylist 에서 date의 년도, 월을 구분하여 각각 obslist에 저장
				String s = (String) listYear.get(i);
				obsListYear.add(s);
			}
			for (int i = 0; i < listMonth.size(); i++) {
				// arraylist 에서 date의 년도, 월을 구분하여 각각 obslist에 저장
				String s = (String) listMonth.get(i);
				obsListMonth.add(s);
			}

			// 값 저장
			cmbYear.setItems(obsListYear);
			cmbMonth.setItems(obsListMonth);

			// promptText 설정
			cmbYear.setPromptText("연도 선택");
			cmbMonth.setPromptText("월 선택");

			// 년도가 먼저 선택되게 학 위한 장치
			cmbMonth.setDisable(true);
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}
	}

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

	// 연매출 라인차트 이벤트
	private void handleLineChart() {
		// 년도가 먼저 선택되게 학 위한 장치
		cmbMonth.setDisable(false);
//		Data<String, String> cd = new XYChart.Data<String, String>("1월", "1111111");
//		ObservableList<XYChart.Data> od = FXCollections.observableArrayList();
//		od.add(cd);
//		chartTotalSales.setData(od);

		// 선택한 연도의 text 받음
		String selectYear = cmbYear.getSelectionModel().getSelectedItem().toString();
		// 연도 라벨 : 선택한 연도로 변경
		lblYear.setText(selectYear);

		// 라인차트 총매출, 날짜의 달
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxYearSales(selectYear);
		ObservableList<TradeListModel> obsListTrade = FXCollections.observableArrayList();

		// 차트의 월
		XYChart.Series month1 = new XYChart.Series();
		month1.setName("1");
		XYChart.Series month2 = new XYChart.Series();
		month2.setName("2");
		XYChart.Series month3 = new XYChart.Series();
		month3.setName("3");
		XYChart.Series month4 = new XYChart.Series();
		month4.setName("4");
		XYChart.Series month5 = new XYChart.Series();
		month5.setName("5");
		XYChart.Series month6 = new XYChart.Series();
		month6.setName("6");
		XYChart.Series month7 = new XYChart.Series();
		month7.setName("7");
		XYChart.Series month8 = new XYChart.Series();
		month8.setName("8");
		XYChart.Series month9 = new XYChart.Series();
		month9.setName("9");
		XYChart.Series month10 = new XYChart.Series();
		month10.setName("10");
		XYChart.Series month11 = new XYChart.Series();
		month11.setName("11");
		XYChart.Series month12 = new XYChart.Series();
		month12.setName("12");
		ObservableList<XYChart.Series> obsChartMonth = FXCollections.observableArrayList();
		obsChartMonth.addAll(month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11,
				month12);

		// arrayList에 있는 값을 obsList에 입력한다.
//		if (arrayList.size() != 0) {
//			obsListTrade.clear();
//			for (int i = 0; i < arrayList.size(); i++) {
//				TradeListModel tm = arrayList.get(i);
//				String month = tm.getDate().substring(5, 7);//객체에서 월만 가져옴
//				XYChart.Series r = obsChartMonth.get(Integer.parseInt(month));//차트 x축의 객체중 arraylist에서 꺼낸 객체와 같은 것만 저장
//				r.
//				obsListTrade.add(tm);
//			}

//			// 먼저 차트는 XYchart 시리즈를 만든다. (영어)
//			ObservableList englishList = FXCollections.observableArrayList();
//			for (int i = 0; i < obsList.size(); i++) {
//				
//				Student student = obsList.get(i);
//				englishList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
//			}
//			seriesEnglish.setData(englishList);
//			barChart.getData().add(seriesEnglish);
//
//			tlvTradeList.setItems(obsListTrade);
//		} else {
//			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
//		}

//		}
	}

	// 월 매출
	private void handleMonthSalesAction() {
		// 월,년 받아오기
		String month = cmbMonth.getSelectionModel().getSelectedItem().toString();
		String year = cmbYear.getSelectionModel().getSelectedItem().toString();
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxMonthSales(year, month);

		System.out.println();
		
		// 라벨에 월 변경
		lblTotalSales.setText(String.format("%,d", Integer.parseInt(arrayList.get(0).getTotal_price())));
		lblTotalSales.setTextAlignment(TextAlignment.LEFT);
		
	}
}