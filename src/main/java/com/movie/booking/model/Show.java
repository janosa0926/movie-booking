package com.movie.booking.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Show {
	private String showNumber;
	private Integer numOfRows;
	private Integer seatsPerRow;
	private Integer cancelWindowMins;
	private List<String> availableSeats;
	
	public Show(String showNumber, Integer numOfRows, Integer seatsPerRow, Integer cancelWindowMins) {
		super();
		this.showNumber = showNumber;
		this.numOfRows = numOfRows;
		this.seatsPerRow = seatsPerRow;
		this.cancelWindowMins = cancelWindowMins;
		
		generateSeats();
	}

	public Show(String[] show) {
		super();
		this.showNumber = show[0];
		this.numOfRows = Integer.parseInt(show[1]);
		this.seatsPerRow = Integer.parseInt(show[2]);
		this.cancelWindowMins = Integer.parseInt(show[3]);
		this.availableSeats = Arrays.asList(show[4].split(","));
	}

	private void generateSeats() {
		availableSeats = new ArrayList<>();
		
		int seatChar = 65;
		
		for (int row = 0; row < numOfRows; row++) {
			for (int seatNum = 0; seatNum < seatsPerRow; seatNum++) {
				availableSeats.add(Character.toString((char) seatChar) + (seatNum + 1));	
			}

			seatChar++;
		}
		
	}

	public String getShowNumber() {
		return showNumber;
	}
	public void setShowNumber(String showNumber) {
		this.showNumber = showNumber;
	}
	public Integer getNumOfRows() {
		return numOfRows;
	}
	public void setNumOfRows(Integer numOfRows) {
		this.numOfRows = numOfRows;
	}
	public Integer getSeatsPerRow() {
		return seatsPerRow;
	}
	public void setSeatsPerRow(Integer seatsPerRow) {
		this.seatsPerRow = seatsPerRow;
	}
	public Integer getCancelWindowMins() {
		return cancelWindowMins;
	}
	public void setCancelWindowMins(Integer cancelWindowMins) {
		this.cancelWindowMins = cancelWindowMins;
	}
	
	public List<String> getAvailableSeats() {
		return this.availableSeats;
	}
	
	public void setAvailableSeats(List<String> availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	@Override
	public String toString() {
		return showNumber + " " + numOfRows + " " + seatsPerRow + " " + cancelWindowMins + " " + String.join(",", availableSeats);
	}
	
}
