package cart.ticket.ticketservice.application.exception;

public class MessageSendFailureException extends RuntimeException {
  public MessageSendFailureException(String message) {
    super(message);
  }
}
