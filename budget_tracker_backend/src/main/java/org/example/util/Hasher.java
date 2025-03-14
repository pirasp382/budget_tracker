package org.example.util;

import jakarta.inject.Singleton;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@UtilityClass
@Singleton
public class Hasher {

  private static final int SALT_LENGTH = 16;
  private static final int HASH_LENGTH = 64;
  private static final int ITERATIONS = 10000;

  public static String hashValue(final String input) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] encodedHash = digest.digest(input.getBytes());
      final StringBuilder hexString = new StringBuilder();
      for (final byte b : encodedHash) {
        final String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String hashpassword(final String password) {
    try {
      final byte[] salt = generateSalt();
      final byte[] hash = hashPasswordWithPBKDF2(password, salt);
      return Base64.getEncoder().encodeToString(salt)
          + ":"
          + Base64.getEncoder().encodeToString(hash);

    } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Error while hashing password", e);
    }
  }

  private static byte[] generateSalt() {
    final SecureRandom secureRandom = new SecureRandom();
    final byte[] salt = new byte[SALT_LENGTH];
    secureRandom.nextBytes(salt);
    return salt;
  }

  private static byte[] hashPasswordWithPBKDF2(final String password, final byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    final PBEKeySpec spec =
        new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH * 8);
    final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    return factory.generateSecret(spec).getEncoded();
  }

  public static boolean validatePassword(final String password, final String storedHashValue) {

    try {
      final String[] parts = storedHashValue.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid stored hash format");
      }
      final byte[] salt = Base64.getDecoder().decode(parts[0]);
      final byte[] storedHash = Base64.getDecoder().decode(parts[1]);
      final byte[] computedHash = hashPasswordWithPBKDF2(password, salt);
      return constantTimeEquals(storedHash, computedHash);

    } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Error while validating password", e);
    }
  }

  private static boolean constantTimeEquals(final byte[] a, final byte[] b) {
    if (a.length != b.length) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }
}
