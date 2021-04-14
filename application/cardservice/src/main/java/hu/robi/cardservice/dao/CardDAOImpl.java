package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CardDAOImpl implements CardDAO {

    //define field for entitymanager

    private EntityManager entityManager;

    //set up constructor injection
    @Autowired
    public CardDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    //TODO: delete this function, it is only for debug!
    @Override
    public List<Card> requestAllCards() {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //create a query
        Query<Card> theQuery = currentSession.createQuery("from Card", Card.class);

        //execute query and get result list
        List<Card> cards = theQuery.getResultList();

        //return the results
        return cards;
    }

    @Override
    public Card requestCard(String theCardNumber) {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //get the card data
        Card theCard = currentSession.get(Card.class, theCardNumber);

        //return card data
        return theCard;
    }

    @Override
    public void createCard(Card theCard) {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //save card
        currentSession.save(theCard);

    }
}
