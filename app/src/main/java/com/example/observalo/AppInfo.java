package com.example.observalo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.alpha;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class AppInfo {
    private String label;
    private String packageName;
    private Drawable icon;
    private int primaryColor;

    public AppInfo (String label, String packageName, Drawable icon){
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
        this.primaryColor = getDominantColor2(drawableToBitmap(icon));
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

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public int getDominantColor2(Bitmap bitmap) {
        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

        int color = 0;
        Integer count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            if(alpha(color) > 5 && (Math.abs(red(color)-green(color)) + Math.abs(red(color)-blue(color)) + Math.abs(blue(color)-green(color))) > 150){
                count = colorMap.get(color);
                if (count == null)
                    count = 0;
                colorMap.put(color, ++count);
            }
        }

        int dominantColor = 0;
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                dominantColor = entry.getKey();
            }
        }
        return dominantColor;
    }

}