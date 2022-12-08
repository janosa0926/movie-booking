package com.movie.booking.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


@Profile("test")
@Configuration
public class ShowTransactionServiceConfiguration {

	@Bean
	@Primary
	public ShowTransactionService showTransactionService() {
		return Mockito.mock(ShowTransactionService.class);
	}


}
