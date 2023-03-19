package com.example.market;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class sell implements Initializable {
    @FXML
    ComboBox<String> pay_code;

    @FXML
    TextField num_copy;

    @FXML
    TextField priceFromUser;

    @FXML
    TextField returnPrice;

    @FXML
    TextField finalPrice;

    @FXML
    Button addToPaymnt;

    @FXML
    Button calc;

    @FXML
    Button removee;

    @FXML
    Button done;

    @FXML
    Button debit;

    @FXML
    private CheckBox forHome;

    @FXML
    TextField sale;

    @FXML
    TableView<PaymentItem> table;

    @FXML
    TableColumn<PaymentItem, String> ItemCodeee;

    @FXML
    TableColumn<PaymentItem, String> ItemeNameee;

    @FXML
    TableColumn<PaymentItem, Integer> NumberCop;
    @FXML
    TableColumn<PaymentItem, Double> onePrice;
    @FXML
    TableColumn<PaymentItem, Double> priceForAll;

    @FXML
    TextField item_price;

    static Stage soso = new Stage();

    String str = "";

    int prevNumOfCopy;

    ArrayList<PaymentItem> items = new ArrayList<>();

    private Alert error = new Alert(AlertType.ERROR);

    @SuppressWarnings("deprecation")
    public void addToPaymntAction(ActionEvent e) {
        String[] arr = pay_code.getValue().split("[$]");
        ResultSet s = appyQueryOnDataBase(
                "select item_name,final_price,num_of_copy from item where item_code = '" + arr[0] + "';");
        try {
            if (s.next()) {
                if (Integer.parseInt(s.getString(3)) >= Integer.parseInt(num_copy.getText())) {
                    boolean b = true;
                    for (int i = 0; i < items.size(); i++) {

                        if (items.get(i).getItemCodeee().equals(arr[0])) {
                            items.get(i)
                                    .setNumberCop(items.get(i).getNumberCop() + Integer.parseInt(num_copy.getText()));
                            items.get(i).setPriceForAll(items.get(i).getPriceForAll()
                                    + items.get(i).getOnePrice() * Integer.parseInt(num_copy.getText()));
                            b = false;
                            break;
                        }
                    }

                    if (b)
                        items.add(new PaymentItem(
                                Double.parseDouble(s.getString(2)) * Integer.parseInt(num_copy.getText()),
                                Double.parseDouble(s.getString(2)), Integer.parseInt(num_copy.getText()),
                                new Time(new Date().getHours(), new Date().getMinutes(), new Date().getSeconds()),
                                new Date(), s.getString(1), arr[0]));
                } else {
                    error.setContentText("لا يتوفر لديك الا " + s.getString(3) + " من '" + s.getString(1) + "'");
                    error.show();
                }
            } else {
                error.setContentText("هذا الكود ليس موجود !");
                error.show();
            }
            debit.setDisable(false);
            done.setDisable(false);
        } catch (Exception e1) {

            error.setContentText(e1.getMessage());
            error.show();
        }

        double price = 0;
        for (int i = 0; i < items.size(); i++)
            price += items.get(i).getPriceForAll();

        finalPrice.setText(price + "");

        ObservableList<PaymentItem> data = FXCollections.observableArrayList(items);
        table.getItems().clear();
        table.setItems(data);

        num_copy.setText("1");
    }

    public void enableAdd(ActionEvent e) {
        if (pay_code.getValue() != null && !pay_code.getValue().equals(" "))
            try {
                String[] arr = pay_code.getValue().split("[$]");
                ResultSet s = appyQueryOnDataBase(
                        "select item_name,final_price from item where item_code = '" + arr[0] + "';");
                if (s.next())
                    item_price.setText(((Math.round(Double.parseDouble(s.getString(2)) * 100)) / 100.0) + "");
                addToPaymnt.setDisable(false);
            } catch (Exception e1) {

            }
    }

    public void calc(KeyEvent e) {
        calc.setDisable(false);
    }

    public void calcAction(ActionEvent e) {
        returnPrice
                .setText(Double.parseDouble(priceFromUser.getText()) - Double.parseDouble(finalPrice.getText()) + "");
    }

    @SuppressWarnings("deprecation")
    public void doneActione(ActionEvent e) {

        try {

            applyOnDataBase("insert into payment(price,date_of_pay,time_of_pay,emp_name) value (" + finalPrice.getText()
                    + "-" + sale.getText() + ",'" + LocalDate.now() + "','" + new Date().getHours() + ":"
                    + new Date().getMinutes() + ":" + new Date().getSeconds() + "','" + Main.userLogin + "')");

            ResultSet ss = appyQueryOnDataBase("select max(payment_num) from payment;");

            ss.next();

            int paymentNum = Integer.parseInt(ss.getString(1));
            double profitSum = 0;
            for (int i = 0; i < items.size(); i++) {

                String[] arr = items.get(i).getItemCodeee().split("[$]");
                ResultSet rs = appyQueryOnDataBase(
                        "select price,super_item_code,sub_item_code,items_count,Num_of_copy,item_name from item where item_code = '"
                                + arr[0] + "';");
                rs.next();
                if (Integer.parseInt(rs.getString(5)) >= items.get(i).getNumberCop()) {
                    prevNumOfCopy = Integer.parseInt(rs.getString(5));

                    if (!forHome.isSelected()) {
                        double profit = items.get(i).getPriceForAll()
                                - (Double.parseDouble(rs.getString(1)) * items.get(i).getNumberCop());
                        profitSum += profit;
                        applyOnDataBase("insert into items_payment value (" + paymentNum + ",'" + arr[0] + "',"
                                + items.get(i).getOnePrice() + "," + items.get(i).getNumberCop() + ","
                                + items.get(i).getPriceForAll() + "," + profit + ");");
                    } else {
                        profitSum -= (Double.parseDouble(rs.getString(1)) * items.get(i).getNumberCop());
                        applyOnDataBase("insert into items_payment value (" + paymentNum + ",'" + arr[0] + "',"
                                + items.get(i).getOnePrice() + "," + items.get(i).getNumberCop() + ","
                                + items.get(i).getPriceForAll() + ",-" + rs.getString(1) + ");");
                    }
                    applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy - " + items.get(i).getNumberCop()
                            + " WHERE item_code = '" + arr[0] + "';");

                    if (rs.getString(2) != null && !rs.getString(2).equals("null")) {
                        int newNumOfCopy = prevNumOfCopy - items.get(i).getNumberCop();
                        int itemCount = Integer.parseInt(rs.getString(4));
                        int x = newNumOfCopy / itemCount;
                        applyOnDataBase("UPDATE item" + " SET num_of_copy = " + x + " WHERE item_code = '"
                                + rs.getString(2) + "';");
                    } else if (rs.getString(3) != null && !rs.getString(3).equals("null")) {
                        int itemCount = Integer.parseInt(rs.getString(4));
                        int x = items.get(i).getNumberCop() * itemCount;
                        applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy - " + x + " WHERE item_code = '"
                                + rs.getString(3) + "';");
                    }

                } else {
                    error.setContentText("لا يتوفر لديك الا " + rs.getString(5) + " من '" + rs.getString(6) + "'");
                    error.show();
                }
            }
            if (!forHome.isSelected())
                applyOnDataBase("update payment set profit = " + profitSum + "-" + sale.getText()
                        + " where payment_num = " + paymentNum);
            else
                applyOnDataBase("update payment set profit = " + profitSum + " where payment_num = " + paymentNum);

            items.clear();
            ObservableList<PaymentItem> data = FXCollections.observableArrayList(items);
            table.setItems(data);
            finalPrice.clear();
            item_price.clear();
            num_copy.setText("1");
            priceFromUser.setText("");
            returnPrice.setText("");
            pay_code.getEditor().setText("");
            pay_code.getItems().clear();
            pay_code.setValue(null);
            done.setDisable(true);
            debit.setDisable(true);
            addToPaymnt.setDisable(true);
            calc.setDisable(true);
            sale.setText("0");
        } catch (Exception e1) {

            error.setContentText(e1.getMessage());
            error.show();
        }

    }

    @FXML
    void remove(ActionEvent event) {
        items.remove((table.getSelectionModel().getSelectedItem()));
        ObservableList<PaymentItem> data = FXCollections.observableArrayList(items);
        table.setItems(data);
        double price = 0;
        for (int i = 0; i < items.size(); i++)
            price += items.get(i).getPriceForAll();

        pay_code.getEditor().setText("");
        finalPrice.setText(price + "");
        priceFromUser.setText("");
        returnPrice.setText("");
        item_price.setText("");
        num_copy.setText("1");
        calc.setDisable(true);

        if (price == 0) {
            sale.setText("0");
            done.setDisable(true);
            debit.setDisable(true);
        }
        removee.setDisable(true);
    }

    public void click(MouseEvent e) {
        removee.setDisable(false);
        if (e.getClickCount() == 2) {

            NumberCop.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            NumberCop.setOnEditCommit(e1 -> {
                try {
                    PaymentItem selected = e1.getTableView().getItems().get(e1.getTablePosition().getRow());

                    selected.setNumberCop(e1.getNewValue());
                    selected.setPriceForAll(e1.getNewValue() * selected.getOnePrice());
                    ResultSet rs = appyQueryOnDataBase(
                            "select price from item where item_code = '" + selected.getItemCodeee() + "';");
                    rs.next();
                    selected.setProfit(selected.getPriceForAll()
                            - (Double.parseDouble(rs.getString(1)) * selected.getNumberCop()));
                    double price = 0;
                    for (int i = 0; i < items.size(); i++)
                        price += items.get(i).getPriceForAll();

                    finalPrice.setText(price + "");
                } catch (SQLException e2) {
                }
            });

            /* Allow for the values in each cell to be changable */
            table.setEditable(true);
        }
    }

    public void debitActione(ActionEvent e) {

    }

    public void codeAction(KeyEvent e) {

        if (e.getText().length() > 0 || e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.SPACE)) {
            if (!str.equals(pay_code.getEditor().getText())) {
                str = pay_code.getEditor().getText();
                ResultSet s = appyQueryOnDataBase(
                        "select item_code,item_name from item where item_code like '" + pay_code.getEditor().getText()
                                + "%'" + " or item_name like '%" + pay_code.getEditor().getText() + "%';");

                try {
                    pay_code.getItems().clear();
                    while (s.next())
                        pay_code.getItems().add(s.getString(1) + "$" + s.getString(2));

                    pay_code.show();

                } catch (Exception e1) {

                    error.setContentText(e1.getMessage());
                    error.show();
                }
            }
        }
    }

    ResultSet appyQueryOnDataBase(String string) {

        try {
            Statement stmt = Main.con.createStatement();
            ResultSet rs = stmt.executeQuery(string);
            return rs;
        } catch (Exception e) {
            error.setContentText(string);
            error.show();
        }

        return null;

    }

    boolean applyOnDataBase(String string) {

        try {
            Statement stmt = Main.con.createStatement();
            boolean rs = stmt.execute(string);
            return rs;
        } catch (Exception e) {
            error.setContentText(string);
            error.show();
        }
        return false;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        try {
            Scene s = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
            s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            soso.setScene(s);
            soso.setTitle("المنتجات والفواتير   ");
            soso.getIcons().add(new Image("icons8-market-64.png"));
            soso.setResizable(false);
            soso.show();
            soso.toBack();
            Main.primaryStage.toFront();
        } catch (Exception e1) {

            error.setContentText(e1.getMessage());
            error.show();
        }
        pay_code.getEditor().setOnKeyTyped(e -> codeAction(e));
        pay_code.getEditor().setOnKeyPressed(e -> codeAction(e));
        pay_code.getEditor().setOnKeyReleased(e -> codeAction(e));
        pay_code.getEditor().setAlignment(Pos.CENTER);

        ItemCodeee.setCellValueFactory(new PropertyValueFactory<PaymentItem, String>("ItemCodeee"));
        ItemeNameee.setCellValueFactory(new PropertyValueFactory<PaymentItem, String>("ItemeNameee"));
        ;
        NumberCop.setCellValueFactory(new PropertyValueFactory<PaymentItem, Integer>("NumberCop"));
        onePrice.setCellValueFactory(new PropertyValueFactory<PaymentItem, Double>("onePrice"));
        priceForAll.setCellValueFactory(new PropertyValueFactory<PaymentItem, Double>("priceForAll"));

        ItemCodeee.setStyle("-fx-alignment: CENTER;");
        ItemeNameee.setStyle("-fx-alignment: CENTER;");
        NumberCop.setStyle("-fx-alignment: CENTER;");
        onePrice.setStyle("-fx-alignment: CENTER;");
        priceForAll.setStyle("-fx-alignment: CENTER;");

    }

}
