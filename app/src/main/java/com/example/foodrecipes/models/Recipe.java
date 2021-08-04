package com.example.foodrecipes.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class Recipe implements Parcelable {
    private String imageUrl;
    private float socialUrl;
    private String publisher;
    private String id;
    private String title;
    private String[] ingredients;


    public Recipe(String imageUrl, float socialUrl, String publisher, String id, String title, String[] ingredients) {
        this.imageUrl = imageUrl;
        this.socialUrl = socialUrl;
        this.publisher = publisher;
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        imageUrl = in.readString();
        socialUrl = in.readFloat();
        publisher = in.readString();
        id = in.readString();
        title = in.readString();
        ingredients = in.createStringArray();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getSocialUrl() {
        return socialUrl;
    }

    public void setSocialUrl(float socialUrl) {
        this.socialUrl = socialUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "imageUrl='" + imageUrl + '\'' +
                ", socialUrl=" + socialUrl +
                ", publisher='" + publisher + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeFloat(socialUrl);
        dest.writeString(publisher);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringArray(ingredients);
    }
}
