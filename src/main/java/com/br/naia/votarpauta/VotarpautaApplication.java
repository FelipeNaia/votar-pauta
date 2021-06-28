package com.br.naia.votarpauta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VotarpautaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotarpautaApplication.class, args);
	}

}
