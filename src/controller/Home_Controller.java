package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.CompanyMain;
import application.InventoryMain;
import application.ReportMain;
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

public class Home_Controller implements Initializable {
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

	// 멤버 변수
	private Stage newStage = new Stage();
	public Stage stage;

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@이벤트 등록
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnCompany.setOnAction(event -> handleLoadCompanyStageAction()); // load company
		btnTrade.setOnAction(event -> handleLoadTradeStageAction());// load Trade
		btnInventory.setOnAction(event -> handleLoadInventoryStageAction());// load Inventory
		btnReport.setOnAction(event -> handleLoadReportStageAction());// load Report
//		btnLogout.setOnAction(event ->  handleLoadLogoutStageAction());//load Logout
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@핸들러 등록

	// load company
	private void handleLoadCompanyStageAction() {
		try {
			new CompanyMain().start(newStage);
		} catch (Exception e) {
		}
		stage.close();

	}

	// load Trade
	private void handleLoadTradeStageAction() {
		try {
			new TradeListMain().start(newStage);
		} catch (Exception e) {
		}
		stage.close();
	}

//	 load Inventory
	private void handleLoadInventoryStageAction() {
		try {
			new InventoryMain().start(newStage);
		} catch (Exception e) {
		}
		stage.close();
	}

	// load Report
	private void handleLoadReportStageAction() {
		try {
			new ReportMain().start(newStage);
		} catch (Exception e) {
		}
		stage.close();
	}


	// load Logout
//	private void handleLoadLogoutStageAction() {
//		try {
//			new LoginMain().start(newStage);
//		} catch (Exception e) {}
//	stage.close();
//	}

}