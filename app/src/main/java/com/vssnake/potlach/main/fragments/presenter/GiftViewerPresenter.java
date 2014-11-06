package com.vssnake.potlach.main.fragments.presenter;

import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vssnake on 05/11/2014.
 */
public class GiftViewerPresenter extends BasicPresenter{
    public GiftViewerPresenter(MainActivityPresenter mainPresenter){
        super(mainPresenter);
        this.mainActivityPresenter = mainPresenter;
    }

    FragmentGiftViewer mFragment;

    @Override
    public void attach(Fragment fragment) {
        mFragment = ((FragmentGiftViewer)fragment);
    }

    Gift mGift;

    public void showGift(Long id){
        mainActivityPresenter.getConnInterface().showGift("",id,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {



                loadGift(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void loadGift(Gift gift){
        mGift = gift;
        Picasso.with(mainActivityPresenter.getContext())
                .load(gift.getImageURL())
                .into(mFragment.mPhoto.getImage());
        mainActivityPresenter.getConnInterface().updateUser(null,new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if(user.getGiftLiked().contains(mGift.getId())){
                    mFragment.mPhoto.getmFirstOption().setImageResource(R.drawable.heart_icon_des_r);
                }else{
                    mFragment.mPhoto.getmFirstOption().setImageResource(R.drawable.heart_icon_des);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        mFragment.mTitle.setText(gift.getTitle());
        mFragment.mDescription.setText(gift.getDescription());
        mFragment.mUserData.setText(gift.getUserEmail() + "\n " + gift.getCreationDate());
        mFragment.mGiftChainCount.setText(gift.getChainsID().size() +"");

    }

    public void pushLike(){
        mainActivityPresenter.getConnInterface().modifyLike("",mGift.getId(),new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                loadGift(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
