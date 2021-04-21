package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class CardValidationService {

    //define fields
    RestCard inputRestCard;
    String cardNumber;
    String cardType;
    String validThru;
    String cvv;

    //define constructor
    public CardValidationService(RestCard inputRestCard) {
        this.inputRestCard = inputRestCard;
        cardNumber = inputRestCard.getCardNumber();
        cardType = inputRestCard.getCardType();
        validThru = inputRestCard.getValidThru();
        cvv = inputRestCard.getCvv();
    }


    //define methods

    public String validateCard(CardTypeRepository cardTypeRepository, CardRepository cardRepository) {

        verifyInputData(cardTypeRepository, cardRepository);

        Optional<Card> foundCard = cardRepository.findById(cardNumber);
        Card matchedCard = foundCard.get();

        //check cardType, validThru (must match with the card) and isDisabled (must be false or 'N')
        if (!cardType.equals(matchedCard.getCardType().getCardType()) ||
                !validThru.equals(matchedCard.getValidThru()) ||
                matchedCard.getIsDisabledRaw() == 'Y') {
            return "INVALID";
        }

        String inputCardHash = hashInputCard(cardNumber, validThru, cvv, matchedCard);
        if (!inputCardHash.equals(matchedCard.getCardHash())) {
            return "INVALID";
        }

        return "VALID";
    }

    private void verifyInputData(CardTypeRepository cardTypeRepository, CardRepository cardRepository) {
        RestCardVerificationService restCardVerificationService = new RestCardVerificationService(inputRestCard);
        String verificationResult = restCardVerificationService.verifyCardForValidation(cardTypeRepository, cardRepository);
        if (!verificationResult.equals("OK")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, verificationResult);
        }
    }

    private String hashInputCard(String cardNumber, String validThru, String cvv, Card matchedCard) {
        String inputDataToHash = cardNumber + validThru + cvv;
        EncryptService encryptService = new EncryptService();
        encryptService.generateSaltFromBase64(matchedCard.getCardHash().substring(0,24));
        String inputCardHash= encryptService.EncryptString(inputDataToHash);
        return inputCardHash;
    }

}
