package com.example.thea.wecare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class AboutUs extends AppCompatActivity {
    ImageView imgViewAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_about_us);
        imgViewAbout = (ImageView) findViewById(R.id.imgViewAbout);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.about_icon);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        imgViewAbout.setImageDrawable(roundedBitmapDrawable);
        roundedBitmapDrawable.setCircular(true);
    }

    public void onBackPressed(){
        finish();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
