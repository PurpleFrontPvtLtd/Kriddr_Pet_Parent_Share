package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Model.Pet_Search_Details_Model;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;*/
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

import java.util.List;

import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 09-03-2018.
 */

public class SearchPetsAdapter extends BaseAdapter {

    List<Pet_Search_Details_Model> list_client_info_models;
    SearchPetsAdapter.SearchPetsSelectedListener mListener;
    Context scrnContxt;

    public SearchPetsAdapter(List<Pet_Search_Details_Model> client_info_models, Context context, SearchPetsAdapter.SearchPetsSelectedListener listener) {
        scrnContxt = context;
        mListener = listener;
        list_client_info_models = client_info_models;
    }

    @Override
    public int getCount() {
        return list_client_info_models.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(scrnContxt).inflate(R.layout.card_pets_search, null);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.selectedPets(list_client_info_models.get(i));
            }
        });
        ((TextView) convertView.findViewById(R.id.txtPetName)).setText(list_client_info_models.get(i).getPet_name());


        ImageView btnFollow = (ImageView) convertView.findViewById(R.id.btn_follow);
        RelativeLayout rl_follow_contr = (RelativeLayout) convertView.findViewById(R.id.rl_follow_contr);

        final SimpleDraweeView imgPetPic = (SimpleDraweeView) convertView.findViewById(R.id.imgProfPic);
        if (mListener != null) {
            btnFollow.setVisibility(View.VISIBLE);

            ((TextView) convertView.findViewById(R.id.txtPetParent)).setText(list_client_info_models.get(i).getOwner_name());
            if (list_client_info_models.get(i).getFollow_status().equalsIgnoreCase("Notfollower")) {
                // btnFollow.setText("Follow");
                btnFollow.setImageResource(R.drawable.follow_txt);
            } else {
                //btnFollow.setText("Followed");
                btnFollow.setImageResource(R.drawable.followed_txt);
            }
        } else {
            btnFollow.setVisibility(View.GONE);

            ((TextView) convertView.findViewById(R.id.txtPetParent)).setText(list_client_info_models.get(i).getComment());

            //   list_client_info_models.get(i).setPhoto("");
        }
        ResizeOptions resizeOptions = new ResizeOptions(200, 200);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        if (list_client_info_models.get(i).getPhoto().equalsIgnoreCase("")) {

            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.dogandcat))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(resizeOptions)
                            .build();
            imgPetPic.setImageRequest(imageRequest2);
            imgPetPic.getHierarchy().setRoundingParams(roundingParams);
        } else {
/*
            Glide.with(scrnContxt)

                    .load(list_client_info_models.get(i).getPhoto())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(scrnContxt.getResources(), resource);
                            drawable.setCircular(true);
                            imgPetPic.setImageDrawable(drawable);

                        }


                    });
*/

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

            imgPetPic.setHierarchy(hierarchy);
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(list_client_info_models.get(i).getPhoto()))
                            .setResizeOptions(resizeOptions)
                            .build();
            imgPetPic.setImageRequest(imageRequest2);
            imgPetPic.getHierarchy().setRoundingParams(roundingParams);
        }
        return convertView;
    }

    public interface SearchPetsSelectedListener {
        public void selectedPets(Pet_Search_Details_Model modelObj);
    }
}
