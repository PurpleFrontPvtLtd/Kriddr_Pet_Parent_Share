package com.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Model.Document_categary_model;

import java.util.List;

import purplefront.com.kriddrpetparent.R;

public class SpinnerArrayAdapter extends ArrayAdapter {
    Context scrnContxt;
    List<Document_categary_model> document_categary_modelListObj;

    public SpinnerArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, List<Document_categary_model> document_categary_modelList) {
        super(context, resource, textViewResourceId,document_categary_modelList);
        scrnContxt=context;
        document_categary_modelListObj=document_categary_modelList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
       // label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(document_categary_modelListObj.get(position).getDocuments_name());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(scrnContxt);
        if(convertView == null){
            convertView = inflater.inflate( R.layout.simple_spinner_contact,parent, false);
        }
        final Document_categary_model rowItem = document_categary_modelListObj.get(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.spinnerTarget);
        txtTitle.setText(rowItem.getDocuments_name());

        return convertView;
        //return super.getDropDownView(position, convertView, parent);
    }
    public interface category_selected_listener{
        public void on_spinner_catg_sel(String catgId);
    }
}
