package com.example.studentsmanager.screen.DetailStudent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsmanager.MainActivity;
import com.example.studentsmanager.R;
import com.example.studentsmanager.bean.Student;
import com.example.studentsmanager.component.dialog.YesNoDialogFragment;
import com.example.studentsmanager.screen.UpdateStudent.UpdateStudentActivity;

public class DetailStudentActivity extends AppCompatActivity implements YesNoDialogFragment.YesNoDialogFragmentListener {

    private Activity activity = this;
    private final static String TAG = "DetailStudentActivity";
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_detail_student);

        Intent intent = getIntent();
        String json = intent.getStringExtra("student");
        this.student = Student.fromJson(json);
        if (this.student != null) {
            decorateDetail(this.student);
        }
    }

    private void decorateDetail(Student student) {
        ((TextView) findViewById(R.id.text_detail_id)).setText(student.getId());
        ((TextView) findViewById(R.id.text_detail_name)).setText(student.getName());
        ((TextView) findViewById(R.id.text_detail_dob)).setText(student.getDob());
        ((TextView) findViewById(R.id.text_detail_email)).setText(student.getEmail());
        ((TextView) findViewById(R.id.text_detail_address)).setText(student.getAddress());

        ((Button) findViewById(R.id.btn_goto_update)).setOnClickListener(v -> {
            Intent intent2 = new Intent(this, UpdateStudentActivity.class);
            intent2.putExtra("student", student.toJson());
            startActivity(intent2);
        });

        ((Button) findViewById(R.id.btn_delete)).setOnClickListener(v -> {
            onClickDelete();
        });
    }

    private void onClickDelete() {
        // Create YesNoDialogFragment
        DialogFragment dialogFragment = new YesNoDialogFragment();

        // Arguments:
        Bundle args = new Bundle();
        args.putString(YesNoDialogFragment.ARG_TITLE, "Confirmation");
        args.putString(YesNoDialogFragment.ARG_MESSAGE, "Do you want to delete this student?");
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Show:
        dialogFragment.show(fragmentManager, "Dialog");
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof YesNoDialogFragment) {
            YesNoDialogFragment yesNoDialogFragment = (YesNoDialogFragment) fragment;
            yesNoDialogFragment.setOnYesNoDialogFragmentListener(this);
        }
    }

    @Override
    public void onYesNoResultDialog(int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //
            boolean res = MainActivity.db.deleteStudent(this.student.getId());
            if (res) {
                Toast.makeText(activity, "Delete success", Toast.LENGTH_SHORT).show();
                super.finish();
            } else {
                Toast.makeText(activity, "Delete failure", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //
        } else {
            //
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Student student = MainActivity.db.getOne(this.student.getId());
        if (student != null) {
            decorateDetail(student);
            this.student.update(student);
        }
    }
}
