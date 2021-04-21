package hu.robi.cardservice.service;

import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;

import java.util.List;

public interface CardSessionService {

    List<Card> requestAllCards();

    RestCard requestCard(String inputCardNumber);

    void createCard(RestCard inputRestCard);

    String validateCard(RestCard inputRestCard);

    void disableCard(String cardNumber);
}
