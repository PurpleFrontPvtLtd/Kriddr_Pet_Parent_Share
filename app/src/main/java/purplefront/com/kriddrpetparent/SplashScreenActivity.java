package purplefront.com.kriddrpetparent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.Model.UserModel;
import com.db.DBHelper;


/**
 * Created by pf-05 on 2/5/2018.
 */

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    Context mContext;
    boolean per_flag;
    String Per_List[]=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int PERMISSION_REQ_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //hasPermission(this);
        screen_Handler();

    }


    public void screen_Handler() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                try {
                    DBHelper dbHelper = new DBHelper();
                    dbHelper.open(SplashScreenActivity.this);
                    Cursor c = dbHelper.select_Query(DBHelper.TABLE_USER, null, null, null);
                    // c.moveToFirst();
                    if (c.getCount() > 0) {
                        c.moveToFirst();

                        UserModel user_model = new UserModel();
                        user_model.setOwner_id(c.getString(c.getColumnIndex(DBHelper.KEY_USID)));
                        user_model.setOwner_name(c.getString(c.getColumnIndex(DBHelper.KEY_USER)));
                        user_model.setAddress(c.getString(c.getColumnIndex(DBHelper.KEY_ADDRESS)));
                        user_model.setMobile(c.getString(c.getColumnIndex(DBHelper.KEY_PHONE)));
                        user_model.setPreferred_contact(c.getString(c.getColumnIndex(DBHelper.KEY_PREF_CONT)));
                        user_model.setEmail(c.getString(c.getColumnIndex(DBHelper.KEY_EMAIL)));
                        user_model.setStatus(c.getString(c.getColumnIndex(DBHelper.KEY_STATUS)));
                        user_model.setPhoto(c.getString(c.getColumnIndex(DBHelper.KEY_PROF_PIC)));

                        if(user_model.getStatus().equalsIgnoreCase("active")) {


                            Intent intent = new Intent(SplashScreenActivity.this, KridderNavigationActivity.class);
                            intent.putExtra(KridderNavigationActivity.USER_MODEL_TAG, user_model);
                            SplashScreenActivity.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }

                    } else {

                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        //  Log.d("AAQWID","AAQWID"+id);
                        SplashScreenActivity.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                     }


                } catch (SQLException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } catch (Exception e) {

                    Toast.makeText(SplashScreenActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, SPLASH_TIME_OUT);
    }



    public void hasPermission(Context contextObj){
        mContext=contextObj;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this != null && Per_List != null) {
            per_flag=true;
            for (String permission : Per_List) {

                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED)
                {

                    per_flag=false;
                    //isPermissionSuccess=false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                            permission)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        ActivityCompat.requestPermissions((Activity)mContext,
                                new String[]{permission},
                                PERMISSION_REQ_CODE);

                    }
                    else {
                        ActivityCompat.requestPermissions((Activity)mContext,
                                Per_List,
                                PERMISSION_REQ_CODE);
                    }
                    break;

                }
                else{
                    // isPermissionSuccess=true;
                }

                //i++;
            }


            /*if(isPermissionSuccess && (i==Per_List.length)){

                screen_Handler();
            }*/

        }

        if(per_flag){

            screen_Handler();
        }



    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults)
    {

        per_flag=true;
        int i=0;
        final String Permission_Denied;
        boolean showRationale=false;
        for (final String permission:permissions){

            if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                //  isPermissionSuccess=true;
                i++;
            }
            else{
                Permission_Denied=permission;
                per_flag=false;
                //isPermissionSuccess=false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreenActivity.this,"Need permission "+Permission_Denied, Toast.LENGTH_LONG).show();
                    }
                });
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Permission_Denied );
                if (! showRationale) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, PERMISSION_REQ_CODE);
                }

                break;
                //       return;
            }

        }

            screen_Handler();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            //screen_Handler();
            //   hasPermission(SplashScreen.this);
        }
        //hasPermission();
        hasPermission(SplashScreenActivity.this);

    }



}
