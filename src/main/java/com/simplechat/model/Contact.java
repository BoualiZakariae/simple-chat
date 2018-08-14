package com.simplechat.model;

import java.util.UUID;

/**
 * @author Mohsen Jahanshahi
 */
public class Contact {

    private String mobile;
    private String fname;
    private String lname;
    private UUID userId;


    public Contact(String mobile, String fname, String lname) {
        this.mobile = mobile;
        this.fname = fname;
        this.lname = lname;
    }

    public Contact(String mobile, String fname, String lname, UUID userId) {
        this.mobile = mobile;
        this.fname = fname;
        this.lname = lname;
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
