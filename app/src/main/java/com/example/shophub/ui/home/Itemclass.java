package com.example.shophub.ui.home;

import android.graphics.drawable.Drawable;

public class Itemclass {
    private int item_images;
    private String item_namest;
    private String item_image;
    private String item_name;
    private String price;
    private String count;
    private boolean connection;
    public Itemclass() {
    }

    public Itemclass(int item_images, String item_namest) {
        this.item_images = item_images;
        this.item_namest = item_namest;
    }

    public Itemclass(String item_image, String item_name, String price) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.price = price;
    }

    public Itemclass(String count) {
        this.count = count;
    }

    public Itemclass(String item_image, String item_name, String price, String count) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.price = price;
        this.count = count;
    }

    public Itemclass(boolean connection) {
        this.connection = connection;
    }

    public int getItem_images() {
        return item_images;
    }

    public String getItem_namest() {
        return item_namest;
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

    public String getCount() {
        return count;
    }
}
