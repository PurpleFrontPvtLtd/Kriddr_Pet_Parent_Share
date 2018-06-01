package com.main.scrn.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SignUp extends Fragment {

    EditText flname, mobile, email;
    Button signup_button;
    String first_Name, mobile_No, email_Val;
    String tag_string_req_recieve2 = "string_req_recieve2";
    View rootView;
    TextView txtTermsCondts;
    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main genFragmentCall_mainObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sgnup_layout, container, false);

        flname = (EditText) rootView.findViewById(R.id.flname);
        mobile = (EditText) rootView.findViewById(R.id.mobile);
        txtTermsCondts=(TextView)rootView.findViewById(R.id.txtTermsCondts);

        mobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        email = (EditText) rootView.findViewById(R.id.email_val);

        String termsStr="By signing up, I agree to Kriddr's <a href=\"https://www.kriddr.com/terms-of-service\">Terms & Conditions</a> and <a href=\"https://www.kriddr.com/privacy-policy\">Privacy Policy</a>";

        txtTermsCondts.setClickable(true);
        txtTermsCondts.setMovementMethod(LinkMovementMethod.getInstance());
        txtTermsCondts.setText(Html.fromHtml(termsStr));
        txtTermsCondts.setLinkTextColor(Color.BLUE);
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


        actionBarUtilObj.setTitle("Sign Up");


        signup_button = (Button) rootView.findViewById(R.id.signup_button);

        // mobile.addTextChangedListener(this);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();

            }
        });
        return rootView;
    }

    private void validateData() {
        first_Name = flname.getText().toString().trim();
        mobile_No = mobile.getText().toString().trim();

        mobile_No = mobile_No.replaceAll("[^0-9]", "").trim();

        email_Val = email.getText().toString().trim();

        if (first_Name.equals("") || first_Name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter first and last name", Toast.LENGTH_SHORT).show();
        } else if (mobile_No.isEmpty() || mobile_No.equals("")) {
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
        }else if(email_Val.equals(""))
        {
            Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if (!email_Val.equals("") && (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_Val).matches())) {
            Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else {
            signupData();
        }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "SIGNUP_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "frag_sgnUp");


            fragmentTransaction.addToBackStack("frag_sgnUp");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "SIGNUP_STATE", this);
    }


    private void signupData() {
        if (NetworkConnection.isOnline(getContext())) {
           // mobile_No="91||"+mobile_No;
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._reg_user(first_Name,"1||"+mobile_No, email_Val)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<GenResModel>() {
                        @Override
                        public void onNext(GenResModel genResModel) {
                            dialog.dismiss();
                            if (genResModel.getName().equalsIgnoreCase("exist")) {
                                Bundle bundle=new Bundle();
                                bundle.putString("mobile_no",mobile_No);
                                Toast.makeText(getContext(), "User already exist,please sign in", Toast.LENGTH_SHORT).show();
                                genFragmentCall_mainObj.Fragment_call(null,new Login(),"signIn",bundle);

                            } else {
                                Bundle args = new Bundle();
                                args.putString("mobile", mobile_No);
                                args.putString("scrn_from","signup");
                                genFragmentCall_mainObj.Fragment_call(null,new OTP(), "OTP", args);
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
