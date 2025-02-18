package org.example.util;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.dto.Message;
import org.example.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TokenManager {

  @Inject JWTParser parser;

  private static final Logger LOGGER = Logger.getLogger(TokenManager.class.getName());

  private static final String KEY =
      "-----BEGIN PRIVATE KEY-----\n"
          + "MIIEvAIBADANBgkqhkiG9w0BAQEFAASC..."
          + "-----END PRIVATE KEY-----";

  private static final String ENCRYPTIONKEY = "helloWorld";

  public static String createToken(final User user) {
    final String token =
        Jwt.subject(Long.toString(user.getId()))
            .groups("user")
            .claim("username", user.getUsername())
            .expiresAt(System.currentTimeMillis() + (24 * 3600 * 1000))
            .signWithSecret(KEY);
    return Encryption.encrypt(token, ENCRYPTIONKEY);
  }

  public List<Message> validateToken(String token) {
    final List<Message> errorList = new ArrayList<>();
    final String decryptedToken;
    if (token == null || !token.startsWith("Bearer:")) {
      errorList.add(Message.builder().title("Invalid token format").build());
      return errorList;
    } else {
      token = token.replace("Bearer: ", "");
      decryptedToken = Encryption.decrypt(token, ENCRYPTIONKEY);
    }
    try {
      final JsonWebToken jsonWebToken = parser.verify(decryptedToken, KEY);
      if (jsonWebToken.getExpirationTime() <= System.currentTimeMillis()) {
        errorList.add(Message.builder().title("Token already expired").build());
      }
    } catch (final ParseException parseException) {
      LOGGER.warning("Token validation failed: " + parseException.getMessage());
      errorList.add(Message.builder().title("Token validation failed").build());
    }
    return errorList;
  }

  public JsonWebToken extractToken(final String token) {
    final String tempToken = token.replace("Bearer: ", "");
    final String decryptedToken = Encryption.decrypt(tempToken, ENCRYPTIONKEY);
    try {
      return parser.parseOnly(decryptedToken);
    } catch (final Exception parseException) {
      LOGGER.warning("Token validation failed: " + parseException.getMessage());
      return null;
    }
  }

  public Long getUserId(final String token) {
    final String tempToken = token.replace("Bearer: ", "");
    final String decryptedToken = Encryption.decrypt(tempToken, ENCRYPTIONKEY);
    try {
      final JsonWebToken jsonWebToken = parser.parseOnly(decryptedToken);
      return Long.parseLong(jsonWebToken.getSubject());
    } catch (final ParseException parseException) {
      LOGGER.warning("Token validation failed: " + parseException.getMessage());
      return -1L;
    }
  }
}
