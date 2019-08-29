package Controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {
    public Label result;
    public AnchorPane dashboard;
    public ImageView icnManageCustomer;
    public ImageView icnManageItems;
    public ImageView icnManageOrders;
    public ImageView icnSearchOrders;
    public Label lblMenu;
    public Label lblDescription;

    @FXML
    private void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "icnManageCustomer":
                    root = FXMLLoader.load(this.getClass().getResource("/View/POS3.fxml"));
                    break;
                case "icnManageItems":
                    root = FXMLLoader.load(this.getClass().getResource("/View/ItemManage1.fxml"));
                    break;
                case "icnManageOrders":
                    root = FXMLLoader.load(this.getClass().getResource("/View/ManageOrders.fxml"));
                    break;
                case "icnSearchOrders":
                    root = FXMLLoader.load(this.getClass().getResource("/View/Search.fxml"));
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.dashboard.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    @FXML
    private void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "icnManageCustomer":
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "icnManageItems":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("Click to add, edit, delete, search or view items");
                    break;
                case "icnManageOrders":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
                case "icnSearchOrders":
                    lblMenu.setText("Search Orders");
                    lblDescription.setText("Click if you want to search orders");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

//    public void icnManageCustomer_OnAction(MouseEvent mouseEvent) throws IOException {
//        URL resource = this.getClass().getResource("/View/POS3.fxml");
//        Parent root = FXMLLoader.load(resource);
//        Scene scene = new Scene(root);
//        Stage primaryStage = (Stage)(this.dashboard.getScene().getWindow());
//        primaryStage.setScene(scene);
//        primaryStage.centerOnScreen();
//    }
//
//    public void icnManageItems_OnAction(MouseEvent mouseEvent) throws IOException {
//        URL resource = this.getClass().getResource("/View/ItemManage1.fxml");
//        Parent root = FXMLLoader.load(resource);
//        Scene scene = new Scene(root);
//        Stage primaryStage= (Stage) this.dashboard.getScene().getWindow();
//        primaryStage.setScene(scene);
//
//    }
//
//    public void icnManageOrders_OnAction(MouseEvent mouseEvent) throws IOException {
//        URL resource = this.getClass().getResource("/View/ManageOrders.fxml");
//        Parent root = FXMLLoader.load(resource);
//        Scene scene = new Scene(root);
//        Stage primaryStage= (Stage) this.dashboard.getScene().getWindow();
//        primaryStage.setScene(scene);
//    }
//
//    public void icnSearchOrders_OnAction(MouseEvent mouseEvent) throws IOException {
//        URL resource = this.getClass().getResource("/View/Search.fxml");
//        Parent root = FXMLLoader.load(resource);
//        Scene scene = new Scene(root);
//        Stage primaryStage= (Stage) this.dashboard.getScene().getWindow();
//        primaryStage.setScene(scene);
//    }
}
