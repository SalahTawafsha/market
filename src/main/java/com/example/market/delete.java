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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class delete implements Initializable {

	@FXML
	private VBox box;

	@FXML
	private Button deleteButton;

	@FXML
	AnchorPane pane;

	ArrayList<Payment> payments = new ArrayList<>();

	TableView<Payment> table;

	int prevNumOfCopy;

	private Alert error = new Alert(AlertType.ERROR);

	ArrayList<PaymentItem> items = new ArrayList<>();

	@FXML
	void deleteAction(ActionEvent event) {
		Integer paymentNumber = table.getSelectionModel().getSelectedItem().getPaymentNumber();

		ResultSet s = appyQueryOnDataBase("select * from items_payment where payment_num = " + paymentNumber + ";");

		try {
			items.clear();
			while (s.next()) {

				ResultSet rs = appyQueryOnDataBase("select item_name from item where item_code = " + s.getString(2));
				rs.next();
				items.add(new PaymentItem(Double.parseDouble(s.getString(5)), Double.parseDouble(s.getString(3)),
						Integer.parseInt(s.getString(4)), rs.getString(1), s.getString(2),
						(Math.round(Double.parseDouble(s.getString(6)) * 100)) / 100.0));

			}

			for (int i = 0; i < items.size(); i++) {

				ResultSet rs = appyQueryOnDataBase(
						"select price,super_item_code,sub_item_code,items_count,Num_of_copy from item where item_code = '"
								+ items.get(i).getItemCodeee() + "';");
				rs.next();
				prevNumOfCopy = Integer.parseInt(rs.getString(5));

				applyOnDataBase("delete from items_payment where payment_num = " + paymentNumber + " and item_code = '"
						+ items.get(i).getItemCodeee() + "';");

				applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy + " + items.get(i).getNumberCop()
						+ " WHERE item_code = '" + items.get(i).getItemCodeee() + "';");

				if (!rs.getString(2).equals("null")) {
					int newNumOfCopy = prevNumOfCopy - items.get(i).getNumberCop();
					int itemCount = Integer.parseInt(rs.getString(4));
					int x = newNumOfCopy / itemCount;
					applyOnDataBase("UPDATE item" + " SET num_of_copy = " + x + " WHERE item_code = '" + rs.getString(2)
							+ "';");
				} else if (!rs.getString(3).equals("null")) {
					int itemCount = Integer.parseInt(rs.getString(4));
					int x = items.get(i).getNumberCop() * itemCount;
					applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy + " + x + " WHERE item_code = '"
							+ rs.getString(3) + "';");
				}

			}
			applyOnDataBase("delete from payment where payment_num = " + paymentNumber);
			table.getItems().remove(new Payment(paymentNumber, null, null, null, null, null));

		} catch (Exception e) {

			error.setContentText(e.getMessage());
			error.show();
		}

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.primaryStage.getScene().getStylesheets()
				.setAll(getClass().getResource("application.css").toExternalForm());
		addTable();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void addTable() {
		table = new TableView<>();

		table.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) // Checking double click
			{
				showDetail(table.getSelectionModel().getSelectedItem().getPaymentNumber());
				return;
			}
			deleteButton.setDisable(false);
		});

		TableColumn<Payment, Integer> paymentNumberr = new TableColumn<>("رقم الفاتورة");

		TableColumn<Payment, Double> priceColumn = new TableColumn<>("السعر");

		TableColumn<Payment, Date> dateSell = new TableColumn<>("تاريخ البيع");

		TableColumn<Payment, Time> timeSell = new TableColumn<>("وقت البيع");

		TableColumn<Payment, String> empNameColumn = new TableColumn<>("اسم البائع");

		TableColumn<Payment, Double> profitColumn = new TableColumn<>("الربح");

		payments.clear();

		paymentNumberr.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("paymentNumber"));
		paymentNumberr.setPrefWidth(109);

		priceColumn.setCellValueFactory(new PropertyValueFactory<Payment, Double>("priceForAll"));
		priceColumn.setPrefWidth(109);

		dateSell.setCellValueFactory(new PropertyValueFactory<Payment, Date>("dateSell"));
		dateSell.setPrefWidth(200);

		timeSell.setCellValueFactory(new PropertyValueFactory<Payment, Time>("timeSell"));
		timeSell.setPrefWidth(109);

		empNameColumn.setCellValueFactory(new PropertyValueFactory<Payment, String>("empName"));
		empNameColumn.setPrefWidth(109);

		profitColumn.setCellValueFactory(new PropertyValueFactory<Payment, Double>("profit"));
		profitColumn.setPrefWidth(109);

		table.getColumns().addAll(empNameColumn, profitColumn, priceColumn, timeSell, dateSell, paymentNumberr);
		paymentNumberr.setStyle("-fx-alignment: CENTER;");
		priceColumn.setStyle("-fx-alignment: CENTER;");
		dateSell.setStyle("-fx-alignment: CENTER;");
		timeSell.setStyle("-fx-alignment: CENTER;");
		empNameColumn.setStyle("-fx-alignment: CENTER;");
		profitColumn.setStyle("-fx-alignment: CENTER;");

		Label fromDate = new Label("من تاريخ");
		fromDate.setFont(new Font("Arial Black", 24));
		fromDate.setStyle("-fx-background-radius: 20;");
		fromDate.setPrefHeight(70);

		DatePicker fromDatePicker = new DatePicker(LocalDate.of(2022, 9, 20));

		HBox fromBox = new HBox(10, fromDatePicker, fromDate);
		fromBox.setAlignment(Pos.CENTER);
		fromBox.setPrefHeight(70);

		Label toDate = new Label("حتى تاريخ");
		toDate.setFont(new Font("Arial Black", 24));
		toDate.setStyle("-fx-background-radius: 20;");
		toDate.setPrefHeight(70);

		DatePicker toDatePicker = new DatePicker(LocalDate.now());

		HBox toDateBox = new HBox(10, toDatePicker, toDate);
		toDateBox.setAlignment(Pos.CENTER);
		toDateBox.setPrefHeight(70);

		fromDatePicker.setOnAction(e -> {
			ResultSet rs = appyQueryOnDataBase(
					"select * from payment where date_of_pay >= '" + fromDatePicker.getValue()
							+ "' and date_of_pay <= '" + toDatePicker.getValue() + "' order by payment_num desc;");

			try {
				payments.clear();

				while (rs.next()) {
					String[] dateSelll = rs.getString(3).split("-");
					String[] timeSelll = rs.getString(4).split(":");

					payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
							new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
									Integer.parseInt(dateSelll[2])),
							new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
									Integer.parseInt(timeSelll[2])),
							rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));
				}

				ObservableList<Payment> data = FXCollections.observableArrayList(payments);
				table.setItems(data);

			} catch (Exception e1) {
				error.setContentText(e1.getMessage() + "\n" + e1);
				error.show();
			}
		});

		toDatePicker.setOnAction(e -> {
			ResultSet rs = appyQueryOnDataBase(
					"select * from payment where date_of_pay >= '" + fromDatePicker.getValue()
							+ "' and date_of_pay <= '" + toDatePicker.getValue() + "' order by payment_num desc;");

			try {
				payments.clear();

				while (rs.next()) {
					String[] dateSelll = rs.getString(3).split("-");
					String[] timeSelll = rs.getString(4).split(":");

					payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
							new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
									Integer.parseInt(dateSelll[2])),
							new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
									Integer.parseInt(timeSelll[2])),
							rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));
				}

				ObservableList<Payment> data = FXCollections.observableArrayList(payments);
				table.setItems(data);

			} catch (Exception e1) {

				error.setContentText(e1.getMessage() + "\n" + e1);
				error.show();
			}
		});

		HBox boxx = new HBox(20, toDateBox, fromBox);
		boxx.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10, boxx, table);
		vbox.setAlignment(Pos.CENTER);

		ResultSet rs = appyQueryOnDataBase("select * from payment order by payment_num desc;");

		try {
			while (rs.next()) {
				String[] dateSelll = rs.getString(3).split("-");
				String[] timeSelll = rs.getString(4).split(":");

				payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
						new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
								Integer.parseInt(dateSelll[2])),
						new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
								Integer.parseInt(timeSelll[2])),
						rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));

			}

		} catch (Exception e1) {

			error.setContentText(e1.getMessage() + "\n" + e1);
			error.show();
		}

		ObservableList<Payment> data = FXCollections.observableArrayList(payments);
		table.setItems(data);

		if (box.getChildren().size() == 1)
			box.getChildren().add(0, vbox);
		else {
			box.getChildren().set(0, vbox);
			box.getChildren().set(1, deleteButton);
			deleteButton.setDisable(true);
		}

	}

	@SuppressWarnings({ "unchecked" })

	private void showDetail(Integer paymentNumber) {

		TableView<PaymentItem> table = new TableView<>();

		TableColumn<PaymentItem, String> ItemCodeee = new TableColumn<>("كود المنتج");

		TableColumn<PaymentItem, String> ItemeNameee = new TableColumn<>("اسم المنتج");

		TableColumn<PaymentItem, Integer> NumberCop = new TableColumn<>("عدد النسخ");

		TableColumn<PaymentItem, Double> onePrice = new TableColumn<>("سعر القطعة");

		TableColumn<PaymentItem, Double> priceForAll = new TableColumn<>("السعر الاجمالي");

		TableColumn<PaymentItem, Double> profitColumn = new TableColumn<>("الربح");

		ItemCodeee.setCellValueFactory(new PropertyValueFactory<PaymentItem, String>("ItemCodeee"));
		ItemCodeee.setPrefWidth(122);

		ItemeNameee.setCellValueFactory(new PropertyValueFactory<PaymentItem, String>("ItemeNameee"));
		ItemeNameee.setPrefWidth(222);

		NumberCop.setCellValueFactory(new PropertyValueFactory<PaymentItem, Integer>("NumberCop"));
		NumberCop.setPrefWidth(100);

		onePrice.setCellValueFactory(new PropertyValueFactory<PaymentItem, Double>("onePrice"));
		onePrice.setPrefWidth(100);

		priceForAll.setCellValueFactory(new PropertyValueFactory<PaymentItem, Double>("priceForAll"));
		priceForAll.setPrefWidth(103);

		profitColumn.setCellValueFactory(new PropertyValueFactory<PaymentItem, Double>("profit"));
		profitColumn.setPrefWidth(100);

		table.getColumns().addAll(profitColumn, priceForAll, NumberCop, onePrice, ItemeNameee, ItemCodeee);

		ItemCodeee.setStyle("-fx-alignment: CENTER;");
		ItemeNameee.setStyle("-fx-alignment: CENTER;");
		NumberCop.setStyle("-fx-alignment: CENTER;");
		onePrice.setStyle("-fx-alignment: CENTER;");
		priceForAll.setStyle("-fx-alignment: CENTER;");
		profitColumn.setStyle("-fx-alignment: CENTER;");

		box.getChildren().clear();

		Button back = new Button("رجوع");
		back.setFont(new Font("Berlin Sans FB", 16));
		back.setStyle("-fx-background-radius: 20;");
		back.setPrefWidth(116);
		back.setPrefHeight(54);

		Button delete = new Button("حذف المنتج من الفاتورة");
		delete.setFont(new Font("Berlin Sans FB", 16));
		delete.setStyle("-fx-background-radius: 20;");
		delete.setPrefHeight(54);
		delete.setDisable(true);

		HBox control = new HBox(20, delete, back);
		control.setAlignment(Pos.CENTER);

		back.setOnAction(e -> addTable());

		table.setOnMouseClicked(e -> delete.setDisable(false));

		delete.setOnAction(e -> {

			PaymentItem p = table.getSelectionModel().getSelectedItem();
			ResultSet rs = appyQueryOnDataBase(
					"select price,super_item_code,sub_item_code,items_count,Num_of_copy from item where item_code = '"
							+ p.getItemCodeee() + "';");
			try {
				rs.next();
				
				table.getItems().remove(p);

				prevNumOfCopy = Integer.parseInt(rs.getString(5));

				applyOnDataBase("delete from items_payment where payment_num = " + paymentNumber + " and item_code = '"
						+ p.getItemCodeee() + "';");

				applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy + " + p.getNumberCop()
						+ " WHERE item_code = '" + p.getItemCodeee() + "';");

				if (rs.getString(2) != null && !rs.getString(2).equals("null")) {
					int newNumOfCopy = prevNumOfCopy - p.getNumberCop();
					int itemCount = Integer.parseInt(rs.getString(4));
					int x = newNumOfCopy / itemCount;
					applyOnDataBase("UPDATE item" + " SET num_of_copy = " + x + " WHERE item_code = '" + rs.getString(2)
							+ "';");
				} else if (rs.getString(3) != null && !rs.getString(3).equals("null")) {
					int itemCount = Integer.parseInt(rs.getString(4));
					int x = p.getNumberCop() * itemCount;
					applyOnDataBase("UPDATE item" + " SET num_of_copy = num_of_copy + " + x + " WHERE item_code = '"
							+ rs.getString(3) + "';");
				}

				applyOnDataBase("update payment set profit = profit - " + p.getProfit() + ";");

				
			} catch (SQLException e1) {

				error.setContentText(e1.getMessage());
				error.show();
			}

		});

		box.getChildren().addAll(table, control);

		ResultSet s = appyQueryOnDataBase("select * from items_payment where payment_num = " + paymentNumber + ";");

		try {
			items.clear();
			while (s.next()) {

				ResultSet rs = appyQueryOnDataBase("select item_name from item where item_code = " + s.getString(2));
				rs.next();
				items.add(new PaymentItem(Double.parseDouble(s.getString(5)), Double.parseDouble(s.getString(3)),
						Integer.parseInt(s.getString(4)), rs.getString(1), s.getString(2),
						(Math.round(Double.parseDouble(s.getString(6)) * 100)) / 100.0));

			}

			ObservableList<PaymentItem> data = FXCollections.observableArrayList(items);
			table.setItems(data);

		} catch (Exception e1) {

			error.setContentText(e1.getMessage() + "\n" + e1);
			error.show();
		}

	}

}
