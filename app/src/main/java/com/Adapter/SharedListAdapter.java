package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Model.SearchPersonToShare;
import com.Model.SharedListDetailModel;
import com.Model.SharedListModel;
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

import java.util.List;

import purplefront.com.kriddrpetparent.R;

public class SharedListAdapter extends RecyclerView.Adapter<SharedListAdapter.MyViewHolder> {
    Context scrnContxt;
    List<SharedListDetailModel> sharedWithModelslist;
    delete_clicked_Interface mListener;

    public SharedListAdapter(List<SharedListDetailModel> searchPersonToShares, Context context,delete_clicked_Interface listener){
        sharedWithModelslist= searchPersonToShares;
        mListener=listener;
        scrnContxt=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sharedView= LayoutInflater.from(scrnContxt).inflate(R.layout.lo_row_prof_shred_with,null);

        return new MyViewHolder(sharedView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SharedListDetailModel sharedListDetailModel=sharedWithModelslist.get(position);
        holder.txtPersName.setText(sharedListDetailModel.getName());
        holder.txtMobileNo.setText(sharedListDetailModel.getMobile());
        String type;
        if(sharedListDetailModel.getType().equalsIgnoreCase("business")){
          type="Business";
        }
        else
        {
            type="Pet Parent";
        }
        holder.txtPersType.setText(type);

       /* Glide.with(scrnContxt)

                .load(sharedListDetailModel.getPhoto())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                        drawable.setCircular(true);
                        holder.img_prof_pic.setImageDrawable(drawable);

                    }


                });*/
        RoundingParams roundingParams =RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        ResizeOptions circle_resize_opts=new ResizeOptions(200,200);
        if(sharedListDetailModel.getPhoto().trim().equalsIgnoreCase("")){
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
            holder.img_prof_pic.setImageRequest(imageRequest2);
            holder.img_prof_pic.getHierarchy().setRoundingParams(roundingParams);
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
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(sharedListDetailModel.getPhoto()))
                            .setResizeOptions(circle_resize_opts)
                            .build();
            holder.img_prof_pic.setImageRequest(imageRequest2);
            holder.img_prof_pic.getHierarchy().setRoundingParams(roundingParams);
        }


        holder.img_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.on_deleteSharedList(sharedListDetailModel.getShare_id(),sharedListDetailModel.getPet_type());
            }
        });
        holder.btnShare.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return sharedWithModelslist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPersName, txtMobileNo,txtPersType;
        ImageView img_Del;
        SimpleDraweeView img_prof_pic;
        Button btnShare;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtPersName = (TextView) itemView.findViewById(R.id.txtPersName);
            txtMobileNo = (TextView)itemView.findViewById(R.id.txtMobileNo);
            img_Del=(ImageView)itemView.findViewById(R.id.img_Del);
            img_prof_pic=(SimpleDraweeView)itemView.findViewById(R.id.imgProfPic);
            txtPersType=(TextView)itemView.findViewById(R.id.txtPersType);
            btnShare=(Button)itemView.findViewById(R.id.btnShare);


        }
    }
    public interface delete_clicked_Interface{
       public void on_deleteSharedList(String shareId,String Pet_Type);
    }


}
