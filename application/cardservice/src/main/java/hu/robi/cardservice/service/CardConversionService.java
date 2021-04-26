package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class CardConversionService {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final OwnerRepository ownerRepository;
    private final RestCardVerificationService restCardVerificationService;
    private final EncryptService encryptService;
    Logger logger = LoggerFactory.getLogger(CardConversionService.class);

    public CardConversionService(CardRepository cardRepository, CardTypeRepository cardTypeRepository, OwnerRepository ownerRepository, RestCardVerificationService restCardVerificationService, EncryptService encryptService) {
        this.cardRepository = cardRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.ownerRepository = ownerRepository;
        this.restCardVerificationService = restCardVerificationService;
        this.encryptService = encryptService;
    }

    //define methods

    public RestCard convertCardToRestCard(Card inputCard) {
        logger.debug("Card objektum átalakítása RestCard objektummá: " + inputCard.getCardNumber());

        RestCard restCard = new RestCard();

        restCard.setCardType(inputCard.getCardType().getCardType());
        restCard.setCardNumber(inputCard.getCardNumber());
        restCard.setValidThru(inputCard.getValidThru());
        restCard.setDisabled(convertDisabledToBoolean(inputCard.getIsDisabledRaw()));
        restCard.setOwner(inputCard.getOwner().getOwner());
        restCard.setContactInfo(convertContactListToRestContactList(inputCard.getOwner().getContacts()));

        logger.debug("Card objektum sikeresen átalakítva RestCard objektummá: " + restCard.getCardNumber());
        return restCard;
    }

    public Card convertRestCardToCard(RestCard inputRestCard) {
        logger.debug("RestCard objektum átalakítása Card objektummá: " + inputRestCard.getCardNumber());

        verifyRestCard(inputRestCard);

        Card card = new Card();

        card.setCardNumber(inputRestCard.getCardNumber());
        card.setValidThru(inputRestCard.getValidThru());
        card.setIsDisabledRaw('N'); //default value for new cards
        card.setCardType(createCardType(inputRestCard.getCardType()));
        card.setOwner(createOwner(inputRestCard.getOwner()));
        card.getOwner().setContacts(convertRestContactListToContactList(inputRestCard.getContactInfo(), card.getOwner()));
        card.setCardHash(createHash(inputRestCard));

        logger.debug("RestCard objektum sikeresen átalakítva Card objektummá: " + card.getCardNumber());
        return card;
    }

    //helper methods for Card -> RestCard conversion
    private boolean convertDisabledToBoolean(char disabledChar) {
        boolean disabledBoolean = disabledChar == 'Y';
        return disabledBoolean;
    }

    private List<RestContact> convertContactListToRestContactList(List<Contact> contactList) {
        List<RestContact> restContactList = new ArrayList<>();

        for (Contact contact : contactList) {
            RestContact restContact = convertContactToRestContact(contact);
            restContactList.add(restContact);
        }

        return restContactList;
    }

    public RestContact convertContactToRestContact(Contact theContact) {
        RestContact restContact = new RestContact();
        restContact.setType(theContact.getContactType());
        restContact.setContact(theContact.getContact());
        return restContact;
    }

    //helper methods for RestCard -> Card conversion
    private CardType createCardType(String cardTypeName) {
        Optional<CardType> matchingCardType = cardTypeRepository.findByCardType(cardTypeName);
        return (matchingCardType.get());
    }

    private Owner createOwner(String ownerName) {
        Owner cardOwner = new Owner();

        Optional<Owner> matchingOwner = ownerRepository.findByOwner(ownerName);
        if (matchingOwner.isPresent()) {
            return (matchingOwner.get());
        } else {
            cardOwner.setOwner(ownerName);
        }
        return cardOwner;
    }

    private List<Contact> convertRestContactListToContactList(List<RestContact> inputRestContactList, Owner cardOwner) {

        List<Contact> contactList = cardOwner.getContacts();

        if (contactList == null) {
            if (inputRestContactList.size() == 0) {
                logger.warn("Hiba a kártya létrehozása során: Nincs a kártyatulajdonosnak elérhetősége!");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A kártyatulajdonosnak nincs elérhetősége, legalább egyet meg kell adni!");
            } else {
                contactList = new ArrayList<>();
            }
        }

        for (RestContact inputRestContact : inputRestContactList) {
            List<Contact> finalContactList = contactList;
            int matchingContactIndex = IntStream.range(0, contactList.size())
                    .filter(i -> finalContactList.get(i).getContact().equals(inputRestContact.getContact()))
                    .findFirst().orElse(-1);

            if (matchingContactIndex == -1) {
                Contact newContact = new Contact();
                newContact.setOwner(cardOwner);
                newContact.setContactType(inputRestContact.getType());
                newContact.setContact(inputRestContact.getContact());
                contactList.add(newContact);
            }
        }

        return contactList;
    }

    private void verifyRestCard(RestCard inputRestCard) {
        String cardVerificationResult = restCardVerificationService.verifyCardForCreation(inputRestCard);
        if (!cardVerificationResult.equals("OK")) {
            logger.warn("Hiba a " + inputRestCard.getCardNumber() + " kártya létrehozása során: " + cardVerificationResult);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, cardVerificationResult);
        }
    }

    public String createHash(RestCard inputRestCard) {
        String cardDataToHash = inputRestCard.getCardNumber() + inputRestCard.getValidThru() + inputRestCard.getCvv();
        String hash = encryptService.createNewHash(cardDataToHash);
        return hash;
    }


}
