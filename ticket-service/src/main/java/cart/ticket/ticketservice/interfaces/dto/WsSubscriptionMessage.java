package cart.ticket.ticketservice.interfaces.dto;

public record WsSubscriptionMessage(String action, Data data) {

  public record Data(String type) {}
}
