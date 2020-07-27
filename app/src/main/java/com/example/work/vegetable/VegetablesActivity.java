package com.example.work.vegetable;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.work.Dishes;
import com.example.work.R;

import java.util.ArrayList;
import java.util.List;

public class VegetablesActivity extends AppCompatActivity {
    private List<Vegetables> vegetablesList = new ArrayList();
    private VegetablesAdapter vegetablesAdapter;
    private Dishes dishes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetables);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);//设置为两列
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();//得到数据
        dishes = (Dishes) intent.getSerializableExtra("dishes");
        vegetablesAdapter = new VegetablesAdapter(vegetablesList, dishes.getName());
        recyclerView.setAdapter(vegetablesAdapter);
        Button backButton=(Button)findViewById(R.id.title_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button exitButton=(Button)findViewById(R.id.title_edit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VegetablesActivity.this,"在此处编辑蔬菜的种类，新增(需要选择图片)或者删除蔬菜",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        Vegetables yumiao = new Vegetables("育苗", R.drawable.yumiao);
        vegetablesList.add(yumiao);
        Vegetables xiaobaicai = new Vegetables("小白菜", R.drawable.xiaobaicai);
        vegetablesList.add(xiaobaicai);
        Vegetables hongxiancai = new Vegetables("红苋菜", R.drawable.hongxiancai);
        vegetablesList.add(hongxiancai);
        Vegetables niuxinbaocai = new Vegetables("牛心包菜", R.drawable.niuxinbaocai);
        vegetablesList.add(niuxinbaocai);
        Vegetables fanqie = new Vegetables("番茄", R.drawable.fanqie);
        vegetablesList.add(fanqie);
        Vegetables lajiao = new Vegetables("辣椒", R.drawable.lajiao);
        vegetablesList.add(lajiao);
        Vegetables hongshujian = new Vegetables("红薯尖", R.drawable.hongshujian);
        vegetablesList.add(hongshujian);
        Vegetables shengcai = new Vegetables("生菜", R.drawable.shengcai);
        vegetablesList.add(shengcai);
        Vegetables tonghao = new Vegetables("茼蒿", R.drawable.tonghao);
        vegetablesList.add(tonghao);
        Vegetables sijidou = new Vegetables("四季豆", R.drawable.sijidou);
        vegetablesList.add(sijidou);
        Vegetables maodou = new Vegetables("毛豆", R.drawable.maodou);
        vegetablesList.add(maodou);
        Vegetables tianyumi = new Vegetables("甜玉米", R.drawable.tianyumi);
        vegetablesList.add(tianyumi);
        Vegetables xiangcai = new Vegetables("香菜", R.drawable.xiangcai);
        vegetablesList.add(xiangcai);

    }
}
