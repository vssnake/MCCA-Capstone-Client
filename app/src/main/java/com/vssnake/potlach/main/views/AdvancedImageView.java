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

import com.vssnake.potlach.R;

/**
 * TODO: document your custom view class.
 */
public class AdvancedImageView extends RelativeLayout {

    TextView mTitle;
    ImageView mBackImage;
    ImageView mFirstOption;
    ImageView mSecondOption;
    LinearLayout mInteractiveLayer;

    public AdvancedImageView(Context context){
        super(context);
        init();
    }

    public AdvancedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setAttributtes(context, attrs);
    }

    public AdvancedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setAttributtes(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.sample_custom_view, this);

        mTitle = (TextView)findViewById(R.id.AIV_title);
        mBackImage =  (ImageView)findViewById(R.id.AIV_backImage);
        mFirstOption = (ImageView)findViewById(R.id.AIV_firstOption);
        mSecondOption =(ImageView) findViewById(R.id.AIV_secondOption);

        mFirstOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "FirstOption", Toast.LENGTH_LONG).show();
            }
        });
        mSecondOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Second Option",Toast.LENGTH_LONG).show();
            }
        });

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
