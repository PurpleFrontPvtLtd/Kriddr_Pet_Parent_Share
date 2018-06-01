package com.Adapter;

import android.content.Context;

import android.graphics.Color;
import android.net.Uri;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.Model.Client_info_Model;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 07-03-2018.
 */


public class Client_list_adapter extends RecyclerView.Adapter<Client_list_adapter.MyViewHolder> {

    List<Client_info_Model> docList = new ArrayList<>();
    Context context;
    Client_list_adapter.pet_detail_show_iface mListener;
    ResizeOptions circle_resize_opts;

    public Client_list_adapter(List<Client_info_Model> docList, Context context,Client_list_adapter.pet_detail_show_iface listener) {
        Log.d("JASIRE", "JASIRE" + docList.size());
        this.docList = docList;
        this.context = context;
        mListener=listener;
        circle_resize_opts=new ResizeOptions(200,200);

    }


    @Override
    public Client_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.col_client_disp, parent, false);
        return new Client_list_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Client_list_adapter.MyViewHolder holder, int position) {

        Log.d("PIRSDR", "PIRSDR" + docList.get(position));
        final Client_info_Model client_info_model = docList.get(position);
        RoundingParams roundingParams =RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if(client_info_model.getPhoto().trim().equalsIgnoreCase("")){
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
            holder.record_image.setImageRequest(imageRequest2);
            holder.record_image.getHierarchy().setRoundingParams(roundingParams);
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



            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(context.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            holder.record_image.setHierarchy(hierarchy);


            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(client_info_model.getPhoto()))
                            .setResizeOptions(circle_resize_opts)

                            .build();
            holder.record_image.setImageRequest(imageRequest2);
            holder.record_image.getHierarchy().setRoundingParams(roundingParams);
        }

            holder.record_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mListener.onshow_sel_pet_dtl(client_info_model.getPet_id(),client_info_model.getType());
                }
            });
            holder.record_name.setText(client_info_model.getPet_name());



    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.d("RECNAMLST", "RECNAMLST" + docList.size());
        return docList.size();

    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView record_name;
        SimpleDraweeView record_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            record_image = (SimpleDraweeView) itemView.findViewById(R.id.img_pet);
            record_name = (TextView) itemView.findViewById(R.id.txtPet_name);

        }
    }
    public interface pet_detail_show_iface{
        public void onshow_sel_pet_dtl(String pet_id,String type);
    }
}