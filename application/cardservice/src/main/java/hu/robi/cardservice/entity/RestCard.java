package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Card entity and a REST-compatible form vice-versa.

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.robi.cardservice.dao.CardTypeRepository;
import hu.robi.cardservice.dao.ContactRepository;
import hu.robi.cardservice.dao.OwnerRepository;
import hu.robi.cardservice.service.EncryptService;
import hu.robi.cardservice.service.RestCardVerifyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RestCard {
    //define fields

    private String cardType;

    private String cardNumber;

    private String validThru;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cvv; //can only write

    private boolean disabled;

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

    public String getCvv() {
        return cvv;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner.toUpperCase();
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

        verifyRestCard();

        Card theCard = new Card();

        theCard.setCardNumber(this.cardNumber);
        theCard.setValidThru(this.validThru);
        theCard.setIsDisabledRaw('N');
        createCardType(cardTypeRepository, theCard);
        createOwner(ownerRepository, theCard);
        theCard.getOwner().setContacts(convertRestContactListToContactList(contactInfo, theCard.getOwner().getOwnerId(), contactRepository));
        theCard.setCardHash(createHash(this.cardNumber, this.validThru, this.cvv));

        return theCard;
    }

    private void createOwner(OwnerRepository ownerRepository, Card theCard) {
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
        CardType theCardType = new CardType();
        //match the card type with the one in the database, or throw error if not found
        Optional<CardType> matchingCardType = cardTypeRepository.findByCardType(this.cardType);
        if (matchingCardType.isPresent()) {
            theCardType = (matchingCardType.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A megadott kártyatípus nem szerepel az adatbázisban!");
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
            //use a matching contact if found, or create a new if it does not exist
            Optional<Contact> matchingContact = contactRepository.findByContact(restContact.getContact());

            if (matchingContact.isPresent()) {
                currentContact = matchingContact.get();
                currentContact.setOwnerId(ownerId);
            }
            else {
                currentContact.setOwnerId(ownerId);
                currentContact.setContactType(restContact.getType());
                currentContact.setContact(restContact.getContact());
            }

            result.add(currentContact);
        }

        //if the owner does not have any contacts, throw error
        if (result.size() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A kártyatulajdonosnak nincs elérhetősége!");
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

    private String createHash (String cardNumber, String validThru, String cvv)
    {
        EncryptService encryptService = new EncryptService();
        String result= encryptService.EncryptString(cardNumber+validThru+cvv);

        return result;
    }

    private void verifyRestCard()
    {
        RestCardVerifyService restCardVerifyService = new RestCardVerifyService(this);
        String result = restCardVerifyService.verifyCard();
        if (!result.equals("OK")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, result);
        }
    }

}
