package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CardSessionServiceImpl implements CardSessionService {

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
    public List<Card> requestAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public RestCard requestCard(String inputCardNumber) {
        Optional<Card> result = cardRepository.findById(inputCardNumber);
        RestCard theCard = null;
        if (result.isPresent()) {
            theCard = new RestCard();
            theCard.convertCardToRestCard(result.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return theCard;
    }

    @Override
    public void createCard(RestCard inputRestCard) {
        Card theCard = inputRestCard.convertRestCardToCard(cardTypeRepository, ownerRepository, contactRepository);
        cardRepository.save(theCard);
    }

    //TODO: refactor this code
    @Override
    public boolean validateCard(RestCard inputRestCard) {
        //get data from input RestCard
        String cardNumber = inputRestCard.getCardNumber();
        String cardType = inputRestCard.getCardType();
        String validThru = inputRestCard.getValidThru();
        String cvv = inputRestCard.getCvv();

        //get card by card number (if exists)
        Optional<Card> result = cardRepository.findById(cardNumber);
        if (!result.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Card theCard = result.get();

        //check cardType, validThru and isDisabled
        if (!cardType.equals(theCard.getCardType().getCardType()) ||
                !validThru.equals(theCard.getValidThru()) ||
                theCard.getIsDisabledRaw() == 'Y') {
            return false;
        }

        //generate hash and check if it matches with the one in db
        String inputDataToHash = cardNumber + validThru + cvv;

        EncryptService encryptService = new EncryptService();
        encryptService.generateSaltFromBase64(theCard.getCardHash().substring(0,24));
        String hashedInputData= encryptService.EncryptString(inputDataToHash);

        if (!hashedInputData.equals(theCard.getCardHash())) {
            return false;
        }

        //return true if everything is OK
        return true;
    }

    @Override
    public void disableCard(String cardNumber) {
        //get card by card number (if exists)
        Optional<Card> result = cardRepository.findById(cardNumber);
        if (!result.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Card theCard = result.get();

        //disable card
        theCard.setIsDisabledRaw('Y');

        cardRepository.save(theCard);

    }
}
