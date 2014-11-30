package com.vssnake.potlach.main.fragments.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.model.User;

/**
 * Created by vssnake on 07/11/2014.
 */
public class UserInfoPresenter extends BasicPresenter{
    public UserInfoPresenter(MainActivityPresenter mainPresenter) {
        super(mainPresenter);
    }
    FragmentUserInfo mFragment;

    @Override
    public void attach(Fragment fragment) {
        mFragment = (FragmentUserInfo)fragment;
    }
    public void loadUserData(final String userEmail){
        if (userEmail.isEmpty()){
            mainActivityPresenter.getConnInterface().returnUserLogged(new ConnectionManager.ReturnUserHandler() {
                @Override
                public void onReturnUser(User user) {
                   showUserInfo(user.getEmail());
                }

                @Override
                public void onError(String error) {

                }
            });
        }else{
            showUserInfo(userEmail);
        }

    }

    private void showUserInfo(String userEmail){
        mainActivityPresenter.getConnInterface().showUser(userEmail,new ConnectionManager.ReturnUserHandler() {
            @Override
            public void onReturnUser(final User user) {
                mFragment.getEmail().setText(user.getEmail());
                mFragment.getName().setText(user.getName());
                Picasso.with(mainActivityPresenter.getContext())
                        .load(user.getUrlPhoto())
                        .into(mFragment.getUserPhoto());
                mFragment.getGiftCounts().setText(user.getGiftPosted().size() + "");
                mFragment.getInappropriateBox().setChecked(user.hideInappropriate());

                mainActivityPresenter.getConnInterface().returnUserLogged(
                        new ConnectionManager.ReturnUserHandler() {
                            @Override
                            public void onReturnUser(User mainUser) {
                                if (!mainUser.getEmail().equals(user.getEmail())){
                                    mFragment.getInappropriateBox().setVisibility(View.INVISIBLE);
                                }else{
                                    mFragment.getInappropriateBox().setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void showGiftsUser(String userEmail){
        Bundle bundle = new Bundle();
        bundle.putString(
                FragmentListGifts.KEY_USER_MAIL,userEmail);

        mainActivityPresenter.getFragmentManager().launchFragment(
                ViewManager.SHOW_LIST_GIFTS,bundle,true);
    }


    public void InappropriateClicked(){
        mainActivityPresenter.getConnInterface().modifyInappropriate(mFragment
                .getInappropriateBox().isChecked(),new ConnectionManager.ReturnUserInappropriateHandler() {
            @Override
            public void onReturnInappropriate(Boolean inappropriate) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }


}
