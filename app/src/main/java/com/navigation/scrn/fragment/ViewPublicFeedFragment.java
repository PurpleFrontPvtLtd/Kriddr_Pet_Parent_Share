package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.SocialFeedAdapter;
import com.Model.CountModel;
import com.Model.Gen_Response_Model;
import com.Model.Pet_Search_Details_Model;
import com.Model.PostDetailModel;
import com.Model.PostResponseModel;
import com.Model.ResponseModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.generic.RoundingParams;
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
 * Created by Niranjan Reddy on 09-03-2018.
 */

public class ViewPublicFeedFragment extends Fragment implements SocialFeedAdapter.SocialAdapterInterface,ViewTreeObserver.OnScrollChangedListener {
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModelObj;
    SocialFeedAdapter socialFeedAdapter;
    LinearLayout  ll_follow_status_contr;
    ImageView Img_follow;
    RecyclerView lo_pub_feed_contr;
    //  FrameLayout frameLO_Status;
    View rootView;
    int pag_index = 0;
    public static final int Like_Call = 100;
    public static final int Share_Call = 101;
    public static final int Comment_Call = 102;
    public static final int Edit_Del_Call = 103;

    TextView txt_Hdr_PetName, txt_Hdr_Ownr_name, txtPostCount, txtFollwrsCount, txtFollowingCount;


    ImageView imgFollowed, img_dot_unfollow;
    ScrollView scrollView_post;

    SimpleDraweeView imgClient;
    String petId, ownerId, followerId, followStatus;

    List<PostDetailModel> postDetailModelListOBJ = null;
    int Pag_Index = 0;
    // ImageView img_Dots;
    int RestrictToCallMultiple = 0;
    int INDEX_CREATE_VIEW = -1;

    CardView card_follow_cnt;
    ResizeOptions circle_resize_opts = new ResizeOptions(200, 200);
    ResizeOptions mResizeOptions;
    RoundingParams roundingParams;
    //roundingParams.setBorder(color, 1.0f);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_viewpets_public_feed, container, false);
        lo_pub_feed_contr = (RecyclerView) rootView.findViewById(R.id.lo_pub_feed_contr);
        scrollView_post = (ScrollView) rootView.findViewById(R.id.scroll_post);
        txtPostCount = (TextView) rootView.findViewById(R.id.txtPostCount);

        txtFollwrsCount = (TextView) rootView.findViewById(R.id.txtFollwrsCount);
        txt_Hdr_PetName = (TextView) rootView.findViewById(R.id.txtPetName);
        txt_Hdr_Ownr_name = (TextView) rootView.findViewById(R.id.txtPetParntName);
        txtFollowingCount = (TextView) rootView.findViewById(R.id.txtFollowingCount);
        img_dot_unfollow = (ImageView) rootView.findViewById(R.id.img_dot_unfollow);
        Img_follow = (ImageView) rootView.findViewById(R.id.txtFollow);
        ll_follow_status_contr = (LinearLayout) rootView.findViewById(R.id.ll_follow_status_contr);
        card_follow_cnt = (CardView) rootView.findViewById(R.id.card_follow_cnt);
        imgClient = (SimpleDraweeView) rootView.findViewById(R.id.imgClient);
        //frameLO_Status = (FrameLayout) rootView.findViewById(R.id.frame_status);
        imgFollowed = (ImageView) rootView.findViewById(R.id.imgFollowed);
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);

        pag_index = 0;
        RestrictToCallMultiple = 0;
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setText("Back");
        actionBarUtilObj.getTitle().setClickable(true);
        lo_pub_feed_contr = (RecyclerView) rootView.findViewById(R.id.lo_pub_feed_contr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        lo_pub_feed_contr.setLayoutManager(linearLayoutManager);
        lo_pub_feed_contr.setNestedScrollingEnabled(false);
        lo_pub_feed_contr.removeAllViewsInLayout();
        KridderNavigationActivity.setNavigaationVisible(false);
        Img_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetFollowCall();
            }
        });
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                setRemoveScrollChangeListener();
                //   ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();
            }
        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialogHandler.showDialog(getContext(), "Do you want to go back?", false);
                setRemoveScrollChangeListener();
              //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();

            }
        });

        img_dot_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomDialog();
            }
        });

        scrollView_post.getViewTreeObserver().addOnScrollChangedListener(this);
         /*   @Override
            public void onScrollChanged() {
                if (scrollView_post != null) {
                    if (scrollView_post.getChildAt(0).getBottom() <= (scrollView_post.getHeight() + scrollView_post.getScrollY())) {
                        //scroll view is at bottom
                        //Toast.makeText(getContext(),"ScrollView end",Toast.LENGTH_SHORT).show();
                    } else {
                        //scroll view is not at bottom
                    }
                }
            }
        });*/

        Bundle bundle = getArguments();
        Pet_Search_Details_Model modelObj = bundle.getParcelable("pet_info");
        petId = modelObj.getPet_id();
        ownerId = modelObj.getOwner_id();
        followerId = bundle.getString("follower_id");
        followStatus = modelObj.getFollow_status();

        txt_Hdr_PetName.setText(modelObj.getPet_name());
        txt_Hdr_Ownr_name.setText(modelObj.getOwner_name());
        // imgClient.getHierarchy().setRoundingParams(roundingParams);
        if (!modelObj.getPhoto().equalsIgnoreCase("")) {
            /*Glide.with(getContext())

                    .load(modelObj.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgClient.setImageDrawable(drawable);

                        }


                    });
*/
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(modelObj.getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgClient.setImageRequest(imageRequest2);
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
        }

        if (followStatus.equalsIgnoreCase("Notfollower")) {
            Img_follow.setVisibility(View.VISIBLE);
            imgFollowed.setVisibility(View.INVISIBLE);
            img_dot_unfollow.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            /*layoutParams.setMargins(0,-60,0,0);
            lo_pub_feed_contr.setLayoutParams(layoutParams);*/
        } else {
            Img_follow.setVisibility(View.GONE);
            imgClient.setZ(20.0f);

            img_dot_unfollow.setVisibility(View.VISIBLE);

//            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,300);
//            frameLO_Status.setLayoutParams(params);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // layoutParams.gravity= Gravity.CENTER;
            //    ll_follow_status_contr.removeViewAt(1);
            layoutParams.setMargins(0, -60, 0, 0);
            card_follow_cnt.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams rl_layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, -60, 0, 0);
            lo_pub_feed_contr.setLayoutParams(rl_layoutParams);
           /* layoutParams.setMargins(0,-60,0,0);
            imgClient.setLayoutParams(layoutParams);
*/
            imgFollowed.setVisibility(View.VISIBLE);
        }
        lo_pub_feed_contr.removeAllViewsInLayout();
        PostFeedServiceCall();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "View_Public_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "publicfeed");


            fragmentTransaction.addToBackStack("publicfeed");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "View_Public_STATE", this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        lo_pub_feed_contr.addOnLayoutChangeListener(
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
                        final int imageSize = ((right - 10) - (left - 10));
                        mResizeOptions = new ResizeOptions(imageSize, imageSize);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //Fragment fragment=this;
        //Toast.makeText(getContext(),"I am Showing"+hidden,Toast.LENGTH_LONG).show();
        if(!hidden) {
            RestrictToCallMultiple = 0;
            lo_pub_feed_contr.removeAllViewsInLayout();
            postDetailModelListOBJ = null;
            Pag_Index = 0;
            INDEX_CREATE_VIEW=-1;
            actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionBarUtilObj.getTitle().setClickable(false);
                    setRemoveScrollChangeListener();
                    //   ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();
                }
            });
            actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // AlertDialogHandler.showDialog(getContext(), "Do you want to go back?", false);
                    setRemoveScrollChangeListener();
                    //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();

                }
            });

            PostFeedServiceCall();
        }
    }
    public void hide_fragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev_frag = null;

        prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("Search_Pets");
        //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
        //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
        if (prev_frag.isAdded()) { // if the fragment is already in container
            //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
            ft.remove(this);
            ft.show(prev_frag);

            ft.commit();
        }

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
        menu_list.add("Unfollow");
        menu_list.add("Block the user");
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
                                block_user_api_call();
                                break;
                            case 0:

                                _unfollow_pet_CAll();
                                break;

                        }
                    }
                }
        );

    }

    public void block_user_api_call(){
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._block_user(ownerId, userModelObj.getOwner_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            //  lo_own_feed_contr.requestFocus();

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
        }
    }

    public void setRemoveScrollChangeListener() {
        pag_index = 0;
        scrollView_post.getViewTreeObserver().removeOnScrollChangedListener(this);
    }

    public void alert_ChkInternetConn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please check network connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        PostFeedServiceCall();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void PetFollowCall() {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._toFollow(petId, followerId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model
                                                   client_collection_model_obj) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "" + client_collection_model_obj.getResult(), Toast.LENGTH_SHORT).show();
                            Img_follow.setVisibility(View.GONE);

                            int FollowerCount = Integer.parseInt(txtFollwrsCount.getText().toString());
                            FollowerCount++;
                            txtFollwrsCount.setText(String.valueOf(FollowerCount));
                            img_dot_unfollow.setVisibility(View.VISIBLE);
                            imgClient.setZ(20.0f);
                            /*RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 300);
                            frameLO_Status.setLayoutParams(params);*/
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            // layoutParams.gravity= Gravity.CENTER;
                            //   ll_follow_status_contr.removeViewAt(1);
                           // layoutParams.setMargins(0, -60, 0, 0);

                            layoutParams.setMargins(0, -60, 0, 0);
                            card_follow_cnt.setLayoutParams(layoutParams);
                            // layoutParams.setMargins(0,-30,0,0);

                            // card_follow_cnt
                            RelativeLayout.LayoutParams rl_layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, -60, 0, 0);
                            lo_pub_feed_contr.setLayoutParams(rl_layoutParams);
                            imgFollowed.setVisibility(View.VISIBLE);

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

   /* public void getFeedView() {
        for (int i = INDEX_CREATE_VIEW + 1; i < postDetailModelListOBJ.size(); i++) {
            final PostDetailModel postDetailModelOBJ = postDetailModelListOBJ.get(i);
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.card_feed_dtl, null);


            final SimpleDraweeView imgPostPic = (SimpleDraweeView) view.findViewById(R.id.imgFeedPic);
            final SimpleDraweeView imgPetPic = (SimpleDraweeView) view.findViewById(R.id.imgProfPic);
            final RelativeLayout rl_comnt_contr = (RelativeLayout) view.findViewById(R.id.rl_comnt_contr);
            final RelativeLayout rl_share_contr = (RelativeLayout) view.findViewById(R.id.rl_shr_contr);
            final ImageView img_Dots = (ImageView) view.findViewById(R.id.img_dots);
            final TextView txtCmntCount = (TextView) view.findViewById(R.id.txtComntCount);
            final RelativeLayout rl_like_contr = (RelativeLayout) view.findViewById(R.id.rl_like_contr);
            final ImageView imgLike = (ImageView) view.findViewById(R.id.imgLike);
            final RelativeLayout rl_shard_by = (RelativeLayout) view.findViewById(R.id.rl_shard_by);
            final TextView txtShrCnt = (TextView) view.findViewById(R.id.txtShreCnt);
            final TextView txtLikeCnt = (TextView) view.findViewById(R.id.txtLikeCnt);
            final TextView txtPetName = (TextView) view.findViewById(R.id.txtPetName);
            final RelativeLayout rl_cmnt_dots = (RelativeLayout) view.findViewById(R.id.rl_cmnt_dots);
            final TextView txtPostMessage = (TextView) view.findViewById(R.id.txtPostContent);
            final TextView txtPostDate = (TextView) view.findViewById(R.id.txtPostDate);
            final ImageView img_comments_Count = (ImageView) view.findViewById(R.id.imgCommentCountPic);
            int CommentCntVal = Integer.parseInt(postDetailModelOBJ.getComments_counts());
            img_Dots.setVisibility(View.GONE);
            rl_cmnt_dots.setZ(25.0f);
            if (CommentCntVal < 10)
                txtCmntCount.setText(" " + CommentCntVal);
            else
                txtCmntCount.setText("" + CommentCntVal);
            img_comments_Count.setTag(i);
            rl_like_contr.setTag(i);
            view.setTag(i);
            rl_comnt_contr.setTag(i);
            rl_share_contr.setTag(i);

            txtLikeCnt.setText(postDetailModelOBJ.getLikes_counts() + " Likes");
            txtShrCnt.setText(postDetailModelOBJ.getShare_counts() + " Shares");


            if (!postDetailModelOBJ.getLike_status().equalsIgnoreCase("no")) {
                imgLike.setImageResource(R.drawable.like_fill);
                imgLike.setTag(0);//Like Filled
            } else {
                imgLike.setTag(1);//Not Filled
            }


            if (postDetailModelOBJ.getShared_by().get(0).getName().equalsIgnoreCase("empty")) {

            } else {
                rl_shard_by.setVisibility(View.VISIBLE);
                ((TextView) rl_shard_by.findViewById(R.id.txtShrdByName)).setText("Shared by " + postDetailModelOBJ.getShared_by().get(0).getName());
                final SimpleDraweeView imgShrdByPets = ((SimpleDraweeView) rl_shard_by.findViewById(R.id.img_shrdByPets));

                imgShrdByPets.getHierarchy().setRoundingParams(roundingParams);
                if (!postDetailModelOBJ.getShared_by().get(0).getPhoto().equalsIgnoreCase("")) {


              *//*  Glide.with(scrnContxt)

                        .load(postDetailModelOBJ.getShared_by().get(0).getPhoto())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                                drawable.setCircular(true);
                                imgShrdByPets.setImageDrawable(drawable);

                            }


                        });*//*
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getShared_by().get(0).getPhoto()))
                                    .setResizeOptions(circle_resize_opts)
                                    .build();
                    imgShrdByPets.setImageRequest(imageRequest2);
                    //    imgShrdByPets.setImageURI();
                } else {
                    Uri uri = new Uri.Builder()
                            .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                            .path(String.valueOf(R.drawable.dogandcat))
                            .build();
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(uri)
                                    .setResizeOptions(circle_resize_opts)
                                    .build();
                    imgShrdByPets.setImageRequest(imageRequest2);
                }
            }

            txtPetName.setText(postDetailModelOBJ.getPet_name());
            txtPostDate.setText(postDetailModelOBJ.getCreated());

            rl_comnt_contr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Pos = (int) v.getTag();
                    final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                    call_comt_fragment(sel_postDetailModelOBJ);
                  //           Toast.makeText(getContext(), "Pet_id" + sel_postDetailModelOBJ.getPet_posts_id(), Toast.LENGTH_SHORT).show();
                }
            });
            rl_like_contr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Pos = (int) v.getTag();

                    final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                    int likeFillVal = (int) imgLike.getTag();
                    if (likeFillVal == 1) {
                        imgLike.setImageResource(R.drawable.like_fill);
                        imgLike.setTag(0);
                    } else {
                        imgLike.setImageResource(R.drawable.like);
                        imgLike.setTag(1);
                    }
                    _likePost_CALL(followerId, sel_postDetailModelOBJ.getPet_posts_id());
                }
            });
            rl_share_contr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Pos = (int) v.getTag();
                    final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                    //Toast.makeText(getContext(), "Pet_id" + sel_postDetailModelOBJ.getPet_posts_id(), Toast.LENGTH_SHORT).show();
                    sharePost_Call(sel_postDetailModelOBJ);
                }
            });
            txtPostMessage.setText(postDetailModelOBJ.getDescription());
            if (postDetailModelOBJ.getImage().equalsIgnoreCase("")) {

                imgPostPic.setVisibility(View.GONE);
*//*
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) img_comments_Count.getLayoutParams();

                buttonLayoutParams.setMargins(0, 0, 70, 0);
                img_comments_Count.setLayoutParams(buttonLayoutParams);
                buttonLayoutParams.setMargins(0, 0, 40, 0);
                txtCmntCount.setLayoutParams(buttonLayoutParams);
*//*

            } else {
               *//* Glide.with(getContext())

                        .load(postDetailModelOBJ.getImage())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                imgPostPic.setImageBitmap(resource);

                            }
                        });*//*
                final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getImage()))
                                .setResizeOptions(mResizeOptions)
                                .build();
                imgPostPic.setImageRequest(imageRequest);
            }
            imgPetPic.getHierarchy().setRoundingParams(roundingParams);
            if (postDetailModelOBJ.getPet_photo().trim().equalsIgnoreCase("")) {
                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                        .path(String.valueOf(R.drawable.dogandcat))
                        .build();
                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(circle_resize_opts)
                                .build();
                imgPetPic.setImageRequest(imageRequest2);
            } else {
            *//*    Glide.with(getContext())

                        .load(postDetailModelOBJ.getPet_photo())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                drawable.setCircular(true);
                                imgPetPic.setImageDrawable(drawable);

                            }
                        });*//*

                final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getPet_photo()))
                                .setResizeOptions(circle_resize_opts)
                                .build();
                imgPetPic.setImageRequest(imageRequest);
            }
            lo_pub_feed_contr.addView(view);
            INDEX_CREATE_VIEW = i;
        }
        scrollView_post.scrollTo(0, 0);
    }*/

    public void PostFeedServiceCall() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getPetFeedDtls(petId, ownerId, String.valueOf(pag_index))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PostResponseModel>() {
                        @Override
                        public void onNext(PostResponseModel client_collection_model_obj) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            if (client_collection_model_obj != null) {
                                PostDetailModel info_model = client_collection_model_obj.getPosts_details().get(0);
                                if (info_model.getPet_posts_id().equalsIgnoreCase("empty")) {

                                } else {
                                    //  Pet_List = client_collection_model_obj.getResponse();
                                    List<PostDetailModel> detailModels = client_collection_model_obj.getPosts_details();
                                    if (postDetailModelListOBJ == null) {
                                        postDetailModelListOBJ = detailModels;
                                    } else {
                                        postDetailModelListOBJ.addAll(detailModels);
                                    }
                                  setFeedAdapter();


                                }
                                CountModel countModel = client_collection_model_obj.getCount_details();
                                txtPostCount.setText(countModel.getPosts_count());
                                txtFollwrsCount.setText(countModel.getFollow_count());
                                txtFollowingCount.setText(countModel.getFollowing_count());
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
//            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
            alert_ChkInternetConn();
            //  PostFeedServiceCall();
        }

    }

    public void setFeedAdapter() {
        socialFeedAdapter = new SocialFeedAdapter(getContext(), postDetailModelListOBJ, petId, this,true);
        lo_pub_feed_contr.setAdapter(socialFeedAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        //AlertDialogHandler._fragment_handelBackKey(rootView, getContext(), "", false);
        _fragment_handelBackKey(getContext(), "");
    }

    public void call_comt_fragment(PostDetailModel sel_postDetailModelOBJ){
        Bundle bundle = new Bundle();
        bundle.putParcelable("postDtlObj", sel_postDetailModelOBJ);
        bundle.putString("PetID", followerId);
        bundle.putString("scrn_from","public_feed");
        genFragmentCall_mainObj.Fragment_call(this,new CommentScreen(), "cr_co", bundle);

    }

    public void _fragment_handelBackKey(final Context context, final String msg) {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    setRemoveScrollChangeListener();
                    if (msg.equalsIgnoreCase("")) {
                      //  ((AppCompatActivity) context).getSupportFragmentManager().popBackStackImmediate();
                        hide_fragment();
                    }

                    return true;
                }
                return false;
            }
        });

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

  /*  public void sharePost_Call(PostDetailModel postDetailModelObj) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._share_post(followerId, userModelObj.getOwner_id(), postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            PostFeedServiceCall();
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
        }
    }*/


    public void sharePost_Call(PostDetailModel postDetailModelObj) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._share_post(followerId, userModelObj.getOwner_id(), postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            RestrictToCallMultiple = 0;
                            lo_pub_feed_contr.removeAllViewsInLayout();
                            postDetailModelListOBJ = null;
                            Pag_Index = 0;

                            Pag_Index = 0;
                            RestrictToCallMultiple = 0;
                            PostFeedServiceCall();
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
        }
    }


  /*  public void _likePost_CALL(String petId, String Post_id,int AdapteerPos) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._like_Post(petId, Post_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
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
    }*/

    public void _likePost_CALL(String petId, String Post_id, final int position) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._like_Post(petId, Post_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            PostDetailModel postDetailModel = postDetailModelListOBJ.get(position);
                            int likecount = Integer.parseInt( postDetailModel.getLikes_counts());

                            if(postDetailModel.getLike_status().trim().equalsIgnoreCase("no")) {
                                postDetailModel.setLike_status("yes");
                                likecount++;
                            }
                            else {
                                postDetailModel.setLike_status("no");
                                likecount--;
                            }
                            //  String[] likes = postDetailModel.getLikes_counts().split(" ");

                            postDetailModel.setLikes_counts(String.valueOf(likecount));
                            postDetailModelListOBJ.remove(position);
                            postDetailModelListOBJ.add(position, postDetailModel);
                            socialFeedAdapter.setFeedList(postDetailModelListOBJ);
                            socialFeedAdapter.notifyItemChanged(position);
                            // setFeedAdapter();
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


    public void _unfollow_pet_CAll() {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._unfollow_pet(petId, followerId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ResponseModel>() {
                        @Override
                        public void onNext(ResponseModel gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResponse(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            img_dot_unfollow.setVisibility(View.INVISIBLE);
                            imgFollowed.setVisibility(View.INVISIBLE);
                            Img_follow.setVisibility(View.VISIBLE);
                            int FollowerCount = Integer.parseInt(txtFollwrsCount.getText().toString());
                            FollowerCount--;
                            txtFollwrsCount.setText(String.valueOf(FollowerCount));
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            // layoutParams.gravity= Gravity.CENTER;
                            //   ll_follow_status_contr.removeViewAt(1);
                            layoutParams.setMargins(0, 0, 0, 0);


                            layoutParams.setMargins(0, 10, 0, 0);
                            card_follow_cnt.setLayoutParams(layoutParams);
                            RelativeLayout.LayoutParams rl_layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 0, 0, 0);
                            lo_pub_feed_contr.setLayoutParams(rl_layoutParams);
                            imgClient.setZ(0f);
                            Img_follow.setZ(20.0f);
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
    public void onScrollChanged() {
        if (scrollView_post != null) {
            if (scrollView_post.getChildAt(0).getBottom() <= (scrollView_post.getHeight() + scrollView_post.getScrollY())) {
                //scroll view is at bottom
                //  Toast.makeText(getContext(),"ScrollView end",Toast.LENGTH_SHORT).show();
                if (RestrictToCallMultiple == 0 || (scrollView_post.getChildAt(0).getBottom() != RestrictToCallMultiple)) {
                    pag_index++;
                    PostFeedServiceCall();
                    RestrictToCallMultiple = scrollView_post.getChildAt(0).getBottom();
                }
            } else {
                //scroll view is not at bottom
            }
            //  scrollView_post.requestFocus();
        }
    }

    @Override
    public void _callService(PostDetailModel postDetailModelObj, int Options, int AdapterPos) {
        switch (Options) {
            case Like_Call:
                _likePost_CALL(followerId, postDetailModelObj.getPet_posts_id(), AdapterPos);
                break;
            case Share_Call:
                RestrictToCallMultiple = 0;
                sharePost_Call(postDetailModelObj);
                break;
            case Comment_Call:
                Bundle bundle = new Bundle();
                bundle.putParcelable("postDtlObj", postDetailModelObj);
                bundle.putString("PetID",followerId);
                bundle.putString("scrn_from", "public_feed");
              //  isComtClicked=true;
                genFragmentCall_mainObj.Fragment_call(this, new CommentScreen(), "CrteCommet", bundle);
                break;
            case Edit_Del_Call:
               // setBottomDialog(postDetailModelObj, AdapterPos);
                break;

        }
    }
}
