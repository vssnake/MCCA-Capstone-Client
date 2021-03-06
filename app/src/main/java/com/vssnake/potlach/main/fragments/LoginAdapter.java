package com.vssnake.potlach.main.fragments;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vssnake.potlach.R;
import com.vssnake.potlach.testing.Utils;

/**
 * Created by vssnake on 29/10/2014.
 */
public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.ViewHolder>{

    private String[] mDataset;
    Activity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View v){
            super(v);
            mTextView = (TextView)v.findViewById(R.id.lu_name_account);
        }
    }

    public LoginAdapter(String[] myDataSet,Activity activity){
        mDataset = myDataSet;
        this.mActivity = activity;
    }

    @Override
    public LoginAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user,parent,false);
        Utils.setClickAnimation(mActivity, v, R.color.transparent,
                R.color.link_text_material_dark);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
