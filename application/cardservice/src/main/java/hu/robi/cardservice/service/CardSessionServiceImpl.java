package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import hu.robi.cardservice.rest.CardRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CardSessionServiceImpl implements CardSessionService {

    Logger logger = LoggerFactory.getLogger(CardSessionServiceImpl.class);

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final ContactRepository contactRepository;
    private final OwnerRepository ownerRepository;

    public CardSessionServiceImpl(CardRepository cardRepository, CardTypeRepository cardTypeRepository, ContactRepository contactRepository, OwnerRepository ownerRepository) {
        this.cardRepository = cardRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.contactRepository = contactRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public RestCard requestCard(String inputCardNumber) {
        Optional<Card> requestedCard = cardRepository.findById(inputCardNumber);
        RestCard requestedRestCard;
        if (requestedCard.isPresent()) {
            ConversionService conversionService = new ConversionService();
            requestedRestCard = conversionService.convertCardToRestCard(requestedCard.get());
        } else {
            logger.warn("Kártya lekérdezése sikertelen (nem található kártyaszám): " + inputCardNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A megadott kártyaszám nem található az adatbázisban.");
        }
        return requestedRestCard;
    }

    @Override
    public void createCard(RestCard inputRestCard) {
        ConversionService conversionService = new ConversionService();
        Card createdCard = conversionService.convertRestCardToCard(inputRestCard, cardRepository, cardTypeRepository, ownerRepository, contactRepository);
        cardRepository.save(createdCard);
    }

    @Override
    public String validateCard(RestCard inputRestCard) {
        CardValidationService cardValidationService = new CardValidationService(inputRestCard);
        return (cardValidationService.validateCard(cardTypeRepository, cardRepository));
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

    }
}
