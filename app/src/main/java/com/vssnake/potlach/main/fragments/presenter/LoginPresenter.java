package com.vssnake.potlach.main.fragments.presenter;

import android.accounts.Account;
import android.content.Context;

import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.PotlatchApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vssnake on 29/10/2014.
 */
@Singleton
public class LoginPresenter {
   public MainActivityPresenter mainActivityPresenter;


    @Inject
    public LoginPresenter(MainActivityPresenter mainPresenter){
       this.mainActivityPresenter = mainPresenter;
    }

    public MainActivityPresenter getMainPresenter(){
       return mainActivityPresenter;
    }

   public String[] getGoogleAccounts(Context context){
       Account[] accounts = mainActivityPresenter.getGoogleAccounts(context);
       List<String> nameAccounts = new ArrayList<String>();
       for (int i=0;i<accounts.length;i++){
           if (accounts[i].type.equals("com.google")){
               nameAccounts.add(accounts[i].name);
           }

       }
        return nameAccounts.toArray(new String[nameAccounts.size()]);
    }
}
