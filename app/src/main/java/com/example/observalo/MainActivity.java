package com.example.observalo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean isBottom = true;
    ViewPager mViewPager;
    int cellHeight;
    int numberOfRows = 5;

    List<AppObject> installedAppList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Oculta la barra
        getSupportActionBar().hide();

        //Inicializa la Home
        initializeHome();
        initializeDrawer();
    }

    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();
        ArrayList<AppObject> appList = new ArrayList<>();
        for (int i = 0; i<20; i++)
            appList.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));

        //Cantidad de páginas
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));

        //Define la altura de la pantalla
        cellHeight = getDisplayContentHeight() / numberOfRows;

        final GridView mDrawerGridView = findViewById(R.id.grid);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(this, pagerAppList, cellHeight));


    }


    private void initializeDrawer(){
        View mBottomSheet = findViewById(R.id.bottomSheet);
        final GridView mDrawerGridView = findViewById(R.id.drawerGrid);
        final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(100);

        installedAppList = getInstalledAppList();
        mDrawerGridView.setAdapter(new AppAdapter(getApplicationContext(), installedAppList, cellHeight));

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(newState==BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    //Detecta las aplicaciones instaladas y las guarda
    private List<AppObject> getInstalledAppList() {
        List<AppObject> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

            for(ResolveInfo untreatedApp : untreatedAppList) {
                String appName = untreatedApp.activityInfo.loadLabel(getPackageManager()).toString();
                String appPackageName = untreatedApp.activityInfo.packageName;
                Drawable appImage = untreatedApp.activityInfo.loadIcon(getPackageManager());
                AppObject app = new AppObject(appPackageName, appName, appImage);

                if(!list.contains(app)){
                    list.add(app);
                }
            }

        return list;
    }

    //Busca la altura donde debería poner las apps
    private int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;
        if(getActionBar() != null){
            actionBarHeight = getActionBar().getHeight();
        }

        //Saca parámetros del dispositivo
        int resourceId = getResources().getIdentifier("status_bar_height", "dimensions", "android");

        //Si resourceId existe lo toma y calcula
        //la cantidad de píxeles de la pantalla
        if(resourceId > 0){
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        //Encuentra el borde superior
        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);

        //Saca el valor y
        screenHeight = size.y;
        return  screenHeight - contentTop - actionBarHeight - statusBarHeight;
    }
}
