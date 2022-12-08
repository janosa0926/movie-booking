package com.movie.booking.service;

import java.util.List;

import com.movie.booking.model.ShowTransaction;

/**
 * Show transaction service.
 * 
 * @author JANOSA
 *
 */
public interface ShowTransactionService {
	public ShowTransaction findByTicketNumber(String ticketNumber);
	public List<ShowTransaction> findShowTransactionsByShowNumber(String showNumber);
	
	public String save(ShowTransaction showTxn);
	public void deleteByTicketNumber(String ticketNumber);
	
}
