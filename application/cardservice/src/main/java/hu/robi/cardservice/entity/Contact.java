package hu.robi.cardservice.entity;

import javax.persistence.*;

@Entity
@Table(name="CONTACT")
public class Contact {

    //define fields
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="contact_id")
    private int contactId;

    @Column(name="owner_id")
    private int ownerId;

    @Column(name="contact_type")
    private String contactType;

    @Column(name="contact")
    private String contact;


    //define constructors
    public Contact() {

    }

    public Contact(int contactId, int ownerId, String contactType, String contact) {
        this.contactId = contactId;
        this.ownerId = ownerId;
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

    public int getOwner() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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
                ", ownerId=" + ownerId +
                ", contactType='" + contactType + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
