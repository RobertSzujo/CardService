package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.CardType;
import hu.robi.cardservice.entity.RestCard;
import hu.robi.cardservice.entity.RestContact;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class RestCardVerificationService {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    //define fields
    Logger logger = LoggerFactory.getLogger(RestCardVerificationService.class);

    //define constructor

    public RestCardVerificationService(CardRepository cardRepository, CardTypeRepository cardTypeRepository) {
        this.cardRepository = cardRepository;
        this.cardTypeRepository = cardTypeRepository;
    }

    //define methods

    public String verifyCardForValidation(RestCard inputRestCard) {

        String cardDataResult = verifyCardData(inputRestCard);
        if (!cardDataResult.equals("OK")) {
            return cardDataResult;
        }

        String cardDoesNotExistResult = verifyCardDoesNotExist(inputRestCard);
        if (cardDoesNotExistResult.equals("OK")) {
            return "A megadott kártyaszám nem található az adatbázisban.";
        }

        return "OK";
    }

    public String verifyCardForCreation(RestCard inputRestCard) {

        String cardDataResult = verifyCardData(inputRestCard);
        if (!cardDataResult.equals("OK")) {
            return cardDataResult;
        }

        String cardDoesNotExistResult = verifyCardDoesNotExist(inputRestCard);
        if (!cardDoesNotExistResult.equals("OK")) {
            return cardDoesNotExistResult;
        }

        String ownerResult = verifyOwner(inputRestCard);
        if (!ownerResult.equals("OK")) {
            return ownerResult;
        }

        String contactsResult = verifyContacts(inputRestCard);
        if (!contactsResult.equals("OK")) {
            return contactsResult;
        }

        return "OK";
    }

    private String verifyCardData(RestCard inputRestCard) {
        String cardTypeResult = verifyCardType(inputRestCard);
        if (!cardTypeResult.equals("OK")) {
            return cardTypeResult;
        }

        String cardNumberResult = verifyCardNumber(inputRestCard);
        if (!cardNumberResult.equals("OK")) {
            return cardNumberResult;
        }

        String validThruResult = verifyValidThru(inputRestCard);
        if (!validThruResult.equals("OK")) {
            return validThruResult;
        }

        String cvvResult = verifyCvv(inputRestCard);
        if (!cvvResult.equals("OK")) {
            return cvvResult;
        }

        return "OK";
    }

    private String verifyCardType(RestCard inputRestCard) {
        String cardType = inputRestCard.getCardType();

        if (cardType == null) {
            return "Nem került megadásra kártyatípus!";
        }

        Optional<CardType> matchingCardType = cardTypeRepository.findByCardType(cardType);
        if (!matchingCardType.isPresent()) {
            return "A megadott kártyatípus nem található az adatbázisban.";
        }

        return "OK";
    }

    private String verifyCardNumber(RestCard inputRestCard) {
        String cardNumber = inputRestCard.getCardNumber();
        String cardType = inputRestCard.getCardType();

        if (cardNumber == null) {
            return "Nem került megadásra kártyaszám!";
        }

        if (cardType.contains("MASTER") && !CreditCardValidator.MASTERCARD_VALIDATOR.isValid(cardNumber)) {
            return "A megadott MasterCard kártyaszám nem érvényes!";
        }

        if (cardType.contains("VISA") && !CreditCardValidator.VISA_VALIDATOR.isValid(cardNumber)) {
            return "A megadott VISA kártyaszám nem érvényes!";
        }

        return "OK";
    }

    private String verifyCardDoesNotExist(RestCard inputRestCard) {
        String cardNumber = inputRestCard.getCardNumber();
        Optional<Card> matchingCard = cardRepository.findById(cardNumber);
        if (matchingCard.isPresent()) {
            return "A megadott kártyaszám alapján már létezik kártya az adatbázisban.";
        }

        return "OK";
    }

    private String verifyValidThru(RestCard inputRestCard) {
        String validThru = inputRestCard.getValidThru();

        if (validThru == null) {
            return "Nem került megadásra érvényességi dátum!";
        }

        Pattern validThruPattern = Pattern.compile("\\d{2}\\/{1}\\d{2}");
        if (!validThruPattern.matcher(validThru).matches()) {
            return "Az érvényességi idő nem érvényes, MM/YY formátumnak kell lennie. Például: 04/23 (2023. április)";
        }

        int validThruMonth = Integer.parseInt(validThru.substring(0, 2));
        if (validThruMonth > 12 || validThruMonth < 1) {
            return "Az érvényességi idő hónapja nem érvényes, 01 és 12 közötti szám lehet. Például: 04/23 (2023. április)";
        }

        return "OK";
    }

    private String verifyCvv(RestCard inputRestCard) {
        String cvv = inputRestCard.getCvv();

        if (cvv == null) {
            return "Nem került megadásra CVV kód!";
        }

        Pattern cvvPattern = Pattern.compile("\\d{3}");
        if (!cvvPattern.matcher(cvv).matches()) {
            return "A CVV nem érvényes, 3 számjegyből kell állnia. Például: 408";
        }

        return "OK";
    }

    private String verifyOwner(RestCard inputRestCard) {
        if (inputRestCard.getOwner() == null) {
            return "Nem került megadásra kártyatulajdonos!";
        }
        return "OK";
    }

    private String verifyContacts(RestCard inputRestCard) {
        Pattern smsPattern = Pattern.compile("\\+{1}\\d{7,15}");

        for (RestContact restContact : inputRestCard.getContactInfo()) {
            String contactType = restContact.getType();
            String contact = restContact.getContact();

            if (contactType == null) {
                return "Nem került megadásra a kapcsolat típusa!";
            }

            if (!contactType.equals("SMS") && !contactType.equals("EMAIL")) {
                return "A kapcsolat típusa nem megfelelő, csak a következő értékek adhatóak meg: EMAIL (e-mail cím esetén) vagy SMS (telefonszám esetén)";
            }
            if (contactType.equals("EMAIL") && !EmailValidator.getInstance().isValid(contact)) {
                return "A felvett e-mail cím (" + contact + ") nem megfelelő formátumú. Példa helyes formátumra: example@example.com";
            }

            if (contactType.equals("SMS") && !smsPattern.matcher(contact).matches()) {
                return "A felvett telefonszám (" + contact + ") nem megfelelő formátumú. Csak nemzetközi formátum fogadható el, +(országkód) kezdettel. Példa helyes formátumra: +36301234567";
            }
        }


        return "OK";
    }

}
