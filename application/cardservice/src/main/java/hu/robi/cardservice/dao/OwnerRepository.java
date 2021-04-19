package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByOwner(String owner);

}
