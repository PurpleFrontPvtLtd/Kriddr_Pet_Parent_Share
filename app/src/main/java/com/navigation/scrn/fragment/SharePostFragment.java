package com.navigation.scrn.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.SearchPetsAdapter;
import com.Model.Client_collection_model;
import com.Model.Client_info_Model;
import com.Model.Gen_Response_Model;
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

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.util.ActionBarUtil;
import com.util.AlertDialogHandler;
import com.util.GenFragmentCall_Main;
import com.util.ImageChooser_Crop;
import com.util.NetworkConnection;
import com.util.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

import dmax.dialog.SpotsDialog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Niranjan Reddy on 13-03-2018.
 */

public class SharePostFragment extends Fragment {
    UserModel usrModelobj;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    View rootView;
    int OPEN_SETTINGS = 88;
    int PIC_CROP = 99;
    SimpleDraweeView imgShare;
    ImageChooser_Crop imgImageChooser_crop;
    Button btnShare;
    String imgByteStr = "";
    EditText edtPostMsg;
    String Pet_id, Pet_name, Pet_Photo, post_id;
    SimpleDraweeView imgPetsPic;
    ImageView img_del_photo;
    ResizeOptions mResizeOptions;
    ResizeOptions circle_resize_opts;
    boolean not_edit_img = false;
    TextView optPhototxt;
    int PICK_IMAGE_REQ = 77;
    String scrn_from;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        KridderNavigationActivity.setNavigaationVisible(false);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.frag_share_post, container, false);
        btnShare = (Button) rootView.findViewById(R.id.btnShare);
        imgShare = (SimpleDraweeView) rootView.findViewById(R.id.imgPost);
        optPhototxt = (TextView) rootView.findViewById(R.id.optPhototxt);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        circle_resize_opts = new ResizeOptions(200, 200);
        //roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        imgPetsPic = (SimpleDraweeView) rootView.findViewById(R.id.img_Pets_Pic);
        img_del_photo = (ImageView) rootView.findViewById(R.id.img_del_photo);

        edtPostMsg = (EditText) rootView.findViewById(R.id.edt_Message);
        imgImageChooser_crop = new ImageChooser_Crop(getActivity());
        Bundle bundle = getArguments();

        Pet_id = bundle.getString("PetID");
        Pet_name = bundle.getString("PetName");
        scrn_from=bundle.getString("scrn_from");
        Pet_Photo = bundle.getString("PetPhoto");
        post_id = bundle.getString("Post_id", null);
        ((TextView) rootView.findViewById(R.id.txtPetName)).setText(Pet_name);
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setClickable(true);

        imgByteStr = "";
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        img_del_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_id == null) {
                    imgByteStr = "";
                } else {
                    imgByteStr = "del";
                }
                imgShare.setImageResource(R.drawable.option_photo);
                optPhototxt.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //  layoutParams.setMargins(0,0,0,0);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                imgShare.setLayoutParams(layoutParams);


                imgShare.setScaleType(ImageView.ScaleType.FIT_XY);
                img_del_photo.setVisibility(View.GONE);
                //  optPhototxt.setGravity(Gravity.CENTER_HORIZONTAL);


            }
        });
        if (post_id == null) {
            actionBarUtilObj.getTitle().setText("Write a Post");
            img_del_photo.setVisibility(View.GONE);
            imgShare.setImageResource(R.drawable.option_photo);
        } else {


            actionBarUtilObj.getTitle().setText("Edit Post");
            String image = bundle.getString("Post_img", null);
            String Post_desc = bundle.getString("Post_desc");
            edtPostMsg.setText(Post_desc);
            btnShare.setText("UPDATE");
//            imgByteStr="old";

            if (!image.equalsIgnoreCase("")) {
                optPhototxt.setVisibility(View.GONE);
                img_del_photo.setVisibility(View.VISIBLE);
                img_del_photo.setZ(25.0f);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //  layoutParams.setMargins(0,0,0,0);
                imgShare.setLayoutParams(layoutParams);

                imgShare.setScaleType(ImageView.ScaleType.FIT_CENTER);
                not_edit_img = true;
                /*Glide.with(getContext())

                        .load(image)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                imgShare.setImageBitmap(resource);

                            }
                        });*/
                final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(image))
                                .setResizeOptions(mResizeOptions)
                                .build();
                imgShare.setImageRequest(imageRequest);
                /*if (NetworkConnection.isOnline(getContext())) {

                    final AlertDialog dialog = new SpotsDialog(getContext());
                    dialog.setCancelable(false);
                    dialog.show();
                    ApiInterface requestInterface = ApiClient.getClient();
                    Call<ResponseBody> call = requestInterface.fetchImage(image);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    // display the image data in a ImageView or save it
                                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                                    imgShare.setImageBitmap(bm);
                                    //                       imgByteStr = "old";
                                } else {
                                    // TODO
                                }
                            } else {
                                // TODO
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // TODO
                            dialog.dismiss();
                        }
                    });*/
                // }

            }
            else
            {
                imgShare.setImageResource(R.drawable.option_photo);

            }

        }
        actionBarUtilObj.getTitle().setGravity(Gravity.NO_GRAVITY);

        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                hide_fragment();
            }
        });
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();
            }
        });

        if (!Pet_Photo.equalsIgnoreCase("")) {
           /* Glide.with(getContext())

                    .load(Pet_Photo)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgPetsPic.setImageDrawable(drawable);

                        }


                    });*/
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(Pet_Photo))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgPetsPic.setImageRequest(imageRequest2);
        }
        else{
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgPetsPic.setImageRequest(imageRequest2);
        }
        imgPetsPic.getHierarchy().setRoundingParams(roundingParams);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPostMsg.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
                } else
                    SharePost();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                if (intent == null) {
                    //PermissionUtil.
                } else {
                    startActivityForResult(intent, PICK_IMAGE_REQ);
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
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "SHARE_POST_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "Share_Post");


            fragmentTransaction.addToBackStack("Share_Post");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "SHARE_POST_STATE", this); super.onSaveInstanceState(outState);
    }


    public void hide_fragment() {

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev_frag = null;
        if(scrn_from.equalsIgnoreCase("own_feed")) {

            //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
            //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
        }
            else if(scrn_from.equalsIgnoreCase("comts_scrn")){
            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("CrteCommet");
            }
            if (prev_frag.isAdded()) { // if the fragment is already in container
                //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
                ft.remove(this);
                ft.show(prev_frag);

                ft.commit();
            }




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

    public void SharePost() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            if (post_id == null) {
                ApiInterface requestInterface = ApiClient.getClient();
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                mCompositeDisposable.add(requestInterface._pet_post_creation(usrModelobj.getOwner_id(), Pet_id, edtPostMsg.getText().toString(), imgByteStr)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                            @Override
                            public void onNext(Gen_Response_Model gen_response_model) {
                                dialog.dismiss();

                                Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                //                ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                                hide_fragment();
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
               /* BitmapDrawable drawable = (BitmapDrawable) imgShare.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                imgByteStr=getStringImage(bitmap);*/
                if (imgByteStr.trim().equalsIgnoreCase("old") || imgByteStr.trim().equalsIgnoreCase("")) {
                    imgByteStr = "old";
                    ApiInterface requestInterface = ApiClient.getClient();
                    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                    mCompositeDisposable.add(requestInterface._edit_Post(Pet_id, usrModelobj.getOwner_id(), post_id, edtPostMsg.getText().toString(), imgByteStr)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                                @Override
                                public void onNext(Gen_Response_Model gen_response_model) {
                                    dialog.dismiss();

                                    Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                                    ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
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
                    ApiInterface requestInterface = ApiClient.getClient();
                    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                    mCompositeDisposable.add(requestInterface._edit_Post(Pet_id, usrModelobj.getOwner_id(), post_id, edtPostMsg.getText().toString(), imgByteStr)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                                @Override
                                public void onNext(Gen_Response_Model gen_response_model) {
                                    dialog.dismiss();

                                    Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                                    ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
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

        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
      //  AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = PermissionUtil.checkPermissionResult(requestCode, permissions, grantResults);
        if (intent == null) {
            Intent imgChooseIntents = imgImageChooser_crop.getPickImageChooserIntent();
            if (imgChooseIntents == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(imgChooseIntents, PICK_IMAGE_REQ);
            }
        } else {
            startActivityForResult(intent, OPEN_SETTINGS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == OPEN_SETTINGS) {
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }

        }*/
        if (requestCode == PICK_IMAGE_REQ) {
            if (resultCode == Activity.RESULT_OK) {
               /* Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                if (picUri != null) {
                    Intent intent = imgImageChooser_crop.performCrop(picUri,true);
                    startActivityForResult(intent, PIC_CROP);
                }*/
                try {

                    Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    //   Toast.makeText(getContext(),"Imgw:"+bitmap.getWidth()+" ImgH:"+bitmap.getHeight(),Toast.LENGTH_SHORT).show();
                    if (picUri != null) {
                        Intent intent = imgImageChooser_crop.performCrop(picUri, true, bitmap.getWidth(), bitmap.getHeight());
                        startActivityForResult(intent, PIC_CROP);
                    }*/

                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                        CropImage.activity(picUri)

                                .setMinCropResultSize(bitmap.getWidth() / 2, bitmap.getHeight() / 2)
                                .setAspectRatio(bitmap.getWidth() / 2, bitmap.getHeight() / 2)

                                .start(getContext(), this);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    Uri resultUri = result.getUri();
                    //Bitmap bitmap = new Compressor(getContext()).compressToBitmap(new File(resultUri.getPath()));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);

                    imgByteStr = getStringImage(bitmap);
                    if (bitmap != null) {
                       /* RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);*/

                        optPhototxt.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        //     layoutParams.setMargins(0,0,0,0);
                        imgShare.setLayoutParams(layoutParams);
                        imgShare.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        final ImageRequest imageRequest2 =
                                ImageRequestBuilder.newBuilderWithSource(resultUri)
                                        .setResizeOptions(mResizeOptions)
                                        .build();
                        imgShare.setImageRequest(imageRequest2);
                      //  imgShare.setImageBitmap(bitmap);
                        img_del_photo.setVisibility(View.VISIBLE);
                        img_del_photo.setZ(25.0f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == PIC_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = imgImageChooser_crop.getBitmap(data);
                imgByteStr = getStringImage(bitmap);
                if (bitmap != null) {
                    optPhototxt.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //     layoutParams.setMargins(0,0,0,0);
                    imgShare.setLayoutParams(layoutParams);
                    imgShare.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imgShare.setImageBitmap(bitmap);
                    img_del_photo.setVisibility(View.VISIBLE);
                    img_del_photo.setZ(25.0f);
                }
            }
        }

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

    public String getStringImage(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
       /* if (not_edit_img) {
            not_edit_img = false;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } else*/
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
