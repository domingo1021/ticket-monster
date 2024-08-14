package cart.ticket.authservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./auth-service")  // Specify the directory containing .env file
                .filename(".env")  // Specify the .env filename
                .load();
        System.out.println("JWT_SECRET: " + dotenv.get("JWT_SECRET"));
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
