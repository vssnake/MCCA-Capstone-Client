package com.vssnake.potlach.main.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.vssnake.potlach.R;

import java.io.File;

/**
 * TODO: document your custom view class.
 */
public class AdvancedImageView extends RelativeLayout {

    public ImageView getFirstOption() {
        return mFirstOption;
    }
    public ImageView getSecondOption(){
        return mSecondOption;
    }

    public ImageView getmSecondOption() {
        return mSecondOption;
    }

    public static final int PHOTOCAPTURE = 0;
    public static final int TITLE = 1;
    public static final int EMPTY = 2;
    public enum types{
        PHOTOCAPTURE,
        TITLE,
        EMPTY

    }
    TextView mTitle;
    DynamicHeightImageView mBackImage;
    private ImageView mFirstOption;
    private ImageView mSecondOption;
    TextView mFirstOptionCount;
    LinearLayout mFirstOptionLayout;
    types mModeType;


    String mImagePath;

    public DynamicHeightImageView getImage(){
        return mBackImage;
    }

    public AdvancedImageView(Context context){
        super(context);
        inflate(0);
        init(0);
    }

    public AdvancedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        int mode = a.getInt(R.styleable.AdvancedImageView_aiv_mode,0);

        inflate(mode);
        init(mode);
        setAttributtes(context, attrs);
    }

    public AdvancedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        int mode = a.getInt(R.styleable.AdvancedImageView_aiv_mode,0);
        inflate(mode);

        init(mode);
        setAttributtes(context, attrs);
    }

    private void inflate(int mode){


        switch (mode){
            case 1:
                mModeType = types.TITLE;
            case 2:
                inflate(getContext(), R.layout.adv_img_view_title, this);
                break;
            default:
                mModeType = types.PHOTOCAPTURE;
                inflate(getContext(), R.layout.adv_img_view_photo, this);
                break;
        }
    }


    private void init(int mode) {



        mTitle = (TextView)findViewById(R.id.AIV_title);
        mBackImage =  (DynamicHeightImageView)findViewById(R.id.AIV_backImage);
        mFirstOption = (ImageView)findViewById(R.id.AIV_firstOption);
        mFirstOptionCount = (TextView) findViewById(R.id.AIV_firstOptionCount);
        mSecondOption =(ImageView) findViewById(R.id.AIV_secondOption);
        mFirstOptionLayout = (LinearLayout) findViewById(R.id.AIV_firstOptionLayout);
        if (getFirstOption() != null){
            getFirstOption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "FirstOption", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (getmSecondOption() != null){
            getmSecondOption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Second Option", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (mode == 2){
            mFirstOption.setVisibility(View.INVISIBLE);
            mFirstOptionLayout.setVisibility(View.INVISIBLE);
            mSecondOption.setVisibility(View.INVISIBLE);

        }

    }

    private void setAttributtes(Context ctx, AttributeSet attrs){
        TypedArray a = ctx.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        mTitle.setText(a.getString(R.styleable.AdvancedImageView_aiv_title));
        mBackImage.setImageDrawable(a.getDrawable(R.styleable.AdvancedImageView_aiv_imagesrc));
    }

    public void setHandlers (OnClickListener cameraListener,OnClickListener onSDCardListener){
        switch (mModeType) {
            case PHOTOCAPTURE:
                getFirstOption().setOnClickListener(cameraListener);
                break;
            case TITLE:
            case EMPTY:
                getFirstOption().setOnClickListener(null);
                mFirstOptionLayout.setClickable(true);
                mFirstOptionLayout.setOnClickListener(cameraListener);

                getFirstOption().setClickable(false);
                mFirstOptionCount.setClickable(false);

                break;
        }
        getmSecondOption().setOnClickListener(onSDCardListener);


    }

    public void setSecondOptionCounts(Long counts){
        if (mFirstOptionCount != null) mFirstOptionCount.setText(counts +"");
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setPhoto(File file){
        mImagePath = file.getPath();
        mBackImage.setImageURI(Uri.parse(mImagePath));
    }


    public void setPhoto(String filePath){
        mImagePath = filePath;
        mBackImage.setImageURI(Uri.parse(mImagePath));
    }
    public boolean hasPhoto(){
        if (mBackImage.getDrawable() == null){
            return false;
        }
        return true;
    }



    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.stateToSave;
        ss.imageUri = this.mImagePath;

        return ss;
    }

    private int stateToSave;

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.stateToSave = ss.stateToSave;
        this.mImagePath = ss.imageUri;
        if (mImagePath!= null){
            getImage().setImageURI(Uri.parse(mImagePath));
        }

    }

    static class SavedState extends BaseSavedState {
        int stateToSave;
        String imageUri;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
            this.imageUri = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
            out.writeString(this.imageUri);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
