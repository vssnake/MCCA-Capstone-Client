package com.vssnake.potlach.main.fragments.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.OttoEvents;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.GiftCreator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vssnake on 03/11/2014.
 */
public class GiftCreatorPresenter extends BasicPresenter {

    public static final String TAG = "GiftCreatorPresenter";
    FragmentGiftCreator mFragment;
    GiftCreator mGift;

    public GiftCreatorPresenter(MainActivityPresenter mainPresenter) {
        super(mainPresenter);
        MainActivityPresenter.bus.register(this);
        mGift = new GiftCreator();

    }




    public void initLocation(){
        getMainPresenter().getLocationManager().connect(); //Connect Location Manager
        mFragment.mLocationStatus.setText(R.string.getting_location);
    }
    public void disconnectLocation(){
        getMainPresenter().getLocationManager().disconnect();
    }


    public void attach(FragmentGiftCreator fragment){
        mFragment = fragment;

    }
    public void detach(){
        mFragment = null;

    }



    @Subscribe
    public void onActivityResult(OttoEvents.ActivityResultEvent activityResultEvent) {
        switch (activityResultEvent.mRequestCode){
            case SData.REQUEST_CODE_TAKE_PHOTO_CAMERA:
                if (activityResultEvent.mResultCode == Activity.RESULT_OK){


                    if (mFragment!= null){
                        mFragment.mAdvancedImageView.setPhoto(mainActivityPresenter
                                .getFileManager().getTemporaryImageFile());
                        mGift.setImage(mainActivityPresenter
                                .getFileManager().getTemporaryImageFile().getAbsolutePath());
                    }



                }
                break;
            case SData.REQUEST_CODE_TAKE_PHOTO_SD:
                if (activityResultEvent.mResultCode == Activity.RESULT_OK) {
                    Uri selectedImage = activityResultEvent.mData.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = mainActivityPresenter.getContext().getContentResolver()
                            .query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();



                    if (mFragment!= null){
                        mFragment.mAdvancedImageView.setPhoto(filePath);
                        mGift.setImage(filePath);
                    }
                }
                break;
        }
    }

    @Subscribe
    public void onRequestUpdateLocation(OttoEvents.LocationUpdatesEvent locationUpdatesEvent){
        Log.d("TAG","onRequestUpdateLocation Latitude" + locationUpdatesEvent.mLocation.getLatitude
                () +
        " Longitude " + locationUpdatesEvent.mLocation.getLongitude() +
        " Accuracy " + locationUpdatesEvent.mLocation.getAccuracy());
        mFragment.mLocationStatus.setText(R.string.location_found);

        mGift.setLatitude(locationUpdatesEvent.mLocation.getLatitude());
        mGift.setLongitude(locationUpdatesEvent.mLocation.getLongitude());
        mGift.setPrecision(locationUpdatesEvent.mLocation.getAccuracy());
    }

    private boolean  createDirectory() throws IOException {

        File file = mainActivityPresenter.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        if (file.mkdirs()){
           return true;
        }
        return false;
    }



    public void save(FragmentGiftCreator fragment){
        if (checkValues(fragment)){
            mGift.setTitle(fragment.mTitleEdit.getText().toString());
            mGift.setDescription(fragment.mDescriptionEdit.getText().toString());
            getMainPresenter().getConnInterface().createGift(mGift,new ConnectionManager.ReturnGiftHandler() {
                @Override
                public void onReturnHandler(Gift gift) {
                    getMainPresenter().getFragmentManager().showDefaultView();
                }

                @Override
                public void onError(String error) {

                }
            });

        }
    }

    public void selectChain(){
        mainActivityPresenter.getFragmentManager().selectGiftChain(new ChainSelected() {
            @Override
            public void onChainSelectedCallback(Long idGift) {
                Log.d(TAG,"onSelectChain | ChainID=" + idGift);
                mGift.setChainID(idGift);
            }
        });
        mGift.setChainID(null);
    }

    public void checkChain(){
        if (mGift.getChainID()!= null){
            mFragment.mChainCheckBox.setChecked(true);
        }else{
            mFragment.mChainCheckBox.setChecked(false);
        }
    }

    public void takePhoto(FragmentActivity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                createDirectory();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mainActivityPresenter.getFileManager().getTemporaryImageFile
                                ()));
                activity.startActivityForResult(takePictureIntent,
                        SData.REQUEST_CODE_TAKE_PHOTO_CAMERA);
            } catch (IOException e) {
                Log.e(TAG,"onTakePhoto | Error creating image");
                e.printStackTrace();
            }

        }
    }

    public void selectPhoto(FragmentActivity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SData.REQUEST_CODE_TAKE_PHOTO_SD);
    }

    private boolean checkValues(FragmentGiftCreator fragment){
        boolean valid = true;
        if(fragment.mTitleEdit.getText().toString().trim().length() < 1){
            fragment.mTitleEdit.setError("Title not should be blank");
            valid = false;
        }else{
            fragment.mTitleEdit.setError(null);
        }
        if (!fragment.mAdvancedImageView.hasPhoto()){
            valid = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
            builder.setMessage(R.string.alert_need_photo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }

    public interface ChainSelected{
        void onChainSelectedCallback(Long idGift);
    }

    @Override
    public void attach(Fragment fragment) {

    }

}
