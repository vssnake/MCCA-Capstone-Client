package com.vssnake.potlach.manager;

import android.app.Application;
import android.content.Context;

import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import comunication.ComInterface;

/**
 * Created by vssnake on 24/10/2014.
 */
public class RestManager {
    Map<Long,Gift> mCachedGifts;
    Map<String,User> mCachedUsers;
    Map<String,SpecialInfo> mCacheSpecialInfo;


    @Inject
    ComInterface mComunicationInterface;

    public RestManager(PotlatchApp app){
       app.inject(this);

    }

    public void showUser(String email){


    }

}
