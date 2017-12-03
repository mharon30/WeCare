package com.example.thea.wecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "Student_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MIDDLE";
    public static final String COL_5 = "DAY";
    public static final String COL_6 = "MONTH";
    public static final String COL_7 = "YEAR";
    public static final String COL_8 = "WEIGHT";
    public static final String COL_9 = "HEIGHT";
    public static final String COL_10 = "GENDER";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT ,SURNAME TEXT ,MIDDLE TEXT ,DAY INTEGER ,MONTH INTEGER ,YEAR INTEGER ,WEIGHT INTEGER ,HEIGHT INTEGER ,GENDER TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public boolean insertData(String name, String surname,String middle, String day, String month, String year, String weight, String height, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, middle);
        contentValues.put(COL_5, day);
        contentValues.put(COL_6, month);
        contentValues.put(COL_7, year);
        contentValues.put(COL_8, weight);
        contentValues.put(COL_9, height);
        contentValues.put(COL_10, gender);
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
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, "id=?",new String[]{id});
        return i;
    }
}