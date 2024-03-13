package ntua.multimedia.libraryproject.utils;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;

public class EncryptionUtil {

  private static final String SECRET_KEY = "1234567890123456";
  private static final String ALGORITHM = "AES";

  public static String encrypt(String value) {
    try {
      Key key = generateKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encryptedBytes = cipher.doFinal(value.getBytes());
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong. Please try again");
      alert.showAndWait();
    }
    return null;
  }

  public static String decrypt(String encryptedValue) {
    try {
      Key key = generateKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
      return new String(decryptedBytes);
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong. Please try again");
      alert.showAndWait();
    }
    return null;
  }

  private static Key generateKey() {
    return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
  }
}
