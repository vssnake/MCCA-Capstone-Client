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
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import java.io.IOException;

import comunication.RetrofitInterface;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vssnake on 05/11/2014.
 */
public class ConnectionManager{

    String mBearerToken;
    Context mContext;
    String mUserEmail;

    RetrofitInterface mCommunicationInterface;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email " +
            "https://www.googleapis.com/auth/userinfo.profile";

    public ConnectionManager( RetrofitInterface communicationInterface,Context context){
        mCommunicationInterface = communicationInterface;
        mContext = context;
        //TODO Only for testing
        login("virtual.solid.snake@gmail.com", new ReturnBooleanHandler() {
            @Override
            public void onReturnBoolean(boolean success) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
    public Account[] getAccounts(){
        AccountManager manager = (AccountManager) mContext.getSystemService(mContext.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        return list;
    }

    User mLoggedUser = null;

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


    public void login(String email, final ReturnBooleanHandler returnBooleanHandler){
        mCommunicationInterface.login("",email,new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mLoggedUser = user;
                returnBooleanHandler.onReturnBoolean(true);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void logout(final ReturnBooleanHandler returnBooleanHandler){
        mCommunicationInterface.logout(mLoggedUser,new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean, Response response) {
                mLoggedUser = null;
                returnBooleanHandler.onReturnBoolean(aBoolean);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showUser(String email, final ReturnUserHandler returnUserHandler){
        mCommunicationInterface.showUser("",email,new Callback<User>() {
            @Override
            public void success(User user, Response response) {

                returnUserHandler.onReturnUser(user);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void returnUserLogged(ReturnUserHandler returnUserHandler){
        if (mLoggedUser != null){
            returnUserHandler.onReturnUser(mLoggedUser);
        }else{
            returnUserHandler.onError("User not logged");
        }
    }
    public void searchUser(String email, final ReturnUsersHandler returnUsersHandler){
      mCommunicationInterface.searchUser("",email,new Callback<User[]>() {
          @Override
          public void success(User[] users, Response response) {
              returnUsersHandler.onReturnUser(users);
          }

          @Override
          public void failure(RetrofitError error) {

          }
      });
    }
    public void createGift(GiftCreator giftCreator, final ReturnGiftHandler returnGiftHandler){
        mCommunicationInterface.createGift("",giftCreator,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showGift(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mCommunicationInterface.showGift("",idGift,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showUserGifts(String email, final ReturnGiftsHandler returnGiftsHandler){
        mCommunicationInterface.showUserGifts("",email,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void searchGifts(String title, final ReturnGiftsHandler returnGiftsHandler){
        mCommunicationInterface.searchGifts("",title,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void modifyLike(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mCommunicationInterface.modifyLike("",idGift,mLoggedUser,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void setObscene(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mCommunicationInterface.setObscene("",idGift,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void deleteGift(Long idGift, final ReturnBooleanHandler returnBooleanHandler){
        mCommunicationInterface.deleteGift("",idGift,mLoggedUser,new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean, Response response) {
                returnBooleanHandler.onReturnBoolean(aBoolean);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showGifts(int startGift, final ReturnGiftsHandler returnGiftsHandler){
        mCommunicationInterface.showGifts("",startGift,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showGiftsChain(Long idGift, final ReturnGiftsHandler returnGiftsHandler){
        mCommunicationInterface.showGiftChain("",idGift,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    public void showSpecialInfo(String date, final ReturnSpecialInfoHandler returnSpecialInfoHandler){
        mCommunicationInterface.showSpecialInfo("",date,new Callback<SpecialInfo>() {
            @Override
            public void success(SpecialInfo specialInfo, Response response) {
                returnSpecialInfoHandler.onReturnSpecialInfo(specialInfo);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public interface LoginHandler{
        void isLogged(boolean logged);
    }
    public interface returnInterfaceBase{
        void onError(String error);
    }

    public interface ReturnUserHandler extends returnInterfaceBase{
        void onReturnUser(User user);
    }
    public interface ReturnUsersHandler extends returnInterfaceBase{
        void onReturnUser(User[] user);
    }
    public interface ReturnBooleanHandler extends returnInterfaceBase{
        void onReturnBoolean(boolean success);
    }
    public interface ReturnGiftHandler extends returnInterfaceBase{
        void onReturnHandler(Gift gift);
    }
    public interface ReturnGiftsHandler extends returnInterfaceBase{
        void onReturnGifts(Gift[] gifts);
    }
    public interface ReturnSpecialInfoHandler extends returnInterfaceBase{
        void onReturnSpecialInfo(SpecialInfo specialInfo);
    }

}
