package com.vssnake.potlach;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.otto.Bus;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.FileManager;
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.ViewManager;
import com.vssnake.potlach.testing.Utils;

import java.io.IOException;

import javax.inject.Singleton;

import comunication.EasyHttpClient;
import comunication.LocalComunication;
import comunication.RetrofitInterface;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

/**
 * Created by vssnake on 24/10/2014.
 */
@Singleton
public class MainActivityPresenter {

    private static final String TAG="MainPresenter";

    public static Bus  bus = new Bus();

    private Context mContext;

    //GCM Key
    private static final String SENDER_ID = "230668116470";
    String regid;
    GoogleCloudMessaging gcm;

    ConnectionManager mComunicationInterface;
    private FileManager mFileManager;

    private ViewManager mFragmentManager;
    private LocationManager mLocationManager;

    private MainActivity mMainActivity;

    public MainActivityPresenter(PotlatchApp app,ConnectionManager comInterface){
        mContext = app.getApplicationContext();
        mComunicationInterface = comInterface;
        mFileManager = new FileManager(app.getApplicationContext());
        mLocationManager = new LocationManager();



    }

    public void attach(MainActivity main){
        mMainActivity = main;
        mFragmentManager = new ViewManager(this);
        mComunicationInterface.setPresenter(this);

        if (Utils.checkPlayServices(mMainActivity)) {
            gcm = GoogleCloudMessaging.getInstance(mContext);
            regid = Utils.getRegistrationId(mContext, mMainActivity);

            if (regid.isEmpty()) {
                registerInBackground();
            }else{
                mComunicationInterface.setGCMKey(regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

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
            mFragmentManager.launchFragment(ViewManager.SHOW_LIST_GIFTS,new Bundle(),false);
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

    public ViewManager getFragmentManager() {
        return mFragmentManager;
    }

    public FileManager getFileManager() {
        return mFileManager;
    }

    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    public MainActivity getMainActivity() {
        return mMainActivity;
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

    public void setLoadingView(final Boolean visible){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visible){
                    getMainActivity().mLoaderLayout.setVisibility(View.VISIBLE);
                }else{
                    getMainActivity().mLoaderLayout.setVisibility(View.INVISIBLE);
                }
            }
        });


    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    mComunicationInterface.setGCMKey(regid);

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    Utils.storeRegistrationId(mContext, mMainActivity, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    public void changeConnectionManager(boolean local){
            if (local){
                mComunicationInterface = new ConnectionManager(new LocalComunication(mMainActivity
                        .getApplicationContext()),mMainActivity
                        .getApplicationContext());


            }else{
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("https://192.168.1.108:9993")
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new ApacheClient(new EasyHttpClient()))
                        .build();

                RetrofitInterface service = restAdapter.create(RetrofitInterface.class);
                mComunicationInterface = new ConnectionManager(service,
                        mMainActivity.getApplicationContext());
            }
        mComunicationInterface.setPresenter(this);
    }
}
