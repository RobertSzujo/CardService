package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ConversionService {

    Logger logger = LoggerFactory.getLogger(ConversionService.class);

    //define constructor

    public ConversionService() {
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

    public Card convertRestCardToCard(RestCard inputRestCard, CardRepository cardRepository, CardTypeRepository cardTypeRepository, OwnerRepository ownerRepository, ContactRepository contactRepository) {
        logger.debug("RestCard objektum átalakítása Card objektummá: " + inputRestCard.getCardNumber());

        inputRestCard.verifyRestCard(cardTypeRepository, cardRepository);

        Card card = new Card();

        card.setCardNumber(inputRestCard.getCardNumber());
        card.setValidThru(inputRestCard.getValidThru());
        card.setIsDisabledRaw('N'); //default value for new cards
        card.setCardType(createCardType(cardTypeRepository, inputRestCard.getCardType()));
        card.setOwner(createOwner(ownerRepository, inputRestCard.getOwner()));
        card.getOwner().setContacts(convertRestContactListToContactList(contactRepository, inputRestCard.getContactInfo(), card.getOwner()));
        card.setCardHash(inputRestCard.createHash());

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
    private CardType createCardType(CardTypeRepository cardTypeRepository, String cardTypeName) {
        Optional<CardType> matchingCardType = cardTypeRepository.findByCardType(cardTypeName);
        return (matchingCardType.get());
    }

    private Owner createOwner(OwnerRepository ownerRepository, String ownerName) {
        Owner cardOwner = new Owner();

        Optional<Owner> matchingOwner = ownerRepository.findByOwner(ownerName);
        if (matchingOwner.isPresent()) {
            return (matchingOwner.get());
        } else {
            cardOwner.setOwner(ownerName);
        }
        return cardOwner;
    }

    private List<Contact> convertRestContactListToContactList(ContactRepository contactRepository, List<RestContact> inputRestContactList, Owner cardOwner) {

        List<Contact> contactList = cardOwner.getContacts();

        if (contactList == null) {
            if (inputRestContactList.size() == 0) {
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
                newContact.setOwnerId(cardOwner.getOwnerId());
                newContact.setContactType(inputRestContact.getType());
                newContact.setContact(inputRestContact.getContact());
                contactList.add(newContact);
            }
        }

        return contactList;
    }


}
