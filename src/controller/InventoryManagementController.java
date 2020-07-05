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

	// ��� �� �޺��ڽ�
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

	// �ֹ� �� �޺��ڽ�
	@FXML
	private ComboBox cmbCompany2;
	@FXML
	private ComboBox cmbType2;
	@FXML
	private ComboBox cmbSize2;
	@FXML
	private ComboBox cmbColor2;

	// �Ǹ� �� �޺��ڽ�
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
	private String production = "����";
	private String production2 = "�Һ�";
	private String purchase = "�ֹ�";
	private String sell = "�Ǹ�";
	private ArrayList<Inventory> arrayList = null;
	private ArrayList<CompanyModel> arrayList2 = null;// �޺��ڽ��� �� �Һ��ü ��ü���� ���

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
		// �ٸ� ȭ������ �̵� ��ư
		btnCompany.setOnAction(e -> handleBtnCompanyAction());
		btnTrade.setOnAction(e -> handleBtnTradeAction());
		btnReport.setOnAction(e -> handleBtnReportAction());
		btnLogout.setOnAction(e -> handleBtnLogoutAction());

		// �� �̵��� ����Ʈ �ʱ�ȭ
		tabInventory.setOnSelectionChanged(e -> selectTabInventoryAction());
		tabPurchase.setOnSelectionChanged(e -> selectTabPurchaseAction());
		tabSell.setOnSelectionChanged(e -> selectTabSellAction());

		tvInventory1Column(); // ��� ����Ʈ �÷�
		tvInventory2Column(); // �ֹ� ����Ʈ �÷�
		tvInventory3Column(); // �Ǹ� ����Ʈ �÷�

		totalLoadList(); // ��ü ��� ����Ʈ

		registerFormat();// ��� text ����
		companyComboBoxList();
		typeComboBoxList();
		colorComboBoxList();
		sizeComboBoxList();

		radioButtonGroup();// ��� ���� group

		cmbCompany4.setOnAction(e -> cmbselect = cmbCompany4.getSelectionModel().getSelectedIndex());

		// ��� Ŭ���� �̺�Ʈ
		tvInventory1.setOnMouseClicked(e -> handleTvinventory1MouseClicked(e));
		tvInventory2.setOnMouseClicked(e -> handleTvinventory2MouseClicked(e));
		tvInventory3.setOnMouseClicked(e -> handleTvinventory3MouseClicked(e));

		btnOk.setOnAction(e -> handleBtnOkAction(e)); // ��� ��ư
		btnEdit.setOnAction(e -> handleBtnEditAction(e)); // ���� ��ư
		btnDelete.setOnAction(e -> handleBtnDeleteAction(e)); // ������ư

		btnSearch1.setOnAction(e -> handleBtnSearch1Action(e)); // ��� ȭ�� �˻���ư
		btnSearch2.setOnAction(e -> handleBtnSearch2Action(e)); // �ֹ� ȭ�� �˻���ư
		btnSearch3.setOnAction(e -> handleBtnSearch3Action(e)); // �Ǹ� ȭ�� �˻���ư

		btnCal1.setOnAction(e -> handleBtnCal1Action(e)); // �����Ѿ� ��� ��ư
		btnPurchase.setOnAction(e -> handleBtnPurchaseAction(e)); // ���� ��ư
		btnCal2.setOnAction(e -> handleBtnCal2Action(e)); // �Ǹ��Ѿ� ��� ��ư
		btnSell.setOnAction(e -> handleBtnSellAction(e)); // �Ǹ� ��ư

		btnReset1.setOnAction(e -> handleBtnReset1Action(e)); // ��� ȭ�� �ʱ�ȭ ��ư
		btnReset2.setOnAction(e -> handleBtnReset2Action(e)); // �ֹ� ȭ�� �ʱ�ȭ ��ư
		btnReset3.setOnAction(e -> handleBtnReset3Action(e)); // �Ǹ� ȭ�� �ʱ�ȭ ��ư

		rdoRegister.setOnAction(e -> handleRdoRegisterAction());// ��� rdoButton ������ �� �̺�Ʈ
		rdoEdit.setOnAction(e -> handleRdoEditAction());// ���� rdoButton ������ �� �̺�Ʈ
	}// initialize

	// toggleGroup init
	private void radioButtonGroup() {
		ToggleGroup tg = new ToggleGroup();
		rdoRegister.setToggleGroup(tg);
		rdoEdit.setToggleGroup(tg);

		// default ����
		rdoRegister.setSelected(true);
		btnEdit.setVisible(false);
	}

	// ���� rdoButton ������ �� �̺�Ʈ
	private void handleRdoEditAction() {
		btnEdit.setVisible(true);
		btnOk.setVisible(false);
	}

	// ��� rdoButton ������ �� �̺�Ʈ
	private void handleRdoRegisterAction() {
		btnEdit.setVisible(false);
		btnOk.setVisible(true);
		
		//Ŭ����
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

	// ��ǰ ��Ͻ� �Է� ���� ����
	private void registerFormat() {
		// ��ǰ��ȣ 7�ڸ��� ����
		DecimalFormat cmNumber = new DecimalFormat("#######");
		txtProductNumber1.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 8) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
		// ��ǰ�� �ִ� 45�ڸ�
		txtProduct1.setOnKeyTyped(event -> {
			int maxCharacters = 44;
			if (txtProduct1.getText().length() > maxCharacters)
				event.consume();
		});
		// ��� �ִ� 5�ڸ�
		DecimalFormat stockFormat = new DecimalFormat("#####");
		txtStock1.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = stockFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 6) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
		// ���԰� �ִ� 4�ڸ�
		DecimalFormat txtPurchase1Format = new DecimalFormat("####");
		txtPurchase1.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = txtPurchase1Format.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 5) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
		// �ǸŰ� �ִ� 4�ڸ�
		DecimalFormat txtSell1Format = new DecimalFormat("####");
		txtSell1.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = txtSell1Format.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 5) {
				return null; // �Է��� ���� �ȹްڴ�.
			} else {
				return e;
			}
		}));
	}

	private void sizeComboBoxList() {
		cmbSize4.setItems(FXCollections.observableArrayList("���ο�", "��̿�"));
		cmbSize4.setPromptText("������ ����");
	}

	private void colorComboBoxList() {
		cmbColor4.setItems(FXCollections.observableArrayList("���", "������"));
		cmbColor4.setPromptText("���� ����");
	}

	private void typeComboBoxList() {
		cmbType4.setItems(FXCollections.observableArrayList("KF80", "KF94", "��Ż ����ũ", "�� ����ũ"));
		cmbType4.setPromptText("���� ����");
	}

	// ���� ȭ������ �̵� ��ư
	private void handleBtnReportAction() {
		try {
			new ReportMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// �α��� ȭ������ �̵� ��ư
	private void handleBtnLogoutAction() {
		try {
			new LoginMain().start(new Stage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		inventoryStage.close();
	}

	// �ŷ� ���� ȭ������ �̵� ��ư
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ��ü ���� ȭ������ �̵� ��ư
	private void handleBtnCompanyAction() {
		try {
			new CompanyMain().start(inventoryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// �� ���ý� ����Ʈ �ʱ�ȭ �̺�Ʈ
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

	// �Ǹ� ��ư �̺�Ʈ
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
				alert.setTitle("����");
				alert.setHeaderText("��� �����մϴ�");
				alert.setContentText("��� �ֹ� �ʿ�");
				alert.showAndWait();
				return;
			}

			Calendar cal = Calendar.getInstance();
			int second = cal.get(Calendar.YEAR);
			int minute = cal.get(Calendar.MONTH) + 1;
			int hour = cal.get(Calendar.DAY_OF_MONTH);

			// �ŷ��� �̷����� �ý��� ��¥�� �޴� ��
			String s = String.valueOf(second) + "-" + String.valueOf(minute) + "-" + String.valueOf(hour);

			// �޺��ڽ����� ���õ� �Һ��ü�� ��ü��ȣ�� �޾ƿ��� ���� ��
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
			alert.setHeaderText("��� ���� ����");
			alert.setContentText("�Ǹ��� ��� �����Ͻÿ�");
			alert.showAndWait();
		}
	}

	// ���� ��ư �̺�Ʈ
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
			alert.setHeaderText("��� ���� ����");
			alert.setContentText("������ ��� �����Ͻÿ�");
			alert.showAndWait();
		}
	}

	// �Ǹ� �Ѿ� ��� �̺�Ʈ
	private void handleBtnCal2Action(ActionEvent e) {
		try {
			int order = Integer.parseInt(txtOrder2.getText());
			int purchase = Integer.parseInt(txtSell2.getText());

			int totalPrice = order * purchase;

			txtTotalSell.setText(String.valueOf(totalPrice));
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("��� �Է� ����");
			alert.setContentText("�Ǹ� ������ �Է� �Ͻÿ�");
			alert.showAndWait();
		}
	}

	// ���� �Ѿ� ��� �̺�Ʈ
	private void handleBtnCal1Action(ActionEvent e) {
		try {
			int order = Integer.parseInt(txtOrder1.getText());
			int purchase = Integer.parseInt(txtPurchase2.getText());

			int totalPrice = order * purchase;

			txtTotalPurchase.setText(String.valueOf(totalPrice));
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("��� �Է� ����");
			alert.setContentText("���� ������ �Է� �Ͻÿ�");
			alert.showAndWait();
		}
	}

	// ��� ȭ�� �˻� ��ư
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
			alert.setHeaderText("�˻��� ��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.setContentText("��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.showAndWait();
		}
	}

	// �ֹ� ȭ�� �˻� ��ư
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
			alert.setHeaderText("�˻��� ��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.setContentText("��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.showAndWait();
		}
	}

	// �Ǹ� ȭ�� �˻� ��ư
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
			alert.setHeaderText("�˻��� ��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.setContentText("��ǰ ��ȣ�� �Է��Ͻÿ�");
			alert.showAndWait();
		}
	}

	// ��� ��ư �̺�Ʈ
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

	// ���� ��ư �̺�Ʈ
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
			alert.setHeaderText("��� ���� ����");
			alert.setContentText("������ ��� �����Ͻÿ�");
			alert.showAndWait();
		}
	}

	// ��� ����Ʈ���� ��� ���ý� �̺�Ʈ
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

	// ��� ����Ʈ, DB ���� �ش� ��� ����
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
			alert.setHeaderText("��� ���� ����");
			alert.setContentText("������ ��� �����Ͻÿ�");
			alert.showAndWait();
		}

		obList.clear();
		totalLoadList();
	}

	// DB���� ��� ��������
	private void totalLoadList() {
		btnInventory.setDefaultButton(true);// inventory ��ư ǥ�ø� ���� ��
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

	// �˻� ���� �� ����Ʈ �ʱ�ȭ��ư (��� ȭ��)
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

	// �˻� ���� �� ����Ʈ �ʱ�ȭ��ư (�ֹ� ȭ��)
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

	// �˻� ���� �� ����Ʈ �ʱ�ȭ��ư (�Ǹ� ȭ��)
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

	// �޺��ڽ� ����Ʈ
	// ��ü
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

		cmbCompany4.setPromptText("��ü ����");
		cmbCompany4.setItems(obList1);

		cmbCompany5.setPromptText("�Ǹ�ó ����");
		cmbCompany5.setItems(obList3);
	}

	// ��� ����Ʈ ���̺�
	private void tvInventory1Column() {
		TableColumn colPnumber = new TableColumn("��ǰ�ڵ�");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("��ü��");
		colCompany.setPrefWidth(140.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("��ǰ��");
		colProduct.setPrefWidth(180.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("���");
		colStock.setMaxWidth(100);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colPurchase = new TableColumn("���԰�");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));

		TableColumn colSell = new TableColumn("�ǸŰ�");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));

		TableColumn colType = new TableColumn("����");
		colType.setMaxWidth(90);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("������");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("����");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory1.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colPurchase, colSell, colType,
				colSize, colColor);

		tvInventory1.setItems(obList);

	}

	// ����ȭ�� ����Ʈ
	private void tvInventory2Column() {
		TableColumn colPnumber = new TableColumn("��ǰ�ڵ�");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("��ü��");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("��ǰ��");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("���");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colPurchase = new TableColumn("���԰�");
		colPurchase.setPrefWidth(80.0);
		colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchase"));

		TableColumn colType = new TableColumn("����");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("������");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("����");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory2.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colPurchase, colType, colSize,
				colColor);

		tvInventory2.setItems(obList);

	}

	// �Ǹ�ȭ�� ����Ʈ
	private void tvInventory3Column() {
		TableColumn colPnumber = new TableColumn("��ǰ�ڵ�");
		colPnumber.setPrefWidth(90.0);
		colPnumber.setCellValueFactory(new PropertyValueFactory<>("productNumber"));

		TableColumn colCompany = new TableColumn("��ü��");
		colCompany.setPrefWidth(190.0);
		colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

		TableColumn colProduct = new TableColumn("��ǰ��");
		colProduct.setPrefWidth(240.0);
		colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

		TableColumn colStock = new TableColumn("���");
		colStock.setMaxWidth(50);
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn colSell = new TableColumn("�ǸŰ�");
		colSell.setPrefWidth(80.0);
		colSell.setCellValueFactory(new PropertyValueFactory<>("sell"));

		TableColumn colType = new TableColumn("����");
		colType.setMaxWidth(100);
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn colSize = new TableColumn("������");
		colSize.setPrefWidth(80.0);
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

		TableColumn colColor = new TableColumn("����");
		colColor.setMaxWidth(60);
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

		tvInventory3.getColumns().addAll(colPnumber, colCompany, colProduct, colStock, colSell, colType, colSize,
				colColor);

		tvInventory3.setItems(obList);

	}

}
