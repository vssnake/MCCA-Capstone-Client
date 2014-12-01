package com.vssnake.potlach.main.fragments.presenter;

import android.accounts.Account;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.vssnake.potlach.ConfigModule;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.main.fragments.LoginAdapter;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.testing.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vssnake on 29/10/2014.
 */
@Singleton
public class LoginPresenter extends BasicPresenter{

    public interface LoginHandler{
        public void onLoginResult(boolean logged);
    }

    String[] mAccounts;

    FragmentLogin mFragment;

    @Inject
    public LoginPresenter(MainActivityPresenter mainPresenter){
        super(mainPresenter);
       this.mainActivityPresenter = mainPresenter;
    }

    @Override
    public void attach(Fragment fragment) {
        mFragment = (FragmentLogin)fragment;
    }

    public MainActivityPresenter getMainPresenter(){
       return mainActivityPresenter;
    }



    public  String[] getGoogleAccounts(){

       Account[] accounts = mainActivityPresenter.getConnInterface().getAccounts();
       List<String> nameAccounts = new ArrayList<String>();
       for (int i=0;i<accounts.length;i++){
           if (accounts[i].type.equals("com.google")){
               nameAccounts.add(accounts[i].name);
           }

       }
       mAccounts = nameAccounts.toArray(new String[nameAccounts.size()]);
        return mAccounts;
    }

    public LoginAdapter getAccountsAdapter(){
        String accounts[] = getGoogleAccounts();
        return new LoginAdapter(accounts,mainActivityPresenter.getMainActivity());
    }

    public void local(Boolean local){
        getMainPresenter().changeConnectionManager(local);
    }

    public void userSelected(int position,ActionBarActivity activity){
        String email = mAccounts[position];
        mainActivityPresenter.getConnInterface().getLogin(email,activity, new LoginHandler() {
            @Override
            public void onLoginResult(boolean logged) {
                if (logged){
                    mainActivityPresenter.getFragmentManager().launchFragment(
                            ViewManager.SHOW_LIST_GIFTS,new Bundle(),false);
                }else{
                    Utils.ErrorDialogFragment errorDialog =new Utils.ErrorDialogFragment();
                    errorDialog.show(mainActivityPresenter.getMainActivity()
                            .getSupportFragmentManager(),"Login Error");
                }
            }
        });
    }
}
