package com.example.observalo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppAdapter extends BaseAdapter {
    Context context;
    List<AppInfo> appList;
    int cellWidth;
    int cellHeight;

    public AppAdapter(Context context, List<AppInfo> appList, int cellWidth, int cellHeight){
        this.context = context;
        this.appList = appList;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v;
        if(convertView == null){ //si le agregás un "|| position == 0" funciona el primer boton pero dejan de funcionar los siguientes 5 ¿Qué?
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_app, parent, false);

            LinearLayout mLayout = v.findViewById(R.id.layout);
            ImageView mImage = v.findViewById(R.id.image);
            TextView mLabel = v.findViewById(R.id.label);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cellWidth, cellHeight);
            mLayout.setLayoutParams(lp);

            GradientDrawable gradientDrawable = new GradientDrawable();
            int colorPrimario = appList.get(position).getPrimaryColor();

            gradientDrawable.setColor(colorPrimario);
            gradientDrawable.setCornerRadius(20f);

            mLayout.setBackground(gradientDrawable);

            mImage.setImageDrawable(appList.get(position).getIcon());

            mImage.getLayoutParams().height = (int)(cellHeight*0.5);
            mImage.getLayoutParams().width = mImage.getLayoutParams().height;
            mLabel.setText(appList.get(position).getLabel());
            mLabel.setTextSize(mImage.getLayoutParams().width * 0.115f);
            mLayout.setContentDescription(appList.get(position).getLabel());

            //Para abrir apps
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchAppIntent = context.getPackageManager().getLaunchIntentForPackage(appList.get(position).getPackageName());
                    if (launchAppIntent != null)
                        context.startActivity(launchAppIntent);
                }
            });
        } else {
            v = convertView;
        }

        return v;
    }

}
