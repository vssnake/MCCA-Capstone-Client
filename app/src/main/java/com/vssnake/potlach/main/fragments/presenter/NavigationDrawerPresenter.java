package com.vssnake.potlach.main.fragments.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.main.fragments.views.FragmentNavigationDrawer;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.model.User;

import javax.inject.Singleton;

/**
 * Created by vssnake on 05/11/2014.
 */
@Singleton
public class NavigationDrawerPresenter extends BasicPresenter {

    FragmentNavigationDrawer mFragment;

    public NavigationDrawerPresenter(MainActivityPresenter mainPresenter){
        super(mainPresenter);
    }

    @Override
    public void attach(Fragment fragment) {
        mFragment = (FragmentNavigationDrawer)fragment;
    }

    public void clickCreate(){
      //  getMainPresenter().getFragmentManager().removeBackStack();
        getMainPresenter().getFragmentManager().launchFragment(
                ViewManager.SHOW_GIFT_CREATION,new Bundle(),false);

        mFragment.getDrawerLayout().closeDrawers();


    }

    public void clickList(){
        getMainPresenter().getFragmentManager().launchFragment(
                ViewManager.SHOW_LIST_GIFTS,new Bundle(),true);

        mFragment.getDrawerLayout().closeDrawers();
    }

    public void clickLogout(){
        getMainPresenter().getConnInterface().logout(new ConnectionManager.ReturnBooleanHandler() {
            @Override
            public void onReturnBoolean(boolean success) {
                if (success){
                    getMainPresenter().getFragmentManager().removeBackStack();
                    getMainPresenter().getFragmentManager().launchFragment(
                            ViewManager.SHOW_LOGIN,new Bundle(),false);
                    mFragment.getDrawerLayout().closeDrawers();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void clickUserInfo(){
        getMainPresenter().getConnInterface().returnUserLogged(new ConnectionManager.ReturnUserHandler() {
            @Override
            public void onReturnUser(User user) {
                Bundle bundle = new Bundle();
                bundle.putString(FragmentUserInfo.USER_KEY,user.getEmail());
                getMainPresenter().getFragmentManager().launchFragment(
                        ViewManager.SHOW_USER,bundle,true);

                mFragment.getDrawerLayout().closeDrawers();
            }

            @Override
            public void onError(String error) {

            }
        });


    }

    public void clickSpecialInfo(){
        getMainPresenter().getFragmentManager().launchFragment(
                ViewManager.SHOW_SPECIAL_INFO,new Bundle(),true);
        mFragment.getDrawerLayout().closeDrawers();
    }

    public void loadUserData(){
        getMainPresenter().getConnInterface().returnUserLogged(new ConnectionManager.ReturnUserHandler() {
            @Override
            public void onReturnUser(User user) {
                mFragment.getUserEmail().setText(user.getEmail());
                mFragment.getUserName().setText(user.getName());
                Picasso.with(mFragment.getActivity()).load(user.getUrlPhoto()).into
                        (mFragment.getUserPhoto());
            }

            @Override
            public void onError(String error) {
                mFragment.getUserEmail().setText("");
                mFragment.getUserName().setText("");
                mFragment.getUserPhoto().setImageResource(R.drawable.ic_account_circle_grey600_48dp);
            }
        });
    }
}
