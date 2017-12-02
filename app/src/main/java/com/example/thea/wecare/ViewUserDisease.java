package com.example.thea.wecare;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewUserDisease extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    String itemTitle;
    UserDiseaseDbHelper userDiseaseDbHelper;
    TextView txtDiseaseTitle, txtDiseaseInfo, txtDays, txtNameChecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_view_user_disease);

        userDiseaseDbHelper = new UserDiseaseDbHelper(this);
        tts = new TextToSpeech(this, this);

        //******************************************************************************get the data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        itemTitle = bundle.getString("title");

        //******************************************************************************display data
        txtDiseaseTitle = (TextView) findViewById(R.id.txtDiseaseTitle);
        txtDiseaseTitle.setText(itemTitle);
        txtDiseaseInfo = (TextView) findViewById(R.id.txtDiseaseInfo);
        txtNameChecker = (TextView) findViewById(R.id.txtNameChecker);
        txtDays = (TextView) findViewById(R.id.txtDays);
//        fetchData();
    }

    public void fetchData() {
        userDiseaseDbHelper = new UserDiseaseDbHelper(this);

        SQLiteDatabase sd = userDiseaseDbHelper.getReadableDatabase();
        Cursor cursor = sd.query("UserDisease_table" ,null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            txtNameChecker.setText(cursor.getString(cursor.getColumnIndex("DISEASENAME")));
            if (txtNameChecker.getText().toString().toUpperCase().equals(txtDiseaseTitle.getText().toString().toUpperCase())) {
                txtDiseaseInfo.setText(cursor.getString(cursor.getColumnIndex("DISEASEDESCRIPTION")));
                txtDays.setText(cursor.getString(cursor.getColumnIndex("HERBALDAYS")));

                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd");
                String formattedDate = df.format(currentTime.getTime());
                if (formattedDate.equals(txtDays.getText())){
                    speakOutNow("welcome back. do you feel better now?");

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    speakOutNow("very well. just keep your skin and body clean.");
                                    String name = txtDiseaseTitle.getText().toString();
                                    int result = userDiseaseDbHelper.deleteData(name);
                                    //remove to database
                                    String diseaseName = txtDiseaseTitle.getText().toString();
                                    userDiseaseDbHelper.deleteData(diseaseName);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    speakOutNow("did you follow the instructions that i have given?");
                                    dialogYes();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Click Yes/No for your answer").setPositiveButton("yes", dialogClickListener).setNegativeButton("no", dialogClickListener).show();

                }else{
                    speakOutNow("welcome back. you have come so early. are you feeling alright?");

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    speakOutNow("that's good, just continue the medication until the given time.");
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    speakOutNow("did you follow the instructions?");
                                    dialogYes();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Click Yes/No for your answer").setPositiveButton("yes", dialogClickListener).setNegativeButton("no", dialogClickListener).show();

                }

            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){

            int language = tts.setLanguage(Locale.ENGLISH);
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                fetchData();
            }
            else{

            }

        }
        else{

        }
    }

    private void speakOutNow(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }



    public  void dialogYes(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    speakOutNow("you need to stop the medication and consult to experts, herbals can't handle your skin problem.");
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    speakOutNow("i suggest you to do the right procedure until the given time, please get back to me after your medication.");

                    break;
            }
        }
    };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Click Yes/No for your answer").setPositiveButton("yes", dialogClickListener).setNegativeButton("no", dialogClickListener).show();

    }

    public  void dialogNo(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

}
