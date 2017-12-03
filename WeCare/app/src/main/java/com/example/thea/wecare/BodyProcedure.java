package com.example.thea.wecare;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class BodyProcedure extends AppCompatActivity implements TextToSpeech.OnInitListener{

    TextView txtDiseaseName, txtDiseaseDescription, txtHerbalName, txtHerbalSciName, txtHerbalDescription, txtHerbalProcedure, txtHerbalDays;
    GridView grv;
    ImageView baymaxBodyProcedure;
    UserDiseaseDbHelper userDiseaseDbHelper;
    HerbalDbHelper herbalDbHelper;
    DiseasesDbHelper diseasesDbHelper;
    private TextToSpeech tts;
    ArrayList<String> diseasesNames;
    String part;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_body_procedure);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        part = bundle.getString("title");
        tts = new TextToSpeech(this, this);

        baymaxBodyProcedure = (ImageView) findViewById(R.id.baymaxBodyProcedure);
        txtDiseaseName = (TextView) findViewById(R.id.txtDiseaseName);
        txtDiseaseDescription = (TextView) findViewById(R.id.txtDiseaseDescription);
        txtHerbalName = (TextView) findViewById(R.id.txtHerbalName);
        txtHerbalSciName = (TextView) findViewById(R.id.txtHerbalSciName);
        txtHerbalDescription = (TextView) findViewById(R.id.txtHerbalDescription);
        txtHerbalProcedure = (TextView) findViewById(R.id.txtHerbalProcedure);
        txtHerbalDays = (TextView) findViewById(R.id.txtHerbalDays);

        userDiseaseDbHelper = new UserDiseaseDbHelper(this);
        herbalDbHelper = new HerbalDbHelper(this);
        diseasesDbHelper = new DiseasesDbHelper(this);
        diseasesNames = new ArrayList<>();

        grv = (GridView) findViewById(R.id.grv);

        grv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemClicked = grv.getItemAtPosition(i).toString();
                txtDiseaseName.setText(itemClicked);
                txtHerbalName.setText("");
//              baymaxBodyProcedure.setImageResource(R.drawable.doc);
                SQLiteDatabase myHerbalDbHelper = herbalDbHelper.getReadableDatabase();
                Cursor cursor = myHerbalDbHelper.query("tbl_Herbal" ,null, null, null, null, null, null);
                toastThis("cliked");
                while (cursor.moveToNext()) {
                    if (txtDiseaseName.getText().toString().toUpperCase().equals(cursor.getString(0).toString().toUpperCase())) {
                        txtDiseaseDescription.setText("Description - " + cursor.getString(1).toString() + System.getProperty("line.separator"));
                        txtHerbalName.setText("Herbal cure" + System.getProperty("line.separator") + cursor.getString(2).toString());
                        txtHerbalDescription.setText("Description - " + cursor.getString(4).toString() + System.getProperty("line.separator"));
                        txtHerbalProcedure.setText("Procedure" + System.getProperty("line.separator") + cursor.getString(5).toString());
                        txtHerbalDays.setText(cursor.getString(6).toString());
                    }
                }

            }
        });

        fetchData();
    }

    public void toastThis(String text){

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void fetchData() {
        diseasesDbHelper = new DiseasesDbHelper(this);
        try {

            diseasesDbHelper.createDataBase();
            diseasesDbHelper.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SQLiteDatabase myDiseasesDbHelper = diseasesDbHelper.getReadableDatabase();
        Cursor cursor = myDiseasesDbHelper.query("tbl_Diseases" ,null, null, null, null, null, null);
        diseasesNames = new ArrayList<String>();
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(part)).equals("true")) {
                diseasesNames.add(cursor.getString(cursor.getColumnIndex("diseaseName")));
            }
        }

        Collections.sort(diseasesNames, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, diseasesNames);
        grv.setAdapter(adapter);

    }

    public void btnConfirmDisease(View v) {
        boolean result = false;
        String diseaseToConfirm, diseaseName, diseaseDescription, herbalName, herbalDescription, herbalProcedure, herbalDays;
        SQLiteDatabase myUserDiseaseDbHelper = userDiseaseDbHelper.getReadableDatabase();
        Cursor cursorDisease = myUserDiseaseDbHelper.query("UserDisease_table", null, null, null, null, null, null);
        int num = 1;

        while (cursorDisease.moveToNext()) {
            num++;
            diseaseToConfirm = cursorDisease.getString(cursorDisease.getColumnIndex("DISEASENAME")).toString().toString().toLowerCase();
            if (txtDiseaseName.getText().toString().toLowerCase().equals(diseaseToConfirm)) {
                result = true;
                Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT).show();
            }
        }
        if (num < 4) {
            if (result == false) {
                String time;
                /*Date*/ Calendar currentTime = Calendar.getInstance()/*.getTime()*/;
                SimpleDateFormat df = new SimpleDateFormat("dd");
                String formattedDate = df.format(currentTime.getTime());
                time = formattedDate.toString();
                double d = Double.valueOf(String.valueOf(time));
                time = String.valueOf( (int) d);

                diseaseName = txtDiseaseName.getText().toString();
                diseaseDescription = txtDiseaseDescription.getText().toString();
                herbalName = txtHerbalName.getText().toString();
                herbalDescription = txtHerbalDescription.getText().toString();
                herbalProcedure = txtHerbalProcedure.getText().toString();

                double days = Double.valueOf(String.valueOf(txtHerbalDays.getText()));
                double daysTo = Double.valueOf(String.valueOf(time));
                double total = days + daysTo;

                herbalDays = String.valueOf( (int) total);
                speakOutNow("do the procedure, for "+txtHerbalDays.getText()+" days");
                userDiseaseDbHelper.insertData(diseaseName, diseaseDescription, herbalName, herbalDescription, herbalProcedure, herbalDays);
            }
        }else{
            Toast.makeText(this, "max data", Toast.LENGTH_SHORT).show();
        }

    }


    public  void dialogYes(){
        Intent intent = new Intent(this, Body.class);
        startActivity(intent);
    }
    public  void dialogNo(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public void onInit(int status) {
        //do nothing
    }

    private void speakOutNow(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }
}
