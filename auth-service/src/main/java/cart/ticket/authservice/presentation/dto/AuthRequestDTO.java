package cart.ticket.authservice.presentation.dto;

public record AuthRequestDTO (
    String username,
    String password,
    String email
) {}