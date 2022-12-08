package com.movie.booking.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.movie.booking.model.Show;
import com.movie.booking.model.ShowTransaction;
import com.movie.booking.repository.UserCommand;
import com.movie.booking.service.ShowService;
import com.movie.booking.service.ShowTransactionService;

/**
 * Admin command processing.
 * 
 * @author JANOSA
 *
 */
@Component
public class Admin implements UserCommand {
	
	@Value("${max.valid.number.of.rows}")
	private String MAX_VALID_NUMBER_OF_ROWS;
	
	@Value("${max.valid.seats.per.row}")
	private String MAX_VALID_SEATS_PER_ROW;
	
	@Autowired
	private ShowService showService;
	
	@Autowired
	private ShowTransactionService showTransactionService;

	@Override
	public void executeCommand(String command) throws Exception {
		String[] adminCommand = command.split(" ");
		
		String commandName = adminCommand[0];
		
		if (commandName.equals("setup")) {
			executeSetup(adminCommand);
		} else if (commandName.equals("view")) {
			executeView(adminCommand);
		}  else {
			throw new Exception("Invalid command");
		}
	}

	/**
	 * Execute setup command.<br/>
	 * Example:<br/>
	 * setup 12345 26 10 15
	 * @param command - setup {@literal <}show number> {@literal <}number of rows> {@literal <}number of seats> {@literal <}cancellation window (minutes)>
	 * @throws Exception 
	 */
	private void executeSetup(String[] command) throws Exception {
		if (command.length != 5) {
			System.out.println("setup <show number> <number of rows> <number of seats> <cancellation window (minutes)>");

			throw new Exception("Invalid command");
		} else {
			String showNumber = command[1];
			Integer numOfRows = Integer.parseInt(command[2]);
			Integer numOfSeats = Integer.parseInt(command[3]);
			Integer cancelWindowMins = Integer.parseInt(command[4]);

			
			if (numOfRows > Integer.parseInt(MAX_VALID_NUMBER_OF_ROWS)) {
				throw new Exception("Number of rows exceeded the maximum: " + MAX_VALID_NUMBER_OF_ROWS);
			}
			
			if (numOfSeats > Integer.parseInt(MAX_VALID_SEATS_PER_ROW)) {
				throw new Exception("Number of seats per row exceeded the maximum: " + MAX_VALID_SEATS_PER_ROW);
			}
			
			Show show = showService.findByShowNumber(showNumber);
			System.out.println("show: " + show);
			if (show != null) {
				throw new Exception("Show number already exists: " + showNumber);
			} else {
				show = new Show(showNumber, numOfRows, numOfSeats, cancelWindowMins);

				showNumber = showService.save(show);
				
				if (showNumber != null) {
					System.out.println("Show added!");
				} else {
					throw new Exception("Show was not added");
				}
				
			}
			
		} 
	}

	/**
	 * Execute setup command.<br/>
	 * Example:<br/>
	 * view 12345
	 * @param command - setup {@literal <}show number>
	 * @throws Exception 
	 */
	private void executeView(String[] command) throws Exception {
		if (command.length != 2) {
			System.out.println("view <show number>");
			throw new Exception("Invalid command");
		} else {
			String showNumber = command[1];
			List<ShowTransaction> showTxns = showTransactionService.findShowTransactionsByShowNumber(showNumber);
			
			if (showTxns != null && !showTxns.isEmpty()) {
				for (ShowTransaction showTxn : showTxns) {
					System.out.println(showTxn);
				}
			} else {
				throw new Exception("There are no show tranasctions found");
			}
			
		}
	
	}
}
