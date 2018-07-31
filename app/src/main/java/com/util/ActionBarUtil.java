package com.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import purplefront.com.kriddrpetparent.R;


/**
 * Created by Niranjan Reddy on 12-02-2018.
 */

public class ActionBarUtil {
    AppCompatActivity CallActivity;
    ImageView imgBack, imgSettings, imgSearch,imgNotify,imgSharePet;
    TextView txtTitle;
    EditText edtSearch;
    ActionBar actionBar;

    public ActionBarUtil(AppCompatActivity activity) {
        CallActivity = activity;
        actionBar = CallActivity.getSupportActionBar();
        SetView();
        setViewInvisible();
    }
    public void setViewInvisible(){
        imgSettings.setVisibility(View.INVISIBLE);
        imgBack.setVisibility(View.INVISIBLE);
        imgSearch.setVisibility(View.INVISIBLE);
        imgNotify.setVisibility(View.INVISIBLE);
        edtSearch.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
        imgSharePet.setVisibility(View.INVISIBLE);
        edtSearch.setText("");
        txtTitle.setClickable(false);

        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
    }


    public void setActionBarVisible() {
        actionBar.show();
    }

    public void SetActionBarHide() {
        actionBar.hide();
    }

    public void SetView() {
        actionBar.setCustomView(R.layout.actionbar_view);
        actionBar.setDisplayShowCustomEnabled(true);
        View v = actionBar.getCustomView();
        txtTitle = (TextView) v.findViewById(R.id.textBarTitle);
        imgBack = (ImageView) v.findViewById(R.id.img_actionBarBack);
        imgSettings = (ImageView) v.findViewById(R.id.img_settings_menu);
        edtSearch = (EditText) v.findViewById(R.id.edtSearchText);
        imgSearch = (ImageView) v.findViewById(R.id.imgSearch);
        imgNotify= (ImageView) v.findViewById(R.id.img_Notify);
        imgSharePet=(ImageView)v.findViewById(R.id.imgSharePet);
        edtSearch.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public EditText getEdtSearch(){
        return edtSearch;
    }
    public TextView getTitle(){
        return txtTitle;
    }
    public ImageView getImgBack() {
        return imgBack;
    }
    public ImageView getImgSharePet(){
        return imgSharePet;
    }

    public ImageView getImgSettings() {
        return imgSettings;
    }

    public ImageView getImgNotify(){
        return imgNotify;
    }

    public ImageView getImgSearch() {
        return imgSearch;
    }
}
