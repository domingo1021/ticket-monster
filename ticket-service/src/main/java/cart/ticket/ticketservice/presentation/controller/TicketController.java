package cart.ticket.ticketservice.presentation.controller;

import cart.ticket.ticketservice.application.service.TicketService;
import cart.ticket.ticketservice.presentation.dto.ApiResponse;

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

  @PostMapping("/{ticketId}/purchases")
  public ResponseEntity<ApiResponse<Void>> buyTicket(@PathVariable Long ticketId) {
    logger.info("Buying ticket with id: {}", ticketId);
    ticketService.purchaseTicket(ticketId);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
