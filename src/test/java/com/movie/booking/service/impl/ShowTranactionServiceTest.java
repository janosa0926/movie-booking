package com.movie.booking.service.impl;

import static org.springframework.util.Assert.isTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.movie.booking.model.ShowTransaction;
import com.movie.booking.service.ShowTransactionService;

@SpringBootTest
public class ShowTranactionServiceTest {

	@Autowired
	private ShowTransactionService showTransactionService;
	
	@Test
	public void findByTicketNumberFileNotFound() {
		File showTxnsFile = new File("show-txn-test", "txt");
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		ShowTransaction showTxn = showTransactionService.findByTicketNumber("12345" + Calendar.getInstance().getTimeInMillis());
		
		isTrue(showTxn == null, "Show Transaction not found");
	}
	

	@Test
	public void findByTicketNumberFound() throws IOException {
		File showTxnsFile = File.createTempFile("show-txn-test", "txt");
		showTxnsFile.deleteOnExit();
		
		Long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String ticketNumber = "12345" + timeInMillis;

		FileWriter writer = new FileWriter(showTxnsFile);
		writer.write(ticketNumber + " 12345 09123456789 A1,A2,A3 " + timeInMillis);
		writer.close();
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		ShowTransaction showTxn = showTransactionService.findByTicketNumber(ticketNumber);
		
		isTrue(showTxn != null, "Show Transaction found");
	}
	

	@Test
	public void findByShowNumberFileNotFound() {
		File showTxnsFile = new File("show-txn-test", "txt");
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		List<ShowTransaction> showTxns = showTransactionService.findShowTransactionsByShowNumber("12345");
		
		isTrue(showTxns == null, "Show Transactions not found");
	}
	
	@Test
	public void findByShowNumberFound() throws IOException {
		File showTxnsFile = File.createTempFile("show-txn-test", "txt");
		showTxnsFile.deleteOnExit();
		
		Long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String ticketNumber = "12345" + timeInMillis;

		FileWriter writer = new FileWriter(showTxnsFile);
		writer.write(ticketNumber + " 12345 09123456789 A1,A2,A3 " + timeInMillis);
		writer.close();
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		List<ShowTransaction> showTxns = showTransactionService.findShowTransactionsByShowNumber("12345");
		
		isTrue(showTxns != null, "Show Transactions found");
	}
	

	@Test
	public void saveFileNotFound() {
		File showTxnsFile = new File("show-txn-test", "txt");
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		String ticketNumber = showTransactionService.save(null);
		
		isTrue(ticketNumber == null, "Show Transactions not saved");
	}
	

	@Test
	public void saveSuccess() throws IOException {
		File showTxnsFile = File.createTempFile("show-txn-test", "txt");
		showTxnsFile.deleteOnExit();
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		ShowTransaction showTxn = new ShowTransaction("12345", "09123456789", Arrays.asList("A1,A2,A3".split(",")));

		String ticketNumber = showTransactionService.save(showTxn);
		
		isTrue(ticketNumber != null, "Show Transactions saved");
	}
	
	@Test
	public void deleteFileNotFound() {
		File showTxnsFile = new File("show-txn-test", "txt");
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		showTransactionService.deleteByTicketNumber("12345");
		
	}
	

	@Test
	public void deleteSuccess() throws IOException {
		File showTxnsFile = File.createTempFile("show-txn-test", "txt");
		showTxnsFile.deleteOnExit();
		Long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String ticketNumber1 = "12345" + timeInMillis;
		String ticketNumber2 = "12345" + (timeInMillis + 60000L);

		FileWriter writer = new FileWriter(showTxnsFile, true);
		writer.write(ticketNumber1 + " 12345 09123456789 A1,A2,A3 " + timeInMillis);
		writer.write("\n");
		writer.write(ticketNumber2 + " 12345 09111111111 B1,B2,B3 " + (timeInMillis + 60000L));
		writer.close();
		
		ReflectionTestUtils.setField(showTransactionService, "showTxnsFile", showTxnsFile);
		
		showTransactionService.deleteByTicketNumber(ticketNumber1);
	}
}
