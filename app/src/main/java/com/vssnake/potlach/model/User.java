package com.vssnake.potlach.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vssnake on 24/10/2014.
 */
public class User {


    private String urlPhoto;
    private String email;
    private String name;
    private String token;
    private Date expirationDate;

    private List<Long> giftPosted;
    private List<Long> giftLiked;

    private Boolean hideInappropriate;

    public User(){}

    public User(String email,String name, Boolean hideInappropriate,String token, Date expiration,
                String urlPhoto){
        this.email = email;
        this.name = name;
        this.token = token;
        this.expirationDate = expiration;
        this.urlPhoto = urlPhoto;
        this.hideInappropriate = hideInappropriate;

        giftPosted = new ArrayList<Long>();
        giftLiked = new ArrayList<Long>();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public List<Long> getGiftPosted() {
        return giftPosted;
    }

    public boolean addGift(Long id){
        return giftPosted.add(id);
    }
    public boolean removeGift(Long giftID){
        return giftPosted.remove(giftID);
    }

    public boolean giftExist(Long giftID){
        return giftPosted.equals(giftID);
    }
    public boolean giftLikeExist(Long giftID){
        return getGiftLiked().contains(giftID);
    }
    public boolean removeLike(Long giftID){
        if (getGiftLiked().contains(giftID)){
            getGiftLiked().remove(giftID);
            return true;
        }
        return false;
    }
    public boolean addLike(Long giftID){
        if (!getGiftLiked().contains(giftID)){
            getGiftLiked().add(giftID);
            return true;
        }
        return false;
    }

    public List<Long> getGiftLiked() {
        return giftLiked;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public Boolean hideInappropriate() {
        return hideInappropriate;
    }

    public void setHideInappropriate(boolean hideInappropriate) {
        this.hideInappropriate = hideInappropriate;
    }

    public void setToken(String token){
        this.token = token;
    }
}