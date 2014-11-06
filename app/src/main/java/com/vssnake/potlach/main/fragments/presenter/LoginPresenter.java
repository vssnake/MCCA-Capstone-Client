package com.vssnake.potlach.main.fragments.presenter;

import android.accounts.Account;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.OttoEvents;
import com.vssnake.potlach.main.fragments.LoginAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vssnake on 29/10/2014.
 */
@Singleton
public class LoginPresenter extends BasicPresenter{


    String[] mAccounts;

    @Inject
    public LoginPresenter(MainActivityPresenter mainPresenter){
        super(mainPresenter);
       this.mainActivityPresenter = mainPresenter;
    }

    @Override
    public void attach(Fragment fragment) {

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
        return new LoginAdapter(accounts);
    }

    public void userSelected(int position,ActionBarActivity activity){
        String email = mAccounts[position];
        mainActivityPresenter.getConnInterface().getLogin(email,activity);
    }
}
