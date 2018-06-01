package com.navigation.scrn.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Adapter.SpinnerArrayAdapter;
import com.Model.Document_categary_model;
import com.Model.Gen_Response_Model;
import com.Model.Pet_Activity_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
import com.google.gson.Gson;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;
import com.util.ImageChooser_Crop;
import com.util.NetworkConnection;
import com.util.PermissionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.KridderNavigationActivity;
import purplefront.com.kriddrpetparent.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class RecordCreationFragment extends Fragment implements View.OnClickListener {


    ImageView add_photo;
    View rootView;
    EditText docu_name;
    Spinner category_value;
    String imgRec_ByteStr = "";
    String documents_name = "", documents_id = "";
    ArrayList<String> docIdList = new ArrayList<>();
    ArrayList<String> docNameList = new ArrayList<>();
    SpinnerArrayAdapter category_adapter;
    String doc_id = "", doc_name = "";
    int PICK_IMAGE_REQ = 77;
    int OPEN_SETTINGS = 88;
    List<Document_categary_model> document_categary_modelList;
    int PIC_CROP = 99;
    Uri picUri;
    Button submit_button;

    private Bitmap selectedBitmap;
    String image = "";
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    ImageChooser_Crop imgImageChooser_crop;
    String pet_id = "";
    String[] Per_List = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_records, container, false);


        KridderNavigationActivity.setNavigaationVisible(false);
        imgImageChooser_crop = new ImageChooser_Crop(getActivity());
        Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);


        }

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.setTitle("Back");
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        add_photo = (ImageView) rootView.findViewById(R.id.add_photo);
        docu_name = (EditText) rootView.findViewById(R.id.doc_name);
        category_value = (Spinner) rootView.findViewById(R.id.category_value);

        submit_button = (Button) rootView.findViewById(R.id.submit_button);


        _call_doc_catg_list();

        category_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    doc_id = document_categary_modelList.get(position).getDocuments_id();
                    //doc_name = docNameList.get(position).toString();
                    //    Log.v("TYPENAME","TYPENAME"+doc_id + "TYNA"+doc_name);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add_photo.setOnClickListener(this);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();


            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "REC_CRT_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "addrecord");


            fragmentTransaction.addToBackStack("addrecord");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "REC_CRT_STATE", this);
    }



    public void validateData() {

        String docName = docu_name.getText().toString().trim();

        if (docName.isEmpty() || docName.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter the document name", Toast.LENGTH_SHORT).show();
        } else if (imgRec_ByteStr.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please choose the image", Toast.LENGTH_SHORT).show();
        } else {
            if (doc_id.equalsIgnoreCase("-2")) {
                Toast.makeText(getContext(), "Please select one category", Toast.LENGTH_LONG).show();
            } else
                submitData(docName);
        }


    }


    private void submitData(final String docName) {

        if (NetworkConnection.isOnline(getActivity())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._create_record(userModel.getOwner_id(), pet_id, docName, doc_id, imgRec_ByteStr, "owner")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model s) {
                            dialog.dismiss();
                            ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                            //  setAdapter(s);
                            //Toast.makeText(getContext(), "" + s.get(0).getImage_selected(), Toast.LENGTH_SHORT).show();
                            // setSpinnerAdapter(s);
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


    public void _call_doc_catg_list() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._doc_categ("documents_category")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<List<Document_categary_model>>() {
                        @Override
                        public void onNext(List<Document_categary_model> s) {
                            dialog.dismiss();
                            //  setAdapter(s);
                            //Toast.makeText(getContext(), "" + s.get(0).getImage_selected(), Toast.LENGTH_SHORT).show();
                            document_categary_modelList = s;
                            setSpinnerAdapter(s);
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


    @Override
    public void onClick(View v) {

        if (v == add_photo) {
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }
        }

    }

    public void setSpinnerAdapter(List<Document_categary_model> doc_catg_list) {
       /* category_adapter = new SpinnerArrayAdapter(getContext(), R.layout.spinner_layout, R.id.spinnerTarget,doc_catg_list);
        category_value.setAdapter(category_adapter);*/
        Document_categary_model model = new Document_categary_model();
        model.setDocuments_name("Select");
        model.setDocuments_id("-2");
        doc_catg_list.add(0, model);
        ArrayAdapter<Document_categary_model> dataAdapter = new ArrayAdapter<Document_categary_model>(getContext(), R.layout.spinner_layout, doc_catg_list);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_contact);
        category_value.setAdapter(dataAdapter);
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
              /*  Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);

                if (picUri != null) {
                    Intent intent = imgImageChooser_crop.performCrop(picUri,false);
                    startActivityForResult(intent, PIC_CROP);
                }*/
                try {

                    Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                  /*  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    if (picUri != null) {
                        Intent intent = imgImageChooser_crop.performCrop(picUri, false, bitmap.getWidth(), bitmap.getHeight());
                        startActivityForResult(intent, PIC_CROP);
                    }*/
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(add_photo.getWidth(), add_photo.getHeight())

                                .setAspectRatio(1, 1)

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

                    imgRec_ByteStr = getStringImage(bitmap);
                    if (bitmap != null) {
                       /* RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);*/
                        add_photo.setImageBitmap(bitmap);
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
                imgRec_ByteStr = getStringImage(bitmap);
                if (bitmap != null) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    drawable.setCircular(true);
                    add_photo.setImageDrawable(drawable);
                }
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


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
