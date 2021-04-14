package hu.robi.cardservice.sessionservice;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.entity.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardSessionServiceImpl implements CardSessionService {

    private final CardRepository cardRepository;

    public CardSessionServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> requestAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card requestCard(String theCardNumber) {
        return cardRepository.getOne(theCardNumber);
    }

    @Override
    public void createCard(Card theCard) {
        cardRepository.save(theCard);
    }
}
