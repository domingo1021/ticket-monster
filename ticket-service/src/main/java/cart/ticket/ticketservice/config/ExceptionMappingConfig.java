package cart.ticket.ticketservice.config;

import cart.ticket.ticketservice.domain.exception.TicketUnavailableException;
import cart.ticket.ticketservice.interfaces.dto.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Configuration
public class ExceptionMappingConfig {

  @Bean
  public Map<Class<? extends Throwable>, HttpStatus> exceptionStatusMapper() {
    return Map.of(
        IllegalArgumentException.class, HttpStatus.BAD_REQUEST,
        TicketUnavailableException.class, HttpStatus.BAD_REQUEST);
  }

  @Bean
  public Map<Class<? extends Throwable>, ApiResponse<Void>> exceptionResponseMapper() {
    return Map.of(
        IllegalArgumentException.class, ApiResponse.error(40000, "Invalid request"),
        TicketUnavailableException.class, ApiResponse.error(40001, "Ticket is unavailable"));
  }
}
