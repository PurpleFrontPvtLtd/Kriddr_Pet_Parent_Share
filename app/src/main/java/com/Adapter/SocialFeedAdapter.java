
package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
/*import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;*/
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Model.PostDetailModel;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;*/
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
import com.navigation.scrn.fragment.CommentScreen;
import com.navigation.scrn.fragment.OwnFeedFragment;

import java.util.List;

import purplefront.com.kriddrpetparent.R;

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.SocialFeedHolder> {

    Context scrnContxt;
    List<PostDetailModel> postDetailModelListOBJ;
    String selPetId;
    SocialAdapterInterface mListener;
    boolean isPublicFeed_val;


    public SocialFeedAdapter(Context context, List<PostDetailModel> tempList, String PetId, SocialAdapterInterface listener, boolean isPublicFeed) {

        scrnContxt = context;
        postDetailModelListOBJ = tempList;
        selPetId = PetId;
        mListener = listener;
        isPublicFeed_val = isPublicFeed;
    }

    public void setFeedList(List<PostDetailModel> tempList) {
        postDetailModelListOBJ = tempList;
    }


    @Override
    public SocialFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_feed_dtl, parent, false);
        return new SocialFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(final SocialFeedHolder holder, final int position) {
        final PostDetailModel postDetailModelOBJ = postDetailModelListOBJ.get(position);

        int CommentCntVal = Integer.parseInt(postDetailModelOBJ.getComments_counts());
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        //roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        if (postDetailModelOBJ.getShared_by().get(0).getName().equalsIgnoreCase("empty")) {

        } else {
            holder.rl_shard_by.setVisibility(View.VISIBLE);
            ((TextView) holder.rl_shard_by.findViewById(R.id.txtShrdByName)).setText("Shared by " + postDetailModelOBJ.getShared_by().get(0).getName());
            final SimpleDraweeView imgShrdByPets = ((SimpleDraweeView) holder.rl_shard_by.findViewById(R.id.img_shrdByPets));
            //  holder.img_Dots.setVisibility(View.INVISIBLE);
            //    imgShrdByPets.getHierarchy().setRoundingParams(roundingParams);
            if (!postDetailModelOBJ.getShared_by().get(0).getPhoto().equalsIgnoreCase("")) {


              /*  Glide.with(scrnContxt)

                        .load(postDetailModelOBJ.getShared_by().get(0).getPhoto())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                                drawable.setCircular(true);
                                imgShrdByPets.setImageDrawable(drawable);

                            }


                        });*/
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

                imgShrdByPets.setHierarchy(hierarchy);

                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getShared_by().get(0).getPhoto()))
                                .setResizeOptions(OwnFeedFragment.circle_resize_opts)
                                .build();
                imgShrdByPets.setImageRequest(imageRequest2);
                imgShrdByPets.getHierarchy().setRoundingParams(roundingParams);
            } else {
                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                        .path(String.valueOf(R.drawable.dogandcat))
                        .build();
                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(OwnFeedFragment.circle_resize_opts)
                                .build();
                imgShrdByPets.setImageRequest(imageRequest2);
                imgShrdByPets.getHierarchy().setRoundingParams(roundingParams);
            }
        }
        if (CommentCntVal < 10)
            holder.txtCmntCount.setText(" " + CommentCntVal);
        else
            holder.txtCmntCount.setText("" + CommentCntVal);

/* holder.img_comments_Count.setTag(i);
        holder.rl_like_contr.setTag(i);
        holder.view.setTag(i);
        rl_comnt_contr.setTag(i);
        rl_share_contr.setTag(i);*/


        holder.txtLikeCnt.setText(postDetailModelOBJ.getLikes_counts() + " Likes");
        holder.txtShrCnt.setText(postDetailModelOBJ.getShare_counts() + " Shares");

        holder.rl_cmnt_dots.setZ(25.0f);

        if (!postDetailModelOBJ.getLike_status().equalsIgnoreCase("no")) {
            holder.imgLike.setImageResource(R.drawable.like_fill);
            //imgLike.setTag(0);//Like Filled
        } else {
            //imgLike.setTag(1);//Not Filled
            holder.imgLike.setImageResource(R.drawable.like);
        }
        if (!isPublicFeed_val && selPetId.equalsIgnoreCase(postDetailModelOBJ.getPet_id()) && postDetailModelOBJ.getShared_by().get(0).getName().equalsIgnoreCase("empty")) {
            holder.img_Dots.setVisibility(View.VISIBLE);
            //  img_Dots.setTag(i);
            //   rl_share_contr.setVisibility(View.GONE);

/*    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(30,30);
                layoutParams.setMargins(0,0,30,0);
                img_comments_Count.setLayoutParams(layoutParams);*/

            // img_Dots.setZ(25.0f);
        } else {
            /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
            layoutParams.setMargins(0, 3, 2, 0);*/
            //  txtCmntCount.setLayoutParams(layoutParams);
            if (isPublicFeed_val || !postDetailModelOBJ.getShared_by().get(0).getName().equalsIgnoreCase("empty"))
                holder.img_Dots.setVisibility(View.INVISIBLE);
            else
                holder.img_Dots.setVisibility(View.VISIBLE);

        }

        holder.txtPetName.setText(postDetailModelOBJ.getPet_name());
        holder.txtPostDate.setText(postDetailModelOBJ.getCreated());
        holder.img_Dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int Pos = (int) view.getTag();
                if(selPetId.equalsIgnoreCase(postDetailModelOBJ.getPet_id()))
                mListener._callService(postDetailModelOBJ, OwnFeedFragment.Edit_Del_Call, position);
                else
                    mListener._callService(postDetailModelOBJ, OwnFeedFragment.Block_Report_call, position);



            }
        });
        holder.rl_comnt_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  int Pos = (int) v.getTag();

                //final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(position);
                mListener._callService(postDetailModelOBJ, OwnFeedFragment.Comment_Call, position);

                //           Toast.makeText(getContext(), "Pet_id" + sel_postDetailModelOBJ.getPet_posts_id(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.rl_like_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int Pos = (int) v.getTag();

                //              final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                //            int likeFillVal = (int) imgLike.getTag();

      /*          if (likeFillVal == 1) {
                    holder.imgLike.setImageResource(R.drawable.like_fill);
                    holder.imgLike.setTag(0);
                    //int count=Integer.parseInt(txtLikeCnt.getText().toString());

                } else {
                    imgLike.setImageResource(R.drawable.like);

                    imgLike.setTag(1);
                }
*/
                mListener._callService(postDetailModelOBJ, OwnFeedFragment.Like_Call, position);

            }
        });
        holder.rl_share_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int Pos = (int) v.getTag();
              /*  final PostDetailModel sel_postDetailModelOBJ = postDetailModelListOBJ.get(Pos);
                //Toast.makeText(getContext(), "Pet_id" + sel_postDetailModelOBJ.getPet_posts_id(), Toast.LENGTH_SHORT).show();
                isShareClicked = true;*/
                mListener._callService(postDetailModelOBJ, OwnFeedFragment.Share_Call, position);

            }
        });
        holder.txtPostMessage.setText(postDetailModelOBJ.getDescription());
        if (postDetailModelOBJ.getImage().equalsIgnoreCase("")) {

            holder.imgPostPic.setVisibility(View.GONE);

        } else {
/*
            Glide.with(scrnContxt)

                    .load(postDetailModelOBJ.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            holder.imgPostPic.setImageBitmap(resource);

                        }
                    });
*/
           /* GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100).setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();

            holder.imgPostPic.setHierarchy(hierarchy);*/

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

            holder.imgPostPic.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getImage()))
                            .setResizeOptions(OwnFeedFragment.mResizeOptions)

                            .build();
            holder.imgPostPic.setImageRequest(imageRequest);
            //   holder.imgPostPic.setImageURI(postDetailModelOBJ.getImage());
        }


        if (postDetailModelOBJ.getPet_photo().equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(OwnFeedFragment.circle_resize_opts)
                            .build();
            holder.imgPetPic.setImageRequest(imageRequest2);
            holder.imgPetPic.getHierarchy().setRoundingParams(roundingParams);
        } else {
          /*  Glide.with(scrnContxt)

                    .load(postDetailModelOBJ.getPet_photo())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                            drawable.setCircular(true);
                            holder.imgPetPic.setImageDrawable(drawable);

                        }
                    });*/

            /*GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrnContxt.getResources());
            builder.setRetryImage(R.drawable.retry);
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);

            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            holder.imgPetPic.setHierarchy(hierarchy);
            final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setColor(Color.argb(40, 46, 100, 150));
            progressBarDrawable.setBackgroundColor(Color.WHITE);
            progressBarDrawable
                    .setRadius(15);

            holder.imgPetPic.getHierarchy().setProgressBarImage(progressBarDrawable);*/

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

            holder.imgPetPic.setHierarchy(hierarchy);


            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(postDetailModelOBJ.getPet_photo()))
                            .setResizeOptions(OwnFeedFragment.circle_resize_opts)
                            .build();
            holder.imgPetPic.setImageRequest(imageRequest2);
            holder.imgPetPic.getHierarchy().setRoundingParams(roundingParams);
        }
/*lo_own_feed_contr.addView(view);
        INDEX_CREATE_VIEW = i;
    }
        if (isShareClicked) {
        scroll_Post.scrollTo(0, 0);
        isShareClicked = false;
    }
*/


    }

    @Override
    public int getItemCount() {
        return postDetailModelListOBJ.size();
    }

    public class SocialFeedHolder extends RecyclerView.ViewHolder {


        RelativeLayout rl_shard_by;
        SimpleDraweeView imgPostPic;
        SimpleDraweeView imgPetPic;
        RelativeLayout rl_cmnt_dots;
        RelativeLayout rl_comnt_contr;
        RelativeLayout rl_share_contr;
        ImageView img_Dots;
        TextView txtCmntCount;
        RelativeLayout rl_like_contr;
        ImageView imgLike;
        TextView txtShrCnt;
        TextView txtLikeCnt;
        TextView txtPetName;
        TextView txtPostMessage;
        TextView txtPostDate;
        ImageView img_comments_Count;

        public SocialFeedHolder(View itemView) {
            super(itemView);
            rl_shard_by = (RelativeLayout) itemView.findViewById(R.id.rl_shard_by);
            imgPostPic = (SimpleDraweeView) itemView.findViewById(R.id.imgFeedPic);
            imgPetPic = (SimpleDraweeView) itemView.findViewById(R.id.imgProfPic);
            rl_cmnt_dots = (RelativeLayout) itemView.findViewById(R.id.rl_cmnt_dots);
            rl_comnt_contr = (RelativeLayout) itemView.findViewById(R.id.rl_comnt_contr);
            rl_share_contr = (RelativeLayout) itemView.findViewById(R.id.rl_shr_contr);
            img_Dots = (ImageView) itemView.findViewById(R.id.img_dots);
            txtCmntCount = (TextView) itemView.findViewById(R.id.txtComntCount);
            rl_like_contr = (RelativeLayout) itemView.findViewById(R.id.rl_like_contr);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            txtShrCnt = (TextView) itemView.findViewById(R.id.txtShreCnt);
            txtLikeCnt = (TextView) itemView.findViewById(R.id.txtLikeCnt);
            txtPetName = (TextView) itemView.findViewById(R.id.txtPetName);
            txtPostMessage = (TextView) itemView.findViewById(R.id.txtPostContent);
            txtPostDate = (TextView) itemView.findViewById(R.id.txtPostDate);
            img_comments_Count = (ImageView) itemView.findViewById(R.id.imgCommentCountPic);
        }
    }

    public interface SocialAdapterInterface {
        public void _callService(PostDetailModel postDetailModelObj, int Options, int AdapterPos);
    }
}

