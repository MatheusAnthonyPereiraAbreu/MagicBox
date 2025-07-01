package com.purplebox.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Classe principal que inicializa a aplicação Spring Boot do MagicBox.
public class BackendApplication {

	/**
	 * Ponto de entrada da aplicação.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
