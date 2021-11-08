package com.example.shophub.ui.Order;

public class Order_class {
    private String item_image;
    private String item_name;
    private String price;
    private String address;
    private String phone;
    private String name;
    private String count;
    private String pincode;
    public Order_class() {
    }

    public Order_class(String item_image, String item_name, String price, String address, String phone, String name, String count, String pincode) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.price = price;
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.count = count;
        this.pincode = pincode;
    }

    public String getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getPincode() {
        return pincode;
    }
}
