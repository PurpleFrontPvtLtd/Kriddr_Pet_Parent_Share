package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.NotesListAdapter;
import com.Model.Gen_Response_Model;
import com.Model.NotesModel;
import com.Model.PetDetailsModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.util.ActionBarUtil;
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

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotesFragment extends Fragment {

    View rootView;

    RecyclerView mRecycerView;
    RecyclerView.LayoutManager mlayoutManager;
    NotesListAdapter mAdapter;


    ImageView add_Notes;
    NotesModel notesModel;
    List<NotesModel> notesList;
    String pet_id = "";
    String Pet_TYPE;
    AlertDialog dlg_add_notes;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notes_fragment, container, false);

        Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);
            notesList = bundle_args.getParcelableArrayList("notes_list_obj");

           Pet_TYPE=bundle_args.getString("type");
        }

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);


        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.setTitle("Back");


        add_Notes = (ImageView) rootView.findViewById(R.id.add_Notes);
        mRecycerView = (RecyclerView) rootView.findViewById(R.id.note_list);

        mRecycerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycerView.setLayoutManager(mlayoutManager);


        setNotesAdapter(notesList);
        add_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_notes_pop_up_window();
            }
        });
        if(Pet_TYPE.trim().equalsIgnoreCase("shared_pet")){
            add_Notes.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }

    public void setNotesAdapter(List<NotesModel> notesModelList) {
        NotesListAdapter notesAdapter = new NotesListAdapter(new ArrayList<NotesModel>(notesModelList), getContext());
        mRecycerView.setAdapter(notesAdapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "NOTES_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "petparentlist");


            fragmentTransaction.addToBackStack("petparentlist");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "NOTES_STATE", this); super.onSaveInstanceState(outState);
    }



    public void _call_addNotes(String notes) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._notes_Creation(userModel.getOwner_id(), pet_id, notes, "owner")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();
                            dlg_add_notes.dismiss();
                            Toast.makeText(getContext(), gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            callClientDetails();
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
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }


    public void add_notes_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.notes_layout, null);

        alertDialog.setView(dialogView);

        dlg_add_notes = alertDialog.create();

        dlg_add_notes.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_add_notes.cancel();
            }
        });


        TextView title_text = (TextView) dialogView.findViewById(R.id.notes_text);
        title_text.setText("Notes");
        final EditText enter_notes = (EditText) dialogView.findViewById(R.id.enter_notes);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String notes = enter_notes.getText().toString().trim();
                if (notes.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please enter notes", Toast.LENGTH_LONG).show();
                } else
                    _call_addNotes(notes);


            }
        });

        alertDialog.setView(dialogView);


        dlg_add_notes.show();


    }


    public void callClientDetails() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._pet_details(pet_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PetDetailsModel>() {
                        @Override
                        public void onNext(PetDetailsModel gen_response_model) {
                            dialog.dismiss();

                            if (gen_response_model.getNotes_list().get(0).getNotes().equalsIgnoreCase("empty")) {

                            } else {
                                setNotesAdapter(gen_response_model.getNotes_list());
                                notesList = gen_response_model.getNotes_list();
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
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }

}
