package com.fabrizio.recetapp.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fabrizio.recetapp.app.models.service.IUploadFileService;


@SpringBootApplication
public class RecetAppApplication implements CommandLineRunner {
	
	@Autowired
	IUploadFileService uploadFileService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(RecetAppApplication.class, args);
	}

//	para poder borrar e inicializar las fotos cada vez que se levanta la aplicacion
	@Override
	public void run(String... args) throws Exception {
//		uploadFileService.deleteAll();
//		uploadFileService.init();
		
		String password = "12345";
		
		for(int i=0 ; i<2;i++) {
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}
}
