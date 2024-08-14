package cart.ticket.authservice.application.service;

import cart.ticket.authservice.application.exception.AuthenticationFailedException;
import cart.ticket.authservice.application.exception.OAuthUserNoPwdException;
import cart.ticket.authservice.application.exception.UserAlreadyExistException;
import cart.ticket.authservice.application.exception.UserNotFoundException;
import cart.ticket.authservice.domain.User;
import cart.ticket.authservice.infrastructure.repository.UserRepository;
import cart.ticket.authservice.infrastructure.security.JwtProvider;

import cart.ticket.authservice.presentation.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
      UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO signup(String email, String password, String username) {
    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent() && existingUser.get().getIsOAuth2User()) {
      throw new OAuthUserNoPwdException("User with email " + email + " is an OAuth2 user");
    } else if (existingUser.isPresent()) {
      throw new UserAlreadyExistException("User with email " + email + " already exists");
    }

    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setUsername(username);
    user.setIsOAuth2User(false);

    return UserMapper.convertUserEntityToDTO(userRepository.save(user));
  }

  public String signin(String email, String password) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Cannot find user with email " + email));

    if (user.getIsOAuth2User()) {
      throw new OAuthUserNoPwdException(
          "User with email " + email + " is an OAuth2 user, please sign in with OAuth2");
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthenticationFailedException("Authentication failed");
    }

    return jwtProvider.generateToken(user.getUserId());
  }
}
