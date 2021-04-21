package hu.robi.cardservice.service;

import hu.robi.cardservice.entity.RestCard;
import java.util.regex.Pattern;

import hu.robi.cardservice.entity.RestContact;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class RestCardVerifyService {

    //define fields

    private RestCard inputRestCard;

    //define constructor

    public RestCardVerifyService(RestCard inputRestCard) {
        this.inputRestCard = inputRestCard;
    }

    //define methods

    public String verifyCard()
    {
        String cardNumberResult = verifyCardNumber();
        if (!cardNumberResult.equals("OK")) {
            return cardNumberResult;
        };

        String validThruResult = verifyValidThru();
        if (!validThruResult.equals("OK")) {
            return validThruResult;
        }

        String cvvResult = verifyCvv();
        if (!cvvResult.equals("OK")) {
            return cvvResult;
        }

        String contactResult = verifyContact();{
            if (!contactResult.equals("OK")) {
                return contactResult;
            }
        }

    return "OK";
    }

    private String verifyCardNumber() {
        String cardNumber = inputRestCard.getCardNumber();
        String cardType = inputRestCard.getCardType();

        if (cardNumber == null) {
            return "Nem került megadásra kártyaszám!";
        }
        if (cardType == null) {
            return "Nem került megadásra kártyatípus!";
        }

        Pattern cardNumberPattern = Pattern.compile("\\d{16}");
        if (!cardNumberPattern.matcher(cardNumber).matches()) {
            return "A kártyaszámnak 16 számjegyből kell állnia, más karakter (pl. kötőjel, szóköz) nem lehet benne. Például: 5339019326001410";
        }

        if (cardType.contains("Master") && !cardNumber.startsWith("5")) {
            return "A kártyszám nem érvényes, MasterCard esetén a kártyaszám csak 5-össel kezdődhet!";
        }

        if (cardType.contains("Visa") && !cardNumber.startsWith("4")) {
            return "A kártyaszám nem érvényes, Visa esetén a kártyaszám csak 4-essel kezdődhet!";
        }

        if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(cardNumber)) {
            return "A kártyaszám nem érvényes.";
        }

        return "OK";
    }

    private String verifyValidThru() {
        String validThru = inputRestCard.getValidThru();

        if (validThru == null) {
            return "Nem került megadásra érvényességi dátum!";
        }

        Pattern validThruPattern = Pattern.compile("\\d{2}\\/{1}\\d{2}");
        if (!validThruPattern.matcher(validThru).matches()) {
            return "Az érvényességi idő nem érvényes, MM/YY formátumnak kell lennie. Például: 04/23 (2023. április)";
        }

        int validThruMonth = Integer.parseInt(validThru.substring(0,2));
        if (validThruMonth > 12 || validThruMonth < 1) {
            return "Az érvényességi idő hónapja nem érvényes, 01 és 12 közötti szám lehet. Például: 04/23 (2023. április)";
        }

        return "OK";
    }

    private String verifyCvv() {
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

    private String verifyContact() {
        Pattern emailPattern = Pattern.compile("\\w+\\@{1}\\w+\\.{1}\\w+");
        Pattern smsPattern = Pattern.compile("\\+{1}\\d{7,15}");

        for (RestContact restContact : inputRestCard.getContactInfo()) {
            String contactType = restContact.getType();
            String contact = restContact.getContact();

            if (!contactType.equals("SMS") && !contactType.equals("EMAIL")) {
                return "A kapcsolat típusa nem megfelelő, csak a következő értékek adhatóak meg: EMAIL (e-mail cím esetén) vagy SMS (telefonszám esetén)";
            }
            if (contactType.equals("EMAIL") && !emailPattern.matcher(contact).matches()) {
                return "A felvett e-mail cím (" + contact + ") nem megfelelő formátumú. Példa helyes formátumra: example@example.com";
            }

            if (contactType.equals("SMS") && !smsPattern.matcher(contact).matches()) {
                return "A felvett telefonszám (" + contact + ") nem megfelelő formátumú. Csak nemzetközi formátum fogadható el, +(országkód) kezdettel. Példa helyes formátumra: +36301234567";
            }
        }

        return "OK";
    }

}
