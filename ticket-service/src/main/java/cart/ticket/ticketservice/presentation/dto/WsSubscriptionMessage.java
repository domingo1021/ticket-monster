package cart.ticket.ticketservice.presentation.dto;

public record WsSubscriptionMessage(String action, Data data) {

  public record Data(String type) {}
}
