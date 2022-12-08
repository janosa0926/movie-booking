package com.movie.booking.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.springframework.stereotype.Service;

import com.movie.booking.model.Show;
import com.movie.booking.service.ShowService;

/**
 * Show service implementation.
 * 
 * @author JANOSA
 *
 */
@Service
public class ShowServiceImpl implements ShowService {
	
	private File showsFile = new File("shows.txt");
	
	@Override
	public Show findByShowNumber(String showNumber) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(showsFile));
			String currLine = "";
			boolean found = false;

			while ((currLine = br.readLine()) != null) {
				if (showNumber.equals(currLine.split(" ")[0])) {
					found = true;
					break;
				}
			}

			br.close();
			
			if (found) {
				return new Show(currLine.split(" "));
			}
		} catch (Exception e) {
			System.out.println("error encountered: " + e);
		}
		
		return null;
	}

	@Override
	public String save(Show show) {
		try {
			FileWriter fw = new FileWriter(showsFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(show.toString());
			bw.newLine();
			
			bw.close();
			fw.close();
			
			return show.getShowNumber();
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
	
		return null;
	}

	@Override
	public void update(Show show) {
		try {
			File showsTmpFile = new File("shows.tmp");

			BufferedReader br = new BufferedReader(new FileReader(showsFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(showsTmpFile));
			String currLine = "";
			
			while ((currLine = br.readLine()) != null) {
				if (show.getShowNumber().equals(currLine.split(" ")[0])) {
					currLine = show.toString();
				}
				
				bw.write(currLine);
				bw.newLine();
				
			}

			bw.close();
			br.close();
			
			showsFile.delete();
			
			boolean success = showsTmpFile.renameTo(showsFile);
			
			if (success) {
				System.out.println("Show updated");
			}	
		} catch (Exception e) {
			System.out.println("error encountered: " + e);	
		}
		
	
	}

}
