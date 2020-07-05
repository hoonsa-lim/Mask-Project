package controller;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.security.auth.callback.ConfirmationCallback;

import application.InventoryMain;
import application.LoginMain;
import application.ReportMain;
import application.TradeListMain;
import dao.CompanyDAO;
import dao.TradeListDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CompanyModel;
import model.TradeListModel;

public class Company_Management_Controller implements Initializable {
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
	Button btnLogout;

	// ��ü���� �������� ��ư
	@FXML
	Button btnSearch;
	@FXML
	Button btnRegisteration;
	@FXML
	Button btnEdit;
	@FXML
	Button btnDelete;
	@FXML
	ComboBox cmbContract;
	@FXML
	TextField txtSearch;
	@FXML
	TableView tlvCompanyList;
	@FXML
	Button btnReset;
	@FXML
	Tab btnProductTab;
	@FXML
	Tab btnSellTab;

	// ��� ����
	public Stage primaryStage;// stage
	private Stage companyEditStage;// ��ü ���� , logout â�� ����
	private String product;// tableView �ϳ��� �����ϱ� ���ؼ� �����ü����Ʈ�� �Һ��ü ����Ʈ�� ���ÿ� ���� list up�ϱ� ���� ����
	private String consum;
	private String nowListProdConsum;// ���� ��ü���� tableView�� ��Ÿ���� �ִ� ����Ʈ�� �Һ��ü����/�����ü���� Ȯ���ϴ� ����
	private int selectedTable;// tableView ���� ���� ������ ���ڵ��� index ����
	private ObservableList<CompanyModel> obsListCompany;

	// �⺻ ������
	public Company_Management_Controller() {
		this.product = "����";
		this.consum = "�Һ�";
		this.nowListProdConsum = null;
		this.selectedTable = -1;// -1�� ���� ������ default 0���� �Է��Ͽ� tableView�� �������� �ʾƵ� ����, ���� �� 0�� index�� ���� ������
		this.obsListCompany = null;
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�̺�Ʈ ���
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �ʱ� ���� �Լ�
		cmbContractInitialize();// �޺��ڽ� �ʱⰪ �Է� �Լ�
		tableViewColumnInit();// tableView Į�� �ʱ�ȭ �Լ�
		tableViewProdCompanyListInit(product);// '��ü����' ��ư Ŭ�� �� tableView�� �����ü �ʱ�ȭ
		searchAndComboboxReset();// ����(�ʱ�ȭ) �Լ� (�޺��ڽ�, �˻�â)

		// ��ư �̺�Ʈ
		btnProductTab.setOnSelectionChanged(event -> tableViewProdCompanyListInit(product)); // ����tab Ŭ�� �� tlv�� ���� �ʱ�ȭ
		btnSellTab.setOnSelectionChanged(event -> tableViewProdCompanyListInit(consum)); // �Һ�tab Ŭ�� �� tlv�� �Һ� �ʱ�ȭ
		btnEdit.setOnAction(event -> handleBtnEditAction()); // ���� ��ư �̺�Ʈ
		btnRegisteration.setOnAction(event -> handleRegisterationAction());// ��� ��ư �̺�Ʈ
		btnDelete.setOnAction(event -> handleDeleteAction());// ���� ��ư �̺�Ʈ
		cmbContract.setOnAction(event -> handleTableViewContractLineUpAction());// ��࿩�� �޺��ڽ��� ���� �̺�Ʈ
		btnSearch.setOnAction(event -> handleSearchTableViewAction());// �˻� ��ư �̺�Ʈ
		btnReset.setOnAction(event -> handleBtnSearchAction()); // ����(�ʱ�ȭ) ��ư �̺�Ʈ
		tlvCompanyList.setOnMouseClicked(event -> handleSaveClickTableIndexAction());// tableView ������ index ���

		// ȭ�� �̵�
		btnLogout.setOnAction(event -> handleBtnLogoutAction());// �α��� ȭ������ �̵�
		btnInventory.setOnAction(event -> handleBtnInventoryAction());// ������ ȭ������ �̵�
		btnTrade.setOnAction(event -> handleBtnTradeAction());// �ŷ����� ȭ������ �̵�
		btnReport.setOnAction(event -> handleBtnReportAction());// ���� ȭ������ �̵�
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�ڵ鷯 ���

	// �޺��ڽ� �ʱⰪ �Է� �Լ�
	private void cmbContractInitialize() {
		btnCompany.setDefaultButton(true);
		cmbContract.setItems(FXCollections.observableArrayList("�����", "��ุ��"));
		cmbContract.setPromptText("* ��� ���� ���� *");
	}

	// tableView Į�� �ʱⰪ �Է� �Լ�
	private void tableViewColumnInit() {
		TableColumn colNumber = new TableColumn("����� ��Ϲ�ȣ");
		colNumber.setMinWidth(120);
		colNumber.setStyle("-fx-alignment: CENTER;");
		colNumber.setCellValueFactory(new PropertyValueFactory("company_number"));

		TableColumn colName = new TableColumn("��ü��");
		colName.setMinWidth(200);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("company_name"));

		TableColumn colManager = new TableColumn("����� ��");
		colManager.setMinWidth(100);
		colManager.setStyle("-fx-alignment: CENTER;");
		colManager.setCellValueFactory(new PropertyValueFactory("manager"));

		TableColumn colPhone = new TableColumn("����ó");
		colPhone.setMinWidth(150);
		colPhone.setStyle("-fx-alignment: CENTER;");
		colPhone.setCellValueFactory(new PropertyValueFactory("manager_phone"));

		TableColumn colContract = new TableColumn("��࿩��");
		colContract.setMinWidth(100);
		colContract.setStyle("-fx-alignment: CENTER;");
		colContract.setCellValueFactory(new PropertyValueFactory("contract"));

		TableColumn colAddress = new TableColumn("�ּ�");
		colAddress.setMinWidth(500);
		colAddress.setStyle("-fx-alignment: CENTER;");
		colAddress.setCellValueFactory(new PropertyValueFactory("address"));

		tlvCompanyList.getColumns().addAll(colNumber, colName, colManager, colPhone, colContract, colAddress);
	}

	// '��ü����' ��ư Ŭ�� �� tableView�� �����ü �ʱ�ȭ
	private void tableViewProdCompanyListInit(String productOrConsum) {
		ArrayList<CompanyModel> arrayList = new CompanyDAO().companyListUp(productOrConsum);
		obsListCompany = FXCollections.observableArrayList();
		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		if (arrayList.size() != 0) {
			obsListCompany.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				CompanyModel cm = arrayList.get(i);
				obsListCompany.add(cm);
			}
			tlvCompanyList.setItems(obsListCompany);
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}

		// ���� tableView �� �������� ����Ʈ�� �Һ��ü�ΰ�, �����ü�ΰ�
		CompanyModel cm = obsListCompany.get(0);
		nowListProdConsum = cm.getProduction_consumption();

		// �˻� �� �޺��ڽ� ����
		searchAndComboboxReset();
	}

	// ���� �Լ� (�޺��ڽ�, �˻�â)
	private void searchAndComboboxReset() {
		txtSearch.clear();
		cmbContract.getSelectionModel().clearSelection();
	}

	// ���� ��ư �̺�Ʈ
	private void handleBtnEditAction() {
		// ��ü ��� â �ε�
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/view/company_edit.fxml"));
		Parent companyEditRoot = null;
		try {
			companyEditRoot = fxmlloader.load();
		} catch (IOException e) {
		}

		// ���� â ��ü �������� 10��
		Button btnSave = (Button) companyEditRoot.lookup("#btnSave");
		Button btnClose = (Button) companyEditRoot.lookup("#btnClose");
		Button btnCheck = (Button) companyEditRoot.lookup("#btnCheck");
		Button btnNumClear = (Button) companyEditRoot.lookup("#btnNumClear");
		Button btnNameClear = (Button) companyEditRoot.lookup("#btnNameClear");
		Button btnMangerNameClear = (Button) companyEditRoot.lookup("#btnMangerNameClear");
		Button btnPhoneClear = (Button) companyEditRoot.lookup("#btnPhoneClear");
		Button btnAdressClear = (Button) companyEditRoot.lookup("#btnAdressClear");
		TextField txtName = (TextField) companyEditRoot.lookup("#txtName");
		TextField txtNumber = (TextField) companyEditRoot.lookup("#txtNumber");
		TextField txtManagerName = (TextField) companyEditRoot.lookup("#txtManagerName");
		TextField txtPhone = (TextField) companyEditRoot.lookup("#txtPhone");
		TextField txtAdress = (TextField) companyEditRoot.lookup("#txtAdress");
		RadioButton rdoContract = (RadioButton) companyEditRoot.lookup("#rdoContract");
		RadioButton rdoEndContract = (RadioButton) companyEditRoot.lookup("#rdoEndContract");

		// ��ü��, ����� ��Ϲ�ȣ ��Ȱ��ȭ
		txtName.setDisable(true);
		txtNumber.setDisable(true);
		btnCheck.setDisable(true);
		btnNameClear.setDisable(true);
		btnNumClear.setDisable(true);

		// radioButton �׷����
		ToggleGroup contractGroup = new ToggleGroup();
		rdoContract.setToggleGroup(contractGroup);
		rdoEndContract.setToggleGroup(contractGroup);

		// tableView�� ���õ� ���ڵ带 textField�� ��������
		CompanyModel cm = null;
		try {
			cm = obsListCompany.get(selectedTable);
		} catch (ArrayIndexOutOfBoundsException e) {
			Alert alert = new Alert(AlertType.WARNING, "ȭ�鿡 ���̴� ǥ���� ������ ���ϴ� ���� �������ּ���");
			alert.setHeaderText("������ ��ü�� �������ּ���");
			alert.setTitle("���� �ȳ�");
			alert.showAndWait();
			return;
		}
		// ����â���� �Է��� �� model�� ����
		txtNumber.setText(cm.getCompany_number());
		txtName.setText(cm.getCompany_name());
		txtManagerName.setText(cm.getManager());
		txtPhone.setText(cm.getManager_phone());
		txtAdress.setText(cm.getAddress());
		if (cm.getContract().equals(rdoContract.getText())) {
			rdoContract.setSelected(true);
		} else {
			rdoEndContract.setSelected(true);
		}

		// ���� ��ư �̺�Ʈ
		btnSave.setOnAction(event -> {
			CompanyModel editCm = new CompanyModel(txtNumber.getText(), txtName.getText(), txtManagerName.getText(),
					txtPhone.getText(), ((RadioButton) (contractGroup.getSelectedToggle())).getText(),
					txtAdress.getText(), nowListProdConsum);
			int result = new CompanyDAO().CompanyEdit(editCm);
			tableViewProdCompanyListInit(nowListProdConsum);
			companyEditStage.close();
		});

		// ������ ����� ��ư
		btnMangerNameClear.setOnAction(event -> txtManagerName.clear());
		btnPhoneClear.setOnAction(event -> txtPhone.clear());
		btnAdressClear.setOnAction(event -> txtAdress.clear());

		// �ݱ� ��ư �̺�Ʈ
		btnClose.setOnAction(event -> companyEditStage.close());

		// �Է°� ���伳��
		// ����� ��Ϲ�ȣ 10�ڸ�
		DecimalFormat cmNumber = new DecimalFormat("##########");
		txtNumber.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 11) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
		// ��ü�� 40�ڸ�
		txtName.setOnKeyTyped(event -> {
			int maxCharacters = 39;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// ����ڸ� 4�ڸ�
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 3;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// ����ó 11�ڸ�
		DecimalFormat cmPhoneNumber = new DecimalFormat("###########");
		txtPhone.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = cmPhoneNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 12) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		// �ּ� 50�ڸ�
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 49;
			if (txtAdress.getText().length() > maxCharacters)
				event.consume();
		});
		// stage ����
		companyEditStage = new Stage();
		Scene scene = new Scene(companyEditRoot);
		companyEditStage.setScene(scene);
		companyEditStage.initOwner(primaryStage);
		companyEditStage.initModality(Modality.WINDOW_MODAL);
		companyEditStage.setTitle("��ü ����");
		companyEditStage.show();
	}

	// ��� ��ư �̺�Ʈ
	private void handleRegisterationAction() {
		// ��ü ��� â �ε�
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/view/company_edit.fxml"));
		Parent companyEditRoot = null;
		try {
			companyEditRoot = fxmlloader.load();
		} catch (IOException e) {
		}

		// ��� â ��ü �������� 10��
		Button btnSave = (Button) companyEditRoot.lookup("#btnSave");
		Button btnClose = (Button) companyEditRoot.lookup("#btnClose");
		Button btnCheck = (Button) companyEditRoot.lookup("#btnCheck");
		Button btnNumClear = (Button) companyEditRoot.lookup("#btnNumClear");
		Button btnNameClear = (Button) companyEditRoot.lookup("#btnNameClear");
		Button btnMangerNameClear = (Button) companyEditRoot.lookup("#btnMangerNameClear");
		Button btnPhoneClear = (Button) companyEditRoot.lookup("#btnPhoneClear");
		Button btnAdressClear = (Button) companyEditRoot.lookup("#btnAdressClear");
		TextField txtName = (TextField) companyEditRoot.lookup("#txtName");
		TextField txtNumber = (TextField) companyEditRoot.lookup("#txtNumber");
		TextField txtManagerName = (TextField) companyEditRoot.lookup("#txtManagerName");
		TextField txtPhone = (TextField) companyEditRoot.lookup("#txtPhone");
		TextField txtAdress = (TextField) companyEditRoot.lookup("#txtAdress");
		RadioButton rdoContract = (RadioButton) companyEditRoot.lookup("#rdoContract");
		RadioButton rdoEndContract = (RadioButton) companyEditRoot.lookup("#rdoEndContract");

		// radioButton �׷����
		ToggleGroup contractGroup = new ToggleGroup();
		rdoContract.setToggleGroup(contractGroup);
		rdoEndContract.setToggleGroup(contractGroup);

		// �ݱ� ��ư �̺�Ʈ
		btnClose.setOnAction(event -> companyEditStage.close());

		// ���â ����� �� ����
		txtName.setText("�̷��ɷ°��� ������");
		txtNumber.setText("1068231388");
		txtManagerName.setText("ȫ�浢");
		txtPhone.setText("01095586958");
		txtAdress.setText("����� ������ �սʸ���");
		rdoContract.setSelected(true);

		// ������ ����� ��ư
		btnNumClear.setOnAction(event -> txtNumber.clear());
		btnNameClear.setOnAction(event -> txtName.clear());
		btnMangerNameClear.setOnAction(event -> txtManagerName.clear());
		btnPhoneClear.setOnAction(event -> txtPhone.clear());
		btnAdressClear.setOnAction(event -> txtAdress.clear());

		// �Է°� ���伳��
		// ����� ��Ϲ�ȣ 10�ڸ�
		DecimalFormat cmNumber = new DecimalFormat("##########");
		txtNumber.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			if (object == null || e.getControlNewText().length() >= 11) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
		// ��ü�� 40�ڸ�
		txtName.setOnKeyTyped(event -> {
			int maxCharacters = 39;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// ����ڸ� 4�ڸ�
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 3;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// ����ó 11�ڸ�
		DecimalFormat cmPhoneNumber = new DecimalFormat("###########");
		txtPhone.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = cmPhoneNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 12) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		// �ּ� 50�ڸ�
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 49;
			if (txtAdress.getText().length() > maxCharacters)
				event.consume();
		});

		// �ߺ�Ȯ�� ��ư �̺�Ʈ
		btnCheck.setOnAction(event -> {
			if (txtNumber.getText().trim().length() < 10) {
				Alert alert = new Alert(AlertType.WARNING, "���� 10�ڸ��� Ȯ�����ּ���.");
				alert.setHeaderText("����� ��� ��ȣ�� Ȯ�����ּ���.");
				alert.setTitle("��� �ȳ�");
				alert.showAndWait();
				return;
			}
			ArrayList<CompanyModel> aList = new CompanyDAO().CompanyFind(txtNumber.getText(), nowListProdConsum);

			// 0�� �ƴϸ� ����� ��� ��ȣ �ߺ��� (DB�˻����� �� ���� ���Դٴ� �ǹ�)
			if (aList.size() != 0) {
				Alert alert = new Alert(AlertType.INFORMATION, "����� ��� ��ȣ�� Ȯ�����ּ���.");
				alert.setHeaderText("�̹� ��ϵ� ����� ��� ��ȣ�� �����մϴ�.");
				alert.setTitle("��� �ȳ�");
				alert.showAndWait();
				return;
			} else {
				Alert alert = new Alert(AlertType.CONFIRMATION, "�ش� ����� ��ȣ ����� ��� �����Ͻðڽ��ϱ�?", ButtonType.OK,
						ButtonType.CANCEL);
				alert.setHeaderText("��� ������ ����� ��ȣ�Դϴ�.");
				alert.setTitle("��� �ȳ�");
				alert.showAndWait();

				// alert â���� ok�� ������ textField ����(��Ȱ��ȭ)
				if (alert.getResult().getText().equals("OK")) {
					txtNumber.setDisable(true);
					btnCheck.setDisable(true);
				}
			}
		});
		


		// ���� ��ư �̺�Ʈ
		btnSave.setOnAction(event -> {
			//btncheck�� ����� �� �ִ� ��Ȳ�̶�� 
			if (btnCheck.isDisable() != true) {
				Alert alert = new Alert(AlertType.WARNING, "����� ��� ��ȣ�� �ߺ�Ȯ���� ������� �ʾҽ��ϴ�.");
				alert.setHeaderText("����� ��� ��ȣ�� �ߺ�Ȯ��");
				alert.setTitle("��� �ȳ�");
				alert.showAndWait();
			} else {
				
				// ���â�� textField�� �Էµ� ���� �𵨿� ����
				CompanyModel cm = new CompanyModel(txtNumber.getText(), txtName.getText(), txtManagerName.getText(),
						txtPhone.getText(), ((RadioButton) contractGroup.getSelectedToggle()).getText(), txtAdress.getText(),
						nowListProdConsum);
				
				// �ȳ�â �ε�
				Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK, ButtonType.CANCEL);
				alert.setTitle("��� �ȳ�");
				alert.setHeaderText("�ش� ��ü�� ����Ͻðڽ��ϱ�? " + "\n\n * ��ü�� : " + cm.getCompany_name() + "\n * ����ڵ�Ϲ�ȣ : "
						+ cm.getCompany_number());
				alert.showAndWait();

				// alert â���� ok�� ���� ������ ��� ����
				if (alert.getResult().getText().equals("OK")) {
					int result = new CompanyDAO().CompanyRegistration(cm);
					if (result == 0) {
						System.out.println("CompanyRegistration�� �������� ������� �ʾҽ��ϴ� ," + result);
					} else {
						System.out.println("CompanyRegistration�� �������� ���������� ����ƽ��ϴ� ," + result);
						Alert CompleteAlert = new Alert(AlertType.INFORMATION, "������ ��� ����");
						CompleteAlert.showAndWait();
						tableViewProdCompanyListInit(nowListProdConsum);
						companyEditStage.close();
					}
				}
			}
		});

		// stage ����
		companyEditStage = new Stage();
		Scene scene = new Scene(companyEditRoot);
		companyEditStage.setScene(scene);
		companyEditStage.initOwner(primaryStage);
		companyEditStage.initModality(Modality.WINDOW_MODAL);
		companyEditStage.setTitle("��ü ���");
		companyEditStage.show();
	}

	// ���� ��ư �̺�Ʈ
	private void handleDeleteAction() {
		// tableView�� ���õ� ���ڵ带 textField�� ��������
		CompanyModel cm = null;
		try {
			cm = obsListCompany.get(selectedTable);
		} catch (ArrayIndexOutOfBoundsException e) {
			Alert alert = new Alert(AlertType.WARNING, "ȭ�鿡 ���̴� ǥ���� ������ ���ϴ� ���� �������ּ���");
			alert.setHeaderText("������ ��ü�� �������ּ���");
			alert.setTitle("���� �ȳ�");
			alert.showAndWait();
			return;
		}

		// ���â �ε�
		Alert alert = new Alert(AlertType.WARNING, "������ �����ʹ� ������ �� �����ϴ�.", ButtonType.OK, ButtonType.CANCEL);
		alert.setTitle("���");
		alert.setHeaderText("�ش� ��ü �� �����Ͻðڽ��ϱ�? " + "\n\n * ��ü�� : " + cm.getCompany_name() + "\n * ����ڵ�Ϲ�ȣ : "
				+ cm.getCompany_number());
		alert.showAndWait();

		// alert â���� ok�� ���� ������ ���� ����
		if (alert.getResult().getText().equals("OK")) {
			int result = new CompanyDAO().CompanyDelete(cm);
			if (result == 0) {
				System.out.println("CompanyRegistration�� �������� ������� �ʾҽ��ϴ� ," + result);
			} else {
				System.out.println("CompanyRegistration�� �������� ���������� ����ƽ��ϴ� ," + result);
				Alert deleteComplete = new Alert(AlertType.INFORMATION, "������ ���� ����");
				deleteComplete.showAndWait();
				tableViewProdCompanyListInit(nowListProdConsum);
			}
		}
	}

	// tableView ������ index ���
	private void handleSaveClickTableIndexAction() {
		selectedTable = tlvCompanyList.getSelectionModel().getSelectedIndex();
	}

	// ��࿩�� �޺��ڽ��� ���� �̺�Ʈ
	private void handleTableViewContractLineUpAction() {
		txtSearch.clear();
		String contract = null;
		try {
			contract = cmbContract.getSelectionModel().getSelectedItem().toString();
		} catch (NullPointerException e) {
		}
		ArrayList<CompanyModel> arrayListCompany = new CompanyDAO().contractListUp(nowListProdConsum, contract);
		obsListCompany = FXCollections.observableArrayList();
		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		if (arrayListCompany.size() != 0) {
			obsListCompany.clear();
			for (int i = 0; i < arrayListCompany.size(); i++) {
				CompanyModel cm = arrayListCompany.get(i);
				obsListCompany.add(cm);
			}
			tlvCompanyList.setItems(obsListCompany);
		} else {
			System.out.println(arrayListCompany.size() + "arrayList ���� null �Դϴ�.");
		}
	}

	// �˻� ��ư �̺�Ʈ
	private void handleSearchTableViewAction() {
		// ���̺�信 ����ִ� ������ ����
		try {
			if (txtSearch.getText().trim().equals(""))
				throw new Exception();
			ArrayList<CompanyModel> arrayList = new CompanyDAO().CompanyFind(txtSearch.getText().trim(),
					nowListProdConsum);

			// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
			if (arrayList.size() != 0) {
				obsListCompany.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					CompanyModel cm = arrayList.get(i);
					obsListCompany.add(cm);
				}
			}
			tlvCompanyList.setItems(obsListCompany);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(arrayList.size() + " ���� �ڷᰡ �˻� �ƽ��ϴ�.");
			alert.setTitle("�˻�");
			alert.showAndWait();

		} catch (Exception e) {
			Alert findComplete = new Alert(AlertType.ERROR, "ã�� ����, " + e.getMessage());
			findComplete.showAndWait();
		}
	}

	// ����(�ʱ�ȭ) ��ư �̺�Ʈ
	private void handleBtnSearchAction() {
		searchAndComboboxReset();
		tableViewProdCompanyListInit(nowListProdConsum);
	}

	// ������ ȭ������ �̵�
	private void handleBtnInventoryAction() {
		try {
			new InventoryMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// �ŷ����� ȭ������ �̵�
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ���� ȭ������ �̵�
	private void handleBtnReportAction() {
		try {
			new ReportMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// �α��� ȭ������ �̵�
	private void handleBtnLogoutAction() {
		try {
			new LoginMain().start(companyEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}
}
