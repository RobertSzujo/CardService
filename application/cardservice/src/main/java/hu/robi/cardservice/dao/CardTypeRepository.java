package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
    Optional<CardType> findByCardType(String cardType);
}
