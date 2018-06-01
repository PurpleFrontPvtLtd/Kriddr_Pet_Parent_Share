package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.Pet_ActivityList_Adapter;
import com.Model.Client_collection_model;
import com.Model.Client_info_Model;
import com.Model.ImageLoadModel;
import com.Model.PetActivityCreatedDtlModel;
import com.Model.PetActivityListResponse;
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
import com.util.CacheClearUtil;
import com.util.GenFragmentCall_Main;
import com.util.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 01-03-2018.
 */


public class ActivityFragment extends Fragment implements Pet_ActivityList_Adapter.iface_pet_activity_clicked {

    UserModel usrModelobj;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    TextView txtPetDispName, txtNoFeedProf;
    View rootView;
    SimpleDraweeView img_slideHdr_pets;
    Client_info_Model sel_ClientInfo;
    RelativeLayout txtNoFeeds;
    List<Client_info_Model> Pet_List = null;
    ImageView imgPrev, imgNext;
    SimpleDraweeView img_add_rec_bark;
    LinearLayout pet_act_contr, act_list_contr;
    RecyclerView recycle_pet_act_listt;
    ArrayList<ImageLoadModel> ary_pet_crtd_act_img;
    List<PetActivityCreatedDtlModel> petActivityCrtd_list;
    int AsyncTaskLoadedIndex = 0;

    CardView cardContr;
    int ClientCount = 0;
    Button img_rec;
    boolean isImgLoadFinished = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_frag, container, false);
        img_rec = (Button) rootView.findViewById(R.id.img_rec_voice);
        KridderNavigationActivity.setNavigaationVisible(true);
        cardContr = (CardView) rootView.findViewById(R.id.cardContr);
        imgPrev = (ImageView) rootView.findViewById(R.id.imgPrevPets);
        imgNext = (ImageView) rootView.findViewById(R.id.imgNextPets);
        txtNoFeedProf = (TextView) rootView.findViewById(R.id.txtNoFeeds2);

        img_add_rec_bark = (SimpleDraweeView) rootView.findViewById(R.id.img_add_rec_bark);
        act_list_contr = (LinearLayout) rootView.findViewById(R.id.act_list_contr);
        recycle_pet_act_listt = (RecyclerView) rootView.findViewById(R.id.scroll_pet_act_list);
        pet_act_contr = (LinearLayout) rootView.findViewById(R.id.pet_act_contr);
        txtNoFeeds = (RelativeLayout) rootView.findViewById(R.id.rl_noFeeds);
        img_slideHdr_pets = (SimpleDraweeView) rootView.findViewById(R.id.imgPets);

        actionBarUtilObj.setTitle("ACTIVITY");
        actionBarUtilObj.setViewInvisible();
        txtPetDispName = (TextView) rootView.findViewById(R.id.txtPetDispName);
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setGravity(Gravity.CENTER);
        ResizeOptions rec_brk_resize_Options=new ResizeOptions(120,60);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.rec_bark))
                .build();
        final ImageRequest imageRequest2 =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(rec_brk_resize_Options)
                        .build();
        img_add_rec_bark.setImageRequest(imageRequest2);

/*
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        int scrn_height=dm.heightPixels;
        scrn_height=scrn_height-400;*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        /*RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrn_height);*/
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        linearLayoutManager.setReverseLayout(false);
        recycle_pet_act_listt.setLayoutManager(linearLayoutManager);
        recycle_pet_act_listt.setNestedScrollingEnabled(false);
        //recycle_pet_act_listt.setLayoutParams(params);

        txtNoFeedProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCall_mainObj.Fragment_call(null, new View_Parent_Profile(), "edit_prof", null);
            }
        });

        img_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_rec_voice_frag();
            }
        });
       /* if(true){
            pet_act_contr.removeAllViews();
        }*/
        img_add_rec_bark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_rec_voice_frag();
            }
        });
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Pet_List != null) {
                    if (Pet_List.size() > 1) {
                        ClientCount--;
                        if (ClientCount == -1) {
                            ClientCount = Pet_List.size() - 1;
                        }

                        sel_ClientInfo = Pet_List.get(ClientCount);
                        ShowClientDetails();


                    }
                }
            }

        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowClientDetails();
                // Toast.makeText(getContext(),"Clicked"+ClientCount,Toast.LENGTH_SHORT).show();
                if (Pet_List != null) {
                    if (Pet_List.size() > 1) {
                        ClientCount++;
                        if (ClientCount <= Pet_List.size() - 1) {
                            //  ClientCount = Pet_List.size() - 1;
                        } else
                            ClientCount = 0;
                        sel_ClientInfo = Pet_List.get(ClientCount);
                        ShowClientDetails();

                    }
                }
            }
        });

        PetList_ServiceCall();
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "ACTY_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "frag_act");


            fragmentTransaction.addToBackStack("frag_act");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "ACTY_STATE", this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            actionBarUtilObj.setViewInvisible();
            actionBarUtilObj.setViewInvisible();
            actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
            actionBarUtilObj.setTitle("ACTIVITY");
            actionBarUtilObj.getTitle().setGravity(Gravity.CENTER_HORIZONTAL);
            KridderNavigationActivity.setNavigaationVisible(true);
            _call_pet_act_list();

        }
    }

    public void call_rec_voice_frag() {
        Bundle bundle = new Bundle();
        bundle.putString("PetID", sel_ClientInfo.getPet_id());
        bundle.putString("PetImage", sel_ClientInfo.getPhoto());
        fragmentCall_mainObj.Fragment_call(this, new RecordVoice(), "rec_voice", bundle);
    }

    public void PetList_ServiceCall() {

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
                            if (info_model.getPet_id().equalsIgnoreCase("empty") || info_model.getPet_id().equalsIgnoreCase("")) {

                            } else {
                                Pet_List = client_collection_model_obj.getResponse();
                                sel_ClientInfo = Pet_List.get(0);
                                ShowClientDetails();


                            }
                            if (Pet_List != null) {
                                txtNoFeeds.setVisibility(View.GONE);
                                cardContr.setVisibility(View.VISIBLE);

                              //  img_add_rec_bark.getHierarchy().setRoundingParams(roundingParams);
                                // pet_act_contr.setVisibility(View.VISIBLE);
                            } else {
                                txtNoFeeds.setVisibility(View.VISIBLE);
                                cardContr.setVisibility(View.GONE);

                                pet_act_contr.setVisibility(View.GONE);
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
            //  Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
            //  PetList_ServiceCall();
            alert_ChkInternetConn();
            if (Pet_List != null) {
                txtNoFeeds.setVisibility(View.GONE);
                cardContr.setVisibility(View.VISIBLE);
                //   pet_act_contr.setVisibility(View.VISIBLE);
            } else {
                txtNoFeeds.setVisibility(View.VISIBLE);
                // txtNoFeeds.setText("Please check network connection");
                cardContr.setVisibility(View.GONE);
                pet_act_contr.setVisibility(View.GONE);
            }

        }

    }

    public void _call_pet_act_list() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._pet_activity_list(usrModelobj.getOwner_id(), sel_ClientInfo.getPet_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PetActivityListResponse>() {
                        @Override
                        public void onNext(PetActivityListResponse s) {
                            dialog.dismiss();
                            PetActivityCreatedDtlModel petActivityCreatedDtlModel = s.getResponse().get(0);
                            if (petActivityCreatedDtlModel.getActivity_list_id().equalsIgnoreCase("Empty")) {
                                pet_act_contr.setVisibility(View.VISIBLE);
                                act_list_contr.setVisibility(View.GONE);

                            } else {
                                pet_act_contr.setVisibility(View.GONE);
                                act_list_contr.setVisibility(View.VISIBLE);
                            /*Pet_ActivityList_Adapter adapter=new Pet_ActivityList_Adapter(getContext(),s.getResponse(),this);
                            recycle_pet_act_listt.setAdapter(adapter);*/
                                petActivityCrtd_list = s.getResponse();
                                //  load_act_crtd_image(0);
                                setAdapter();
                            }
                            //Toast.makeText(getContext(), "" + s.get(0).getImage_selected(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "err:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    /* public void load_act_crtd_image(final int ImgLoadingIndex) {
         AsyncTaskLoadedIndex=ImgLoadingIndex;
         if(AsyncTaskLoadedIndex==0) {
             ary_pet_crtd_act_img = new ArrayList<>();
       *//*  AsyncTaskBG asyncTaskBG = new AsyncTaskBG(s);
        asyncTaskBG.execute();*//*


          //  CacheClearUtil.trimCache(getContext());
        }

       if(AsyncTaskLoadedIndex<petActivityCrtd_list.size())
       {
           boolean isAlrdyImgLoaded = false;
           int index = 0;

           try {


                final PetActivityCreatedDtlModel petActivityCreatedDtlModelObj = petActivityCrtd_list.get(AsyncTaskLoadedIndex);
                if (ary_pet_crtd_act_img.size() > 0) {
                    for (index = 0; index < ary_pet_crtd_act_img.size(); index++) {
                        ImageLoadModel hasgImg = ary_pet_crtd_act_img.get(index);
                        String url = hasgImg.getUrl();
                        if (petActivityCreatedDtlModelObj.getImage_for_list().trim().equalsIgnoreCase(url.trim())) {
                            isAlrdyImgLoaded = true;
                            break;
                        }
                    }
                }
                    *//*   File SelFile = Glide
                            .with(getContext())
                            .load(petActivityCreatedDtlModelObj.getImage_for_list())
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get(); // needs to be called on background thread
                    petActivityCreatedDtlModelObj.setFile_act(SelFile.getAbsolutePath());
                    pet_activity_modelList.remove(i);
                    pet_activity_modelList.add(i, petActivityCreatedDtlModelObj);*//*
                if (isAlrdyImgLoaded) {
                    isAlrdyImgLoaded=false;
                    petActivityCreatedDtlModelObj.setFile_act(String.valueOf(index));
                    petActivityCrtd_list.remove(AsyncTaskLoadedIndex);
                    petActivityCrtd_list.add(AsyncTaskLoadedIndex, petActivityCreatedDtlModelObj);
                    if(AsyncTaskLoadedIndex==petActivityCrtd_list.size()-1 && isImgLoadFinished){

                        setAdapter();
                    }
                    else {
                        load_act_crtd_image(AsyncTaskLoadedIndex+1);
                    }
                } else {
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).threadPoolSize(3)
                            .threadPriority(Thread.MIN_PRIORITY + 3)


                            .memoryCache(new WeakMemoryCache())
                            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

                            .build();

                    DisplayImageOptions options = new DisplayImageOptions.Builder()

                            .showImageOnLoading(R.drawable.loader)
                            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                            .bitmapConfig(Bitmap.Config.RGB_565)

                            .displayer(new SimpleBitmapDisplayer()) // default
                            .handler(new Handler()) // default
                            .build();


                    ImageLoader.getInstance().init(config);

                    ImageSize targetSize = new ImageSize(200, 80); // result Bitmap will be fit to this size
                    //final ProgressBar spinner = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
                    ImageLoader.getInstance().loadImage(petActivityCreatedDtlModelObj.getImage_for_list(), targetSize, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                         isImgLoadFinished=false;
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        isImgLoadFinished=true;
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            // Do whatever you want with Bitmap

                            isImgLoadFinished=true;
                            ImageLoadModel imageLoadModel = new ImageLoadModel();
                            imageLoadModel.setUrl(imageUri);
                            imageLoadModel.setImgBitmap(loadedImage);
                            ary_pet_crtd_act_img.add(imageLoadModel);
                            int FILEINDEX;
                            if(ary_pet_crtd_act_img.size()==0){
                                FILEINDEX=0;
                            }
                            else{
                                FILEINDEX=ary_pet_crtd_act_img.size() - 1;
                            }
                            petActivityCreatedDtlModelObj.setFile_act(String.valueOf(FILEINDEX));
                            petActivityCrtd_list.remove(AsyncTaskLoadedIndex);
                            petActivityCrtd_list.add(AsyncTaskLoadedIndex, petActivityCreatedDtlModelObj);

                            if(AsyncTaskLoadedIndex==petActivityCrtd_list.size()-1 && isImgLoadFinished){
                                setAdapter();
                            }
                            else {
                                load_act_crtd_image(AsyncTaskLoadedIndex+1);
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                }

  *//*           } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception w) {
                w.printStackTrace();
            }*//*


            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    }
*/
    public void setAdapter() {

        Pet_ActivityList_Adapter adapter = new Pet_ActivityList_Adapter(getContext(), petActivityCrtd_list, this);
        recycle_pet_act_listt.setAdapter(adapter);
       /* for(int index=0;index<petActivityCrtd_list.size();index++){
            View v = LayoutInflater.from(getContext())
                    .inflate(R.layout.lo_act_list_row_dtl,null);
            recycle_pet_act_listt.addView(v);
            ImageView imgAct = (ImageView) v.findViewById(R.id.img_act_name);
            TextView txtMonthDay = (TextView) v.findViewById(R.id.txtMon_Day);
            TextView txtTime = (TextView) v.findViewById(R.id.txtTime);
            View topLine = (View) v.findViewById(R.id.vw_line_top_cir);
            View BottomLine = (View) v.findViewById(R.id.vw_line_bottom_cir);
        }*/
    }

    public void ShowClientDetails() {
        ResizeOptions resizeOptions = new ResizeOptions(200, 200);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {
           /* Glide.with(getContext())

                    .load(sel_ClientInfo.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            img_slideHdr_pets.setImageDrawable(drawable);

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
                    .setFadeDuration(100)
                    .build();

            img_slideHdr_pets.setHierarchy(hierarchy);
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(sel_ClientInfo.getPhoto()))
                            .setResizeOptions(resizeOptions)
                            .build();
            img_slideHdr_pets.setImageRequest(imageRequest2);
            img_slideHdr_pets.getHierarchy().setRoundingParams(roundingParams);
        } else {
           /* Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.dogandcat);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), icon);
            drawable.setCircular(true);*/
            // img_slideHdr_pets.setImageResource(R.drawable.dogandcat);
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            img_slideHdr_pets.setImageRequest(imageRequest2);
            img_slideHdr_pets.getHierarchy().setRoundingParams(roundingParams);
        }

        txtPetDispName.setText(sel_ClientInfo.getPet_name());
        _call_pet_act_list();

    }


    public void alert_ChkInternetConn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please check network connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        PetList_ServiceCall();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "Do you want to exit?", true);
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

   /* class AsyncTaskBG extends AsyncTask<String, String, String> {
        List<PetActivityCreatedDtlModel> pet_activity_modelList;
        final AlertDialog dialog = new SpotsDialog(getContext());

        public AsyncTaskBG(List<PetActivityCreatedDtlModel> pet_activity_models) {
            pet_activity_modelList = pet_activity_models;
            //  CacheClearUtil.trimCache(getContext());
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            boolean isImgLoaded = false;
            int index = 0;

            for (int i = 0; i < pet_activity_modelList.size(); i++) {
                AsyncTaskLoadedIndex = i;
                try {


                    final PetActivityCreatedDtlModel petActivityCreatedDtlModelObj = pet_activity_modelList.get(i);
                    if (ary_pet_crtd_act_img.size() > 0) {
                        for (index = 0; index < ary_pet_crtd_act_img.size(); index++) {
                            ImageLoadModel hasgImg = ary_pet_crtd_act_img.get(index);
                            String url = hasgImg.getUrl();
                            if (petActivityCreatedDtlModelObj.getImage_for_list().trim().equalsIgnoreCase(url.trim())) {
                                isImgLoaded = true;
                                break;
                            }
                        }
                    }
                    *//*   File SelFile = Glide
                            .with(getContext())
                            .load(petActivityCreatedDtlModelObj.getImage_for_list())
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get(); // needs to be called on background thread
                    petActivityCreatedDtlModelObj.setFile_act(SelFile.getAbsolutePath());
                    pet_activity_modelList.remove(i);
                    pet_activity_modelList.add(i, petActivityCreatedDtlModelObj);*//*
                    if (isImgLoaded) {
                        petActivityCreatedDtlModelObj.setFile_act(String.valueOf(index));
                        pet_activity_modelList.remove(i);
                        pet_activity_modelList.add(i, petActivityCreatedDtlModelObj);
                    } else {
                        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).threadPoolSize(3)
                                .threadPriority(Thread.MIN_PRIORITY + 3)


                                .memoryCache(new WeakMemoryCache())
                                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

                                .build();

                        DisplayImageOptions options = new DisplayImageOptions.Builder()


                                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                                .bitmapConfig(Bitmap.Config.RGB_565)

                                .displayer(new SimpleBitmapDisplayer()) // default
                                .handler(new Handler()) // default
                                .build();


                        ImageLoader.getInstance().init(config);

                        ImageSize targetSize = new ImageSize(200, 80); // result Bitmap will be fit to this size
                        ImageLoader.getInstance().loadImage(petActivityCreatedDtlModelObj.getImage_for_list(), targetSize, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                // Do whatever you want with Bitmap
                                ImageLoadModel imageLoadModel = new ImageLoadModel();
                                imageLoadModel.setUrl(imageUri);
                                imageLoadModel.setImgBitmap(loadedImage);
                                ary_pet_crtd_act_img.add(imageLoadModel);
                                petActivityCreatedDtlModelObj.setFile_act(String.valueOf(ary_pet_crtd_act_img.size() - 1));
                                pet_activity_modelList.remove(AsyncTaskLoadedIndex);
                                pet_activity_modelList.add(AsyncTaskLoadedIndex, petActivityCreatedDtlModelObj);


                            }
                        });
                    }

  *//*           } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception w) {
                w.printStackTrace();
            }*//*


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            setAdapter();
        }
    }*/

    @Override
    public void _activity_clicked(PetActivityCreatedDtlModel cr_petActivityCreatedDtlModel) {
        Bundle bundle = new Bundle();
        bundle.putString("PetID", sel_ClientInfo.getPet_id());
        bundle.putString("PetImage", sel_ClientInfo.getPhoto());
        bundle.putParcelable("pet_act_list_obj", cr_petActivityCreatedDtlModel);
        bundle.putString("scrn_from", "act_frag");
        fragmentCall_mainObj.Fragment_call(this, new Create_PetActivity(), "act_click", bundle);
    }
}
