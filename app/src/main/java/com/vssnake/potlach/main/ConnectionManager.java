package com.vssnake.potlach.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

import comunication.ComInterface;

/**
 * Created by vssnake on 05/11/2014.
 */
public abstract class ConnectionManager implements ComInterface {

    String mBearerToken;
    Context mContext;
    String mUserEmail;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email " +
            "https://www.googleapis.com/auth/userinfo.profile";

    public ConnectionManager(Context context){
        mContext = context;
    }
    public Account[] getAccounts(){
        AccountManager manager = (AccountManager) mContext.getSystemService(mContext.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        return list;
    }

    public void isLogged(final LoginHandler loginHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBearerToken = GoogleAuthUtil.getToken(mContext, mUserEmail, SCOPE);
                    loginHandler.isLogged(true);
                } catch (Exception e) {
                    loginHandler.isLogged(false);
                }
            }
        }).start();
    }

    public void getLogin(final ActionBarActivity activity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBearerToken = GoogleAuthUtil.getToken(mContext,mUserEmail,SCOPE);
                } catch (UserRecoverableAuthException userRecoverableException) {
                    handleException(userRecoverableException,activity);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getLogin(String email,final ActionBarActivity activity){

        mUserEmail = email;
        getLogin(activity);

    }

    /**
     * This method is a hook for background threads and async tasks that need to
     * provide the user a response UI when an exception occurs.
     */
    public void handleException(final Exception e,final ActionBarActivity activity) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            activity,
                            SData.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    activity.startActivityForResult(intent,
                            SData.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    public interface LoginHandler{
        void isLogged(boolean logged);
    }
}
