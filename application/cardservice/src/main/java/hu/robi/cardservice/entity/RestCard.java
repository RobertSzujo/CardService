package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Card entity and a REST-compatible form vice-versa.

import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RestCard {
    //define fields

    private String cardType;

    private String cardNumber;

    private String validThru;

    private String cvv; //can only set

    private boolean disabled; //can only get

    private String owner;

    private List<RestContact> contactInfo;

    //define constructor

    public RestCard() {

    }

    //define getters/setters

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getValidThru() {
        return validThru;
    }

    public void setValidThru(String validThru) {
        this.validThru = validThru;
    }

    public void setCVV(String cvv) {
        this.cvv = cvv;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<RestContact> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<RestContact> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void convertCardToRestCard(Card theCard) {
        this.cardType = theCard.getCardType().getCardType();
        this.cardNumber = theCard.getCardNumber();
        this.validThru = theCard.getValidThru();
        this.disabled = disabledCharToBoolean(theCard.getIsDisabledRaw());
        this.owner = theCard.getOwner().getOwner();
        this.contactInfo = convertContactListToRestContactList(theCard.getOwner().getContacts());
    }

    //TODO: refactor this code
    public Card convertRestCardToCard(CardTypeRepository cardTypeRepository, OwnerRepository ownerRepository, ContactRepository contactRepository) {

        Card theCard = new Card();

        theCard.setCardNumber(this.cardNumber);
        theCard.setValidThru(this.validThru);
        theCard.setIsDisabledRaw('N');
        createCardType(cardTypeRepository, theCard);
        createOwner(ownerRepository, theCard);
        theCard.getOwner().setContacts(convertRestContactListToContactList(contactInfo, theCard.getOwner().getOwnerId(), contactRepository));
        //TODO: add hash calculation

        return theCard;
    }

    private void createOwner(OwnerRepository ownerRepository, Card theCard) {
        //create owner and inject into card object
        Owner theOwner = new Owner();
        //use a matching owner if found, or create a new if it does not exist
        Optional<Owner> matchingOwner = ownerRepository.findByOwner(this.owner);
        if (matchingOwner.isPresent()) {
            theOwner = (matchingOwner.get());
        }
        else {
            theOwner.setOwner(this.owner);
        }
        theCard.setOwner(theOwner);
    }

    private void createCardType(CardTypeRepository cardTypeRepository, Card theCard) {
        //create card type and inject into card object
        CardType theCardType = new CardType();
        //use a matching card type if found, or create a new if it does not exist
        Optional<CardType> matchingCardType = cardTypeRepository.findByCardType(this.cardType);
        if (matchingCardType.isPresent()) {
            theCardType = (matchingCardType.get());
        }
        else {
            theCardType.setCardType(this.cardType);
        }
        theCard.setCardType(theCardType);
    }

    private List<RestContact> convertContactListToRestContactList(List<Contact> contactList)
    {
        List<RestContact> result = new ArrayList<>();

        for (Contact contact : contactList)
        {
            RestContact currentRestContact = new RestContact();
            currentRestContact.convertContactToRestContact(contact);
            result.add(currentRestContact);
        }

        return result;
    }

    private List<Contact> convertRestContactListToContactList(List<RestContact> restContactList, int ownerId, ContactRepository contactRepository)
    {
        List<Contact> result = new ArrayList<>();

        for (RestContact restContact : restContactList)
        {
            Contact currentContact = new Contact();
            Optional<Contact> matchingContact = contactRepository.findByContact(restContact.getContact());

            if (matchingContact.isPresent()) {
                currentContact = matchingContact.get();
            }
            else {
                currentContact.setOwnerId(ownerId);
                currentContact.setContactType(restContact.getType());
                currentContact.setContact(restContact.getContact());
            }

            result.add(currentContact);
        }

        return result;
    }

    private boolean disabledCharToBoolean (char disabledChar)
    {
        boolean result = false;
        if (disabledChar == 'Y') {
            result = true;
        }
        return result;
    }
}
