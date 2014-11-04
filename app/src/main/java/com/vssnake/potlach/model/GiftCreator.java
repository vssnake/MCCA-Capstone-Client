package com.vssnake.potlach.model;

import java.util.Date;
import java.util.List;

/**
 * Created by vssnake on 04/11/2014.
 */
public class GiftCreator {

    private String mTitle;
    private String mDescription;
    private byte[] mImage;
    private long mViewCounts;
    private Date mCreationDate;
    private List<Long> mChainsID;

    public GiftCreator(){}

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

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        this.mImage = image;
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
