package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.Adapter.RecordSearchAdapter;
import com.Model.DocumentModel;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.R;


/**
 * Created by pf-05 on 3/12/2018.
 */

public class RecordViewDetails extends Fragment{

    View rootView;


    ActionBarUtil actionBarUtilObj;
    String pet_id = "", doc_id = "";
    TextView record_name, category_name;
    SimpleDraweeView record_image;
    Dialog mBottomSheetDialog;
    DocumentModel sel_document_model_obj;
   // RecordSearchAdapter adapter;
    ResizeOptions mResizeOptions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.record_view, container, false);

        record_name = (TextView) rootView.findViewById(R.id.record_name);
        category_name = (TextView) rootView.findViewById(R.id.category_name);
        record_image = (SimpleDraweeView) rootView.findViewById(R.id.record_image);

        Bundle bundle_args = getArguments();
        if (bundle_args != null) {

            sel_document_model_obj = bundle_args.getParcelable("sel_doc_obj");
        }
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setTitle("Back");
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
  /*              Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);*/
//                fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(), "fragviewclient", bundle);
            }
        });


        record_name.setText(sel_document_model_obj.getDocument_name() + " " + sel_document_model_obj.getCreated());
        category_name.setText("Category: " + sel_document_model_obj.getDocument_category());
       // Glide.with(getActivity()).load(sel_document_model_obj.getDocument()).into(record_image);
        final ImageRequest imageRequest2 =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(sel_document_model_obj.getDocument()))
                        .setResizeOptions(mResizeOptions)
                        .build();
        record_image.setImageRequest(imageRequest2);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        rootView.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(
                            View view,
                            int left,
                            int top,
                            int right,
                            int bottom,
                            int oldLeft,
                            int oldTop,
                            int oldRight,
                            int oldBottom) {
                        final int imageSize = ((right ) - (left));
                        mResizeOptions = new ResizeOptions(imageSize, imageSize);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "REC_VW_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "recordview");


            fragmentTransaction.addToBackStack("recordview");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "REC_VW_STATE", this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }


    }

}
