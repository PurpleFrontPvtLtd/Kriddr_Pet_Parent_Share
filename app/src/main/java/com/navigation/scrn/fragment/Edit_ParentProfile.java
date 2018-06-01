package com.navigation.scrn.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.Model.Gen_Response_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.db.DBHelper;
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

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Niranjan Reddy on 27-02-2018.
 */

public class Edit_ParentProfile extends Fragment {

    EditText edtParntName, edtParntPhone, edtParntAddrs, edt_email;
    SimpleDraweeView imgEdtParntProf;
    String imgByteStr = "";
    View rootView;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    Button btnSave;
    UserModel userModelObj;
    int PICK_IMAGE_REQ = 77;
    int OPEN_SETTINGS = 88;
    InterfaceUserModel interfaceUserModelObj;
    int PIC_CROP = 99;

    ImageChooser_Crop imgImageChooser_crop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_edt_pet_prent_prfle, container, false);

        KridderNavigationActivity.setNavigaationVisible(false);

        edtParntName = (EditText) rootView.findViewById(R.id.edt_PetParntName);
        edtParntPhone = (EditText) rootView.findViewById(R.id.edt_PetPhoneNo);
        edtParntAddrs = (EditText) rootView.findViewById(R.id.edt_PetParntAddrs);
        edt_email = (EditText) rootView.findViewById(R.id.edt_email);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        imgEdtParntProf = (SimpleDraweeView) rootView.findViewById(R.id.img_edtParntProf);


        imgImageChooser_crop = new ImageChooser_Crop(getActivity());

        edtParntName.setText(userModelObj.getOwner_name());
        edtParntPhone.setText(userModelObj.getMobile());
        edtParntAddrs.setText(userModelObj.getAddress());
        edt_email.setText(userModelObj.getEmail());
        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);


        actionBarUtilObj.setTitle("Edit Profile");
        actionBarUtilObj.getTitle().setGravity(Gravity.NO_GRAVITY);
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialogHandler.showDialog(getContext(), "Do you want to go back?", false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(getContext(), "Work in progress", Toast.LENGTH_SHORT).show();
                updatePetParent();
            }
        });
        imgEdtParntProf.setOnClickListener(new View.OnClickListener() {
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
        ResizeOptions resizeOptions = new ResizeOptions(200, 200);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if (!userModelObj.getPhoto().equalsIgnoreCase("")) {

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
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(userModelObj.getPhoto()))
                            .setResizeOptions(resizeOptions)
                            .build();
            imgEdtParntProf.setImageRequest(imageRequest2);
            imgEdtParntProf.getHierarchy().setRoundingParams(roundingParams);

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
            imgEdtParntProf.setImageRequest(imageRequest2);
            imgEdtParntProf.getHierarchy().setRoundingParams(roundingParams);
        }
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "EDIT_PARNTPROF_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "edit_profile");


            fragmentTransaction.addToBackStack("edit_profile");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "EDIT_PARNTPROF_STATE", this);
    }


    public void updatePetParent() {
        final String petparent_name = edtParntName.getText().toString().trim();
        final String address = edtParntAddrs.getText().toString().trim();
        final String mobile = edtParntPhone.getText().toString().trim();
        final String email = edt_email.getText().toString().trim();


        if (petparent_name.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter pet parent name", Toast.LENGTH_SHORT).show();
        } else if (mobile.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
        }
        else if(email.trim().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else {
            if (imgByteStr.equalsIgnoreCase("")) {
                //Toast.makeText(getContext(), "Please choose the pet image", Toast.LENGTH_SHORT).show();
                imgByteStr = "old";
            }
            if (NetworkConnection.isOnline(getContext())) {

                final AlertDialog dialog = new SpotsDialog(getContext());
                dialog.setCancelable(false);
                dialog.show();
                ApiInterface requestInterface = ApiClient.getClient();
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                mCompositeDisposable.add(requestInterface._Parnt_Prof_Update(userModelObj.getOwner_id(), petparent_name, mobile, "", address, imgByteStr, email)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                            @Override
                            public void onNext(Gen_Response_Model s) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "" + s.getResult(), Toast.LENGTH_SHORT).show();
                                userModelObj.setOwner_name(petparent_name);
                                userModelObj.setMobile(mobile);
                                userModelObj.setAddress(address);
                                userModelObj.setEmail(email);
                                if (s.getPhoto() != null)
                                    userModelObj.setPhoto(s.getPhoto());
                                interfaceUserModelObj.setUserModel(userModelObj);
                                DBHelper dbHelper = new DBHelper();
                                dbHelper.open(getContext());
                                dbHelper.UpdateDetail(petparent_name, address, mobile, userModelObj.getPhoto(), userModelObj.getOwner_id());
                                dbHelper._closeDb();
                                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

                            }


                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "err :" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (resultCode == RESULT_OK) {

                try {

                    Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    //           Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(500, 500)
                                .setMaxCropResultSize(2000, 2000)
                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.OVAL)
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);

                    imgByteStr = getStringImage(bitmap);
                    if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgEdtParntProf.setImageDrawable(drawable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == PIC_CROP) {
            if (resultCode == RESULT_OK) {
                //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data);
                Bitmap bitmap = imgImageChooser_crop.getBitmap(data);
                Uri fileUri = data.getData();

                imgByteStr = getStringImage(bitmap);
                if (bitmap != null) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    drawable.setCircular(true);
                    imgEdtParntProf.setImageDrawable(drawable);
                }
            }
        }


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        /*try {
        InputStream inputStream = null;//You can get an inputStream using any IO API

            inputStream = new FileInputStream(fileName);

        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }*/

    }

    public void onResume() {
        super.onResume();
        edtParntName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtParntName.clearFocus();
                }
                return false;
            }
        });
        edtParntPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtParntPhone.clearFocus();
                }
                return false;
            }
        });
        edtParntAddrs.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtParntAddrs.clearFocus();
                }
                return false;
            }
        });

        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceUserModel) {
            interfaceUserModelObj = (InterfaceUserModel) context;
            userModelObj = interfaceUserModelObj.getUserModel();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
        if (context instanceof FragmentCallInterface) {
            genFragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }
    }
}
