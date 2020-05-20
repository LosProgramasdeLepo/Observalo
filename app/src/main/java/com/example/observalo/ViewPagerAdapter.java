package com.example.observalo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<PagerObject> pagerAppList;
    private int cellWidth;
    private int cellHeight;
    private int verticalSpacing;
    private int numberOfCols;

    public ViewPagerAdapter(Context context, ArrayList<PagerObject> pagerAppList, int cellWidth, int cellHeight, int numberOfCols,  int verticalSpacing) {
        this.context = context;
        this.pagerAppList = pagerAppList;
        this.cellWidth = cellWidth;
        this.verticalSpacing = verticalSpacing;
        this.numberOfCols = numberOfCols;
        this.cellHeight = cellHeight;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout, container, false);

        final GridView mGridView = layout.findViewById(R.id.grid);
        mGridView.setVerticalSpacing(verticalSpacing);
        mGridView.setNumColumns(numberOfCols);
        mGridView.setAdapter(new AppAdapter(context, pagerAppList.get(position).getAppList(), cellWidth, cellHeight));

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pagerAppList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
