package com.movie.booking.repository;


/**
 * User command interface.
 * 
 * @author JANOSA
 *
 */
public interface UserCommand {
	/**
	 * Execute command.
	 * 
	 * @param command
	 */
	void executeCommand(String command) throws Exception;
}
