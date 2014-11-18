package com.vssnake.potlach.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vssnake on 18/11/2014.
 */
public class FileManager {

    Context mContext;
    public FileManager(Context context){
        mContext = context;
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getTemporaryImageFile(){

        File potlachBase = new File(Environment.getExternalStorageDirectory() + "/Potlach/");

        potlachBase.mkdirs();

        File imageFile = new File(potlachBase,"tempPicture.jpg");



        return imageFile;
    }

    public static File setTempImageThumb(Bitmap bitmap){
        File potlachBase = new File(Environment.getExternalStorageDirectory() + "/Potlach/");

        potlachBase.mkdirs();


        File file = new File(potlachBase,"tempPictureThumb.jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



}
