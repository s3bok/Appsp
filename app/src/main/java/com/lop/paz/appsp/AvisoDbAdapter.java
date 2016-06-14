package com.lop.paz.appsp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pali on 12/06/16.
 */
public class AvisoDbAdapter {

    //Nombres de las columnas
    public static final String  COL_ID = "_id";
    public static final String  COL_CONTENT = "content";
    public static final String  COL_IMPORTANT = "important";

    //Estos son los indices correspondentes
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;


    //se uasa para el logg
    public static final String TAG ="AcisosDbAdapter";


    private DataBaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String  DATABASE_NAME = "dba_remdrs";
    private static final String  TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;


    //Declaracion SQL usada  para crea la base de datos
    private static  final  String DATABASE_CREATE =
            "CREATE TABLE   inf not exists " + TABLE_NAME +"("+
                    COL_ID +"INTEGER PRIMARY KEY  autoincrement, " +
                    COL_CONTENT +" TEXT, "+
                    COL_IMPORTANT + " INTEGER );";

    public AvisoDbAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    //Abrir conexion db
     public void open() throws SQLException{
         mDbHelper =  new DataBaseHelper(mCtx);
         mDb = mDbHelper.getWritableDatabase();
     }

    //cerrar
    public void close(){
        if (mDbHelper != null){
            mDbHelper.close();
        }
    }

    //CREATE
    //La id se crea automaicamente
    public void createReminder(String name, boolean important){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, name);
        values.put(COL_IMPORTANT, important ? 1:0);
        mDb.insert(TABLE_NAME, null, values);
    }

    //Sobrecarga para tomar aviso
    public long createReminder(Aviso aviso){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, aviso.getmContet());//NOMBRE CONTACTO
        values.put(COL_IMPORTANT, aviso.getmImportant());//NUMERO DE TELEFNO

        //isertar fila
        return mDb.insert(TABLE_NAME, null, values);
    }

    //READ
    public Aviso fetchReminderById(int id){
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT}, COL_ID+ "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        return new Aviso(cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IMPORTANT)
        );
    }

    public Cursor fetchAllReminders(){
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT},
                null, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    //Update
    public long updateReminder(Aviso aviso) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, aviso.getmContet());//NOMBRE CONTACTO
        values.put(COL_IMPORTANT, aviso.getmImportant());//NUMERO DE TELEFNO

        //insertar fila
        return mDb.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(aviso.getmId())});
    }

    //DELETE
    public void deleteReminderById(int nId ){
       mDb.delete(TABLE_NAME, COL_ID +"=?", new String[]{String.valueOf(nId)});
    }


    public void deleteReminder(){
        mDb.delete(TABLE_NAME,null, null);
    }


    private static class  DataBaseHelper  extends SQLiteOpenHelper {

        DataBaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.w(TAG, DATABASE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG, "cambiando de verion "+1+" ala version "+i1);

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(sqLiteDatabase);

        }
    }





}
