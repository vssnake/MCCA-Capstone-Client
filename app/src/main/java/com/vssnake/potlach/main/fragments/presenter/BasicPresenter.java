package com.vssnake.potlach.main.fragments.presenter;

import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;
import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.OttoEvents;

/**
 * Created by vssnake on 03/11/2014.
 */
public  abstract class BasicPresenter {
    public MainActivityPresenter mainActivityPresenter;

    public BasicPresenter(MainActivityPresenter mainPresenter){
        this.mainActivityPresenter = mainPresenter;
        ;
    }

    public abstract void attach(Fragment fragment);


    public MainActivityPresenter getMainPresenter(){
        return mainActivityPresenter;
    }




}
