package com.example.market;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class addItems implements Initializable {
	private Alert error = new Alert(AlertType.ERROR);

	@FXML
	TextField itemCode;

	@FXML
	TextField itemName;

	@FXML
	TextField price;

	@FXML
	TextField payPrice;

	@FXML
	TextField numberOfCopy;

	@FXML
	TextField itemsCode;

	@FXML
	TextField itemsCount;

	@FXML
	TextField payPrice1;

	@FXML
	DatePicker productionDate;

	@FXML
	DatePicker expiryDate;

	@FXML
	private RadioButton group;

	@FXML
	private RadioButton one;

	ToggleGroup g;

	@FXML
	Button addItem;

	public void itemCodeTyped(KeyEvent e) {
		if (!itemCode.getText().equals("")) {
			one.setDisable(false);
			group.setDisable(false);
			itemName.setDisable(false);
		} else {
			one.setDisable(true);
			group.setDisable(true);
			itemName.setDisable(true);
			price.setDisable(true);
			payPrice.setDisable(true);
			numberOfCopy.setDisable(true);
			productionDate.setDisable(true);
			expiryDate.setDisable(true);
		}
	}

	public void itemNameTyped(KeyEvent e) {
		if (!itemName.getText().equals(""))
			price.setDisable(false);
		else {
			price.setDisable(true);
			payPrice.setDisable(true);
			numberOfCopy.setDisable(true);
			productionDate.setDisable(true);
			expiryDate.setDisable(true);

		}
	}

	@FXML
	void groupAction(ActionEvent event) {

		itemsCount.setDisable(false);
		itemsCode.setDisable(false);
		payPrice1.setDisable(false);
	}

	@FXML
	void oneAction(ActionEvent event) {

		itemsCount.setDisable(true);
		itemsCode.setDisable(true);
		payPrice1.setDisable(true);

	}

	public void priceTyped(KeyEvent e) {
		if (!price.getText().equals(""))
			payPrice.setDisable(false);
		else {
			payPrice.setDisable(true);
			numberOfCopy.setDisable(true);
			productionDate.setDisable(true);
			expiryDate.setDisable(true);
		}
	}

	public void payPriceTyped(KeyEvent e) {
		if (!payPrice.getText().equals(""))
			numberOfCopy.setDisable(false);
		else {
			numberOfCopy.setDisable(true);
			productionDate.setDisable(true);
			expiryDate.setDisable(true);
		}
	}

	public void numberOfCopyTyped(KeyEvent e) {
		if (!numberOfCopy.getText().equals("")) {
			productionDate.setDisable(false);
			expiryDate.setDisable(false);

		} else {
			productionDate.setDisable(true);
			expiryDate.setDisable(true);
		}
	}

	public void addItemButton(ActionEvent e) {
		if (!expiryDate.isDisabled())
			try {
				ResultSet soso = appyQueryOnDataBase(
						"select item_name from item where item_code = '" + itemCode.getText() + "';");

				if (!soso.next()) {
					LocalDate productionDatee = productionDate.getValue();
					LocalDate expiryDatee = expiryDate.getValue();

					if (((RadioButton) (g.getSelectedToggle())).getText().equals("منتج واحد"))
						applyOnDataBase(
								"insert into item(item_code,item_name,Production_date,expiry_date,price,final_price,num_of_copy,profit) values('"
										+ itemCode.getText() + "','" + itemName.getText() + "','" + productionDatee
										+ "','" + expiryDatee + "'," + price.getText() + "," + payPrice.getText() + ","
										+ numberOfCopy.getText() + ","
										+ (Double.parseDouble(payPrice.getText()) - Double.parseDouble(price.getText()))
										+ ");");
					else {
						/*
						 * sub_item_code VARCHAR(15), super_item_code VARCHAR(15), items_count
						 * VARCHAR(15)
						 */
						ResultSet sos = appyQueryOnDataBase(
								"select item_name from item where item_code = '" + itemsCode.getText() + "';");
						if (!sos.next()) {
							applyOnDataBase(
									"insert into item(item_code,item_name,Production_date,expiry_date,price,final_price,num_of_copy,profit,super_item_code,items_count) values('"
											+ itemsCode.getText() + "','" + itemName.getText() + "','" + productionDatee
											+ "','" + expiryDatee + "',"
											+ Double.parseDouble(price.getText())
													/ Double.parseDouble(itemsCount.getText())
											+ "," + payPrice.getText() + ","
											+ (Integer.parseInt(itemsCount.getText())
													* Integer.parseInt(numberOfCopy.getText()))
											+ ","
											+ (Double.parseDouble(payPrice.getText())
													- (Double.parseDouble(price.getText())
															/ Double.parseDouble(itemsCount.getText())))
											+ ", '" + itemCode.getText() + "', " + itemsCount.getText() + ");");

							applyOnDataBase(
									"insert into item(item_code,item_name,Production_date,expiry_date,price,final_price,num_of_copy,profit,sub_item_code,items_count) values('"
											+ itemCode.getText() + "','" + "صندوق " + itemName.getText() + "','"
											+ productionDatee + "','" + expiryDatee + "'," + price.getText() + ","
											+ payPrice1.getText() + "," + numberOfCopy.getText() + ","
											+ (Double.parseDouble(payPrice1.getText())
													- Double.parseDouble(price.getText()))
											+ ", '" + itemsCode.getText() + "', " + itemsCount.getText() + ");");

						} else {
							error.setContentText("الكود الخاص بالمنتجات داخل الصندوق مسجل في قاعدة البيانات");
							error.show();
							return;
						}
					}

					
				} else {

					error.setContentText("كود المنتج مسجل في قاعدة البيانات");
					error.show();
					return;
				}
			} catch (NumberFormatException e1) {
				error.setContentText("يرجى التاكد من الخانات المخصصة للارقام");
				error.show();
				return;
			} catch (NullPointerException e1) {
				error.setContentText("يجب ان تقوم بملئ جميع الخانات !");
				error.show();
				return;
			} catch (Exception e1) {
				error.setContentText(e1.getMessage());
				error.show();
				return;
			}
		else {
			error.setContentText("يجب ان تقوم بملئ جميع الخانات !");
			error.show();
			return;
		}
		productionDate.getEditor().clear();
		expiryDate.getEditor().clear();
		itemCode.clear();
		itemName.clear();
		price.clear();
		payPrice.clear();
		numberOfCopy.clear();
		itemsCount.clear();
		itemsCode.clear();
		payPrice1.clear();
		itemName.setDisable(true);
		price.setDisable(true);
		payPrice.setDisable(true);
		numberOfCopy.setDisable(true);
		productionDate.setDisable(true);
		expiryDate.setDisable(true);
		itemsCount.setDisable(true);
		itemsCode.setDisable(true);
		payPrice1.setDisable(true);
		one.setSelected(true);
		one.setDisable(true);
		group.setDisable(true);
	}

	@FXML
	void dateAction(ActionEvent event) {
		expiryDate.setValue(productionDate.getValue());
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
		g = new ToggleGroup();
		group.setToggleGroup(g);
		one.setToggleGroup(g);
		productionDate.getEditor().setAlignment(Pos.CENTER);
		expiryDate.getEditor().setAlignment(Pos.CENTER);

	}
}
