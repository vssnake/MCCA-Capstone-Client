package com.vssnake.potlach;

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
import com.squareup.otto.Bus;

import java.io.IOException;

import javax.inject.Singleton;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {

    public static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    public static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    public static final int REQUEST_CODE_TAKE_PHOTO_CAMERA = 1003;
    public static final int REQUEST_CODE_TAKE_PHOTO_SD = 1004;

    public static Bus  bus = new Bus();

    private Context mContext;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email " +
            "https://www.googleapis.com/auth/userinfo.profile";
    String bearerToken;
    private AuthManager authManager;

    public MainActivityPresenter(Context context){
        mContext = context;
        authManager = new AuthManager();
    }

    String mUserEmail;



    public void onActivityResult(int requestCode, int resultCode, Intent data,ActionBarActivity activity){
        if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == activity.RESULT_OK) {
            // Receiving a result that follows a GoogleAuthException, try auth again
            getAuthManager().getLogin(mUserEmail, activity);
        }
        
        bus.post( new OttoEvents.ActivityResultEvent(requestCode,resultCode,data,activity));
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public Context getContext() {
        return mContext;
    }

    interface LoginHandler{
        void isLogged(boolean logged);
    }





    public class AuthManager{



        public Account[] getAccounts(){
            AccountManager manager = (AccountManager) getContext().getSystemService(getContext().ACCOUNT_SERVICE);
            Account[] list = manager.getAccounts();
            return list;
        }

        public void isLogged(String email,final LoginHandler loginHandler){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bearerToken = GoogleAuthUtil.getToken(getContext(),mUserEmail,SCOPE);
                        loginHandler.isLogged(true);
                    } catch (Exception e) {
                        loginHandler.isLogged(false);
                    }
                }
            }).start();
        }


        public void getLogin(String email,final ActionBarActivity activity){



                mUserEmail = email;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bearerToken = GoogleAuthUtil.getToken(getContext(),mUserEmail,SCOPE);
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
                                REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                        dialog.show();
                    } else if (e instanceof UserRecoverableAuthException) {
                        // Unable to authenticate, such as when the user has not yet granted
                        // the app access to the account, but the user can fix this.
                        // Forward the user to an activity in Google Play services.
                        Intent intent = ((UserRecoverableAuthException)e).getIntent();
                        activity.startActivityForResult(intent,
                                REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    }
                }
            });
        }
    }



}
