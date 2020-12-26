package com.example.studentsmanager.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studentsmanager.bean.Student;

import java.util.ArrayList;

import io.bloco.faker.Faker;

public class Database extends SQLiteOpenHelper {
    static public SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        if (db == null || !db.isOpen()) db = getWritableDatabase();
    }

    // C , U, D
    public boolean queryData(String query) {
        try {
            if (db == null || !db.isOpen()) db = getWritableDatabase();
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public Cursor getData(String query) {
        try {
            if (db == null || !db.isOpen()) db = getWritableDatabase();
            return db.rawQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addStudents(Student student) {
        Cursor old = this.getData("select id from " + Student.tableName + " where id like '" + student.getId().replace("'", " ") + "';");
        if (old != null && old.moveToNext()) {
            String t = old.getString(0);
            int index = old.getColumnIndex("id");
            String t2 = old.getString(index);
            Student st = new Student(
                    old.getString(old.getColumnIndex("id")),
                    old.getString(old.getColumnIndex("name")),
                    old.getString(old.getColumnIndex("dob")),
                    old.getString(old.getColumnIndex("email")),
                    old.getString(old.getColumnIndex("address"))
            );
            return false;
        }
        boolean result = this.queryData("insert into " + Student.tableName + " values (" +
                "\"" + student.getId().trim() + " \" , " +
                "\"" + student.getName().trim() + "\" , " +
                "\"" + student.getDob().trim() + "\" , " +
                "\"" + student.getEmail().trim() + "\" , " +
                "\"" + student.getAddress().trim() + "\" );");
        return result;
    }


    public boolean updateStudent(Student student) {
        String query = "update " + Student.tableName + " set " +
                "name = '" + student.getName().trim().replace("'", " ") + "', " +
                "dob = '" + student.getDob().trim().replace("'", " ") + "', " +
                "address = '" + student.getAddress().trim().replace("'", " ") + "', " +
                "email = '" + student.getEmail().trim().replace("'", " ") + "' " +
                "where id = '" + student.getId() + "' ;";
        return this.queryData(query);
    }

    public boolean deleteStudent(String id) {
        String query = "delete from " + Student.tableName + " where " +
                "id = '" + id.trim().replace("'", " ") + "' ;";
        return this.queryData(query);
    }

    public Student getOne(String id) {
        String query = "select * from " + Student.tableName + " where " +
                "id like '" + id.replace("'", " ") + "' ;";
        Cursor cursor = this.getData(query);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Student st = new Student(
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("dob")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("address"))
            );
            return st;
        }
        return null;
    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
    }

    public boolean deleteListStudent(ArrayList<String> listDelete) {
        if (listDelete.size() > 0) {
            String args = TextUtils.join("', '", listDelete);
            args = "'" + args + "'";
            return this.queryData(String.format("DELETE FROM " + Student.tableName + " WHERE id IN (%s);", args));
        }
        return true;
    }
}
