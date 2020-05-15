package com.example.observalo;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.widget.ImageView;

public class AppInfo {
    private String label;
    private String packageName;
    private Drawable icon;

    public AppInfo (String label, String packageName, Drawable icon){
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getPackageName(){
        return packageName;
    }
    public String getLabel(){
        return label;
    }
    public Drawable getIcon(){
        return icon;
    }

}