package application;

import controller.InventoryManagementController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryMain extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/inventory_management2.fxml"));
		Parent root=fxmlLoader.load();
		InventoryManagementController imController=fxmlLoader.getController();
		imController.stage=primaryStage;
		
		Scene scene=new Scene(root);
		//scene.getStylesheets().add("/");
		primaryStage.setTitle("재고관리");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
