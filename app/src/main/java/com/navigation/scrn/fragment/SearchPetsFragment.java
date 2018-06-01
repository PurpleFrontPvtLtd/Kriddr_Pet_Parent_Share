package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.Adapter.SearchPetsAdapter;
import com.Model.Pet_Search_Details_Model;
import com.Model.Pet_Search_Model;
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


import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 08-03-2018.
 */

public class SearchPetsFragment extends Fragment implements SearchPetsAdapter.SearchPetsSelectedListener {

    TextView txtResults;
    ListView lstvw_SrchPetsList;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    View rootView;
    UserModel userModelObj;
    String pet_id;
    String searchText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_search_pets, container, false);
        txtResults = (TextView) rootView.findViewById(R.id.txtResults);
        lstvw_SrchPetsList = (ListView) rootView.findViewById(R.id.lstvw_srchPetList);
        Bundle bundle = getArguments();
        pet_id= bundle.getString("pet_id");

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        KridderNavigationActivity.setNavigaationVisible(false);
        actionBarUtilObj.getEdtSearch().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgSearch().setVisibility(View.VISIBLE);
        actionBarUtilObj.getEdtSearch().requestFocus();
        actionBarUtilObj.getImgSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actionBarUtilObj.getImgBack().getWindowToken(), 0);
                 searchText = actionBarUtilObj.getEdtSearch().getText().toString();
                if ((searchText.trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getContext(), "Please enter the phone number", Toast.LENGTH_SHORT).show();
                } else if (!searchText.trim().equalsIgnoreCase(userModelObj.getMobile())) {

                    getClient(pet_id, searchText);
                } else
                    Toast.makeText(getContext(), "You can't follow of your own pets", Toast.LENGTH_SHORT).show();
            }

        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();
            }
        });

      /*  ArrayList<String> petsList=new ArrayList<>();
        petsList.add("Tommy");
        petsList.add("Tiger");
        petsList.add("Milo");
        SearchPetsAdapter adapter=new SearchPetsAdapter(getContext(),petsList);
        lstvw_SrchPetsList.setAdapter(adapter);*/

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            actionBarUtilObj.setViewInvisible();
            actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
            actionBarUtilObj.getEdtSearch().setVisibility(View.VISIBLE);
            actionBarUtilObj.getImgSearch().setVisibility(View.VISIBLE);
            actionBarUtilObj.getEdtSearch().setText(searchText);
            actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();
                }
            });
            getClient(pet_id, searchText);
        }
    }

    public void hide_fragment() {

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev_frag = null;

            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
            //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
            //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
            if (prev_frag.isAdded()) { // if the fragment is already in container
                //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
                ft.remove(this);
                ft.show(prev_frag);

                ft.commit();
            }

    }
    @Override
    public void onResume() {
        super.onResume();
       // AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {

                    //  AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
                    hide_fragment();
                    return true;
                }
                return false;
            }});
    }

    public void getClient(String pet_id, String mobNo) {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getClientsBySearch(userModelObj.getOwner_id(),pet_id, mobNo)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Pet_Search_Model>() {
                        @Override
                        public void onNext(Pet_Search_Model pet_search_model) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            List<Pet_Search_Details_Model> info_model = pet_search_model.getDetails();
                            Pet_Search_Details_Model pet_search_details_model = info_model.get(0);
                            if (pet_search_details_model.getOwner_name().equalsIgnoreCase("empty") || pet_search_details_model.getOwner_name().trim().equalsIgnoreCase("")) {
                              Toast.makeText(getContext(),"No pets are found",Toast.LENGTH_LONG).show();
                            } else {
                                setPetsAdapter(info_model);

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
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "SEARCHPET_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "Search_Pets");


            fragmentTransaction.addToBackStack("Search_Pets");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "SEARCHPET_STATE", this);
    }



    public void setPetsAdapter(List<Pet_Search_Details_Model> info_model ){
        SearchPetsAdapter mAdapter = new SearchPetsAdapter(info_model, getActivity(),this);
        lstvw_SrchPetsList.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceUserModel) {
            userModelObj = ((InterfaceUserModel) context).getUserModel();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
        if (context instanceof FragmentCallInterface) {
            genFragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }
    }

    @Override
    public void selectedPets(Pet_Search_Details_Model modelObj) {

        Bundle bundle=new Bundle();
        bundle.putParcelable("pet_info",modelObj);

        bundle.putString("follower_id",pet_id);

        genFragmentCall_mainObj.Fragment_call(this,new ViewPublicFeedFragment(),"publicfeed",bundle);
    }
}
