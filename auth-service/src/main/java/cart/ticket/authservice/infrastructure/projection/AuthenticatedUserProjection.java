package cart.ticket.authservice.infrastructure.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Set;

public interface AuthenticatedUserProjection {
    String getFirstName();

    String getLastName();

    String getUsername();

    String getPhotoUrl();

    Instant getCreatedAt();

    @Value("#{target.getRoleNames}")
    Set<String> getRoleNames();

    @Value("#{target.getIsAuthenticated}")
    Boolean getIsAuthenticated();

    @Value("#{target.getIsNotAuthenticated}")
    Boolean getIsNotAuthenticated();

    @Value("#{target.getIsAdmin}")
    Boolean getIsAdmin();

    @Value("#{target.getIsNotAdmin}")
    Boolean getIsNotAdmin();
}