package cart.ticket.ticketservice.interfaces.handler;

import cart.ticket.ticketservice.interfaces.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExceptionResponseFactory {

  private final Map<Class<? extends Throwable>, HttpStatus> exceptionStatusMapper;
  private final Map<Class<? extends Throwable>, ApiResponse<Void>> exceptionResponseMapper;

  // Constructor injection for better flexibility and testing
  public ExceptionResponseFactory(Map<Class<? extends Throwable>, HttpStatus> statusMapper,
                                  Map<Class<? extends Throwable>, ApiResponse<Void>> responseMapper) {
    this.exceptionStatusMapper = statusMapper;
    this.exceptionResponseMapper = responseMapper;
  }

  public ApiResponse<Void> handleException(Throwable ex) {
    return exceptionResponseMapper.getOrDefault(
            ex.getClass(), ApiResponse.error(50000, "Internal server error"));
  }

  public HttpStatus getHttpStatus(Throwable ex) {
    return exceptionStatusMapper.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}