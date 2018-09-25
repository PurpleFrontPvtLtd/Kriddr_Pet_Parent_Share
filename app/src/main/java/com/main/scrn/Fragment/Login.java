package com.main.scrn.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Model.GenResModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class Login extends Fragment {

    View rootView;
    private EditText firstName, mobileNo;
    private Button login_button;
    String first_name, mobile_number;
    String tag_string_req_recieve2 = "string_req_recieve2";
    List<UserModel> feedslist;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_login, container, false);

        firstName = (EditText) rootView.findViewById(R.id.flname);
        mobileNo = (EditText) rootView.findViewById(R.id.mobile);


        mobileNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        login_button = (Button) rootView.findViewById(R.id.login_button);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String bun_mobileNo = bundle.getString("mobile_no", null);
            if (bun_mobileNo != null) {
                mobileNo.setText(bun_mobileNo);
            }
        }
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
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
        });
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.setTitle("Sign In");
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);


        final String blockCharacterSet = " -,/.@%`'\"\\=~#^|$%&*!";

        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };

        mobileNo.setFilters(new InputFilter[]{filter});


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });

        return rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallInterface) {
            genFragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }
    }


    private void validateData() {

        first_name = firstName.getText().toString().trim();
        mobile_number = mobileNo.getText().toString().trim();

        mobile_number = mobile_number.replaceAll("[^0-9]", "").trim();

        if (first_name.equals("")) {
            Toast.makeText(getActivity(), "Please enter first and last name", Toast.LENGTH_SHORT).show();
        } else if (mobile_number.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkConnection.isOnline(getActivity())) {
                loginData();

            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "LOGIN_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "frag_lgnIn");


            fragmentTransaction.addToBackStack("frag_lgnIn");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "LOGIN_STATE", this);
        super.onSaveInstanceState(outState);
    }

    private void loginData() {
        //mobile_number="91||"+mobile_number;
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();


            final ApiInterface requestInterface = ApiClient.getClient();
            final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._lgn_user(first_name, "1||" + mobile_number)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<GenResModel>() {

                        @Override
                        public void onNext(GenResModel genResModel) {
                            dialog.dismiss();
                            if (genResModel.getName().equalsIgnoreCase("success")) {
                                Bundle args = new Bundle();
                                args.putString("mobile", mobile_number);
                                args.putString("scrn_from","login");
                                genFragmentCall_mainObj.Fragment_call(null,new OTP(), "OTP", args);
                            } else {
                                Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
        }
    }
}
