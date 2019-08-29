package Controller;

import DB.DB;
import DTO.ItemManageTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ItemMangeController {
    public JFXTextField txtItemCode;
    public JFXTextField txtItemDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public TableView<ItemManageTM> tableItemDetails;
    public AnchorPane itemManageFrame;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public ImageView icnHome;

    public void initialize() {

        tableItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tableItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tableItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tableItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
        tableItemDetails.setItems(items);


    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        ObservableList<ItemManageTM> items = tableItemDetails.getItems();
        items.add(new ItemManageTM(txtItemCode.getText(), txtItemDescription.getText(), Integer.parseInt(txtQtyOnHand.getText()), Integer.parseInt(txtUnitPrice.getText())));
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            ItemManageTM selectedItem = tableItemDetails.getSelectionModel().getSelectedItem();
            tableItemDetails.getItems().remove(selectedItem);
        }
    }

    public void icnHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/View/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.itemManageFrame.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    public void btnNewItem_OnAction(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtItemDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        tableItemDetails.getSelectionModel().clearSelection();
        txtItemCode.setDisable(false);
        txtItemDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtItemDescription.requestFocus();
        btnSave.setDisable(false);

        // Generate a new id
        int maxId = 0;
        for (ItemManageTM items : DB.items) {
            int id = Integer.parseInt(items.getItemCode().replace("I", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "I00" + maxId;
        } else if (maxId < 100) {
            id = "I0" + maxId;
        } else {
            id = "I" + maxId;
        }
        txtItemCode.setText(id);
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
        }
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();


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
}
