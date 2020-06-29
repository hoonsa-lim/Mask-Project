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
	private Stage newStage;// logout ��
	private ObservableList<TradeListModel> obsList;// ������ �߻��� ����, ��
	private ObservableList<String> obsListYear;// combobox �ʱ�ȭ
	private ObservableList<String> obsListMonth;// combobox �ʱ�ȭ
	private ArrayList<TradeListModel> arrayList;
	private XYChart.Series series;//��Ʈ �ø��� �Լ��ȿ��� �����ϸ� Ŭ���� ������ ��� ������
//	private String month;
	
	// �⺻ ������
	public Report_Controller() {
		this.obsList = FXCollections.observableArrayList();
		this.obsListYear = FXCollections.observableArrayList();
		this.obsListMonth = FXCollections.observableArrayList();
		this.newStage = new Stage();
		this.arrayList = new ArrayList<TradeListModel>();
		this.series = new XYChart.Series();
//		this.month = String.valueOf(new Date().getMonth()+1);
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�̺�Ʈ ���
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �ʱ� ���� �Լ�
		comboboxInit();// �޺��ڽ� �ʱⰪ
//		chartInit();//��Ʈ �ʱ�ȭ
		cmbYear.setOnAction(event -> handleLineChart());// ������ ������Ʈ �̺�Ʈ
		cmbMonth.setOnAction(event -> handleMonthSalesAction());// �� ����

		// ȭ�� �̵�
//		btnLogout.setOnAction(event -> handleBtnLogoutAction());// �α��� ȭ������ �̵�
		btnAdmin.setOnAction(event -> handleBtnAdminAction());// ������ ȭ������ �̵�
		btnCompany.setOnAction(event -> handleBtnCompanyAction());// ��ü���� ȭ������ �̵�
		btnInventory.setOnAction(event -> handleBtnInventoryAction());// ������ ȭ������ �̵�
		btnTradeList.setOnAction(event -> handleBtnTradeAction());// �ŷ����� ȭ������ �̵�
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�ڵ鷯 ���
	
	

	// �޺��ڽ� �ʱⰪ
	private void comboboxInit() {
		btnReport.setDefaultButton(true);// btnReport ��ư ǥ�ø� ���� ��
		arrayList = new TradeListDAO().reportCombocox();
		List listYear = new ArrayList<String>();// �ߺ��� ���ſ�

		// arrayList�� �ִ� ���� year �� month�� ����
		if (arrayList.size() != 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsList.add(tm);
				String year = tm.getDate().substring(0, 4);
				listYear.add(year);
			}

			// �ߺ��� ����
			listYear = (List) listYear.stream().distinct().collect(Collectors.toList());

			// list���� obslist�� ��ȯ
			for (int i = 0; i < listYear.size(); i++) {
				// arraylist ���� date�� �⵵, ���� �����Ͽ� ���� obslist�� ����
				String s = (String) listYear.get(i);
				obsListYear.add(s);
			}

			// �� ����
			cmbYear.setItems(obsListYear);

			// promptText ����
			cmbYear.setPromptText("���� ����");
			cmbMonth.setPromptText("�� ����");
			
			// �⵵�� ���� ���õǰ� �ϱ� ���� ��ġ
			cmbMonth.setDisable(true);
			
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}
	}

	// ������ ������Ʈ �̺�Ʈ
	private void handleLineChart() {
		// �⵵�� ���� ���õǰ� �ϱ� ���� ��ġ
		cmbMonth.setDisable(false);

		// ������ ������ text ����
		String selectYear = cmbYear.getSelectionModel().getSelectedItem().toString();

		// ���� �� : ������ ������ ����
		lblYear.setText(selectYear);
		
		// ���� ���� �� ������ �߻��� ���� �޺��ڽ��� �ʱ�ȭ
		obsListMonth.clear();
		for (int i = 0; i < obsList.size(); i ++) {
			TradeListModel tmMonth = obsList.get(i);
			String month = tmMonth.getDate().substring(0, 4);
			if (month.equals(selectYear)) {
				obsListMonth.add(tmMonth.getDate().substring(5, 7));
			}
		}
		cmbMonth.setItems(obsListMonth);

		// ������Ʈ �Ѹ���, ��¥�� ��
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxYearSales(selectYear);
		ObservableList obsListTrade = FXCollections.observableArrayList();
		series.setName("���� ���� �׷���");
		
		// ��Ʈ�� �� �ֱ�
		for (int i = 0; i < arrayList.size(); i++) {
			TradeListModel tm = arrayList.get(i);
			String month = tm.getDate().substring(5, 7);
			int totalSales = Integer.parseInt(tm.getTotal_price());
			obsListTrade.add(new XYChart.Data(month, totalSales));
		}
		series.setData(obsListTrade);
		chartTotalSales.getData().add(series);
		
		// ��Ʈ ����
		chartTotalSales.getXAxis().setTickLabelRotation(10);
	}

	// �� ����
	private void handleMonthSalesAction() {
		// ��,�� �޾ƿ���
		String month = cmbMonth.getSelectionModel().getSelectedItem().toString();
		String year = cmbYear.getSelectionModel().getSelectedItem().toString();
		ArrayList<TradeListModel> arrayList = new TradeListDAO().reportCombocoxMonthSales(year, month);
		
		// �󺧿� �� ����
		lblTotalSales.setText(String.format("%,d", Integer.parseInt(arrayList.get(0).getTotal_price())));
		lblTotalSales.setTextAlignment(TextAlignment.CENTER);
	
		//�޺��ڽ� ���ý� �� ����
		lblMonth.setText(month);
	}
//	//��Ʈ �ʱ�ȭ
//	private void chartInit() {
//		new TradeListDAO().
//	}
	
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

	// �ŷ����� ȭ������ �̵�
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(reportStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ������ ȭ������ �̵�
	private void handleBtnAdminAction() {
		try {
			new AdminMain().start(newStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// �α��� ȭ������ �̵�
//	private void handleBtnLogoutAction() {
//		try {
//			new LoginMain().start(companyEditStage);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		primaryStage.close();
//	}
}