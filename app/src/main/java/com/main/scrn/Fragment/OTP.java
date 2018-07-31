package com.main.scrn.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.GenResModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.db.DBHelper;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.MainActivity;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class OTP extends Fragment {

    View rootView;
    EditText pin_first_edittext, pin_second_edittext, pin_third_edittext, pin_forth_edittext, pin_fifth_edittext;
    String mobile;

    String append_OTP;

    GenFragmentCall_Main genFragmentCall_main;
    ActionBarUtil actionBarUtilObj;
    String type;
    String first_pin, second_pin, third_pin, fourth_pin, fifth_pin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.confirmation_layout, container, false);

        pin_first_edittext = (EditText) rootView.findViewById(R.id.pin_first_edittext);
        pin_second_edittext = (EditText) rootView.findViewById(R.id.pin_second_edittext);
        pin_third_edittext = (EditText) rootView.findViewById(R.id.pin_third_edittext);
        pin_forth_edittext = (EditText) rootView.findViewById(R.id.pin_forth_edittext);
        pin_fifth_edittext = (EditText) rootView.findViewById(R.id.pin_fifth_edittext);


        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        pin_first_edittext.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                pin_first_edittext.requestFocus();
                imm.showSoftInput(pin_first_edittext, 0);
            }
        }, 100);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Confirmation Code");



        Bundle bundle = getArguments();

        try {
            mobile = bundle.getString("mobile");
            type=bundle.getString("scrn_from");
            Log.d("MOBRES", "MOBRES" + mobile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        pin_first_edittext.getText().toString();


        pin_first_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_first_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_second_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });
        pin_second_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_second_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_third_edittext.requestFocus();
                } else {
                    pin_first_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });

        pin_third_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_third_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_forth_edittext.requestFocus();
                } else {
                    pin_second_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });

        pin_forth_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_forth_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_fifth_edittext.requestFocus();
                } else {
                    pin_third_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });


        pin_fifth_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_fifth_edittext.getText().toString().trim();
                if (text.length() == 0) {
                    pin_forth_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();

            }
        });

        return rootView;


    }


    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof FragmentCallInterface){
            genFragmentCall_main=((FragmentCallInterface)context).Get_GenFragCallMainObj();
        }
        if(context instanceof InterfaceActionBarUtil){
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }
    }

    public void isEnteredAllDigit() {

        first_pin = pin_first_edittext.getText().toString().trim();
        second_pin = pin_second_edittext.getText().toString().trim();
        third_pin = pin_third_edittext.getText().toString().trim();
        fourth_pin = pin_forth_edittext.getText().toString().trim();
        fifth_pin = pin_fifth_edittext.getText().toString().trim();


        if (!first_pin.equals("") && !second_pin.equals("") && !third_pin.equals("") && !fourth_pin.equals("") && !fifth_pin.equals("")) {
            if (NetworkConnection.isOnline(getActivity())) {
                passingOTPData();
            }
            else{
                Toast.makeText(getContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }


        }


    }


    private void passingOTPData() {
        final String full_otp = first_pin + second_pin + third_pin + fourth_pin + fifth_pin;
        if(NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._otp_verify(full_otp, mobile,type)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<UserModel>() {
                        @Override
                        public void onNext(UserModel userModel) {
                            dialog.dismiss();
                            if (userModel.getOwner_id().equalsIgnoreCase("invalid otp")) {
                                Toast.makeText(getContext(), userModel.getOwner_id(), Toast.LENGTH_SHORT).show();
                            } else {

                                DBHelper dbHelper = new DBHelper();
                                dbHelper.open(getContext());
                                dbHelper.createuser(userModel.getOwner_id(), userModel.getOwner_name(), userModel.getEmail(), userModel.getMobile(), userModel.getStatus(), userModel.getAddress(), userModel.getPreferred_contact(), userModel.getPhoto());
                                dbHelper._closeDb();
                                call_Activity(userModel);
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                             Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
dialog.dismiss();
                        }


                    }));
        }
        else
        {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
        }
    }


    public void call_Activity(UserModel userModelObj) {

        Intent in =new Intent(getActivity(),KridderNavigationActivity.class);
        in.putExtra(KridderNavigationActivity.USER_MODEL_TAG,userModelObj);
        getActivity().finish();
        startActivity(in);
    }
}
