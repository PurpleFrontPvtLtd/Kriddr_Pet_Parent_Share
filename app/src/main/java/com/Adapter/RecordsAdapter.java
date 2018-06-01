package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Model.DocumentModel;
import com.facebook.drawee.drawable.AutoRotateDrawable;
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
 * Created by pf-05 on 2/22/2018.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    List<DocumentModel> docList = new ArrayList<>();
    Context context;
    RecordsAdapter.DataFromAdapterToFragment dataFromAdapterToFragment;



    public RecordsAdapter(List<DocumentModel> docList, Context context, RecordsAdapter.DataFromAdapterToFragment listener)
    {
        Log.d("JASIRE","JASIRE"+docList.size());
        this.docList = docList;
        this.context = context;
        this.dataFromAdapterToFragment = listener;

    }

    public interface DataFromAdapterToFragment
    {
        public void getRecordsinfo(DocumentModel docObj);
    }


    @Override
    public RecordsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lo_row_rec,parent,false);
        return new RecordsAdapter.MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecordsAdapter.MyViewHolder holder, final int position) {

        Log.d("PIRSDR","PIRSDR"+docList.get(position));

       /* Glide.with(context).load(docList.get(position).getDocument()).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                holder.record_image.setImageBitmap(resource);

            }
        });*/
        ResizeOptions resizeOptions=new ResizeOptions(150,150);
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
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(docList.get(position).getDocument()))
                            .setResizeOptions(resizeOptions)
                            .build();
            holder.record_image.setImageRequest(imageRequest2);



        if(docList.get(position).getCreated().equalsIgnoreCase("Empty"))
        {
            holder.record_name.setVisibility(View.GONE);
        }
        else
        {
            holder.record_name.setText(docList.get(position).getCreated());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataFromAdapterToFragment.getRecordsinfo(docList.get(position));

            }
        });


    }

    @Override
    public int getItemCount() {
        Log.d("RECNAMLST","RECNAMLST"+docList.size());
        return docList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView record_name;
        SimpleDraweeView record_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            record_image = (SimpleDraweeView) itemView.findViewById(R.id.recordimagView);
            record_name = (TextView)itemView.findViewById(R.id.record_name);

        }
    }
}
