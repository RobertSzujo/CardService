package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Card;

import java.util.List;

public interface CardDAO {

    public List<Card> requestAllCards();

    public Card requestCard(String theCardNumber);

    public void createCard(Card theCard);
}
