package hu.robi.cardservice.rest;

import hu.robi.cardservice.entity.RestCard;
import hu.robi.cardservice.service.CardSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ecards")
public class CardRestController {

    Logger logger = LoggerFactory.getLogger(CardRestController.class);

    private final CardSessionService cardSessionService;

    public CardRestController(CardSessionService cardSessionService) {
        this.cardSessionService = cardSessionService;
    }

    //add mapping for GET /ecards/{cardNumber}
    @GetMapping("/{cardNumber}")
    public RestCard requestCard(@PathVariable String cardNumber) {
        logger.info("Kártya lekérdezése: " + cardNumber);
        RestCard theCard = cardSessionService.requestCard(cardNumber);

        logger.info("Kártya lekérdezése sikeres: " + cardNumber);
        return theCard;
    }

    //add mapping for POST /ecards - add new card
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void createCard(@RequestBody RestCard theRestCard) {
        logger.info("Kártya létrehozása: " + theRestCard.getCardNumber());
        cardSessionService.createCard(theRestCard);

        logger.info("Kártya létrehozása sikeres: " + theRestCard.getCardNumber());
    }

    //add mapping for POST /ecards/validate - validate card
    @PostMapping("/validate")
    public String validateCard(@RequestBody RestCard theRestCard) {
        logger.info("Kártya ellenőrzése: " + theRestCard.getCardNumber());
        String result = cardSessionService.validateCard(theRestCard);

        logger.info("Kártya ellenőrzése megtörtént: " + theRestCard.getCardNumber() + " - Eredmény: " + result);
        return result;
    }

    //add mapping for PUT /ecards/{cardNumber}
    @PutMapping("/{cardNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void disableCard(@PathVariable String cardNumber) {
        logger.info("Kártya letiltása: " + cardNumber);
        cardSessionService.disableCard(cardNumber);

        logger.info("Kártya letiltása sikeres: " + cardNumber);
    }
}
