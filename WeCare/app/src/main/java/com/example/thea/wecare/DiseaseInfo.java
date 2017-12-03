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
import android.widget.TextView;

public class DiseaseInfo extends AppCompatActivity implements TextToSpeech.OnInitListener{


    TextView txtDiseaseLearning, txtDescriptionLearning, txtCheckerLearning;
    String itemTitle;
    private TextToSpeech tts;
    HerbalDbHelper herbalDbHelper;
    BookmarkDatabaseHelper bookmarkDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_disease_info);


        //****************************************************************************Initialization
        txtCheckerLearning = (TextView) findViewById(R.id.txtCheckerLearning);
        herbalDbHelper = new HerbalDbHelper(this);
        bookmarkDatabaseHelper = new BookmarkDatabaseHelper(this);
        tts = new TextToSpeech(this, this);


        //******************************************************************************get the data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        itemTitle = bundle.getString("title");

        //******************************************************************************display data
        txtDiseaseLearning = (TextView) findViewById(R.id.txtDiseaseLearning);
        txtDiseaseLearning.setText(itemTitle);
        txtDescriptionLearning = (TextView) findViewById(R.id.txtDescriptionLearning);
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
            txtCheckerLearning.setText(cursor.getString(cursor.getColumnIndex("diseaseName")));
            if (txtCheckerLearning.getText().toString().toUpperCase().equals(txtDiseaseLearning.getText().toString().toUpperCase())) {
                txtDescriptionLearning.setText(cursor.getString(cursor.getColumnIndex("diseaseDescription")));
            }
        }
    }


    @Override
    public void onInit(int status) {
        //Put Codes
    }

    //*******************************************************Function for inserting data to bookmark
    public void btnSaveBookmarkLearning(View v){
        String type = "disease";
        Cursor data = bookmarkDatabaseHelper.getAllData();
        String newEntryHerbal = txtDiseaseLearning.getText().toString();
        boolean found = false;
        //******************************************************************************************
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            while (!data.isAfterLast()) {
                txtCheckerLearning.setText(data.getString(1).toString().toUpperCase());
                if (txtCheckerLearning.getText().toString().equals(txtDiseaseLearning.getText().toString().toUpperCase())){
                    found = true;
                }
                data.moveToNext();
            }
            //******************************************************************************************
        }
        if (found == true){
            speakOutNow("This Disease is already in Bookmark.");
        }else {
            AddData(newEntryHerbal, type);
        }
    }

    public void AddData(String newEntry, String type) {

        boolean insertData = bookmarkDatabaseHelper.insertData(newEntry, type);

        if(insertData==true){
            speakOutNow("Disease successfully added to bookmark.");
        }else{
            speakOutNow("Something went wrong while adding the data.");
        }
    }


    //********************************************************************************Text to speech
    private void speakOutNow (String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);

    }
}
