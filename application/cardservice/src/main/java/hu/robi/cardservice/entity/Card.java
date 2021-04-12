package hu.robi.cardservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cards")
public class Card {

    //define fields
    @Id
    @Column(name="card_number")
    private String cardNumber;

    @Column(name="valid_thru")
    private String validThru;

    @Column(name="card_hash")
    private String card_hash;

    @Column(name="disabled")
    private char isDisabledRaw;

    //TODO: define owner, type, contact info

    //define constructors
    public Card() {

    }

    public Card(String cardNumber, String validThru, String card_hash, char isDisabledRaw) {
        this.cardNumber = cardNumber;
        this.validThru = validThru;
        this.card_hash = card_hash;
        this.isDisabledRaw = isDisabledRaw;
    }

    //define getter-setter

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

    public String getCard_hash() {
        return card_hash;
    }

    public void setCard_hash(String card_hash) {
        this.card_hash = card_hash;
    }

    public char getIsDisabledRaw() {
        return isDisabledRaw;
    }

    public void setIsDisabledRaw(char isDisabledRaw) {
        this.isDisabledRaw = isDisabledRaw;
    }

    //define tostring


    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", validThru='" + validThru + '\'' +
                ", card_hash='" + card_hash + '\'' +
                ", isDisabledRaw=" + isDisabledRaw +
                '}';
    }
}
