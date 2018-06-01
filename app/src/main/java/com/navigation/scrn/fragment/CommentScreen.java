package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.SearchPetsAdapter;
import com.Model.Gen_Response_Model;
import com.Model.Pet_Search_Details_Model;
import com.Model.Pet_Search_Model;
import com.Model.PostDetailModel;
import com.Model.ResponseModel;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;*/
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
import com.util.AlertDialogHandler;
import com.util.CustomNonScrollListView;
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
 * Created by Niranjan Reddy on 22-03-2018.
 */

public class CommentScreen extends Fragment {

    View rootView;
    TextView txtPostCreatedTime;
    SimpleDraweeView imgFeedPic, imgPet;
    TextView txtPetName, txtPostMsg;
    EditText edtPostComnt;
    TextView txtComntCount;
    CustomNonScrollListView list_comments;


    String PET_ID;
    GenFragmentCall_Main genFragmentCall_mainObj;
    LinearLayout lo_comnts_contr;
    PostDetailModel postDetailModelObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModelObj;
    String Scrn_from;
    ResizeOptions mResizeOptions, circle_resize_opts;

    @Override
    public void onResume() {
        super.onResume();
        edtPostComnt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtPostComnt.clearFocus();
                }
                return false;
            }
        });
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
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.setTitle("Back");
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();

            }
        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    hide_fragment();
                } catch (Exception w) {
                    Toast.makeText(getContext(), "error" + w.getMessage(), Toast.LENGTH_LONG).show();
                }
                //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_comnt, container, false);
        ((LinearLayout) rootView.findViewById(R.id.status_contr)).setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.txtPostContent)).setVisibility(View.GONE);
        ((ImageView) rootView.findViewById(R.id.imgCommentCountPic)).setVisibility(View.GONE);
        ImageView img_dots = (ImageView) rootView.findViewById(R.id.img_dots);
        ((View) rootView.findViewById(R.id.vw1)).setVisibility(View.GONE);
        txtPostCreatedTime = (TextView) rootView.findViewById(R.id.txtCreatedTime);
        imgPet = (SimpleDraweeView) rootView.findViewById(R.id.imgProfPic);
        lo_comnts_contr = (LinearLayout) rootView.findViewById(R.id.lo_comnts_contr);
        txtComntCount = (TextView) rootView.findViewById(R.id.txtComntCount);
        //  list_comments = (CustomNonScrollListView) rootView.findViewById(R.id.lo_comnts_contr);
        txtPetName = (TextView) rootView.findViewById(R.id.txtPetName);
        txtPostMsg = (TextView) rootView.findViewById(R.id.txtPostDate);
        imgFeedPic = (SimpleDraweeView) rootView.findViewById(R.id.imgFeedPic);
        edtPostComnt = (EditText) rootView.findViewById(R.id.edtPostCommnt);
        txtComntCount.setVisibility(View.GONE);
        txtPostCreatedTime.setVisibility(View.VISIBLE);
        final RelativeLayout rl_cmnt_dots = (RelativeLayout) rootView.findViewById(R.id.rl_cmnt_dots);
        Bundle bundle = getArguments();
        postDetailModelObj = bundle.getParcelable("postDtlObj");
        PET_ID = bundle.getString("PetID");
        Scrn_from=bundle.getString("scrn_from");
        txtPostCreatedTime.setText(postDetailModelObj.getCreated_time());
        txtPetName.setText(postDetailModelObj.getPet_name());
        txtPostMsg.setText(postDetailModelObj.getDescription());

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();

            }
        });
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setText("Back");
        actionBarUtilObj.getTitle().setGravity(Gravity.NO_GRAVITY);
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    hide_fragment();
                } catch (Exception w) {
                    Toast.makeText(getContext(), "error" + w.getMessage(), Toast.LENGTH_LONG).show();
                }
                //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        });
        if (!PET_ID.equalsIgnoreCase(postDetailModelObj.getPet_id())) {
            img_dots.setVisibility(View.GONE);

        } else {
            img_dots.setVisibility(View.VISIBLE);
            rl_cmnt_dots.setZ(20.0f);
        }
        _CommentsListCalll();
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        circle_resize_opts = new ResizeOptions(200, 200);
        if (!postDetailModelObj.getPet_photo().trim().equalsIgnoreCase("")) {
           /* Glide.with(getContext())

                    .load(postDetailModelObj.getPet_photo())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgPet.setImageDrawable(drawable);

                        }


                    });*/
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelObj.getPet_photo()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgPet.setImageRequest(imageRequest2);
            imgPet.getHierarchy().setRoundingParams(roundingParams);
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgPet.setImageRequest(imageRequest2);
            imgPet.getHierarchy().setRoundingParams(roundingParams);
        }
        if (!postDetailModelObj.getImage().trim().equalsIgnoreCase("")) {

           /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
          //  layoutParams.setMargins(0, 0, 0, 0);
            imgFeedPic.setLayoutParams(layoutParams);

            imgFeedPic.setScaleType(ImageView.ScaleType.CENTER);*/

           /* Glide.with(getContext())

                    .load(postDetailModelObj.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            imgFeedPic.setImageBitmap(resource);

                        }
                    });*/

            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelObj.getImage()))
                            .setResizeOptions(mResizeOptions)
                            .build();
            imgFeedPic.setImageRequest(imageRequest2);
        } else {
            imgFeedPic.setVisibility(View.GONE);
        }

        img_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // int Pos = (int) view.getTag();

                setBottomDialog(postDetailModelObj);

            }
        });
        edtPostComnt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPostComnt.getRight() - edtPostComnt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //Toast.makeText(getContext(),"I am Touched",Toast.LENGTH_SHORT).show();
                        String comments = edtPostComnt.getText().toString().trim();
                        if (comments.equalsIgnoreCase("")) {
                            Toast.makeText(getContext(), "Please enter your comments", Toast.LENGTH_SHORT).show();
                        } else {
                            edtPostComnt.setText("");
                            _ComentPost_CALL(comments);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "COMMENT_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "cr_co");


            fragmentTransaction.addToBackStack("cr_co");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "COMMENT_STATE", this);
    }

    public void hide_fragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev_frag = null;
        switch (Scrn_from){
            case "my_post":
                prev_frag= getActivity().getSupportFragmentManager().findFragmentByTag("My_Post");
                break;
            case "own_feed":
                prev_frag= getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
                break;
            case "public_feed":
                prev_frag=getActivity().getSupportFragmentManager().findFragmentByTag("publicfeed");
                break;
        }
        //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
      //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
        if (prev_frag.isAdded()) { // if the fragment is already in container
        //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
            ft.remove(this);
            ft.show(prev_frag);

            ft.commit();
        }
    }

    public void setBottomDialog(final PostDetailModel _postDtlMdlObj) {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Edit Post");
        menu_list.add("Delete Post");

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
                                _del_Post_CALL();
                                break;
                            case 0:
                                edit_post_frag(_postDtlMdlObj);
                                break;

                        }
                    }
                }
        );

    }
    public void edit_post_frag(PostDetailModel _postDtlMdlObj){
        Bundle bundle = new Bundle();
        bundle.putString("PetID", postDetailModelObj.getPet_id());
        bundle.putString("PetName", postDetailModelObj.getPet_name());
        bundle.putString("PetPhoto", postDetailModelObj.getPet_photo());
        bundle.putString("Post_id", _postDtlMdlObj.getPet_posts_id());
        bundle.putString("Post_img", _postDtlMdlObj.getImage());
        bundle.putString("Post_desc", _postDtlMdlObj.getDescription());
        bundle.putString("scrn_from","comts_scrn");
        genFragmentCall_mainObj.Fragment_call(this, new SharePostFragment(), "edit_Post", bundle);
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
                        final int imageSize = ((right - 10) - (left - 10));
                        mResizeOptions = new ResizeOptions(imageSize, imageSize);
                    }
                });
    }

    public void _del_Post_CALL() {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._del_Post(postDetailModelObj.getPet_id(), userModelObj.getOwner_id(), postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ResponseModel>() {
                        @Override
                        public void onNext(ResponseModel gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResponse(), Toast.LENGTH_SHORT).show();
                            ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

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

    public void _ComentPost_CALL(String comments) {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._post_comments(PET_ID, postDetailModelObj.getPet_posts_id(), comments)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            // ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            _CommentsListCalll();
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

    public void _CommentsListCalll() {
        lo_comnts_contr.removeAllViews();
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._comments_list(postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Pet_Search_Model>() {
                        @Override
                        public void onNext(Pet_Search_Model pet_search_model) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            List<Pet_Search_Details_Model> info_model = pet_search_model.getDetails();
                            Pet_Search_Details_Model pet_search_details_model = info_model.get(0);
                            if (pet_search_details_model.getPet_name().equalsIgnoreCase("empty")) {

                            } else {
                                _getCommentsView(info_model);

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

    public void _getCommentsView(List<Pet_Search_Details_Model> info_model) {
        for (int i = 0; i < info_model.size(); i++) {
            View convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_pets_comments, null);
            ((TextView) convertView.findViewById(R.id.txtPetName)).setText(info_model.get(i).getPet_name());

            TextView txtFollow = (TextView) convertView.findViewById(R.id.txt_follow);
            // ImageView btnFollow = (ImageView) convertView.findViewById(R.id.btn_follow);
            // RelativeLayout rl_follow_contr = (RelativeLayout) convertView.findViewById(R.id.rl_follow_contr);

            final SimpleDraweeView imgPetPic = (SimpleDraweeView) convertView.findViewById(R.id.imgProfPic);


            //  btnFollow.setVisibility(View.GONE);
            txtFollow.setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.txtComment)).setText(info_model.get(i).getComment());

            txtFollow.setTextColor(Color.GRAY);
            txtFollow.setText(info_model.get(i).getCreated());
            //   list_client_info_models.get(i).setPhoto("");
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);
            if (info_model.get(i).getPhoto().equalsIgnoreCase("")) {
                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                        .path(String.valueOf(R.drawable.dogandcat))
                        .build();
                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(circle_resize_opts)
                                .build();
                imgPetPic.setImageRequest(imageRequest2);
                imgPetPic.getHierarchy().setRoundingParams(roundingParams);
            } else {
              /*  Glide.with(getContext())

                        .load(info_model.get(i).getPhoto())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                drawable.setCircular(true);
                                imgPetPic.setImageDrawable(drawable);

                            }


                        });*/
                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(info_model.get(i).getPhoto()))
                                .setResizeOptions(circle_resize_opts)
                                .build();
                imgPetPic.setImageRequest(imageRequest2);
                imgPetPic.getHierarchy().setRoundingParams(roundingParams);
            }
            lo_comnts_contr.addView(convertView);
        }
    }

    public void setCommentsAdapter(List<Pet_Search_Details_Model> info_model) {
        SearchPetsAdapter mAdapter = new SearchPetsAdapter(info_model, getActivity(), null);
        list_comments.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
        if (context instanceof FragmentCallInterface) {
            genFragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceUserModel) {
            userModelObj = ((InterfaceUserModel) context).getUserModel();
        }
    }

}
