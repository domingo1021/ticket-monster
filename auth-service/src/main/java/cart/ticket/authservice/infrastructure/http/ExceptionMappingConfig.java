package cart.ticket.authservice.infrastructure.http;

import cart.ticket.authservice.application.exception.AuthenticationFailedException;
import cart.ticket.authservice.application.exception.OAuthUserNoPwdException;
import cart.ticket.authservice.application.exception.UserAlreadyExistException;
import cart.ticket.authservice.application.exception.UserNotFoundException;
import cart.ticket.ticketservice.domain.exception.TicketUnavailableException;
import cart.ticket.ticketservice.presentation.dto.ApiResponse;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ExceptionMappingConfig {

  @Bean
  public Map<Class<? extends Throwable>, HttpStatus> exceptionStatusMapper() {
    return Map.of(
        AuthenticationFailedException.class,
        HttpStatus.UNAUTHORIZED,
        UserNotFoundException.class,
        HttpStatus.UNAUTHORIZED,
        UserAlreadyExistException.class,
        HttpStatus.UNAUTHORIZED,
        OAuthUserNoPwdException.class,
        HttpStatus.UNAUTHORIZED);
  }

  @Bean
  public Map<Class<? extends Throwable>, ApiResponse<Void>> exceptionResponseMapper() {
    return Map.of(
        AuthenticationFailedException.class, ApiResponse.error(40100, "Authentication failed"),
        UserAlreadyExistException.class, ApiResponse.error(40101, "User already exists"),
        OAuthUserNoPwdException.class, ApiResponse.error(40102, "User uses password approach to login"),
        UserNotFoundException.class, ApiResponse.error(40103, "User not found")
    );

  }
}
