package com.navigation.scrn.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Model.Gen_Response_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

public class Client_Creation extends Fragment {

    View rootView;
    UserModel usrModelobj;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    String PickupDate_Str = "", upcoming_date = "";
    String myFormat = "MM-dd-yyyy";
    int PICK_IMAGE_REQ = 77;
    int OPEN_SETTINGS = 88;
    int PIC_CROP = 99;
    ImageChooser_Crop imgImageChooser_crop;
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    EditText day_month_year, edtPetName, edtBrand, edtProtein, edtServings;
    Calendar myCalendar1 = Calendar.getInstance();
    ImageView imgPet;
    String imgPet_ByteStr = "";
    Button btnSubmit;
    DatePickerDialog.OnDateSetListener pickup_date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_client_creation, container, false);
        KridderNavigationActivity.setNavigaationVisible(false);
        edtPetName = (EditText) rootView.findViewById(R.id.petname_value);
        //String DOB=((EditText)rootView.findViewById(R.id.petname_value)).getText().toString().trim();
        edtBrand = (EditText) rootView.findViewById(R.id.brand_value);
        edtProtein = (EditText) rootView.findViewById(R.id.protein_value);
        edtServings = (EditText) rootView.findViewById(R.id.servings_value);

        day_month_year = (EditText) rootView.findViewById(R.id.day_month_year);
        imgPet = (ImageView) rootView.findViewById(R.id.add_photo);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);

        imgImageChooser_crop = new ImageChooser_Crop(getActivity());


        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setGravity(Gravity.NO_GRAVITY);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        actionBarUtilObj.setTitle("Back");

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  AlertDialogHandler.showDialog(getContext(),"Do you want to go back?",false);
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        imgPet.setOnClickListener(new View.OnClickListener() {
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePet();
            }
        });

        pickup_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.v("date", monthOfYear + "");


                updatePickupDate();

            }

        };


        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), pickup_date, myCalendar1
                .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                myCalendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        day_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new DatePickerDialog(getActivity(), pickup_date, myCalendar1
//                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
//                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
//
//
//                updatePickupDate();


                datePickerDialog.show();


                updatePickupDate();
            }
        });

        return rootView;


    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "Client_CRT_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "create_client");


            fragmentTransaction.addToBackStack("create_client");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "Client_CRT_STATE", this);
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
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    /*if (picUri != null) {
                        Intent intent = imgImageChooser_crop.performCrop(picUri, false,bitmap.getWidth(),bitmap.getHeight());
                        startActivityForResult(intent, PIC_CROP);
                    }*/
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

                    imgPet_ByteStr = getStringImage(bitmap);
                    if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
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
                imgPet_ByteStr = getStringImage(bitmap);
                if (bitmap != null) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    drawable.setCircular(true);
                    imgPet.setImageDrawable(drawable);
                }
            }
        }

    }

    public void onResume() {
        super.onResume();
        edtPetName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtPetName.clearFocus();
                }
                return false;
            }
        });
        edtServings.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtServings.clearFocus();
                }
                return false;
            }
        });
        edtProtein.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtProtein.clearFocus();
                }
                return false;
            }
        });
        edtBrand.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    edtBrand.clearFocus();
                }
                return false;
            }
        });

        day_month_year.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    day_month_year.clearFocus();
                }
                return false;
            }
        });
        // ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
    }

    public void updatePickupDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));////

        if (myCalendar1.getTime().after(cal.getTime())) {
            Toast.makeText(getActivity(), "Please select valid date", Toast.LENGTH_SHORT).show();
        }

        day_month_year.setText(sdf.format(myCalendar1.getTime()));

        PickupDate_Str = sdf.format(myCalendar1.getTime());
        String pick_date = sdf.format(myCalendar1.getTime()) + "";


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        upcoming_date = df.format(myCalendar1.getTime());


    }


    public void CreatePet() {

        String petName = edtPetName.getText().toString().trim();
        String DOB = day_month_year.getText().toString().trim();

        String brand = edtBrand.getText().toString().trim();
        String protein = edtProtein.getText().toString().trim();
        String servings = edtServings.getText().toString().trim();

        if (petName.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter pet name", Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkConnection.isOnline(getContext())) {

                final AlertDialog dialog = new SpotsDialog(getContext());
                dialog.setCancelable(false);
                dialog.show();
                ApiInterface requestInterface = ApiClient.getClient();
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                mCompositeDisposable.add(requestInterface._createClient(usrModelobj.getOwner_id(), petName, upcoming_date, brand, protein, servings, imgPet_ByteStr)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                            @Override
                            public void onNext(Gen_Response_Model s) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "" + s.getResult(), Toast.LENGTH_SHORT).show();
                                if (s.getResult().equalsIgnoreCase("success")) {
                                    ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                                }
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
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_SHORT).show();
            }
        }
    }

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
}
