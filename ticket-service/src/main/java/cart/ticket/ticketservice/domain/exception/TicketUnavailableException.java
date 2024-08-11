package cart.ticket.ticketservice.domain.exception;

public class TicketUnavailableException extends RuntimeException {
  public TicketUnavailableException(String message) {
    super(message);
  }
}
