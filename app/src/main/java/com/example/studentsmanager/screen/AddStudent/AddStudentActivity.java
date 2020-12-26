package com.example.studentsmanager.screen.AddStudent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsmanager.MainActivity;
import com.example.studentsmanager.R;
import com.example.studentsmanager.bean.Student;
import com.example.studentsmanager.component.dialog.YesNoDialogFragment;
import com.example.studentsmanager.database.Database;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity implements YesNoDialogFragment.YesNoDialogFragmentListener {
    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextAddress;
    private EditText editTextDob;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        editTextId = findViewById(R.id.et_add_id);
        editTextName = findViewById(R.id.et_add_name);
        editTextEmail = findViewById(R.id.et_add_email);
        editTextAddress = findViewById(R.id.et_add_address);
        editTextDob = findViewById(R.id.et_add_dob);
        Button buttonSubmit = findViewById(R.id.btn_add);

        editTextDob.setOnClickListener(
                v -> {
                    openPicker();
                }
        );
        buttonSubmit.setOnClickListener(v -> {
            onSubmit();
        });
    }

    private void onSubmit() {

        // Create YesNoDialogFragment
        DialogFragment dialogFragment = new YesNoDialogFragment();

        // Arguments:
        Bundle args = new Bundle();
        args.putString(YesNoDialogFragment.ARG_TITLE, "Confirmation");
        args.putString(YesNoDialogFragment.ARG_MESSAGE, "Do you want to add this student?");
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Show:
        dialogFragment.show(fragmentManager, "Dialog");
    }


    private void openPicker() {
        final TextView txtDate = findViewById(R.id.et_add_dob);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //TextView txtDate = null;
                        editTextDob.setText(dayOfMonth + "-" + (monthOfYear + 1)
                                + "-" + year);

                        Toast.makeText(activity, mYear + "/" + mMonth + "/"
                                + mDay, Toast.LENGTH_SHORT);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();

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
            Student student = new Student(
                    editTextId.getText().toString(),
                    editTextName.getText().toString(),
                    editTextDob.getText().toString(),
                    editTextEmail.getText().toString(),
                    editTextAddress.getText().toString()
            );
            boolean res = MainActivity.db.addStudents(student);
            if (res) {
                Toast.makeText(activity, "Add success", Toast.LENGTH_SHORT).show();
               super.finish();
            } else {
                Toast.makeText(activity, "Add failure", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //
        } else {
            //
        }
    }
}
