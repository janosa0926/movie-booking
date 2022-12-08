package com.movie.booking.service;

import com.movie.booking.model.Show;

/**
 * Show service interface.
 * 
 * @author JANOSA
 *
 */
public interface ShowService {

	public Show findByShowNumber(String showNumber);
	
	public String save(Show show);
	
	public void update(Show show);
}
