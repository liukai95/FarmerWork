package com.example.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.house.HouseActivity;
import com.example.work.vegetable.VegetablesActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity {
    private MyDatabaseHelper databaseHelper;
    private String plantTime = "2017-01-01";//种植时间
    private String state = "无";
    private Dishes dishes;
    private long test = 0;//用于测试，进度条一秒增加
    private int yieldNumber = 1000;//产量
    private int matureTime = 100;//预计成熟时间
    String crop = "无农作物";
    //使用Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);//得到MenuInflater对象，调用inflate()方法在活动中创建菜单
        return true;
    }

    //菜单响应事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setUp_item:
                Toast.makeText(this,"在该次重新设置农作物的剩余成熟时间",Toast.LENGTH_SHORT).show();
                break;
            case R.id.warehouse_item:
                //Toast.makeText(this,"仓库",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ScheduleActivity.this, HouseActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        databaseHelper = new MyDatabaseHelper(this, "dishesManager.db", null, 1);
        //得到数据
        final Intent intent = getIntent();
        dishes = (Dishes) intent.getSerializableExtra("dishes");
        init();
    }

    //初始化界面
    public void init() {
        String name = dishes.getName();
        // Toast.makeText(ScheduleActivity.this, name, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Dishes where name=?", new String[]{name});
        double area = 0;
        int imageId = 0;

        if (cursor.moveToFirst()) {
            do {
                area = cursor.getDouble(cursor.getColumnIndex("area"));
                state = cursor.getString(cursor.getColumnIndex("state"));
                crop = cursor.getString(cursor.getColumnIndex("crop"));
                matureTime= cursor.getInt(cursor.getColumnIndex("matureTime"));
                plantTime = cursor.getString(cursor.getColumnIndex("plantTime"));
                imageId = cursor.getInt(cursor.getColumnIndex("image"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        TextView areaView = (TextView) findViewById(R.id.area_view);

        ImageView cropView = (ImageView) findViewById(R.id.crop_image);//农作物图片
        TextView cropText = (TextView) findViewById(R.id.crop_text);//农作物名字
        TextView cropTime = (TextView) findViewById(R.id.time_view);//农作物种植时间
        TextView mTime = (TextView) findViewById(R.id.mature_view);//农作物成熟时间
        if (state.equals("无")) {//没有农作物
            areaView.setText("无");
            cropView.setImageResource(R.drawable.field);
            cropTime.setText("没有种植");
            mTime.setText("无");

            test = new Date(System.currentTimeMillis()).getTime();//初始化
        } else {//显示该农作物的照片
            areaView.setText(area + "亩");
            mTime.setText(matureTime+"天");
            cropView.setImageResource(imageId);
            cropText.setText(crop);
            cropTime.setText(plantTime);
        }
        final String s = state;//重新得到一个新的变量，为了在OnClick中使用
        //得到种植的按钮
        Button plantButton = (Button) findViewById(R.id.plant_button);
        plantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!s.equals("无")) {
                    Toast.makeText(ScheduleActivity.this, "已有农作物，采摘后才能种植", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(ScheduleActivity.this, VegetablesActivity.class);
                    i.putExtra("dishes", dishes);//向下个活动传输数据
                    startActivity(i);
                }

            }
        });
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
        progressBar.setMax(matureTime);//设置进度条的长度为预计成熟的时间
        int progress = progressBar.getProgress();
        //计算进度条，因为进度较慢因此无需不断刷新，退出重新进入刷新
        if (!state.equals("无")) {//有农作物
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");
            String str = sf.format(System.currentTimeMillis());
            Date plantDate = null;
            Date curDate = null;
            try {
                plantDate = sf.parse(plantTime);//种植时间
                curDate = sf.parse(str);//当前时间
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //long diff = curDate.getTime() - plantDate.getTime();//差值单位为ms，解除注释
            long diff = new Date(System.currentTimeMillis()).getTime() - test;//，注释掉
            //操作进度条
            if (progress < matureTime) {
                //progress = progress + (int) (diff / (1000*60*24));//一天增加一点，解除注释
                progress = progress + (int) (diff / (1000));//一秒增加一点，用于测试，返回上个界面回来便成熟了，注释掉

                progressBar.setProgress(progress);
            } else {//设置状态为成熟
                ContentValues values = new ContentValues();
                values.put("state", "成熟");
                db.update("Dishes", values, "name=?", new String[]{dishes.getName()});
            }
        }
        final int len = progress;

        //得到采摘的按钮
        Button pickButton = (Button) findViewById(R.id.pick_button);
        pickButton.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;
            @Override
            public void onClick(View v) {
                if (s.equals("无")) {
                    Toast.makeText(ScheduleActivity.this, "无农作物，无法采摘", Toast.LENGTH_SHORT).show();
                } else if (len < matureTime) {
                    Toast.makeText(ScheduleActivity.this, "农作物还未成熟，无法采摘", Toast.LENGTH_SHORT).show();
                }  else if (!flag) {
                    Toast.makeText(ScheduleActivity.this, "已经采摘过了，无法采摘", Toast.LENGTH_SHORT).show();
                } else {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.input_number, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    // 设置
                    alertDialogBuilder.setView(promptsView);
                    final EditText yield = (EditText) promptsView.findViewById(R.id.yield_edit);
                    TextView textView=(TextView)promptsView.findViewById(R.id.input_text);
                    textView.setText("输入农作物的产量(单位公斤) :");
                    // 设置对话框
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // 得到数据
                                            if (!"".equals(yield.getText().toString()) && yield.getText().toString() != null) {
                                                yieldNumber = Integer.parseInt(yield.getText().toString());
                                            }
                                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            //重置田地
                                            values.put("crop","无农作物");
                                            values.put("state", "无");
                                            values.put("area",0);
                                            values.put("image",0);
                                            values.put("matureTime",0);
                                            db.update("Dishes", values, "name=?", new String[]{dishes.getName()});

                                            ContentValues v = new ContentValues();
                                            // 开始组装数据，插入到仓库表
                                            v.put("fieldName", dishes.getName());
                                            v.put("crop",crop);
                                            v.put("yield", yieldNumber);
                                            v.put("supply", 0);
                                            v.put("plantTime",plantTime);
                                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                            String str = sf.format(curDate);
                                            v.put("pickTime",str);
                                            v.put("listedTime","2017-01-01");//默认
                                            v.put("offTime","2017-01-01");
                                            db.insert("House", null, v); // 插入一条数据
                                            flag = false;
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

            }
        });


    }

    //从另一个活动回来，重新设置，即进行刷新
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //静态方法，完成Intent的构建,传数据Dishes
    public static void actionStart(Context context, Dishes dishes) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        intent.putExtra("dishes", dishes);
        context.startActivity(intent);

    }
}
