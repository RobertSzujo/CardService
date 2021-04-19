package hu.robi.cardservice.dao;

import hu.robi.cardservice.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Optional<Contact> findByContact(String contact);

}
