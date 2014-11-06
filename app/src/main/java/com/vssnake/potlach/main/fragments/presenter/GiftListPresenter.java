package com.vssnake.potlach.main.fragments.presenter;

import android.support.v4.app.Fragment;

import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.ListGiftsAdapter;
import com.vssnake.potlach.main.fragments.ListGiftsData;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.model.Gift;

import java.util.ArrayList;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vssnake on 05/11/2014.
 */
public class GiftListPresenter extends BasicPresenter{

    public GiftListPresenter(MainActivityPresenter mainPresenter){
        super(mainPresenter);
        this.mainActivityPresenter = mainPresenter;
    }

    @Override
    public void attach(Fragment fragment) {

    }




    FragmentListGifts fragment;

    public void attach(FragmentListGifts fragmentListGifts){
        fragment = fragmentListGifts;
    }

    public ArrayList<ListGiftsData> listGiftData = new ArrayList<ListGiftsData>();

    public static final int  SAMPLE_DATA_ITEM_COUNT = 20;

    public void generateSampleData(final int more) {
        mainActivityPresenter.getConnInterface().showGifts("",0, new Callback<Gift[]>() {
            @Override
            public void success(Gift[] gifts, Response response) {
                int number;
                if (more == 0){number = 20;}else{number = more;}
                for (int i = 0; i < number; i++) {
                    Random ran = new Random();
                    int randomNumber = ran.nextInt(gifts.length);

                    ListGiftsData data = new ListGiftsData();
                    data.id = gifts[randomNumber].getId();
                    data.imageUrl = gifts[randomNumber].getThumbnailURL();
                    data.title = gifts[randomNumber].getTitle();
                    data.description = gifts[randomNumber].getDescription();
                    listGiftData.add(data);


                }
               // if (fragment.mAdapter == null){
                    fragment.mAdapter = new ListGiftsAdapter(fragment.getActivity(),
                            R.layout.list_gift_sample,
                            listGiftData);
                    fragment.mGridView.setAdapter(fragment.mAdapter);
               // }
                fragment.mAdapter.notifyDataSetChanged();

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }



    public void showGift(Long id){
        mainActivityPresenter.getFragmentManager().showGift(id);
    }




}
