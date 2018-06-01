

package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.Model.NotesModel;
import com.Model.NotificationModel;
import com.facebook.common.util.UriUtil;
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
    Context scrnContxt;


    public NotifyAdapter(List<NotificationModel> notificationModels, Context context) {
        this.notifyList = notificationModels;
        this.scrnContxt = context;


    }


    @Override
    public NotifyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lo_row_notify, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotifyAdapter.MyViewHolder holder, int position) {
        String sourceString = "<b>" + notifyList.get(position).getReacted_pet_name() + "</b> " + notifyList.get(position).getNotification();
      //  holder.txtNotify.setText(Html.fromHtml(sourceString,Fr);
        holder.txtTime.setText(notifyList.get(position).getCreated());
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
        RoundingParams roundingParams =RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        ResizeOptions circle_resize_opts=new ResizeOptions(200,200);
        if(notifyList.get(position).getPhoto().trim().equalsIgnoreCase("")){
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
        }
        else {
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
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(notifyList.get(position).getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            holder.imgNfy.setImageRequest(imageRequest2);
            holder.imgNfy.getHierarchy().setRoundingParams(roundingParams);
        }



    }

    @Override
    public int getItemCount() {

        return notifyList.size();


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotify, txtTime;
        SimpleDraweeView imgNfy;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtNotify = (TextView) itemView.findViewById(R.id.txtNotify);
            txtTime = (TextView) itemView.findViewById(R.id.txtNfyTIme);
            imgNfy = (SimpleDraweeView) itemView.findViewById(R.id.img_notify);

        }
    }
}
