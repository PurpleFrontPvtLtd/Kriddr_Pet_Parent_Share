package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.SocialFeedAdapter;
import com.Model.Client_collection_model;
import com.Model.Client_info_Model;
import com.Model.Gen_Response_Model;
import com.Model.PostDetailModel;
import com.Model.PostResponseModel;
import com.Model.ResponseModel;
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

public class OwnFeedFragment extends Fragment implements SocialFeedAdapter.SocialAdapterInterface, ViewTreeObserver.OnScrollChangedListener {
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    RecyclerView lo_own_feed_contr;
    UserModel userModelObj;


    EditText edtSharePetsPost;
    AlertDialog dlg_reprt;

    RelativeLayout shareContr;
    ImageView imgPrev, imgNext;
    SimpleDraweeView imgPets, imgSharePets;
    List<Client_info_Model> Pet_List = null;

    TextView txtPetDispName;
    Client_info_Model sel_ClientInfo;
    RelativeLayout txtNoFeeds;
    TextView txtNoFeedProf;
    boolean isComtClicked = false;
    public static ResizeOptions mResizeOptions, circle_resize_opts;

    ScrollView scroll_Post;
    SocialFeedAdapter socialFeedAdapter;
    CardView cardContr;
    boolean isShareClicked = false;
    List<PostDetailModel> postDetailModelListOBJ = null;
    RelativeLayout rl_pet_dtls;
    boolean isEditPostClicked = false;
    int Pag_Index = 0;
    int ClientCount = 0;
    // ImageView img_Dots;
    int RestrictToCallMultiple = 0;
    int INDEX_CREATE_VIEW = -1;
    public static final int Like_Call = 100;
    public static final int Share_Call = 101;
    public static final int Comment_Call = 102;
    public static final int Edit_Del_Call = 103;
    public static final int Block_Report_call = 104;

    View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_own_pet_feed_fragment, container, false);
        imgPets = (SimpleDraweeView) rootView.findViewById(R.id.imgPets);
        imgPrev = (ImageView) rootView.findViewById(R.id.imgPrevPets);
        imgNext = (ImageView) rootView.findViewById(R.id.imgNextPets);
        imgSharePets = (SimpleDraweeView) rootView.findViewById(R.id.imgPetsPic);
        txtNoFeeds = (RelativeLayout) rootView.findViewById(R.id.rl_noFeeds);
        rl_pet_dtls = (RelativeLayout) rootView.findViewById(R.id.rl_pet_dtls);

        txtNoFeedProf = (TextView) rootView.findViewById(R.id.txtNoFeeds2);


        shareContr = (RelativeLayout) rootView.findViewById(R.id.shareContr);
        txtPetDispName = (TextView) rootView.findViewById(R.id.txtPetDispName);
        cardContr = (CardView) rootView.findViewById(R.id.cardContr);
        scroll_Post = (ScrollView) rootView.findViewById(R.id.scroll_post);

        lo_own_feed_contr = (RecyclerView) rootView.findViewById(R.id.lo_ownpets_feed_contr);
        edtSharePetsPost = (EditText) rootView.findViewById(R.id.edtSharePetsPost);


        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.setViewInvisible();
        Pag_Index = 0;
        RestrictToCallMultiple = 0;
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgNotify().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgSearch().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setText("FEED");
        actionBarUtilObj.getTitle().setGravity(Gravity.CENTER);
        KridderNavigationActivity.setNavigaationVisible(true);
        scroll_Post.getViewTreeObserver().addOnScrollChangedListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        lo_own_feed_contr.setLayoutManager(linearLayoutManager);
        lo_own_feed_contr.setNestedScrollingEnabled(false);
        circle_resize_opts = new ResizeOptions(200, 200);
        actionBarUtilObj.getImgNotify().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                call_notify_frag();
            }
        });

        txtNoFeedProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genFragmentCall_mainObj.Fragment_call(null, new View_Parent_Profile(), "edit_prof", null);
            }
        });
        rl_pet_dtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle=new Bundle();
             /*   Bundle bundle = new Bundle();

                bundle.putInt("Pet_Pos", ClientCount);
                bundle.putString("pet_id", sel_ClientInfo.getPet_id());
                genFragmentCall_mainObj.Fragment_call(null,new MyPostImageFragement(), "My_past", bundle);*/
                call_mypost_frag();
            }
        });


        lo_own_feed_contr.removeAllViewsInLayout();

        PetList_ServiceCall();


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
                        INDEX_CREATE_VIEW = -1;
                        lo_own_feed_contr.removeAllViewsInLayout();
                        Pag_Index = 0;
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
                        lo_own_feed_contr.removeAllViewsInLayout();
                        postDetailModelListOBJ = null;
                        INDEX_CREATE_VIEW = -1;
                        Pag_Index = 0;
                        PostFeedServiceCall();
                    }
                }
            }
        });
        edtSharePetsPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Pet_List.size() > 0) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("PetID", sel_ClientInfo.getPet_id());
                    bundle.putString("PetName", sel_ClientInfo.getPet_name());
                    bundle.putString("PetPhoto", sel_ClientInfo.getPhoto());


                    genFragmentCall_mainObj.Fragment_call(null,new SharePostFragment(), "Share_Post", bundle);*/
                    call_create_post();
                } else {

                }
            }
        });
        actionBarUtilObj.getImgSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Pet_List != null) {
                   /* Bundle bundle = new Bundle();
                    bundle.putString("pet_id", sel_ClientInfo.getPet_id());


                    bundle.putInt("Pet_Pos", ClientCount);
                    bundle.putString("pet_id", sel_ClientInfo.getPet_id());
                    genFragmentCall_mainObj.Fragment_call(null, new SearchPetsFragment(), "Search_Pets", bundle);*/
                    search_pets_frag();
                } else {
                    Toast.makeText(getContext(), "Your Pet list is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void call_notify_frag() {

        Bundle bundle = new Bundle();
        bundle.putString("owner_id", userModelObj.getOwner_id());
        bundle.putInt("Pet_Pos", ClientCount);

        genFragmentCall_mainObj.Fragment_call(this, new NotificationFragment(), "nfy_frag", bundle);

    }

    public void search_pets_frag() {
        if (Pet_List != null) {
            Bundle bundle = new Bundle();
            bundle.putString("pet_id", sel_ClientInfo.getPet_id());


            bundle.putInt("Pet_Pos", ClientCount);
            bundle.putString("pet_id", sel_ClientInfo.getPet_id());
            genFragmentCall_mainObj.Fragment_call(this, new SearchPetsFragment(), "Search_Pets", bundle);
        } else {
            Toast.makeText(getContext(), "No pets are added yet", Toast.LENGTH_LONG).show();
        }
    }

    public void call_create_post() {
        Bundle bundle = new Bundle();
        bundle.putString("PetID", sel_ClientInfo.getPet_id());
        bundle.putString("PetName", sel_ClientInfo.getPet_name());
        bundle.putString("PetPhoto", sel_ClientInfo.getPhoto());

        bundle.putString("scrn_from", "own_feed");
        genFragmentCall_mainObj.Fragment_call(this, new SharePostFragment(), "Share_Post", bundle);
    }

    public void call_edit_post(PostDetailModel _postDtlMdlObj) {
        Bundle bundle = new Bundle();
        bundle.putString("PetID", sel_ClientInfo.getPet_id());
        bundle.putString("PetName", sel_ClientInfo.getPet_name());
        bundle.putString("PetPhoto", sel_ClientInfo.getPhoto());
        bundle.putString("Post_id", _postDtlMdlObj.getPet_posts_id());
        bundle.putString("Post_img", _postDtlMdlObj.getImage());
        bundle.putString("Post_desc", _postDtlMdlObj.getDescription());
        bundle.putString("scrn_from", "own_feed");
        genFragmentCall_mainObj.Fragment_call(this, new SharePostFragment(), "edit_Post", bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        lo_own_feed_contr.addOnLayoutChangeListener(
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

    public void setBottomDialog(final PostDetailModel _postDtlMdlObj, final int SelPos, final boolean isReprt_Block) {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        if (!isReprt_Block) {
            menu_list.add("Edit Post");
            menu_list.add("Delete Post");
        } else {
            menu_list.add("Report post");
            menu_list.add("Block the user");
        }
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
                                if (isReprt_Block) {
                                    show_block_dlg(_postDtlMdlObj.getOwner_id());
                                } else {
                                    _del_Post_CALL(SelPos, _postDtlMdlObj);
                                }
                                break;
                            case 0:
                                if (isReprt_Block) {
                                    report_popup(_postDtlMdlObj.getPet_posts_id());
                                } else {
                                    isEditPostClicked = true;
                                    call_edit_post(_postDtlMdlObj);
                                }

                                break;

                        }
                    }
                }
        );

    }


    public void report_popup(final String PostId) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.notes_layout, null);

        alertDialog.setView(dialogView);

        dlg_reprt = alertDialog.create();

        dlg_reprt.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_reprt.cancel();
            }
        });


        TextView title_text = (TextView) dialogView.findViewById(R.id.notes_text);
        title_text.setText("Please give the reason for reporting this post");
        final EditText enter_notes = (EditText) dialogView.findViewById(R.id.enter_notes);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);
        enter_notes.setHint("Comments");
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String coments = enter_notes.getText().toString().trim();
                if (coments.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Please enter comments", Toast.LENGTH_LONG).show();
                } else
                    report_user_api_call(coments,PostId);


            }
        });

        alertDialog.setView(dialogView);


        dlg_reprt.show();


    }

    public void report_user_api_call(String comments,String post_id) {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._report_post(sel_ClientInfo.getPet_id() , userModelObj.getOwner_id(),post_id, comments)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ResponseModel>() {
                        @Override
                        public void onNext(ResponseModel gen_response_model) {
                            dialog.dismiss();
                            dlg_reprt.cancel();
                            Toast.makeText(getContext(), gen_response_model.getResponse(), Toast.LENGTH_SHORT).show();
                            //callClientDetails();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            dlg_reprt.cancel();
                            Toast.makeText(getContext(), "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                            dlg_reprt.cancel();
                        }
                    }));
        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.net_con), Toast.LENGTH_LONG).show();
        }
    }

    public void show_block_dlg(final String block_OwnerId) {
        AlertDialog.Builder dlg_buildr = new AlertDialog.Builder(getContext());
        dlg_buildr.setMessage("Are you sure want to block this user so that you want see any future posts from this user?")
                .setCancelable(false)
                .setPositiveButton("Block", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        block_user_api_call(block_OwnerId);
                        RestrictToCallMultiple = 0;
                        lo_own_feed_contr.removeAllViewsInLayout();
                        postDetailModelListOBJ = null;
                        Pag_Index = 0;
                        PostFeedServiceCall();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = dlg_buildr.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "OWNFEED_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame_layout, mContent, "frag_feed");


            fragmentTransaction.addToBackStack("frag_feed");

            fragmentTransaction.commit();

        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "OWNFEED_STATE", this);
    }

    public void block_user_api_call(String blocking_ownerId){
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._block_user(blocking_ownerId, userModelObj.getOwner_id())
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
        Pag_Index = 0;
        scroll_Post.getViewTreeObserver().removeOnScrollChangedListener(this);
    }

    public void _fragment_handelBackKey(final Context context, final String msg) {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {

                    setRemoveScrollChangeListener();
                    if (!msg.equalsIgnoreCase("")) {
                        AlertDialogHandler.showDialog(getActivity(), msg, true);
                        // ((AppCompatActivity) context).getSupportFragmentManager().popBackStackImmediate();
                    }

                    return true;
                }
                return false;
            }
        });

    }

    public void call_mypost_frag() {
        Bundle bundle = new Bundle();

        bundle.putInt("Pet_Pos", ClientCount);
        bundle.putString("pet_id", sel_ClientInfo.getPet_id());
        genFragmentCall_mainObj.Fragment_call(this, new MyPostImageFragement(), "My_Post", bundle);
    }

    public void onResume() {
        super.onResume();
        _fragment_handelBackKey(getContext(), "Do you want to exit?");
        //  AlertDialogHandler._fragment_handelBackKey(rootView,getActivity(),"Do u want to exit?",true);
    }

    public void PostFeedServiceCall() {

        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();

            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._getPetFeedDtls(sel_ClientInfo.getPet_id(), sel_ClientInfo.getOwner_id(), String.valueOf(Pag_Index))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<PostResponseModel>() {
                        @Override
                        public void onNext(PostResponseModel client_collection_model_obj) {
                            dialog.dismiss();
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            if (Pag_Index == 0) {
                                lo_own_feed_contr.removeAllViewsInLayout();
                            }
                            if (client_collection_model_obj != null) {
                                PostDetailModel info_model = client_collection_model_obj.getPosts_details().get(0);
                                if (info_model.getPet_posts_id().equalsIgnoreCase("empty")) {

                                } else {
                                    if (postDetailModelListOBJ == null)
                                        postDetailModelListOBJ = client_collection_model_obj.getPosts_details();
                                    else {
                                        List<PostDetailModel> temPostDetailModels = client_collection_model_obj.getPosts_details();
                                        postDetailModelListOBJ.addAll(temPostDetailModels);
                                    }
                                    setFeedAdapter();


                                }
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

    public void setFeedAdapter() {
        socialFeedAdapter = new SocialFeedAdapter(getContext(), postDetailModelListOBJ, sel_ClientInfo.getPet_id(), this, false);
        lo_own_feed_contr.setAdapter(socialFeedAdapter);

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
                            postDetailModelListOBJ = null;
                            INDEX_CREATE_VIEW = -1;
                            lo_own_feed_contr.removeAllViewsInLayout();
                            Pag_Index = 0;
                            // Toast.makeText(getContext(),"Owner"+userModel.getOwner_id(),Toast.LENGTH_SHORT).show();
                            Client_info_Model info_model = client_collection_model_obj.getResponse().get(0);
                            if (info_model.getPet_id().trim().equalsIgnoreCase("") || info_model.getPet_id().trim().equalsIgnoreCase("empty")) {

                            } else {
                                Pet_List = client_collection_model_obj.getResponse();
                                sel_ClientInfo = Pet_List.get(0);
                                ShowClientDetails();

                                PostFeedServiceCall();

                            }
                            if (Pet_List != null) {
                                txtNoFeeds.setVisibility(View.GONE);
                                cardContr.setVisibility(View.VISIBLE);
                                shareContr.setVisibility(View.VISIBLE);
                            } else {
                                txtNoFeeds.setVisibility(View.VISIBLE);
                                cardContr.setVisibility(View.GONE);
                                shareContr.setVisibility(View.GONE);
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
                shareContr.setVisibility(View.VISIBLE);
            } else {
                txtNoFeeds.setVisibility(View.VISIBLE);
                // txtNoFeeds.setText("Please check network connection");
                cardContr.setVisibility(View.GONE);
                shareContr.setVisibility(View.GONE);
            }

        }

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

    public void ShowClientDetails() {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        //roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);

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
        GenericDraweeHierarchy hierarchy2 = builder
                .setFadeDuration(100)
                .build();

        imgPets.setHierarchy(hierarchy);
        imgSharePets.setHierarchy(hierarchy2);
        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(sel_ClientInfo.getPhoto()))
                        .setResizeOptions(circle_resize_opts)
                        .build();
        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {


            //  holder.imgPostPic.setImageRequest(imageRequest);
            imgSharePets.setImageRequest(imageRequest);
            imgPets.setImageRequest(imageRequest);
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgSharePets.setImageRequest(imageRequest2);
            imgPets.setImageRequest(imageRequest2);

        }

        imgPets.getHierarchy().setRoundingParams(roundingParams);
        imgSharePets.getHierarchy().setRoundingParams(roundingParams);


/*
        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {
         */
/*   Glide.with(getContext())

                    .load(sel_ClientInfo.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgSliderPets.setImageDrawable(drawable);

                        }


                    });
*//*

            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            //roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            imgSliderPets.getHierarchy().setRoundingParams(roundingParams);

            imgSliderPets.setImageURI(sel_ClientInfo.getPhoto());
        } else {
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.dogandcat);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), icon);
            drawable.setCircular(true);
            imgSliderPets.setImageDrawable(drawable);
        }
*/
/*        if (!sel_ClientInfo.getPhoto().equalsIgnoreCase("")) {
            Glide.with(getContext())

                    .load(sel_ClientInfo.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            imgSharePets.setImageDrawable(drawable);

                        }


                    });
        } else {
           *//* Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.dogandcat);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), icon);
            drawable.setCircular(true);*//*
            imgSharePets.setImageResource(R.drawable.dogandcat);
        }*/
        txtPetDispName.setText(sel_ClientInfo.getPet_name());
        edtSharePetsPost.setHint("Share what " + sel_ClientInfo.getPet_name() + " is up to");
    }

  /*  public void getFeedView() {
        for (int i = INDEX_CREATE_VIEW + 1; i < postDetailModelListOBJ.size(); i++) {
            final PostDetailModel postDetailModelOBJ = postDetailModelListOBJ.get(i);
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.card_feed_dtl, null);
            final RelativeLayout rl_shard_by = (RelativeLayout) view.findViewById(R.id.rl_shard_by);
            final SimpleDraweeView imgPostPic = (SimpleDraweeView) view.findViewById(R.id.imgFeedPic);
            final ImageView imgPetPic = (ImageView) view.findViewById(R.id.imgProfPic);
            final RelativeLayout rl_cmnt_dots = (RelativeLayout) view.findViewById(R.id.rl_cmnt_dots);
            final RelativeLayout rl_comnt_contr = (RelativeLayout) view.findViewById(R.id.rl_comnt_contr);
            final RelativeLayout rl_share_contr = (RelativeLayout) view.findViewById(R.id.rl_shr_contr);
            final ImageView img_Dots = (ImageView) view.findViewById(R.id.img_dots);
            final TextView txtCmntCount = (TextView) view.findViewById(R.id.txtComntCount);
            final RelativeLayout rl_like_contr = (RelativeLayout) view.findViewById(R.id.rl_like_contr);
            final ImageView imgLike = (ImageView) view.findViewById(R.id.imgLike);
            final TextView txtShrCnt = (TextView) view.findViewById(R.id.txtShreCnt);
            final TextView txtLikeCnt = (TextView) view.findViewById(R.id.txtLikeCnt);
            final TextView txtPetName = (TextView) view.findViewById(R.id.txtPetName);
            final TextView txtPostMessage = (TextView) view.findViewById(R.id.txtPostContent);
            final TextView txtPostDate = (TextView) view.findViewById(R.id.txtPostDate);
            final ImageView img_comments_Count = (ImageView) view.findViewById(R.id.imgCommentCountPic);
            int CommentCntVal = Integer.parseInt(postDetailModelOBJ.getComments_counts());
            if (postDetailModelOBJ.getShared_by().get(0).getName().equalsIgnoreCase("empty")) {

            } else {
                rl_shard_by.setVisibility(View.VISIBLE);
                ((TextView) rl_shard_by.findViewById(R.id.txtShrdByName)).setText("Shared by " + postDetailModelOBJ.getShared_by().get(0).getName());
                final ImageView imgShrdByPets = ((ImageView) rl_shard_by.findViewById(R.id.img_shrdByPets));
                if (!postDetailModelOBJ.getShared_by().get(0).getPhoto().equalsIgnoreCase("")) {


                    Glide.with(getContext())

                            .load(postDetailModelOBJ.getShared_by().get(0).getPhoto())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                    drawable.setCircular(true);
                                    imgShrdByPets.setImageDrawable(drawable);

                                }


                            });
                }
            }
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

            rl_cmnt_dots.setZ(25.0f);

            if (!postDetailModelOBJ.getLike_status().equalsIgnoreCase("no")) {
                imgLike.setImageResource(R.drawable.like_fill);
                imgLike.setTag(0);//Like Filled
            } else {
                imgLike.setTag(1);//Not Filled
            }
            if (sel_ClientInfo.getPet_id().equalsIgnoreCase(postDetailModelOBJ.getPet_id())) {
                img_Dots.setVisibility(View.VISIBLE);
                img_Dots.setTag(i);
                //   rl_share_contr.setVisibility(View.GONE);
           *//*    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(30,30);
                layoutParams.setMargins(0,0,30,0);
                img_comments_Count.setLayoutParams(layoutParams);*//*
                // img_Dots.setZ(25.0f);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
                layoutParams.setMargins(0, 3, 2, 0);
                //  txtCmntCount.setLayoutParams(layoutParams);
                img_Dots.setVisibility(View.GONE);

            }

            txtPetName.setText(postDetailModelOBJ.getPet_name());
            txtPostDate.setText(postDetailModelOBJ.getCreated());
            img_Dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Pos = (int) view.getTag();

                    setBottomDialog(postDetailModelListOBJ.get(Pos), Pos);

                }
            });
            rl_comnt_contr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Pos = (int) v.getTag();
                    final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("postDtlObj", sel_postDetailModelOBJ);
                    bundle.putString("PetID", sel_ClientInfo.getPet_id());


                    bundle.putInt("Pet_Pos", ClientCount);
                    bundle.putString("pet_id", sel_ClientInfo.getPet_id());
                    genFragmentCall_mainObj.Fragment_call(new CommentScreen(), "CrteCommet", bundle);
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
                        //int count=Integer.parseInt(txtLikeCnt.getText().toString());

                    } else {
                        imgLike.setImageResource(R.drawable.like);

                        imgLike.setTag(1);
                    }


                    _likePost_CALL(sel_ClientInfo.getPet_id(), sel_postDetailModelOBJ.getPet_posts_id());
                }
            });
            rl_share_contr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Pos = (int) v.getTag();
                    final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                    //Toast.makeText(getContext(), "Pet_id" + sel_postDetailModelOBJ.getPet_posts_id(), Toast.LENGTH_SHORT).show();
                    isShareClicked = true;
                    sharePost_Call(sel_postDetailModelOBJ);
                }
            });
            txtPostMessage.setText(postDetailModelOBJ.getDescription());
            if (postDetailModelOBJ.getImage().equalsIgnoreCase("")) {

                imgPostPic.setVisibility(View.GONE);
*//*
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) img_comments_Count.getLayoutParams();
                if (postDetailModelOBJ.getPet_id().equalsIgnoreCase(sel_ClientInfo.getPet_id())) {
                    buttonLayoutParams.setMargins(0, 0, 80, 0);
                    img_comments_Count.setLayoutParams(buttonLayoutParams);
                    buttonLayoutParams.setMargins(0, 0, 40, 0);
                    txtCmntCount.setLayoutParams(buttonLayoutParams);
                }
*//*


            } else {
              *//*  Glide.with(getContext())

                        .load(postDetailModelOBJ.getImage())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                imgPostPic.setImageBitmap(resource);

                            }
                        });*//*
                imgPostPic.setImageURI(postDetailModelOBJ.getImage());
            }
            if (postDetailModelOBJ.getPet_photo().equalsIgnoreCase("")) {

            } else {
                Glide.with(getContext())

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
                        });
            }
            ///lo_own_feed_contr.addView(view);
            INDEX_CREATE_VIEW = i;
        }
        if (isShareClicked) {
            //  scroll_Post.scrollTo(0, 0);
            isShareClicked = false;
        }
        //  lo_own_feed_contr.invalidate();
    }*/

    @Override
    public void onHiddenChanged(boolean hidden) {
        //Fragment fragment=this;
        //Toast.makeText(getContext(),"I am Showing"+hidden,Toast.LENGTH_LONG).show();
        if (!hidden) {
            RestrictToCallMultiple = 0;
            lo_own_feed_contr.removeAllViewsInLayout();
            postDetailModelListOBJ = null;
            Pag_Index = 0;
            actionBarUtilObj.setViewInvisible();
            actionBarUtilObj.getImgNotify().setVisibility(View.VISIBLE);
            actionBarUtilObj.getImgSearch().setVisibility(View.VISIBLE);
            actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
            actionBarUtilObj.setTitle("FEED");
            actionBarUtilObj.getTitle().setGravity(Gravity.CENTER_HORIZONTAL);
            KridderNavigationActivity.setNavigaationVisible(true);
            actionBarUtilObj.getImgSearch().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Pet_List != null) {
                   /* Bundle bundle = new Bundle();
                    bundle.putString("pet_id", sel_ClientInfo.getPet_id());


                    bundle.putInt("Pet_Pos", ClientCount);
                    bundle.putString("pet_id", sel_ClientInfo.getPet_id());
                    genFragmentCall_mainObj.Fragment_call(null, new SearchPetsFragment(), "Search_Pets", bundle);*/
                        search_pets_frag();
                    } else {
                        Toast.makeText(getContext(), "Your Pet list is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (Pet_List != null)
                PostFeedServiceCall();
        }

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

    public void _del_Post_CALL(final int SelPos, PostDetailModel postDetailModelObj) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._del_Post(sel_ClientInfo.getPet_id(), userModelObj.getOwner_id(), postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ResponseModel>() {
                        @Override
                        public void onNext(ResponseModel gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResponse(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            //  lo_own_feed_contr.requestFocus();
                            //lo_own_feed_contr.removeViewAt(SelPos);
                            lo_own_feed_contr.removeAllViewsInLayout();
                            postDetailModelListOBJ = null;
                            INDEX_CREATE_VIEW = -1;
                            Pag_Index = 0;
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

    public void sharePost_Call(PostDetailModel postDetailModelObj) {
        if (NetworkConnection.isOnline(getContext())) {

            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._share_post(sel_ClientInfo.getPet_id(), sel_ClientInfo.getOwner_id(), postDetailModelObj.getPet_posts_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                        @Override
                        public void onNext(Gen_Response_Model gen_response_model) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "" + gen_response_model.getResult(), Toast.LENGTH_SHORT).show();
                            //((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                            RestrictToCallMultiple = 0;
                            lo_own_feed_contr.removeAllViewsInLayout();
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
                            PostFeedServiceCall();
                        }
                    }));
        }
    }


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
                            int likecount = Integer.parseInt(postDetailModel.getLikes_counts());

                            if (postDetailModel.getLike_status().trim().equalsIgnoreCase("no")) {
                                postDetailModel.setLike_status("yes");
                                likecount++;
                            } else {
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

    @Override
    public void onScrollChanged() {
        if (scroll_Post != null) {
            if (scroll_Post.getChildAt(0).getBottom() <= (scroll_Post.getHeight() + scroll_Post.getScrollY())) {
                //scroll view is at bottom
                //  Toast.makeText(getContext(),"ScrollView end",Toast.LENGTH_SHORT).show();
                if (RestrictToCallMultiple == 0 || (scroll_Post.getChildAt(0).getBottom() != RestrictToCallMultiple)) {
                    Pag_Index++;
                    if (Pet_List != null)
                        PostFeedServiceCall();
                    RestrictToCallMultiple = scroll_Post.getChildAt(0).getBottom();
                }
            } else {
                //scroll view is not at bottom
            }
        }
    }

    @Override
    public void _callService(PostDetailModel postDetailModelObj, int Options, int AdapterPos) {
        switch (Options) {
            case Like_Call:
                _likePost_CALL(sel_ClientInfo.getPet_id(), postDetailModelObj.getPet_posts_id(), AdapterPos);
                break;
            case Share_Call:
                RestrictToCallMultiple = 0;

                sharePost_Call(postDetailModelObj);
                break;
            case Comment_Call:
                Bundle bundle = new Bundle();
                bundle.putParcelable("postDtlObj", postDetailModelObj);
                bundle.putString("PetID", sel_ClientInfo.getPet_id());
                bundle.putString("scrn_from", "own_feed");
                isComtClicked = true;
                genFragmentCall_mainObj.Fragment_call(this, new CommentScreen(), "CrteCommet", bundle);
                break;
            case Edit_Del_Call:

                setBottomDialog(postDetailModelObj, AdapterPos, false);
                break;
            case Block_Report_call:
                setBottomDialog(postDetailModelObj, AdapterPos, true);

        }
    }
}
