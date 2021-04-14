package hu.robi.cardservice.sessionservice;

import hu.robi.cardservice.entity.Card;

import java.util.List;

public interface CardSessionService {

    public List<Card> requestAllCards();

    public Card requestCard(String theCardNumber);

    public void createCard(Card theCard);
}
