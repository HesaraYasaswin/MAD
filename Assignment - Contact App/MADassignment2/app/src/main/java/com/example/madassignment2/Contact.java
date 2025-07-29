package com.example.madassignment2;

import java.io.Serializable;
public class Contact implements Serializable {
    private long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String photoPath;

    public Contact() {
        // Default constructor
    }

    public Contact(String name, String phoneNumber, String email, String photoPath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photoPath = photoPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}

