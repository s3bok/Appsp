package com.lop.paz.appsp;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private AvisoDbAdapter mDBAdapter;
    private AvisoSimpleCursorAdapter mCursorAdapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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


        //Cuando pulsas sobre un item de la lista
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ListView modeListView = new ListView(MainActivity.this);
                String[] modes = new String[] { "Editar Aviso", "Borrar Aviso" };
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar aviso
                        if (position == 0) {
                            Toast.makeText(MainActivity.this, "editar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                            //borrar aviso
                        } else {
                            Toast.makeText(MainActivity.this, "borrar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) { }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_aviso:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDBAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDBAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) { }
            });

        }

    }

    private int getIdFromPosition(int nC) {
        return (int)mCursorAdapter.getItemId(nC);
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
