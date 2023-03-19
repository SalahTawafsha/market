package com.example.market;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

public class addEmployee {
	@FXML
	TextField userName;

	@FXML
	TextField empName;

	@FXML
	DatePicker dateOfBirth;

	@FXML
	TextField salary;

	@FXML
	TextField password;

	@FXML
	TextField password2;

	@FXML
	Button addEmployee;

	private Alert error = new Alert(AlertType.ERROR);

	public void userNameTyped(KeyEvent e) {
		if (!userName.getText().equals(""))
			empName.setDisable(false);
		else {
			empName.setDisable(true);
			dateOfBirth.setDisable(true);
			salary.setDisable(true);
			password.setDisable(true);
			password2.setDisable(true);
		}
	}

	public void empNameTyped(KeyEvent e) {
		if (!empName.getText().equals(""))
			salary.setDisable(false);
		else {
			dateOfBirth.setDisable(true);
			salary.setDisable(true);
			password.setDisable(true);
			password2.setDisable(true);
		}
	}

	public void pass2Typed(KeyEvent e) {
		if (!password2.getText().equals(""))
			dateOfBirth.setDisable(false);
		else {
			dateOfBirth.setDisable(true);
		}
	}

	public void salaryTyped(KeyEvent e) {
		if (!salary.getText().equals(""))
			password.setDisable(false);
		else {
			password.setDisable(true);
			password2.setDisable(true);
			dateOfBirth.setDisable(true);
		}
	}

	public void passTyped(KeyEvent e) {
		if (!password.getText().equals(""))
			password2.setDisable(false);
		else {
			password2.setDisable(true);
			dateOfBirth.setDisable(true);
		}
	}

	public void addEmpButton(ActionEvent e) {
		if (password.getText().equals(password2.getText())) {
			LocalDate value1 = dateOfBirth.getValue();
			applyOnDataBase("insert into employee values('" + userName.getText() + "','" + empName.getText() + "','"
					+ value1 + "','" + password.getText() + "')");
		} else {
			error.setContentText("كلمة السر غير متطابقة");
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
}
