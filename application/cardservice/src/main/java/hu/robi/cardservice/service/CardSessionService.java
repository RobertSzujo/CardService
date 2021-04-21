package hu.robi.cardservice.service;

import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;

import java.util.List;

public interface CardSessionService {

    public List<Card> requestAllCards();

    public RestCard requestCard(String inputCardNumber);

    public void createCard(RestCard inputRestCard);

    public String validateCard(RestCard inputRestCard);

    public void disableCard(String cardNumber);
}
