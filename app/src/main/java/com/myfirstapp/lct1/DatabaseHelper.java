package com.myfirstapp.lct1;

// DatabaseHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "MyAppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_USERS = "users";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_AGE = "age";

    // Create table SQL query
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_EMAIL + " TEXT UNIQUE," +
                    COLUMN_AGE + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }

    // Insert user
    public long insertUser(String name, String email, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_AGE, age);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();

        return id;
    }

    // Get single user
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_AGE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));

        cursor.close();
        db.close();

        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return users;
    }

    // Update user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_AGE, user.getAge());

        int result = db.update(TABLE_USERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});

        db.close();

        return result;
    }

    // Delete user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Get users count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }
}