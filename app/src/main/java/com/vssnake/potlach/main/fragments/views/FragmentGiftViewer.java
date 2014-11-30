package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.GiftViewerPresenter;
import com.vssnake.potlach.main.views.AdvancedImageView;
import com.vssnake.potlach.testing.Utils;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentGiftViewer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentGiftViewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGiftViewer extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String KEY_GIFT_ID = "GiftID";

    // TODO: Rename and change types of parameters
    private Long mGiftID;
    private String mTransitionName;

    public AdvancedImageView mPhoto;
    public TextView mTitle;
    public TextView mDescription;
    public TextView mUserData;
    public TextView mGiftChainCount;
    public LinearLayout mGiftChain;
    public LinearLayout mGpsPosition;
    public Button mDeleteButton;

    private OnFragmentInteractionListener mListener;

    @Inject
    GiftViewerPresenter presenter;


    /**
     *
     * @param bundle
     * @return
     */
    public static FragmentGiftViewer newInstance(Bundle bundle) {
        FragmentGiftViewer fragment = new FragmentGiftViewer();

        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentGiftViewer() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGiftID = getArguments().getLong(KEY_GIFT_ID);
            //mTransitionName = getArguments().getString(ARG_PARAM2);
        }

        ((PotlatchApp)getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gift_viewer, container, false);

        mPhoto = (AdvancedImageView)view.findViewById(R.id.gv_photo);
        mTitle =(TextView) view.findViewById(R.id.gv_title);
        mDescription =(TextView) view.findViewById(R.id.gv_description);
        mUserData= (TextView)view.findViewById(R.id.gv_userData);
        mGiftChain = (LinearLayout)view.findViewById(R.id.gv_giftChain);
        mGiftChainCount = (TextView)view.findViewById(R.id.gv_giftChainCount);
        mDeleteButton = (Button)view.findViewById(R.id.gv_delete_gift);

        mGpsPosition = (LinearLayout) view.findViewById(R.id.gv_photo_location);

        mPhoto.setHandlers(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pushLike();
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pushObscene();
            }
        });

        mUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.userDataClicked();
            }
        });

        mGiftChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.chainClicked();
            }
        });

        mGpsPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.launchGoogleMaps();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pushDelete();
            }
        });

        Utils.setClickAnimation(getActivity(), mGiftChain, R.color.transparent,
                R.color.link_text_material_light);

        Utils.setClickAnimation(getActivity(), mUserData,mUserData.getBackground() ,
                R.color.link_text_material_light);

        Utils.setClickAnimation(getActivity(), mGpsPosition,mGpsPosition.getBackground(),
                R.color.link_text_material_light);


        mPhoto.setTransitionName(mTransitionName);
        presenter.attach(this);
        presenter.showGift(mGiftID);
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
       /* try {
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
