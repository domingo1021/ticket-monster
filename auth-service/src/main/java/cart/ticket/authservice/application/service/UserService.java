package cart.ticket.authservice.application.service;

import cart.ticket.authservice.application.exception.AuthenticationFailedException;
import cart.ticket.authservice.application.exception.UserNotFoundException;
import cart.ticket.authservice.application.exception.OAuthUserNoPwdException;
import cart.ticket.authservice.domain.User;
import cart.ticket.authservice.infrastructure.repository.UserRepository;
import cart.ticket.authservice.presentation.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public UserDTO getUserById(Long id) {
    return userRepository
        .findById(id)
        .map(UserMapper::convertUserEntityToDTO)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  public void authenticateUser(String email, String password) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) throw new AuthenticationFailedException("Authentication failed");

    var user = userOptional.get();
    if (user.getIsOAuth2User()) {
      throw new OAuthUserNoPwdException(
          String.format("User with email %s is an OAuth2 user", email));
    }

    if (!this.passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthenticationFailedException("Authentication failed");
    }
  }

  public UserDTO handleOAuth2User(String email, String username) {
    UserDTO user = userRepository.findByEmail(email).map(UserMapper::convertUserEntityToDTO).orElse(null);
    if (user != null) return user;

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setUsername(username);
    newUser.setIsOAuth2User(true);
    userRepository.save(newUser);

    return UserMapper.convertUserEntityToDTO(newUser);
  }
}
