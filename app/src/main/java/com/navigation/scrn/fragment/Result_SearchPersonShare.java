package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.ShareProfileAdapter;
import com.Model.Gen_Response_Model;
import com.Model.ResponseModel;
import com.Model.SearchPersonResponseShare;
import com.Model.SearchPersonToShare;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.util.ActionBarUtil;
import com.util.AlertDialogHandler;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.R;

public class Result_SearchPersonShare extends Fragment implements ShareProfileAdapter.shareClickInterface {
    View rootView;
    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main genFragmentCall_main;
    RecyclerView recycle_res_to_share_list;
    String pet_id, ownerId, srchTxt;
    boolean isMobile = false;
    List<SearchPersonToShare> searchPersonToSharesList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentCallInterface){
            genFragmentCall_main=((FragmentCallInterface)context).Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_res_srch_pers_to_share, container, false);
        recycle_res_to_share_list = (RecyclerView) rootView.findViewById(R.id.recycle_res_to_share_list);
        Bundle bundle = getArguments();
        searchPersonToSharesList = bundle.getParcelableArrayList("search_per_obj");
        pet_id = bundle.getString("pet_id");
        ownerId = bundle.getString("owner_id");
        srchTxt = bundle.getString("srchTxt");
        isMobile = bundle.getBoolean("isMobile");
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Back");
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        //  ShareProfileAdapter adapter=new ShareProfileAdapter(searchPersonToSharesList,getContext(),this);
        recycle_res_to_share_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //recycle_res_to_share_list.setAdapter(adapter);
        setSearchAdapter();
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "RSLT_SRCH_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "res_per_to_share");


            fragmentTransaction.addToBackStack("res_per_to_share");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "RSLT_SRCH_STATE", this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialogHandler._fragment_handelBackKey(rootView,getActivity(), "", false);
    }

    public void setSearchAdapter() {
        ShareProfileAdapter adapter = new ShareProfileAdapter(searchPersonToSharesList, getContext(), this);
        //  recycle_res_to_share_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycle_res_to_share_list.setAdapter(adapter);

    }

    @Override
    public void onShareClick(String usrId, String usrType) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface._to_share_profile(pet_id, ownerId, usrId, usrType)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();
                            _call_search_person(srchTxt, isMobile);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                        }
                    }));
        } else {
            Toast.makeText(getContext(), getActivity().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }

    public void _call_search_person(String srchTxt, boolean isMobile) {
        if(NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            String Name, Mobile;
            if (isMobile) {
                Name = "";
                Mobile = srchTxt;
            } else {
                Mobile = "";
                Name = srchTxt;
            }
            mCompositeDisposable.add(requestInterface._search_parent_business(Name, ownerId, pet_id, Mobile)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<SearchPersonResponseShare>() {
                        @Override
                        public void onNext(SearchPersonResponseShare gen_response_model) {
                            dialog.dismiss();
                            SearchPersonToShare searchPersonToShare = gen_response_model.getResponse().get(0);
                            if (searchPersonToShare.getMobile().equalsIgnoreCase("empty")) {
                                recycle_res_to_share_list.removeAllViews();
                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
                             //   genFragmentCall_main.Fragment_call(new Shared_Profile_Details(),"shrPro",null);
                                ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();

                            } else {
                                searchPersonToSharesList = gen_response_model.getResponse();
                                setSearchAdapter();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                        }
                    }));
        }
        else{
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.net_con),Toast.LENGTH_LONG).show();
        }
    }
}

