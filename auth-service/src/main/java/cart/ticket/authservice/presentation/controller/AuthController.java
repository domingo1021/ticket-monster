package cart.ticket.authservice.presentation.controller;

import cart.ticket.authservice.application.service.AuthService;
import cart.ticket.authservice.presentation.dto.ApiResponse;
import cart.ticket.authservice.presentation.dto.AuthRequestDTO;
import cart.ticket.authservice.presentation.dto.AuthResponseDTO;

import cart.ticket.authservice.presentation.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequestDTO request) {
        var user = authService.signup(request.email(), request.password(), request.username());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthRequestDTO request) {
        var token = authService.signin(request.email(), request.password());
        return ResponseEntity.ok(ApiResponse.success(new AuthResponseDTO(token)));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}