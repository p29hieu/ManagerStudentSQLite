package com.example.studentsmanager.screen.DeleteManyStudent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.studentsmanager.MainActivity;
import com.example.studentsmanager.R;
import com.example.studentsmanager.bean.Student;
import com.example.studentsmanager.component.dialog.YesNoDialogFragment;

import java.util.ArrayList;

public class DeleteManyActivity extends AppCompatActivity implements YesNoDialogFragment.YesNoDialogFragmentListener  {

    private static final String TAG = "DeleteManyActivity";
    private final Activity activity = this;

    protected RecyclerView mRecyclerView;
    protected DeleteStudentAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> listDelete = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_delete_many);
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = findViewById(R.id.rv_delete_list_students);
        if (mRecyclerView == null) {
            return;
        }
        mLayoutManager = new LinearLayoutManager(this);

        setRecyclerViewLayoutManager();
        Cursor cursor = MainActivity.db.getData("select * from " + Student.tableName + ";");
        mAdapter = new DeleteStudentAdapter(cursor, this,listDelete);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)


        ((Button) findViewById(R.id.btn_delete_many)).setOnClickListener(v -> {
            onClickDeleteMany();
        });

    }

    private void onClickDeleteMany() {  // Create YesNoDialogFragment
        DialogFragment dialogFragment = new YesNoDialogFragment();

        // Arguments:
        Bundle args = new Bundle();
        args.putString(YesNoDialogFragment.ARG_TITLE, "Confirmation");
        args.putString(YesNoDialogFragment.ARG_MESSAGE, "Do you want to delete these student?");
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Show:
        dialogFragment.show(fragmentManager, "Dialog");
    }

    public void refreshData(){
        Cursor cursor2 = MainActivity.db.getData("select * from " + Student.tableName + ";");
        mAdapter = new DeleteStudentAdapter(cursor2, this, this.listDelete);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     */
    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshData();
        Log.d(TAG, "onRestart: ");
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
            boolean res = MainActivity.db.deleteListStudent(this.listDelete);
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

}
