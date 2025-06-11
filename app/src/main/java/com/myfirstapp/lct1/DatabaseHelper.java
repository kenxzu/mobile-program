package com.myfirstapp.lct1;



import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "employee.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE employee (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "position TEXT," +
                "department TEXT," +
                "email TEXT," +
                "phone TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
    }

    public long addEmployee(Employee e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", e.getName());
        values.put("position", e.getPosition());
        values.put("department", e.getDepartment());
        values.put("email", e.getEmail());
        values.put("phone", e.getPhone());
        return db.insert("employee", null, values);
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM employee", null);
        while (cursor.moveToNext()) {
            list.add(new Employee(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            ));
        }
        cursor.close();
        return list;
    }

    public Employee getEmployee(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM employee WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Employee e = new Employee(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            return e;
        }
        return null;
    }

    public int updateEmployee(Employee e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", e.getName());
        values.put("position", e.getPosition());
        values.put("department", e.getDepartment());
        values.put("email", e.getEmail());
        values.put("phone", e.getPhone());
        return db.update("employee", values, "id=?", new String[]{String.valueOf(e.getId())});
    }

    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("employee", "id=?", new String[]{String.valueOf(id)});
    }
}
