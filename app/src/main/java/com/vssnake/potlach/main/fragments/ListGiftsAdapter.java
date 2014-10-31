package com.vssnake.potlach.main.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.views.AdvancedImageView;

import java.util.List;

/**
 * Created by vssnake on 28/10/2014.
 */
public class ListGiftsAdapter extends ArrayAdapter<ListGiftsData> {

    Activity activity;
    int resource;
    List<ListGiftsData> datas;

    public ListGiftsAdapter(Activity activity, int resource, List<ListGiftsData> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.datas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DealHolder();
            holder.image = (AdvancedImageView)row.findViewById(R.id.image);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.description = (TextView)row.findViewById(R.id.description);

            row.setTag(holder);
        }
        else {
            holder = (DealHolder) row.getTag();
        }

        final ListGiftsData data = datas.get(position);

        Picasso.with(this.getContext())
                .load(data.imageUrl)
                .into(holder.image.getImage());

       // holder.image.setImageResource(R.drawable.default_image);

        holder.image.getImage().setHeightRatio(1.0);
        holder.title.setText(data.title);
        holder.description.setText(data.description);

        return row;
    }

    static class DealHolder {
        AdvancedImageView image;
        TextView title;
        TextView description;
    }
}