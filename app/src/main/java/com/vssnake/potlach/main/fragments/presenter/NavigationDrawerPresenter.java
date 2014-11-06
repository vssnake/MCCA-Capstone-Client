package com.vssnake.potlach.main.fragments.presenter;

import com.vssnake.potlach.MainActivityPresenter;

import javax.inject.Singleton;

/**
 * Created by vssnake on 05/11/2014.
 */
@Singleton
public class NavigationDrawerPresenter {


    MainActivityPresenter mMainPresenter;

    public NavigationDrawerPresenter(MainActivityPresenter mainPresenter){
        this.mMainPresenter = mainPresenter;
    }

    public void clickCreate(){}

    public void clickList(){}

    public void clickLogout(){}
}
