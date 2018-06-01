package com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Niranjan Reddy on 22-02-2018.
 */

public class DBHelper {
    public static final String DATABASE_NAME = "kriddr_parent";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_USID = "ownr_id";
    public static final String KEY_USER = "ownr_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "mobile";
    public static final String KEY_ADDRESS= "address";
    public static final String KEY_PREF_CONT = "prefed_cont";
    public static final String KEY_PROF_PIC = "prof_pic";

    public static final String TABLE_USER = "user_info";
    public static final String KEY_STATUS = "user_status";

    private SQLiteDatabase ourDB;

    protected static class DatabaseCreation extends SQLiteOpenHelper {

        public DatabaseCreation(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                    KEY_USID + " INTEGER NOT NULL, " +
                    KEY_USER + " TEXT, " +
                    KEY_ADDRESS+ " TEXT, " +
                    KEY_PREF_CONT+ " TEXT, " +
                    KEY_PROF_PIC+ " TEXT, " +
                    KEY_STATUS + " TEXT, " +
                    KEY_EMAIL + " TEXT , " +
                    KEY_PHONE + " TEXT NOT NULL )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }

    }


    public void createuser(String id2, String name, String email,  String phone, String status, String address, String pref_cont,String prof_pic) {
        deleteTable();
        ContentValues cv = new ContentValues();
        cv.put(KEY_USID, id2);
        cv.put(KEY_USER, name);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_PHONE, phone);
        cv.put(KEY_ADDRESS,address);
        cv.put(KEY_PREF_CONT,pref_cont);
        cv.put(KEY_PROF_PIC,prof_pic);
        cv.put(KEY_STATUS, status);

        ourDB.insert(TABLE_USER, null, cv);

    }

    public void open(Context context) {
        DatabaseCreation dbCreate = new DatabaseCreation(context);
        ourDB = dbCreate.getWritableDatabase();

    }
    public void deleteTable(){

        ourDB.delete(TABLE_USER, null, null);
        //  ourdb.rawQuery(delQuery,null);
    }
    public void UpdateDetail(String name,String address,String mobile,String photoUrl,String ownerId){
        ContentValues cv=new ContentValues();
        cv.put(KEY_USER,name);
        cv.put(KEY_PHONE,mobile);
        cv.put(KEY_ADDRESS,address);
        cv.put(KEY_PROF_PIC,photoUrl);
        ourDB.update(TABLE_USER,cv,KEY_USID+"=?",new String[]{ownerId});
    }
    public Cursor select_Query(String Table,String columnSel[],String selection,String selectionArgs[])
    {
        Cursor cursor=ourDB.query(Table,columnSel,selection,selectionArgs,null,null,null);
        return cursor;
    }
    public void _closeDb(){
        ourDB.close();
    }

}
