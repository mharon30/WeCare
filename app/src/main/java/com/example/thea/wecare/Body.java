package com.example.thea.wecare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Body extends AppCompatActivity {
    ListView listView;
    TextView txt10;
    String title;
    String [] item = {"Head", "Neck", "Shoulder", "Arm", "Hand", "Body", "Leg", "Foot", "Back"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_body);

        txt10 = (TextView) findViewById(R.id.textView10);

        //display item on the listview
        listView = (ListView) findViewById(R.id.idlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");

        txt10.setText(title.toString());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open the new activity
                Intent intent = new Intent(getApplicationContext(), BodyProcedure.class);
                startActivity(intent);
            }
        });
    }
}
