package com.example.work.house;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.MainActivity;
import com.example.work.MyDatabaseHelper;
import com.example.work.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 刘开 on 2017/6/1.
 */

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {
    private List<House> mHouseList;
    private MyDatabaseHelper databaseHelper;
    private Context context;
    private int supplyNumber;//上市的供量，默认为一半
    private int yNumber = 0;//上市后的剩余，默认为0

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView crop;//农作物
        private TextView supply;//农作物的供货量
        private TextView yield;//农作物的剩余量
        private TextView listedTime;//上市时间
        private TextView offTime;//下架时间
        ImageView houseId;
        View houseView;

        public ViewHolder(View view) {
            super(view);
            houseView = view;
            id = (TextView) view.findViewById(R.id.house_id);
            crop = (TextView) view.findViewById(R.id.house_crop);
            supply = (TextView) view.findViewById(R.id.house_supply);
            yield = (TextView) view.findViewById(R.id.house_yield);
            houseId = (ImageView) view.findViewById(R.id.resetIV);
            listedTime = (TextView) view.findViewById(R.id.house_listed);
            offTime = (TextView) view.findViewById(R.id.house_off);
        }
    }

    public HouseAdapter(List<House> mHouseList, MyDatabaseHelper databaseHelper, Context context) {
        this.mHouseList = mHouseList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.houseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                House house = mHouseList.get(position);
                Toast.makeText(view.getContext(), "所在田地"+house.getFieldName() + "，农作物名字" + house.getCrop() + "采摘时间" + house.getPickTime(), Toast.LENGTH_SHORT).show();
            }
        });

        //点击上市
        holder.listedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final House house = mHouseList.get(position);
                if (!"2017-01-01".equals(house.getListedTime())) {
                    Toast.makeText(context, "已经上市，下架后才能再一次上市", Toast.LENGTH_SHORT).show();
                } else {
                    HouseActivity.myLDialog(context,house);
                }

            }
        });
        //点击下架
        holder.offTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                final House house = mHouseList.get(position);
                if (!"2017-01-01".equals(house.getOffTime())) {
                    Log.d("MM",house.getOffTime());
                    Toast.makeText(context, "已经下架，上市后才能再一次下架", Toast.LENGTH_SHORT).show();
                } else {
                    HouseActivity.myOffDialog(context,house);
                }

            }
        });
        //点击重置
        holder.houseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                House house = mHouseList.get(position);
                HouseActivity.reset(context,house);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        House house = mHouseList.get(position);
        holder.id.setText(String.valueOf(house.getId()));
        holder.crop.setText(house.getCrop());
        holder.houseId.setImageResource(house.getImageId());//得到重置图标
        holder.yield.setText(String.valueOf(house.getYield()));

        if ("2017-01-01".equals(house.getListedTime()) && "2017-01-01".equals(house.getOffTime())) {//未供货
            holder.supply.setText("无");
            holder.listedTime.setText("无");
            holder.offTime.setText("无");
        } else if (!"2017-01-01".equals(house.getListedTime()) &&"2017-01-01".equals(house.getOffTime())) {//未下架
            holder.supply.setText(String.valueOf(house.getSupply()));
            holder.listedTime.setText(house.getListedTime());
            holder.offTime.setText("无");
        } else {
            holder.supply.setText(String.valueOf(house.getSupply()));
            holder.listedTime.setText(house.getListedTime());
            holder.offTime.setText(house.getOffTime());
        }


    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }
}
