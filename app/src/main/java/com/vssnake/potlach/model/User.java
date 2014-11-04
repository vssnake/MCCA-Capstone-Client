package com.vssnake.potlach.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vssnake on 24/10/2014.
 */
public class User {

    private String mEmail;
    private String mName;
    private String mToken;
    private Date mExpirationDate;

    private List<Long> giftPosted;
    private List<Long> giftLiked;

    public User(){}

    public User(String email,String name,String token, Date expiration){
        this.mEmail = email;
        mName = name;
        mToken = token;
        mExpirationDate = expiration;

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
        return giftLiked.equals(giftID);
    }
    public boolean removeLike(Long giftID){
       if (giftLiked.equals(giftID)){
           giftLiked.remove(giftID);
           return true;
        }
        return false;
    }
    public boolean addLike(Long giftID){
        if (!giftLiked.equals(giftID)){
            giftLiked.add(giftID);
            return true;
        }
        return false;
    }
}
