package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Adapter.NotifyAdapter;
import com.Model.Gen_Response_Model;
import com.Model.NotificationModel;
import com.Model.NotificationResponseModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.db.DBHelper;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.util.ActionBarUtil;
import com.util.AlertDialogHandler;
import com.util.NetworkConnection;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

public class NotificationFragment extends Fragment implements NotifyAdapter.Ntfy_dtls_interface {
    View rootView;
    RecyclerView recycle_notify;
    ActionBarUtil actionBarUtilObj;
    InterfaceUserModel iface_usrModel_obj;
    String owner_id;
    String petPos;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }
        if (context instanceof InterfaceUserModel) {
            iface_usrModel_obj = ((InterfaceUserModel) context);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.lo_notify, container, false);
        KridderNavigationActivity.setNavigaationVisible(false);
        recycle_notify = (RecyclerView) rootView.findViewById(R.id.recycle_notify_list);
        Bundle bundle = getArguments();
        owner_id = bundle.getString("owner_id");
        petPos = bundle.getString("Pet_Pos");


        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Notifications");
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
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
        recycle_notify.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _call_notify();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if (savedInstanceState != null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "NFY_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "nfy_frag");


            fragmentTransaction.addToBackStack("nfy_frag");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "NFY_STATE", this); super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
    }

    public void setNfyAdapter(List<NotificationModel> listNfyModel) {
        NotifyAdapter adapter = new NotifyAdapter(listNfyModel, getActivity(), this);
        recycle_notify.setAdapter(adapter);
    }

    public void _call_notify() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface.
                    _nfy_list(owner_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<NotificationResponseModel>() {
                        @Override
                        public void onNext(NotificationResponseModel gen_response_model) {
                            dialog.dismiss();
                            recycle_notify.removeAllViewsInLayout();
                            if (gen_response_model.getResponse().get(0).getNotification().equalsIgnoreCase("empty")) {

                            } else {
                                setNfyAdapter(gen_response_model.getResponse());


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
        } else {
            Toast.makeText(getContext(), getActivity().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void _notify_click(final String type, String reaction_id, final String reaction) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            mCompositeDisposable.add(requestInterface.
                    _allow_or_deny(type, reaction, reaction_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();
                            if (gen_response_model.getResult().equalsIgnoreCase("Success")) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            }
                            if(type.equalsIgnoreCase("profile_edit") && reaction.equalsIgnoreCase("accept")){
                                UserModel userModel=iface_usrModel_obj.getUserModel();
                                DBHelper dbHelper = new DBHelper();
                                dbHelper.open(getContext());
                                String Email=gen_response_model.getEmail();
                                String Address=gen_response_model.getAddress();
                                dbHelper.createuser(userModel.getOwner_id(), userModel.getOwner_name(), Email, userModel.getMobile(), userModel.getStatus(), Address, userModel.getPreferred_contact(), userModel.getPhoto());
                                dbHelper._closeDb();
                                userModel.setEmail(Email);
                                userModel.setAddress(Address);
                                iface_usrModel_obj.setUserModel(userModel);

                            }
                            _call_notify();

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
}
