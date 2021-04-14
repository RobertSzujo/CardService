package hu.robi.cardservice.rest;

import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.sessionservice.CardSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecards")
public class CardRestController {

    private final CardSessionService cardSessionService;

    public CardRestController(CardSessionService cardSessionService) {
        this.cardSessionService = cardSessionService;
    }

    //TODO: delete this feature later, it is only for debug!
    //expose "/ecards/getAll" and return list of cards
    @GetMapping("/getAll")
    public List<Card> requestAllCards() {
        return cardSessionService.requestAllCards();
    }

    //add mapping for GET /ecards/{cardNumber}
    @GetMapping("/{cardNumber}")
    public Card requestCard(@PathVariable String cardNumber) {
        Card theCard = cardSessionService.requestCard(cardNumber);
        return theCard;
    }

    //add mapping for POST /ecards - add new card
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Card createCard(@RequestBody Card theCard) {
        cardSessionService.createCard(theCard);
        return theCard;
    }
}
