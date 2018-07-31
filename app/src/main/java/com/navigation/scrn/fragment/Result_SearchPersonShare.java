package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.ShareProfileAdapter;
import com.Model.Gen_Response_Model;
import com.Model.ResponseModel;
import com.Model.SearchPersonResponseShare;
import com.Model.SearchPersonToShare;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
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
    String pet_id, srchTxt,pet_name;
    boolean isMobile = false;
    UserModel userModelObj;
    TextView txt_emty_msg;
    SimpleDraweeView img_empty;
    RelativeLayout rslt_contr,empty_contr;
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
        if (context instanceof InterfaceUserModel) {
            userModelObj = ((InterfaceUserModel) context).getUserModel();

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_res_srch_pers_to_share, container, false);
        recycle_res_to_share_list = (RecyclerView) rootView.findViewById(R.id.recycle_res_to_share_list);
        rslt_contr=(RelativeLayout)rootView.findViewById(R.id.rslt_contr);
        empty_contr=(RelativeLayout)rootView.findViewById(R.id.empty_contr);
        txt_emty_msg=(TextView)rootView.findViewById(R.id.txt_emty_msg);
        img_empty=(SimpleDraweeView)rootView.findViewById(R.id.img_empty);
        Bundle bundle = getArguments();
        searchPersonToSharesList = bundle.getParcelableArrayList("search_per_obj");
        pet_id = bundle.getString("pet_id");
        srchTxt = bundle.getString("srchTxt");
        isMobile = bundle.getBoolean("isMobile");
        pet_name= bundle.getString("pet_name");
        boolean isEmpty_Person=bundle.getBoolean("isEmpty");
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
        if(!isEmpty_Person) {
            recycle_res_to_share_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            //recycle_res_to_share_list.setAdapter(adapter);
            setSearchAdapter();
            rslt_contr.setVisibility(View.VISIBLE);
            empty_contr.setVisibility(View.GONE);
        }
        else {
            rslt_contr.setVisibility(View.GONE);
            empty_contr.setVisibility(View.VISIBLE);
            txt_emty_msg.setText(searchPersonToSharesList.get(0).getResult());
            ResizeOptions resizeOptions=new ResizeOptions(200,154);
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogcat_grey))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            img_empty.setImageRequest(imageRequest2);

        }
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


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "RSLT_SRCH_STATE", this);super.onSaveInstanceState(outState);
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
    public void onShareClick(String usrId, String usrType,String usrMobile) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface._to_share_profile(pet_id, userModelObj.getOwner_id(), userModelObj.getOwner_name(),pet_name,usrId, usrType,usrMobile)
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
            mCompositeDisposable.add(requestInterface._search_parent_business(userModelObj.getOwner_name(),pet_name,Name, userModelObj.getOwner_id(), pet_id, Mobile)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<SearchPersonResponseShare>() {
                        @Override
                        public void onNext(SearchPersonResponseShare gen_response_model) {
                            dialog.dismiss();
                            SearchPersonToShare searchPersonToShare = gen_response_model.getResponse().get(0);
                            if (searchPersonToShare.getMobile().equalsIgnoreCase("empty")) {
                                recycle_res_to_share_list.removeAllViews();
                                //Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
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

