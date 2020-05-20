package com.example.observalo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.widget.ImageView;

import com.example.observalo.herramientas.paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.alpha;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class AppInfo extends paint {
    private String label;
    private String packageName;
    private Drawable icon;
    private int primaryColor;

    public AppInfo (String label, String packageName, Drawable icon){
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;

        float[] hsv = new float[3];
        int color = paint.getDominantColor2(drawableToBitmap(icon));
        Color.colorToHSV(color, hsv);
        //hsv[2] += 15f - hsv[2]; // value component
        //hsv[1] = 0.6f;
        hsv[2] = 0.9f;

        color = Color.HSVToColor(hsv);

        this.primaryColor = color;
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
    public int getPrimaryColor(){
        return primaryColor;
    }

}