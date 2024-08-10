package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.domain.exception.TicketUnavailableException;
import cart.ticket.ticketservice.domain.model.Ticket;
import cart.ticket.ticketservice.domain.model.TicketStatus;
import cart.ticket.ticketservice.domain.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public void purchaseTicket(Long ticketId) {
        try {
          ticketRepository
              .findByIdAndLock(ticketId, TicketStatus.AVAILABLE)
              .map(this::markTicketAsSold)
              .orElseThrow(() -> new TicketUnavailableException("Ticket is not available"));
        } catch (TicketUnavailableException e) {
            logger.warn("Failed to purchase ticket: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while purchasing ticket with ID {}: {}", ticketId, e.getMessage(), e);
            throw new RuntimeException("Failed to purchase ticket due to an unexpected error", e);
        }
    }

    private Ticket markTicketAsSold(Ticket ticket) {
        ticket.setStatus(TicketStatus.SOLD);
        return ticketRepository.save(ticket);
    }

}
