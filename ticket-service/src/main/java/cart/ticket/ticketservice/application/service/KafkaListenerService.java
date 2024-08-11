package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.interfaces.dto.TicketStatusUpdate;
import cart.ticket.ticketservice.interfaces.handler.WebSocketHandler;
import cart.ticket.ticketservice.interfaces.handler.WebSocketHandler.WebSocketConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {
    private final WebSocketHandler webSocketHandler;

    public KafkaListenerService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(topics = "ticket-status-topic", groupId = "ticket-status-group")
    public void consume(TicketStatusUpdate ticketStatusUpdate) {
        String message = ticketStatusUpdate.toJson();
        webSocketHandler.sendMessageToSubscribers(WebSocketConstants.TICKET_STATUS, message);
    }
}