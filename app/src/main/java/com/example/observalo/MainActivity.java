
//hay que hacer que el tamaño del texto dependa del tamaño de los botones

package com.example.observalo;

import android.app.ActivityOptions;
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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
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
    int verticalSpacing = 10;
    int numberOfRows;
    int numberOfCols = 3;
    int cantAppsEnPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<AppInfo> listaDeApps = getListaDeApps(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Oculta la barra
        getSupportActionBar().hide();

        //Inicializa la Home
        initializeHome(listaDeApps);

        setWallpaper();

    }

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
        //Define la altura de cada layout individual de los botones
        cellHeight = getDisplayContentSize(false) / numberOfCols - verticalSpacing;

        numberOfRows = getDisplayContentSize(true) / (cellHeight+verticalSpacing); //height

        // debería agarrar el resto
        // si ese resto es relativamente grande, debería achicar los botones
        int resto =  getDisplayContentSize(true) % (cellHeight+verticalSpacing);    //obtiene el espacio restante blanco
        if (resto > 0.7*cellHeight){                                                    //se fija si este espacio es muy grande
            resto = cellHeight-resto;                                                   //si lo es, calcula el espacio que le falta para que haya un boton más
            cellHeight -= resto / numberOfRows;                                         //achica a todos los botones para que entre una fila más
            numberOfRows++;                                                             //y agrega una fila más
        }
        cellWidth = cellHeight;

        cantAppsEnPantalla = numberOfCols*numberOfRows;

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

        final GridView mPagerGridView = findViewById(R.id.grid);
        mViewPager = findViewById(R.id.viewPager);


        mViewPager.setAdapter(new ViewPagerAdapter(this, pagerAppList, cellWidth, cellWidth, numberOfCols, verticalSpacing));

    }

    //Busca la altura de la pantalla
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
