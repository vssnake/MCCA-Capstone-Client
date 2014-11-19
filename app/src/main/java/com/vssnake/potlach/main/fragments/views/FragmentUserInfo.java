package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.UserInfoPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentUserInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentUserInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserInfo extends android.support.v4.app.Fragment {
    @Inject
    UserInfoPresenter presenter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mUserEmail;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView mUserPhoto;
    private TextView mName;
    private TextView mEmail;
    private TextView mGiftCounts;
    private TextView mGiftChainCounts;
    private CheckBox mInappropriateBox;
    LinearLayout mLinearGifts;
    LinearLayout mLinearGiftChains;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userEmail Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserInfo newInstance(String userEmail, String param2) {
        FragmentUserInfo fragment = new FragmentUserInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userEmail);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentUserInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserEmail = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((PotlatchApp)getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__user_info, container, false);

        mUserPhoto = (ImageView) view.findViewById(R.id.ui_photo);
        mName = (TextView) view.findViewById(R.id.ui_name);
        mEmail = (TextView) view.findViewById(R.id.ui_email);
        mGiftCounts = (TextView) view.findViewById(R.id.ui_gift_upload_cont);
        mGiftChainCounts = (TextView) view.findViewById(R.id.ui_giftChains_cont);
        mInappropriateBox = (CheckBox) view.findViewById(R.id.ui_innappro_checkbox);
        mLinearGiftChains = (LinearLayout) view.findViewById(R.id.ui_linear_gift_chains);
        mLinearGifts = (LinearLayout) view.findViewById(R.id.ui_linear_gift_upload);

        mLinearGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showGiftsUser(mUserEmail);
            }
        });
        mLinearGiftChains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mInappropriateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.InappropriateClicked();
            }
        });
        presenter.attach(this);
        presenter.loadUserData(mUserEmail);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ImageView getUserPhoto() {
        return mUserPhoto;
    }

    public TextView getName() {
        return mName;
    }

    public TextView getEmail() {
        return mEmail;
    }

    public TextView getGiftCounts() {
        return mGiftCounts;
    }

    public TextView getGiftChainCounts() {
        return mGiftChainCounts;
    }

    public CheckBox getInappropriateBox() {
        return mInappropriateBox;
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
