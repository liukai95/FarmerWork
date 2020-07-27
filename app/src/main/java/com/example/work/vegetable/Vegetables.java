package com.example.work.vegetable;

/**
 * 蔬菜类
 */

public class Vegetables {
    private String name;
    private int imageId;

    public Vegetables(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
