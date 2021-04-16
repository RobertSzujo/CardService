package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Contact entity and a REST-compatible form vice-versa.

import java.util.List;

public class RestContact {

    //define fields

    private String type;

    private String contact;

    //define constructor

    public RestContact(Contact theContact) {
        this.type = theContact.getContactType();
        this.contact = theContact.getContact();
    }

    //define getter/setter

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
