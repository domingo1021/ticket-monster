package cart.ticket.authservice.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProvider {

  @Value("${jwt.expiration}")
  private Long expiration;
  private final SecretKey signingKey;
  private final String ISSUER = "Ticket Monster";


  public JwtProvider(@Value("${jwt.secret}") String secret) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  /**
   * reference: https://github.com/jwtk/jjwt
   *
   * @param userId the subject username
   * @return the generated token
   */
  public String generateToken(Long userId) {
    return Jwts.builder()
        .issuer(this.ISSUER)
        .subject(userId.toString())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .issuedAt(new Date(System.currentTimeMillis()))
        .signWith(this.signingKey)
        .compact();
  }

  public Boolean validateToken(String token) {
    var claims = getAllClaimsFromToken(token);
    return !isTokenExpired(claims) && isTokenIssuedByUs(claims);
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String getUserIdFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().verifyWith(this.signingKey).build().parseSignedClaims(token).getPayload();
  }

  private Boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }

  private Boolean isTokenIssuedByUs(Claims claims) {
    return this.ISSUER.equals(claims.getIssuer());
  }
}
