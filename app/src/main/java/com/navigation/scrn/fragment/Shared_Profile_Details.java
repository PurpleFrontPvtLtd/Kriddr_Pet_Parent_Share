package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.ShareProfileAdapter;
import com.Adapter.SharedListAdapter;
import com.Model.ResponseModel;
import com.Model.SearchPersonResponseShare;
import com.Model.SearchPersonToShare;
import com.Model.SharedListDetailModel;
import com.Model.SharedListModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
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

public class Shared_Profile_Details extends Fragment implements SharedListAdapter.delete_clicked_Interface {
    View rootView;
    EditText edtName, edtMobNo;
    Button btnSearch;
    TextView txtShrdPetName,txtNoShare;
    String pet_id;
    RecyclerView recycle_shrdWith;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    boolean isNameChanged = false, isMobChanged = false;
    boolean isMobile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_profile_share_detail, container, false);
        edtName = (EditText) rootView.findViewById(R.id.edtName);
        edtMobNo = (EditText) rootView.findViewById(R.id.edtMob_No);
        recycle_shrdWith = (RecyclerView) rootView.findViewById(R.id.recycle_shrdWith);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        txtNoShare=(TextView)rootView.findViewById(R.id.txtNoShare);
        txtShrdPetName = (TextView) rootView.findViewById(R.id.txtShrdPetName);

        Bundle bundle = getArguments();
        pet_id = bundle.getString("pet_id");
        String petName = bundle.getString("petName");
        txtShrdPetName.setText("Share " + petName + "'s Profile");
        actionBarUtilObj.setViewInvisible();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtMobNo.getText().toString().equalsIgnoreCase("")|| !edtName.getText().toString().equalsIgnoreCase("")) {
                    if (isMobile) {
                        _call_search_person(edtMobNo.getText().toString(), true);
                    } else {
                        _call_search_person(edtName.getText().toString(), false);
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Please enter name/mobile to search",Toast.LENGTH_LONG).show();
                }
            }
        });
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edtName.getText().toString().equalsIgnoreCase("")) {

                } else {
                    isMobile = false;
                    edtMobNo.setText("");
                }

            }
        });
        edtMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtMobNo.getText().toString().equalsIgnoreCase("")) {

                } else {
                    isMobile = true;
                    edtName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recycle_shrdWith.setLayoutManager(mlayoutManager);
        recycle_shrdWith.setNestedScrollingEnabled(false);
        recycle_shrdWith.setHasFixedSize(true);
        _shared_list();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "SHARE_PROF_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "shar_prof_dtl");


            fragmentTransaction.addToBackStack("shar_prof_dtl");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "SHARE_PROF_STATE", this);
    }


    public void _shared_list() {
        if(NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface._shared_list(pet_id, userModel.getOwner_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<SharedListModel>() {
                        @Override
                        public void onNext(SharedListModel gen_response_model) {
                            dialog.dismiss();
                            if (gen_response_model.getDetails().get(0).getMobile().equalsIgnoreCase("empty")) {
                            txtNoShare.setVisibility(View.VISIBLE);
                            recycle_shrdWith.setVisibility(View.GONE);
                            } else {
                                txtNoShare.setVisibility(View.GONE);
                                recycle_shrdWith.setVisibility(View.VISIBLE);
                                SetSharedAdapter(gen_response_model.getDetails());
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
        else {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.net_con),Toast.LENGTH_LONG).show();
        }

    }

    public void SetSharedAdapter(List<SharedListDetailModel> sharedListDetailModelList) {
        SharedListAdapter adapter = new SharedListAdapter(sharedListDetailModelList, getContext(), this);
        recycle_shrdWith.setAdapter(adapter);
    }

    @Override
    public void on_deleteSharedList(String ShareId) {
        if(NetworkConnection.isOnline(getContext())) {


            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface._del_shared_contact(userModel.getOwner_id(), ShareId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ResponseModel>() {
                        @Override
                        public void onNext(ResponseModel gen_response_model) {
                            dialog.dismiss();
                            _shared_list();
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
        else {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.net_con),Toast.LENGTH_LONG).show();
        }

    }

    public void _call_search_person(final String srchTxt, final boolean isMobile) {
        if(NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            String Name = "", Mobile = "";
            if (isMobile) {
                Mobile = srchTxt;
            } else {
                Name = srchTxt;

            }
            mCompositeDisposable.add(requestInterface._search_parent_business(Name, userModel.getOwner_id(), pet_id, Mobile)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<SearchPersonResponseShare>() {
                        @Override
                        public void onNext(SearchPersonResponseShare gen_response_model) {
                            dialog.dismiss();
                            SearchPersonToShare searchPersonToShare = gen_response_model.getResponse().get(0);
                            if (searchPersonToShare.getMobile().equalsIgnoreCase("empty")) {
                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("pet_id", pet_id);
                                bundle.putString("owner_id", userModel.getOwner_id());
                                bundle.putString("srchTxt", srchTxt);
                                bundle.putBoolean("isMobile", isMobile);
                                bundle.putParcelableArrayList("search_per_obj", new ArrayList(gen_response_model.getResponse()));
                                fragmentCall_mainObj.Fragment_call(null,new Result_SearchPersonShare(), "res_per_to_share", bundle);
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
        else {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.net_con),Toast.LENGTH_LONG).show();
        }
    }



}
