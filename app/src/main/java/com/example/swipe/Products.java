package com.example.swipe;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Products {
    @SerializedName("image")
    String image;
    @BindingAdapter({"image"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load( url).placeholder(R.drawable.img).into(imageView);
    }


    @SerializedName("price")
    double price;

    @SerializedName("product_name")
    String productName;

    @SerializedName("product_type")
    String productType;

    @SerializedName("tax")
    double tax;
    @Override
    public String toString() {
        return "Products{" +
                "name='" + productName + '\'' +
                ", price=" + price +
                ", type=" + productType +
                // Add other fields if necessary
                '}';
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setPrice(double price) {
        this.price =price; // Convert double to int
    }
    public int getPrice() {
        return (int)price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getProductType() {
        return productType;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    public String getTax() {
          return  String.valueOf(tax);
    }
}
