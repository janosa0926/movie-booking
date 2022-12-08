package com.movie.booking.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.movie.booking.model.Show;
import com.movie.booking.model.ShowTransaction;
import com.movie.booking.repository.UserCommand;
import com.movie.booking.service.ShowService;
import com.movie.booking.service.ShowTransactionService;

/**
 * Buyer command processing.
 * 
 * @author JANOSA
 *
 */
@Component
public class Buyer implements UserCommand {
	
	@Autowired
	private ShowService showService;
	
	@Autowired
	private ShowTransactionService showTransactionService;

	@Override
	public void executeCommand(String command) throws Exception {
		String[] buyerCommand = command.split(" ");
	
		String commandName = buyerCommand[0];

		if (commandName.equals("availability")) {
			executeAvailability(buyerCommand);
		} else if (commandName.equals("book")) {
			executeBook(buyerCommand);
		} else if (commandName.equals("cancel")) {
			executeCancel(buyerCommand);
		}  else {
			throw new Exception("Invalid command");
		}
		
	}

	/**
	 * Execute availability command.<br/>
	 * Example:<br/>
	 * availability 12345
	 * @param command - availability {@literal <}show number>
	 * @throws Exception 
	 */
	private void executeAvailability(String[] command) throws Exception {
		if (command.length != 2) {
			System.out.println("availablility <show number>");
			throw new Exception("Invalid command");
		} else {
			String showNumber = command[1];
			Show show = showService.findByShowNumber(showNumber);
			
			if (show != null) {
				System.out.println(String.join(",", show.getAvailableSeats()));
			} else {
				throw new Exception("Show number not found");
			}
			
		}
		
	}

	/**
	 * Execute book command.<br/>
	 * Example:<br/>
	 * book 12345 09123456789 A1,A2,A3
	 * @param command - book {@literal <}show number> {@literal <}phone number> {@literal <}comma separated list of seats>
	 * @throws Exception 
	 */
	private void executeBook(String[] command) throws Exception {
		if (command.length != 4) {
			System.out.println("book <show number> <phone number> <comma separated list of seats>");
			throw new Exception("Invalid command");
		} else {
			String showNumber = command[1];
			String phoneNum = command[2];
			List<String> seats = Arrays.asList(command[3].split(","));
			
			System.out.println(command[3]);
			
			ShowTransaction showTxn = new ShowTransaction(showNumber, phoneNum, seats);
			
			Show show = showService.findByShowNumber(showNumber);
			List<String> availSeatsTmp = new ArrayList<>();
			boolean found = false;
			
			for (String availSeat : show.getAvailableSeats()) {
				for (String seat : seats) {
					if (availSeat.equals(seat)) {
						found = true;
					}
				}
				if (found) {
					found = false;
					continue;	
				} 

				availSeatsTmp.add(availSeat);
			}
			
			if ((show.getAvailableSeats().size() - availSeatsTmp.size()) < seats.size()) {
				throw new Exception("Some selected seats are already taken");
			}
			
			show.setAvailableSeats(availSeatsTmp);
			
			String ticketNumber =  showTransactionService.save(showTxn);
			
			if (ticketNumber != null) {
				showService.update(show);
				
				System.out.println("Booking is sucessful: " + ticketNumber);				
			} else {
				throw new Exception("Booking not successful");
			}
			
		}
	}

	/**
	 * Execute cancel command.<br/>
	 * Example:<br/>
	 * cancel 123451234567890123 09123456789
	 * @param command - cancel {@literal <}ticket number> {@literal <}phone number>
	 * @throws Exception 
	 */
	private void executeCancel(String[] command) throws Exception {
		if (command.length != 3) {
			System.out.println("cancel <ticket number> <phone number>");
			throw new Exception("Invalid command");
		} else {
			String ticketNumber = command[1];
			String phoneNum = command[2];
			
			ShowTransaction showTxn = showTransactionService.findByTicketNumber(ticketNumber);
			String showNum = showTxn.getShowNumber();
			
			Show show = showService.findByShowNumber(showNum);
			
			Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
			
			Long allowedTimeInMillis = showTxn.getTimeInMillis() + (show.getCancelWindowMins() * 60000); // 1 minute = 60000 milliseconds
			System.out.println(allowedTimeInMillis + " < " + currentTimeInMillis);
			if (allowedTimeInMillis < currentTimeInMillis) {
				throw new Exception("Unable to proceed since the window for cancellation is already lapsed");
			} else {
				if (showTxn.getBuyerPhoneNum().equals(phoneNum)) {
					List<String> availableSeats = new ArrayList<>();
					availableSeats.addAll(show.getAvailableSeats());
					availableSeats.addAll(showTxn.getSeatNumbers());
					
					show.setAvailableSeats(availableSeats);
					
					showTransactionService.deleteByTicketNumber(ticketNumber);
					showService.update(show);
				} else {
					throw new Exception("Phone number mismatched");
				}
			}
		}
		
	}
}
