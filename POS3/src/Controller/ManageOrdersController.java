package Controller;

import DB.DB;
import DTO.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ManageOrdersController {
    public AnchorPane ManagerOrders;
    public JFXTextField txtOrderID;
    public JFXTextField txtDate;
    public JFXTextField txtCustomerName;
    public JFXComboBox comboCustomerID;
    public JFXTextField txtItemDesc;
    public JFXComboBox comboItemCode;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public TableView<ManageOrdersTM> tableOrderDetails;
    public int total = 0;
    public Label labTotal;
    public JFXButton btnSave;
    public ImageView icnHome;

    public void initialize() {
        //Date
        SimpleDateFormat fomatter = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        txtDate.setText(fomatter.format(date));
        txtDate.setDisable(true);

        if (txtOrderID.getText().isEmpty()) {
            orderID();
        }

        comboCustomerID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

//                if(comboItemCode.getSelectionModel().getSelectedItem().equals(null)){
//                    return;
//                }
                if (comboCustomerID.getSelectionModel().getSelectedItem() != null) {
                    Object selectedItem = comboCustomerID.getSelectionModel().getSelectedItem();
                    ObservableList<TableModel> customers = FXCollections.observableList(DB.customer);
                    String ID = (String) selectedItem;
                    for (int i = 0; i < customers.size(); i++) {
                        if (ID.equals(customers.get(i).getId())) {
                            txtCustomerName.setText(customers.get(i).getName());
                            txtCustomerName.setDisable(true);
                        }
                    }
                }
            }
        });

        comboItemCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                Object selectedItem = comboItemCode.getSelectionModel().getSelectedItem();
                ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
                String ID = (String) selectedItem;
                if (comboItemCode.getSelectionModel().isEmpty()) {
                    return;
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (ID.equals(items.get(i).getItemCode())) {
                            if (items.get(i).getQtyOnHand() <= 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR,
                                        "Out of stock!",
                                        ButtonType.OK);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                                return;
                            } else {
                                txtItemDesc.setText(items.get(i).getItemDescription());
                                txtQtyOnHand.setText(Integer.toString(items.get(i).getQtyOnHand()));
                                txtUnitPrice.setText(Integer.toString(items.get(i).getUnitPrice()));
                                txtItemDesc.setDisable(true);
                                txtUnitPrice.setDisable(true);
                                txtQtyOnHand.setDisable(true);
                            }
                        }
                    }
                }
            }
        });
        tableOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ManageOrdersTM>() {
            @Override
            public void changed(ObservableValue<? extends ManageOrdersTM> observable, ManageOrdersTM oldValue, ManageOrdersTM newValue) {
                if (tableOrderDetails.getSelectionModel().isEmpty()) {
                    return;
                }

                btnSave.setText("Update");
                ManageOrdersTM selectedItem = tableOrderDetails.getSelectionModel().getSelectedItem();
                //   ObservableList<ManageOrdersTM> orders = tableOrderDetails.getItems();
                ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
                comboItemCode.getSelectionModel().select(selectedItem.getItemCode());
                txtItemDesc.setText(selectedItem.getItemDescription());
                for (int i = 0; i < items.size(); i++) {
                    if (selectedItem.getItemCode().equals(items.get(i).getItemCode())) {
                        txtQtyOnHand.setText(Integer.toString(items.get(i).getQtyOnHand()));
                    }
                }
                txtUnitPrice.setText(Integer.toString(selectedItem.getUnitPrice()));
                txtQty.setText(Integer.toString(selectedItem.getQty()));
                txtItemDesc.setDisable(true);
                txtUnitPrice.setDisable(true);
                txtQtyOnHand.setDisable(true);
            }
        });


        //set table columns
        tableOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tableOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tableOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        tableOrderDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("delete"));

        ObservableList<ManageOrdersTM> orders = FXCollections.observableList(DB.orders);
        tableOrderDetails.setItems(orders);

        //customers
        ObservableList<TableModel> customers = FXCollections.observableList(DB.customer);
        ObservableList custID = comboCustomerID.getItems();
        for (int i = 0; i < customers.size(); i++) {
            custID.add(customers.get(i).getId());
        }
        //items
        ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
        ObservableList itemID = comboItemCode.getItems();
        for (int i = 0; i < items.size(); i++) {
            itemID.add(items.get(i).getItemCode());
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

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        ObservableList<ManageOrdersTM> orderTable = FXCollections.observableList(DB.orders);
        //ObservableList<Orders> ordersTable = FXCollections.observableList(DB.placeOrder);
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();
        String itemCode = (String) comboItemCode.getSelectionModel().getSelectedItem();
        //orders.add(new Orders(txtDate.getText(),(String) comboCustomerID.getSelectionModel().getSelectedItem(),txtOrderID.getText(),orderDetails.add(new OrderDetails( itemCode,Integer.parseInt(txtQty.getText()),Integer.parseInt(txtUnitPrice.getText())))));

        ObservableList<ManageOrdersTM> orders = tableOrderDetails.getItems();

        for (int i = 0; i < orders.size(); i++) {
            orderDetails.add(new OrderDetails(orderTable.get(i).getItemCode(), orderTable.get(i).getQty(), orderTable.get(i).getUnitPrice()));
//            System.out.println("=======================================p;");
//            System.out.println(orderDetails.get(i).getItemCode());
//            System.out.println(orderDetails.get(i).getUnitPrice());
        }
        ObservableList<Orders> placeOrder = FXCollections.observableList(DB.placeOrder);
        placeOrder.add(new Orders(txtDate.getText(), (String) comboCustomerID.getSelectionModel().getSelectedItem(), txtOrderID.getText(), orderDetails));
        System.out.println("============================");
        System.out.println(placeOrder.toString());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "You have successfully placed the order.",
                ButtonType.OK);
        Optional<ButtonType> buttonType = alert.showAndWait();

        orderID();

        clearALL();
        return;
    }

    private void orderID() {
        // Generate a new id
        int maxId = 0;
        for (Orders orders : DB.placeOrder) {
            int id = Integer.parseInt(orders.getOrderID().replace("OD", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "OD00" + maxId;
        } else if (maxId < 100) {
            id = "OD0" + maxId;
        } else {
            id = "OD" + maxId;
        }
        txtOrderID.setText(id);
    }

    void clearALL() {
        btnNewOrder_OnAction(null);
        txtCustomerName.clear();
        tableOrderDetails.getItems().clear();
        labTotal.setText("0");
        comboCustomerID.getSelectionModel().clearSelection();
    }

    public void btnNewOrder_OnAction(ActionEvent actionEvent) {

        tableOrderDetails.getSelectionModel().clearSelection();
        //txtCustomerName.clear();
        btnSave.setText("Add to cart");
        txtItemDesc.clear();
        // txtOrderID.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        comboItemCode.getSelectionModel().clearSelection();
        //comboCustomerID.setDisable(true);
        comboCustomerID.requestFocus();


    }

    public void icnHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/View/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.ManagerOrders.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();


    }

//    public void comboCustomerID_OnAction(ActionEvent actionEvent) {
//       // ObservableList custID=comboCustomerID.getItems();
//        ObservableList<TableModel> customers=FXCollections.observableList(DB.customer);
//        String ID= (String) comboCustomerID.getSelectionModel().getSelectedItem();
//        for (int i = 0; i <customers.size() ; i++) {
//            if(ID.equals(customers.get(i).getId())){
//                txtCustomerName.setText(customers.get(i).getName());
//                txtCustomerName.setDisable(true);
//            }
//        }
//
//    }

//    public void comboItemCode_OnAction(ActionEvent actionEvent) {
//
//        ObservableList<ItemManageTM> items=FXCollections.observableList(DB.items);
//        String ID= (String) comboItemCode.getSelectionModel().getSelectedItem();
//        for (int i = 0; i <items.size() ; i++) {
//            if(ID.equals(items.get(i).getItemCode())){
//                txtItemDesc.setText(items.get(i).getItemDescription());
//                txtQtyOnHand.setText(Integer.toString(items.get(i).getQtyOnHand()));
//                txtUnitPrice.setText(Integer.toString(items.get(i).getUnitPrice()));
//                txtItemDesc.setDisable(true);
//                txtUnitPrice.setDisable(true);
//                txtQtyOnHand.setDisable(true);
//            }
//        }
//    }

    public void btnSave_OnAction(ActionEvent actionEvent) {

        ObservableList<ManageOrdersTM> orders = tableOrderDetails.getItems();
        ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);

        if (btnSave.getText().equals("Add to cart")) {

            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            String qtyy = txtQty.getText();

            if (txtQty.getText().isEmpty() || txtQty.getText().equals("0")) {

                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please add valid quontity!",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
                return;

            } else if (comboItemCode.getSelectionModel().isEmpty() || comboCustomerID.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select the customer ID and item ID",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
                return;
            } else if (qtyOnHand < Integer.parseInt(qtyy)) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Out of stock!",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
                return;
            } else {
                //ObservableList<ManageOrdersTM> orders=tableOrderDetails.getItems();
                int qty = Integer.parseInt(txtQty.getText());
                int uniPrice = Integer.parseInt(txtUnitPrice.getText());


                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getItemCode().equals(comboItemCode.getSelectionModel().getSelectedItem())) {
                        int newQty = orders.get(i).getQty() + Integer.parseInt(txtQty.getText());
                        orders.get(i).setQty(newQty);
                        int unitPrice = Integer.parseInt(txtUnitPrice.getText());
                        int newTotal = newQty * unitPrice;
                        orders.get(i).setTotal(newTotal);
                        tableOrderDetails.refresh();
                        System.out.println("Done editing.");
                        int finalTotal = 0;
                        for (int j = 0; j < orders.size(); j++) {
                            finalTotal += orders.get(i).getTotal();
                        }
                        labTotal.setText(Integer.toString(finalTotal));
                        for (int j = 0; j < items.size(); j++) {
                            if (items.get(j).getItemCode().equals(comboItemCode.getSelectionModel().getSelectedItem())) {
                                int qtyOH = items.get(j).getQtyOnHand();
                                items.get(j).setQtyOnHand(qtyOH - newQty);

                            }
                        }
                        return;

                    }
                }

                total = (qty * uniPrice);
                JFXButton button = new JFXButton("Delete");
                String itemCode = (String) comboItemCode.getSelectionModel().getSelectedItem();
                ManageOrdersTM object = new ManageOrdersTM((String) comboItemCode.getSelectionModel().getSelectedItem(), txtItemDesc.getText(), Integer.parseInt(txtQty.getText()), Integer.parseInt(txtUnitPrice.getText()), total, button);
                orders.add(object);

                button.setOnAction(event -> {
                    btnTableDelete_OnAction(object);
                });
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).getItemCode().equals(comboItemCode.getSelectionModel().getSelectedItem())) {
                        int qtyOH = items.get(j).getQtyOnHand();
                        items.get(j).setQtyOnHand(qtyOH - qty);
                    }
                }

                int finalTotal = 0;
                for (int i = 0; i < orders.size(); i++) {
                    finalTotal += orders.get(i).getTotal();
                }
                labTotal.setText(Integer.toString(finalTotal));
            }


        } else {
            System.out.println("Update");
            ManageOrdersTM item = tableOrderDetails.getSelectionModel().getSelectedItem();
            String ID = (String) comboCustomerID.getSelectionModel().getSelectedItem();
            ObservableList<ManageOrdersTM> table = tableOrderDetails.getItems();
//            for (int i = 0; i <orders.size() ; i++) {
//                if(item.getItemCode().equals(orders.get(i).getItemCode())){
//                    orders.get(i).setQty(Integer.parseInt(txtQty.getText()));
//                    int qty=orders.get(i).getQty();
//                    int unitPrice=orders.get(i).getUnitPrice();
//                    orders.get(i).setTotal(qty*unitPrice);
//
//                    int finalTotal=0;
//                    for (int j = 0; j <orders.size() ; j++) {
//                        finalTotal+=orders.get(i).getTotal();
//                    }
//                    labTotal.setText(Integer.toString(finalTotal));
//                    return;
//                }
//            }

            for (int i = 0; i < table.size(); i++) {
                if (item.getItemCode().equals(orders.get(i).getItemCode())) {
                    table.get(i).setQty(Integer.parseInt(txtQty.getText()));
                    int qty = table.get(i).getQty();
                    int unitPrice = table.get(i).getUnitPrice();
                    table.get(i).setTotal(qty * unitPrice);

                    int finalTotal = 0;
                    for (int j = 0; j < orders.size(); j++) {
                        finalTotal += table.get(i).getTotal();
                    }
                    labTotal.setText(Integer.toString(finalTotal));


                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Successfully updated the data..",
                            ButtonType.OK);
                    Optional<ButtonType> buttonType = alert.showAndWait();

                    for (int k = 0; k < items.size(); k++) {
                        if (items.get(k).getItemCode().equals(item.getItemCode())) {
                            int qtyOH = items.get(k).getQtyOnHand() + item.getQty();
                            int QTY = Integer.parseInt(txtQty.getText());
                            items.get(k).setQtyOnHand(qtyOH - QTY);
                        }
                    }
                    tableOrderDetails.refresh();
                    return;
                }
            }

        }
    }

    public void btnTableDelete_OnAction(ManageOrdersTM object) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
        if (buttonType.get() == ButtonType.YES) {
            int total = object.getTotal();
            int finalTotal = Integer.parseInt(labTotal.getText());
            tableOrderDetails.getItems().remove(object);
            labTotal.setText(Integer.toString(finalTotal - total));


            for (int j = 0; j < items.size(); j++) {
                if (items.get(j).getItemCode().equals(object.getItemCode())) {
                    int qOH = items.get(j).getQtyOnHand();
                    int qty = object.getQty();

                    items.get(j).setQtyOnHand(qOH + qty);
                }
            }
        }
    }

    public void txtQty_OnAction(ActionEvent actionEvent) {
        btnSave_OnAction(null);
    }

    public void initializeForSearchOrder(String orderID) {

        ObservableList<Orders> orders = FXCollections.observableList(DB.placeOrder);
        ObservableList<ItemManageTM> items = FXCollections.observableList(DB.items);
        ObservableList<TableModel> customers = FXCollections.observableList(DB.customer);

        ObservableList<ManageOrdersTM> table = FXCollections.observableArrayList();


        for (int i = 0; i < orders.size(); i++) {
            if (orderID.equals(orders.get(i).getOrderID())) {
                for (int j = 0; j < orders.get(i).getOrderDetails().size(); j++) {
                    String description = null;
                    String custName = null;
                    int qtyOnHand = 0;
                    for (int k = 0; k < items.size(); k++) {
                        if (orders.get(i).getOrderDetails().get(j).getItemCode().equals(items.get(k).getItemCode())) {
                            description = items.get(k).getItemDescription();
                            qtyOnHand = items.get(k).getQtyOnHand();
                        }
                    }
                    for (int k = 0; k < customers.size(); k++) {
                        if (orders.get(i).getCustomerID().equals(customers.get(k).getId())) {
                            custName = customers.get(k).getName();
                        }
                    }
                    int total = orders.get(i).getOrderDetails().get(j).getQty() * orders.get(i).getOrderDetails().get(j).getUnitPrice();

                    table.add(new ManageOrdersTM(orders.get(i).getOrderDetails().get(j).getItemCode(), description,
                            orders.get(i).getOrderDetails().get(j).getQty(), orders.get(i).getOrderDetails().get(j).getUnitPrice(),
                            total, new JFXButton()));

                    txtOrderID.setText(orderID);
                    txtOrderID.setDisable(true);
                    txtUnitPrice.setText(String.valueOf(orders.get(i).getOrderDetails().get(j).getUnitPrice()));
                    txtUnitPrice.setDisable(true);
                    txtQty.setText(String.valueOf(orders.get(i).getOrderDetails().get(j).getQty()));
                    txtQty.setDisable(true);
                    txtQtyOnHand.setText(String.valueOf(qtyOnHand));
                    txtQtyOnHand.setDisable(true);
                    txtDate.setText(orders.get(i).getDate());
                    txtItemDesc.setText(description);
                    txtItemDesc.setDisable(true);

                    String custID = orders.get(i).getCustomerID();

                    comboCustomerID.setPromptText(custID);
                    comboCustomerID.setDisable(true);

                    comboItemCode.setPromptText(orders.get(i).getOrderDetails().get(j).getItemCode());
                    comboItemCode.setDisable(true);

                    txtCustomerName.setText(custName);
                    txtCustomerName.setDisable(true);
                }
            }
        }
        tableOrderDetails.setItems(table);

    }
}
