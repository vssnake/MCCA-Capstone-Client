package com.vssnake.potlach.main.fragments.presenter;

import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
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
        mainActivityPresenter.getConnInterface().showGift(id,new ConnectionManager.ReturnGiftHandler() {
            @Override
            public void onReturnHandler(Gift gift) {
                loadGift(gift);
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void loadGift(final Gift gift){
        mGift = gift;
        Picasso.with(mainActivityPresenter.getContext())
                .load(gift.getImageURL())
                .into(mFragment.mPhoto.getImage());
        if (gift.getObscene()){
            mFragment.mPhoto.getSecondOption().setImageResource(R.drawable.deny_a);
        }else{
            mFragment.mPhoto.getSecondOption().setImageResource(R.drawable.deny);
        }
        mainActivityPresenter.getConnInterface().returnUserLogged(new ConnectionManager.ReturnUserHandler() {
            @Override
            public void onReturnUser(User user) {
                if(user.getGiftLiked().contains(mGift.getId())){
                    mFragment.mPhoto.getFirstOption().setImageResource(R.drawable.heart_icon_des_r);
                }else{
                    mFragment.mPhoto.getFirstOption().setImageResource(R.drawable.heart_icon_des);
                }
                mFragment.mPhoto.setSecondOptionCounts(gift.getViewCounts());
            }

            @Override
            public void onError(String error) {

            }
        });
        mFragment.mTitle.setText(gift.getTitle());
        mFragment.mDescription.setText(gift.getDescription());
        mFragment.mUserData.setText(gift.getUserEmail() + "\n " + gift.getCreationDate());
        mFragment.mGiftChainCount.setText(gift.getChainsID().size() +"");

    }

    public void pushLike(){
        mainActivityPresenter.getConnInterface().modifyLike(mGift.getId(),new ConnectionManager.ReturnGiftHandler() {
            @Override
            public void onReturnHandler(Gift gift) {
                loadGift(gift);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void pushObscene(){

        mainActivityPresenter.getConnInterface().setObscene(mGift.getId(),new ConnectionManager.ReturnGiftHandler() {
            @Override
            public void onReturnHandler(Gift gift) {
                loadGift(gift);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
