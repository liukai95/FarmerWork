package com.example.work.house;

import java.io.Serializable;

/**
 * Created by 刘开 on 2017/6/1.
 * 仓库类
 */

public class House implements Serializable{//实现序列化

    private int id;
    private String fieldName;//地区的名字
    private String crop;//农作物
    private int supply;//农作物的供货量
    private int yield;//农作物的剩余量
    private String pickTime;//采摘时间
    private String listedTime;//上市时间
    private String offTime;//下架时间
    private int imageId;//图片地址

    public House(int id, String fieldName, String crop, int supply, int yield, String pickTime, String listedTime, String offTime,int imageId) {
        this.id = id;
        this.fieldName = fieldName;
        this.crop = crop;
        this.supply = supply;
        this.yield = yield;
        this.pickTime = pickTime;
        this.listedTime = listedTime;
        this.offTime = offTime;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getCrop() {
        return crop;
    }

    public int getSupply() {
        return supply;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }

    public int getYield() {
        return yield;
    }

    public String getPickTime() {
        return pickTime;
    }

    public String getListedTime() {
        return listedTime;
    }

    public String getOffTime() {
        return offTime;
    }

    public int getImageId() {
        return imageId;
    }
}
