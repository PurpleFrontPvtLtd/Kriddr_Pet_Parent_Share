

package com.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.Model.Nfy_Variable_Model;
import com.Model.NotesModel;
import com.Model.NotificationModel;
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
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;*/

import java.util.ArrayList;
import java.util.List;

import purplefront.com.kriddrpetparent.R;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    List<NotificationModel> notifyList = new ArrayList<>();
    Activity scrnContxt;
    Ntfy_dtls_interface mNfyListenr;
    AlertDialog confirm_dtl_pop_up, new_pet_dlg, alrt_doc_dlg, alrt_pet_prof_edit;
    ResizeOptions circle_resize_opts;
    RoundingParams roundingParams;


    public NotifyAdapter(List<NotificationModel> notificationModels, Activity context, Ntfy_dtls_interface listener) {
        this.notifyList = notificationModels;
        this.scrnContxt = context;
        mNfyListenr = listener;


    }


    @Override
    public NotifyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lo_row_notify, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotifyAdapter.MyViewHolder holder, final int position) {

        String sourceString = "<b>" + notifyList.get(position).getReacted_pet_name() + "</b> " + notifyList.get(position).getNotification();
        //  holder.txtNotify.setText(Html.fromHtml(sourceString,Fr);
        holder.txtTime.setText(notifyList.get(position).getCreated());

        if (notifyList.get(position).getRead_status().trim().equalsIgnoreCase("read")) {
            holder.rl_nfy_contr.setBackgroundColor(Color.WHITE);
        } else {
            holder.rl_nfy_contr.setBackgroundColor(Color.LTGRAY);
        }
        if (notifyList.get(position).getType().trim().equalsIgnoreCase("request_send")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNfyListenr._notify_click(notifyList.get(position));
                    new_pet_dialog(notifyList.get(position));
                }
            });
        } else if (notifyList.get(position).getType().trim().equalsIgnoreCase("newpet")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNfyListenr._notify_click(notifyList.get(position));
                    new_pet_dialog(notifyList.get(position));
                }
            });
        } else if (notifyList.get(position).getType().trim().equalsIgnoreCase("food_edit")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNfyListenr._notify_click(notifyList.get(position));
                    new_pet_dialog(notifyList.get(position));
                }
            });
        } else if (notifyList.get(position).getType().trim().equalsIgnoreCase("document")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNfyListenr._notify_click(notifyList.get(position));
                    show_doc_dlg(notifyList.get(position));
                }
            });
        } else if (notifyList.get(position).getType().trim().equalsIgnoreCase("profile_edit")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNfyListenr._notify_click(notifyList.get(position));
                    show_alrt_pet_prof_edit(notifyList.get(position));
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtNotify.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.txtNotify.setText(Html.fromHtml(sourceString));
        }

      /*  Glide.with(scrnContxt)

                .load(notifyList.get(position).getPhoto())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                        drawable.setCircular(true);
                        holder.imgNfy.setImageDrawable(drawable);

                    }


                });*/
        roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        circle_resize_opts = new ResizeOptions(200, 200);
        if (notifyList.get(position).getPhoto().trim().equalsIgnoreCase("")) {
            /*Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.dogandcat);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), icon);
            drawable.setCircular(true);
            holder.record_image.setImageDrawable(drawable);*/
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(circle_resize_opts)
                            .build();
            holder.imgNfy.setImageRequest(imageRequest2);
            holder.imgNfy.getHierarchy().setRoundingParams(roundingParams);
        } else {
/*            Glide.with(context)

                    .load(client_info_model.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            drawable.setCircular(true);
                            holder.record_image.setImageDrawable(drawable);

                        }


                    });*/
           /* final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(notifyList.get(position).getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            holder.imgNfy.setImageRequest(imageRequest2);
            holder.imgNfy.getHierarchy().setRoundingParams(roundingParams);*/

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            holder.imgNfy.setHierarchy(hierarchy);

            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(notifyList.get(position).getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            holder.imgNfy.setImageRequest(imageRequest);
            holder.imgNfy.getHierarchy().setRoundingParams(roundingParams);


        }


    }

    @Override
    public int getItemCount() {

        return notifyList.size();


    }

    public void new_pet_dialog(final NotificationModel notificationModel) {
        AlertDialog.Builder alrtDlgBldr = new AlertDialog.Builder(scrnContxt);
        LayoutInflater inflater = scrnContxt.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.lo_dlg_prof_req, null);
        TextView txtHdr = (TextView) dialogView.findViewById(R.id.txt_main_hdr);
        TextView txtMsgDtl = (TextView) dialogView.findViewById(R.id.txtMsgDtl);
        RelativeLayout profile_details_layout = (RelativeLayout) dialogView.findViewById(R.id.profile_details_layout);
        RelativeLayout rl_brand_layout = (RelativeLayout) dialogView.findViewById(R.id.brand_details_layout);
        RelativeLayout rl_protein_layout = (RelativeLayout) dialogView.findViewById(R.id.protein_details_layout);
        RelativeLayout rl_servings_layout = (RelativeLayout) dialogView.findViewById(R.id.servings_details_layout);
        RelativeLayout lo_hdr_food_pref = dialogView.findViewById(R.id.food_prefrences_layout);
        RelativeLayout rl_dlg_contr = dialogView.findViewById(R.id.scroll_pet_req_contr);
        ImageView image_close = (ImageView) dialogView.findViewById(R.id.image_close);
        Button btn_approve = (Button) dialogView.findViewById(R.id.btn_allow);
        SimpleDraweeView imgPet = (SimpleDraweeView) dialogView.findViewById(R.id.imgPet);
        TextView brand_value = (TextView) dialogView.findViewById(R.id.brand_value);
        TextView protein_value = (TextView) dialogView.findViewById(R.id.protein_value);
        TextView servings_value = (TextView) dialogView.findViewById(R.id.servings_value);
        TextView profile_name = (TextView) dialogView.findViewById(R.id.profile_name);
        TextView dob = (TextView) dialogView.findViewById(R.id.profile_dob);
        Button btn_deny = (Button) dialogView.findViewById(R.id.btn_deny);
        List<Nfy_Variable_Model> variable_modelList = notificationModel.getVariables();
        brand_value.setText(variable_modelList.get(0).getPet_brand());
        protein_value.setText(variable_modelList.get(0).getPet_protien());
        servings_value.setText(variable_modelList.get(0).getPet_portion_size());


        String sourceString = "<b>" + notificationModel.getReacted_pet_name() + "</b> " + notificationModel.getNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtMsgDtl.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtMsgDtl.setText(Html.fromHtml(sourceString));
        }
        // txtMsgDtl.setText(notificationModel.getReacted_pet_name()+" "+notificationModel.getNotification());
        if (notificationModel.getType().trim().equalsIgnoreCase("request_send")) {
            txtHdr.setText("Pet access request");


        } else if (notificationModel.getType().trim().equalsIgnoreCase("food_edit")) {
            txtHdr.setText("Food preference edit");
            profile_details_layout.setVisibility(View.GONE);
            imgPet.setVisibility(View.GONE);
            lo_hdr_food_pref.setVisibility(View.GONE);
            String brand = variable_modelList.get(0).getPet_brand();
            String protein = variable_modelList.get(0).getPet_protien();
            String portion = variable_modelList.get(0).getPet_portion_size();
            if (brand.trim().equalsIgnoreCase("Empty")) {
                rl_brand_layout.setVisibility(View.GONE);
            }
            if (protein.trim().equalsIgnoreCase("Empty")) {
                rl_protein_layout.setVisibility(View.GONE);
            }
            if (portion.trim().equalsIgnoreCase("Empty")) {
                rl_servings_layout.setVisibility(View.GONE);
            }

        } else {
            txtHdr.setText("New pet creation");

        }
        if (!notificationModel.getType().trim().equalsIgnoreCase("food_edit")) {
            profile_name.setText(variable_modelList.get(0).getPet_name());
            dob.setText("DOB:" + variable_modelList.get(0).getPet_dob());
            imgPet.setZ(20.0f);
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            imgPet.setHierarchy(hierarchy);

            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(notificationModel.getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            imgPet.setImageRequest(imageRequest);
            imgPet.getHierarchy().setRoundingParams(roundingParams);
        }

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pet_dlg.dismiss();
                mNfyListenr._notify_click(notificationModel.getType().trim(), notificationModel.getReaction_id(), "accept");
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pet_dlg.dismiss();
            }
        });
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new_pet_dlg.dismiss();
                show_confrim_dialog(notificationModel.getType(), notificationModel.getReaction_id());
            }
        });

        alrtDlgBldr.setView(dialogView);
        new_pet_dlg = alrtDlgBldr.create();
        new_pet_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        new_pet_dlg.show();
    }

    public void show_alrt_pet_prof_edit(final NotificationModel notificationModel) {
        AlertDialog.Builder alrtDlgBldr = new AlertDialog.Builder(scrnContxt);
        LayoutInflater inflater = scrnContxt.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.lo_dlg_pet_ownr_update, null);
        TextView txtHdr_dob = (TextView) dialogView.findViewById(R.id.txtHdr_Dob);
        TextView txtHdr_Email = (TextView) dialogView.findViewById(R.id.txtHdr_email);
        TextView txtHdr_Addrs = (TextView) dialogView.findViewById(R.id.txtHdr_Addrs);
        TextView txtHdr_Pref_cont = (TextView) dialogView.findViewById(R.id.txtHdr_Pref_conts);
        TextView txtDob_val = (TextView) dialogView.findViewById(R.id.txtDob_val);

        TextView txtEmail_Val = (TextView) dialogView.findViewById(R.id.txtEmailVal);

        TextView txtAddrs_Val = (TextView) dialogView.findViewById(R.id.txtAddrs_val);

        TextView txtVal_Pref_cont = (TextView) dialogView.findViewById(R.id.txtPref_conts_val);

        ImageView image_close = (ImageView) dialogView.findViewById(R.id.image_close);
        Button btn_okay = (Button) dialogView.findViewById(R.id.btn_cnfrm);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        List<Nfy_Variable_Model> variable_models = notificationModel.getVariables();

        if (variable_models.get(0).getEmail().equalsIgnoreCase("empty")) {
            txtEmail_Val.setVisibility(View.GONE);
            txtHdr_Email.setVisibility(View.GONE);
        } else {
            txtEmail_Val.setText(variable_models.get(0).getEmail());
        }
        if (variable_models.get(0).getAddress().equalsIgnoreCase("empty")) {
            txtHdr_Addrs.setVisibility(View.GONE);
            txtAddrs_Val.setVisibility(View.GONE);
        } else {
            txtAddrs_Val.setText(variable_models.get(0).getAddress());
        }
        if (variable_models.get(0).getPreferred_contact().equalsIgnoreCase("empty")) {
            txtHdr_Pref_cont.setVisibility(View.GONE);
            txtVal_Pref_cont.setVisibility(View.GONE);
        } else {
            if(variable_models.get(0).getPreferred_contact().equalsIgnoreCase("text")){
                txtVal_Pref_cont.setText("Text message");
            }
            else
            txtVal_Pref_cont.setText(variable_models.get(0).getPreferred_contact());
        }
        if (variable_models.get(0).getDob().equalsIgnoreCase("empty")) {
            txtDob_val.setVisibility(View.GONE);
            txtHdr_dob.setVisibility(View.GONE);
        } else {
            txtDob_val.setText(variable_models.get(0).getDob());
        }

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alrt_pet_prof_edit.dismiss();
                mNfyListenr._notify_click(notificationModel.getType().trim(), notificationModel.getReaction_id(), "accept");
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alrt_pet_prof_edit.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alrt_pet_prof_edit.dismiss();
                show_confrim_dialog(notificationModel.getType(), notificationModel.getReaction_id());
            }
        });

        alrtDlgBldr.setView(dialogView);
        alrt_pet_prof_edit = alrtDlgBldr.create();
        alrt_pet_prof_edit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alrt_pet_prof_edit.show();
    }

    public void show_doc_dlg(final NotificationModel notificationModel) {
        AlertDialog.Builder alrtDlgBldr = new AlertDialog.Builder(scrnContxt);
        LayoutInflater inflater = scrnContxt.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.lo_dlg_doc, null);
        TextView txt_main_hdr = (TextView) dialogView.findViewById(R.id.txt_main_hdr);
        TextView txtMsgDtl = (TextView) dialogView.findViewById(R.id.txtMsgDtl);
        TextView txtDocName = (TextView) dialogView.findViewById(R.id.txt_doc_name);
        TextView txtDocCatg = (TextView) dialogView.findViewById(R.id.txt_doc_category);
        ImageView image_close = (ImageView) dialogView.findViewById(R.id.image_close);
        SimpleDraweeView imgDoc = (SimpleDraweeView) dialogView.findViewById(R.id.imgDoc);
        Button btn_Allow = (Button) dialogView.findViewById(R.id.btn_allow);
        Button btn_Deny = (Button) dialogView.findViewById(R.id.btn_deny);
        txt_main_hdr.setText("New record");
        String sourceString = "<b>" + notificationModel.getReacted_pet_name() + "</b> " + notificationModel.getNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtMsgDtl.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtMsgDtl.setText(Html.fromHtml(sourceString));
        }
        List<Nfy_Variable_Model> variable_modelList = notificationModel.getVariables();
        txtDocName.setText(variable_modelList.get(0).getDoc_name());
        txtDocCatg.setText(variable_modelList.get(0).getDoc_category());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
        builder.setProgressBarImage(R.drawable.loader);
        builder.setRetryImage(R.drawable.retry);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imgDoc.setHierarchy(hierarchy);

        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(variable_modelList.get(0).getDocument()))
                        .setResizeOptions(circle_resize_opts)
                        .build();
        imgDoc.setImageRequest(imageRequest);
        //imgDoc.getHierarchy().setRoundingParams(roundingParams);
        btn_Allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alrt_doc_dlg.dismiss();
                mNfyListenr._notify_click(notificationModel.getType().trim(), notificationModel.getReaction_id(), "accept");
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alrt_doc_dlg.dismiss();
            }
        });
        btn_Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_confrim_dialog(notificationModel.getType(), notificationModel.getReaction_id());
            }
        });

        alrtDlgBldr.setView(dialogView);
        alrt_doc_dlg = alrtDlgBldr.create();
        alrt_doc_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alrt_doc_dlg.show();
    }

    public void show_confrim_dialog(final String Type, final String ReactionId) {
        AlertDialog.Builder alrtDlgBldr = new AlertDialog.Builder(scrnContxt);
        LayoutInflater inflater = scrnContxt.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dlg_alrt_confrm_del, null);
        TextView txtMsg = (TextView) dialogView.findViewById(R.id.txtMsg);
        ImageView image_close = (ImageView) dialogView.findViewById(R.id.image_close);
        Button btn_okay = (Button) dialogView.findViewById(R.id.btn_cnfrm);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        txtMsg.setText("Are you sure want to deny the request?");
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Type.trim().equalsIgnoreCase("request_send")) {
                    new_pet_dlg.dismiss();
                    confirm_dtl_pop_up.dismiss();
                } else if (Type.trim().equalsIgnoreCase("newpet")) {
                    new_pet_dlg.dismiss();
                    confirm_dtl_pop_up.dismiss();
                } else if (Type.trim().equalsIgnoreCase("food_edit")) {
                    new_pet_dlg.dismiss();
                    confirm_dtl_pop_up.dismiss();
                } else if (Type.trim().equalsIgnoreCase("document")) {
                    alrt_doc_dlg.dismiss();
                    confirm_dtl_pop_up.dismiss();
                } else if (Type.trim().equalsIgnoreCase("profile_edit")) {
                    alrt_pet_prof_edit.dismiss();
                    confirm_dtl_pop_up.dismiss();
                }
                mNfyListenr._notify_click(Type, ReactionId, "deny");
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_dtl_pop_up.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_dtl_pop_up.dismiss();
            }
        });

        alrtDlgBldr.setView(dialogView);
        confirm_dtl_pop_up = alrtDlgBldr.create();
        confirm_dtl_pop_up.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        confirm_dtl_pop_up.show();
    }

    public interface Ntfy_dtls_interface {
        public void _notify_click(String type, String reaction_id, String reaction);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotify, txtTime;
        SimpleDraweeView imgNfy;

        RelativeLayout rl_nfy_contr;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNotify = (TextView) itemView.findViewById(R.id.txtNotify);
            txtTime = (TextView) itemView.findViewById(R.id.txtNfyTIme);
            imgNfy = (SimpleDraweeView) itemView.findViewById(R.id.img_notify);
            rl_nfy_contr = (RelativeLayout) itemView.findViewById(R.id.rl_nfy_contr);


        }
    }
}
