package fr.fms.booking_com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.fms.booking_com.console.App;

@SpringBootApplication
public class BookingComApplication implements CommandLineRunner {

	@Autowired
	private App appMgr;

	public static void main(String[] args) {
		SpringApplication.run(BookingComApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		appMgr.start();
	}

}
