package cl.udemy.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cl.udemy.jpa.service.IUploadService;

@SpringBootApplication
public class SpringUdemy6JpaApplication implements CommandLineRunner{
	
	@Autowired
	IUploadService uploadService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringUdemy6JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		uploadService.deleteAll();
		uploadService.init();
		
		String password ="12345";
		for (int i = 0; i < 2; i++) {
			String bCryptPassword = passwordEncoder.encode(password);
			System.out.println(bCryptPassword);
			}
		
	}

}
