package hu.robi.cardservice.service;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class EncryptService {

    //define fields

    private SecretKeyFactory secretKeyFactory;

    //define constructor

    public EncryptService() {
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //define methods

    public String createNewHash(String stringToHash) {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return encryptString(stringToHash, salt);
    }

    public boolean equalsToHash(String stringToCompare, String hashToCompare) {
        byte[] salt;
        salt = Base64.getDecoder().decode(hashToCompare.substring(0, 24));

        String encryptedString = encryptString(stringToCompare, salt);

        return encryptedString.equals(hashToCompare);
    }

    private String encryptString(String stringToEncrypt, byte[] salt) {
        KeySpec keySpec = new PBEKeySpec(stringToEncrypt.toCharArray(), salt, 65536, 128);

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = new byte[0];
        try {
            hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        String encodedHash = Base64.getEncoder().encodeToString(hash);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        return encodedSalt + encodedHash;
    }

}
