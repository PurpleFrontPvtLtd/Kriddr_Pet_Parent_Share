package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.ImageMyPostAdapter;
import com.Model.Client_collection_model;
import com.Model.Client_info_Model;
import com.Model.CountModel;
import com.Model.PostDetailModel;
import com.Model.PostResponseModel;
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

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

public class MyPostImageFragement extends Fragment implements ImageMyPostAdapter.CommentInterface

{
    TextView txt_Hdr_PetName, txt_Hdr_Ownr_name, txtPostCount, txtFollwrsCount, txtFollowingCount, txtPetDispName;
    ImageView imgPrev, imgNext;
    SimpleDraweeView imgSliderPets, imgClient;
    int ClientCount = 0;
    GridView grid_img_post;
    List<Client_info_Model> Pet_List = null;
    List<PostDetailModel> postDetailModelListOBJ = null;
    Client_info_Model sel_ClientInfo;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModelObj;
    ResizeOptions circle_resize_opts;
    View rootView;
    String pet_id;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_my_post_img, container, false);
        KridderNavigationActivity.setNavigaationVisible(false);
        imgSliderPets = (SimpleDraweeView) rootView.findViewById(R.id.imgPets);
        imgPrev = (ImageView) rootView.findViewById(R.id.imgPrevPets);
        imgNext = (ImageView) rootView.findViewById(R.id.imgNextPets);
        txtPostCount = (TextView) rootView.findViewById(R.id.txtPostCount);
        txtPetDispName = (TextView) rootView.findViewById(R.id.txtPetDispName);
        grid_img_post = (GridView) rootView.findViewById(R.id.grid_img_post);
        txtFollwrsCount = (TextView) rootView.findViewById(R.id.txtFollwrsCount);
        txt_Hdr_PetName = (TextView) rootView.findViewById(R.id.txtPetName);
        txt_Hdr_Ownr_name = (TextView) rootView.findViewById(R.id.txtPetParntName);
        imgClient = (SimpleDraweeView) rootView.findViewById(R.id.imgClient);
        txtFollowingCount = (TextView) rootView.findViewById(R.id.txtFollowingCount);
        actionBarUtilObj.setViewInvisible();
        circle_resize_opts = new ResizeOptions(200, 200);
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("");
        grid_img_post.setNestedScrollingEnabled(false);
        Bundle bundle = getArguments();
        pet_id = bundle.getString("pet_id");
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                hide_fragment();
            }
        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                actionBarUtilObj.getTitle().setClickable(false);
                hide_fragment();

            }
        });
        PetList_ServiceCall();

        imgClient.setZ(20.f);
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
                        postDetailModelListOBJ = null;


                        PostFeedServiceCall();
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
                        postDetailModelListOBJ = null;


                        PostFeedServiceCall();
                    }
                }
            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "MY_POST_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "My_Post");


            fragmentTransaction.addToBackStack("My_Post");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "MY_POST_STATE", this); super.onSaveInstanceState(outState);
    }

    public void hide_fragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev_frag = null;
        prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
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
        //    AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
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
            }
        });
    }


    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public void ShowClientDetails() {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {
           /* Glide.with(getContext())

                    .load(sel_ClientInfo.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgSliderPets.setImageDrawable(drawable);

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

            imgSliderPets.setHierarchy(hierarchy);

            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(sel_ClientInfo.getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgSliderPets.setImageRequest(imageRequest);
            imgSliderPets.getHierarchy().setRoundingParams(roundingParams);

        } else {
           /* Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.dogandcat);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), icon);
            drawable.setCircular(true);*/
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgSliderPets.setImageRequest(imageRequest2);
            imgSliderPets.getHierarchy().setRoundingParams(roundingParams);

        }
        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {
            /*Glide.with(getContext())

                    .load(sel_ClientInfo.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgClient.setImageDrawable(drawable);

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

            imgClient.setHierarchy(hierarchy);
            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(sel_ClientInfo.getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgClient.setImageRequest(imageRequest);
            imgClient.getHierarchy().setRoundingParams(roundingParams);
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgClient.setImageRequest(imageRequest2);
            imgClient.getHierarchy().setRoundingParams(roundingParams);
            //    imgClient.setImageResource(R.drawable.dogandcat);
        }
        txt_Hdr_PetName.setText(sel_ClientInfo.getPet_name());
        txt_Hdr_Ownr_name.setText(sel_ClientInfo.getOwner_name());
        actionBarUtilObj.setTitle(sel_ClientInfo.getPet_name() + "'s Profile");
        txtPetDispName.setText(sel_ClientInfo.getPet_name());


    }

    public void PostFeedServiceCall() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getPetFeedDtls(sel_ClientInfo.getPet_id(), sel_ClientInfo.getOwner_id(), "my")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PostResponseModel>() {
                         @Override
                        public void onNext(PostResponseModel client_collection_model_obj) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            grid_img_post.removeAllViewsInLayout();
                             int count = 0;
                            if (client_collection_model_obj != null) {
                                if(client_collection_model_obj.getPosts_details().size()==0){

                                }
                                else {
                                    PostDetailModel info_model = client_collection_model_obj.getPosts_details().get(0);
                                    CountModel countModel = client_collection_model_obj.getCount_details();
                                    txtFollwrsCount.setText(countModel.getFollow_count());
                                    txtFollowingCount.setText(countModel.getFollowing_count());

                                    if (info_model.getPet_posts_id().equalsIgnoreCase("empty")) {

                                    } else {

                                        postDetailModelListOBJ = client_collection_model_obj.getPosts_details();


                                        for (int i = 0; i < postDetailModelListOBJ.size(); i++) {

                                            if (postDetailModelListOBJ.get(i).getImage().equalsIgnoreCase("")) {
                                                // count--;
                                                postDetailModelListOBJ.remove(i);
                                            } else {
                                                count++;
                                            }
                                        }
                                        if (count > 0)
                                            setMyPostAdapter();
                                        else {
                                            Toast.makeText(getContext(), "No Image found", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }

                                txtPostCount.setText(String.valueOf(count));

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

    public void setMyPostAdapter() {

        ImageMyPostAdapter adapter = new ImageMyPostAdapter(getContext(), postDetailModelListOBJ, this);
        grid_img_post.setAdapter(adapter);
        setGridViewHeightBasedOnChildren(grid_img_post, 4);
    }

    public void PetList_ServiceCall() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getClients(userModelObj.getOwner_id(), "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Client_collection_model>() {
                        @Override
                        public void onNext(Client_collection_model client_collection_model_obj) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            Client_info_Model info_model = client_collection_model_obj.getResponse().get(0);
                            if (info_model.getPet_id().equalsIgnoreCase("empty")) {

                            } else {
                                Pet_List = client_collection_model_obj.getResponse();
                                for (int i = 0; i < Pet_List.size(); i++) {
                                    if (pet_id.equalsIgnoreCase(Pet_List.get(i).getPet_id())) {
                                        sel_ClientInfo = Pet_List.get(i);
                                        ClientCount = i;
                                        break;
                                    }
                                }

                                ShowClientDetails();

                                PostFeedServiceCall();

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
            //  PetList_ServiceCall();


        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            actionBarUtilObj.setTitle(sel_ClientInfo.getPet_name() + "'s Profile");
            actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            });
            actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            });
        }
    }

    @Override
    public void onShowComment(String PetId, PostDetailModel postDetailModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("postDtlObj", postDetailModel);
        bundle.putString("PetID", PetId);
        bundle.putString("scrn_from", "my_post");
        genFragmentCall_mainObj.Fragment_call(this, new CommentScreen(), "CrteCommet", bundle);

    }
}
