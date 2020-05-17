
package com.example.observalo;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean isBottom = true;
    ViewPager mViewPager;
    int cellWidth;
    int cellHeight;
    int verticalSpacing = 5;
    int numberOfRows = 5;
    int cantAppsEnPantalla = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<AppInfo> listaDeApps = getListaDeApps(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Oculta la barra
        getSupportActionBar().hide();

        //Inicializa la Home
        initializeHome(listaDeApps);

    }

    //NO FUNCIONA, Attempt to invoke virtual method 'boolean android.graphics.Bitmap.compress(android.graphics.Bitmap$CompressFormat, int, java.io.OutputStream)' on a null object reference
    private void setWallpaper() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
        try{
            manager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeHome(ArrayList<AppInfo> listaDeApps) {

        //a continuación se van a crear algnos pagerObjects, cada uno por una página
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();
        int k = 0;
        float cantApps = listaDeApps.size();
        //loop para crear todas las páginas con las aplicaciones:
        for (int i = 0; i < Math.ceil(cantApps/cantAppsEnPantalla); i++) {
            ArrayList<AppInfo> appList = new ArrayList<>(); //lista para una página
            for (int j = 0; j < cantAppsEnPantalla; j++) {
                if(k < cantApps) {
                    appList.add(listaDeApps.get(k));
                    k++;
                }
            }
            pagerAppList.add(new PagerObject(appList));
        }

        //Define la altura de cada layout individual de los botones
        cellHeight = getDisplayContentSize(true) / numberOfRows - verticalSpacing*2;

        cellWidth = getDisplayContentSize(false) / 3 - verticalSpacing*2;

        final GridView mPagerGridView = findViewById(R.id.grid);
        mViewPager = findViewById(R.id.viewPager);


        mViewPager.setAdapter(new ViewPagerAdapter(this, pagerAppList, cellWidth, cellWidth, verticalSpacing));

    }

    //Busca la altura que debería tener cada cuadrado de app
    private int getDisplayContentSize(boolean wh) {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        if(wh == false){
            //Saca el ancho de la pantalla
            return size.x;
        }else {
            int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;
            if (getActionBar() != null) {
                actionBarHeight = getActionBar().getHeight();
            }

            //Saca parámetros del dispositivo
            int resourceId = getResources().getIdentifier("status_bar_height", "dimensions", "android");

            //Si resourceId existe lo toma y calcula
            //la cantidad de píxeles de la pantalla
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            //Encuentra el borde superior
            int contentTop = (findViewById(android.R.id.content)).getTop();

            //Saca el alto de la pantalla
            screenHeight = size.y;
            return screenHeight - contentTop - actionBarHeight - statusBarHeight;
        }
    }

    //Función que devuelve el ícono de una aplicación
    //Hay que ver cómo hacer para cambiar algunos por los propios
    //(Parte de este código no se entiende, así que cuidadito al modificar)
    public static Drawable getActivityIcon(Context context, String packageName, String activityName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        return resolveInfo.loadIcon(pm);
    }

    //La siguiente función devuelve la lista de aplicaciones del sistema
    //Cada aplicación está guardada como un "AppInfo" (clase nuestra)
    //Los datos por ahora son: título, nombre de paquete e ícono, aunque también debería tener uno para el color de fondo
    public ArrayList<AppInfo> getListaDeApps(Context c) {
        PackageManager pm = c.getPackageManager();
        ArrayList<AppInfo> appsList = new ArrayList<AppInfo>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

        //Lo siguiente es un for que pasa por todas las aplicaciones y crea un coso para cada una
        for(ResolveInfo ri:allApps) {
            String label = ri.loadLabel(pm).toString();
            String packageName = ri.activityInfo.packageName;
            Drawable icon = ri.activityInfo.loadIcon(pm);
            AppInfo app = new AppInfo(label, packageName, icon);
            appsList.add(app);
        }

        return appsList;
    }

    @Override
    public void onBackPressed() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            mViewPager.setCurrentItem(0);
        }
    }

}
