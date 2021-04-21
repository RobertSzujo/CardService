package hu.robi.cardservice.service;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConversionService {
    //define fields
    boolean isExistingOwner = false;

    //define constructor

    public ConversionService() {
    }

    //define methods

    public RestCard convertCardToRestCard(Card inputCard) {
        RestCard restCard = new RestCard();

        restCard.setCardType(inputCard.getCardType().getCardType());
        restCard.setCardNumber(inputCard.getCardNumber());
        restCard.setValidThru(inputCard.getValidThru());
        restCard.setDisabled(convertDisabledToBoolean(inputCard.getIsDisabledRaw()));
        restCard.setOwner(inputCard.getOwner().getOwner());
        restCard.setContactInfo(convertContactListToRestContactList(inputCard.getOwner().getContacts()));

        return restCard;
    }

    public Card convertRestCardToCard(RestCard inputRestCard, CardRepository cardRepository, CardTypeRepository cardTypeRepository, OwnerRepository ownerRepository, ContactRepository contactRepository) {

        inputRestCard.verifyRestCard(cardTypeRepository, cardRepository);

        Card card = new Card();

        card.setCardNumber(inputRestCard.getCardNumber());
        card.setValidThru(inputRestCard.getValidThru());
        card.setIsDisabledRaw('N'); //default value for new cards
        card.setCardType(createCardType(cardTypeRepository, inputRestCard.getCardType()));
        card.setOwner(createOwner(ownerRepository, inputRestCard.getOwner()));
        card.getOwner().setContacts(convertRestContactListToContactList(contactRepository, inputRestCard.getContactInfo(), card.getOwner().getOwnerId(), isExistingOwner));
        card.setCardHash(inputRestCard.createHash());

        return card;
    }

    //helper methods for Card -> RestCard conversion
    private boolean convertDisabledToBoolean(char disabledChar)
    {
        boolean disabledBoolean = false;
        if (disabledChar == 'Y') {
            disabledBoolean = true;
        }
        return disabledBoolean;
    }

    private List<RestContact> convertContactListToRestContactList(List<Contact> contactList)
    {
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
            isExistingOwner = true;
            return (matchingOwner.get());
        }
        else {
            cardOwner.setOwner(ownerName);
        }
        return cardOwner;
    }

    private List<Contact> convertRestContactListToContactList(ContactRepository contactRepository, List<RestContact> inputRestContactList, int ownerId, boolean isExistingOwner)
    {

        if (inputRestContactList.size() == 0 && !isExistingOwner) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Új kártyatulajdonos felvétele esetén kötelező legalább egy elérhetőséget megadni.");
        }

        List<Contact> result = new ArrayList<>();

        for (RestContact currentRestContact : inputRestContactList)
        {
            Contact currentContact = new Contact();
            //use a matching contact if found, or create a new if it does not exist
            Optional<Contact> matchingContact = contactRepository.findByContact(currentRestContact.getContact());

            if (!matchingContact.isPresent()) {
                currentContact.setOwnerId(ownerId);
                currentContact.setContactType(currentRestContact.getType());
                currentContact.setContact(currentRestContact.getContact());
                result.add(currentContact);
            }

        }

        return result;
    }


}
