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
	// main �������� ��ư
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

	// �ŷ����� �������� ��ư
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

	// ��� ����
	public Stage reportStage;// ����Ʈ stage
	public ObservableList<TradeListModel> obsList;// combobox init
	public ObservableList<String> obsListYear;// combobox init
	public ObservableList<String> obsListMonth;// combobox init

	public Report_Controller() {
		this.obsList = FXCollections.observableArrayList();
		this.obsListYear = FXCollections.observableArrayList();
		this.obsListMonth = FXCollections.observableArrayList();

	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�̺�Ʈ ���
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �ʱ� ���� �Լ�
		comboboxInit();// �޺��ڽ� �ʱⰪ
		cmbYear.setOnAction(event -> handleLineChart());// ������ ������Ʈ �̺�Ʈ
		cmbMonth.setOnAction(event -> handleMonthSalesAction());// �� ����

		// ��ư �̺�Ʈ
		btnInventory.setOnAction(event -> handleBtnInventoryAction());
		btnCompany.setOnAction(event -> handleBtnCompanyAction());
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�ڵ鷯 ���
	// �޺��ڽ� �ʱⰪ
	private void comboboxInit() {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocox();
		List listYear = new ArrayList<String>();// �ߺ��� ���ſ�
		List listMonth = new ArrayList<String>();// �ߺ��� ���ſ�

		// arrayList�� �ִ� ���� year �� month�� ����
		if (arrayList.size() != 0) {
			obsList.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				String year = tm.getDate().substring(0, 4);
				String month = tm.getDate().substring(5, 7);
				listYear.add(year);
				listMonth.add(month);
			}

			// �ߺ��� ����
			listYear = (List) listYear.stream().distinct().collect(Collectors.toList());
			listMonth = (List) listMonth.stream().distinct().collect(Collectors.toList());

			// list���� obslist�� ��ȯ
			for (int i = 0; i < listYear.size(); i++) {
				// arraylist ���� date�� �⵵, ���� �����Ͽ� ���� obslist�� ����
				String s = (String) listYear.get(i);
				obsListYear.add(s);
			}
			for (int i = 0; i < listMonth.size(); i++) {
				// arraylist ���� date�� �⵵, ���� �����Ͽ� ���� obslist�� ����
				String s = (String) listMonth.get(i);
				obsListMonth.add(s);
			}

			// �� ����
			cmbYear.setItems(obsListYear);
			cmbMonth.setItems(obsListMonth);

			// promptText ����
			cmbYear.setPromptText("���� ����");
			cmbMonth.setPromptText("�� ����");

			// �⵵�� ���� ���õǰ� �� ���� ��ġ
			cmbMonth.setDisable(true);
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}
	}

	// ��� ���� ȭ������ �̵�
	private void handleBtnInventoryAction() {
		try {
			new InventoryMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ��ü ���� ȭ������ �̵�
	private void handleBtnCompanyAction() {
		try {
			new TradeListMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ������ ������Ʈ �̺�Ʈ
	private void handleLineChart() {
		// �⵵�� ���� ���õǰ� �� ���� ��ġ
		cmbMonth.setDisable(false);
//		Data<String, String> cd = new XYChart.Data<String, String>("1��", "1111111");
//		ObservableList<XYChart.Data> od = FXCollections.observableArrayList();
//		od.add(cd);
//		chartTotalSales.setData(od);

		// ������ ������ text ����
		String selectYear = cmbYear.getSelectionModel().getSelectedItem().toString();
		// ���� �� : ������ ������ ����
		lblYear.setText(selectYear);

		// ������Ʈ �Ѹ���, ��¥�� ��
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxYearSales(selectYear);
		ObservableList<TradeListModel> obsListTrade = FXCollections.observableArrayList();

		// ��Ʈ�� ��
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

		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
//		if (arrayList.size() != 0) {
//			obsListTrade.clear();
//			for (int i = 0; i < arrayList.size(); i++) {
//				TradeListModel tm = arrayList.get(i);
//				String month = tm.getDate().substring(5, 7);//��ü���� ���� ������
//				XYChart.Series r = obsChartMonth.get(Integer.parseInt(month));//��Ʈ x���� ��ü�� arraylist���� ���� ��ü�� ���� �͸� ����
//				r.
//				obsListTrade.add(tm);
//			}

//			// ���� ��Ʈ�� XYchart �ø�� �����. (����)
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
//			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
//		}

//		}
	}

	// �� ����
	private void handleMonthSalesAction() {
		// ��,�� �޾ƿ���
		String month = cmbMonth.getSelectionModel().getSelectedItem().toString();
		String year = cmbYear.getSelectionModel().getSelectedItem().toString();
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxMonthSales(year, month);

		System.out.println();
		
		// �󺧿� �� ����
		lblTotalSales.setText(String.format("%,d", Integer.parseInt(arrayList.get(0).getTotal_price())));
		lblTotalSales.setTextAlignment(TextAlignment.LEFT);
		
	}
}