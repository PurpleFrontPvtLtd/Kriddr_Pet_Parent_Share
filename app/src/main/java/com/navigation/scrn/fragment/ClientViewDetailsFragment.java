package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.NotesAdapter;
import com.Adapter.RecordsAdapter;
import com.Model.Client_info_Model;
import com.Model.DocumentModel;
import com.Model.Gen_Response_Model;
import com.Model.NotesModel;
import com.Model.PetDetailsModel;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.util.ActionBarUtil;
import com.util.AlertDialogHandler;
import com.util.GenFragmentCall_Main;
import com.util.ImageChooser_Crop;
import com.util.NetworkConnection;

import java.io.ByteArrayOutputStream;
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
import purplefront.com.kriddrpetparent.R;


import static android.app.Activity.RESULT_OK;
import static com.Adapter.RecordsAdapter.*;

/**
 * Created by pf-05 on 2/19/2018.
 */

public class ClientViewDetailsFragment extends Fragment implements DataFromAdapterToFragment {

    View rootView;
    TextView profile_name, profile_dob, brand_value, protein_value, servings_value, txt_notes_no_data_items, txt_rec_no_data_items;
    ImageView food_edit, add_Notes, img_plus_btn, img_next_btn;
    SimpleDraweeView imgPet;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    TextView txt_protein_text, txtSharedWithPersons;
    String pet_id, owner_id;
    RecyclerView recycle_record_list, recycle_notes_list;
    RecyclerView.LayoutManager mLayoutManager;
    RecordsAdapter mAdapter;
    NotesAdapter notesAdapter;
    ImageView imgShrEdit;
    int PICK_IMAGE_REQ = 77;
    int OPEN_SETTINGS = 88;

    TextView see_more_text;
    String prefered_msg;
    AlertDialog dlg_notes_add;
    Client_info_Model petDetailObj;
    DocumentModel documentModel;
    NotesModel notesModel;
    ImageChooser_Crop imgImageChooser_crop;
    List<NotesModel> notest_list_obj;
    RelativeLayout see_more_layout;
    String imgByteStr;
    String pet_type;
    ResizeOptions circle_resize_opts;
    RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);


//    ListView invoice_list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.client_profile_view, container, false);

        see_more_text = (TextView) rootView.findViewById(R.id.see_more_text);
        KridderNavigationActivity.setNavigaationVisible(false);

        roundingParams.setRoundAsCircle(true);
        circle_resize_opts = new ResizeOptions(200, 200);
        final Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);
            pet_type = bundle_args.getString("type");

        }

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                //          fragmentCall_mainObj.Fragment_call(new ClientFragment(),"fragclient",null);
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
        actionBarUtilObj.getImgSharePet().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                bundle.putString("petName", petDetailObj.getPet_name());
                fragmentCall_mainObj.Fragment_call(null, new Shared_Profile_Details(), "shar_prof_dtl", bundle);
            }
        });
        imgShrEdit = (ImageView) rootView.findViewById(R.id.imgShrEdit);
        txtSharedWithPersons = (TextView) rootView.findViewById(R.id.txtSharedWithPersons);
        txt_protein_text = (TextView) rootView.findViewById(R.id.protein_text);
        txt_notes_no_data_items = (TextView) rootView.findViewById(R.id.notes_no_data_items);
        txt_rec_no_data_items = (TextView) rootView.findViewById(R.id.rec_no_data_items);
        profile_name = (TextView) rootView.findViewById(R.id.profile_name);
        profile_dob = (TextView) rootView.findViewById(R.id.profile_dob);

        brand_value = (TextView) rootView.findViewById(R.id.brand_value);
        protein_value = (TextView) rootView.findViewById(R.id.protein_value);
        servings_value = (TextView) rootView.findViewById(R.id.servings_value);
        see_more_layout = (RelativeLayout) rootView.findViewById(R.id.see_more_layout);

        img_next_btn = (ImageView) rootView.findViewById(R.id.img_next_btn);
        imgPet = (SimpleDraweeView) rootView.findViewById(R.id.imgPet);
        food_edit = (ImageView) rootView.findViewById(R.id.food_edit);
        img_plus_btn = (ImageView) rootView.findViewById(R.id.img_plus_btn);
        add_Notes = (ImageView) rootView.findViewById(R.id.add_Notes);

       /* txt_protein_text.setVisibility(View.GONE);
        protein_value.setVisibility(View.GONE);*/

        recycle_record_list = (RecyclerView) rootView.findViewById(R.id.lst_view);
        recycle_record_list.setHasFixedSize(true);
        recycle_record_list.setNestedScrollingEnabled(false);

        recycle_notes_list = (RecyclerView) rootView.findViewById(R.id.notes_list);
        recycle_record_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        recycle_notes_list.setHasFixedSize(true);
        recycle_notes_list.setNestedScrollingEnabled(false);

        //    mLayoutManager = new LinearLayoutManager(getActivity());
//
//  recycle_record_list.setLayoutManager(mLayoutManager);

        if (!pet_type.trim().equalsIgnoreCase("shared_pet"))
            actionBarUtilObj.getImgSharePet().setVisibility(View.VISIBLE);
        else {
            add_Notes.setVisibility(View.INVISIBLE);
            food_edit.setVisibility(View.INVISIBLE);
            img_plus_btn.setVisibility(View.INVISIBLE);
            imgShrEdit.setVisibility(View.INVISIBLE);
        }

        imgShrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                bundle.putString("petName", petDetailObj.getPet_name());
                fragmentCall_mainObj.Fragment_call(null, new Shared_Profile_Details(), "shar_prof_dtl", bundle);
            }
        });

        imgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgImageChooser_crop = new ImageChooser_Crop(getActivity());
                Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                if (intent == null) {
                    //PermissionUtil.
                } else {
                    startActivityForResult(intent, PICK_IMAGE_REQ);
                }
            }
        });

        see_more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList listNOTES = new ArrayList(notest_list_obj);
                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                bundle.putParcelableArrayList("notes_list_obj", listNOTES);
                bundle.putString("type",pet_type);
                fragmentCall_mainObj.Fragment_call(null, new NotesFragment(), "petparentlist", bundle);

            }
        });

        img_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("NECTPIS", "NECTPIS");

//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recycle_record_list.getLayoutManager();
//
//                recycle_record_list.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycle_record_list.getLayoutManager();

                int lastVisibleItemIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();


                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {

                    linearLayoutManager.smoothScrollToPosition(recycle_record_list, null, lastVisibleItemIndex - 1);

                    LinearLayoutManager manager = (LinearLayoutManager) recycle_record_list.getLayoutManager();
                    //Toast.makeText(getActivity(), "" + manager.findFirstCompletelyVisibleItemPosition(), Toast.LENGTH_SHORT).show();
                }


                //   manager.scrollToPositionWithOffset(manager.find() + 1,0);


/*
                RecyclerView.SmoothScroller smoothScroller = new
                        LinearSmoothScroller(getActivity()) {
                            @Override protected int getVerticalSnapPreference() {
                                return LinearSmoothScroller.SNAP_TO_START;
                            }
                        };
                smoothScroller.setTargetPosition();*/


            }
        });


        recycle_notes_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        // mAdapter = new BusinessRecordAdapter(record_nameList, getActivity());
        // recycle_record_list.setAdapter(mAdapter);


        callClientDetails();


        food_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_food_pop_up_window();
            }
        });

        add_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ADDNOTES", "ADDNOTES");
                add_notes_pop_up_window();

            }
        });


        img_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                fragmentCall_mainObj.Fragment_call(null, new RecordCreationFragment(), "addrecord", bundle);

            }
        });


        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "CLIENT_VW_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "view_pet_dtl");


            fragmentTransaction.addToBackStack("view_pet_dtl");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "CLIENT_VW_STATE", this);
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
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(circle_resize_opts)
                                    .build();
                    imgPet.setImageRequest(imageRequest2);
                    imgPet.getHierarchy().setRoundingParams(roundingParams);
                    Call_pet_photo_update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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

    public void Call_pet_photo_update() {
        if (pet_id.trim().equalsIgnoreCase("") || (imgByteStr.trim().equalsIgnoreCase(""))) {
        } else if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._pet_photo_update(pet_id, imgByteStr)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), gen_response_model.getResult(), Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

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

    public void callClientDetails() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._pet_details(pet_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PetDetailsModel>() {
                        @Override
                        public void onNext(PetDetailsModel gen_response_model) {
                            dialog.dismiss();

                            petDetailObj = gen_response_model.getPet_details().get(0);
                            profile_dob.setText("DOB: " + petDetailObj.getDob());
                            profile_name.setText(petDetailObj.getPet_name());
                            brand_value.setText(petDetailObj.getBrand());
                            servings_value.setText(petDetailObj.getPortion_size());
                            protein_value.setText(petDetailObj.getProtein());
                            if (gen_response_model.getShared_with().trim().equalsIgnoreCase("empty")) {
                                txtSharedWithPersons.setText("Not shared with anyone");
                                txtSharedWithPersons.setGravity(Gravity.CENTER);
                                txtSharedWithPersons.setTextColor(Color.parseColor("#dc4a2b"));
                            } else {
                                txtSharedWithPersons.setText(gen_response_model.getShared_with());
                                txtSharedWithPersons.setGravity(Gravity.START);
                                txtSharedWithPersons.setTextColor(Color.parseColor("#000000"));
                            }

                          /*  Glide.with(getContext())

                                    .load(petDetailObj.getPhoto())
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

                            if (petDetailObj.getPhoto().trim().equalsIgnoreCase("")) {
                                Uri uri = new Uri.Builder()
                                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                                        .path(String.valueOf(R.drawable.dogandcat))
                                        .build();
                                final ImageRequest imageRequest2 =
                                        ImageRequestBuilder.newBuilderWithSource(uri)
                                                .setResizeOptions(circle_resize_opts)
                                                .build();
                                imgPet.setImageRequest(imageRequest2);
                            } else {
                                final ImageRequest imageRequest2 =
                                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(petDetailObj.getPhoto()))
                                                .setResizeOptions(circle_resize_opts)
                                                .build();
                                imgPet.setImageRequest(imageRequest2);
                            }
                            imgPet.getHierarchy().setRoundingParams(roundingParams);
                            if (gen_response_model.getNotes_list().get(0).getNotes().equalsIgnoreCase("empty")) {
                                txt_notes_no_data_items.setVisibility(View.VISIBLE);

                            } else {
                                txt_notes_no_data_items.setVisibility(View.GONE);
                                setNotesAdapter(gen_response_model.getNotes_list());
                                notest_list_obj = gen_response_model.getNotes_list();
                                if (gen_response_model.getNotes_list().size() >= 3) {
                                    see_more_text.setVisibility(View.VISIBLE);
                                    see_more_layout.setVisibility(View.VISIBLE);
                                } else {
                                    see_more_text.setVisibility(View.GONE);
                                    see_more_layout.setVisibility(View.GONE);
                                }
                            }
                            if (gen_response_model.getDocuments_list().get(0).getDocument().equalsIgnoreCase("empty")) {
                                txt_rec_no_data_items.setVisibility(View.VISIBLE);
                            } else {
                                List<DocumentModel> TempDocList = new ArrayList<>(gen_response_model.getDocuments_list());


                                Collections.sort(TempDocList, new Comparator<DocumentModel>() {
                                    public int compare(DocumentModel s1, DocumentModel s2) {
                                        // Write your logic here.

                                        return (s1.getDocuments_id().compareToIgnoreCase(s2.getDocuments_id()));
                                    }
                                });
                                setRecordsAdapter(TempDocList);
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
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //    business.requestFocus();


        AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "", false);
    }

    public void setNotesAdapter(List<NotesModel> notesModelList) {
        NotesAdapter notesAdapter = new NotesAdapter(notesModelList, getContext());
        recycle_notes_list.setAdapter(notesAdapter);

    }

    public void setRecordsAdapter(List<DocumentModel> documentModels) {
        RecordsAdapter recordsAdapter = new RecordsAdapter(documentModels, getContext(), this);
        recycle_record_list.setAdapter(recordsAdapter);
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


    public void add_notes_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.notes_layout, null);

        alertDialog.setView(dialogView);

        dlg_notes_add = alertDialog.create();

        dlg_notes_add.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_notes_add.cancel();
            }
        });


        TextView title_text = (TextView) dialogView.findViewById(R.id.notes_text);
        title_text.setText("Notes");
        final EditText enter_notes = (EditText) dialogView.findViewById(R.id.enter_notes);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String notes = enter_notes.getText().toString().trim();
                if (notes.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please enter notes", Toast.LENGTH_LONG).show();
                } else
                    _call_addNotes(notes);


            }
        });

        alertDialog.setView(dialogView);


        dlg_notes_add.show();


    }


    public void _call_addNotes(String notes) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._notes_Creation(userModel.getOwner_id(), pet_id, notes, "owner")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();
                            dlg_notes_add.cancel();
                            Toast.makeText(getContext(), gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            callClientDetails();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            dlg_notes_add.cancel();
                            Toast.makeText(getContext(), "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            dlg_notes_add.cancel();
                        }
                    }));
        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }


    public void edit_food_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.food_edit_layout, null);

        alertDialog.setView(dialogView);

        dlg_notes_add = alertDialog.create();

        dlg_notes_add.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_notes_add.cancel();
            }
        });

        TextView title_text = (TextView) dialogView.findViewById(R.id.title);
        final EditText brand_value = (EditText) dialogView.findViewById(R.id.brand_value);
        final EditText protein_value = (EditText) dialogView.findViewById(R.id.protein_value);
        //  protein_value.setVisibility(View.GONE);
        final EditText servings_value = (EditText) dialogView.findViewById(R.id.servings_value);

        title_text.setText("Edit Food Preferences");

        brand_value.setText(petDetailObj.getBrand());
        protein_value.setText(petDetailObj.getProtein());
        servings_value.setText(petDetailObj.getPortion_size());

        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String brandVal = brand_value.getText().toString().trim();
                String protVal = protein_value.getText().toString().trim();
                String serVal = servings_value.getText().toString().trim();


                _update_food_pref(brandVal, protVal, serVal);


            }
        });


        alertDialog.setView(dialogView);


        dlg_notes_add.show();

    }

    public void _update_food_pref(String brand, String protVal, String servVal) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog prog_dlg = new SpotsDialog(getContext());
            prog_dlg.setCancelable(false);
            prog_dlg.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._food_update(userModel.getOwner_id(), pet_id, brand, protVal, servVal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            prog_dlg.dismiss();
                            dlg_notes_add.dismiss();
                            Toast.makeText(getContext(), gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            callClientDetails();
                        }

                        @Override
                        public void onError(Throwable e) {
                            prog_dlg.dismiss();
                            dlg_notes_add.cancel();
                            Toast.makeText(getContext(), "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            prog_dlg.dismiss();
                            dlg_notes_add.cancel();
                        }
                    }));
        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void getRecordsinfo(DocumentModel documentModel) {

        Bundle bundle = new Bundle();

        bundle.putParcelable("sel_doc_obj", documentModel);
        fragmentCall_mainObj.Fragment_call(null, new RecordViewDetails(), "recordview", bundle);

    }


}
