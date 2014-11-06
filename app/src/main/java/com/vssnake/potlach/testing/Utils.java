package com.vssnake.potlach.testing;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

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
}
