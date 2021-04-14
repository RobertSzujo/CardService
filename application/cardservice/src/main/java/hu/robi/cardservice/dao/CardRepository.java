package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {

}
