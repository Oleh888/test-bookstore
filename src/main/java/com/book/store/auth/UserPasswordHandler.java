package com.book.store.auth;

import com.book.store.exception.PasswordHashingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordHandler {

  private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
  private static final int HASH_ITERATION = 65536;
  private static final int KEY_LENGTH = 128;

  private final SecureRandom secureRandom;
  private final SecretKeyFactory secretKeyFactory;

  public UserPasswordHandler() throws NoSuchAlgorithmException {
    this.secureRandom = new SecureRandom();
    this.secretKeyFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
  }

  public byte[] generateRandomSalt() {
    byte[] salt = new byte[16];
    secureRandom.nextBytes(salt);
    return salt;
  }

  public String hashPassword(String password, byte[] salt) {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATION, KEY_LENGTH);
    try {
      return new String(secretKeyFactory.generateSecret(spec).getEncoded());
    } catch (InvalidKeySpecException e) {
      throw new PasswordHashingException();
    }
  }
}
