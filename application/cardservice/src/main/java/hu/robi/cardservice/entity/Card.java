package hu.robi.cardservice.entity;

import javax.persistence.*;

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
    private String cardHash;

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

    public Card(String cardNumber, String validThru, String cardHash, char isDisabledRaw) {
        this.cardNumber = cardNumber;
        this.validThru = validThru;
        this.cardHash = cardHash;
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

    public String getCardHash() {
        return cardHash;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
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
                ", card_hash='" + cardHash + '\'' +
                ", isDisabledRaw=" + isDisabledRaw +
                ", owner=" + owner +
                ", cardType=" + cardType +
                '}';

    }
}
