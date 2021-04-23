package hu.robi.cardservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "CONTACT")
public class Contact {

    //define fields
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_SEQGEN")
    @SequenceGenerator(name = "CONTACT_SEQGEN", sequenceName = "CONTACT_SEQ", allocationSize = 1)
    @Column(name = "contact_id")
    private int contactId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "contact")
    private String contact;


    //define constructors
    public Contact() {

    }

    public Contact(int contactId, Owner owner, String contactType, String contact) {
        this.contactId = contactId;
        this.owner = owner;
        this.contactType = contactType;
        this.contact = contact;
    }

    //define getter-setter

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    //define tostring

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", owner=" + owner +
                ", contactType='" + contactType + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
