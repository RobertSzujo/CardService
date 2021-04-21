package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;

import java.util.Optional;

public class CardValidationService {

    //define fields
    RestCard inputRestCard;

    //define constructor
    public CardValidationService(RestCard inputRestCard) {
        this.inputRestCard = inputRestCard;
    }


    //define methods

    public String validateCard(CardTypeRepository cardTypeRepository, CardRepository cardRepository) {

        RestCardVerificationService restCardVerificationService = new RestCardVerificationService(inputRestCard);
        String verificationResult = restCardVerificationService.verifyCardForValidation(cardTypeRepository, cardRepository);
        if (!verificationResult.equals("OK")) {
            return verificationResult;
        }

        String cardNumber = inputRestCard.getCardNumber();
        String cardType = inputRestCard.getCardType();
        String validThru = inputRestCard.getValidThru();
        String cvv = inputRestCard.getCvv();

        Optional<Card> foundCard = cardRepository.findById(cardNumber);
        Card matchedCard = foundCard.get();

        //check cardType, validThru (must match with the card) and isDisabled (must be false or 'N')
        if (!cardType.equals(matchedCard.getCardType().getCardType()) ||
                !validThru.equals(matchedCard.getValidThru()) ||
                matchedCard.getIsDisabledRaw() == 'Y') {
            return "INVALID";
        }

        String inputCardHash = inputRestCard.createHash();

        if (!inputCardHash.equals(matchedCard.getCardHash())) {
            return "INVALID";
        }

        //return true if everything is OK
        return "VALID";
    }

}
