package cart.ticket.authservice.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * UserDTO is a DTO that exclude password hash field from User entity.
 */
@Getter
@Setter
@Builder
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private Boolean isOAuth2User;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
