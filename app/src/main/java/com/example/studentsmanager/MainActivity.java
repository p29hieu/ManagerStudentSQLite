package com.example.studentsmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsmanager.bean.Student;
import com.example.studentsmanager.database.Database;
import com.example.studentsmanager.screen.ListStudent.StudentListActivity;

import io.bloco.faker.Faker;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private final Activity activity = this;
    public static Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this, "dbstudents", null, 1);

        findViewById(R.id.btn_init_db).setOnClickListener(
                v -> initDb()
        );

        findViewById(R.id.btn_view_list_student).setOnClickListener(
                v -> goToListStudents()
        );
    }

    private void goToListStudents() {
        Intent intent = new Intent(activity, StudentListActivity.class);
        this.startActivity(intent);
    }

    private void initDb() {
        db.queryData("drop table if exists " + Student.tableName + ";");
        db.queryData("create table if not exists " + Student.tableName + "(" +
                "id text primary key," +
                "name text," +
                "dob text," +
                "email text," +
                "address text" +
                ")");
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            db.addStudents(
                    new Student("2017" + faker.number.number(4),
                            faker.name.firstName() + " " + faker.name.lastName(),
                            faker.date.birthday(18, 22).toString(),
                            faker.internet.email(),
                            faker.address.streetAddress())
            );
        }
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume:");
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

}
