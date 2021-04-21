package hu.robi.cardservice.entity;

//This entity converts data between the database-linked Contact entity and a REST-compatible form vice-versa.

public class RestContact {

    //define fields

    private String type;

    private String contact;

    //define constructor

    public RestContact() {

    }

    //define getter/setter

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact.toUpperCase();
    }
}
