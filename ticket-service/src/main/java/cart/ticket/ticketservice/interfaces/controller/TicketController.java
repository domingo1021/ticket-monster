package cart.ticket.ticketservice.interfaces.controller;

import cart.ticket.ticketservice.domain.model.Ticket;
import cart.ticket.ticketservice.application.service.TicketService;
import cart.ticket.ticketservice.interfaces.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @PostMapping("/buy/{ticketId}")
  public ResponseEntity<ApiResponse<Ticket>> buyTicket(@PathVariable Long ticketId) {
    logger.info("Buying ticket with id: {}", ticketId);
    try {
      Ticket ticket = ticketService.buyTicket(ticketId);
      return ResponseEntity.ok(ApiResponse.success(ticket));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(40000, e.toString()));
    }
  }
}
