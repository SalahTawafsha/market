package com.example.market;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class editItem implements Initializable {
	private Alert error = new Alert(AlertType.ERROR);

	@FXML
	ComboBox<String> pay_code;

	@FXML
	TextField itemName;

	@FXML
	TextField price;

	@FXML
	TextField payPrice;

	@FXML
	TextField numberOfCopy;

	@FXML
	DatePicker productionDate;

	@FXML
	DatePicker expiryDate;

	@FXML
	Button Edittem;

	int prevNumberOfCopy;

	String str = "";

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

	@SuppressWarnings("deprecation")
	public void enableAdd(ActionEvent e) {
		if (pay_code.getValue() != null && !pay_code.getValue().equals(""))
			try {
				String[] arr = pay_code.getValue().split("[$]");

				ResultSet s = appyQueryOnDataBase("select * from item where item_code = '" + arr[0] + "';");

				if (s != null && s.next()) {
					itemName.setText(s.getString(2));
					productionDate.getEditor().setText(s.getString(3));
					expiryDate.getEditor().setText(s.getString(4));
					price.setText(s.getString(5));
					payPrice.setText(s.getString(6));
					numberOfCopy.setText(s.getString(7));

					prevNumberOfCopy = Integer.parseInt(numberOfCopy.getText());

					itemName.setDisable(false);
					productionDate.setDisable(false);
					expiryDate.setDisable(false);
					price.setDisable(false);
					payPrice.setDisable(false);
					numberOfCopy.setDisable(false);

					String[] productionDatee = productionDate.getEditor().getText().split("[-]");
					String[] expiryDatee = expiryDate.getEditor().getText().split("[-]");

					String pattern = "MM/dd/yyyy";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

					String productionDateee = simpleDateFormat
							.format(new Date(Integer.parseInt(productionDatee[0]) - 1900,
									Integer.parseInt(productionDatee[1]) - 1, Integer.parseInt(productionDatee[2])));

					String expiryDateee = simpleDateFormat.format(new Date(Integer.parseInt(expiryDatee[0]) - 1900,
							Integer.parseInt(expiryDatee[1]) - 1, Integer.parseInt(expiryDatee[2])));

					productionDate.getEditor().setText(productionDateee);
					expiryDate.getEditor().setText(expiryDateee);
				}
			} catch (Exception e1) {
			}

		Edittem.setDisable(false);
	}

	public void EditItemButton(ActionEvent e) {
		String[] arr = pay_code.getValue().split("[$]");
		ResultSet s = appyQueryOnDataBase(
				"select super_item_code,sub_item_code,items_count,item_name from item where item_code = '" + arr[0]
						+ "';");
		try {

			if (s.next() && s.getString(4) != null && !s.getString(4).equalsIgnoreCase("null")) {
				String[] productionDatee = productionDate.getEditor().getText().split("[/]");
				String[] expiryDatee = expiryDate.getEditor().getText().split("[/]");

				productionDate.setValue(LocalDate.of(Integer.parseInt(productionDatee[2]),
						Integer.parseInt(productionDatee[0]), Integer.parseInt(productionDatee[1])));

				expiryDate.setValue(LocalDate.of(Integer.parseInt(expiryDatee[2]), Integer.parseInt(expiryDatee[0]),
						Integer.parseInt(expiryDatee[1])));

				applyOnDataBase("update item set item_name = '" + itemName.getText() + "',Production_date = '"
						+ productionDate.getValue().toString() + "',expiry_date = '" + expiryDate.getValue().toString()
						+ "',price = " + price.getText() + ",final_price=" + payPrice.getText() + ",num_of_copy="
						+ numberOfCopy.getText() + ", profit = "
						+ (Double.parseDouble(payPrice.getText()) - Double.parseDouble(price.getText()))
						+ " where item_code = '" + arr[0] + "';");

				if (s.getString(1) != null && !s.getString(1).equals("null")) {

					// have super
					int newNum = (Integer.parseInt(numberOfCopy.getText())) / Integer.parseInt(s.getString(3));

					applyOnDataBase(
							"update item set num_of_copy = " + newNum + " where item_code = '" + s.getString(1) + "';");

				}
				if (s.getString(2) != null && !s.getString(2).equals("null")) {
					// have sub
					int newNum = (prevNumberOfCopy - Integer.parseInt(numberOfCopy.getText()))
							* Integer.parseInt(s.getString(3));

					applyOnDataBase("update item set num_of_copy = num_of_copy - " + newNum + ", price = "
							+ Double.parseDouble(price.getText()) / Double.parseDouble(s.getString(3))
							+ ", profit = final_price - "
							+ Double.parseDouble(price.getText()) / Double.parseDouble(s.getString(3))
							+ " where item_code = '" + s.getString(2) + "';");

				}
			} else {
				error.setContentText("هذا الكود ليس موجود !");
				error.show();
			}
		} catch (SQLException e1) {
			error.setContentText(e1.getMessage());
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

		pay_code.getEditor().setAlignment(Pos.CENTER);
		expiryDate.getEditor().setAlignment(Pos.CENTER);
		productionDate.getEditor().setAlignment(Pos.CENTER);

	}
}
