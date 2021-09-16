package fatec.grupodois.endurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class EnduranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnduranceApplication.class, args);
	}

<<<<<<< HEAD

=======
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f

}
 