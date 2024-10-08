package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.domain.exception.TicketUnavailableException;
import cart.ticket.ticketservice.domain.model.Ticket;
import cart.ticket.ticketservice.domain.model.TicketStatus;
import cart.ticket.ticketservice.infrastructure.repository.TicketRepository;
import cart.ticket.ticketservice.presentation.dto.TicketStatusUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

  private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
  private static final String TICKET_STATUS_TOPIC = "ticket-status-topic";
  private final TicketRepository ticketRepository;
  private final KafkaProducerService producer;

  public TicketService(TicketRepository ticketRepository, KafkaProducerService producer) {
    this.ticketRepository = ticketRepository;
    this.producer = producer;
  }

  @Transactional
  public void purchaseTicket(Long ticketId) {
    try {
      ticketRepository
          .findByIdAndLock(ticketId, TicketStatus.AVAILABLE)
          .map(this::markTicketStatusPending)
          .orElseThrow(() -> new TicketUnavailableException("Ticket is not available"));
    } catch (TicketUnavailableException e) {
      logger.warn("Failed to purchase ticket: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error(
          "Unexpected error occurred while purchasing ticket with ID {}: {}",
          ticketId,
          e.getMessage(),
          e);
      throw new RuntimeException("Failed to purchase ticket due to an unexpected error", e);
    }

    sendTicketStatusSold(ticketId);
  }

  private Ticket markTicketStatusPending(Ticket ticket) {
    ticket.setStatus(TicketStatus.PENDING);
    return ticketRepository.save(ticket);
  }

  @Async
  public void sendTicketStatusSold(Long ticketId) {
    String message =
        new TicketStatusUpdate(ticketId, TicketStatus.PENDING, TicketStatus.SOLD).toJson();
    try {
      producer.sendMessage(TICKET_STATUS_TOPIC, message);
      this.updateTicketStatus(ticketId, TicketStatus.SOLD);
    } catch (Exception e) {
      logger.error(
          "Failed to send Kafka message for ticket ID {}. Rolling back ticket status to AVAILABLE.",
          ticketId);
      this.updateTicketStatus(ticketId, TicketStatus.AVAILABLE);
    }
  }

  private void updateTicketStatus(Long ticketId, TicketStatus toStatus) {
    ticketRepository
        .findById(ticketId)
        .ifPresent(
            ticket -> {
              ticket.setStatus(toStatus);
              ticketRepository.save(ticket);
              logger.info("Ticket ID {} status updated to {}", ticketId, toStatus);
            });
  }
}
