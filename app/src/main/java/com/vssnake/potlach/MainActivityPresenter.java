package com.vssnake.potlach;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;


import com.squareup.otto.Bus;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import comunication.ComInterface;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {



    public static Bus  bus = new Bus();

    private Context mContext;




    ConnectionManager mComunicationInterface;

    private FragmentManager mFragmentManager;

    MainActivity mMainActivity;

    public MainActivityPresenter(PotlatchApp app,ConnectionManager comInterface){
        mContext = app.getApplicationContext();
        mComunicationInterface = comInterface;



    }

    public void attach(MainActivity main){
        mFragmentManager = new FragmentManager(main);
        mMainActivity = main;
    }

    public void detach(){
        mMainActivity = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data,ActionBarActivity activity){
        if ((requestCode == SData.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == SData.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == activity.RESULT_OK) {
            // Receiving a result that follows a GoogleAuthException, try auth again
            mComunicationInterface.getLogin(activity);
        }
        
        bus.post( new OttoEvents.ActivityResultEvent(requestCode,resultCode,data,activity));
    }

    public ConnectionManager getConnInterface() {
        return mComunicationInterface;
    }

    public Context getContext() {
        return mContext;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }


    public class FragmentManager{



        android.support.v4.app.FragmentManager mFragmentManager;
        public FragmentManager(MainActivity mainActivity){
            mMainActivity = mainActivity;
            mFragmentManager=  mMainActivity.getSupportFragmentManager();
        }
        public void showGift(Long giftID){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentGiftViewer.newInstance(giftID, ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void launchFragment(SData.Fragments fragments){
            switch (fragments) {
                case LoginFragment:
                    mComunicationInterface.isLogged(new ConnectionManager.LoginHandler() {
                        @Override
                        public void isLogged(boolean logged) {
                            if (logged){
                                launchFragment(SData.Fragments.GiftListFragment);
                            }else{
                                mFragmentManager.beginTransaction()
                                        .replace(R.id.container, FragmentLogin.newInstance("", ""))
                                        .addToBackStack("yeah")
                                        .commit();
                            }
                        }
                    });

                    break;
                case UserFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentUserInfo.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftCreatorFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentGiftCreator.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftListFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentListGifts.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftViewerFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentGiftViewer.newInstance(0l, ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case SpecialInfoFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentSpecialInfo.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
            }
        }
    }



}
