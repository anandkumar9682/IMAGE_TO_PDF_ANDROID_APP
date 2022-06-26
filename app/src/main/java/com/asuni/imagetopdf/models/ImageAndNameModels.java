package com.asuni.imagetopdf.models;

import android.graphics.Bitmap;

public class ImageAndNameModels {

    private Bitmap bitmap;
    private String name;

    public ImageAndNameModels(String name,Bitmap bitmap){
        this.name=name;
        this.bitmap=bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

