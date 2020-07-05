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
	Button btnLogout;

	// 업체관리 페이지의 버튼
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

	// 멤버 변수
	public Stage primaryStage;// stage
	private Stage companyEditStage;// 업체 수정 , logout 창에 쓰임
	private String product;// tableView 하나로 구현하기 위해서 생산업체리스트와 소비업체 리스트를 선택에 따라 list up하기 위한 변수
	private String consum;
	private String nowListProdConsum;// 현재 업체관리 tableView에 나타나고 있는 리스트가 소비업체인지/생산업체인지 확인하는 변수
	private int selectedTable;// tableView 에서 현재 선택한 레코드의 index 저장
	private ObservableList<CompanyModel> obsListCompany;

	// 기본 생성자
	public Company_Management_Controller() {
		this.product = "생산";
		this.consum = "소비";
		this.nowListProdConsum = null;
		this.selectedTable = -1;// -1을 주지 않으면 default 0값을 입력하여 tableView를 선택하지 않아도 수정, 삭제 시 0번 index의 값이 삭제됨
		this.obsListCompany = null;
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 초기 세팅 함수
		cmbContractInitialize();// 콤보박스 초기값 입력 함수
		tableViewColumnInit();// tableView 칼럼 초기화 함수
		tableViewProdCompanyListInit(product);// '업체관리' 버튼 클릭 시 tableView에 생산업체 초기화
		searchAndComboboxReset();// 리셋(초기화) 함수 (콤보박스, 검색창)

		// 버튼 이벤트
		btnProductTab.setOnSelectionChanged(event -> tableViewProdCompanyListInit(product)); // 생산tab 클릭 시 tlv에 생산 초기화
		btnSellTab.setOnSelectionChanged(event -> tableViewProdCompanyListInit(consum)); // 소비tab 클릭 시 tlv에 소비 초기화
		btnEdit.setOnAction(event -> handleBtnEditAction()); // 수정 버튼 이벤트
		btnRegisteration.setOnAction(event -> handleRegisterationAction());// 등록 버튼 이벤트
		btnDelete.setOnAction(event -> handleDeleteAction());// 삭제 버튼 이벤트
		cmbContract.setOnAction(event -> handleTableViewContractLineUpAction());// 계약여부 콤보박스로 정렬 이벤트
		btnSearch.setOnAction(event -> handleSearchTableViewAction());// 검색 버튼 이벤트
		btnReset.setOnAction(event -> handleBtnSearchAction()); // 리셋(초기화) 버튼 이벤트
		tlvCompanyList.setOnMouseClicked(event -> handleSaveClickTableIndexAction());// tableView 선택한 index 기억

		// 화면 이동
		btnLogout.setOnAction(event -> handleBtnLogoutAction());// 로그인 화면으로 이동
		btnInventory.setOnAction(event -> handleBtnInventoryAction());// 재고관리 화면으로 이동
		btnTrade.setOnAction(event -> handleBtnTradeAction());// 거래내역 화면으로 이동
		btnReport.setOnAction(event -> handleBtnReportAction());// 보고서 화면으로 이동
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록

	// 콤보박스 초기값 입력 함수
	private void cmbContractInitialize() {
		btnCompany.setDefaultButton(true);
		cmbContract.setItems(FXCollections.observableArrayList("계약중", "계약만료"));
		cmbContract.setPromptText("* 계약 여부 선택 *");
	}

	// tableView 칼럼 초기값 입력 함수
	private void tableViewColumnInit() {
		TableColumn colNumber = new TableColumn("사업자 등록번호");
		colNumber.setMinWidth(120);
		colNumber.setStyle("-fx-alignment: CENTER;");
		colNumber.setCellValueFactory(new PropertyValueFactory("company_number"));

		TableColumn colName = new TableColumn("업체명");
		colName.setMinWidth(200);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("company_name"));

		TableColumn colManager = new TableColumn("담당자 명");
		colManager.setMinWidth(100);
		colManager.setStyle("-fx-alignment: CENTER;");
		colManager.setCellValueFactory(new PropertyValueFactory("manager"));

		TableColumn colPhone = new TableColumn("연락처");
		colPhone.setMinWidth(150);
		colPhone.setStyle("-fx-alignment: CENTER;");
		colPhone.setCellValueFactory(new PropertyValueFactory("manager_phone"));

		TableColumn colContract = new TableColumn("계약여부");
		colContract.setMinWidth(100);
		colContract.setStyle("-fx-alignment: CENTER;");
		colContract.setCellValueFactory(new PropertyValueFactory("contract"));

		TableColumn colAddress = new TableColumn("주소");
		colAddress.setMinWidth(500);
		colAddress.setStyle("-fx-alignment: CENTER;");
		colAddress.setCellValueFactory(new PropertyValueFactory("address"));

		tlvCompanyList.getColumns().addAll(colNumber, colName, colManager, colPhone, colContract, colAddress);
	}

	// '업체관리' 버튼 클릭 시 tableView에 생산업체 초기화
	private void tableViewProdCompanyListInit(String productOrConsum) {
		ArrayList<CompanyModel> arrayList = new CompanyDAO().companyListUp(productOrConsum);
		obsListCompany = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayList.size() != 0) {
			obsListCompany.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				CompanyModel cm = arrayList.get(i);
				obsListCompany.add(cm);
			}
			tlvCompanyList.setItems(obsListCompany);
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}

		// 현재 tableView 에 보여지는 리스트가 소비업체인가, 생산업체인가
		CompanyModel cm = obsListCompany.get(0);
		nowListProdConsum = cm.getProduction_consumption();

		// 검색 및 콤보박스 리셋
		searchAndComboboxReset();
	}

	// 리셋 함수 (콤보박스, 검색창)
	private void searchAndComboboxReset() {
		txtSearch.clear();
		cmbContract.getSelectionModel().clearSelection();
	}

	// 수정 버튼 이벤트
	private void handleBtnEditAction() {
		// 업체 등록 창 로드
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/view/company_edit.fxml"));
		Parent companyEditRoot = null;
		try {
			companyEditRoot = fxmlloader.load();
		} catch (IOException e) {
		}

		// 수정 창 객체 가져오기 10개
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

		// 업체명, 사업자 등록번호 비활성화
		txtName.setDisable(true);
		txtNumber.setDisable(true);
		btnCheck.setDisable(true);
		btnNameClear.setDisable(true);
		btnNumClear.setDisable(true);

		// radioButton 그룹생성
		ToggleGroup contractGroup = new ToggleGroup();
		rdoContract.setToggleGroup(contractGroup);
		rdoEndContract.setToggleGroup(contractGroup);

		// tableView의 선택된 레코드를 textField로 가져오기
		CompanyModel cm = null;
		try {
			cm = obsListCompany.get(selectedTable);
		} catch (ArrayIndexOutOfBoundsException e) {
			Alert alert = new Alert(AlertType.WARNING, "화면에 보이는 표에서 수정을 원하는 행을 선택해주세요");
			alert.setHeaderText("수정할 업체를 선택해주세요");
			alert.setTitle("수정 안내");
			alert.showAndWait();
			return;
		}
		// 수정창에서 입력한 값 model에 저장
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

		// 저장 버튼 이벤트
		btnSave.setOnAction(event -> {
			CompanyModel editCm = new CompanyModel(txtNumber.getText(), txtName.getText(), txtManagerName.getText(),
					txtPhone.getText(), ((RadioButton) (contractGroup.getSelectedToggle())).getText(),
					txtAdress.getText(), nowListProdConsum);
			int result = new CompanyDAO().CompanyEdit(editCm);
			tableViewProdCompanyListInit(nowListProdConsum);
			companyEditStage.close();
		});

		// 기존값 지우기 버튼
		btnMangerNameClear.setOnAction(event -> txtManagerName.clear());
		btnPhoneClear.setOnAction(event -> txtPhone.clear());
		btnAdressClear.setOnAction(event -> txtAdress.clear());

		// 닫기 버튼 이벤트
		btnClose.setOnAction(event -> companyEditStage.close());

		// 입력값 포멧설정
		// 사업자 등록번호 10자리
		DecimalFormat cmNumber = new DecimalFormat("##########");
		txtNumber.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 11) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
		// 업체명 40자리
		txtName.setOnKeyTyped(event -> {
			int maxCharacters = 39;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// 담당자명 4자리
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 3;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// 연락처 11자리
		DecimalFormat cmPhoneNumber = new DecimalFormat("###########");
		txtPhone.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = cmPhoneNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 12) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		// 주소 50자리
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 49;
			if (txtAdress.getText().length() > maxCharacters)
				event.consume();
		});
		// stage 생성
		companyEditStage = new Stage();
		Scene scene = new Scene(companyEditRoot);
		companyEditStage.setScene(scene);
		companyEditStage.initOwner(primaryStage);
		companyEditStage.initModality(Modality.WINDOW_MODAL);
		companyEditStage.setTitle("업체 수정");
		companyEditStage.show();
	}

	// 등록 버튼 이벤트
	private void handleRegisterationAction() {
		// 업체 등록 창 로드
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/view/company_edit.fxml"));
		Parent companyEditRoot = null;
		try {
			companyEditRoot = fxmlloader.load();
		} catch (IOException e) {
		}

		// 등록 창 객체 가져오기 10개
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

		// radioButton 그룹생성
		ToggleGroup contractGroup = new ToggleGroup();
		rdoContract.setToggleGroup(contractGroup);
		rdoEndContract.setToggleGroup(contractGroup);

		// 닫기 버튼 이벤트
		btnClose.setOnAction(event -> companyEditStage.close());

		// 등록창 디버깅 용 내용
		txtName.setText("미래능력개발 교육원");
		txtNumber.setText("1068231388");
		txtManagerName.setText("홍길덩");
		txtPhone.setText("01095586958");
		txtAdress.setText("서울시 성동구 왕십리동");
		rdoContract.setSelected(true);

		// 기존값 지우기 버튼
		btnNumClear.setOnAction(event -> txtNumber.clear());
		btnNameClear.setOnAction(event -> txtName.clear());
		btnMangerNameClear.setOnAction(event -> txtManagerName.clear());
		btnPhoneClear.setOnAction(event -> txtPhone.clear());
		btnAdressClear.setOnAction(event -> txtAdress.clear());

		// 입력값 포멧설정
		// 사업자 등록번호 10자리
		DecimalFormat cmNumber = new DecimalFormat("##########");
		txtNumber.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = cmNumber.parse(e.getControlNewText(), parsePosition);
			if (object == null || e.getControlNewText().length() >= 11) {
				return null; // 입력한 값을 안받겠다.
			} else {
				return e;
			}
		}));
		// 업체명 40자리
		txtName.setOnKeyTyped(event -> {
			int maxCharacters = 39;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// 담당자명 4자리
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 3;
			if (txtName.getText().length() > maxCharacters)
				event.consume();
		});
		// 연락처 11자리
		DecimalFormat cmPhoneNumber = new DecimalFormat("###########");
		txtPhone.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = cmPhoneNumber.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			if (object == null || e.getControlNewText().length() >= 12) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		// 주소 50자리
		txtManagerName.setOnKeyTyped(event -> {
			int maxCharacters = 49;
			if (txtAdress.getText().length() > maxCharacters)
				event.consume();
		});

		// 중복확인 버튼 이벤트
		btnCheck.setOnAction(event -> {
			if (txtNumber.getText().trim().length() < 10) {
				Alert alert = new Alert(AlertType.WARNING, "숫자 10자리를 확인해주세요.");
				alert.setHeaderText("사업자 등록 번호를 확인해주세요.");
				alert.setTitle("등록 안내");
				alert.showAndWait();
				return;
			}
			ArrayList<CompanyModel> aList = new CompanyDAO().CompanyFind(txtNumber.getText(), nowListProdConsum);

			// 0이 아니면 사업자 등록 번호 중복됨 (DB검색했을 때 값이 나왔다는 의미)
			if (aList.size() != 0) {
				Alert alert = new Alert(AlertType.INFORMATION, "사업자 등록 번호를 확인해주세요.");
				alert.setHeaderText("이미 등록된 사업자 등록 번호가 존재합니다.");
				alert.setTitle("등록 안내");
				alert.showAndWait();
				return;
			} else {
				Alert alert = new Alert(AlertType.CONFIRMATION, "해당 사업자 번호 등록을 계속 진행하시겠습니까?", ButtonType.OK,
						ButtonType.CANCEL);
				alert.setHeaderText("등록 가능한 사업자 번호입니다.");
				alert.setTitle("등록 안내");
				alert.showAndWait();

				// alert 창에서 ok를 누르면 textField 고정(비활성화)
				if (alert.getResult().getText().equals("OK")) {
					txtNumber.setDisable(true);
					btnCheck.setDisable(true);
				}
			}
		});
		


		// 저장 버튼 이벤트
		btnSave.setOnAction(event -> {
			//btncheck가 사용할 수 있는 상황이라면 
			if (btnCheck.isDisable() != true) {
				Alert alert = new Alert(AlertType.WARNING, "사업자 등록 번호의 중복확인이 진행되지 않았습니다.");
				alert.setHeaderText("사업자 등록 번호를 중복확인");
				alert.setTitle("등록 안내");
				alert.showAndWait();
			} else {
				
				// 등록창의 textField에 입력된 값을 모델에 담음
				CompanyModel cm = new CompanyModel(txtNumber.getText(), txtName.getText(), txtManagerName.getText(),
						txtPhone.getText(), ((RadioButton) contractGroup.getSelectedToggle()).getText(), txtAdress.getText(),
						nowListProdConsum);
				
				// 안내창 로드
				Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK, ButtonType.CANCEL);
				alert.setTitle("등록 안내");
				alert.setHeaderText("해당 업체를 등록하시겠습니까? " + "\n\n * 업체명 : " + cm.getCompany_name() + "\n * 사업자등록번호 : "
						+ cm.getCompany_number());
				alert.showAndWait();

				// alert 창에서 ok를 눌러 닫으면 등록 진행
				if (alert.getResult().getText().equals("OK")) {
					int result = new CompanyDAO().CompanyRegistration(cm);
					if (result == 0) {
						System.out.println("CompanyRegistration의 쿼리문이 실행되지 않았습니다 ," + result);
					} else {
						System.out.println("CompanyRegistration의 쿼리문이 성공적으로 실행됐습니다 ," + result);
						Alert CompleteAlert = new Alert(AlertType.INFORMATION, "데이터 등록 성공");
						CompleteAlert.showAndWait();
						tableViewProdCompanyListInit(nowListProdConsum);
						companyEditStage.close();
					}
				}
			}
		});

		// stage 생성
		companyEditStage = new Stage();
		Scene scene = new Scene(companyEditRoot);
		companyEditStage.setScene(scene);
		companyEditStage.initOwner(primaryStage);
		companyEditStage.initModality(Modality.WINDOW_MODAL);
		companyEditStage.setTitle("업체 등록");
		companyEditStage.show();
	}

	// 삭제 버튼 이벤트
	private void handleDeleteAction() {
		// tableView의 선택된 레코드를 textField로 가져오기
		CompanyModel cm = null;
		try {
			cm = obsListCompany.get(selectedTable);
		} catch (ArrayIndexOutOfBoundsException e) {
			Alert alert = new Alert(AlertType.WARNING, "화면에 보이는 표에서 삭제를 원하는 행을 선택해주세요");
			alert.setHeaderText("삭제할 업체를 선택해주세요");
			alert.setTitle("삭제 안내");
			alert.showAndWait();
			return;
		}

		// 경고창 로드
		Alert alert = new Alert(AlertType.WARNING, "삭제된 데이터는 복구할 수 없습니다.", ButtonType.OK, ButtonType.CANCEL);
		alert.setTitle("경고");
		alert.setHeaderText("해당 업체 를 삭제하시겠습니까? " + "\n\n * 업체명 : " + cm.getCompany_name() + "\n * 사업자등록번호 : "
				+ cm.getCompany_number());
		alert.showAndWait();

		// alert 창에서 ok를 눌러 닫으면 삭제 진행
		if (alert.getResult().getText().equals("OK")) {
			int result = new CompanyDAO().CompanyDelete(cm);
			if (result == 0) {
				System.out.println("CompanyRegistration의 쿼리문이 실행되지 않았습니다 ," + result);
			} else {
				System.out.println("CompanyRegistration의 쿼리문이 성공적으로 실행됐습니다 ," + result);
				Alert deleteComplete = new Alert(AlertType.INFORMATION, "데이터 삭제 성공");
				deleteComplete.showAndWait();
				tableViewProdCompanyListInit(nowListProdConsum);
			}
		}
	}

	// tableView 선택한 index 기억
	private void handleSaveClickTableIndexAction() {
		selectedTable = tlvCompanyList.getSelectionModel().getSelectedIndex();
	}

	// 계약여부 콤보박스로 정렬 이벤트
	private void handleTableViewContractLineUpAction() {
		txtSearch.clear();
		String contract = null;
		try {
			contract = cmbContract.getSelectionModel().getSelectedItem().toString();
		} catch (NullPointerException e) {
		}
		ArrayList<CompanyModel> arrayListCompany = new CompanyDAO().contractListUp(nowListProdConsum, contract);
		obsListCompany = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayListCompany.size() != 0) {
			obsListCompany.clear();
			for (int i = 0; i < arrayListCompany.size(); i++) {
				CompanyModel cm = arrayListCompany.get(i);
				obsListCompany.add(cm);
			}
			tlvCompanyList.setItems(obsListCompany);
		} else {
			System.out.println(arrayListCompany.size() + "arrayList 값이 null 입니다.");
		}
	}

	// 검색 버튼 이벤트
	private void handleSearchTableViewAction() {
		// 테이블뷰에 들어있는 데이터 제공
		try {
			if (txtSearch.getText().trim().equals(""))
				throw new Exception();
			ArrayList<CompanyModel> arrayList = new CompanyDAO().CompanyFind(txtSearch.getText().trim(),
					nowListProdConsum);

			// arrayList에 있는 값을 obsList에 입력한다.
			if (arrayList.size() != 0) {
				obsListCompany.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					CompanyModel cm = arrayList.get(i);
					obsListCompany.add(cm);
				}
			}
			tlvCompanyList.setItems(obsListCompany);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(arrayList.size() + " 개의 자료가 검색 됐습니다.");
			alert.setTitle("검색");
			alert.showAndWait();

		} catch (Exception e) {
			Alert findComplete = new Alert(AlertType.ERROR, "찾기 실패, " + e.getMessage());
			findComplete.showAndWait();
		}
	}

	// 리셋(초기화) 버튼 이벤트
	private void handleBtnSearchAction() {
		searchAndComboboxReset();
		tableViewProdCompanyListInit(nowListProdConsum);
	}

	// 재고관리 화면으로 이동
	private void handleBtnInventoryAction() {
		try {
			new InventoryMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 거래내역 화면으로 이동
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 보고서 화면으로 이동
	private void handleBtnReportAction() {
		try {
			new ReportMain().start(primaryStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// 로그인 화면으로 이동
	private void handleBtnLogoutAction() {
		try {
			new LoginMain().start(companyEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}
}
