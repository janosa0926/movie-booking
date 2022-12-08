package com.movie.booking.model;

import java.util.Arrays;
import java.util.List;

public class ShowTransaction {
	private String ticketNumber;
	private String showNumber;
	private String buyerPhoneNum;
	private List<String> seatNumbers;
	private Long timeInMillis;
	
	public ShowTransaction(String showNumber, String buyerPhoneNum, List<String> seatNumbers) {
		super();
		this.showNumber = showNumber;
		this.buyerPhoneNum = buyerPhoneNum;
		this.seatNumbers = seatNumbers;
	}
	
	public ShowTransaction(String[] showTxn) {
		super();
		this.ticketNumber = showTxn[0];
		this.showNumber = showTxn[1];
		this.buyerPhoneNum = showTxn[2];
		this.seatNumbers = Arrays.asList(showTxn[3].split(","));
		this.timeInMillis = Long.parseLong(showTxn[4]);
	}

	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getShowNumber() {
		return showNumber;
	}
	public void setShowNumber(String showNumber) {
		this.showNumber = showNumber;
	}
	public String getBuyerPhoneNum() {
		return buyerPhoneNum;
	}
	public void setBuyerPhoneNum(String buyerPhoneNum) {
		this.buyerPhoneNum = buyerPhoneNum;
	}
	public List<String> getSeatNumbers() {
		return seatNumbers;
	}
	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
	}
	
	public Long getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(Long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}
	
	@Override
	public String toString() {
		return ticketNumber + " " + showNumber + " " + buyerPhoneNum + " " + String.join(",", seatNumbers) + " " + timeInMillis;
	}
	
}
