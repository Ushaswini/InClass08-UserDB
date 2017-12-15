package edu.uncc.inclass08;

/**
 * Created by sunand on 11/30/17.
 */

public class User {
    String firstName, lastName;
    String email, ipAddress, gender;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User(){

    }


    public User(String firstName, String lastName, String gender, String ipAddress){
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.ipAddress = ipAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
