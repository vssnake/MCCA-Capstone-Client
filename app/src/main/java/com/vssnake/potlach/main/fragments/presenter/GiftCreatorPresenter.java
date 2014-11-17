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
import com.vssnake.potlach.main.SData;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.model.GiftCreator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vssnake on 03/11/2014.
 */
public class GiftCreatorPresenter extends BasicPresenter {

    public GiftCreatorPresenter(MainActivityPresenter mainPresenter) {
        super(mainPresenter);
        MainActivityPresenter.bus.register(this);
        mGift = new GiftCreator();

    }

    public static final String TAG = "GiftCreatorPresenter";

    @Override
    public void attach(Fragment fragment) {

    }

    FragmentGiftCreator mFragment;
    GiftCreator mGift;
    public void attach(FragmentGiftCreator fragment){
        mFragment = fragment;
    }
    public void detach(){
        mFragment = null;
    }

    File imageFile;

    @Subscribe
    public void onActivityResult(OttoEvents.ActivityResultEvent activityResultEvent) {
        switch (activityResultEvent.mRequestCode){
            case SData.REQUEST_CODE_TAKE_PHOTO_CAMERA:
                if (activityResultEvent.mResultCode == Activity.RESULT_OK){


                    if (mFragment!= null && imageFile != null){
                        mFragment.mAdvancedImageView.setPhoto(imageFile);
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


                    Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);
                    if (mFragment!= null){
                        mFragment.mAdvancedImageView.setPhoto(imageBitmap);
                    }
                }
                break;
        }
    }

    private boolean  createDirectory() throws IOException {

        File file = mainActivityPresenter.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        if (file.mkdirs()){
           return true;
        }
        return false;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;

        imageFile = new File(mainActivityPresenter.getContext().getExternalFilesDir(Environment
                .DIRECTORY_PICTURES) + "/"+ imageFileName + ".jpg");



        return imageFile;
    }

    public void save(FragmentGiftCreator fragment){
        checkValues(fragment);
    }

    public void selectChain(){}

    public void takePhoto(FragmentActivity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                createDirectory();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(createImageFile()));
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


}
