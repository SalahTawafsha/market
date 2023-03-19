package com.example.market;

import java.util.Calendar;
import java.util.Date;

public class Item {
	String item_code;
	String item_name;
	Date Production_date;
	Date expiry_date;
	double price;
	double final_price;
	int num_of_copy;

	public Item(String item_code, String item_name, Date production_date, Date expiry_date, double price,
			double final_price, int num_of_copy) {
		super();
		this.item_code = item_code;
		this.item_name = item_name;
		this.Production_date = production_date;
		this.expiry_date = expiry_date;
		this.price = price;
		this.final_price = final_price;
		this.num_of_copy = num_of_copy;
	}

	public String getItem_code() {
		return item_code;
	}

	public String getItem_name() {
		return item_name;
	}

	@SuppressWarnings("deprecation")
	public String getProduction_date() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Production_date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (Production_date.getMonth() + 1) + "/"
				+ (Production_date.getYear() + 1900);
	}

	@SuppressWarnings("deprecation")
	public String getExpiry_date() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expiry_date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (expiry_date.getMonth() + 1) + "/"
				+ (expiry_date.getYear() + 1900);
	}

	public double getPrice() {
		return Math.round(price * 100) / 100.0;
	}

	public double getFinal_price() {
		return Math.round(final_price * 100) / 100.0;
	}

	public int getNum_of_copy() {
		return num_of_copy;
	}

}
