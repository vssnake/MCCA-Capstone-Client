package com.vssnake.potlach.testing;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vssnake on 05/11/2014.
 */
public class Utils {

    public static void setClickAnimation(final Context context,final View view, final int colorFrom,
                                         final int colorTo){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(context.getResources().getColor(colorTo));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_HOVER_EXIT:
                    case MotionEvent.ACTION_OUTSIDE:
                        view.setBackgroundColor(context.getResources().getColor(colorFrom));

                        break;
                }

                return true;
            }

        });
    }

    public static String saveTestPhoto(Context context,Bitmap bitmap, String namePhoto){
        // Create an image file name
        File appDirectory = context.getFilesDir();
        File file = new File(appDirectory,namePhoto);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int bitmapResolution = bitmap.getWidth() -1080;
            if (bitmapResolution > 0){
                bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()-bitmapResolution,
                        bitmap.getHeight() - bitmapResolution,false);

            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      return file.getAbsolutePath();
    }


    public static long[] toPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }


    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


}
