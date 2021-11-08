package com.example.shophub;

public class user_details {
    private String name;
    private String emailid;
    private String phone;
    private String address;
    private String pincode;

    public user_details() {
    }

    public user_details(String name, String emailid, String phone, String address, String pincode) {
        this.name = name;
        this.emailid = emailid;
        this.phone = phone;
        this.address = address;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }
}
