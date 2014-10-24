package com.vssnake.potlach.model;

import java.util.Date;
import java.util.List;

/**
 * Created by unai on 24/10/2014.
 */
public class Gift {

    private String mTitle;
    private String mDescription;
    private String mImageURL;
    private String mThumbnailURL;
    private long mViewCounts;
    private Date mCreationDate;
    private List<Long> mChainsID;

    public Gift(){}

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
}
