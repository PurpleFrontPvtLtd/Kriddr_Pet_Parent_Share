package com.navigation.scrn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.TimeZone;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Gen_Response_Model;
import com.Model.PetActivityCreatedDtlModel;
import com.Model.Pet_Activity_Model;
import com.Model.UserModel;
import com.api.ApiClient;
import com.api.ApiInterface;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchyInflater;
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
import com.util.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 05-04-2018.
 */

public class Create_PetActivity extends Fragment implements MediaPlayer.OnBufferingUpdateListener {
    View rootView;
    SeekBar audio_seek_bar;

    ImageView img_play_stop;
    LinearLayout scroll_acty_list;
    private static int RECORDER_SAMPLE_RATE = 22050;
    private static final int RECORDER_CHANNELS = 1;
    private static final int RECORDER_ENCODING_BIT = 16;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_DECIBELS = 120;
    private AudioTrack track = null;
    String PetId, PetIMG;
    TextView txt_PlayProgTime;
    String activity_sel_id = "";
    private final Handler handler = new Handler();
    MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    boolean isAudioPrepare = false, isPlaying = false;
    String TotalTime;
    UserModel usrModelobj;
    String SCRN_FROM;
    Button btnSubmit;
    LinearLayout ll_show_act_contr, ll_act_cr_contr;
    String filePath;
    boolean act_clicked;
    TextView txtDate, txtTime, txtDesc;
    PetActivityCreatedDtlModel pet_act_list_obj = null;
    EditText edt_opt_detls;
    TextView txtCrtdActy;
    SimpleDraweeView img_act_crtd, img_pet;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    int PREVIOUS_POS_SEL = -1;

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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "CRT_ACT_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "act_click");


            fragmentTransaction.addToBackStack("act_click");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "CRT_ACT_STATE", this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_activity_cr_edt, null);
        audio_seek_bar = (SeekBar) rootView.findViewById(R.id.seek_audio_rec);
        img_pet = (SimpleDraweeView) rootView.findViewById(R.id.img_pet);
        txt_PlayProgTime = (TextView) rootView.findViewById(R.id.txt_PlayProgTime);
        scroll_acty_list = (LinearLayout) rootView.findViewById(R.id.scroll_acty_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        img_act_crtd = (SimpleDraweeView) rootView.findViewById(R.id.img_act_crtd);
        txtCrtdActy = (TextView) rootView.findViewById(R.id.txtCrtdActy);
        //   linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ll_show_act_contr = (LinearLayout) rootView.findViewById(R.id.ll_show_act_contr);
        ll_act_cr_contr = (LinearLayout) rootView.findViewById(R.id.ll_act_cr_contr);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        img_play_stop = (ImageView) rootView.findViewById(R.id.img_play_stop);
        edt_opt_detls = (EditText) rootView.findViewById(R.id.edt_opt_detls);
        txtDesc = (TextView) rootView.findViewById(R.id.txtDesc);
        txtDate = (TextView) rootView.findViewById(R.id.txtDate);
        txtTime = (TextView) rootView.findViewById(R.id.txtTime);
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Back");
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                //   ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                mediaPlayer.pause();
                mediaPlayer.release();
                isPlaying = false;
                hide_fragment();
            }
        });
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.release();
                isPlaying = false;
                // ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();

            }
        });
        Bundle bundle = getArguments();
        filePath = bundle.getString("filePath");
        PetId = bundle.getString("PetID");
        PetIMG = bundle.getString("PetImage");
        SCRN_FROM = bundle.getString("scrn_from");
        pet_act_list_obj = bundle.getParcelable("pet_act_list_obj");
        ResizeOptions resizeOptions = new ResizeOptions(200, 200);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if (pet_act_list_obj == null) {
            ll_act_cr_contr.setVisibility(View.VISIBLE);
            ll_show_act_contr.setVisibility(View.GONE);

        } else {
            ll_act_cr_contr.setVisibility(View.GONE);
            txtCrtdActy.setText(pet_act_list_obj.getActivity());
            ll_show_act_contr.setVisibility(View.VISIBLE);

            try {


                String dateStr = pet_act_list_obj.getDate() + " " + pet_act_list_obj.getTime();
                SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa", Locale.ENGLISH);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = df.parse(dateStr);
                df.setTimeZone(TimeZone.getDefault());

                String formattedDate = df.format(date);
                String Time[] = formattedDate.split(" ");
                txtTime.setText(Time[3] + " " + Time[4]);
            } catch (Exception e) {
                txtTime.setText(pet_act_list_obj.getTime());
            }

          /*  Glide.with(getContext())

                    .load(pet_act_list_obj.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            img_act_crtd.setImageDrawable(drawable);

                        }


                    });*/

            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(pet_act_list_obj.getImage()))
                            .setResizeOptions(resizeOptions)
                            .build();
            img_act_crtd.setImageRequest(imageRequest2);
            img_act_crtd.getHierarchy().setRoundingParams(roundingParams);
            txtDate.setText(pet_act_list_obj.getDate());

            txtDesc.setText(pet_act_list_obj.getDetails());

        }

        audio_seek_bar.setMax(99); // It means 100% .0-99
        audio_seek_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.seek_audio_rec) {
                    /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
                    if (mediaPlayer.isPlaying()) {
                        SeekBar sb = (SeekBar) v;
                        int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                        mediaPlayer.seekTo(playPositionInMillisecconds);
                    } else {
                        img_play_stop.setImageResource(R.drawable.stop_rec);

                        mediaPlayer.start();
                        SeekBar sb = (SeekBar) v;
                        int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                        mediaPlayer.seekTo(playPositionInMillisecconds);
                        isPlaying = true;
                        primarySeekBarProgressUpdater();

                    }
                }
                return false;
            }
        });
        if (!PetIMG.equalsIgnoreCase("")) {

          /*  Glide.with(getContext())

                    .load(PetIMG)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            drawable.setCircular(true);
                            img_pet.setImageDrawable(drawable);

                        }


                    });*/
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(PetIMG))
                            .setResizeOptions(resizeOptions)
                            .build();
            img_pet.setImageRequest(imageRequest2);
            img_pet.getHierarchy().setRoundingParams(roundingParams);
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            img_pet.setImageRequest(imageRequest2);
            img_pet.getHierarchy().setRoundingParams(roundingParams);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Pet_Actiivity();
            }
        });
        if (mediaPlayer == null) {
            try {
                mediaPlayer = new MediaPlayer();
                AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                if (pet_act_list_obj == null) {

                } else {
                    filePath = pet_act_list_obj.getAudio_file();
                }
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.prepareAsync();
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // mp.stop();

                        img_play_stop.setImageResource(R.drawable.play_rec);
                        isPlaying = false;
                        mediaPlayer = mp;
                        //   mp.prepareAsync();
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // to get total duration in milliseconds
                        mediaPlayer = mp;
                        long currentDuration = mediaPlayer.getCurrentPosition();
                        TotalTime = TimeFormat.formateMilliSeccond(mediaFileLengthInMilliseconds);
                        String cur_Pos = TimeFormat.formateMilliSeccond(currentDuration);
                        txt_PlayProgTime.setText(cur_Pos + "/" + TotalTime);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        img_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    img_play_stop.setImageResource(R.drawable.play_rec);
                    isPlaying = false;
                    //mediaPlayer.prepareAsync();
                } else {

                    img_play_stop.setImageResource(R.drawable.stop_rec);

                    mediaPlayer.start();
                    isPlaying = true;
                    primarySeekBarProgressUpdater();

                }
            }
        });
       /* linearLayoutManager.setReverseLayout(false);
        scroll_acty_list.setLayoutManager(linearLayoutManager);*/
//        audio_seek_bar.getThumb().mutate().setAlpha(0);
        _call_pet_act_list();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //  mediaPlayer.pause();

        // AlertDialogHandler._fragment_handelBackKey(rootView, getActivity(), "Do you want to exit?", true);
        //((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    isPlaying = false;
                    mediaPlayer.pause();
                    mediaPlayer.release();
                    // ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();
                    return true;
                }


                return false;
            }
        });
    }

    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        if (isPlaying) {
            audio_seek_bar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
            String cur_Pos = TimeFormat.formateMilliSeccond(mediaPlayer.getCurrentPosition());
            txt_PlayProgTime.setText(cur_Pos + "/" + TotalTime);
            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater();
                    }
                };
                handler.postDelayed(notification, 1000);
            }
        }
    }

    /*  public void getValidSampleRates() {
          for (int rate : new int[] {8000, 11025, 16000, 22050, 44100}) {  // add the rates you wish to check against
              int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
              Toast.makeText(getContext(),"Rate :"+rate,Toast.LENGTH_LONG).show();
              if (bufferSize > 0) {
                  // buffer size is valid, Sample rate supported
                  Toast.makeText(getContext(),"Rate> :"+rate,Toast.LENGTH_LONG).show();
                  RECORDER_SAMPLE_RATE=bufferSize;

              }
          }
      }
  */
    public void _call_pet_act_list() {
        if (NetworkConnection.isOnline(getContext())) {
            final AlertDialog dialog = new SpotsDialog(getContext());
            dialog.setCancelable(false);
            dialog.show();
            ApiInterface requestInterface = ApiClient.getClient();
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface._pet_act_list("activity_list")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<List<Pet_Activity_Model>>() {
                        @Override
                        public void onNext(List<Pet_Activity_Model> s) {
                            dialog.dismiss();
                            setAdapter(s);
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

    public void setAdapter(final List<Pet_Activity_Model> pet_activity_modelList) {
        /*AsyncTaskBG asyncTaskBGObj = new AsyncTaskBG(s);
        asyncTaskBGObj.execute();
*/
        for (int index = 0; index < pet_activity_modelList.size(); index++) {
            final Pet_Activity_Model pet_activity_model = pet_activity_modelList.get(index);
            View v = null;
            final SimpleDraweeView imgActCatg;
            switch (index % 2) {
                case 0:
                    v = LayoutInflater.from(getContext())
                            .inflate(R.layout.lo_act_pos_1, null);

                    break;

                case 1:
                    v = LayoutInflater.from(getContext())
                            .inflate(R.layout.lo_act_pos_2, null);
                    break;
            }
            scroll_acty_list.addView(v);
            imgActCatg = (SimpleDraweeView) v.findViewById(R.id.img_act_pos_1);
            v.setTag(index);
            imgActCatg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PREVIOUS_POS_SEL != -1) {
                        Pet_Activity_Model pet_activity_model1OBJ = pet_activity_modelList.get(PREVIOUS_POS_SEL);
                        RelativeLayout relativeLayout = (RelativeLayout) scroll_acty_list.findViewWithTag(PREVIOUS_POS_SEL);
                        SimpleDraweeView imgPrevAct = (SimpleDraweeView) relativeLayout.findViewById(R.id.img_act_pos_1);
                        load_Image(imgPrevAct, pet_activity_model1OBJ.getImage());
                        load_Image(imgActCatg, pet_activity_model.getImage_selected());
                    } else
                        load_Image(imgActCatg, pet_activity_model.getImage_selected());
                    String tagVal = ((RelativeLayout) v.getParent()).getTag().toString();
                    PREVIOUS_POS_SEL = Integer.parseInt(tagVal);
                    activity_sel_id = pet_activity_model.getId();

                }
            });
            TextView txtActCatg_Name = (TextView) v.findViewById(R.id.txt_act_name_1);
            load_Image(imgActCatg, pet_activity_model.getImage());
            txtActCatg_Name.setText(pet_activity_model.getName());

        }

    }

    public void hide_fragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev_frag = null;
        Fragment cur_frag;
        if (SCRN_FROM.equalsIgnoreCase("act_frag")) {

            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
            //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
            //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
        } else {
            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("rec_voice");
         /*   first_frag=getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
            if (first_frag.isAdded()) {

                ft.hide(first_frag);
            }*/
            //  getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
        if (SCRN_FROM.equalsIgnoreCase("submit")) {
            cur_frag = getActivity().getSupportFragmentManager().findFragmentByTag("rec_voice");
            prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
            if (prev_frag.isAdded()) { // if the fragment is already in container
                //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
                // ft.remove(this);
                ft.remove(this);
                ft.remove(cur_frag);
                ft.show(prev_frag);
           /* prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
            ft.hide(prev_frag);*/
                ft.commit();
            }
        } else if (prev_frag.isAdded()) { // if the fragment is already in container
            //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
            // ft.remove(this);
            ft.remove(this);
            ft.show(prev_frag);
           /* prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
            ft.hide(prev_frag);*/
            ft.commit();
        }

    }


   /* class AsyncTaskBG extends AsyncTask<String, String, String> {
        List<Pet_Activity_Model> pet_activity_modelList;
        final AlertDialog dialog = new SpotsDialog(getContext());

        public AsyncTaskBG(List<Pet_Activity_Model> pet_activity_models) {
            pet_activity_modelList = pet_activity_models;

            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < pet_activity_modelList.size(); i++) {
                try {

                    Pet_Activity_Model petActivityListModelObj = pet_activity_modelList.get(i);
      *//*  String FileName = getContext().getCacheDir() + "/ActivityImage";
        File filedir = new File(FileName);
        if (filedir.exists()) {

        } else
            filedir.mkdir();
        for (int i = -0; i < s.size(); i++) {
            Pet_Activity_Model petActivityListModelObj = s.get(i);
            File img_unsel_file = new File(FileName + "activity_unsel_" + i);
            File img_selfile = new File(FileName + "activity_sel_" + i);

            try {
                if (img_unsel_file.exists()) {

                } else {
                    img_unsel_file.createNewFile();
                }
                if (img_selfile.exists()) {

                } else {
                    img_selfile.createNewFile();
                }
*//*
                    File file = Glide
                            .with(getContext())

                            .load(petActivityListModelObj.getImage())
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get(); // needs to be called on background thread
                    petActivityListModelObj.setFile_unSel(file.getAbsolutePath());
                    File SelFile = Glide
                            .with(getContext())
                            .load(petActivityListModelObj.getImage_selected())

                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

                            .get(); // needs to be called on background thread
                    petActivityListModelObj.setFile_sel(SelFile.getAbsolutePath());
                    pet_activity_modelList.remove(i);
                    pet_activity_modelList.add(i, petActivityListModelObj);
  *//*           } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception w) {
                w.printStackTrace();
            }*//*


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
           *//* PetActivityAdapter adapter = new PetActivityAdapter(pet_activity_modelList, getContext());
            scroll_acty_list.setAdapter(adapter);*//*
            for (int index = 0; index < pet_activity_modelList.size(); index++) {
                final Pet_Activity_Model pet_activity_model = pet_activity_modelList.get(index);
                View v = null;
                final ImageView imgActCatg;
                switch (index % 2) {
                    case 0:
                        v = LayoutInflater.from(getContext())
                                .inflate(R.layout.lo_act_pos_1, null);

                        break;

                    case 1:
                        v = LayoutInflater.from(getContext())
                                .inflate(R.layout.lo_act_pos_2, null);
                        break;
                }
                scroll_acty_list.addView(v);
                imgActCatg = (ImageView) v.findViewById(R.id.img_act_pos_1);
                v.setTag(index);
                imgActCatg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PREVIOUS_POS_SEL != -1) {
                            Pet_Activity_Model pet_activity_model1OBJ = pet_activity_modelList.get(PREVIOUS_POS_SEL);
                            RelativeLayout relativeLayout = (RelativeLayout) scroll_acty_list.findViewWithTag(PREVIOUS_POS_SEL);
                            ImageView imgPrevAct = (ImageView) relativeLayout.findViewById(R.id.img_act_pos_1);
                            load_Image(imgPrevAct, pet_activity_model1OBJ.getFile_unSel());
                            load_Image(imgActCatg, pet_activity_model.getFile_sel());
                        } else
                            load_Image(imgActCatg, pet_activity_model.getFile_sel());
                        String tagVal = ((RelativeLayout) v.getParent()).getTag().toString();
                        PREVIOUS_POS_SEL = Integer.parseInt(tagVal);
                        activity_sel_id = pet_activity_model.getId();

                    }
                });
                TextView txtActCatg_Name = (TextView) v.findViewById(R.id.txt_act_name_1);
                load_Image(imgActCatg, pet_activity_model.getFile_unSel());
                txtActCatg_Name.setText(pet_activity_model.getName());

            }
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        if (isPlaying) {
            mediaPlayer.pause();
            img_play_stop.setImageResource(R.drawable.play_rec);
            isPlaying = false;
            // mediaPlayer.prepareAsync();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.release();
    }

    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();
        return out.toByteArray();
    }


    public void Create_Pet_Actiivity() {
        if (activity_sel_id.equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please select one activity", Toast.LENGTH_SHORT).show();
        } else {
            if (isPlaying)
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                    isPlaying = false;
                }

            //  mediaPlayer.release();
            byte[] soundBytes;

            try {
                InputStream inputStream =
                        getActivity().getContentResolver().openInputStream(Uri.fromFile(new File(filePath)));

                soundBytes = new byte[inputStream.available()];
                soundBytes = toByteArray(inputStream);
                String encoded_audio = Base64.encodeToString(soundBytes, Base64.DEFAULT);

                if (NetworkConnection.isOnline(getContext())) {


                    final AlertDialog dialog = new SpotsDialog(getContext());
                    dialog.setCancelable(false);
                    dialog.show();
                    ApiInterface requestInterface = ApiClient.getClient();
                    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                    mCompositeDisposable.add(requestInterface._cr_pet_activity(usrModelobj.getOwner_id(), PetId, activity_sel_id, edt_opt_detls.getText().toString(), encoded_audio)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<Gen_Response_Model>() {
                                @Override
                                public void onNext(Gen_Response_Model s) {
                                    dialog.dismiss();
                                    mediaPlayer.release();
                                    Toast.makeText(getContext(), "" + s.getResult(), Toast.LENGTH_SHORT).show();
                                    SCRN_FROM = "submit";
                                    //fragmentCall_mainObj.Fragment_call(null, new ActivityFragment(), "act_list", null);
                                    hide_fragment();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        audio_seek_bar.setSecondaryProgress(percent);
    }

   /* public class PetActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Pet_Activity_Model> pet_activity_models_list;
        Context scrnContxt;
        int PREVIOUS_POS_SEL = -1;
        int CURRENT_POS_SEL;

        public PetActivityAdapter(List<Pet_Activity_Model> list_pet_activity_models, Context context) {
            pet_activity_models_list = list_pet_activity_models;
            scrnContxt = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.lo_act_pos_1, parent, false);
                    return new ViewHolderActPos1(v);

                case 1:
                    View v1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.lo_act_pos_2, parent, false);
                    return new ViewHolderActPos2(v1);


            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final Pet_Activity_Model pet_activity_modelObj = pet_activity_models_list.get(position);
            switch (holder.getItemViewType()) {
                case 0:
                    final ViewHolderActPos1 pos1Holder = (ViewHolderActPos1) holder;
                    if (!pet_activity_modelObj.isSelected()) {
                        load_Image(pos1Holder.imgActPos1, pet_activity_modelObj.getFile_unSel());
                    } else {
                        load_Image(pos1Holder.imgActPos1, pet_activity_modelObj.getFile_sel());
                    }
                    pos1Holder.txtActPos1_Name.setText(pet_activity_modelObj.getName());
                    pos1Holder.imgActPos1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity_sel_id = pet_activity_modelObj.getId();
                            if (!pet_activity_modelObj.isSelected()) {
                                pet_activity_modelObj.setSelected(true);
                                pet_activity_models_list.remove(position);
                                pet_activity_models_list.add(position, pet_activity_modelObj);
                                if (PREVIOUS_POS_SEL == -1) {
                                    PREVIOUS_POS_SEL = position;
                                } else {
                                    Pet_Activity_Model previous_sel_model = pet_activity_models_list.get(PREVIOUS_POS_SEL);
                                    previous_sel_model.setSelected(false);

                                    pet_activity_models_list.remove(PREVIOUS_POS_SEL);
                                    pet_activity_models_list.add(PREVIOUS_POS_SEL, previous_sel_model);

                                    notifyItemChanged(PREVIOUS_POS_SEL);

                                    PREVIOUS_POS_SEL = position;
                                }
                                notifyItemChanged(position);
                            }
                        }
                    });
                    break;
                case 1:
                    final ViewHolderActPos2 pos2Holder = (ViewHolderActPos2) holder;
                    if (!pet_activity_modelObj.isSelected()) {
                        load_Image(pos2Holder.imgActPos2, pet_activity_modelObj.getImage());
                    } else {
                        load_Image(pos2Holder.imgActPos2, pet_activity_modelObj.getImage_selected());
                    }
                    pos2Holder.txtActPos2_Name.setText(pet_activity_modelObj.getName());
                    pos2Holder.imgActPos2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity_sel_id = pet_activity_modelObj.getId();
                            if (!pet_activity_modelObj.isSelected()) {
                                if (PREVIOUS_POS_SEL == -1) {
                                    PREVIOUS_POS_SEL = position;
                                } else {
                                    Pet_Activity_Model previous_sel_model = pet_activity_models_list.get(PREVIOUS_POS_SEL);
                                    previous_sel_model.setSelected(false);
                                    pet_activity_modelObj.setSelected(true);
                            *//*Glide.with(scrnContxt)

                                    .load(pet_activity_modelObj.getImage())
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {


                                            pos1Holder.imgActPos1.setImageBitmap(resource);

                                        }


                                    });*//*
                                    pet_activity_models_list.remove(PREVIOUS_POS_SEL);
                                    pet_activity_models_list.add(PREVIOUS_POS_SEL, previous_sel_model);
                                    pet_activity_models_list.remove(position);
                                    pet_activity_models_list.add(position, pet_activity_modelObj);
                                    notifyItemChanged(PREVIOUS_POS_SEL);
                                    notifyItemChanged(position);
                                    PREVIOUS_POS_SEL = position;
                                }
                            }
                        }
                    });
                    break;
            }
        }


        public void load_Image(final ImageView imgActivity, String imgUrl) {

            Glide.with(scrnContxt)

                    .load(imgUrl)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imgActivity);
        }


        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return pet_activity_models_list.size();
        }

        public class ViewHolderActPos1 extends RecyclerView.ViewHolder {
            ImageView imgActPos1;

            TextView txtActPos1_Name;

            public ViewHolderActPos1(View itemView) {
                super(itemView);
                imgActPos1 = (ImageView) itemView.findViewById(R.id.img_act_pos_1);
                txtActPos1_Name = (TextView) itemView.findViewById(R.id.txt_act_name_1);

            }
        }

        public class ViewHolderActPos2 extends RecyclerView.ViewHolder {
            ImageView imgActPos2;

            TextView txtActPos2_Name;

            public ViewHolderActPos2(View itemView) {
                super(itemView);
                imgActPos2 = (ImageView) itemView.findViewById(R.id.img_act_pos_2);
                txtActPos2_Name = (TextView) itemView.findViewById(R.id.txt_act_name_2);

            }

        }
    }*/

    public void load_Image(final SimpleDraweeView imgActivity, String imgUrl) {

      /*  Glide.with(getContext())

                .load(imgUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgActivity);*/
      /*  final ImageRequest imageRequest2 =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(circle_resize_opts)
                        .build();
        holder.record_image.setImageRequest(imageRequest2);
        holder.record_image.getHierarchy().setRoundingParams(roundingParams);*/
       /* final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(Color.argb(40,46,100,150));
        progressBarDrawable.setBackgroundColor(Color.WHITE);
        progressBarDrawable
                .setRadius(15);

        imgActivity.getHierarchy().setProgressBarImage(R.drawable.loader);*/
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        builder.setProgressBarImage(R.drawable.loader);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        builder.setRetryImage(R.drawable.retry);

        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imgActivity.setHierarchy(hierarchy);


        imgActivity.setImageURI(imgUrl);
    }
}
