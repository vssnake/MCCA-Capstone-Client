package com.vssnake.potlach;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {



    public MainActivityPresenter(){

    }

    public Account[] getGoogleAccounts(Context context){
        AccountManager manager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        return list;
    }
}
