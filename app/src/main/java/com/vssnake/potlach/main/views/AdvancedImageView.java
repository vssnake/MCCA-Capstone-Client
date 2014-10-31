package com.vssnake.potlach.main.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.vssnake.potlach.R;

/**
 * TODO: document your custom view class.
 */
public class AdvancedImageView extends RelativeLayout {

    public enum types{
        PHOTOCAPTURE,
        TITLE,

    }
    TextView mTitle;
    DynamicHeightImageView mBackImage;
    ImageView mFirstOption;
    ImageView mSecondOption;
    LinearLayout mInteractiveLayer;
    types mModeType;

    public DynamicHeightImageView getImage(){
        return mBackImage;
    }

    public AdvancedImageView(Context context){
        super(context);
        inflate(0);
        init();
    }

    public AdvancedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        int mode = a.getInt(R.styleable.AdvancedImageView_aiv_mode,0);
        inflate(mode);
        init();
        setAttributtes(context, attrs);
    }

    public AdvancedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        int mode = a.getInt(R.styleable.AdvancedImageView_aiv_mode,0);
        inflate(mode);
        init();
        setAttributtes(context, attrs);
    }

    private void inflate(int mode){


        switch (mode){
            case 1:
                inflate(getContext(), R.layout.adv_img_view_title, this);
                break;
            default:
                inflate(getContext(), R.layout.adv_img_view_photo, this);
                break;
        }
    }


    private void init() {




        mTitle = (TextView)findViewById(R.id.AIV_title);
        mBackImage =  (DynamicHeightImageView)findViewById(R.id.AIV_backImage);
        mFirstOption = (ImageView)findViewById(R.id.AIV_firstOption);
        mSecondOption =(ImageView) findViewById(R.id.AIV_secondOption);
        if (mFirstOption != null){
            mFirstOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "FirstOption", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (mSecondOption != null){
            mSecondOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Second Option",Toast.LENGTH_LONG).show();
                }
            });
        }


      /*  this.header = (TextView)findViewById(R.id.header);
        this.description = (TextView)findViewById(R.id.description);
        this.thumbnail = (ImageView)findViewById(R.id.thumbnail);
        this.icon = (ImageView)findViewById(R.id.icon);*/
    }

    private void setAttributtes(Context ctx, AttributeSet attrs){
        TypedArray a = ctx.obtainStyledAttributes(attrs,R.styleable.AdvancedImageView);
        mTitle.setText(a.getString(R.styleable.AdvancedImageView_aiv_title));
        mBackImage.setImageDrawable(a.getDrawable(R.styleable.AdvancedImageView_aiv_imagesrc));

//        mBackImage.setImageResource(a.getInteger(R.styleable.AdvancedImageView_aiv_imagesrc,0));
    }

    public void setHandlers (OnClickListener cameraListener,OnClickListener onSDCardListener){
        mFirstOption.setOnClickListener(cameraListener);
        mSecondOption.setOnClickListener(onSDCardListener);
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }
}
