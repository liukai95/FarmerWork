package com.example.work.house;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.MainActivity;
import com.example.work.MyDatabaseHelper;
import com.example.work.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HouseActivity extends AppCompatActivity {
    private static List<House> houseList = new ArrayList();
    private static MyDatabaseHelper databaseHelper;
    private static HouseAdapter houseAdapter;

    public HouseAdapter getHouseAdapter() {
        return houseAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        databaseHelper = new MyDatabaseHelper(this, "dishesManager.db", null, 1);//得到数据库
        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.house_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        houseAdapter = new HouseAdapter(houseList, databaseHelper, this);
        recyclerView.setAdapter(houseAdapter);

    }

    //上市对话框
    public static void myLDialog(Context context, final House house) {
        // 设置对话框
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.input_number, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // 设置
        alertDialogBuilder.setView(promptsView);
        final EditText s = (EditText) promptsView.findViewById(R.id.yield_edit);
        TextView textView = (TextView) promptsView.findViewById(R.id.input_text);
        textView.setText("输入上市的供量(单位公斤) :");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int supplyNumber = house.getYield() / 2;//默认上市一半
                                // 得到数据
                                if (!"".equals(s.getText().toString())  && s.getText().toString()!= null && Integer.parseInt(s.getText().toString()) <= house.getYield()
                                        && Integer.parseInt(s.getText().toString()) > 0) {
                                    supplyNumber = Integer.parseInt(s.getText().toString());
                                }
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                String str = sf.format(curDate);
                                values.put("supply", supplyNumber);
                                values.put("yield", (house.getYield() - supplyNumber));
                                values.put("listedTime", str);
                                db.update("House", values, "id=?", new String[]{String.valueOf(house.getId())});
                                init();
                                houseAdapter.notifyDataSetChanged();

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //下架对话框
    public static void myOffDialog(Context context, final House house) {
        // 设置对话框
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.input_number, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // 设置
        alertDialogBuilder.setView(promptsView);
        final EditText s = (EditText) promptsView.findViewById(R.id.yield_edit);
        TextView textView = (TextView) promptsView.findViewById(R.id.input_text);
        textView.setText("输入该次上市后的剩余量(单位公斤) :");
        // 设置对话框
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int yNumber = 0;
                                // 得到数据
                                if (!"".equals(s.getText().toString()) && s.getText().toString()!= null && Integer.parseInt(s.getText().toString()) <= house.getSupply()
                                        && Integer.parseInt(s.getText().toString()) >= 0) {
                                    yNumber = Integer.parseInt(s.getText().toString());
                                }

                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                if (house.getYield() + yNumber == 0) {//没有剩余量，从数据库中删除
                                    db.delete("House", "id=?", new String[]{String.valueOf(house.getId())});
                                } else {
                                    ContentValues values = new ContentValues();
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                    String str = sf.format(curDate);
                                    values.put("supply", 0);
                                    values.put("yield", (house.getYield() + yNumber));
                                    values.put("offTime", str);
                                    db.update("House", values, "id=?", new String[]{String.valueOf(house.getId())});
                                    init();
                                    houseAdapter.notifyDataSetChanged();
                                }

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void reset(Context context, House house) {
        if(!"2017-01-01".equals(house.getOffTime()) && !"2017-01-01".equals(house.getListedTime())){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("offTime", "2017-01-01");
            values.put("listedTime", "2017-01-01");
            db.update("House", values, "id=?", new String[]{String.valueOf(house.getId())});
            init();
            houseAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(context, "上市,然后下架后才能重置", Toast.LENGTH_SHORT).show();
        }
    }
    private static void init() {
        houseList.clear();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // 查询Dishes表中所有的数据
        Cursor cursor = db.query("House", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String fieldName = cursor.getString(cursor.getColumnIndex("fieldName"));//地区的名字
                String crop = cursor.getString(cursor.getColumnIndex("crop"));//农作物
                int supply = cursor.getInt(cursor.getColumnIndex("supply"));//农作物的供货量
                int yield = cursor.getInt(cursor.getColumnIndex("yield"));//农作物的剩余量
                String pickTime = cursor.getString(cursor.getColumnIndex("pickTime"));//采摘时间
                String listedTime = cursor.getString(cursor.getColumnIndex("listedTime"));//上市时间
                String offTime = cursor.getString(cursor.getColumnIndex("offTime"));//下架时间
                if (yield > 0 || listedTime.equals(offTime)) {//存在剩余量或者还没有下架
                    House h = new House(id, fieldName, crop, supply, yield, pickTime, listedTime, offTime,R.drawable.reset);
                    houseList.add(h);
                }

            } while (cursor.moveToNext());
        }
        //Toast.makeText(this,houseList.size()+"",Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
