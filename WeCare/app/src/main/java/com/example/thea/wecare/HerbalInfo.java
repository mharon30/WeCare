package com.example.thea.wecare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class HerbalInfo extends AppCompatActivity implements TextToSpeech.OnInitListener{

    TextView txtTitle, txtDescription, txtChecker;
    String itemTitle;
    private TextToSpeech tts;
    BookmarkDatabaseHelper bookmarkDatabaseHelper;
    HerbalDbHelper herbalDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_herbal_info);


        //****************************************************************************Initialization
        txtChecker = (TextView) findViewById(R.id.txtChecker);
        herbalDbHelper = new HerbalDbHelper(this);
        bookmarkDatabaseHelper = new BookmarkDatabaseHelper(this);
        tts = new TextToSpeech(this, this);

        //******************************************************************************get the data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        itemTitle = bundle.getString("title");

        //******************************************************************************display data
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(itemTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        fetchData();
    }


    public void fetchData() {
        herbalDbHelper = new HerbalDbHelper(this);
        try {

            herbalDbHelper.createDataBase();
            herbalDbHelper.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SQLiteDatabase sd = herbalDbHelper.getReadableDatabase();
        Cursor cursor = sd.query("tbl_Herbal" ,null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            txtChecker.setText(cursor.getString(cursor.getColumnIndex("herbalName")));
            if (txtChecker.getText().toString().toUpperCase().equals(txtTitle.getText().toString().toUpperCase())) {
                txtDescription.setText(cursor.getString(cursor.getColumnIndex("herbalDescription")));
            }
        }
    }


    //*************************************************************************Text to speech OnInit
    @Override
    public void onInit(int text) {

        if (text == TextToSpeech.SUCCESS){

            int language = tts.setLanguage(Locale.ENGLISH);
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED){
                speakOutNow("");

            }
            else{

            }

        }
        else{

        }
    }

    //********************************************************************************Text to speech
    private void speakOutNow (String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);

    }


    //*******************************************************Function for inserting data to bookmark
    public void btnSaveBookmark(View v){
        String type = "herbal";
        Cursor data = bookmarkDatabaseHelper.getAllData();
        String newEntryHerbal = txtTitle.getText().toString();
        boolean found = false;
        //******************************************************************************************
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            while (!data.isAfterLast()) {
                txtChecker.setText(data.getString(1).toString().toUpperCase());
                if (txtChecker.getText().toString().equals(txtTitle.getText().toString().toUpperCase())){
                    found = true;
                }
                data.moveToNext();
            }
            //******************************************************************************************
        }
        if (found == true){
            speakOutNow("This Herbal is already in Bookmark.");
        }else {
            AddData(newEntryHerbal, type);
        }
    }

    public void AddData(String newEntry, String type) {

        boolean insertData = bookmarkDatabaseHelper.insertData(newEntry, type);

        if(insertData==true){
            speakOutNow("Herbal successfully added to bookmark.");
        }else{
            speakOutNow("Something went wrong while adding the data.");
        }
    }
}
