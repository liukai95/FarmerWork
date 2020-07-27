package com.example.work;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DISH = "create table Dishes ("
            + "name text primary key, "//地的名字，用作主键
            + "crop text, "//种植的农作物
            + "area real, "//种植的面积，单位亩，浮点型
            + "image integer, "//农作物图片，整形
            + "matureTime integer, "//农作物预计成熟时间，单位天，整形
            + "plantTime date, "//农作物种植时间，格式为是‘YYYY-MM-DD’
            + "state text)";//地的状态

    public static final String CREATE_HOUSE = "create table House ("
            + "id integer primary key autoincrement, "//使用自动增长的id作为主键
            + "fieldName text, "//田地的名字
            + "crop text, "//农作物
            + "supply integer, "//农作物的供货量，单位公斤，整形
            + "yield integer, "//农作物的剩余量，单位公斤，整形
            + "plantTime date, "//农作物种植时间，格式为是‘YYYY-MM-DD’
            + "pickTime date, "//农作物采摘时间，格式为是‘YYYY-MM-DD’
            + "listedTime date, "//农作物上市时间，格式为是‘YYYY-MM-DD’
            + "offTime date)";//农作物下架时间，格式为是‘YYYY-MM-DD’


    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_HOUSE);
        db.execSQL(CREATE_DISH);

        Toast.makeText(mContext, "数据库建立成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果存在则删除重新建立
       //db.execSQL("drop table if exists House");
       //db.execSQL("drop table if exists House");
        onCreate(db);
    }

}
