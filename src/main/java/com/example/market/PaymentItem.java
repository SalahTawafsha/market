package com.example.market;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class PaymentItem {

	private String ItemCodeee;

	private String ItemeNameee;

	private Date dateSell;

	private Time timeSell;

	private Integer NumberCop;

	private Double onePrice;

	private Double priceForAll;

	private Double profit;

	public PaymentItem(Double priceForAll, Double onePrice, Integer numberCop, Time timeSell, Date dateSell,
			String itemeNameee, String itemCodeee) {
		super();
		this.ItemCodeee = itemCodeee;
		this.ItemeNameee = itemeNameee;
		this.dateSell = dateSell;
		this.timeSell = timeSell;
		this.NumberCop = numberCop;
		this.onePrice = onePrice;
		this.priceForAll = priceForAll;
	}

	public PaymentItem(Double priceForAll, Double onePrice, Integer numberCop, String itemeNameee, String itemCodeee,
			Double profit) {
		super();
		ItemCodeee = itemCodeee;
		ItemeNameee = itemeNameee;
		NumberCop = numberCop;
		this.onePrice = onePrice;
		this.priceForAll = priceForAll;
		this.profit = profit;
	}

	public Double getProfit() {
		return Math.round(profit * 100) / 100.0;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public void setItemCodeee(String itemCodeee) {
		ItemCodeee = itemCodeee;
	}

	public void setItemeNameee(String itemeNameee) {
		ItemeNameee = itemeNameee;
	}

	public void setDateSell(Date dateSell) {
		this.dateSell = dateSell;
	}

	public void setTimeSell(Time timeSell) {
		this.timeSell = timeSell;
	}

	public void setNumberCop(Integer numberCop) {
		NumberCop = numberCop;
	}

	public void setOnePrice(Double onePrice) {
		this.onePrice = onePrice;
	}

	public void setPriceForAll(Double priceForAll) {
		this.priceForAll = priceForAll;
	}

	public String getItemCodeee() {
		return ItemCodeee;
	}

	public String getItemeNameee() {
		return ItemeNameee;
	}

	@SuppressWarnings("deprecation")
	public String getDateSelll() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateSell);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (dateSell.getMonth() + 1) + "/"
				+ (dateSell.getYear() + 1900);
	}

	public Date getDateSell() {
		return dateSell;
	}

	public Time getTimeSell() {
		return timeSell;
	}

	public Integer getNumberCop() {
		return NumberCop;
	}

	public Double getOnePrice() {
		return Math.round(onePrice * 100) / 100.0;
	}

	public Double getPriceForAll() {
		return Math.round(priceForAll * 100) / 100.0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PaymentItem) {
			PaymentItem i = (PaymentItem) obj;
			return i.getItemCodeee().equals(ItemCodeee) && i.getItemeNameee().equals(ItemeNameee)
					&& i.getNumberCop().equals(NumberCop) && i.getOnePrice().equals(onePrice)
					&& i.getPriceForAll().equals(priceForAll);
		}

		return obj == this;
	}

	@Override
	public String toString() {
		return "PaymentItem [ItemCodeee=" + ItemCodeee + ", ItemeNameee=" + ItemeNameee + ", NumberCop=" + NumberCop
				+ ", onePrice=" + onePrice + ", priceForAll=" + priceForAll + ", profit=" + profit + "]";
	}

}
