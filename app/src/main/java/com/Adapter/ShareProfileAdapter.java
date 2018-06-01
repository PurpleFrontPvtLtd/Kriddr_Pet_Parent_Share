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

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import purplefront.com.kriddrpetparent.R;

public class ShareProfileAdapter extends RecyclerView.Adapter<ShareProfileAdapter.MyViewHolder> {
    Context scrnContxt;
    List<SearchPersonToShare> searchPersonToSharesList;
    shareClickInterface mListenr;
    public ShareProfileAdapter(List<SearchPersonToShare> searchPersonToShares, Context context,shareClickInterface listener){
        this.searchPersonToSharesList = searchPersonToShares;
        scrnContxt=context;
        mListenr=listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sharedView= LayoutInflater.from(scrnContxt).inflate(R.layout.lo_row_prof_shred_with,null);

        return new MyViewHolder(sharedView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SearchPersonToShare personToShare=searchPersonToSharesList.get(position);
        holder.txtPersName.setText(personToShare.getName());
        holder.txtMobileNo.setText(personToShare.getMobile());
        holder.txtPersType.setVisibility(View.VISIBLE);
        String type;
        if(personToShare.getType().equalsIgnoreCase("business")){
            type="Business";
        }
        else
        {
            type="Pet Parent";
        }
        ResizeOptions resizeOptions=new ResizeOptions(150,150);
        RoundingParams roundingParams =RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if(personToShare.getPhoto().equalsIgnoreCase("")){
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            holder.imgProfPic.setImageRequest(imageRequest2);
            holder.imgProfPic.getHierarchy().setRoundingParams(roundingParams);
        }
        else {
            /*Glide.with(scrnContxt)

                    .load(personToShare.getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                            drawable.setCircular(true);
                            holder.imgProfPic.setImageDrawable(drawable);

                        }


                    });*/
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(personToShare.getPhoto()))
                            .setResizeOptions(resizeOptions)
                            .build();
            holder.imgProfPic.setImageRequest(imageRequest2);
            holder.imgProfPic.getHierarchy().setRoundingParams(roundingParams);
        }
        holder.txtPersType.setText(type);

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenr.onShareClick(personToShare.getId(),personToShare.getType());
            }
        });
        holder.btnShare.setVisibility(View.VISIBLE);
        holder.img_Del.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return searchPersonToSharesList.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPersName, txtMobileNo, txtPersType;
        ImageView img_Del;
        Button btnShare;
        SimpleDraweeView imgProfPic;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtPersName = (TextView) itemView.findViewById(R.id.txtPersName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            img_Del = (ImageView) itemView.findViewById(R.id.img_Del);
            txtPersType = (TextView) itemView.findViewById(R.id.txtPersType);
            btnShare = (Button) itemView.findViewById(R.id.btnShare);
            imgProfPic=(SimpleDraweeView)itemView.findViewById(R.id.imgProfPic);

        }
    }

    public interface shareClickInterface{
        public void onShareClick(String usrId,String usrType);
    }

}
