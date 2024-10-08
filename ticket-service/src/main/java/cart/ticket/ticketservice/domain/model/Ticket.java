package cart.ticket.ticketservice.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String concert;
  private LocalDate date;
  private String venue;
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  public Ticket() {}
}
