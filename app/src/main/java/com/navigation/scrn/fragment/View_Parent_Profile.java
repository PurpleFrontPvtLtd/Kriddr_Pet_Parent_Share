package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.Client_list_adapter;
import com.Model.Client_collection_model;
import com.Model.Client_info_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.MainActivity;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 27-02-2018.
 */

public class View_Parent_Profile extends Fragment implements Client_list_adapter.pet_detail_show_iface {
    View rootView;
    UserModel usrModelobj;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    ImageView imgEditProfile, imgAddClient;
    SimpleDraweeView imgParent;
    TextView txtParntName, txtPhone, txtAddress,txtEmail;
    RecyclerView vwRecycle_pet_list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_pet_parent_profile, container, false);
        KridderNavigationActivity.setNavigaationVisible(true);
        txtParntName = (TextView) rootView.findViewById(R.id.txtPrntNme);
        txtPhone = (TextView) rootView.findViewById(R.id.txt_mobNo);
        txtAddress = (TextView) rootView.findViewById(R.id.txtAddrs);
        txtEmail=(TextView)rootView.findViewById(R.id.txtEmail);
        imgParent = (SimpleDraweeView) rootView.findViewById(R.id.img_Prnt);
        vwRecycle_pet_list = (RecyclerView) rootView.findViewById(R.id.recycle_pet_list);
        imgEditProfile = (ImageView) rootView.findViewById(R.id.imgEditProf);
        imgAddClient = (ImageView) rootView.findViewById(R.id.imgAddClient);
        txtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        vwRecycle_pet_list.setHasFixedSize(true);

       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
    //  linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        linearLayoutManager.setSmoothScrollbarEnabled(true);
        //linearLayoutManager.scrollToPositionWithOffset(0,0);
        vwRecycle_pet_list.setLayoutManager(linearLayoutManager);
        vwRecycle_pet_list.setNestedScrollingEnabled(false);
        txtParntName.setText(usrModelobj.getOwner_name());
        txtPhone.setText(usrModelobj.getMobile());
        txtAddress.setText(usrModelobj.getAddress());
        txtEmail.setText(usrModelobj.getEmail());

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
//        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        //  actionBarUtilObj.getTitle().setGravity(Gravity.NO_GRAVITY);
        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomDialog();
            }
        });
        actionBarUtilObj.setTitle("PROFILE");
        actionBarUtilObj.getTitle().setGravity(Gravity.CENTER);
        //actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);
        ResizeOptions resizeOptions = new ResizeOptions(200, 200);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if (!usrModelobj.getPhoto().equalsIgnoreCase("")) {

           /* Glide.with(getContext())

                    .load(usrModelobj.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgParent.setImageDrawable(drawable);

                        }


                    });*/
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getContext().getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100).build();
            imgParent.setHierarchy(hierarchy);
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(usrModelobj.getPhoto()))
                            .setResizeOptions(resizeOptions)
                            .build();
            imgParent.setImageRequest(imageRequest2);
            imgParent.getHierarchy().setRoundingParams(roundingParams);

        } else {

           /* Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.profile_default);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), icon);
            drawable.setCircular(true);
            imgParent.setImageDrawable(drawable);*/
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.profile_default))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            imgParent.setImageRequest(imageRequest2);
            imgParent.getHierarchy().setRoundingParams(roundingParams);
        }
        getClient();

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogHandler.showDialog(getActivity(), "Do you want to go exit?", true);
            }
        });

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCall_mainObj.Fragment_call(null,new Edit_ParentProfile(), "edit_profile", null);
            }
        });
        imgAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCall_mainObj.Fragment_call(null,new Client_Creation(), "create_client", null);
            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "View_PARNTPROF_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "view_parent");


            fragmentTransaction.addToBackStack("view_parent");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "View_PARNTPROF_STATE", this);
        super.onSaveInstanceState(outState);
    }


    public void setBottomDialog() {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Sign out");

        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        list_SettingsMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getContext(),"Position :"+position,Toast.LENGTH_SHORT).show();
                        mBottomSheetDialog.dismiss();
                        switch (position) {
                            case 1:

                                break;
                            case 0:
                                /*DBHelper dbHelper=new DBHelper();
                                dbHelper.open(getContext());
                                dbHelper.deleteTable();
                                dbHelper._closeDb();
                                Toast.makeText(getContext(),"Successfully signed out",Toast.LENGTH_SHORT ).show();
                                Intent newIntent=new Intent(getContext(),MainActivity.class);
                                getActivity().finish();
                                startActivity(newIntent);*/
                                AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "Do you want to sign out?", true);
                                break;

                        }
                    }
                }
        );

    }

    public void onResume() {
        super.onResume();
        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "Do you want to exit?", true);
    }


    public void getClient() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getClients(usrModelobj.getOwner_id(), "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Client_collection_model>() {
                        @Override
                        public void onNext(Client_collection_model client_collection_model_obj) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            Client_info_Model info_model = client_collection_model_obj.getResponse().get(0);
                            if ((info_model.getPet_id() == null) || (info_model.getPet_id().trim().equalsIgnoreCase("empty")) || info_model.getPet_id().trim().equalsIgnoreCase("")) {

                            } else {

                                setPetListAdapter(client_collection_model_obj.getResponse());


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

    public void setPetListAdapter(List<Client_info_Model> list_Client_info_models) {

      //  List<Client_info_Model> TempDocList = new ArrayList<>(list_Client_info_models);


        Collections.sort(list_Client_info_models, new Comparator<Client_info_Model>() {
            @Override public int compare(Client_info_Model p1, Client_info_Model p2) {
                return Integer.parseInt(p2.getPet_id()) - Integer.parseInt(p1.getPet_id()); // Descending
            }

        });
        Client_list_adapter mAdapter = new Client_list_adapter(list_Client_info_models, getActivity(), this);
        vwRecycle_pet_list.setAdapter(mAdapter);
        vwRecycle_pet_list.scrollToPosition(0);
        //((LinearLayoutManager) vwRecycle_pet_list.getLayoutManager()).smoothScrollToPosition(vwRecycle_pet_list,null,0);
        //vwRecycle_pet_list.scrollToPosition(list_Client_info_models.size() - 1);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceUserModel) {
            usrModelobj = ((InterfaceUserModel) context).getUserModel();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
        if (context instanceof FragmentCallInterface) {
            fragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }

    }

    @Override
    public void onshow_sel_pet_dtl(String pet_ID,String type) {
        Bundle bundle = new Bundle();
        bundle.putString("pet_id", pet_ID);
        bundle.putString("type",type);
        fragmentCall_mainObj.Fragment_call(null,new ClientViewDetailsFragment(), "view_pet_dtl", bundle);

    }
}
