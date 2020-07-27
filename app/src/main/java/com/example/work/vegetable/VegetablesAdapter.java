package com.example.work.vegetable;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.MyDatabaseHelper;
import com.example.work.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VegetablesAdapter extends RecyclerView.Adapter<VegetablesAdapter.ViewHolder> {

    private List<Vegetables> mVegetablesList;
    private String name;//地的名字
    double areaNumber=1;//面积
    int timeNumber=100;//成熟时间
    static class ViewHolder extends RecyclerView.ViewHolder {
        View vegetableView;
        ImageView vegetableImage;
        TextView vegetableName;

        public ViewHolder(View view) {
            super(view);
            vegetableView = view;
            vegetableImage = (ImageView) view.findViewById(R.id.vegetables_image);
            vegetableName = (TextView) view.findViewById(R.id.vegetables_name);
        }
    }

    public VegetablesAdapter(List<Vegetables> mVegetablesList, String name) {
        this.mVegetablesList = mVegetablesList;
        this.name = name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vegetables_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.vegetableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Vegetables vegetable = mVegetablesList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + vegetable.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        final MyDatabaseHelper databaseHelper = new MyDatabaseHelper(view.getContext(), "dishesManager.db", null, 1);//操作数据库
        databaseHelper.getWritableDatabase();


        holder.vegetableImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                final Vegetables vegetable = mVegetablesList.get(position);
                final Context context = v.getContext();
                //Toast.makeText(v.getContext(), "you clicked image " + vegetable.getName(), Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要种植该农作物吗！")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.input_dialog, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                // 设置
                                alertDialogBuilder.setView(promptsView);
                                final EditText area = (EditText) promptsView
                                        .findViewById(R.id.input_number);
                                final EditText time = (EditText) promptsView
                                        .findViewById(R.id.mature_time);
                                // 设置对话框
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // 得到输入数据，设置最大输入为2亩
                                                        if (!"".equals(area.getText().toString())  && area.getText().toString() != null
                                                                && Double.parseDouble(area.getText().toString())<=2 && Double.parseDouble(area.getText().toString())>0) {
                                                            areaNumber = Double.parseDouble(area.getText().toString());
                                                        }
                                                        if (!"".equals(time.getText().toString())  && time.getText().toString() != null
                                                                && Integer.parseInt(time.getText().toString())>0) {
                                                            timeNumber  = Integer.parseInt(time.getText().toString());
                                                        }
                                                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                                        ContentValues values = new ContentValues();//更新数据

                                                        values.put("crop", vegetable.getName());
                                                        values.put("state", "成长");
                                                        values.put("area", areaNumber);
                                                        values.put("matureTime", timeNumber);
                                                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                                        String str = sf.format(curDate);
                                                        values.put("plantTime", str);
                                                        values.put("image", vegetable.getImageId());//存放图片地址
                                                        db.update("Dishes", values, "name=?", new String[]{name});
                                                        notifyDataSetChanged();
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
                                //Toast.makeText(context,areaNumber+"",Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Vegetables vegetable = mVegetablesList.get(position);
        holder.vegetableImage.setImageResource(vegetable.getImageId());
        holder.vegetableName.setText(vegetable.getName());
    }

    @Override
    public int getItemCount() {
        return mVegetablesList.size();
    }

}