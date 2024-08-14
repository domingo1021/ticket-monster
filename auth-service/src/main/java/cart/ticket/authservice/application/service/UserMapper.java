package cart.ticket.authservice.application.service;

import cart.ticket.authservice.domain.User;
import cart.ticket.authservice.presentation.dto.UserDTO;

public class UserMapper {
    public static UserDTO convertUserEntityToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isOAuth2User(user.getIsOAuth2User())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
