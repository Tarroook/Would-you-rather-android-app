package com.tarook.wouldyourather.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tarook.wouldyourather.model.WouldYouRather;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper{
    private static SQLiteManager instance;
    private static final String DB_NAME = "wyrDB";
    private static final int DB_VERSION = 1;

    private static final String WYR_TABLE = "wyrInfos";
    private static final String WYR_ID = "id";
    private static final String WYR_QUESTION = "question";

    private static final String OPTIONS_TABLE = "options";
    private static final String OPTIONS_ID = "id";
    private static final String OPTIONS_OPTION = "option";
    private static final String OPTIONS_WYR_ID = "wyrInfos_id";

    private static final String USERS_TABLE = "users";
    private static final String USERS_ID = "id";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_DESCRIPTION = "description";
    private static final String USERS_PICTURE = "picture";

    private static final String VOTES_TABLE = "votes";
    private static final String VOTES_ID = "id";
    private static final String VOTES_OPTION_ID = "option_id";
    private static final String VOTES_USER_ID = "user_id";

    private SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized SQLiteManager getInstance(Context context) {
        if(instance == null)
            instance = new SQLiteManager(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // create the tables
        StringBuilder sql;

        sql = new StringBuilder().append("CREATE TABLE ").append(WYR_TABLE).append(" (")
                .append(WYR_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(WYR_QUESTION).append(" TEXT);");
        db.execSQL(sql.toString());


        // need to reference the id of the wyrInfo table as a foreign key
        sql = new StringBuilder().append("CREATE TABLE ").append(OPTIONS_TABLE).append(" (")
                .append(OPTIONS_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(OPTIONS_OPTION).append(" TEXT, ")
                .append(OPTIONS_WYR_ID).append(" INTEGER, ")
                .append("FOREIGN KEY (").append(OPTIONS_WYR_ID).append(") REFERENCES wyrInfos(id));");
        db.execSQL(sql.toString());


        sql = new StringBuilder().append("CREATE TABLE ").append(USERS_TABLE).append(" (")
                .append(USERS_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(USERS_USERNAME).append(" TEXT, ")
                .append(USERS_DESCRIPTION).append(" TEXT, ")
                .append(USERS_PICTURE).append(" BLOB);");
        db.execSQL(sql.toString());


        sql = new StringBuilder().append("CREATE TABLE ").append(VOTES_TABLE).append(" (")
                .append(VOTES_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(VOTES_OPTION_ID).append(" INTEGER, ")
                .append(VOTES_USER_ID).append(" INTEGER, ")
                .append("FOREIGN KEY (").append(VOTES_OPTION_ID).append(") REFERENCES options(id), ")
                .append("FOREIGN KEY (").append(VOTES_USER_ID).append(") REFERENCES users(id));");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void addWYR(WouldYouRather wyr){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WYR_QUESTION, wyr.getQuestion());

        db.insert(WYR_TABLE, null, values);

        // get the id of the wyrInfo that was just added
        Cursor c = db.rawQuery("SELECT * FROM " + WYR_TABLE + " WHERE " + WYR_QUESTION + " = '" + wyr.getQuestion() + "'", null);
        if(c.moveToFirst()){
            @SuppressLint("Range") int wyrId = c.getInt(c.getColumnIndex(WYR_ID));
            addOptions(wyrId, wyr.getOptions());
        }
    }
    public void addOptions(int wyrId, ArrayList<String> options){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        for(String option : options){
            values.put(OPTIONS_OPTION, option);
            values.put(OPTIONS_WYR_ID, wyrId);
            db.insert(OPTIONS_TABLE, null, values);
            values.clear();
        }
    }


    public WouldYouRather getWYR(int id) {
        WouldYouRather wyr = null;
        SQLiteDatabase db = getReadableDatabase();

        // get the wyrInfo
        Cursor c = db.rawQuery("SELECT * FROM " + WYR_TABLE + " WHERE " + WYR_ID + " = " + id, null);
        if(c.moveToFirst()){
            @SuppressLint("Range") int wyrId = c.getInt(c.getColumnIndex(WYR_ID));
            @SuppressLint("Range") String question = c.getString(c.getColumnIndex(WYR_QUESTION));
            wyr = new WouldYouRather(wyrId, question);

            // get the options and add them to the wyrInfo using addOption()
            c = db.rawQuery("SELECT * FROM " + OPTIONS_TABLE + " WHERE " + OPTIONS_WYR_ID + " = " + id, null);
            if(c.moveToFirst()){
                do{
                    @SuppressLint("Range") String option = c.getString(c.getColumnIndex(OPTIONS_OPTION));
                    wyr.addOption(option);
                }while(c.moveToNext());
            }
        }
        c.close();
        return wyr;
    }

    public ArrayList<WouldYouRather> getAllWYRS(){
        ArrayList<WouldYouRather> wyrList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // get the wyrInfos
        try(Cursor result = db.rawQuery("SELECT * FROM " + WYR_TABLE, null)){
            if(result.getCount() != 0) {
                while (result.moveToNext()) {
                    @SuppressLint("Range") int wyrId = result.getInt(result.getColumnIndex(WYR_ID));
                    @SuppressLint("Range") String question = result.getString(result.getColumnIndex(WYR_QUESTION));
                    WouldYouRather wyr = new WouldYouRather(wyrId, question);

                    // get the options and add them to the wyrInfo using addOption()
                    try(Cursor result2 = db.rawQuery("SELECT * FROM " + OPTIONS_TABLE + " WHERE " + OPTIONS_WYR_ID + " = " + wyrId, null)){
                        if(result2.getCount() != 0) {
                            while (result2.moveToNext()) {
                                @SuppressLint("Range") String option = result2.getString(result2.getColumnIndex(OPTIONS_OPTION));
                                wyr.addOption(option);
                            }
                        }
                    }
                    wyrList.add(wyr);
                }
            }
        }
        return wyrList;
    }

    public void deleteWYR(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(WYR_TABLE, WYR_ID + " = " + id, null);
        db.delete(OPTIONS_TABLE, OPTIONS_WYR_ID + " = " + id, null);
    }

    private Bitmap getBitmapFromByteArray(byte[] data) { // database -> class
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) { // class -> database
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
