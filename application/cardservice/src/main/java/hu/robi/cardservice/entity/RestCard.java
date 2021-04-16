package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Card entity and a REST-compatible form vice-versa.

import java.util.ArrayList;
import java.util.List;

public class RestCard {
    //define fields

    private String cardType;

    private String cardNumber;

    private String validThru;

    private String CVV; //can only set

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

    public void setCVV(String CVV) {
        this.CVV = CVV;
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

    private boolean disabledCharToBoolean (char disabledChar)
    {
        boolean result = false;
        if (disabledChar == 'Y') {
            result = true;
        }
        return result;
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

    private List<Contact> convertRestContactListToContactList(List<RestContact> restContactList, int ownerId)
    {
        List<Contact> result = new ArrayList<>();

        for (RestContact contact : restContactList)
        {
            Contact currentContact = new Contact();
            currentContact.setOwnerId(ownerId);
            currentContact.setContactType(contact.getType());
            currentContact.setContact(contact.getContact());
            result.add(currentContact);
        }

        return result;
    }

    public void convertCardToRestCard(Card theCard) {
        this.cardType = theCard.getCardType().getCardType();
        this.cardNumber = theCard.getCardNumber();
        this.validThru = theCard.getValidThru();
        this.disabled = disabledCharToBoolean(theCard.getIsDisabledRaw());
        this.owner = theCard.getOwner().getOwner();
        this.contactInfo = convertContactListToRestContactList(theCard.getOwner().getContacts());
    }

    //TODO: refactor this code and fix problems (see below)
    public Card convertRestCardToCard() {
        Card theCard = new Card();

        //create card type and inject into card object
        CardType theCardType = new CardType();
        theCardType.setCardType(this.cardType); //TODO: If card type exists, use its row instead of creating new
        theCard.setCardType(theCardType);

        theCard.setCardNumber(this.cardNumber);
        theCard.setValidThru(this.validThru);

        //create owner and inject into card object
        Owner theOwner = new Owner();
        theOwner.setOwner(this.owner);
        theCard.setOwner(theOwner); //TODO: If owner exists, use its row instead of creating new

        //create list of contacts and inject into card object
        theCard.getOwner().setContacts(convertRestContactListToContactList(contactInfo, theCard.getOwner().getOwnerId()));
        theCard.setIsDisabledRaw('N');

        //TODO: add hash calculation
        return theCard;
    }
}
