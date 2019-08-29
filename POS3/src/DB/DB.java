package DB;

import DTO.*;

import java.util.ArrayList;

public class DB {

    public static ArrayList<TableModel> customer = new ArrayList<>();
    public static ArrayList<ItemManageTM> items = new ArrayList<>();
    public static ArrayList<ManageOrdersTM> orders = new ArrayList<>();
    public static ArrayList<Orders> placeOrder = new ArrayList<>();
    public static ArrayList<SearchTM> search = new ArrayList<>();
    public static int nextIndex = 4;

    static {
        customer.add(new TableModel("C001", "James", "Gampaha"));
        customer.add(new TableModel("C002", "Robin", "Matara"));
        customer.add(new TableModel("C003", "Patrick", "Jaffna"));

        items.add(new ItemManageTM("I001", "Soap", 50, 30));
        items.add(new ItemManageTM("I002", "Ganja", 30, 100));
        items.add(new ItemManageTM("I003", "Cocaine", 65, 60));

        //  orders.add(new ManageOrdersTM("I001","Sap",5,120,10,new JFXButton("delete")));


//        private String orderID;
//        private String date;
//        private int total;
//        private String customerID;
//        private String customerName;

    }


}
