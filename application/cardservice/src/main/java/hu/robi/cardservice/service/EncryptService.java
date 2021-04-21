package hu.robi.cardservice.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptService {

    //define fields

    byte[] salt;
    SecretKeyFactory secretKeyFactory;

    //define constructor

    public EncryptService() {
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);

        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //define methods

    public String EncryptString(String stringToEncrypt) {
        KeySpec keySpec = new PBEKeySpec(stringToEncrypt.toCharArray(), salt, 65536, 128);

        byte[] hash = new byte[0];
        try {
            hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        String encodedHash = Base64.getEncoder().encodeToString(hash);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String encryptedString = encodedSalt + encodedHash;

        return encryptedString;
    }

    public void generateSaltFromBase64 (String encodedSalt)
    {
        salt = Base64.getDecoder().decode(encodedSalt);
    }

}
