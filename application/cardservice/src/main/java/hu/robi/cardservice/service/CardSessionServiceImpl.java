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
public class CardSessionServiceImpl implements CardSessionService {

    private final CardRepository cardRepository;
    private final ConversionService conversionService;
    private final CardValidationService cardValidationService;
    Logger logger = LoggerFactory.getLogger(CardSessionServiceImpl.class);

    public CardSessionServiceImpl(CardRepository cardRepository, ConversionService conversionService, CardValidationService cardValidationService) {
        this.cardRepository = cardRepository;
        this.conversionService = conversionService;
        this.cardValidationService = cardValidationService;
    }

    @Override
    public RestCard requestCard(String inputCardNumber) {
        Optional<Card> requestedCard = cardRepository.findById(inputCardNumber);
        RestCard requestedRestCard;
        if (requestedCard.isPresent()) {
            requestedRestCard = conversionService.convertCardToRestCard(requestedCard.get());
        } else {
            logger.warn("Kártya lekérdezése sikertelen (nem található kártyaszám): " + inputCardNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A megadott kártyaszám nem található az adatbázisban.");
        }
        return requestedRestCard;
    }

    @Override
    public void createCard(RestCard inputRestCard) {
        Card createdCard = conversionService.convertRestCardToCard(inputRestCard);

        cardRepository.save(createdCard);
        logger.debug("Kártya sikeresen mentve az adatbázisba: " + createdCard.getCardNumber());
    }

    @Override
    public String validateCard(RestCard inputRestCard) {
        return (cardValidationService.validateCard(inputRestCard));
    }

    @Override
    public void disableCard(String cardNumber) {
        Optional<Card> result = cardRepository.findById(cardNumber);
        if (!result.isPresent()) {
            logger.warn("Sikertelen letiltás kérés kártyaszámra (nem található kártyaszám): " + cardNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A megadott kártyaszám nem található az adatbázisban.");
        }
        Card theCard = result.get();
        theCard.setIsDisabledRaw('Y');

        cardRepository.save(theCard);
        logger.debug("Kártya sikeresen frissítve az adatbázisban: " + theCard.getCardNumber());

    }
}
