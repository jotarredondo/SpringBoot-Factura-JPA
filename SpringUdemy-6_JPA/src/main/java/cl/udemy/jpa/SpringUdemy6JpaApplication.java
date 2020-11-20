package cl.udemy.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cl.udemy.jpa.service.IUploadService;

@SpringBootApplication
public class SpringUdemy6JpaApplication implements CommandLineRunner{
	
	@Autowired
	IUploadService uploadService;

	public static void main(String[] args) {
		SpringApplication.run(SpringUdemy6JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		uploadService.deleteAll();
		uploadService.init();
		
	}

}
