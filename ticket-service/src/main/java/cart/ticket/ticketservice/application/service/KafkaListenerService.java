package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.domain.model.SnowflakeIdGenerator;
import cart.ticket.ticketservice.presentation.dto.TicketStatusUpdate;
import cart.ticket.ticketservice.presentation.handler.WebSocketHandler;
import cart.ticket.ticketservice.presentation.handler.WebSocketHandler.WebSocketConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {
  private static final String TICKET_STATUS_TOPIC = "ticket-status-topic";
  private final WebSocketHandler webSocketHandler;

  public KafkaListenerService(WebSocketHandler webSocketHandler, SnowflakeIdGenerator snowflakeIdGenerator) {
    this.webSocketHandler = webSocketHandler;
  }

  @KafkaListener(topics = KafkaListenerService.TICKET_STATUS_TOPIC, groupId = "#{T(java.lang.String).valueOf(@snowflakeIdGenerator.getId())}")
  public void consume(TicketStatusUpdate ticketStatusUpdate) {
    String message = ticketStatusUpdate.toJson();
    webSocketHandler.sendMessageToSubscribers(WebSocketConstants.TICKET_STATUS, message);
  }

}
