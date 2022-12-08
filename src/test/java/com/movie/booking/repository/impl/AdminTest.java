package com.movie.booking.repository.impl;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.util.Assert.isTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.movie.booking.model.Show;
import com.movie.booking.model.ShowTransaction;
import com.movie.booking.service.ShowService;
import com.movie.booking.service.ShowTransactionService;

@SpringBootTest
@ActiveProfiles("test")
public class AdminTest {

	@Autowired
	private Admin admin;
	
	@Autowired
	private ShowService showService;
	
	@Autowired
	private ShowTransactionService showTransactionService;

	@Test
	@Order(1)
	public void invalidCommandTest() {
		try {
			admin.executeCommand("test");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(2)
	public void invalidCommandTest2() {
		try {
			admin.executeCommand("");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(3)
	public void invalidSetupCommandTest() {
		try {
			admin.executeCommand("setup");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(4)
	public void invalidSetupCommandTest2() {
		try {
			admin.executeCommand("setup 12345");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}

	@Test
	@Order(5)
	public void invalidSetupCommandTest3() {
		try {
			admin.executeCommand("setup 12345 26 10 15 test");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(6)
	public void invalidNumOfRowsTest() {
		ReflectionTestUtils.setField(admin, "MAX_VALID_NUMBER_OF_ROWS", "26");
		ReflectionTestUtils.setField(admin, "MAX_VALID_SEATS_PER_ROW", "10");
		
		try {
			admin.executeCommand("setup 12345 27 10 15");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Number of rows exceeded the maximum: 26"), "Max rows is not valid");
		}
	}
	

	@Test
	@Order(7)
	public void invalidNumOfSeatsPerRowTest() {
		ReflectionTestUtils.setField(admin, "MAX_VALID_NUMBER_OF_ROWS", "26");
		ReflectionTestUtils.setField(admin, "MAX_VALID_SEATS_PER_ROW", "10");
		
		try {
			admin.executeCommand("setup 12345 26 11 15");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Number of seats per row exceeded the maximum: 10"), "Max seats per row is not valid");
		}
	}
	
	@Test
	@Order(8)
	public void showNumberAlreadyExistsTest() {
		
		String[] showArr = {"12345","26","10","15",","};
		
		when(showService.findByShowNumber(anyString())).thenReturn(new Show(showArr));
		
		try {
			admin.executeCommand("setup 12345 26 10 15");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Show number already exists: 12345"), "Show number already exists");
		}
	}
	
	@Test
	@Order(9)
	public void showNotSavedTest() {
		when(showService.findByShowNumber(anyString())).thenReturn(null);
		when(showService.save(any(Show.class))).thenReturn(null);
		ReflectionTestUtils.setField(admin, "MAX_VALID_NUMBER_OF_ROWS", "26");
		ReflectionTestUtils.setField(admin, "MAX_VALID_SEATS_PER_ROW", "10");

		try {
			admin.executeCommand("setup 12345 26 10 15");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Show was not added"), "Show was not added");
		}
	}
	
	@Test
	@Order(10)
	public void showSavedTest() throws Exception {
		when(showService.findByShowNumber(anyString())).thenReturn(null);
		when(showService.save(any(Show.class))).thenReturn("12345");
		ReflectionTestUtils.setField(admin, "MAX_VALID_NUMBER_OF_ROWS", "26");
		ReflectionTestUtils.setField(admin, "MAX_VALID_SEATS_PER_ROW", "10");

		admin.executeCommand("setup 12345 26 10 15");
	}
	
	@Test
	@Order(11)
	public void invalidViewCommandTest() {
		try {
			admin.executeCommand("view");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(12)
	public void invalidViewCommandTest2() {
		try {
			admin.executeCommand("view 12345 test");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(13)
	public void noShowTranactionsTest() {
		when(showTransactionService.findShowTransactionsByShowNumber(anyString())).thenReturn(new ArrayList<>());

		try {
			admin.executeCommand("view 12345");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("There are no show tranasctions found"), "No show tranactions found");
		}
	}
	

	@Test
	@Order(14)
	public void showTranactionsFoundTest() throws Exception {
		String seatArr = "A1,A2,A3";
		
		ShowTransaction showTransaction = new ShowTransaction("12345", "09123456789", Arrays.asList(seatArr.split(","))) ;
		
		List<ShowTransaction> showTransactions = new ArrayList<>();
		showTransactions.add(showTransaction);
		
		when(showTransactionService.findShowTransactionsByShowNumber(anyString())).thenReturn(showTransactions);

		admin.executeCommand("view 12345");		
	}
}
