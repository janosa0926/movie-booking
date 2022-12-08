package com.movie.booking.repository.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;

import com.movie.booking.model.Show;
import com.movie.booking.model.ShowTransaction;
import com.movie.booking.service.ShowService;
import com.movie.booking.service.ShowTransactionService;

@SpringBootTest
@ActiveProfiles("test")
public class BuyerTest {

	@Autowired
	private Buyer buyer;
	
	@Autowired
	private ShowService showService;
	
	@Autowired
	private ShowTransactionService showTransactionService;
	

	@Test
	@Order(1)
	public void invalidCommandTest() {
		try {
			buyer.executeCommand("test");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(2)
	public void invalidCommandTest2() {
		try {
			buyer.executeCommand("");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(3)
	public void invalidAvailabilityCommandTest() {
		try {
			buyer.executeCommand("availability");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(4)
	public void showUnvailableTest() {
		when(showService.findByShowNumber(anyString())).thenReturn(null);
		
		try {
			buyer.executeCommand("availability 12345");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Show number not found"), "Show number not found");
		}
	}
	

	@Test
	@Order(5)
	public void showAvailableTest() throws Exception {
		String showStr = "12345 26 10 15 A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		Show show = new Show(showStr.split(" "));
		when(showService.findByShowNumber(anyString())).thenReturn(show);

		buyer.executeCommand("availability 12345");
	}

	@Test
	@Order(6)
	public void invalidBookCommandTest() {
		try {
			buyer.executeCommand("book");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}

	@Test
	@Order(7)
	public void invalidBookCommandTest2() {
		try {
			buyer.executeCommand("book 12345");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}

	@Test
	@Order(8)
	public void invalidBookCommandTest3() {
		try {
			buyer.executeCommand("book 12345 09123456789 A1,A2,A3 test");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(9)
	public void bookingFailedTest() {
		String showStr = "12345 26 10 15 A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		
		Show show = new Show(showStr.split(" "));
		when(showService.findByShowNumber(anyString())).thenReturn(show);
		
		when(showTransactionService.save(any(ShowTransaction.class))).thenReturn(null);
		
		try {
			buyer.executeCommand("book 12345 09123456789 A1,A2,A3");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Booking not successful"), "Booking failed");
		}
	}
	
	@Test
	@Order(9)
	public void bookingFailedTest2() {
		String showStr = "12345 26 10 15 A1,A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		
		Show show = new Show(showStr.split(" "));
		when(showService.findByShowNumber(anyString())).thenReturn(show);
		
		when(showTransactionService.save(any(ShowTransaction.class))).thenReturn(null);
		
		try {
			buyer.executeCommand("book 12345 09123456789 A1,A2,A3");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Some of the selected seats are already taken"), "Booking failed");
		}
	}
	
	@Test
	@Order(10)
	public void bookingSucessTest() throws Exception {
		Long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String ticketNumber = "12345" + timeInMillis;
		
		when(showTransactionService.save(any(ShowTransaction.class))).thenReturn(ticketNumber);
		
		doNothing().when(showService).update(any(Show.class));

		buyer.executeCommand("book 12345 09123456789 A1,A2,A3");
	}
	
	@Test
	@Order(11)
	public void invalidCancelCommandTest() {
		try {
			buyer.executeCommand("cancel");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}
	
	@Test
	@Order(12)
	public void invalidCancelCommandTest2() {
		Long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String ticketNumber = "12345" + timeInMillis;
		
		try {
			buyer.executeCommand("cancel " + ticketNumber);
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Invalid command"), "Invalid command");
		}
	}

	@Test
	@Order(13)
	public void cancelNotAllowedTest() {
		Long currtimeInMillis = Calendar.getInstance().getTimeInMillis();
		Long showTxnTimeInMillis = currtimeInMillis - 960000L; //960000 is equal to 16minutes
		String ticketNumber = "12345" + showTxnTimeInMillis;
		
		String showTxnStr = ticketNumber + " 12345 09123456789 A1,A2,A3 " +  showTxnTimeInMillis;
		ShowTransaction showTxn = new ShowTransaction(showTxnStr.split(" "));

		String showStr = "12345 26 10 15 A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		Show show = new Show(showStr.split(" "));
		
		when(showTransactionService.findByTicketNumber(anyString())).thenReturn(showTxn);
		when(showService.findByShowNumber(anyString())).thenReturn(show);
		
		try {
			buyer.executeCommand("cancel " + ticketNumber + " 09123456789");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Unable to proceed since the window for cancellation is already lapsed"), "Cancel not allowed");
		}
	}
	
	
	@Test
	@Order(14)
	public void cancelNotAllowedTest2() {
		Long currtimeInMillis = Calendar.getInstance().getTimeInMillis();
		Long showTxnTimeInMillis = currtimeInMillis - 60000L; //60000 is equal to 1minute
		String ticketNumber = "12345" + showTxnTimeInMillis;
		
		String showTxnStr = ticketNumber + " 12345 09123456789 A1,A2,A3 " +  showTxnTimeInMillis;
		ShowTransaction showTxn = new ShowTransaction(showTxnStr.split(" "));

		String showStr = "12345 26 10 15 A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		Show show = new Show(showStr.split(" "));
		
		when(showTransactionService.findByTicketNumber(anyString())).thenReturn(showTxn);
		when(showService.findByShowNumber(anyString())).thenReturn(show);
		
		try {
			buyer.executeCommand("cancel " + ticketNumber + " 091111111111");
		} catch (Exception e) {
			isTrue(e.getMessage().equals("Phone number mismatched"), "Cancel not allowed");
		}
	}
	
	@Test
	@Order(15)
	public void cancelSuccess() throws Exception {
		Long currtimeInMillis = Calendar.getInstance().getTimeInMillis();
		Long showTxnTimeInMillis = currtimeInMillis - 60000L; //60000 is equal to 1minute
		String ticketNumber = "12345" + showTxnTimeInMillis;
		
		String showTxnStr = ticketNumber + " 12345 09123456789 A1,A2,A3 " +  showTxnTimeInMillis;
		ShowTransaction showTxn = new ShowTransaction(showTxnStr.split(" "));

		String showStr = "12345 26 10 15 A4,A5,A6,A7,A8,A9,A10,B2,B3,B4,B5,B6,B7,B8,B9,B10,C2,C3,C4,C5,C6,C7,C8,C9,C10,D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,J1,J2,J3,J4,J5,J6,J7,J8,J9,J10,K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,N1,N2,N3,N4,N5,N6,N7,N8,N9,N10,O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,U1,U2,U3,U4,U5,U6,U7,U8,U9,U10,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,W1,W2,W3,W4,W5,W6,W7,W8,W9,W10,X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Z1,Z2,Z3,Z4,Z5,Z6,Z7,Z8,Z9,Z10";
		Show show = new Show(showStr.split(" "));
		
		when(showTransactionService.findByTicketNumber(anyString())).thenReturn(showTxn);
		when(showService.findByShowNumber(anyString())).thenReturn(show);
		doNothing().when(showTransactionService).deleteByTicketNumber(anyString());
		doNothing().when(showService).update(any(Show.class));
		
		buyer.executeCommand("cancel " + ticketNumber + " 09123456789");
	}	
}
