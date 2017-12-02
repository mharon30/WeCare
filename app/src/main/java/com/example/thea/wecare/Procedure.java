package com.example.thea.wecare;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Procedure extends AppCompatActivity {

    HerbalDbHelper herbalDbHelper;
    TextView diseasesNameProcedure, txtDiseaseName, txtDiseaseDescription, txtHerbalName, txtHerbalSciName, txtHerbalDescription,
            txtHerbalProcedure, txtHerbalDays, txtDaysProcedure;
    String titleName;

    UserDiseaseDbHelper userDiseaseDbHelper;
    final static int RQS_1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        titleName = bundle.getString("title");

        userDiseaseDbHelper = new UserDiseaseDbHelper(this);

        txtDiseaseDescription = (TextView) findViewById(R.id.txtDiseaseDescription);
        txtHerbalProcedure = (TextView) findViewById(R.id.txtHerbalProcedure);
        txtHerbalDays = (TextView) findViewById(R.id.txtHerbalDays);
        txtHerbalName = (TextView) findViewById(R.id.txtHerbalName);
        txtHerbalSciName = (TextView) findViewById(R.id.txtHerbalSciName);
        txtHerbalDescription = (TextView) findViewById(R.id.txtHerbalDescription);
        txtDaysProcedure = (TextView) findViewById(R.id.txtDaysProcedure);



        diseasesNameProcedure = (TextView) findViewById(R.id.diseasesNameProcedure);
        diseasesNameProcedure.setText(titleName);

        txtDiseaseName = (TextView) findViewById(R.id.txtDiseaseName);
        txtDiseaseName.setText(titleName);
        herbalDbHelper = new HerbalDbHelper(this);
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

            SQLiteDatabase myHerbalDbHelper = herbalDbHelper.getReadableDatabase();
            Cursor cursor = myHerbalDbHelper.query("tbl_Herbal" ,null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("diseaseName")).toLowerCase().equals(titleName.toLowerCase())) {
                    txtDiseaseDescription.setText(cursor.getString(1).toString()+System.getProperty("line.separator"));
                    txtHerbalName.setText(cursor.getString(2).toString());
                    txtHerbalSciName.setText(cursor.getString(3).toString());
                    txtHerbalDescription.setText(cursor.getString(4).toString()+System.getProperty("line.separator"));
                    txtHerbalProcedure.setText(cursor.getString(5).toString()+System.getProperty("line.separator"));
                    txtHerbalDays.setText("Do this within "+cursor.getString(6).toString()+"-8 Days");
                    txtDaysProcedure.setText(cursor.getString(6).toString());
                }
            }
    }

    public void btnConfirmDisease(View v) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialogYes();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("yes", dialogClickListener).setNegativeButton("no", dialogClickListener).show();
    }

    public  void dialogYes(){
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
                //===================================================================================
                String time;
                /*Date*/ Calendar currentTime = Calendar.getInstance()/*.getTime()*/;
                SimpleDateFormat df = new SimpleDateFormat("dd");
                String formattedDate = df.format(currentTime.getTime());
                time = formattedDate.toString();
                double d = Double.valueOf(String.valueOf(time));
                int dd = (int) d;
                time = String.valueOf( (int) d);
                int hday = 0;
                hday = Integer.parseInt(txtDaysProcedure.getText().toString());

                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(calNow.get(Calendar.YEAR), calNow.get(Calendar.MONTH), (calNow.get(Calendar.DATE)+hday), 8, 00, 00000);

                if(calSet.compareTo(calNow) <= 0){
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }
                setAlarm(calSet);

                diseaseName = txtDiseaseName.getText().toString();
                diseaseDescription = txtDiseaseDescription.getText().toString();
                herbalName = txtHerbalName.getText().toString();
                herbalDescription = txtHerbalDescription.getText().toString();
                herbalProcedure = txtHerbalProcedure.getText().toString();
//
                double days = Double.valueOf(String.valueOf(txtDaysProcedure.getText()));
                double daysTo = Double.valueOf(String.valueOf(time));
                double total = days + daysTo;
//
                herbalDays = String.valueOf( (int) total);
////                speakOutNow("do the procedure, for "+txtDaysProcedure.getText()+" days");
        userDiseaseDbHelper.insertData(diseaseName, diseaseDescription, herbalName, herbalDescription, herbalProcedure, herbalDays);
            }
        }else{
            Toast.makeText(this, "max data", Toast.LENGTH_SHORT).show();
        }

    }
    public  void dialogNo(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


    private void setAlarm(Calendar targetCal){
//        txtHerbalSciName.setText(
//                "\n\n***\n"
//                        + "Alarm is set@ " + targetCal.getTime() + "\n"
//                        + "***\n");

        Intent intent = new Intent(getBaseContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0 /*MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT*/);
        intent.putExtra("extra", "alarm on");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }



    public void toastThis(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
