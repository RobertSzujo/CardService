package hu.robi.cardservice.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="OWNER")
public class Owner {

    //define fields
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="owner_id")
    private int ownerId;

    @Column(name="owner")
    private String Owner;

    @OneToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private List<Contact> contacts;

    //define constructors

    public Owner() {

    }

    public Owner(int ownerId, String owner) {
        this.ownerId = ownerId;
        Owner = owner;
    }

    //define getter-setter

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
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
                ", Owner='" + Owner + '\'' +
                ", contacts=" + contacts +
                '}';
    }

}
