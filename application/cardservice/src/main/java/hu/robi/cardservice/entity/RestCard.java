package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Card entity and a REST-compatible form vice-versa.

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class RestCard {
    //define fields

    private String cardType;

    private String cardNumber;

    private String validThru;

    @JsonProperty(value = "CVV", access = JsonProperty.Access.WRITE_ONLY)
    private String cvv;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean disabled;

    private String owner;

    private List<RestContact> contactInfo;

    //define getters/setters

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType.toUpperCase();
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

}
