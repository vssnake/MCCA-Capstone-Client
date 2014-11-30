package com.vssnake.potlach.main.fragments.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.etsy.android.grid.StaggeredGridView;
import com.vssnake.potlach.PotlatchApp;
import com.vssnake.potlach.R;
import com.vssnake.potlach.main.fragments.ListGiftsAdapter;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.presenter.GiftListPresenter;

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


    public static final String KEY_USER_MAIL = "userMail";
    public static final String KEY_GIFT_ID = "giftID";

    public StaggeredGridView mGridView;
    public ListGiftsAdapter mAdapter;

    View view;

    @Inject
    GiftListPresenter presenter;

    GiftCreatorPresenter.ChainSelected mChainCallback;

    private String mUserEmail;
    private long mGiftID;

    int visibleThreshold = 5;
    int currentPage = 0;
    int previousTotal = 0;
    public boolean loading = true;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FragmentGiftView.
     */

    public static FragmentListGifts newInstance(Bundle bundle) {
        FragmentListGifts fragment = new FragmentListGifts();
        fragment.setArguments(bundle);
        return fragment;
    }



    public FragmentListGifts() {
        // Required empty public constructor
    }

    public void setChainSelected(GiftCreatorPresenter.ChainSelected chainSelected){
        mChainCallback = chainSelected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mUserEmail = getArguments().getString(KEY_USER_MAIL,"");
            mGiftID = getArguments().getLong(KEY_GIFT_ID);
        }

        ((PotlatchApp)getActivity().getApplication()).inject(this);
        setHasOptionsMenu(true);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view =  inflater.inflate(R.layout.fragment_list_gifts_view, container, false);
            presenter.attach(this);
            mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);




        return view;
    }
    @Override
    public void onResume(){

        super.onResume();
        if (mAdapter!= null){
            mAdapter.notifyDataSetChanged();
        }
        init();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();


        if (mUserEmail.isEmpty()) {
            if (mGiftID == 0) {
                inflater.inflate(R.menu.search_gifts,menu);
                SearchView search = (SearchView)menu.findItem(R.id.action_search).getActionView();
                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        presenter.titleSearch(s);
                        return false;
                    }
                });
            }
        }


    }

    public void init(){

        if (mUserEmail.isEmpty()){
            if (mGiftID == 0){
                presenter.generateSampleData(0);
                loading = true;

                mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view,
                                         int firstVisibleItem,
                                         int visibleItemCount,
                                         int totalItemCount) {
                        if (loading && mUserEmail.isEmpty()) {
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
                            presenter.generateSampleData(totalItemCount);
                            mAdapter.notifyDataSetChanged();
                            loading = true;
                        }
                    }
                });

            }else{
                presenter.showGiftIds(mGiftID);
            }
        }else{
            presenter.showGiftsUser(mUserEmail);
        }


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mChainCallback == null){
                    View imageView = view.findViewById(R.id.image);
                    presenter.showGift(mAdapter.getItem(position).id,imageView);
                    mAdapter.getItem(position);
                }else{
                    mChainCallback.onChainSelectedCallback(mAdapter.getItem(position).id);
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
