package com.vssnake.potlach.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vssnake on 24/10/2014.
 */
public class User {


    private String mUrlPhoto;
    private String mEmail;
    private String mName;
    private String mToken;
    private Date mExpirationDate;

    private List<Long> giftPosted;
    private List<Long> giftLiked;

    private boolean mShowInappropriate;

    public User(){}

    public User(String email,String name, Boolean inappropriate,String token, Date expiration,
                String urlPhoto){
        this.mEmail = email;
        mName = name;
        mToken = token;
        mExpirationDate = expiration;
        mUrlPhoto = urlPhoto;
        mShowInappropriate = inappropriate;

        giftPosted = new ArrayList<Long>();
        giftLiked = new ArrayList<Long>();
    }

    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

    public String getToken() {
        return mToken;
    }

    public Date getExpirationDate() {
        return mExpirationDate;
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
        return mUrlPhoto;
    }

    public boolean isShowInappropriate() {
        return mShowInappropriate;
    }
}
