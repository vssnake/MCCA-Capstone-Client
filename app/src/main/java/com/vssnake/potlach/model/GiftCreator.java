package com.vssnake.potlach.model;

import java.io.File;
import java.util.Date;
import java.util.List;

import retrofit.mime.TypedFile;

/**
 * Created by vssnake on 04/11/2014.
 */
public class GiftCreator {

    private String mTitle;
    private String mDescription;
    private TypedFile mImage;
    private TypedFile mImageThumb;
    private long mViewCounts;
    private Date mCreationDate;
    private String mUserEmail;

    public GiftCreator(){}

    public static GiftCreator creator(){return new GiftCreator();}

    public String getTitle() {
        return mTitle;
    }

    public GiftCreator setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }



    public TypedFile getImage() {
        return mImage;
    }



    public long getViewCounts() {
        return mViewCounts;
    }



    public Date getCreationDate() {
        return mCreationDate;
    }

    public GiftCreator setDescription(String mDescription) {
        this.mDescription = mDescription;
        return this;
    }

    public GiftCreator setCreationDate(Date mCreationDate) {
        this.mCreationDate = mCreationDate;
        return this;
    }
    public GiftCreator setUserEmail(String userEmail) {
        this.mUserEmail = userEmail;
        return this;
    }
    public GiftCreator setViewCounts(long mViewCounts) {
        this.mViewCounts = mViewCounts;
        return this;
    }
    public GiftCreator setImage(String imageUri) {
        this.mImage = new TypedFile("image/jpeg",new File(imageUri));
        return this;
    }
    public GiftCreator setImageThumb(String imageUri) {
        this.mImageThumb = new TypedFile("image/jpeg",new File(imageUri));
        return this;
    }

    public String getUserEmail() {
        return mUserEmail;
    }


    public TypedFile getImageThumb() {
        return mImageThumb;
    }


}
