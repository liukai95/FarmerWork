package com.example.work;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 刘开 on 2017/6/1.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.ViewHolder> {
    private List<Dishes> mDishesList;
    private MyDatabaseHelper databaseHelper;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View dishesView;
        TextView dishesName;//名字
        TextView dishesCrop;//农作物
        TextView dishesState;//状态
        ImageView dishesId;//删除标志
        public ViewHolder(View view) {
            super(view);
            dishesView = view;
            dishesName = (TextView) view.findViewById(R.id.dishes_name);
            dishesCrop = (TextView) view.findViewById(R.id.dishes_crop);
            dishesState = (TextView) view.findViewById(R.id.dishes_state);
            dishesId=(ImageView)view.findViewById(R.id.deleteIV);
        }
    }

    public DishesAdapter(List<Dishes> mDishesList, MyDatabaseHelper databaseHelper,Context context) {
        this.mDishesList = mDishesList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dishes_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //点击田地的状态以及种植物均进入下一个活动
        holder.dishesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                Dishes dishes = mDishesList.get(position);
//                Intent intent =new Intent(view.getContext(),ScheduleActivity.class);
//                view.getContext().startActivity(intent);
                ScheduleActivity.actionStart(view.getContext(),dishes);
                ((Activity)context).finish();
            }
        });

        //点击田地名字仅仅显示
        holder.dishesName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Dishes dishes = mDishesList.get(position);
                Toast.makeText(view.getContext(), "田地的名字为" + dishes.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.dishesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final Dishes dishes = mDishesList.get(position);
                //Toast.makeText(view.getContext(), "You clicked Image" + dishes.getState(), Toast.LENGTH_SHORT).show();
                if(!dishes.getState().equals("无")){
                    Toast.makeText(view.getContext(), "有农作物，无法删除", Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(view.getContext()).setTitle("系统提示")//设置对话框标题
                            .setMessage("确定要删除吗！")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    mDishesList.remove(position);
                                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                    db.delete("Dishes", "name = ?", new String[] { dishes.getName() });
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
                }

            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dishes dishes = mDishesList.get(position);
        holder.dishesName.setText(dishes.getName());
        holder.dishesCrop.setText(dishes.getCrop());
        holder.dishesState.setText(dishes.getState());
        holder.dishesId.setImageResource(dishes.getDeleteId());//得到删除图标
    }

    @Override
    public int getItemCount() {
        return mDishesList.size();
    }
}
