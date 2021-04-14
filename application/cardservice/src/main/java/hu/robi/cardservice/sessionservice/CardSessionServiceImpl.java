package hu.robi.cardservice.sessionservice;

import hu.robi.cardservice.dao.CardDAO;
import hu.robi.cardservice.entity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardSessionServiceImpl implements CardSessionService {

    private CardDAO cardDAO;

    @Autowired
    public CardSessionServiceImpl (CardDAO theCardDAO) {
        cardDAO = theCardDAO;
    }

    @Override
    @Transactional
    public List<Card> requestAllCards() {
        return cardDAO.requestAllCards();
    }

    @Override
    @Transactional
    public Card requestCard(String theCardNumber) {
        return cardDAO.requestCard(theCardNumber);
    }

    @Override
    @Transactional
    public void createCard(Card theCard) {
        cardDAO.createCard(theCard);
    }
}
