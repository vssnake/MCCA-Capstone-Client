package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.GiftViewerPresenter;
import com.vssnake.potlach.main.views.AdvancedImageView;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Long mGiftID;
    private String mParam2;

    public AdvancedImageView mPhoto;
    public TextView mTitle;
    public TextView mDescription;
    public TextView mUserData;
    public TextView mGiftChainCount;
    public LinearLayout mGiftChain;

    private OnFragmentInteractionListener mListener;

    @Inject
    GiftViewerPresenter presenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGiftViewer.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGiftViewer newInstance(Long param1, String param2) {
        FragmentGiftViewer fragment = new FragmentGiftViewer();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentGiftViewer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGiftID = getArguments().getLong(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
