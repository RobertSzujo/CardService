package hu.robi.cardservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "CARD_TYPE")
public class CardType {

    //define fields
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARD_TYPE_SEQGEN")
    @SequenceGenerator(name = "CARD_TYPE_SEQGEN", sequenceName = "CARD_TYPE_SEQ", allocationSize = 1)
    @Column(name = "card_type_id")
    private int cardTypeId;

    @Column(name = "card_type")
    private String cardType;


    //define constructors

    public CardType() {

    }

    public CardType(int cardTypeId, String cardType) {
        this.cardTypeId = cardTypeId;
        this.cardType = cardType;
    }

    //define getter-setter

    public int getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(int cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    //define tostring

    @Override
    public String toString() {
        return "CardType{" +
                "cardTypeId=" + cardTypeId +
                ", cardType='" + cardType + '\'' +
                '}';
    }

}
