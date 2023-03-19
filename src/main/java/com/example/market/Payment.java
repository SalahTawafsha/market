package com.example.market;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Payment {

	private Integer paymentNumber;

	private Double priceForAll;

	private Date dateSell;

	private Time timeSell;

	private String empName;

	private Double profit;

	public Payment(Integer paymentNumber, Double priceForAll, Date dateSell, Time timeSell, String empName,
			Double profit) {
		super();
		this.paymentNumber = paymentNumber;
		this.priceForAll = priceForAll;
		this.dateSell = dateSell;
		this.timeSell = timeSell;
		this.empName = empName;
		this.profit = profit;
	}

	public Double getProfit() {
		return Math.round(profit * 100) / 100.0;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Integer getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(Integer paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public void setPriceForAll(Double priceForAll) {
		this.priceForAll = priceForAll;
	}

	public void setDateSell(Date dateSell) {
		this.dateSell = dateSell;
	}

	public void setTimeSell(Time timeSell) {
		this.timeSell = timeSell;
	}

	@SuppressWarnings("deprecation")
	public String getDateSell() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateSell);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (dateSell.getMonth() + 1) + "/"
				+ (dateSell.getYear() + 1900);
	}

	public Time getTimeSell() {
		return timeSell;
	}

	public Double getPriceForAll() {
		return Math.round(priceForAll * 100) / 100.0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Payment)
			return ((Payment) obj).getPaymentNumber().equals(this.paymentNumber);
		else
			return super.equals(obj);
	}

}
