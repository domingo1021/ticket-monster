package cart.ticket.authservice.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

  public static final String AUTH_SERVICE = "./auth-service";
  public static final String ENV = ".env";

  @Bean
  public Dotenv dotenv() {
    Dotenv dotenv = Dotenv.configure().directory(AUTH_SERVICE).filename(ENV).load();
    dotenv
        .entries()
        .forEach(
            entry -> {
              System.setProperty(entry.getKey(), entry.getValue());
            });

    return dotenv;
  }
}
