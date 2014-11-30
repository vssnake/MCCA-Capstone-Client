package com.vssnake.potlach.main.fragments.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

/**
 * Created by vssnake on 27/11/2014.
 */
public class SpecialInfoPresenter extends BasicPresenter {

    SpecialInfo mSpecialInfo;
    FragmentSpecialInfo mFragment;
    public SpecialInfoPresenter(MainActivityPresenter mainPresenter) {
        super(mainPresenter);
    }

    @Override
    public void attach(Fragment fragment) {
        mFragment = (FragmentSpecialInfo)fragment;
    }

    public void giftClicked(int number){
        Bundle bundle = new Bundle();
        bundle.putLong(FragmentGiftViewer.KEY_GIFT_ID, mSpecialInfo.getGiftsOfTheDay()
                [number - 1].getId());
        getMainPresenter().getFragmentManager().launchFragment(ViewManager.SHOW_GIFT,bundle,true);
    }

    public void userClicked(int number){
        Bundle bundle = new Bundle();
        bundle.putString(FragmentUserInfo.USER_KEY,
                mSpecialInfo.getUsersOfTheDay()[number -1].getEmail());
        getMainPresenter().getFragmentManager().launchFragment(ViewManager.SHOW_USER,bundle,true);
    }

    public void init(){
        getMainPresenter().getConnInterface().showSpecialInfo("",new ConnectionManager.ReturnSpecialInfoHandler() {
            @Override
            public void onReturnSpecialInfo(SpecialInfo specialInfo) {
                SpecialInfoPresenter.this.mSpecialInfo = specialInfo;

                loadGifts(1,specialInfo.getGiftsOfTheDay()[0]);
                loadGifts(2,specialInfo.getGiftsOfTheDay()[1]);
                loadGifts(3,specialInfo.getGiftsOfTheDay()[2]);

                loadUsers(1,specialInfo.getUsersOfTheDay()[0]);
                loadUsers(2,specialInfo.getUsersOfTheDay()[1]);
                loadUsers(3,specialInfo.getUsersOfTheDay()[2]);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void loadGifts(int count,Gift gift){
        ImageView image = null;
        switch (count){
            case 1:
                image = mFragment.mGift1;
                break;
            case 2:
                image = mFragment.mGift2;
                break;
            case 3:
                image = mFragment.mGift3;
                break;
        }
        Picasso.with(getMainPresenter().getContext()).load(gift.getThumbnailURL())
                .into(image);

    }

    private void loadUsers(int count,User user){
        TextView textView = null;

        switch (count){
            case 1:
                textView = mFragment.mUser1;
                break;
            case 2:
                textView = mFragment.mUser2;
                break;
            case 3:
                textView = mFragment.mUser3;
                break;
        }
        textView.setText(count  + "º " + user.getEmail());
    }
}
