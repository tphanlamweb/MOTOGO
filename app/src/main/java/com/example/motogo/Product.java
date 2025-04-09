package com.example.motogo;

public class Product {
    private int ImageResID;
    private String name;
    private String description, MotoID, Status;
    private int price;


    public Product(int ImageResID, String name, String description, int price, String MotoID , String Status) {
        this.ImageResID = ImageResID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.MotoID = MotoID;
        this.Status = Status;
    }

    public int getImageResID() {
        return ImageResID;
    }

    public void setImageResID(int imageResID) {
        ImageResID = imageResID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMotoID() {
        return MotoID;
    }

    public void setMotoID(String motoID) {
        MotoID = motoID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageResId() { return ImageResID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
}