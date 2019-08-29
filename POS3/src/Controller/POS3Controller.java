package Controller;

import DB.DB;
import DTO.TableModel;
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
import javafx.scene.control.TableColumn;
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

public class POS3Controller {

    public AnchorPane MngCust;
    public JFXTextField txtID;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public TableView<TableModel> tableCustomer;
    public TableColumn columnID;
    public TableColumn ColumnName;
    public TableColumn columnAddress;
    public JFXButton btnSave;
    public ImageView icnHome;
    int count = 0;


    public void initialize() {


        //setID();


        //loadTable(String id,String name,String address)

        tableCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        //loadCustomers();

        ObservableList<TableModel> customers = FXCollections.observableList(DB.customer);
        tableCustomer.setItems(customers);
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

    private void setID() {
        String ID = "";
        if (0 < DB.nextIndex && DB.nextIndex < 10) {
            ID = "C00" + DB.nextIndex;
        } else if (10 <= DB.nextIndex && DB.nextIndex < 100) {
            ID = "C0" + DB.nextIndex;
        } else if ((100 <= DB.nextIndex && DB.nextIndex < 1000)) {
            ID = "C" + DB.nextIndex;
        }
        txtID.setText(ID);

    }

    public void loadTable(String id, String name, String address) {
        //tableList.add(new TableModel("C001","Nimal","Gampaha"));
    }

    private void loadCustomers() {
        //customer.add(new Customer("C001","Nimal","Gampaha"));
//        customer.add(new Customer("C002","Kamal","Polhena"));
        //      customer.add(new Customer("C003","Ajith","Gampaha"));
//        ObservableList<TableModel> tableList= tableCustomer.getItems();
//
//        if(count==0){
//
//            tableList.add(new TableModel("C001","Nimal","Gampaha"));
//            tableList.add(new TableModel("C002","Jagath","Matara"));
//            tableList.add(new TableModel("C003","Sunil","Panadura"));
//
//        }else{
//            tableList.add(new TableModel(customer.get(count-1).getId(),customer.get(count-1).getName(),customer.get(count-1).getAddress()));
//        }
    }


    public void icnHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/View/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.MngCust.getScene().getWindow());
        primaryStage.setScene(scene);
        // primaryStage.centerOnScreen();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }


    public void btnSave_OnAction(ActionEvent actionEvent) {

//        String id=(String) txtID.getText();
//        String name=txtName.getText();
//        String address=txtAddress.getText();
//
//        this.customer.add(new Customer(id,name,address));
//
//        //customer.add(DB.nextIndex,new Customer(txtID.getText(),txtName.getText(),txtAddress.getText()));
//        //Customer c1 = new Customer(txtID.getText(),txtName.getText(),txtAddress.getText());
//        //customer.add(c1);
//        System.out.println(customer.get(count).getId());
//        System.out.println(customer.get(count).getName());
//        System.out.println(customer.get(count).getAddress());
////        System.out.println(c1.getId());
////        System.out.println(c1.getName());
////        System.out.println(c1.getAddress());
//        count++;
//        DB.nextIndex++;
//        initialize();
        if (btnSave.getText().equals("Save")) {
            ObservableList<TableModel> customers = tableCustomer.getItems();
            customers.add(new TableModel(txtID.getText(), txtName.getText(), txtAddress.getText()));
            DB.nextIndex++;
            setID();
        } else {

        }

    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            TableModel selectedItem = tableCustomer.getSelectionModel().getSelectedItem();
            tableCustomer.getItems().remove(selectedItem);
//            int a=Integer.parseInt(tableCustomer.getSelectionModel().getSelectedItem().getId().replace("C",""));
//            if(a==DB.nextIndex-1){
//                DB.nextIndex=DB.nextIndex-2;
//                setID();
//            }
        }


    }

    public void tableCustomer_OnClicked(MouseEvent mouseEvent) {
        TableModel temp = (TableModel) tableCustomer.getSelectionModel().getSelectedItem();
        txtID.setText(temp.getId());
        txtName.setText(temp.getName());
        txtAddress.setText(temp.getAddress());
    }

    public void btnNew_OnAction(ActionEvent actionEvent) {
        txtID.clear();
        txtName.clear();
        txtName.clear();
        tableCustomer.getSelectionModel().clearSelection();
        txtName.setDisable(false);
        txtAddress.setDisable(false);
        txtName.requestFocus();
        btnSave.setDisable(false);

        // Generate a new id
        int maxId = 0;
        for (TableModel customer : DB.customer) {
            int id = Integer.parseInt(customer.getId().replace("C", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "C00" + maxId;
        } else if (maxId < 100) {
            id = "C0" + maxId;
        } else {
            id = "C" + maxId;
        }
        txtID.setText(id);
    }
}
