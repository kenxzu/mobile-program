package com.example.memolite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelperMemo extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bepungMemo.db";
    public static final String TABLE_NAME = "memo";
    public static final String COLUMN_NAME = "judul" ;
    public static final String COLUMN_CONTENT = "catatan" ;

    public DBHelperMemo(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + "(id integer primary key, "+
                        COLUMN_NAME+" text,"+COLUMN_CONTENT+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String parJudul, String parCatatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, parJudul);
        contentValues.put(COLUMN_CONTENT, parCatatan);
        long i = db.insert(TABLE_NAME, null, contentValues);
        return (i>=0);
    }

    public Cursor getData(int parId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where id=" + parId + "", null);
        return res;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateData(Integer id, String parJudul, String parCatatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, parJudul);
        contentValues.put(COLUMN_CONTENT, parCatatan);
        long i = db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return (i>=0);
    }

    public Integer deleteData(Integer parId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(parId)});
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
            Log.d("________bepung_jmlData", Integer.toString(res.getCount()));
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                Log.d("_______bepung_baca_data", res.getString(res.getColumnIndex(COLUMN_NAME)));
                array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
                res.moveToNext();
            }
        } catch (Exception e) {
            Log.d("___________bepung", e.getMessage());
        }
        return array_list;
    }
}