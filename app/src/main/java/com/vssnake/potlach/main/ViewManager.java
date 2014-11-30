package com.vssnake.potlach.main;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;

import com.vssnake.potlach.MainActivity;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by vssnake on 21/11/2014.
 */
public class ViewManager {

    MainActivityPresenter mMainPresenter;

    public static final int SHOW_USER = 1;
    public static final int SHOW_GIFT = 3;
    public static final int SHOW_LIST_GIFTS = 4;
    public static final int SHOW_LOGIN = 5;
    public static final int SHOW_GIFT_CREATION = 6;
    public static final int SHOW_SPECIAL_INFO = 7;


    android.support.v4.app.FragmentManager mFragmentManager;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SHOW_USER,
            SHOW_GIFT,
            SHOW_LIST_GIFTS,
            SHOW_LOGIN,
            SHOW_GIFT_CREATION,
            SHOW_SPECIAL_INFO})
    public @interface NavigationMode {}

    public ViewManager(MainActivityPresenter mainPresenter){
        mMainPresenter = mainPresenter;
        mFragmentManager=  mMainPresenter.getMainActivity().getSupportFragmentManager();

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.
                OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });

    }

    public void launchFragment(final @NavigationMode int mode,final Bundle bundle,
                               final boolean backStack){

        //TODO check login
        mMainPresenter.getConnInterface().isLogged(new ConnectionManager.LoginHandler() {
            @Override
            public void isLogged(boolean logged) {
                if (logged){
                    switch (mode){
                        case SHOW_USER:
                            showUser(bundle,backStack);
                            break;
                        case SHOW_LIST_GIFTS:
                            showGiftList(bundle,backStack);
                            break;
                        case SHOW_GIFT:
                            showGift(bundle,backStack);
                            break;
                        case SHOW_LOGIN:
                            showLogin(backStack);
                            break;
                        case SHOW_GIFT_CREATION:
                           // mFragmentManager.popBackStack();
                            showCreation();

                            break;
                        case SHOW_SPECIAL_INFO:
                            showSpecialInfo(backStack);
                            break;
                    }
                }else{
                    showLogin(false);
                }

            }
        });


    }


    public void removeBackStack(){

      //  mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void removeBackStack(String code){
      //  mFragmentManager.popBackStack(code, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void removeBackStack(Fragment fragment){

        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        mFragmentManager.popBackStack();
    }

    private void showCreation(){
        Fragment fragment = FragmentGiftCreator.newInstance();
        fragment.setEnterTransition(new Slide());
        mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("creator")
                .commit();
    }

    private void showLogin(boolean backStack){
        Fragment fragment = FragmentLogin.newInstance();
        fragment.setEnterTransition(new Slide());
        FragmentTransaction transaction = mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment);
        if (backStack){
            transaction = transaction.addToBackStack("login");
        }
        transaction.commit();

    }

    private void showGiftList(Bundle bundle,boolean backStack){
        Fragment fragment = FragmentListGifts.newInstance(bundle);
        fragment.setEnterTransition(new Slide());
        FragmentTransaction transaction = mFragmentManager.beginTransaction()
                .replace(R.id.container,fragment );

        if (backStack){
            transaction = transaction.addToBackStack("giftList");
        }
        transaction.commit();
    }

    private void showUser(Bundle bundle,boolean backStack){
        Fragment fragment = FragmentUserInfo.newInstance(bundle);
        fragment.setEnterTransition(new Slide());
        FragmentTransaction transaction = mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment);
        if (backStack){
            transaction = transaction.addToBackStack("user");
        }
        transaction.commit();
    }

    private void showSpecialInfo(boolean backStack) {
        Fragment fragment = FragmentSpecialInfo.newInstance();
        fragment.setEnterTransition(new Slide());
        FragmentTransaction transaction = mFragmentManager.beginTransaction()
                .replace(R.id.container, fragment);
        if (backStack){
            transaction = transaction.addToBackStack("specialInfo");
        }
        transaction.commit();
    }

   /* private void showGiftChain(Bundle bundle){

        mFragmentManager.beginTransaction()
                .replace(R.id.container,
                        FragmentListGifts.newInstance(bundle))
                .addToBackStack("yeah")
                .commit();

    }*/


    private void showGift(Bundle bundle,boolean backStack){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //transaction.addSharedElement(sharedElement,sharedElement.getTransitionName());
        FragmentGiftViewer fragment = FragmentGiftViewer.newInstance(bundle);
        fragment.setEnterTransition(new Explode());
        //fragment.setSharedElementEnterTransition(new Explode());
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("yeah");
        transaction.commit();

    }

   public void selectGiftChain(final GiftCreatorPresenter.ChainSelected chainCallback){
        FragmentListGifts fragment = FragmentListGifts.newInstance(new Bundle());
        fragment.setEnterTransition(new Slide());
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



    /*public void launchFragment(SData.Fragments fragments){
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

                break;
            case GiftListFragment:
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, FragmentListGifts.newInstance(""))
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
    }*/
}
