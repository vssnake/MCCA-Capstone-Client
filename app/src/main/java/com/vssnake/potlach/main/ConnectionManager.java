package com.vssnake.potlach.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.main.fragments.presenter.LoginPresenter;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;
import com.vssnake.potlach.testing.Utils;

import java.io.File;
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

    private String mGCMKey;

    public static final String TAG = "ConnectionManager";

    RetrofitInterface mCommunicationInterface;

    MainActivityPresenter mMainActivityPresenter;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email " +
            "https://www.googleapis.com/auth/userinfo.profile";

    User mLoggedUser = null;
    String mUserEmail = "";

    public ConnectionManager( RetrofitInterface communicationInterface,Context context){
        mCommunicationInterface = communicationInterface;
        mContext = context;

    }
    public Account[] getAccounts(){
        AccountManager manager = (AccountManager) mContext.getSystemService(mContext.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        return list;
    }

    public void setPresenter(MainActivityPresenter presenter){
        mMainActivityPresenter = presenter;
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

    public void isLogged(final LoginHandler loginHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBearerToken = GoogleAuthUtil.getToken(mContext, mLoggedUser.getEmail(), SCOPE);
                    loginHandler.isLogged(true);
                } catch (Exception e) {
                    loginHandler.isLogged(false);
                }
            }
        }).start();
    }

    private void getLogin(final ActionBarActivity activity,
                          final LoginPresenter.LoginHandler loginHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBearerToken = GoogleAuthUtil.getToken(mContext,mUserEmail,SCOPE);
                    if (mBearerToken != null){
                        login(mUserEmail,new ReturnBooleanHandler() {
                            @Override
                            public void onReturnBoolean(boolean success) {
                                loginHandler.onLoginResult(success);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }else{
                        loginHandler.onLoginResult(false);
                    }
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

    public void getLogin(final ActionBarActivity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBearerToken = GoogleAuthUtil.getToken(mContext, mUserEmail, SCOPE);
                } catch (UserRecoverableAuthException userRecoverableException) {
                    handleException(userRecoverableException, activity);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getLogin(String email,final ActionBarActivity activity,
                         LoginPresenter.LoginHandler loginHandler){

        mUserEmail = email;
        getLogin(activity,loginHandler);

    }


    private void login(String email,final ReturnBooleanHandler returnBooleanHandler){
        mUserEmail = email;
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.login(mBearerToken,email,mGCMKey,new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mLoggedUser = user;
                returnBooleanHandler.onReturnBoolean(true);
                mMainActivityPresenter.setLoadingView(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"onlogin " +error.getMessage());
                mMainActivityPresenter.setLoadingView(false);
            }
        });
    }
    public void logout(final ReturnBooleanHandler returnBooleanHandler){
        isLogged(new LoginHandler() {
            @Override
            public void isLogged(boolean logged) {
                if (logged){
                    mMainActivityPresenter.setLoadingView(true);
                    mCommunicationInterface.logout(mBearerToken,mGCMKey,new Callback<Boolean>() {
                        @Override
                        public void success(Boolean aBoolean, Response response) {
                            mLoggedUser = null;
                            mMainActivityPresenter.setLoadingView(false);
                            returnBooleanHandler.onReturnBoolean(aBoolean);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG,"onlogout " +error.getMessage());
                            mMainActivityPresenter.setLoadingView(false);
                        }
                    });
                }
            }
        });

    }
    public void showUser(String email, final ReturnUserHandler returnUserHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showUser(mBearerToken,email,new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnUserHandler.onReturnUser(user);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowUser " +error.getMessage());
            }
        });
    }

    public void modifyInappropriate(Boolean inappropriate,final ReturnUserInappropriateHandler
            handler){
        if (mLoggedUser != null){
            mMainActivityPresenter.setLoadingView(true);
            mCommunicationInterface.modifyInappropriate(mBearerToken,inappropriate,new Callback<Boolean>() {
                @Override
                public void success(Boolean aBoolean, Response response) {
                    mMainActivityPresenter.setLoadingView(false);
                    mLoggedUser.setHideInappropriate(aBoolean);
                    handler.onReturnInappropriate(aBoolean);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG,"onModifyInappropriate " +error.getMessage());
                }
            });
        }
    }
    public void returnUserLogged(final ReturnUserHandler returnUserHandler){
        if (mLoggedUser != null){
            mMainActivityPresenter.setLoadingView(true);
            showUser(mLoggedUser.getEmail(),new ReturnUserHandler() {
                @Override
                public void onReturnUser(User user) {
                    mMainActivityPresenter.setLoadingView(false);
                    mLoggedUser = user;
                    returnUserHandler.onReturnUser(mLoggedUser);
                }

                @Override
                public void onError(String error) {
                    mMainActivityPresenter.setLoadingView(false);

                }
            });

        }else{
            returnUserHandler.onError("User not logged");

        }
    }
    public void searchUser(String email, final ReturnUsersHandler returnUsersHandler){
      mMainActivityPresenter.setLoadingView(true);
      mCommunicationInterface.searchUser("",email,new Callback<User[]>() {
          @Override
          public void success(User[] users, Response response) {
              mMainActivityPresenter.setLoadingView(false);
              returnUsersHandler.onReturnUser(users);
          }

          @Override
          public void failure(RetrofitError error) {
              mMainActivityPresenter.setLoadingView(false);
              Log.e(TAG,"onSearchUser " +error.getMessage());
          }
      });
    }
    public void createGift(GiftCreator gift,
            final ReturnGiftHandler returnGiftHandler){
        Bitmap photo = BitmapFactory.decodeFile(gift.getImage().file().getPath());
        Bitmap thumbnailPhoto = ThumbnailUtils.extractThumbnail(photo, 150, 150);
        File fileThumb = FileManager.setTempImageThumb(thumbnailPhoto);


        gift.setImageThumb(fileThumb.getAbsolutePath());
        gift.setUserEmail(mLoggedUser.getEmail());

        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.createGift(mBearerToken,gift.getChainID(),
                gift.getTitle(),
                gift.getDescription(),
                gift.getLatitude(),
                gift.getLongitude(),
                gift.getPrecision(),
                gift.getImage(),
                gift.getImageThumb(),
                new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                //Log.e(TAG,"onCreateGift");
                mMainActivityPresenter.setLoadingView(false);
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onCreateGift " +error.getMessage());
            }
        });
    }
    public void showGift(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showGift(mBearerToken,idGift,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowGift " +error.getMessage());
            }
        });
    }


    public void showUserGifts(String email, final ReturnGiftsHandler returnGiftsHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showUserGifts(mBearerToken,email,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowUserGifts " +error.getMessage());
            }
        });
    }
    public void searchGifts(String title, final ReturnGiftsHandler returnGiftsHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.searchGifts(mBearerToken,title,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onSearchGifts " +error.getMessage());
            }
        });
    }
    public void modifyLike(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.modifyLike(mBearerToken,idGift,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onModifyLike " +error.getMessage());
            }
        });
    }
    public void setObscene(Long idGift, final ReturnGiftHandler returnGiftHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.setObscene("",idGift,new Callback<Gift>() {
            @Override
            public void success(Gift gift, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftHandler.onReturnHandler(gift);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onSetObscene " +error.getMessage());
            }
        });
    }
    public void deleteGift(Long idGift, final ReturnBooleanHandler returnBooleanHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.deleteGift(mBearerToken,idGift,new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnBooleanHandler.onReturnBoolean(aBoolean);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onDeleteGift " +error.getMessage());
            }
        });
    }
    public void showGifts(int startGift, final ReturnGiftsHandler returnGiftsHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showGifts(mBearerToken,startGift,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowGifts " +error.getMessage());
            }
        });
    }
    public void showGiftsChain(Long idGift, final ReturnGiftsHandler returnGiftsHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showGiftChain(mBearerToken,idGift,new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnGiftsHandler.onReturnGifts(gifts);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowGiftsChain " +error.getMessage());
            }
        });
    }
    public void showSpecialInfo(String date, final ReturnSpecialInfoHandler returnSpecialInfoHandler){
        mMainActivityPresenter.setLoadingView(true);
        mCommunicationInterface.showSpecialInfo(mBearerToken,new Callback<SpecialInfo>() {
            @Override
            public void success(SpecialInfo specialInfo, Response response) {
                mMainActivityPresenter.setLoadingView(false);
                returnSpecialInfoHandler.onReturnSpecialInfo(specialInfo);
            }

            @Override
            public void failure(RetrofitError error) {
                mMainActivityPresenter.setLoadingView(false);
                Log.e(TAG,"onShowSpecialInfo " +error.getMessage());
            }
        });
    }

    public String getGCMKey() {
        return mGCMKey;
    }

    public void setGCMKey(String mGCMKey) {
        this.mGCMKey = mGCMKey;
    }

    public interface LoginHandler{
        void isLogged(boolean logged);
    }
    public interface returnInterfaceBase{
        void onError(String error);
    }
    public interface ReturnUserInappropriateHandler extends returnInterfaceBase{
        void onReturnInappropriate(Boolean inappropriate);
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
