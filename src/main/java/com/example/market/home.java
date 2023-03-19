package com.example.market;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class home implements Initializable {

	@FXML
	Text allItemsCount;

	@FXML
	Text paymentes;

	@FXML
	private VBox box;

	@FXML
	private HBox booxxx;

	@FXML
	private VBox allItems;

	@FXML
	private VBox allPayment;

	private Alert error = new Alert(AlertType.ERROR);
	ArrayList<Payment> payments = new ArrayList<>();
	ArrayList<Item> items = new ArrayList<>();
	ArrayList<PaymentItem> PaymentItems = new ArrayList<>();

	@SuppressWarnings({ "unchecked", "deprecation" })
	@FXML
	void showAllItems(MouseEvent event) {
		Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		TableView<Item> table = new TableView<>();

		TableColumn<Item, String> item_code = new TableColumn<>("كود المنتج");
		item_code.setPrefWidth(120);

		TableColumn<Item, String> item_name = new TableColumn<>("إسم المنتج");
		item_name.setPrefWidth(222);

		TableColumn<Item, Date> Production_date = new TableColumn<>("تاريخ الإنتاج");
		Production_date.setPrefWidth(99);

		TableColumn<Item, Date> expiry_date = new TableColumn<>("تاريخ الإنتهاء");
		expiry_date.setPrefWidth(99);

		TableColumn<Item, Integer> num_of_copy = new TableColumn<>("عدد النسخ");
		num_of_copy.setPrefWidth(60);

		TableColumn<Item, Double> final_price = new TableColumn<>("سعر القطعة");

		TableColumn<Item, Double> price = new TableColumn<>("سعر الجملة");

		items.clear();

		item_code.setCellValueFactory(new PropertyValueFactory<Item, String>("item_code"));
		item_name.setCellValueFactory(new PropertyValueFactory<Item, String>("item_name"));
		Production_date.setCellValueFactory(new PropertyValueFactory<Item, Date>("Production_date"));
		expiry_date.setCellValueFactory(new PropertyValueFactory<Item, Date>("expiry_date"));
		num_of_copy.setCellValueFactory(new PropertyValueFactory<Item, Integer>("num_of_copy"));
		final_price.setCellValueFactory(new PropertyValueFactory<Item, Double>("final_price"));
		price.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));

		table.getColumns().addAll(num_of_copy, final_price, price, expiry_date, Production_date, item_name, item_code);
		item_code.setStyle("-fx-alignment: CENTER;");
		item_name.setStyle("-fx-alignment: CENTER;");
		Production_date.setStyle("-fx-alignment: CENTER;");
		expiry_date.setStyle("-fx-alignment: CENTER;");
		num_of_copy.setStyle("-fx-alignment: CENTER;");
		final_price.setStyle("-fx-alignment: CENTER;");
		price.setStyle("-fx-alignment: CENTER;");

		ResultSet rs = appyQueryOnDataBase("select * from item order by item_name;");

		try {
			while (rs.next()) {
				String[] propDate = rs.getString(3).split("-");
				String[] expDate = rs.getString(4).split("-");

				// (String item_code, String item_name, Date production_date, Date expiry_date,
				// double price,
//				double final_price, int num_of_copy) 
				items.add(new Item(rs.getString(1), rs.getString(2),
						new Date(Integer.parseInt(propDate[0]) - 1900, Integer.parseInt(propDate[1]) - 1,
								Integer.parseInt(propDate[2])),
						new Date(Integer.parseInt(expDate[0]) - 1900, Integer.parseInt(expDate[1]) - 1,
								Integer.parseInt(expDate[2])),
						Double.parseDouble(rs.getString(5)), Double.parseDouble(rs.getString(6)),
						Integer.parseInt(rs.getString(7))));
			}
		} catch (Exception e1) {

			error.setContentText(e1.getMessage() + "\n" + e1);
			error.show();
		}

		ObservableList<Item> data = FXCollections.observableArrayList(items);
		table.setItems(data);

		Label l = new Label("بحث من خلال الاسم:");
		l.setTextFill(Color.BLACK);
		l.setFont(new Font("Arial Black", 24));

		l.setPrefHeight(44);

		TextField t = new TextField();
		t.setStyle("-fx-background-radius: 20;");
		t.setPrefWidth(258);
		t.setPrefHeight(44);
		t.setAlignment(Pos.CENTER);

		t.setOnKeyTyped(e -> {
			items.clear();

			ResultSet rss = appyQueryOnDataBase(
					"select * from item where item_name like '%" + t.getText() + "%' order by item_name;");

			try {
				while (rss.next()) {

					String[] propDate = rss.getString(3).split("-");
					String[] expDate = rss.getString(4).split("-");

					items.add(new Item(rss.getString(1), rss.getString(2),
							new Date(Integer.parseInt(propDate[0]) - 1900, Integer.parseInt(propDate[1]) - 1,
									Integer.parseInt(propDate[2])),
							new Date(Integer.parseInt(expDate[0]) - 1900, Integer.parseInt(expDate[1]) - 1,
									Integer.parseInt(expDate[2])),
							Double.parseDouble(rss.getString(5)), Double.parseDouble(rss.getString(6)),
							Integer.parseInt(rss.getString(7))));
				}
			} catch (Exception e1) {

				error.setContentText(e1.getMessage() + "\n" + e1);
				error.show();
			}

			ObservableList<Item> dataa = FXCollections.observableArrayList(items);
			table.setItems(dataa);
		});

		HBox search = new HBox(t, l);
		search.setAlignment(Pos.CENTER);
		search.setSpacing(20);

		VBox all = new VBox(20, search, table);

		if (box.getChildren().size() == 1)
			box.getChildren().add(all);
		else
			box.getChildren().set(1, all);
	}

	@FXML
	void getCounts() {
		ResultSet s = appyQueryOnDataBase("select count(i.item_code) from item i;");
		ResultSet rs = appyQueryOnDataBase("select count(p.payment_num) from payment p;");

		try {
			s.next();
			rs.next();

			allItemsCount.setText(s.getString(1));
			paymentes.setText(rs.getString(1));

		} catch (Exception e) {
			error.setContentText(e.getMessage());
			error.show();
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

	@SuppressWarnings({ "deprecation", "unchecked" })
	@FXML
	void showAllPayment(MouseEvent event) {
		Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		TableView<Payment> table = new TableView<>();

		table.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) // Checking double click
			{
				showDetail(table.getSelectionModel().getSelectedItem().getPaymentNumber());
			}
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

		Label profitSum = new Label("مجموع الارباح:");
		profitSum.setFont(new Font("Arial Black", 24));
		profitSum.setStyle("-fx-background-radius: 20;");
		profitSum.setPrefHeight(70);

		TextField profitSumField = new TextField();
		profitSumField.setAlignment(Pos.CENTER);
		profitSumField.setStyle("-fx-background-radius: 20;");
		profitSumField.setPrefWidth(116);
		profitSumField.setPrefHeight(70);

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

		HBox text = new HBox(10, profitSumField, profitSum);
		text.setAlignment(Pos.CENTER);
		text.setPrefHeight(70);

		Label sellSum = new Label("مجموع المبيعات:");
		sellSum.setFont(new Font("Arial Black", 24));
		sellSum.setStyle("-fx-background-radius: 20;");
		sellSum.setPrefHeight(70);

		TextField sellSumField = new TextField();
		sellSumField.setAlignment(Pos.CENTER);
		sellSumField.setStyle("-fx-background-radius: 20;");
		sellSumField.setPrefWidth(116);
		sellSumField.setPrefHeight(70);

		HBox text1 = new HBox(10, sellSumField, sellSum);
		text1.setAlignment(Pos.CENTER);
		text1.setPrefHeight(70);

		HBox sums = new HBox(10, text, text1);
		sums.setAlignment(Pos.CENTER);
		sums.setPrefHeight(70);

		HBox boxx = new HBox(20, toDateBox, fromBox);
		boxx.setAlignment(Pos.CENTER);

		VBox all = new VBox(10, boxx, table, sums);
		all.setAlignment(Pos.CENTER);

		fromDatePicker.setOnAction(e -> {
			ResultSet rs = appyQueryOnDataBase(
					"select * from payment where date_of_pay >= '" + fromDatePicker.getValue()
							+ "' and date_of_pay <= '" + toDatePicker.getValue() + "' order by payment_num desc;");

			try {
				payments.clear();
				double sum = 0;
				double sellSumm = 0;

				while (rs.next()) {
					String[] dateSelll = rs.getString(3).split("-");
					String[] timeSelll = rs.getString(4).split(":");

					payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
							new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
									Integer.parseInt(dateSelll[2])),
							new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
									Integer.parseInt(timeSelll[2])),
							rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));
					sellSumm += Double.parseDouble(rs.getString(2));
					sum += Double.parseDouble(rs.getString(6));
				}

				ObservableList<Payment> data = FXCollections.observableArrayList(payments);
				table.setItems(data);
				profitSumField.setText(Math.round(sum * 100) / 100.0 + "");
				sellSumField.setText(Math.round(sellSumm * 100) / 100.0 + "");

			} catch (Exception e1) {

				error.setContentText(e1.getMessage() + "\n" + e1);
				error.show();
			}
		});

		toDatePicker.setOnAction(e -> {
			ResultSet rs = appyQueryOnDataBase(
					"select * from payment where date_of_pay >= '" + fromDatePicker.getValue()
							+ "' and date_of_pay <= '" + toDatePicker.getValue() + "'order by payment_num desc;");

			try {
				payments.clear();
				double sum = 0;
				double sellSumm = 0;

				while (rs.next()) {
					String[] dateSelll = rs.getString(3).split("-");
					String[] timeSelll = rs.getString(4).split(":");

					payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
							new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
									Integer.parseInt(dateSelll[2])),
							new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
									Integer.parseInt(timeSelll[2])),
							rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));
					sum += Double.parseDouble(rs.getString(6));
					sellSumm += Double.parseDouble(rs.getString(2));
				}

				ObservableList<Payment> data = FXCollections.observableArrayList(payments);
				table.setItems(data);
				profitSumField.setText(Math.round(sum * 100) / 100.0 + "");
				sellSumField.setText(Math.round(sellSumm * 100) / 100.0 + "");


			} catch (Exception e1) {

				error.setContentText(e1.getMessage() + "\n" + e1);
				error.show();
			}
		});

		ResultSet rs = appyQueryOnDataBase("select * from payment order by payment_num desc;");

		try {
			double sellSumm = 0;
			double sum = 0;
			while (rs.next()) {
				String[] dateSelll = rs.getString(3).split("-");
				String[] timeSelll = rs.getString(4).split(":");

				payments.add(new Payment(Integer.parseInt(rs.getString(1)), Double.parseDouble(rs.getString(2)),
						new Date(Integer.parseInt(dateSelll[0]) - 1900, Integer.parseInt(dateSelll[1]) - 1,
								Integer.parseInt(dateSelll[2])),
						new Time(Integer.parseInt(timeSelll[0]), Integer.parseInt(timeSelll[1]),
								Integer.parseInt(timeSelll[2])),
						rs.getString(5), (Math.round(Double.parseDouble(rs.getString(6)) * 100)) / 100.0));

				sum += Double.parseDouble(rs.getString(6));
				sellSumm += Double.parseDouble(rs.getString(2));

			}

			profitSumField.setText(Math.round(sum * 100) / 100.0 + "");
			sellSumField.setText(Math.round(sellSumm * 100) / 100.0 + "");

		} catch (Exception e1) {

			error.setContentText(e1.getMessage() + "\n" + e1);
			error.show();
		}

		ObservableList<Payment> data = FXCollections.observableArrayList(payments);
		table.setItems(data);

		if (box.getChildren().size() == 1)
			box.getChildren().add(all);
		else {
			box.getChildren().set(0, booxxx);
			box.getChildren().set(1, all);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void showDetail(Integer paymentNumber) {
		Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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

		back.setOnAction(e -> {
			showAllPayment(null);
		});

		box.getChildren().addAll(table, back);

		ResultSet s = appyQueryOnDataBase("select * from items_payment where payment_num = " + paymentNumber + ";");

		try {
			PaymentItems.clear();
			while (s.next()) {

				ResultSet rs = appyQueryOnDataBase("select item_name from item where item_code = " + s.getString(2));
				rs.next();
				PaymentItems.add(new PaymentItem(Double.parseDouble(s.getString(5)), Double.parseDouble(s.getString(3)),
						Integer.parseInt(s.getString(4)), rs.getString(1), s.getString(2),
						(Math.round(Double.parseDouble(s.getString(6)) * 100)) / 100.0));

			}

			ObservableList<PaymentItem> data = FXCollections.observableArrayList(PaymentItems);
			table.setItems(data);

		} catch (Exception e1) {

			error.setContentText(e1.getMessage() + "\n" + e1);
			error.show();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		box.setAlignment(Pos.CENTER);
		getCounts();

	}

}
