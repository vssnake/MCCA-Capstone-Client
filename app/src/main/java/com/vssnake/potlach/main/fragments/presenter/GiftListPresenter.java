package com.vssnake.potlach.main.fragments.presenter;

import android.support.v4.app.Fragment;

import com.vssnake.potlach.MainActivityPresenter;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.ConnectionManager;
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

        mainActivityPresenter.getConnInterface().showGifts(0,new ConnectionManager.ReturnGiftsHandler() {
            @Override
            public void onReturnGifts(Gift[] gifts) {
                int number;
                if (more == 0) {
                    listGiftData.clear();
                    number = 20;
                } else {
                    number = more;
                }
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
            public void onError(String error) {

            }
        });

    }



    public void showGift(Long id){
        mainActivityPresenter.getFragmentManager().showGift(id);
    }


    public void showGiftsUser(String userEmail){
        mainActivityPresenter.getConnInterface().showUserGifts(userEmail,new ConnectionManager.ReturnGiftsHandler() {
            @Override
            public void onReturnGifts(Gift[] gifts) {
                listGiftData.clear();
                for (int i = 0; i < gifts.length; i++) {


                    ListGiftsData data = new ListGiftsData();
                    data.id = gifts[i].getId();
                    data.imageUrl = gifts[i].getThumbnailURL();
                    data.title = gifts[i].getTitle();
                    data.description = gifts[i].getDescription();
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
            public void onError(String error) {

            }
        });

    }

}
