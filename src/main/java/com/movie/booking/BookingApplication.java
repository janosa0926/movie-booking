package com.movie.booking;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.movie.booking.repository.impl.Admin;
import com.movie.booking.repository.impl.Buyer;

/**
 * Show booking application.
 * 
 * @author JANOSA
 *
 */
@SpringBootApplication
public class BookingApplication implements CommandLineRunner {

	@Autowired	
	private Admin admin;

	@Autowired
	private Buyer buyer;
	
	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

	@Override
	public void run(String... args1) throws Exception {
		String menuCommand = "";

		Scanner scanner = new Scanner(System.in);
		while (!menuCommand.equals("X")) {
			System.out.println("******************************************************************************************");
			System.out.println("[A] - Admin    [B] - Buyer    [X] - Exit");
			System.out.println("******************************************************************************************");
			menuCommand = scanner.nextLine();

			if (menuCommand.equals("A")) {
				System.out.println("******************************************************************************************");
				System.out.println("Enter command");
				System.out.println("******************************************************************************************");
				System.out.println("    setup <show number> <number of rows> <number of seats> <cancellation window (minutes)>");
				System.out.println("    view <show number>");
				System.out.println("    back");
				System.out.println("******************************************************************************************");

				String adminCommand = scanner.nextLine();
				
				while (!adminCommand.equals("back")) {
					try {
						admin.executeCommand(adminCommand);	
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
					adminCommand = scanner.nextLine();
				}
				
			} else if (menuCommand.equals("B")) {
				System.out.println("******************************************************************************************");
				System.out.println("Enter command");
				System.out.println("******************************************************************************************");
				System.out.println("    availablility <show number>");
				System.out.println("    book <show number> <phone number> <comma separated list of seats>");
				System.out.println("    cancel <ticket number> <phone number>");
				System.out.println("    back");
				System.out.println("******************************************************************************************");

				String buyerCommand = scanner.nextLine();
				
				while (!buyerCommand.equals("back")) {
					try {
						buyer.executeCommand(buyerCommand);
					} catch (Exception e) {
						System.out.println(e.getMessage());						
					}
					
					buyerCommand = scanner.nextLine();
				}
			}						
		}
		
		scanner.close();
	}
	

}
