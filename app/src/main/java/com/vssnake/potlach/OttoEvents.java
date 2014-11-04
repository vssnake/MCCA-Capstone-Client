package com.vssnake.potlach;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by vssnake on 03/11/2014.
 */
public class OttoEvents{
    public static class ActivityResultEvent{
        public int mRequestCode,mResultCode;
        public Intent mData;
        public ActionBarActivity mActivity;
        public ActivityResultEvent(int requestCode, int resultCode, Intent data,
                                   ActionBarActivity activity){
            mRequestCode = requestCode;
            mResultCode = resultCode;
            mData = data;
            mActivity = activity;
        }
    }

}