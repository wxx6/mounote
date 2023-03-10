package com.mou;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements DialogCallBack {

    private static final String TAG = "wxx";
    private View ib_home;
    private View ib_add;
    private View ib_setting;
    private ViewPager2 viewpager;
    private SQLiteDatabase basic_database;
    private Cursor cursor;
    private final List<String> mTableList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        databaseInit();
        fragmentInit();
        viewpagerInit();
        pageInit();
        //Log.d("wxx", "onCreate: ");

    }

    private void pageInit() {
        //set image_button on_click_listener
        ib_home = findViewById(R.id.ib_home);
        ib_add = findViewById(R.id.ib_add);
        ib_setting = findViewById(R.id.ib_setting);
        ib_home.setActivated(true);
        ib_home.setOnClickListener(V -> {
            viewpager.setCurrentItem(0);
            ib_home.setActivated(true);
            ib_add.setActivated(false);
            ib_setting.setActivated(false);
        });
        ib_add.setOnClickListener(v -> {
            viewpager.setCurrentItem(1);
            ib_home.setActivated(false);
            ib_add.setActivated(true);
            ib_setting.setActivated(false);
        });
        ib_setting.setOnClickListener(v -> {
            viewpager.setCurrentItem(2);
            ib_home.setActivated(false);
            ib_add.setActivated(false);
            ib_setting.setActivated(true);
        });

        //set viewpager on_change_listener
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position) {
                    case 0:
                        ib_home.setActivated(true);
                        ib_add.setActivated(false);
                        ib_setting.setActivated(false);
                        break;
                    case 1:
                        ib_home.setActivated(false);
                        ib_add.setActivated(true);
                        ib_setting.setActivated(false);
                        break;
                    case 2:
                        ib_home.setActivated(false);
                        ib_add.setActivated(false);
                        ib_setting.setActivated(true);
                        break;
                    default:
                        Toast.makeText(MainActivity2.this, "123", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void viewpagerInit() {
        viewpager = findViewById(R.id.viewpager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("123", "456"));
        mTableList.add("??????");
        fragments.add(AddFragment.newInstance(mTableList));
        fragments.add(SettingFragment.newInstance("123", "456"));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewpager.setAdapter(viewPagerAdapter);
    }

    private void fragmentInit() {
        basic_database = openOrCreateDatabase("basic_database.db", MODE_PRIVATE, null);
        cursor = basic_database.query("user_basic_tb", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getColumnIndex("folder_name");
                if (index != -1) {
                    String string = cursor.getString(index);
                    mTableList.add(string);
                }
            } while (cursor.moveToNext());
        } else Log.d("wxx", "fragmentInit: cursor??????");
        //cursor.close();
        //basic_database.close();
    }

    private void databaseInit() {
        //create a basic database if not exist
        basic_database = openOrCreateDatabase("basic_database.db", MODE_PRIVATE, null);
        //create a table to store all the folder
        basic_database.execSQL("create table if not exists user_basic_tb("
                + "_id integer primary key,"
                + "table_name varchar(100),"
                + "folder_name varchar(100))");
        //if(basic_database!=null) basic_database.close();
        cursor = basic_database.query("user_basic_tb", null, null, null, null, null, null);
        //??????user_basic_tb?????????????????????default_tb,??????user_basic_tb?????????????????????
        if (!cursor.moveToFirst()) {
            //?????????????????????????????????default_tb
            basic_database.execSQL("create table if not exists default_tb("
                    + "_id integer primary key,"
                    + "title varchar(100),"
                    + "component varchar(1024),"
                    + "date date)");
            ContentValues contentValues = new ContentValues();
            contentValues.put("table_name", "default_tb");
            contentValues.put("folder_name", "??????");
            basic_database.insert("user_basic_tb", null, contentValues);
        }
        //cursor.close();
        //basic_database.close();
    }

    @Override
    public void onDialogCallBack(String text) {
        Log.d(TAG, "onDialogCallBack: 123" + text);
        //???????????????default_tb+id,?????????user_basic_tb????????????????????????
        cursor.moveToFirst();
        while(cursor.moveToNext()){}
        int index=cursor.getPosition();
        basic_database.execSQL("create table if not exists default_tb"+index+"(" +
                "_id integer primary key," +
                "title varchar(100)," +
                "component varchar(1024)," +
                "date date)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("table_name", "default_tb"+index);
        contentValues.put("folder_name", text);
        basic_database.insert("user_basic_tb",null,contentValues);

    }

}
