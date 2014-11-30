package com.vssnake.potlach.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by unai on 24/10/2014.
 */
public class Gift {

    private Long id;
    private String title;
    private String description;
    private String imageURL;
    private String thumbnailURL;
    private long viewCounts;
    private Long creationDate;
    private List<Long> chainsID;
    private boolean obscene;
    private String userEmail;
    private Double latitude;
    private Double longitude;
    private Float precision;

    public Gift(){}

    /**
     * Only for Local Test
     */
    public Gift(Long id,String userEmail,String title,String description,String imageURL,
                String imageThumbnailURL){
        this.id = id;
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.thumbnailURL = imageThumbnailURL;
        this.viewCounts = 0;
        this.creationDate = new Date().getTime();
        this.chainsID = new ArrayList<Long>();
        this.obscene = false;
    }

    public Gift(Long id,GiftCreator giftCreator,String imageURL,
                String imageThumbnailURL){
        this.id = id;
        this.userEmail = giftCreator.getUserEmail();
        this.title = giftCreator.getTitle();
        this.description = giftCreator.getDescription();
        this.imageURL = imageURL;
        this.thumbnailURL = imageThumbnailURL;
        this.viewCounts = 0;
        this.creationDate = new Date().getTime();
        this.chainsID = new ArrayList<Long>();
        this.obscene = false;

        this.latitude = giftCreator.getLatitude();
        this.longitude = giftCreator.getLongitude();
        this.precision = giftCreator.getPrecision();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String mDescription) {
        this.description = mDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String mImageURL) {
        this.imageURL = mImageURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String mThumbnailURL) {
        this.thumbnailURL = mThumbnailURL;
    }

    public long getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(long mViewCounts) {
        this.viewCounts = mViewCounts;
    }

    public Date getCreationDate() {
        return new Date(creationDate);
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate.getTime();
    }

    public List<Long> getChainsID() {
        return chainsID;
    }

    /**
     *
     * @param mChainID the new chain to add
     * @return true if a chain is added
     */
    public boolean addNewChain(Long mChainID) {
        return chainsID.add(mChainID);
    }

    public Long getId() {
        return id;
    }
    public long incrementDecrementLike(boolean increment){
        return increment ? viewCounts++: viewCounts--;
    }
    public void setObscene(boolean obscene){
        this.obscene = obscene;
    }

    public boolean getObscene(){
        return obscene;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double mLatitude) {
        this.latitude = mLatitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double mLongitude) {
        this.longitude = mLongitude;
    }

    public Float getPrecision() {
        return precision;
    }

    public void setPrecision(Float mPrecision) {
        this.precision = mPrecision;
    }
}