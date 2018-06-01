package com.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.db.DBHelper;

import purplefront.com.kriddrpetparent.MainActivity;
import purplefront.com.kriddrpetparent.R;

/**
 * Created by Niranjan Reddy on 27-02-2018.
 */

public class AlertDialogHandler  {
   // static Context scrnContext;

    public static void showDialog(final Activity scrnContext,String Message, final boolean isExit){
        AlertDialog.Builder builder = new AlertDialog.Builder(scrnContext);
        if(isExit && Message.toLowerCase().contains("sign out")) {

        }
        else
            Message="Do you want to exit?";
       final String tempMes=Message;
        builder.setMessage(Message)
                .setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         if(isExit && tempMes.toLowerCase().contains("sign out")) {
                             DBHelper dbHelper=new DBHelper();
                             dbHelper.open(scrnContext);
                             dbHelper.deleteTable();
                             dbHelper._closeDb();
                             Toast.makeText(scrnContext,"Successfully signed out",Toast.LENGTH_SHORT ).show();
                             Intent newIntent=new Intent(scrnContext,MainActivity.class);
                             scrnContext.finish();
                             scrnContext.startActivity(newIntent);
                         }
                         else if(isExit){
                             ((Activity) scrnContext).finish();
                         }
                         else{
                             ((AppCompatActivity)scrnContext).getSupportFragmentManager().popBackStackImmediate();
                         }
                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Button positiveButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FF0000"));
        //positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

        negativeButton.setTextColor(Color.parseColor("#FF0000"));
    }

    public static void _fragment_handelBackKey(View rootView, final Activity context, final String msg, final boolean isExit){
        if(msg.toLowerCase().contains("sign out"))
        {
            showDialog(context,msg,isExit);
        }
        else {
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();

            rootView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                        if (msg.equalsIgnoreCase("")) {
                            ((AppCompatActivity) context).getSupportFragmentManager().popBackStackImmediate();
                        } else
                            showDialog(context, msg, isExit);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

}
