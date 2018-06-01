package com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.Model.PetActivityCreatedDtlModel;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 12-04-2018.
 */

public class Pet_ActivityList_Adapter extends RecyclerView.Adapter<Pet_ActivityList_Adapter.MyHolder> {
    List<PetActivityCreatedDtlModel> petActivityModl_listObj;
    Context scrnContxt;
    iface_pet_activity_clicked pet_activity_clicked_iface_obj;


    public Pet_ActivityList_Adapter(Context c, List<PetActivityCreatedDtlModel> j, iface_pet_activity_clicked listener) {
        scrnContxt = c;

        petActivityModl_listObj = j;
        pet_activity_clicked_iface_obj = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_act_list_row_dtl, parent, false);

        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        final PetActivityCreatedDtlModel petActivityCreatedDtlModel = petActivityModl_listObj.get(position);
      /*  Glide.with(scrnContxt)

                .load(petActivityCreatedDtlModel.getFile_act())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {


                        holder.imgAct.setImageBitmap(resource);

                    }


                });*/
      /*  ImageLoadModel loadModel=imageLoadModelsList.get(Integer.parseInt(petActivityCreatedDtlModel.getFile_act()));
        holder.imgAct.setImageBitmap(loadModel.getImgBitmap());*/
        ResizeOptions resizeOptions=new ResizeOptions(170,50);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
        builder.setProgressBarImage(R.drawable.loader);
        builder.setRetryImage(R.drawable.retry);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100).setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build();

        holder.imgAct.setHierarchy(hierarchy);


        final ImageRequest imageRequest2 =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(petActivityCreatedDtlModel.getImage_for_list()))
                        .setResizeOptions(resizeOptions)
                        .setProgressiveRenderingEnabled(true)
                        .build();
        holder.imgAct.setImageRequest(imageRequest2);


        if (position == 0) {
            holder.topLine.setVisibility(View.INVISIBLE);
        }
        if (position == (petActivityModl_listObj.size() - 1)) {
            holder.BottomLine.setVisibility(View.INVISIBLE);
        }
        holder.txtMonthDay.setText(petActivityCreatedDtlModel.getMonth());
        setTime(holder.txtTime, petActivityCreatedDtlModel.getDate() + " " + petActivityCreatedDtlModel.getTime());
        holder.imgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet_activity_clicked_iface_obj._activity_clicked(petActivityCreatedDtlModel);
            }
        });

    }

    public void setTime(TextView txtTime, String DateVAL) {
        try {


            String dateStr = DateVAL;
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateStr);
            df.setTimeZone(TimeZone.getDefault());

            String formattedDate = df.format(date);
            String Time[] = formattedDate.split(" ");
            txtTime.setText(Time[3] + " " + Time[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return petActivityModl_listObj.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imgAct;

        TextView txtMonthDay, txtTime;
        View topLine, BottomLine;

        public MyHolder(View itemView) {
            super(itemView);
            imgAct = (SimpleDraweeView) itemView.findViewById(R.id.img_act_name);
            txtMonthDay = (TextView) itemView.findViewById(R.id.txtMon_Day);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            topLine = (View) itemView.findViewById(R.id.vw_line_top_cir);
            BottomLine = (View) itemView.findViewById(R.id.vw_line_bottom_cir);

        }
    }

    public interface iface_pet_activity_clicked {
        public void _activity_clicked(PetActivityCreatedDtlModel petActivityCreatedDtlModel);
    }
}
