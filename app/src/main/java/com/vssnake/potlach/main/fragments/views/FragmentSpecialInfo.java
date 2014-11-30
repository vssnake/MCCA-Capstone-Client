package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.SpecialInfoPresenter;
import com.vssnake.potlach.testing.Utils;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSpecialInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSpecialInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSpecialInfo extends android.support.v4.app.Fragment {


    @Inject
    SpecialInfoPresenter presenter;

    public ImageView mGift1;
    public ImageView mGift2;
    public ImageView mGift3;
    public TextView mUser1;
    public TextView mUser2;
    public TextView mUser3;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentSpecialInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSpecialInfo newInstance() {
        FragmentSpecialInfo fragment = new FragmentSpecialInfo();
        return fragment;
    }

    public FragmentSpecialInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PotlatchApp)getActivity().getApplication()).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_info, container, false);

        mGift1= (ImageView)view.findViewById(R.id.sp_1_gift);
        mGift2= (ImageView)view.findViewById(R.id.sp_2_gift);
        mGift3= (ImageView)view.findViewById(R.id.sp_3_gift);

        mUser1 = (TextView)view.findViewById(R.id.sp_1_user);
        mUser2 = (TextView)view.findViewById(R.id.sp_2_user);
        mUser3 = (TextView)view.findViewById(R.id.sp_3_user);


        Utils.setClickAnimation(getActivity(), mUser1, R.color.transparent,
                R.color.colorPrimary_dark);
        Utils.setClickAnimation(getActivity(), mUser2, R.color.transparent,
                R.color.colorPrimary_dark);
        Utils.setClickAnimation(getActivity(), mUser3, R.color.transparent,
                R.color.colorPrimary_dark);

        mGift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.giftClicked(1);
            }
        });

        mGift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.giftClicked(2);
            }
        });

        mGift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.giftClicked(3);
            }
        });

        mUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.userClicked(1);
            }
        });

        mUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.userClicked(2);
            }
        });

        mUser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.userClicked(3);
            }
        });
        presenter.attach(this);
        presenter.init();
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
