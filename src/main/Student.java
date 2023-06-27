package main;

public class Student {

    int id;

    String promotion;

    boolean is_delegate;

    String firstname;

    String lastname;

    String mail;

    String phonenumber;

    int nbmiss;

    public Student() {
    }

    public Student(String promotion, boolean is_delegate, String firstname, String lastname, String mail, String phonenumber, int nbmiss) {
        super();
        this.promotion = promotion;
        this.is_delegate = is_delegate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.phonenumber = phonenumber;
        this.nbmiss = nbmiss;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public Boolean getIs_delegate() {
        return is_delegate;
    }

    public void setIs_delegate(Boolean is_delegate) {
        this.is_delegate = is_delegate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getNbmiss() {
        return nbmiss;
    }

    public void setNbmiss(int nbmiss) {
        this.nbmiss = nbmiss;
    }

}