package com.example.market;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;

public class Control implements Initializable {

	@FXML
	ScrollPane parent;

	Alert error = new Alert(AlertType.ERROR);

	public void concHandler(ActionEvent e) {
		try {
			if (sell.soso.isShowing())
				sell.soso.close();
			parent.setContent(FXMLLoader.load(getClass().getResource("home.fxml")));
		} catch (Exception e1) {
			error.setContentText(e1.getMessage());
			error.show();
		}
	}
	
	public void deletePayment(ActionEvent e) {
		try {
			if (sell.soso.isShowing())
				sell.soso.close();
			parent.setContent(FXMLLoader.load(getClass().getResource("deletePayment.fxml")));
		} catch (Exception e1) {
			error.setContentText(e1.getMessage());
			error.show();
		}
	}

	public void sellHandler(ActionEvent e) {
		try {
			if (sell.soso.isShowing())
				sell.soso.close();
			parent.setContent(FXMLLoader.load(getClass().getResource("sell.fxml")));
		} catch (Exception e1) {
			error.setContentText(e1.getMessage());
			error.show();
		}

	}

	public void addHandler(ActionEvent e) {
		try {
			if (sell.soso.isShowing())
				sell.soso.close();
			parent.setContent(FXMLLoader.load(getClass().getResource("addItems.fxml")));
		} catch (Exception e1) {
			error.setContentText(e1.getMessage());
			error.show();
		}
	}

	public void edit(ActionEvent e) {
		try {
			if (sell.soso.isShowing())
				sell.soso.close();
			parent.setContent(FXMLLoader.load(getClass().getResource("editItem.fxml")));
		} catch (Exception e1) {
			error.setContentText(e1.getMessage());
			error.show();
		}
	}

	public void exitEmpHandler(ActionEvent e) {
		System.exit(0);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		try {
			parent.setContent(FXMLLoader.load(getClass().getResource("home.fxml")));
		} catch (Exception e) {
			error.setContentText(e.getMessage());
			error.show();
		}

	}

}
