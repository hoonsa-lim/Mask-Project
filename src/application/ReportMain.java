package application;

import controller.TradeList_Management_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReportMain extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoder = new FXMLLoader(getClass().getResource("/view/report.fxml"));
		Parent root = fxmlLoder.load();
//		TradeList_Management_Controller tradeListController = fxmlLoder.getController();
//		tradeListController.tradeStage = primaryStage;
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
