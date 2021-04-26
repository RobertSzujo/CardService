package hu.robi.cardservice.service;

import hu.robi.cardservice.entity.RestCard;

public interface CardSessionService {

    RestCard requestCard(String inputCardNumber);

    void createCard(RestCard inputRestCard);

    String validateCard(RestCard inputRestCard);

    void disableCard(String cardNumber);
}
