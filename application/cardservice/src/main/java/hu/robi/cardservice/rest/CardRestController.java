package hu.robi.cardservice.rest;

import hu.robi.cardservice.dao.CardDAO;
import hu.robi.cardservice.entity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardRestController {

    private CardDAO cardDAO;

    //inject card DAO (refactor later)
    @Autowired
    public CardRestController(CardDAO theCardDAO) {
        cardDAO = theCardDAO;
    }

    //expose "/ecards" and return list of cards
    @GetMapping("/ecards")
    public List<Card> requestAllCards()
    {
        return cardDAO.requestAllCards();
    }
}
