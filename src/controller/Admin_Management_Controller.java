package controller;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.security.auth.callback.ConfirmationCallback;

import application.CompanyMain;
import application.InventoryMain;
import application.ReportMain;
import application.TradeListMain;
import dao.AdminDAO;
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
import model.AdminModel;
import model.CompanyModel;
import model.TradeListModel;

public class Admin_Management_Controller implements Initializable {
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
	ComboBox cmbEmployment;
	@FXML
	TextField txtSearch;
	@FXML
	TableView tlvEmployeeList;
	@FXML
	Button btnReset;
	@FXML
	Tab btnAdminTab;

	// 멤버 변수
	public Stage primaryStage;// stage
	private Stage adminEditStage;// 관리자 수정 stage
	private int selectedTable;// tableView 에서 현재 선택한 레코드의 index 저장
	private ObservableList<AdminModel> obsListAdmin;

	public Admin_Management_Controller() {
		this.selectedTable = -1;// -1을 주지 않으면 자동으로 0값을 입력하여 tableView를 선택하지 않아도 수정, 삭제 시 0번 index의 값이 삭제됨
		this.obsListAdmin = null;
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 초기 세팅 함수
		cmbContractInitialize();// 콤보박스 초기값 입력 함수
		tableViewColumnInit();// tableView 칼럼 초기화 함수
		tableViewListInit();// tableView에 '관리자'
		searchAndComboboxReset();// 리셋(초기화) 함수 (콤보박스, 검색창)

		// 버튼 이벤트
		btnEdit.setOnAction(event -> handleBtnEditAction()); // 수정 버튼 이벤트
		btnRegisteration.setOnAction(event -> handleRegisterationAction());// 등록 버튼 이벤트
		btnDelete.setOnAction(event -> handleDeleteAction());// 삭제 버튼 이벤트
		cmbEmployment.setOnAction(event -> handleTableViewContractLineUpAction());// 계약여부 콤보박스로 정렬 이벤트
		btnSearch.setOnAction(event -> handleSearchTableViewAction());// 검색 버튼 이벤트
		btnReset.setOnAction(event -> handleBtnSearchAction()); // 리셋(초기화) 버튼 이벤트
		tlvEmployeeList.setOnMouseClicked(event -> handleSaveClickTableIndexAction());// tableView 선택한 index 기억

		// 화면 이동
//		btnLogout.setOnAction(event -> handleBtnLogoutAction());// 로그인 화면으로 이동
		btnCompany.setOnAction(event -> handleBtnCompanyAction());// 업체관리 화면으로 이동
		btnInventory.setOnAction(event -> handleBtnInventoryAction());// 재고관리 화면으로 이동
		btnTrade.setOnAction(event -> handleBtnTradeAction());// 거래내역 화면으로 이동
		btnReport.setOnAction(event -> handleBtnReportAction());// 보고서 화면으로 이동
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록

	// 콤보박스 초기값 입력 함수
	private void cmbContractInitialize() {
		cmbEmployment.setItems(FXCollections.observableArrayList("재직중", "퇴사"));
		cmbEmployment.setPromptText("* 재직 여부 선택 *");
	}

	// tableView 칼럼 초기값 입력 함수
	private void tableViewColumnInit() {
		btnAdmin.setDefaultButton(true);
		
		TableColumn colNumber = new TableColumn("사원 번호");
		colNumber.setMinWidth(120);
		colNumber.setStyle("-fx-alignment: CENTER;");
		colNumber.setCellValueFactory(new PropertyValueFactory("employee_number"));

		TableColumn colName = new TableColumn("성함");
		colName.setMinWidth(200);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("employee_name"));

		TableColumn colManager = new TableColumn("연락처");
		colManager.setMinWidth(100);
		colManager.setStyle("-fx-alignment: CENTER;");
		colManager.setCellValueFactory(new PropertyValueFactory("phone"));

		TableColumn colPhone = new TableColumn("비밀번호");
		colPhone.setMinWidth(150);
		colPhone.setStyle("-fx-alignment: CENTER;");
		colPhone.setCellValueFactory(new PropertyValueFactory("password"));

		TableColumn colContract = new TableColumn("재직중");
		colContract.setMinWidth(100);
		colContract.setStyle("-fx-alignment: CENTER;");
		colContract.setCellValueFactory(new PropertyValueFactory("employment"));

		tlvEmployeeList.getColumns().addAll(colNumber, colName, colManager, colPhone, colContract);
	}

	// '관리자' 버튼 클릭 시 tableView에 생산업체 초기화
	private void tableViewListInit() {
		btnAdmin.setDefaultButton(false);
		ArrayList<AdminModel> arrayList = new AdminDAO().adminListUp();
		obsListAdmin = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayList.size() != 0) {
			obsListAdmin.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				AdminModel am = arrayList.get(i);
				obsListAdmin.add(am);
			}
			tlvEmployeeList.setItems(obsListAdmin);
		} else {
			System.out.println(arrayList.size() + "arrayList 값이 null 입니다.");
		}
	}

	// 리셋 함수 (콤보박스, 검색창)
	private void searchAndComboboxReset() {
		txtSearch.clear();
		cmbEmployment.getSelectionModel().clearSelection();
	}

	// 수정 버튼 이벤트
	private void handleBtnEditAction() {
		// 업체 등록 창 로드
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/view/admin_edit.fxml"));
		Parent adminEdit = null;
		try {
			adminEdit = fxmlloader.load();
		} catch (IOException e) {
		}

		// 수정 창 객체 가져오기 10개
		Button btnSave = (Button) adminEdit.lookup("#btnSave");
		Button btnClose = (Button) adminEdit.lookup("#btnClose");
		Button btnCheck = (Button) adminEdit.lookup("#btnCheck");
		Button btnNumClear = (Button) adminEdit.lookup("#btnNumClear");
		Button btnNameClear = (Button) adminEdit.lookup("#btnNameClear");
		Button btnMangerNameClear = (Button) adminEdit.lookup("#btnMangerNameClear");
		Button btnPhoneClear = (Button) adminEdit.lookup("#btnPhoneClear");
		Button btnAdressClear = (Button) adminEdit.lookup("#btnAdressClear");
		TextField txtName = (TextField) adminEdit.lookup("#txtName");
		TextField txtNumber = (TextField) adminEdit.lookup("#txtNumber");
		TextField txtManagerName = (TextField) adminEdit.lookup("#txtManagerName");
		TextField txtPhone = (TextField) adminEdit.lookup("#txtPhone");
		TextField txtAdress = (TextField) adminEdit.lookup("#txtAdress");
		RadioButton rdoContract = (RadioButton) adminEdit.lookup("#rdoContract");
		RadioButton rdoEndContract = (RadioButton) adminEdit.lookup("#rdoEndContract");

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
			cm = obsListAdmin.get(selectedTable);
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
			tableViewListInit(nowListProdConsum);
		});

		// 기존값 지우기 버튼
		btnMangerNameClear.setOnAction(event -> txtManagerName.clear());
		btnPhoneClear.setOnAction(event -> txtPhone.clear());
		btnAdressClear.setOnAction(event -> txtAdress.clear());

		// 닫기 버튼 이벤트
		btnClose.setOnAction(event -> adminEditStage.close());

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
		adminEditStage = new Stage();
		Scene scene = new Scene(adminEdit);
		adminEditStage.setScene(scene);
		adminEditStage.initOwner(primaryStage);
		adminEditStage.initModality(Modality.WINDOW_MODAL);
		adminEditStage.setTitle("업체 수정");
		adminEditStage.show();
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
		btnClose.setOnAction(event -> adminEditStage.close());

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
			// 위치조사( 키보드 치는 위치를 추적한다.)
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

		// 등록창의 textField에 입력된 값을 모델에 담음
		CompanyModel cm = new CompanyModel(txtNumber.getText(), txtName.getText(), txtManagerName.getText(),
				txtPhone.getText(), ((RadioButton) contractGroup.getSelectedToggle()).getText(), txtAdress.getText(),
				nowListProdConsum);

		// 저장 버튼 이벤트
		btnSave.setOnAction(event -> {
			if (btnCheck.isDisable() != true) {
				Alert alert = new Alert(AlertType.WARNING, "사업자 등록 번호의 중복확인이 진행되지 않았습니다.");
				alert.setHeaderText("사업자 등록 번호를 중복확인");
				alert.setTitle("등록 안내");
				alert.showAndWait();
			} else {
				// 경고창 로드
				Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK, ButtonType.CANCEL);
				alert.setTitle("등록 안내");
				alert.setHeaderText("해당 업체를 등록하시겠습니까? " + "\n\n * 업체명 : " + cm.getCompany_name() + "\n * 사업자등록번호 : "
						+ cm.getCompany_number());
				alert.showAndWait();

				// alert 창에서 ok를 눌러 닫으면 삭제 진행
				if (alert.getResult().getText().equals("OK")) {
					int result = new CompanyDAO().CompanyRegistration(cm);
					if (result == 0) {
						System.out.println("CompanyRegistration의 쿼리문이 실행되지 않았습니다 ," + result);
					} else {
						System.out.println("CompanyRegistration의 쿼리문이 성공적으로 실행됐습니다 ," + result);
						Alert CompleteAlert = new Alert(AlertType.INFORMATION, "데이터 등록 성공");
						CompleteAlert.showAndWait();
						tableViewListInit(nowListProdConsum);
					}
				}
			}
		});

		// stage 생성
		adminEditStage = new Stage();
		Scene scene = new Scene(companyEditRoot);
		adminEditStage.setScene(scene);
		adminEditStage.initOwner(primaryStage);
		adminEditStage.initModality(Modality.WINDOW_MODAL);
		adminEditStage.setTitle("업체 등록");
		adminEditStage.show();
	}

	// 삭제 버튼 이벤트
	private void handleDeleteAction() {
		// tableView의 선택된 레코드를 textField로 가져오기
		CompanyModel cm = null;
		try {
			cm = obsListAdmin.get(selectedTable);
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
				tableViewListInit(nowListProdConsum);
			}
		}
	}

	// tableView 선택한 index 기억
	private void handleSaveClickTableIndexAction() {
		selectedTable = tlvEmployeeList.getSelectionModel().getSelectedIndex();
	}

	// 계약여부 콤보박스로 정렬 이벤트
	private void handleTableViewContractLineUpAction() {
		txtSearch.clear();
		String contract = null;
		try {
			contract = cmbEmployment.getSelectionModel().getSelectedItem().toString();
		} catch (NullPointerException e) {
		}
		ArrayList<CompanyModel> arrayListCompany = new CompanyDAO().comboboxYearMonth(nowListProdConsum, contract);
		obsListAdmin = FXCollections.observableArrayList();
		// arrayList에 있는 값을 obsList에 입력한다.
		if (arrayListCompany.size() != 0) {
			obsListAdmin.clear();
			for (int i = 0; i < arrayListCompany.size(); i++) {
				CompanyModel cm = arrayListCompany.get(i);
				obsListAdmin.add(cm);
			}
			tlvEmployeeList.setItems(obsListAdmin);
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
				obsListAdmin.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					CompanyModel cm = arrayList.get(i);
					obsListAdmin.add(cm);
				}
			}
			tlvEmployeeList.setItems(obsListAdmin);
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
		tableViewListInit(nowListProdConsum);
	}

	// 재고관리 화면으로 이동
	private void handleBtnInventoryAction() {
		try {
			new InventoryMain().start(adminEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}

	// 거래내역 화면으로 이동
	private void handleBtnTradeAction() {
		try {
			new TradeListMain().start(adminEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}

	// 보고서 화면으로 이동
	private void handleBtnReportAction() {
		try {
			new ReportMain().start(adminEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}

	// 업체 관리 화면으로 이동
	private void handleBtnCompanyAction() {
		try {
			new CompanyMain().start(adminEditStage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		primaryStage.close();
	}

	// 로그인 화면으로 이동
//		private void handleBtnLogoutAction() {
//			try {
//				new LoginMain().start(adminEditStage);
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//			primaryStage.close();
//		}
}
