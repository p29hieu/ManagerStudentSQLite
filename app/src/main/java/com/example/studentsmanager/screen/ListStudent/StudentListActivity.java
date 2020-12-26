package com.example.studentsmanager.screen.ListStudent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsmanager.MainActivity;
import com.example.studentsmanager.R;
import com.example.studentsmanager.bean.Student;
import com.example.studentsmanager.component.dialog.SearchDialog;
import com.example.studentsmanager.screen.AddStudent.AddStudentActivity;
import com.example.studentsmanager.screen.DeleteManyStudent.DeleteManyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentListActivity extends AppCompatActivity {

    private static final String TAG = "StudentListActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private final Activity activity = this;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected StudentAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_student_list);
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = findViewById(R.id.rv_list_students);
        if (mRecyclerView == null) {
            return;
        }
        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        Cursor cursor = MainActivity.db.getData("select * from " + Student.tableName + ";");
        mAdapter = new StudentAdapter(cursor, this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)


        ((FloatingActionButton) findViewById(R.id.btn_refresh)).setOnClickListener(v -> {
            Toast.makeText(this, "refreshing...", Toast.LENGTH_SHORT).show();
            refreshData();
            Toast.makeText(this, "refresh done!", Toast.LENGTH_SHORT).show();

        });

//        mLinearLayoutRadioButton = (RadioButton) findViewById(R.id.linear_layout_rb);
//        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
//            }
//        });
//
//        mGridLayoutRadioButton = (RadioButton) findViewById(R.id.grid_layout_rb);
//        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
//            }
//        });
    }

    public void refreshData(){
        Cursor cursor2 = MainActivity.db.getData("select * from " + Student.tableName + ";");
        mAdapter = new StudentAdapter(cursor2, this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ((MenuItem) menu.findItem(R.id.menu_add)).setOnMenuItemClickListener(
                item -> {
                    Intent intent = new Intent(activity, AddStudentActivity.class);
                    activity.startActivity(intent);
                    return true;
                }
        );
        ((MenuItem) menu.findItem(R.id.menu_search)).setOnMenuItemClickListener(
                item -> {
                    openSearchDialog();
                    return true;
                }
        );
        ((MenuItem) menu.findItem(R.id.menu_delete_many)).setOnMenuItemClickListener(
                item -> {
                    Intent intent = new Intent(activity, DeleteManyActivity.class);
                    activity.startActivity(intent);
                    return true;
                }
        );

        return super.onCreateOptionsMenu(menu);

    }

    private void openSearchDialog() {
        SearchDialog.SearchListener listener = new SearchDialog.SearchListener() {
            @Override
            public void search(String id, String name) {
                id = id.replace("'", " ");
                name = name.replace("'", " ");
                Cursor cursor = MainActivity.db.getData("select * from " + Student.tableName + "" +
                        " where id like '%" + id + "%' " +
                        " and name like '%" + name + "%' " +
                        ";");
                mAdapter = new StudentAdapter(cursor, activity);
                // Set CustomAdapter as the adapter for RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
            }
        };

        final Dialog dialog = new SearchDialog(this, listener);
        dialog.show();
    }

}

