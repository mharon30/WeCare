package com.example.thea.wecare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BookmarkItem extends AppCompatActivity {


    //Declarations
//    Button btnDeleteItem;
    TextView txtTitleBookmark, txtDescriptionBookmark, txtCheck;
    String itemTitle, type;
    BookmarkDatabaseHelper bookmarkDatabaseHelper;
    HerbalDbHelper herbalDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bookmark_item);

        txtCheck = (TextView) findViewById(R.id.txtCheck);
        herbalDbHelper = new HerbalDbHelper(this);
        bookmarkDatabaseHelper = new BookmarkDatabaseHelper(this);
        txtTitleBookmark = (TextView) findViewById(R.id.txtTitleBookmark);
        txtDescriptionBookmark = (TextView) findViewById(R.id.txtDescriptionBookmark);

        //******************************************************************************get the data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        itemTitle = bundle.getString("title");
        type = bundle.getString("type");

        //******************************************************************************display data
        txtTitleBookmark.setText(itemTitle);
//        txtDescriptionBookmark.setText(itemDescription);
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
        if (type.toString().toLowerCase().equals("herbal")) {
            while (cursor.moveToNext()) {
                txtCheck.setText(cursor.getString(cursor.getColumnIndex("herbalName")));
                if (txtCheck.getText().toString().toUpperCase().equals(txtTitleBookmark.getText().toString().toUpperCase())) {
                    txtDescriptionBookmark.setText(cursor.getString(cursor.getColumnIndex("herbalDescription")));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                txtCheck.setText(cursor.getString(cursor.getColumnIndex("diseaseName")));
                if (txtCheck.getText().toString().toUpperCase().equals(txtTitleBookmark.getText().toString().toUpperCase())) {
                    txtDescriptionBookmark.setText(cursor.getString(cursor.getColumnIndex("diseaseDescription")));
                }
            }
        }
    }

    //  ##########################################################***************************************View delete
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }
    //  ##########################################################***************************************Delete Item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_delete) {
            String name = txtTitleBookmark.getText().toString();
            int result = bookmarkDatabaseHelper.deleteData(name);
            Toast.makeText(this, name + " deleted",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, BookMark.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    ###########################################################*****************************************End
}
