package hu.robi.cardservice.rest;

import hu.robi.cardservice.entity.Card;
import hu.robi.cardservice.entity.RestCard;
import hu.robi.cardservice.service.CardSessionService;
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
    public RestCard requestCard(@PathVariable String cardNumber) {
        RestCard theCard = cardSessionService.requestCard(cardNumber);
        return theCard;
    }

    //add mapping for POST /ecards - add new card
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void createCard(@RequestBody RestCard theRestCard) {
        cardSessionService.createCard(theRestCard);
    }

    //add mapping for POST /ecards/validate - validate card
    @PostMapping("/validate")
    public String validateCard(@RequestBody RestCard theRestCard) {
        String result = cardSessionService.validateCard(theRestCard);
        return result;
    }

    //add mapping for PUT /ecards/{cardNumber}
    @PutMapping("/{cardNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void disableCard(@PathVariable String cardNumber) {
        cardSessionService.disableCard(cardNumber);
    }
}
