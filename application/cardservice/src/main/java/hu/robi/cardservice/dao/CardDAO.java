package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Card;

import java.util.List;

public interface CardDAO {

    public List<Card> requestAllCards();
}
