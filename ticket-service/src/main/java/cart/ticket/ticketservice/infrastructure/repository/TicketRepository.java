package cart.ticket.ticketservice.infrastructure.repository;

import cart.ticket.ticketservice.domain.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.status = :status")
  Optional<Ticket> findByIdAndLock(Long id, TicketStatus status);

  Optional<Ticket> findById(Long id);

  Optional<Ticket> findByStatus(TicketStatus status);
}
