package com.example.clavis.FavouriteRoomData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouriteData {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String product_name;
    private String imageView;
    private String price;
    private int count;
    private int amount;
    private String shop;
    private String category;

    public FavouriteData() {
    }

    public FavouriteData(String product_name, String imageView, String price, int count, int amount, String shop,String category) {
        this.product_name = product_name;
        this.imageView = imageView;
        this.price = price;
        this.count = count;
        this.amount = amount;
        this.shop = shop;
        this.category = category;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
