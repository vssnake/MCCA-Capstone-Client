package com.vssnake.potlach.main.fragments.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.User;

import java.lang.ref.WeakReference;
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

    WeakReference<FragmentGiftViewer> mFragment;

    @Override
    public void attach(Fragment fragment) {
        mFragment = new WeakReference<FragmentGiftViewer>((FragmentGiftViewer)fragment);
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
        Bundle bundle = new Bundle();
        bundle.putString(FragmentUserInfo.USER_KEY, mGift.getUserEmail());
        getMainPresenter().getFragmentManager().launchFragment(
                ViewManager.SHOW_USER,bundle,true);

    }

    public void chainClicked(){
        Bundle bundle = new Bundle();
        bundle.putLong(FragmentListGifts.KEY_GIFT_ID,mGift.getId());
        getMainPresenter().getFragmentManager().launchFragment(
                ViewManager.SHOW_LIST_GIFTS,bundle,true);
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

        FragmentGiftViewer fragment = mFragment.get();
        if (fragment == null) return;

        if (gift.getObscene()){
            fragment.mPhoto.getSecondOption().setImageResource(R.drawable.deny_a);
        }else{
            fragment.mPhoto.getSecondOption().setImageResource(R.drawable.deny);
        }
        fragment.mPhoto.getImage().setImageURI(Uri.parse(gift.getImageURL()));
       /* Picasso.with(mainActivityPresenter.getContext())
                .load(gift.getImageURL())
                .resize(50, 50)
                .centerCrop()
                .into(mFragment.mPhoto.getImage());*/


        mainActivityPresenter.getConnInterface().returnUserLogged(new ConnectionManager.ReturnUserHandler() {
            @Override
            public void onReturnUser(User user) {

                FragmentGiftViewer fragment = mFragment.get();
                if (fragment == null) return;
                if (user.getGiftPosted().contains(gift.getId())){
                    fragment.mDeleteButton.setVisibility(View.VISIBLE);
                }else{
                    fragment.mDeleteButton.setVisibility(View.INVISIBLE);
                }

                if(user.getGiftLiked().contains(mGift.getId())){
                    fragment.mPhoto.getFirstOption().setImageResource(R.drawable.heart_icon_des_r);
                }else{
                    fragment.mPhoto.getFirstOption().setImageResource(R.drawable.heart_icon_des);
                }
                fragment.mPhoto.setSecondOptionCounts(gift.getViewCounts());
            }

            @Override
            public void onError(String error) {

            }
        });
        if (mGift.getLongitude() == null && mGift.getLatitude() == null){
            ((TextView)fragment.mGpsPosition.getChildAt(0)).setText(R.string.no_location);
        }
        fragment.mTitle.setText(gift.getTitle());
        fragment.mDescription.setText(gift.getDescription());
        fragment.mUserData.setText(gift.getUserEmail() + "\n " + gift.getCreationDate());
        fragment.mGiftChainCount.setText(gift.getChainsID().size() +"");

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

    public void pushDelete(){
        mainActivityPresenter.getConnInterface().deleteGift(mGift.getId(),new ConnectionManager.ReturnBooleanHandler() {
            @Override
            public void onReturnBoolean(boolean success) {
                FragmentGiftViewer fragment = mFragment.get();
                if (fragment == null) return;
                getMainPresenter().getFragmentManager().removeBackStack(fragment);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
