package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.HomeMain;
import application.LoginMain;
import dao.AdminDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AdminModel;
import model.CompanyModel;

public class LoginController implements Initializable {

	@FXML
	Button btnLogin;
	@FXML
	PasswordField txtPassword;
	@FXML
	TextField txtEmploye;
	@FXML
	Button btnFindPassword;

	Parent loginRe;
	public Stage stage;
	public Stage newStage;
	public static String loginID;
	public ObservableList<AdminModel> obsListCompany;

	public LoginController() {
		this.newStage = new Stage();
		this.obsListCompany = FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		btnFindPassword.setOnAction(event -> handleBtnFindPasswordAction());
		btnLogin.setOnAction(event -> handleLoginAction());
		txtEmploye.setOnAction(event -> handleTxtEmployeAction());
		txtPassword.setOnAction(event -> henaleTxtPassword());
	}

	private void handleLoginAction() {
		//�����ͺ��̽��� �����ϴ� ��ü 
		AdminDAO ad = new AdminDAO();
		
		//DB�� �ִ� ����� ��ü�� ������
		ArrayList<AdminModel> arrayList = ad.adminConnect();

		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		if (arrayList.size() != 0) {
			obsListCompany.clear();
			for (int i = 0; i < arrayList.size(); i++) {
				//
				AdminModel am = arrayList.get(i);
				System.out.println(am.getEmployee_number());
				System.out.println(txtEmploye.getText());
				if (txtEmploye.getText().trim().equals(new String().valueOf(am.getEmployee_number()))) {
					if (txtPassword.getText().equals(new String().valueOf(am.getPassword()))) {
						try {
							new HomeMain().start(newStage);
						} catch (Exception e) {
						}
						stage.close();
					} else {
						Alert alert = new Alert(AlertType.INFORMATION, "���̵� �ٽ�");
						alert.setHeaderText("�ƴ�");
						alert.setTitle("Ʋ��");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.INFORMATION, "�����ȣ�� �������� �ʽ��ϴ�.");
					alert.setHeaderText("��� ��Ȯ��");
					alert.setTitle("�α���");
					alert.showAndWait();
				}
			}
		} else {
			System.out.println(arrayList.size() + "arrayList ���� null �Դϴ�.");
		}
	}

	private void handleTxtEmployeAction() {
//		Stage loginStage = new Stage();

	}

	private void henaleTxtPassword() {

	}

//	private void handleBtnFindPasswordAction() {
//		Stage loginStage=new Stage();
//		 FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/password_find.fxml"));
//		Parent loginRoot= null;
//		 try {
//			loginRoot = fxmlLoader.load();
//		} catch (IOException e) {}
//		 Scene scene=new Scene(loginRoot);
//		 loginStage.setScene(scene);
//		 loginStage.initModality(Modality.WINDOW_MODAL);
//		 loginStage.initOwner(stage);
//		 loginStage.show();
//		TextField employeNo= (TextField) loginRoot.lookup("#employeNo");
//		ComboBox cmbQuestion= (ComboBox) loginRoot.lookup("#cmbQuestion");
//		TextField txtAnswer= (TextField) loginRoot.lookup("#txtAnwser");
//		Button btnOk=(Button) loginRoot.lookup("#btnOk");
//		
//		cmbQuestion.getItems().addAll("�� ������?", "�ݷ����� �̸���?", "��� �ʵ��б� �̸���?", "��� ����б� �̸���?");
//		cmbQuestion.setPromptText("�����ϼ���.");
//		
//		btnOk.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//			Stage reStage=new Stage();
//			FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/password_re.fxml"));
//			try {
//				loginRe=fxmlLoader.load();
//			}catch(IOException e) {}
//			Scene scene=new Scene(loginRe);
//			reStage.setScene(scene);
//			reStage.show();
//			loginStage.close();
//			TextField txfPassword=(TextField) loginRe.lookup("#txfPassword");
//			TextField txfPasswordck=(TextField) loginRe.lookup("#txfPasswordck");
//			Button btnOkk=(Button) loginRe.lookup("#btnOkk");
//			
//			btnOkk.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent event) {
//					reStage.close();					
//					
//				}
//			});
//			}
//		});
//}

}