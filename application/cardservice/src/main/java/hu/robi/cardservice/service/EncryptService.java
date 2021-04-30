package hu.robi.cardservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

    Logger logger = LoggerFactory.getLogger(EncryptService.class);
    private SecretKeyFactory secretKeyFactory;

    //define constructor

    public EncryptService() {
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Hiba az EncryptService létrehozása során: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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

        byte[] hash;
        try {
            hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            logger.error("Hiba a hash készítése közben: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        String encodedHash = Base64.getEncoder().encodeToString(hash);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        return encodedSalt + encodedHash;
    }

}
