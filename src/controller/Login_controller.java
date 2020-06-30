package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.LoginMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login_controller implements Initializable {

	@FXML
	Button btnLogin;
	@FXML
	PasswordField txtPassword;
	@FXML
	TextField txtEmploye;
	@FXML
	Button btnFindPassword;
	Parent loginRoot;
	Parent loginRe;
	public Stage stage;
	public static String loginID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnFindPassword.setOnAction(event -> handleBtnFindPasswordAction());
		btnLogin.setOnAction(event -> handleLoginAction());
		txtEmploye.setOnAction(event -> handleTxtEmployeAction());
		txtPassword.setOnAction(event -> henaleTxtPassword());
	}

	private void handleLoginAction() {

	}

	private void handleTxtEmployeAction() {
		Stage loginStage = new Stage();

	}

	private void henaleTxtPassword() {

	}

	private void handleBtnFindPasswordAction() {
		Stage loginStage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/password_find.fxml"));
		try {
			loginRoot = fxmlLoader.load();
		} catch (IOException e) {
		}
		Scene scene = new Scene(loginRoot);
		loginStage.setScene(scene);
		loginStage.show();
		TextField employeNo = (TextField) loginRoot.lookup("#employeNo");
		ComboBox cmbQuestion = (ComboBox) loginRoot.lookup("#cmbQuestion");
		TextField txtAnswer = (TextField) loginRoot.lookup("#txtAnwser");
		Button btnOk = (Button) loginRoot.lookup("#btnOk");

		cmbQuestion.getItems().addAll("내 고향은?", "반려동물 이름은?", "출신 초등학교 이름은?", "출신 고등학교 이름은?");
		cmbQuestion.setPromptText("선택하세요.");

		btnOk.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage reStage = new Stage();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/password_re.fxml"));
				try {
					loginRe = fxmlLoader.load();
				} catch (IOException e) {
				}
				Scene scene = new Scene(loginRe);
				reStage.setScene(scene);
				reStage.show();
				loginStage.close();
				TextField txfPassword = (TextField) loginRe.lookup("#txfPassword");
				TextField txfPasswordck = (TextField) loginRe.lookup("#txfPasswordck");
				Button btnOkk = (Button) loginRe.lookup("#btnOkk");

				btnOkk.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						reStage.close();

					}
				});
			}
		});
	}

}