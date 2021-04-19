package hu.robi.cardservice.sessionservice;

import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;

import java.util.List;

public interface CardSessionService {

    public List<Card> requestAllCards();

    public RestCard requestCard(String inputCardNumber);

    public void createCard(RestCard inputRestCard);

    public boolean validateCard(RestCard inputRestCard);
}
