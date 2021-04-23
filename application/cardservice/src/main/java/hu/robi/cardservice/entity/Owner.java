package hu.robi.cardservice.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OWNER")
public class Owner {

    //define fields
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OWNER_SEQGEN")
    @SequenceGenerator(name = "OWNER_SEQGEN", sequenceName = "OWNER_SEQ", allocationSize = 1)
    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "owner")
    private String owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="owner")
    private List<Contact> contacts;

    //define constructors

    public Owner() {

    }

    public Owner(int ownerId, String owner) {
        this.ownerId = ownerId;
        this.owner = owner;
    }

    //define getter-setter

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    //define tostring

    @Override
    public String toString() {
        return "Owner{" +
                "ownerId=" + ownerId +
                ", Owner='" + owner + '\'' +
                ", contacts=" + contacts +
                '}';
    }

}
