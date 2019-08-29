package Controller;

import DB.DB;
import DTO.Orders;
import DTO.SearchTM;
import DTO.TableModel;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class SearchController {

    public JFXTextField txtSearch;
    public TableView<SearchTM> tableResult;
    public AnchorPane searchPane;
    public ImageView icnHome;


    public void initialize() {

        tableResult.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderID"));
        tableResult.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tableResult.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("total"));
        tableResult.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tableResult.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("customerName"));

        //ObservableList<SearchTM> search = FXCollections.observableList(DB.search);
        ObservableList<SearchTM> search = tableResult.getItems();
        ObservableList<Orders> placeOrder = FXCollections.observableList(DB.placeOrder);

        if (placeOrder.size() > 0) {
            //ArrayList<OrderDetails> orderDetails = new ArrayList<>();
            ObservableList<TableModel> customer = FXCollections.observableList(DB.customer);
            for (int i = 0; i < placeOrder.size(); i++) {
                for (int j = 0; j < placeOrder.get(i).getOrderDetails().size(); j++) {
                    //orderDetails.add(placeOrder.get(i).getOrderDetails().get(j));a
                    String customerName = null;
                    for (int k = 0; k < customer.size(); k++) {
                        if (placeOrder.get(i).getCustomerID() == customer.get(k).getId()) {
                            System.out.println("Found!");
                            customerName = customer.get(k).getName();
                            break;
                        } else {
                            System.out.println("User not found.");
                        }
                    }
                    int total = (placeOrder.get(i).getOrderDetails().get(j).getQty()) * (placeOrder.get(i).getOrderDetails().get(j).getUnitPrice());
                    search.add(new SearchTM(placeOrder.get(i).getOrderID(), placeOrder.get(i).getDate(), total, placeOrder.get(i).getCustomerID(), customerName));

                }
            }
//
        }
        tableResult.setItems(search);

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String searchText = txtSearch.getText();
                ObservableList<SearchTM> tempOrders = FXCollections.observableArrayList();

                for (SearchTM tempSearch : search) {
                    String total = Integer.toString(tempSearch.getTotal());
                    if (tempSearch.getOrderID().contains(searchText) || tempSearch.getDate().contains(searchText) || tempSearch.getCustomerID().contains(searchText) || tempSearch.getCustomerName().contains(searchText) || total.contains(searchText)) {
                        tempOrders.add(tempSearch);
                    }
                }
                tableResult.setItems(tempOrders);
            }
        });
    }

    public void icnHome_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/View/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.searchPane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
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

    public void tableResult_OnAction(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            URL resource = this.getClass().getResource("/View/ManageOrders.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent root = fxmlLoader.load();
            Scene placeOrderScene = new Scene(root);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(placeOrderScene);
            secondaryStage.centerOnScreen();
            secondaryStage.setTitle("View Orders");
            secondaryStage.setResizable(false);
            secondaryStage.show();

            ManageOrdersController contr = fxmlLoader.getController();

            SearchTM selectedOrder = tableResult.getSelectionModel().getSelectedItem();

            String orderID = selectedOrder.getOrderID();

            contr.initializeForSearchOrder(orderID);

        }
    }
}
