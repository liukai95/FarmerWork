package com.example.work;

import java.io.Serializable;

/**
 * Created by 刘开 on 2017/6/1.
 * 菜品类
 */

public class Dishes implements Serializable{//实现序列化，进行Intent的传输
    private String name;//地区的名字
    private String crop;//地区的农作物
    private String state;//地区的状态
    private double area;//种植面积
    private int imageId;//图片地址

    private int deleteId;//删除

    public String getName() {
        return name;
    }

    public double getArea() {
        return area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeleteId() {
        return deleteId;
    }

    public String getCrop() {
        return crop;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Dishes(String name, String crop, String state, double area, int imageId, int deleteId) {
        this.name = name;
        this.crop = crop;
        this.state = state;
        this.area = area;
        this.imageId = imageId;
        this.deleteId = deleteId;
    }
}
