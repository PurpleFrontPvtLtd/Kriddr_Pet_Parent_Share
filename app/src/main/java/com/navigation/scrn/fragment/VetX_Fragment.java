package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Model.NotificationResponseModel;
import com.Model.Res_FoundQues_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

public class VetX_Fragment extends Fragment  {
    View rootView;
    RecyclerView recycle_vetx_Q_list;
    ActionBarUtil actionBarUtilObj;
    InterfaceUserModel iface_usrModel_obj;
    GenFragmentCall_Main fragmentCall_mainObj;
    RelativeLayout rootLayout;
    UserModel userModel;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }
        if (context instanceof InterfaceUserModel) {
            iface_usrModel_obj = ((InterfaceUserModel) context);
            userModel=iface_usrModel_obj.getUserModel();

        }
        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.lo_vetx_main,container,false);
        recycle_vetx_Q_list=(RecyclerView)rootView.findViewById(R.id.recycle_vetx_Q_list);
        rootLayout=(RelativeLayout)rootView.findViewById(R.id.rl_contr_add);
        actionBarUtilObj.setViewInvisible();


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                rootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 500) { // if more than 100 pixels, its probably a keyboard...
                 Toast.makeText(getContext(),"Show Keyboard",Toast.LENGTH_LONG).show();
                    KridderNavigationActivity.setNavigaationVisible(false);
                }
                else{
                    Toast.makeText(getContext(),"Hide Keyboard",Toast.LENGTH_LONG).show();
                    KridderNavigationActivity.setNavigaationVisible(true);
                }
            }
        });

       /* actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Notifications");
        actionBarUtilObj.getTitle().setGravity(Gravity.CENTER);*/
        actionBarUtilObj.getEdtSearch().setVisibility(View.VISIBLE);
        actionBarUtilObj.getEdtSearch().setInputType(InputType.TYPE_CLASS_TEXT);
        actionBarUtilObj.getEdtSearch().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            String srchText=actionBarUtilObj.getEdtSearch().getText().toString();
            _call_find_question(srchText);
            }
        });
     /*   actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });*/
        recycle_vetx_Q_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return rootView;
    }

    public void _call_find_question(String srchText) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface.
                    _find_questions(userModel.getOwner_id(),srchText,"all","1")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Res_FoundQues_Model>() {
                        @Override
                        public void onNext(Res_FoundQues_Model gen_response_model) {
                            dialog.dismiss();
                            /*recycle_notify.removeAllViewsInLayout();
                            if (gen_response_model.getResponse().get(0).getNotification().equalsIgnoreCase("empty")) {

                            } else {
                                setNfyAdapter(gen_response_model.getResponse());


                            }*/

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
