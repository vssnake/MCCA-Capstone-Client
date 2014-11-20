package com.vssnake.potlach.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by unai on 24/10/2014.
 */
public class Gift {

    private Long mID;
    private String mTitle;
    private String mDescription;
    private String mImageURL;
    private String mThumbnailURL;
    private long mViewCounts;
    private Date mCreationDate;
    private List<Long> mChainsID;
    private boolean mObscene;
    private String mUserEmail;
    private Double mLatitude;
    private Double mLongitude;
    private Float mPrecision;

    public Gift(){}

    /**
     * Only for Local Test
     */
    public Gift(Long id,String userEmail,String title,String description,String imageURL,
                String imageThumbnailURL){
        mID = id;
        mUserEmail = userEmail;
        mTitle = title;
        mDescription = description;
        mImageURL = imageURL;
        mThumbnailURL = imageThumbnailURL;
        mViewCounts = 0;
        mCreationDate = new Date();
        mChainsID = new ArrayList<Long>();
        mObscene = false;
    }

    public Gift(Long id,GiftCreator giftCreator,String imageURL,
                String imageThumbnailURL){
        mID = id;
        mUserEmail = giftCreator.getUserEmail();
        mTitle = giftCreator.getTitle();
        mDescription = giftCreator.getDescription();
        mImageURL = imageURL;
        mThumbnailURL = imageThumbnailURL;
        mViewCounts = 0;
        mCreationDate = new Date();
        mChainsID = new ArrayList<Long>();
        mObscene = false;

        mLatitude = giftCreator.getLatitude();
        mLongitude = giftCreator.getLongitude();
        mPrecision = giftCreator.getPrecision();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setThumbnailURL(String mThumbnailURL) {
        this.mThumbnailURL = mThumbnailURL;
    }

    public long getViewCounts() {
        return mViewCounts;
    }

    public void setViewCounts(long mViewCounts) {
        this.mViewCounts = mViewCounts;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public List<Long> getChainsID() {
        return mChainsID;
    }

    /**
     *
     * @param mChainID the new chain to add
     * @return true if a chain is added
     */
    public boolean addNewChain(Long mChainID) {
       return mChainsID.add(mChainID);
    }

    public Long getId() {
        return mID;
    }
    public long incrementDecrementLike(boolean increment){
        return increment ? mViewCounts++: mViewCounts--;
    }
    public void setObscene(boolean obscene){
        mObscene = obscene;
    }

    public boolean getObscene(){
        return mObscene;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Float getPrecision() {
        return mPrecision;
    }

    public void setPrecision(Float mPrecision) {
        this.mPrecision = mPrecision;
    }
}
