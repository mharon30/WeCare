package com.example.thea.wecare;

/**
 * Created by Tottie on 10/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDiseaseDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "UserDisease.db";
    public static final String TABLE_NAME = "UserDisease_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "DISEASENAME";
    public static final String COL_3 = "DISEASEDESCRIPTION";
    public static final String COL_4 = "HERBALNAME";
    public static final String COL_5 = "HERBALDESCRIPTION";
    public static final String COL_6 = "HERBALPROCEDURE";
    public static final String COL_7 = "HERBALDAYS";

    public UserDiseaseDbHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT ,DISEASENAME TEXT ,DISEASEDESCRIPTION TEXT ,HERBALNAME TEXT ,HERBALDESCRIPTION TEXT ,HERBALPROCEDURE TEXT ,HERBALDAYS TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public boolean insertData(String diseaseName, String diseaseDescription, String herbalName,
                              String herbalDescription, String herbalProcedure, String herbalDays){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, diseaseName);
        contentValues.put(COL_3, diseaseDescription);
        contentValues.put(COL_4, herbalName);
        contentValues.put(COL_5, herbalDescription);
        contentValues.put(COL_6, herbalProcedure);
        contentValues.put(COL_7, herbalDays);


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
        int i = db.delete(TABLE_NAME, "DISEASENAME=?",new String[]{name});
        return i;
    }
}
