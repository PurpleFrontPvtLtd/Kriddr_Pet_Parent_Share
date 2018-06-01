/*
package com.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Model.Pet_Activity_Model;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import purplefront.com.kriddrpetparent.R;

*/
/**
 * Created by Niranjan Reddy on 05-04-2018.
 *//*


public class PetActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pet_Activity_Model> pet_activity_models_list;
    Context scrnContxt;
    int PREVIOUS_POS_SEL=-1;
    int CURRENT_POS_SEL;

   public PetActivityAdapter(List<Pet_Activity_Model> list_pet_activity_models, Context context){
        pet_activity_models_list=list_pet_activity_models;
        scrnContxt=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lo_act_pos_1, parent, false);
            return new ViewHolderActPos1(v);

            case 1:
                View v1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.lo_act_pos_2, parent, false);
                return new ViewHolderActPos2(v1);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final Pet_Activity_Model pet_activity_modelObj=pet_activity_models_list.get(position);
        switch (holder.getItemViewType()){
            case 0:
                final ViewHolderActPos1 pos1Holder=(ViewHolderActPos1)holder;
                if(!pet_activity_modelObj.isSelected()) {
                    load_Image(pos1Holder.imgActPos1,pet_activity_modelObj.getImage());
                }
                else{
                    load_Image(pos1Holder.imgActPos1,pet_activity_modelObj.getImage_selected());
                }
                pos1Holder.txtActPos1_Name.setText(pet_activity_modelObj.getName());
                pos1Holder.imgActPos1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!pet_activity_modelObj.isSelected()) {
                            pet_activity_modelObj.setSelected(true);
                            pet_activity_models_list.remove(position);
                            pet_activity_models_list.add(position, pet_activity_modelObj);
                            if(PREVIOUS_POS_SEL==-1) {
                                PREVIOUS_POS_SEL=position;
                            }
                            else {
                                Pet_Activity_Model previous_sel_model = pet_activity_models_list.get(PREVIOUS_POS_SEL);
                                previous_sel_model.setSelected(false);

                                pet_activity_models_list.remove(PREVIOUS_POS_SEL);
                                pet_activity_models_list.add(PREVIOUS_POS_SEL, previous_sel_model);

                                notifyItemChanged(PREVIOUS_POS_SEL);

                                PREVIOUS_POS_SEL = position;
                            }
                            notifyItemChanged(position);
                        }
                    }
                });
                break;
            case 1:
                final ViewHolderActPos2 pos2Holder=(ViewHolderActPos2)holder;
                if(!pet_activity_modelObj.isSelected()) {
                    load_Image(pos2Holder.imgActPos2,pet_activity_modelObj.getImage());
                }
                else{
                    load_Image(pos2Holder.imgActPos2,pet_activity_modelObj.getImage_selected());
                }
                pos2Holder.txtActPos2_Name.setText(pet_activity_modelObj.getName());
                pos2Holder.imgActPos2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!pet_activity_modelObj.isSelected()) {
                            if(PREVIOUS_POS_SEL==-1){
                                PREVIOUS_POS_SEL=position;
                            }
                            else {
                                Pet_Activity_Model previous_sel_model = pet_activity_models_list.get(PREVIOUS_POS_SEL);
                                previous_sel_model.setSelected(false);
                                pet_activity_modelObj.setSelected(true);
                            */
/*Glide.with(scrnContxt)

                                    .load(pet_activity_modelObj.getImage())
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {


                                            pos1Holder.imgActPos1.setImageBitmap(resource);

                                        }


                                    });*//*

                                pet_activity_models_list.remove(PREVIOUS_POS_SEL);
                                pet_activity_models_list.add(PREVIOUS_POS_SEL, previous_sel_model);
                                pet_activity_models_list.remove(position);
                                pet_activity_models_list.add(position, pet_activity_modelObj);
                                notifyItemChanged(PREVIOUS_POS_SEL);
                                notifyItemChanged(position);
                                PREVIOUS_POS_SEL = position;
                            }
                        }
                    }
                });
                break;
        }
    }


    public void load_Image(final ImageView imgActivity, String imgUrl){

        Glide.with(scrnContxt)

                .load(imgUrl)
                .asBitmap()

                .diskCacheStrategy(DiskCacheStrategy.RESULT)

                .into(imgActivity);




    }


    @Override
    public int getItemViewType(int position) {
        if(position%2==0){
         return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return pet_activity_models_list.size();
    }

    class ViewHolderActPos1 extends RecyclerView.ViewHolder {
       ImageView imgActPos1;

       TextView txtActPos1_Name;
        public ViewHolderActPos1(View itemView) {
            super(itemView);
            imgActPos1=(ImageView)itemView.findViewById(R.id.img_act_pos_1);
            txtActPos1_Name=(TextView)itemView.findViewById(R.id.txt_act_name_1);

        }
    }

    class ViewHolderActPos2 extends RecyclerView.ViewHolder {
        ImageView imgActPos2;

        TextView txtActPos2_Name;
        public ViewHolderActPos2(View itemView) {
            super(itemView);
            imgActPos2=(ImageView)itemView.findViewById(R.id.img_act_pos_2);
            txtActPos2_Name=(TextView)itemView.findViewById(R.id.txt_act_name_2);

        }

    }
}

*/
