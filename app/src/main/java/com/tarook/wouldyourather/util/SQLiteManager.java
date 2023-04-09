package com.tarook.wouldyourather.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.model.Vote;
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
    private static final String OPTIONS_NUMBER = "number"; // 0 or 1

    private static final String USERS_TABLE = "users";
    private static final String USERS_ID = "id";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_DESCRIPTION = "description";
    private static final String USERS_PICTURE = "picture";

    private static final String VOTES_TABLE = "votes";
    private static final String VOTES_ID = "id";
    private static final String VOTES_WYR_ID = "wyrInfos_id";
    private static final String VOTES_OPTION_NUMBER = "option_id";
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
                .append(OPTIONS_NUMBER).append(" INTEGER, ")
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
                .append(VOTES_OPTION_NUMBER).append(" INTEGER, ")
                .append(VOTES_USER_ID).append(" INTEGER, ")
                .append(VOTES_WYR_ID).append(" INTEGER, ")
                .append("FOREIGN KEY (").append(VOTES_OPTION_NUMBER).append(") REFERENCES options(id), ")
                .append("FOREIGN KEY (").append(VOTES_USER_ID).append(") REFERENCES users(id), ")
                .append("FOREIGN KEY (").append(VOTES_WYR_ID).append(") REFERENCES wyrInfos(id));");
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
            values.put(OPTIONS_NUMBER, options.indexOf(option));
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

    public int getNumberOfVotesForWYR(int wyrId){
        SQLiteDatabase db = getReadableDatabase();
        int numberOfVotes = 0;
        try(Cursor result = db.rawQuery("SELECT * FROM " + VOTES_TABLE + " WHERE " + VOTES_WYR_ID + " = " + wyrId, null)){
            if(result.getCount() != 0) {
                numberOfVotes = result.getCount();
            }
        }
        return numberOfVotes;
    }

    public int getNumberOfVotesForOption(int wyrId, int optionNumber){
        SQLiteDatabase db = getReadableDatabase();
        int numberOfVotes = 0;
        try(Cursor result = db.rawQuery("SELECT * FROM " + VOTES_TABLE + " WHERE " + VOTES_WYR_ID + " = " + wyrId + " AND " + VOTES_OPTION_NUMBER + " = " + optionNumber, null)){
            if(result.getCount() != 0) {
                numberOfVotes = result.getCount();
            }
        }
        return numberOfVotes;
    }

    public void addUser(Profile profile){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_USERNAME, profile.getName());
        values.put(USERS_DESCRIPTION, profile.getDescription());
        values.put(USERS_PICTURE, getBitmapAsByteArray(profile.getProfilePicture()));

        db.insert(USERS_TABLE, null, values);
    }

    public Profile getUserById(int id){
        SQLiteDatabase db = getReadableDatabase();
        // get the user
        try(Cursor c = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_ID + " = " + id, null)){
            if(c.moveToFirst()){
                @SuppressLint("Range") int userId = c.getInt(c.getColumnIndex(USERS_ID));
                @SuppressLint("Range") String username = c.getString(c.getColumnIndex(USERS_USERNAME));
                @SuppressLint("Range") String description = c.getString(c.getColumnIndex(USERS_DESCRIPTION));
                @SuppressLint("Range") Bitmap picture = getBitmapFromByteArray(c.getBlob(c.getColumnIndex(USERS_PICTURE)));
                return new Profile(userId, username, description, picture);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Profile getUserByUsername(String username){
        SQLiteDatabase db = getReadableDatabase();
        // get the user
        try(Cursor c = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + " = '" + username + "'", null)){
            if(c.moveToFirst()){
                @SuppressLint("Range") int userId = c.getInt(c.getColumnIndex(USERS_ID));
                @SuppressLint("Range") String username2 = c.getString(c.getColumnIndex(USERS_USERNAME));
                @SuppressLint("Range") String description = c.getString(c.getColumnIndex(USERS_DESCRIPTION));
                @SuppressLint("Range") Bitmap picture = getBitmapFromByteArray(c.getBlob(c.getColumnIndex(USERS_PICTURE)));
                return new Profile(userId, username2, description, picture);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(Profile profile){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_USERNAME, profile.getName());
        values.put(USERS_DESCRIPTION, profile.getDescription());
        values.put(USERS_PICTURE, getBitmapAsByteArray(profile.getProfilePicture()));

        db.update(USERS_TABLE, values, USERS_ID + " = " + profile.getId(), null);
    }

    public ArrayList<Profile> getAllUsers(){
        ArrayList<Profile> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // get the users
        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE, null)){
            if(result.getCount() != 0) {
                while (result.moveToNext()) {
                    @SuppressLint("Range") int userId = result.getInt(result.getColumnIndex(USERS_ID));
                    @SuppressLint("Range") String username = result.getString(result.getColumnIndex(USERS_USERNAME));
                    @SuppressLint("Range") String description = result.getString(result.getColumnIndex(USERS_DESCRIPTION));
                    @SuppressLint("Range") Bitmap picture = getBitmapFromByteArray(result.getBlob(result.getColumnIndex(USERS_PICTURE)));
                    userList.add(new Profile(userId, username, description, picture));
                }
            }
        }
        return userList;
    }

    public void getOptionsFromWYRID(int wyrId){
        SQLiteDatabase db = getReadableDatabase();
        // get the options
        try(Cursor result = db.rawQuery("SELECT * FROM " + OPTIONS_TABLE + " WHERE " + OPTIONS_WYR_ID + " = " + wyrId, null)){
            if(result.getCount() != 0) {
                while (result.moveToNext()) {
                    @SuppressLint("Range") String option = result.getString(result.getColumnIndex(OPTIONS_OPTION));
                    // add the option to the wyr
                }
            }
        }
    }

    /**
     * Checks if a user has voted for a wyr
     * @param wyrId the vote to check
     * @param userId the user to check
     */
    public boolean hasVotedFor(int wyrId, int userId){ // gets a user's vote by checking if there is a vote with the same wyrId and userId
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + VOTES_TABLE + " WHERE " + VOTES_WYR_ID + " = " + wyrId + " AND " + VOTES_USER_ID + " = " + userId, null)){
            if(result.getCount() != 0) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public void addVote(Vote vote){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VOTES_WYR_ID, vote.getWyrId());
        values.put(VOTES_USER_ID, vote.getUserId());
        values.put(VOTES_OPTION_NUMBER, vote.getOptionNumber());

        db.insert(VOTES_TABLE, null, values);
    }

    public void deleteVote(int wyrId, int userId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(VOTES_TABLE, VOTES_WYR_ID + " = " + wyrId + " AND " + VOTES_USER_ID + " = " + userId, null);
    }

    public Vote getVote(int wyrId, int userId){
        SQLiteDatabase db = getReadableDatabase();
        // get the vote
        try(Cursor c = db.rawQuery("SELECT * FROM " + VOTES_TABLE + " WHERE " + VOTES_WYR_ID + " = " + wyrId + " AND " + VOTES_USER_ID + " = " + userId, null)){
            if(c.moveToFirst()){
                @SuppressLint("Range") int voteId = c.getInt(c.getColumnIndex(VOTES_ID));
                @SuppressLint("Range") int wyrId2 = c.getInt(c.getColumnIndex(VOTES_WYR_ID));
                @SuppressLint("Range") int userId2 = c.getInt(c.getColumnIndex(VOTES_USER_ID));
                @SuppressLint("Range") int optionNumber = c.getInt(c.getColumnIndex(VOTES_OPTION_NUMBER));
                return new Vote(voteId, wyrId2, optionNumber, userId2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
