package cart.ticket.ticketservice.interfaces.handler;

import cart.ticket.ticketservice.interfaces.dto.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Global exception designed to handle exceptions thrown by both MVC controllers and WebFlux handlers.
 * And it will return a General JSON response with the appropriate status code and error message.
 */
@Component
@Order(-2)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements WebExceptionHandler {

    private final ExceptionResponseFactory exceptionResponseFactory;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ExceptionResponseFactory exceptionResponseFactory) {
        this.exceptionResponseFactory = exceptionResponseFactory;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleMvcException(Exception ex) {
        HttpStatus status = exceptionResponseFactory.getHttpStatus(ex);
        ApiResponse<Void> response = exceptionResponseFactory.handleException(ex);

        logException(status, ex);

        return ResponseEntity.status(status).body(response);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = exceptionResponseFactory.getHttpStatus(ex);
        ApiResponse<Void> apiResponse = exceptionResponseFactory.handleException(ex);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        logException(status, ex);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(apiResponse.toJson().getBytes())));
    }

    private void logException(HttpStatus status, Throwable ex) {
        if (status.is4xxClientError()) {
            logger.warn("Client error: {}", ex.getMessage());
        } else {
            logger.error("Server error: {}", ex.getMessage(), ex);
        }
    }
}
