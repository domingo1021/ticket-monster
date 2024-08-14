package cart.ticket.authservice.application.exception;

public class OAuthUserNoPwdException extends RuntimeException {
  public OAuthUserNoPwdException(String message) {
    super(message);
  }
}
