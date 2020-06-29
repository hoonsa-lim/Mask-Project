package application;

import controller.Admin_Management_Controller;
import controller.TradeList_Management_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminMain extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoder = new FXMLLoader(getClass().getResource("/view/admin_management.fxml"));
		Parent root = fxmlLoder.load();
		Admin_Management_Controller admin = fxmlLoder.getController();
		admin.primaryStage = primaryStage;
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
