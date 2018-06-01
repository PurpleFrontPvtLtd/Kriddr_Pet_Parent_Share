package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.Model.PostDetailModel;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import purplefront.com.kriddrpetparent.R;

public class ImageMyPostAdapter extends BaseAdapter {
    Context scrnContext;
    List<PostDetailModel> postDetailModelList;
    CommentInterface mListener;
    ResizeOptions mResizeOptions;

    public ImageMyPostAdapter(Context context, List<PostDetailModel> tmpPostDetailModels, CommentInterface listener) {
        scrnContext = context;
        postDetailModelList = tmpPostDetailModels;
        mListener = listener;
        mResizeOptions = new ResizeOptions(150, 150);

    }

    @Override
    public int getCount() {
        return postDetailModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return postDetailModelList.get(position);
    }

/*
    @NonNull
    @Override
    public MyPostFeed_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(scrnContext).inflate(R.layout.lo_img_my_post, null);
        return new MyPostFeed_ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostFeed_ViewHolder holder, final int position) {
        holder.imgMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShowComment(postDetailModelList.get(position).getPet_id(), postDetailModelList.get(position));
            }
        });
        Glide.with(scrnContext)

                .load(postDetailModelList.get(position).getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        holder.imgMyPost.setImageBitmap(resource);

                    }
                });
    }
*/

    @Override
    public long getItemId(int position) {
        return position;
    }

/*
    @Override
    public int getItemCount() {
        return postDetailModelList.size()/3;
    }
*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(scrnContext).inflate(R.layout.lo_img_my_post, null);
        }
        final SimpleDraweeView imgMyPost = (SimpleDraweeView) convertView.findViewById(R.id.img_my_post);
        imgMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShowComment(postDetailModelList.get(position).getPet_id(), postDetailModelList.get(position));
            }
        });
     /*   Glide.with(scrnContext)

                .load(postDetailModelList.get(position).getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        imgMyPost.setImageBitmap(resource);

                    }
                });*/
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrnContext.getResources());
        builder.setProgressBarImage(R.drawable.loader);
        builder.setRetryImage(R.drawable.retry);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imgMyPost.setHierarchy(hierarchy);
        final ImageRequest imageRequest2 =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelList.get(position).getImage()))
                        .setResizeOptions(mResizeOptions)
                        .build();
        imgMyPost.setImageRequest(imageRequest2);
        return convertView;
    }


    public interface CommentInterface {
        public void onShowComment(String petId, PostDetailModel postDetailModel);
    }
}
