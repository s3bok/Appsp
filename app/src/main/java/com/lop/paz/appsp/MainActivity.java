package com.lop.paz.appsp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private AvisoDbAdapter mDBAdapter;
    private AvisoSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.avisos_list_view);
       findViewById(R.id.avisos_list_view);
        mListView.setDivider(null);
        mDBAdapter = new AvisoDbAdapter(this);
        mDBAdapter.open();

        if (savedInstanceState == null){
            //limpiar todos lo datos
            mDBAdapter.deleteReminder();

            //Add algunos avisos
            mDBAdapter.createReminder("Estudiar", true);
            mDBAdapter.createReminder("Estudiar 1", false);
            mDBAdapter.createReminder("Estudiar 2", false);
            mDBAdapter.createReminder("Estudiar 3", true);
        }

        Cursor cursor  = mDBAdapter.fetchAllReminders();

        //Desde las columnas definidas en la BD
        String[] from = new String[]{
                AvisoDbAdapter.COL_CONTENT
        };

        //al id de view  en el layout
        int[] to  = new int[]{
            R.id.row_text
        };


        mCursorAdapter = new AvisoSimpleCursorAdapter(
                //Context
                MainActivity.this,
                //El layout de la fila
                R.layout.avisos_row,
                //Cursor
                cursor,
                //desde columnas definidas en la base de datos
                from,
                //A las id de views en  el layout
                to,
                //flag- no usado
                0);

        //El cursor adapter (controller) esta actualziando la listview (view)
        //Con datos de la base de datos (model)
        mListView.setAdapter(mCursorAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_nuevo:
                //crea nuevo aviso
                Log.d(getLocalClassName(), "Crear  nuevo aviso");
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
