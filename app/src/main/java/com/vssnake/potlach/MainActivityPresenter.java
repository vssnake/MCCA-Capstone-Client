package com.vssnake.potlach;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.otto.Bus;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.FileManager;
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;
import com.vssnake.potlach.testing.Utils;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {



    public static Bus  bus = new Bus();

    private Context mContext;




    ConnectionManager mComunicationInterface;
    private FileManager mFileManager;

    private FragmentManager mFragmentManager;
    private LocationManager mLocationManager;

    MainActivity mMainActivity;

    public MainActivityPresenter(PotlatchApp app,ConnectionManager comInterface){
        mContext = app.getApplicationContext();
        mComunicationInterface = comInterface;
        mFileManager = new FileManager(app.getApplicationContext());
        mLocationManager = new LocationManager();



    }

    public void attach(MainActivity main){
        mFragmentManager = new FragmentManager(main);
        mMainActivity = main;
    }

    public void detach(){
        mMainActivity = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data,ActionBarActivity activity){
        if ((requestCode == SData.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == SData.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == activity.RESULT_OK) {
            // Receiving a result that follows a GoogleAuthException, try auth again
            mComunicationInterface.getLogin(activity);
        }else if(requestCode == SData.CONNECTION_FAILURE_RESOLUTION_REQUEST){

        }
        
        bus.post( new OttoEvents.ActivityResultEvent(requestCode,resultCode,data,activity));
    }

    public ConnectionManager getConnInterface() {
        return mComunicationInterface;
    }

    public Context getContext() {
        return mContext;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public FileManager getFileManager() {
        return mFileManager;
    }

    public LocationManager getLocationManager() {
        return mLocationManager;
    }


    public class FragmentManager{



        android.support.v4.app.FragmentManager mFragmentManager;
        public FragmentManager(MainActivity mainActivity){
            mMainActivity = mainActivity;
            mFragmentManager=  mMainActivity.getSupportFragmentManager();
        }
        public void showDefaultView(){
            android.support.v4.app.FragmentManager fm = mMainActivity.getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentListGifts.newInstance("", ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showUser(String email){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentUserInfo.newInstance(email, ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showGiftChain(long giftID){

            mFragmentManager.beginTransaction()
                    .replace(R.id.container,
                            FragmentListGifts.newInstance(giftID))
                    .addToBackStack("yeah")
                    .commit();

        }


        public void showGift(Long giftID,View sharedElement){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.addSharedElement(sharedElement,sharedElement.getTransitionName());
            FragmentGiftViewer fragment = FragmentGiftViewer.newInstance(giftID
                    ,sharedElement.getTransitionName());
            fragment.setEnterTransition(new Explode());
            //fragment.setSharedElementEnterTransition(new Explode());
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("yeah");
            transaction.commit();
            /*mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentGiftViewer.newInstance(giftID, ""))
                    .addToBackStack("yeah")
                    .commit();*/
        }

        public void selectGiftChain(final GiftCreatorPresenter.ChainSelected chainCallback){
            FragmentListGifts fragment = FragmentListGifts.newInstance("", "");
            fragment.setChainSelected(new GiftCreatorPresenter.ChainSelected() {
                @Override
                public void onChainSelectedCallback(Long idGift) {
                    mFragmentManager.popBackStack();
                    chainCallback.onChainSelectedCallback(idGift);

                }
            });
            mFragmentManager.beginTransaction()
                    .replace(R.id.container,fragment )
                    .addToBackStack("yeah")
                    .commit();
        }

        public void showUserGifts(String userEmail){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, FragmentListGifts.newInstance(userEmail, ""))
                    .addToBackStack("yeah")
                    .commit();
        }

        public void launchFragment(SData.Fragments fragments){
            switch (fragments) {
                case LoginFragment:
                    mComunicationInterface.isLogged(new ConnectionManager.LoginHandler() {
                        @Override
                        public void isLogged(boolean logged) {
                            if (logged){
                                launchFragment(SData.Fragments.GiftListFragment);
                            }else{
                                mFragmentManager.beginTransaction()
                                        .replace(R.id.container, FragmentLogin.newInstance("", ""))
                                        .addToBackStack("yeah")
                                        .commit();
                            }
                        }
                    });

                    break;
                case UserFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentUserInfo.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftCreatorFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentGiftCreator.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftListFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentListGifts.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case GiftViewerFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentGiftViewer.newInstance(0l, ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
                case SpecialInfoFragment:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentSpecialInfo.newInstance("", ""))
                            .addToBackStack("yeah")
                            .commit();
                    break;
            }
        }
    }


    public class LocationManager implements
            GooglePlayServicesClient.ConnectionCallbacks,
            GooglePlayServicesClient.OnConnectionFailedListener,
            LocationListener {

        LocationClient mLocationClient;
        // Define an object that holds accuracy and frequency parameters
        LocationRequest mLocationRequest;

        // Milliseconds per second
        private static final int MILLISECONDS_PER_SECOND = 1000;
        // Update frequency in seconds
        public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
        // Update frequency in milliseconds
        private static final long UPDATE_INTERVAL =
                MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
        // The fastest update frequency, in seconds
        private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
        // A fast frequency ceiling in milliseconds
        private static final long FASTEST_INTERVAL =
                MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;


        public LocationManager(){
            mLocationClient = new LocationClient(mContext,this,this);
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        }

        public void connect(){
            mLocationClient.connect();
        }
        public void disconnect(){
            mLocationClient.disconnect();
        }


        private boolean servicesConnected() {
            // Check that Google Play services is available
            int resultCode =
                    GooglePlayServicesUtil.
                            isGooglePlayServicesAvailable(mContext);
            // If Google Play services is available
            if (ConnectionResult.SUCCESS == resultCode) {
                // In debug mode, log the status
                Log.d("Location Updates",
                        "Google Play services is available.");
                // Continue
                return true;
                // Google Play services was not available for some reason.
                // resultCode holds the error code.
            } else {
                // Get the error dialog from Google Play services
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                        resultCode,
                        mMainActivity,
                        SData.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                // If Google Play services can provide an error dialog
                if (errorDialog != null) {
                    // Create a new DialogFragment for the error dialog
                    Utils.ErrorDialogFragment errorFragment =
                            new Utils.ErrorDialogFragment();
                    // Set the dialog in the DialogFragment
                    errorFragment.setDialog(errorDialog);
                    // Show the error dialog in the DialogFragment
                    errorFragment.show(mMainActivity.getSupportFragmentManager(),
                            "Location Updates");
                }
                return false;
            }
        }

        @Override
        public void onConnected(Bundle bundle) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

         /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */

            if (connectionResult.hasResolution()) {
                try {
                    // Start an Activity that tries to resolve the error
                    connectionResult.startResolutionForResult(
                            mMainActivity,SData.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
                } catch (IntentSender.SendIntentException e) {
                    // Log the error
                    e.printStackTrace();
                }
            } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
               // showErrorDialog(connectionResult.getErrorCode());
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            bus.post(new OttoEvents.LocationUpdatesEvent(location));
        }


    }

}
