package hu.robi.cardservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CARD")
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

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private Owner owner;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="card_type_id")
    private CardType cardType;

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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    //define tostring

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", validThru='" + validThru + '\'' +
                ", card_hash='" + card_hash + '\'' +
                ", isDisabledRaw=" + isDisabledRaw +
                ", owner=" + owner +
                ", cardType=" + cardType +
                '}';

    }
}
