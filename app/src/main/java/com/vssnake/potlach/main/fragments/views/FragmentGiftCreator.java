package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.views.AdvancedImageView;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentGiftCreator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentGiftCreator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGiftCreator extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Inject
    GiftCreatorPresenter presenter;


    public AdvancedImageView mAdvancedImageView;
    public EditText mTitleEdit;
    public EditText mDescriptionEdit;
    public CheckBox mChainCheckBox;
    public Button mChainButton;
    public Button mSaveButton;
    public TextView mDescriptionLeftText;
    public CardView mGiftChain;
    public TextView mLocationStatus;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentGiftCreator.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGiftCreator newInstance() {
        FragmentGiftCreator fragment = new FragmentGiftCreator();
        return fragment;
    }

    public FragmentGiftCreator() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PotlatchApp) getActivity().getApplication()).inject(this);
        presenter.attach(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_gift_creator,container,false);

        mTitleEdit =  (EditText)view.findViewById(R.id.gc_title);
        mDescriptionEdit =  (EditText)view.findViewById(R.id.gc_description);
        mDescriptionLeftText =  (TextView)view.findViewById(R.id.gc_descrip_left);
        mChainCheckBox =  (CheckBox)view.findViewById(R.id.gc_gift_chain_check);
        mChainButton = (Button) view.findViewById(R.id.gc_gift_chain_btn);
        mSaveButton = (Button) view.findViewById(R.id.gc_save_btn);
        mAdvancedImageView = (AdvancedImageView) view.findViewById(R.id.gc_photo);
        mGiftChain = (CardView) view.findViewById(R.id.gc_gift_chain);

        mLocationStatus = (TextView)view.findViewById(R.id.gc_location_updates);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(FragmentGiftCreator.this);
            }
        });

        mGiftChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.selectChain();
            }
        });


        mDescriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   mDescriptionLeftText.setText(200- mDescriptionEdit.length() +"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAdvancedImageView.setHandlers(
             new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      //Camera
                      presenter.takePhoto(getActivity());
                      }
             },
             new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                      //SD CARD
                      presenter.selectPhoto(getActivity());
                 }
        });



        mChainCheckBox.setSaveEnabled(false);

        presenter.checkChain();



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
     public void onPause(){
        super.onPause();
        presenter.disconnectLocation();
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.initLocation();
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
