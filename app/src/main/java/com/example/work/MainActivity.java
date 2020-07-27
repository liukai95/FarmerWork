package com.example.work;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.work.house.HouseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Dishes> dishesList = new ArrayList();
    private MyDatabaseHelper databaseHelper;

    private static final String[] m_Qu = {"A 棚区 5亩", "B 田区 5亩", "C 地区 2亩", "D 坡区 3亩", "E 果树 2亩", "F 水池 1亩", "G 池周 1亩"};   //定义地区数组
    private static final String[] m_Bia = {"A", "B", "C", "D"};   //定义地标数组
    private static final String[] m_Kua = {"1", "2", "3", "4"};   //定义地块数组
    private static final String[] m_Wei = {"前", "后"};   //定义地位数组
    private ArrayAdapter<String> adapter;     //存放数据
    private DishesAdapter dishesadapter;
    private Spinner spinnerQu;
    private Spinner spinnerBia;
    private Spinner spinnerKua;
    private Spinner spinnerWei;
    private String strQu = m_Qu[0];//选择的地区
    private String strBia = m_Bia[0];//选择的地标
    private String strKua = m_Kua[0];//选择的地块
    private String strWei = m_Wei[0];//选择的地位

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
                if(dishesList.size()!=0){
                    Toast.makeText(this,"存在田地无法重写设置",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"在此处重新设置地区、地标、地块、地位",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.warehouse_item:
                //Toast.makeText(this,"仓库",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this, HouseActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connector.getDatabase();
        databaseHelper = new MyDatabaseHelper(this, "dishesManager.db", null, 1);//得到数据库
        databaseHelper.getWritableDatabase();
        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dishesadapter = new DishesAdapter(dishesList,databaseHelper,this);
        recyclerView.setAdapter(dishesadapter);

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, m_Qu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉列表的风格
        spinnerQu = (Spinner) findViewById(R.id.spinner_qu);
        spinnerQu.setAdapter(adapter);
        //添加SpinnerQu事件监听
        spinnerQu.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
               // Toast.makeText(MainActivity.this, m_Qu[arg2], Toast.LENGTH_SHORT).show();
                strQu = m_Qu[arg2];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, m_Bia);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉列表的风格
        spinnerBia = (Spinner) findViewById(R.id.spinner_bia);
        spinnerBia.setAdapter(adapter);
        //添加SpinnerBia事件监听
        spinnerBia.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                //Toast.makeText(MainActivity.this, m_Bia[arg2], Toast.LENGTH_SHORT).show();
                strBia = m_Bia[arg2];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, m_Kua);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉列表的风格
        spinnerKua = (Spinner) findViewById(R.id.spinner_kua);
        spinnerKua.setAdapter(adapter);
        //添加SpinnerKua事件监听
        spinnerKua.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                //Toast.makeText(MainActivity.this, m_Kua[arg2], Toast.LENGTH_SHORT).show();
                strKua = m_Kua[arg2];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, m_Wei);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉列表的风格
        spinnerWei = (Spinner) findViewById(R.id.spinner_wei);
        spinnerWei.setAdapter(adapter);
        //添加SpinnerWei事件监听
        spinnerWei.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                //Toast.makeText(MainActivity.this, m_Wei[arg2], Toast.LENGTH_SHORT).show();
                strWei = m_Wei[arg2];
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void init() {
//        Dishes d = new Dishes("A 棚区 5亩,A,1,前", "小白菜");
//        for (int i = 0; i < 10; i++) {
//            dishesList.add(d);
//        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // 查询Dishes表中所有的数据
        Cursor cursor = db.query("Dishes", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String crop = cursor.getString(cursor.getColumnIndex("crop"));
                String state = cursor.getString(cursor.getColumnIndex("state"));
                double area = cursor.getDouble(cursor.getColumnIndex("area"));
                int imageId=cursor.getInt(cursor.getColumnIndex("image"));
                Dishes d = new Dishes(name,crop,state,area,imageId,R.drawable.ic_menu_delete);
                dishesList.add(d);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    //activity_mian.xml 对应增加的点击事件触发的方法
    public void add(View v) {
        String name = strQu +","+ strBia +"," + strKua +"," + strWei;
        String crop ="无农作物";
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // 判断该地是否已经存在
       for(Dishes dishes:dishesList){
           if(dishes.getName().equals(name)){
               Toast.makeText(MainActivity.this, "已经存在，请重新选择", Toast.LENGTH_SHORT).show();
               return;
           }
       }
        ContentValues values = new ContentValues();
        // 开始组装数据
        values.put("name", name);
        values.put("crop",crop);
        values.put("state", "无");
        values.put("area",0);
        values.put("image",0);
        values.put("matureTime",0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sf.format(curDate);
        values.put("plantTime",str);
        db.insert("Dishes", null, values); // 插入一条数据
        values.clear();
        //插入集合
        Dishes d = new Dishes(name,crop,"无",0,0,R.drawable.ic_menu_delete);
        dishesList.add(d);
        dishesadapter.notifyDataSetChanged();//刷新界面
    }

}
