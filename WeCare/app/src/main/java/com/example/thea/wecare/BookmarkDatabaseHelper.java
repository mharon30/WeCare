package com.example.thea.wecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tottie on 10/11/2017.
 */

public class BookmarkDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "HerbalSave.db";
    public static final String TABLE_NAME = "Herbal_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "ITEMNAME";
    public static final String COL_3 = "TYPE";

    public BookmarkDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT ,ITEMNAME TEXT ,TYPE TEXT )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public boolean insertData(String itemname, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemname);
        contentValues.put(COL_3, type);


        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME,null);
        return  res;
    }
    public Integer deleteData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, "ITEMNAME=?",new String[]{name});
        return i;
    }
}
