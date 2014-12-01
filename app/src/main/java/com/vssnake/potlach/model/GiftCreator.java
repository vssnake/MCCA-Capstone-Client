package com.vssnake.potlach.model;

import java.io.File;
import java.util.Date;
import java.util.List;

import retrofit.mime.TypedFile;

/**
 * Created by vssnake on 04/11/2014.
 */
public class GiftCreator {

    private String title;
    private String description;
    private TypedFile image;
    private TypedFile imageThumb;
    private long viewCounts;
    private String userEmail;
    private Long chainID;
    private Double latitude;
    private Double longitude;
    private Float precision;

    public GiftCreator(){
        latitude = new Double(0);
        longitude = new Double(0);
    }

    public static GiftCreator creator(){return new GiftCreator();}

    public String getTitle() {
        return title;
    }

    public GiftCreator setTitle(String mTitle) {
        this.title = mTitle;
        return this;
    }

    public String getDescription() {
        return description;
    }



    public TypedFile getImage() {
        return image;
    }



    public long getViewCounts() {
        return viewCounts;
    }





    public GiftCreator setDescription(String description) {
        this.description = description;
        return this;
    }


    public GiftCreator setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }
    public GiftCreator setViewCounts(long viewCounts) {
        this.viewCounts = viewCounts;
        return this;
    }
    public GiftCreator setImage(String imageUri) {
        this.image = new TypedFile("image/jpeg",new File(imageUri));
        return this;
    }

    public GiftCreator setImage(TypedFile typeFile){
        this.image = typeFile;
        return this;
    }
    public GiftCreator setImageThumb(String imageUri) {
        this.imageThumb = new TypedFile("image/jpeg",new File(imageUri));
        return this;
    }
    public GiftCreator setImageThumb(TypedFile typeFile){
        this.imageThumb = typeFile;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public TypedFile getImageThumb() {
        return imageThumb;
    }


    public Long getChainID() {
        if (chainID == null){
            return -1l;
        }
        return chainID;
    }

    public GiftCreator setChainID(Long chainID) {
        this.chainID = chainID;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public GiftCreator setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public GiftCreator setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getPrecision() {
        return precision;
    }

    public GiftCreator setPrecision(Float precision) {
        this.precision =precision;
        return this;
    }
}
