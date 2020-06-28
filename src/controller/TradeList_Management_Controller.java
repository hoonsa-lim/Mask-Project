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
	// main �������� ��ư
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

	// �ŷ����� �������� ��ư
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

	// ��� ����
	public Stage tradeStage;// ��ü���� stage
	private String purchase = "�ֹ�";// tableView �ϳ��� �����ϱ� ���ؼ� �����ü����Ʈ�� �Һ��ü ����Ʈ�� ���ÿ� ���� list up�ϱ� ���� ����
	private String sell = "�Ǹ�";
	private String nowPurchaseOrSell = null;// ���� ��ü���� tableView�� ��Ÿ���� �ִ� ����Ʈ�� �Һ��ü����/�����ü���� Ȯ���ϴ� ����
	private ObservableList<TradeListModel> obsListTrade;

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�̺�Ʈ ���
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �ʱ� ���� �Լ�
		tableViewColumnInit();// tableView Į�� �ʱ�ȭ �Լ�
		tableViewtradeListInit(purchase);// '�ŷ�����' ��ư Ŭ�� �� tableView�� �����ü �ʱ�ȭ
		searchAndDatePickerReset();// ����(�ʱ�ȭ) �Լ� (�޺��ڽ�, �˻�â)
		setTextFormatEditRegistry();// ��� �� ���� �ؽ�Ʈ ����

		// ��ư �̺�Ʈ
		btnPurchaseTab.setOnAction(event -> tableViewtradeListInit(purchase)); // �����ü ��ư Ŭ�� �� tableView�� �����ü �ʱ�ȭ
		btnSellTab.setOnAction(event -> tableViewtradeListInit(sell)); // �Һ��ü ��ư Ŭ�� �� tableView�� �Һ��ü �ʱ�ȭ
		btnReset.setOnAction(event -> handelBtnSearchAction()); // ����(�ʱ�ȭ) ��ư �̺�Ʈ
		dpkStart.setOnAction(event -> handleBtnDateSearchAction()); // ���� ��¥ ���� ��������
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�ڵ鷯 ���

	// tableView Į�� �ʱⰪ �Է� �Լ�
	private void tableViewColumnInit() {
		TableColumn colDate = new TableColumn("�ŷ� ��¥");
		colDate.setMinWidth(150);
		colDate.setStyle("-fx-alignment: CENTER;");
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colCompany = new TableColumn("��ü��");
		colCompany.setMinWidth(300);
		colCompany.setStyle("-fx-alignment: CENTER;");
		colCompany.setCellValueFactory(new PropertyValueFactory("companyName"));

		TableColumn colProductName = new TableColumn("��ǰ��");
		colProductName.setMinWidth(300);
		colProductName.setStyle("-fx-alignment: CENTER;");
		colProductName.setCellValueFactory(new PropertyValueFactory("productName"));

		TableColumn colProductNum = new TableColumn("��ǰ�ڵ�");
		colProductNum.setMinWidth(120);
		colProductNum.setStyle("-fx-alignment: CENTER;");
		colProductNum.setCellValueFactory(new PropertyValueFactory("productNumber"));

		TableColumn colQuantity = new TableColumn("�ŷ� ����");
		colQuantity.setMinWidth(90);
		colQuantity.setStyle("-fx-alignment: CENTER;");
		colQuantity.setCellValueFactory(new PropertyValueFactory("order_quantity"));

		TableColumn colPrice = new TableColumn("�ѱݾ�");
		colPrice.setMinWidth(180);
		colPrice.setStyle("-fx-alignment: CENTER;");
		colPrice.setCellValueFactory(new PropertyValueFactory("total_price"));

		tlvTradeList.getColumns().addAll(colDate, colCompany, colProductName, colProductNum, colQuantity, colPrice);
	}

	// '��ü����' ��ư Ŭ�� �� tableView�� �����ü �ʱ�ȭ
	private void tableViewtradeListInit(String purchaseOrSell) {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().tradeListUp(purchaseOrSell);
		obsListTrade = FXCollections.observableArrayList();
		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		if (arrayList.size() != 0) {
			obsListTrade.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsListTrade.add(tm);
			}
			tlvTradeList.setItems(obsListTrade);
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}
		// ���� tableView �� �������� ����Ʈ�� �Һ��ü�ΰ�, �����ü�ΰ�
		TradeListModel tm = obsListTrade.get(0);
		nowPurchaseOrSell = tm.getPurchaseOrSell();

		// �˻� �� ����Ʈ��Ŀ ����
		searchAndDatePickerReset();

		// button ���� default ����
		if (purchaseOrSell == purchase) {
			btnPurchaseTab.setDefaultButton(true);
			btnSellTab.setDefaultButton(false);
		} else {
			btnSellTab.setDefaultButton(true);
			btnPurchaseTab.setDefaultButton(false);
		}
	}
	
	// ���� �Լ� (�˻�â)
	private void searchAndDatePickerReset() {
		txtSearch.clear();
		dpkStart.setValue(null);
	}

	// ��� �� ���� �ؽ�Ʈ ����
	private void setTextFormatEditRegistry() {
		// ����� ��Ϲ�ȣ 10�ڸ��� ����

	}

	// ����(�ʱ�ȭ) ��ư �̺�Ʈ
	private void handelBtnSearchAction() {
		searchAndDatePickerReset();
		tableViewtradeListInit(nowPurchaseOrSell);
	}

	// ���� ��¥ ���� ��������
	private void handleBtnDateSearchAction() {
		ArrayList<TradeListModel> arrayList = new TradeListDAO().FindTradeListDate(dpkStart.getValue().toString(),
				nowPurchaseOrSell);
		obsListTrade = FXCollections.observableArrayList();
		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		if (arrayList.size() != 0) {
			obsListTrade.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				TradeListModel tm = arrayList.get(i);
				obsListTrade.add(tm);
			}
			tlvTradeList.setItems(obsListTrade);
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}

		// ���� tableView �� �������� ����Ʈ�� �Ǹ��ΰ�, �ֹ��ΰ�
		TradeListModel tm = obsListTrade.get(0);
		nowPurchaseOrSell = tm.getPurchaseOrSell();
	}
}