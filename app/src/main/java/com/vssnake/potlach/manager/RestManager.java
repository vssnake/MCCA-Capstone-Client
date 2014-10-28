package com.vssnake.potlach.manager;

import com.vssnake.potlach.model.Gift;
import com.vssnake.potlach.model.SpecialInfo;
import com.vssnake.potlach.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vssnake on 24/10/2014.
 */
public class RestManager {



    private class ModelCache{

        Map<Integer,Gift> mCachedGifts;
        Map<String,User> mCachedUsers;
        Map<Integer,SpecialInfo> mCacheSpecialInfo;

        public ModelCache(){
            mCachedGifts = new HashMap<Integer, Gift>();
            mCachedUsers = new HashMap<String,User>();
            mCacheSpecialInfo = new HashMap<Integer,SpecialInfo>();
        }

        boolean userExist(String email){
            //TODO
            return false;
        }
        boolean GiftExist(int id){
            //TODO
            return false;
        }
        boolean specialInfoExist(int id){
            //TODO
            return false;
        }





    }
}
