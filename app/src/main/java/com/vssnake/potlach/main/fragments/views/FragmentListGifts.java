package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.etsy.android.grid.StaggeredGridView;
import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.ListGiftsData;
import com.vssnake.potlach.main.fragments.ListGiftsAdapter;
import com.vssnake.potlach.main.fragments.presenter.GiftListPresenter;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListGifts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentListGifts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListGifts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public StaggeredGridView mGridView;
    public ListGiftsAdapter mAdapter;

    @Inject
    GiftListPresenter presenter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userEmail Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGiftView.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListGifts newInstance(String userEmail, String param2) {
        FragmentListGifts fragment = new FragmentListGifts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userEmail);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListGifts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((PotlatchApp)getActivity().getApplication()).inject(this);
    }
     int visibleThreshold = 5;
     int currentPage = 0;
     int previousTotal = 0;
     boolean loading = true;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view =  inflater.inflate(R.layout.fragment_list_gifts_view, container, false);
            presenter.attach(this);
            mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);

            init();

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mAdapter!= null){
            mAdapter.notifyDataSetChanged();
        }

    }

    public void init(){

        if (mParam1.isEmpty()){
            presenter.generateSampleData(20);
        }else{
            presenter.showGiftsUser(mParam1);
        }


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.showGift(mAdapter.getItem(position).id);
                mAdapter.getItem(position);
            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading && mParam1.isEmpty()) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        currentPage++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem +
                        visibleThreshold)) {
                    // I load the next page of gigs using a background task,
                    // but you can call any function here.
                    presenter.generateSampleData(15);
                    mAdapter.notifyDataSetChanged();
                    loading = true;
                }
            }
        });
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
