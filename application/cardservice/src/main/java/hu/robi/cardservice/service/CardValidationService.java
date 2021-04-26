package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CardValidationService {

    //define fields
    private final CardRepository cardRepository;
    private final EncryptService encryptService;
    private final RestCardVerificationService restCardVerificationService;

    Logger logger = LoggerFactory.getLogger(CardValidationService.class);

    //define constructor
    public CardValidationService(CardRepository cardRepository, EncryptService encryptService, RestCardVerificationService restCardVerificationService) {
        this.cardRepository = cardRepository;
        this.encryptService = encryptService;
        this.restCardVerificationService = restCardVerificationService;
    }


    //define methods

    public String validateCard(RestCard inputRestCard) {

        verifyInputData(inputRestCard);

        Optional<Card> foundCard = cardRepository.findById(inputRestCard.getCardNumber());
        Card matchedCard = foundCard.get();

        String cardNumber = inputRestCard.getCardNumber();
        String cardType = inputRestCard.getCardType();
        String validThru = inputRestCard.getValidThru();
        String cvv = inputRestCard.getCvv();

        //check cardType, validThru (must match with the card) and isDisabled (must be false or 'N')
        if (!cardType.equals(matchedCard.getCardType().getCardType()) ||
                !validThru.equals(matchedCard.getValidThru()) ||
                matchedCard.getIsDisabledRaw() == 'Y') {
            return "INVALID";
        }

        String inputCardDataToHash = cardNumber + validThru + cvv;
        if (!encryptService.equalsToHash(inputCardDataToHash, matchedCard.getCardHash())) {
            return "INVALID";
        }

        return "VALID";
    }

    private void verifyInputData(RestCard inputRestCard) {
        String cardVerificationResult = restCardVerificationService.verifyCardForValidation(inputRestCard);
        if (!cardVerificationResult.equals("OK")) {
            logger.warn("Hiba a " + inputRestCard.getCardNumber() + " kártya ellenőrzése során: " + cardVerificationResult);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, cardVerificationResult);
        }
    }

}
