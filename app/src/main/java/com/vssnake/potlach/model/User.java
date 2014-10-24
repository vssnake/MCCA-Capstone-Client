package com.vssnake.potlach.model;

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

    public User(){}

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
}
