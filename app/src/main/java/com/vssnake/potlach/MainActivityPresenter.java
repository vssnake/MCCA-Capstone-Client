package com.vssnake.potlach;

import android.content.Context;
import android.content.Intent;
import android.os.storage.StorageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;


import com.squareup.otto.Bus;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.FileManager;
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.testing.Utils;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {



    public static Bus  bus = new Bus();

    private Context mContext;




    ConnectionManager mComunicationInterface;
    private FileManager mFileManager;

    private FragmentManager mFragmentManager;

    MainActivity mMainActivity;

    public MainActivityPresenter(PotlatchApp app,ConnectionManager comInterface){
        mContext = app.getApplicationContext();
        mComunicationInterface = comInterface;
        mFileManager = new FileManager(app.getApplicationContext());



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

    public FileManager getFileManager() {
        return mFileManager;
    }


    public class FragmentManager{



        android.support.v4.app.FragmentManager mFragmentManager;
        public FragmentManager(MainActivity mainActivity){
            mMainActivity = mainActivity;
            mFragmentManager=  mMainActivity.getSupportFragmentManager();
        }
        public void showDefaultView(){
            android.support.v4.app.FragmentManager fm = mMainActivity.getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentListGifts.newInstance("", ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showUser(String email){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentUserInfo.newInstance(email, ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showGiftChain(long giftID){

            mFragmentManager.beginTransaction()
                    .replace(R.id.container,
                            FragmentListGifts.newInstance(giftID))
                    .addToBackStack("yeah")
                    .commit();

        }


        public void showGift(Long giftID,View sharedElement){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.addSharedElement(sharedElement,sharedElement.getTransitionName());
            FragmentGiftViewer fragment = FragmentGiftViewer.newInstance(giftID
                    ,sharedElement.getTransitionName());
            fragment.setEnterTransition(new Explode());
            //fragment.setSharedElementEnterTransition(new Explode());
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("yeah");
            transaction.commit();
            /*mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentGiftViewer.newInstance(giftID, ""))
                    .addToBackStack("yeah")
                    .commit();*/
        }

        public void selectGiftChain(final GiftCreatorPresenter.ChainSelected chainCallback){
            FragmentListGifts fragment = FragmentListGifts.newInstance("", "");
            fragment.setChainSelected(new GiftCreatorPresenter.ChainSelected() {
                @Override
                public void onChainSelectedCallback(Long idGift) {
                    mFragmentManager.popBackStack();
                    chainCallback.onChainSelectedCallback(idGift);

                }
            });
            mFragmentManager.beginTransaction()
                    .replace(R.id.container,fragment )
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showUserGifts(String userEmail){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentListGifts.newInstance(userEmail, ""))
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
