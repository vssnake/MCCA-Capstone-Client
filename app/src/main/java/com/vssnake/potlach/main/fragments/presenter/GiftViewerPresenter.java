package com.vssnake.potlach.main.fragments.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.User;

import java.util.Locale;

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
    public void userDataClicked(){
        getMainPresenter().getFragmentManager().showUser(mGift.getUserEmail());
    }

    public void chainClicked(){
        getMainPresenter().getFragmentManager().showGiftChain(mGift.getId());
    }

    public void launchGoogleMaps(){
        if (mGift.getLongitude() != null && mGift.getLatitude() != null){
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mGift.getLatitude(),
                    mGift.getLongitude());
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getMainPresenter().getContext().startActivity(intent);
        }

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
        if (mGift.getLongitude() == null && mGift.getLatitude() == null){
            ((TextView)mFragment.mGpsPosition.getChildAt(0)).setText(R.string.no_location);
        }
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
