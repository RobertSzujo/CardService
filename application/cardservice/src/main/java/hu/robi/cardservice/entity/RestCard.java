package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Card entity and a REST-compatible form vice-versa.

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.robi.cardservice.dao.CardRepository;
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
    private String cvv;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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

    //define methods

    public void verifyRestCard(CardTypeRepository cardTypeRepository, CardRepository cardRepository)
    {
        RestCardVerifyService restCardVerifyService = new RestCardVerifyService(this);
        String verificationResult = restCardVerifyService.verifyCard(cardTypeRepository, cardRepository);
        if (!verificationResult.equals("OK")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, verificationResult);
        }
    }

}
