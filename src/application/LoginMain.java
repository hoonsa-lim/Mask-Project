package application;


import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginMain extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoder = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent root = fxmlLoder.load();
		LoginController loginController = fxmlLoder.getController();
		loginController.stage = primaryStage;
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}