package hu.robi.cardservice.sessionservice;

import hu.robi.cardservice.dao.CardRepository;
import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
    public RestCard requestCard(String theCardNumber) {
        Optional<Card> result = cardRepository.findById(theCardNumber);
        RestCard theCard = null;
        if (result.isPresent()) {
            theCard = new RestCard(result.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return theCard;
    }

    @Override
    public void createCard(Card theCard) {
        cardRepository.save(theCard);
    }
}
