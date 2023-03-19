package com.example.market;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    static Connection con;
    private Alert error = new Alert(AlertType.ERROR);
    static String userLogin = "salah";
    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        boolean b = connectDataBase();
        if (b) {
            try {
                ResultSet rs = appyQueryOnDataBase("select * from item;");
                ResultSet r = appyQueryOnDataBase("select * from items_payment;");
                ResultSet s = appyQueryOnDataBase("select * from payment;");
                PrintWriter itemsPrintWriter = new PrintWriter(new File("items.txt"));
                PrintWriter itemsPaymentPrintWriter = new PrintWriter(new File("items_payment.txt"));
                PrintWriter paymentPrintWriter = new PrintWriter(new File("payment.txt"));

                while (rs.next()) {
                    String[] propDate = rs.getString(3).split("-");
                    String[] expDate = rs.getString(4).split("-");
                    itemsPrintWriter.printf(
                            "insert into item value('%s','%s','%d-%d-%d','%d-%d-%d',%f,%f,%d,%f,'%s','%s','%s');\n",
                            rs.getString(1), rs.getString(2), Integer.parseInt(propDate[0]),
                            Integer.parseInt(propDate[1]), Integer.parseInt(propDate[2]), Integer.parseInt(expDate[0]),
                            Integer.parseInt(expDate[1]), Integer.parseInt(expDate[2]),
                            Double.parseDouble(rs.getString(5)), Double.parseDouble(rs.getString(6)),
                            Integer.parseInt(rs.getString(7)), Double.parseDouble(rs.getString(8)), rs.getString(9),
                            rs.getString(10), rs.getString(11));

                }

                while (s.next()) {
                    String[] payDate = s.getString(3).split("[-]");
                    String[] time = s.getString(4).split("[:]");

                    paymentPrintWriter.printf("insert into payment value(%s,%s,'%d-%d-%d','%s:%s','%s',%s);\n",
                            s.getString(1), s.getString(2), Integer.parseInt(payDate[0]), Integer.parseInt(payDate[1]),
                            Integer.parseInt(payDate[2]), time[0], time[1], s.getString(5), s.getString(6));

                }

                while (r.next()) {
                    itemsPaymentPrintWriter.printf("insert into items_payment value(%s,'%s',%s,%s,%s,%s);\n",
                            r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5),
                            r.getString(6));
                }
                paymentPrintWriter.close();
                itemsPrintWriter.close();
                itemsPaymentPrintWriter.close();

                primaryStage.setTitle("بقالة أبو العليا   ");
                primaryStage.getIcons().add(new Image("icons8-market-64.png"));
                primaryStage.setResizable(false);
                Main.primaryStage = primaryStage;
                login(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
                error.setContentText(e.getMessage());
                error.show();
            }
        }

    }

    ResultSet appyQueryOnDataBase(String string) {

        try {
            Statement stmt = Main.con.createStatement();
            ResultSet rs = stmt.executeQuery(string);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            error.setContentText(string);
            error.show();
        }

        return null;

    }

    private void login(Stage primaryStage) {
        Label l = new Label("اسم المستخدم:");
        l.setTextFill(Color.BLACK);
        l.setFont(new Font("Arial Black", 24));
        l.setPrefWidth(177);
        l.setPrefHeight(44);

        TextField t = new TextField("صلاح");
        t.setStyle("-fx-background-radius: 20;");
        t.setPrefWidth(258);
        t.setPrefHeight(44);
        t.setAlignment(Pos.CENTER);

        HBox name = new HBox(t, l);
        name.setAlignment(Pos.CENTER);
        name.setSpacing(20);

        Button b = new Button("تسجيل دخول");
        b.setFont(new Font("Berlin Sans FB", 16));
        b.setStyle("-fx-background-radius: 20;");
        b.setPrefWidth(116);
        b.setPrefHeight(54);

        b.setOnAction(e -> {
            try {
                userLogin = t.getText();
                VBox root = FXMLLoader.load(getClass().getResource("interface.fxml"));
                Scene s = new Scene(root);
                primaryStage.setScene(s);
            } catch (Exception e1) {
                e1.printStackTrace();
                error.setContentText(e1.getMessage());
                error.show();
            }
        });

        t.setOnAction(e -> {
            try {
                userLogin = t.getText();
                VBox root = FXMLLoader.load(getClass().getResource("interface.fxml"));
                Scene s = new Scene(root);
                primaryStage.setScene(s);
            } catch (Exception e1) {
                e1.printStackTrace();
                error.setContentText(e1.getMessage());
                error.show();
            }
        });

        VBox all = new VBox(40, name, b);
        all.setAlignment(Pos.CENTER);
        all.setBackground(new Background(new BackgroundImage(new Image("brown.png"), null, null, null, null)));
        Scene s = new Scene(all, 1087, 729);
        s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.centerOnScreen();
        primaryStage.setScene(s);
        primaryStage.show();
    }

    private boolean connectDataBase() {

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/" + "market" + "?autoReconnect=true&useSSL=false", "salah",
                    "Salah2023");
            return true;
        } catch (SQLException ee) {
            ee.printStackTrace();
            error.setContentText("Can't connect to database");
            error.show();
            return false;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
