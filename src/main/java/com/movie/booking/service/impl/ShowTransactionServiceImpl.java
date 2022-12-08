package com.movie.booking.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.booking.model.ShowTransaction;
import com.movie.booking.service.ShowTransactionService;

/**
 * Show transaction service implementation.
 * 
 * @author JANOSA
 *
 */
@Service
public class ShowTransactionServiceImpl implements ShowTransactionService {
	
	private File showTxnsFile = new File("showTxns.txt");

	@Override
	public ShowTransaction findByTicketNumber(String ticketNumber) {
		try {
			File showTxnsTmpFile = new File("showTxns.tmp");

			BufferedReader br = new BufferedReader(new FileReader(showTxnsFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(showTxnsTmpFile));
			String currLine = "";
			
			boolean found = false;
			
			while ((currLine = br.readLine()) != null) {
				if (ticketNumber.equals(currLine.split(" ")[0])) {
					found = true;
					break;
				}
			}

			bw.close();
			br.close();
			
			if (found) {
				return new ShowTransaction(currLine.split(" "));
			}
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
	
		return null;
	}

	@Override
	public List<ShowTransaction> findShowTransactionsByShowNumber(String showNumber) {
		try {
			List<ShowTransaction> showTxns = new ArrayList<>();
			File showTxnsTmpFile = new File("showTxns.tmp");

			BufferedReader br = new BufferedReader(new FileReader(showTxnsFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(showTxnsTmpFile));
			String currLine = "";
			
			while ((currLine = br.readLine()) != null) {
				if (showNumber.equals(currLine.split(" ")[1])) {
					showTxns.add(new ShowTransaction(currLine.split(" ")));
				}
			}

			bw.close();
			br.close();
			
			return showTxns;
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
	
		return null;
	}


	@Override
	public String save(ShowTransaction showTxn) {
		try {
			Long timeInMillis = Calendar.getInstance().getTimeInMillis();
			
			String ticketNumber = showTxn.getShowNumber() + timeInMillis;
			
			showTxn.setTicketNumber(ticketNumber);
			showTxn.setTimeInMillis(timeInMillis);
			
			FileWriter fw = new FileWriter(showTxnsFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(showTxn.toString());
			bw.newLine();
			
			bw.close();
			fw.close();
			
			return ticketNumber;
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
		return null;
	}
	
	@Override
	public void deleteByTicketNumber(String ticketNumber) {
		try {
			File showTxnsTmpFile = new File("showTxns.tmp");

			BufferedReader br = new BufferedReader(new FileReader(showTxnsFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(showTxnsTmpFile));
			String currLine = "";
			
			while ((currLine = br.readLine()) != null) {
				if (ticketNumber.equals(currLine.split(" ")[0])) {
					continue;
				}
				
				bw.write(currLine);
				bw.newLine();
				
			}

			bw.close();
			br.close();
			
			showTxnsFile.delete();
			
			boolean success = showTxnsTmpFile.renameTo(showTxnsFile);
			
			if (success) {
				System.out.println("Ticket number cancelled");
			}	
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
	}


}
