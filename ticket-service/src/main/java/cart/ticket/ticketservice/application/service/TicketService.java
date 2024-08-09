package cart.ticket.ticketservice.application.service;

import cart.ticket.ticketservice.domain.model.Ticket;
import cart.ticket.ticketservice.domain.model.TicketStatus;
import cart.ticket.ticketservice.domain.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket buyTicket(Long ticketId) {
        return ticketRepository.findByIdAndLock(ticketId, TicketStatus.AVAILABLE)
                .map(ticket -> {
                    if (ticket.getStatus() == TicketStatus.AVAILABLE) {
                        ticket.setStatus(TicketStatus.SOLD);
                        return ticketRepository.save(ticket);
                    } else {
                        throw new RuntimeException("Ticket not available");
                    }
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }
}
