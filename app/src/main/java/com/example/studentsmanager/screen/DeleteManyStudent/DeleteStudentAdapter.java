package com.example.studentsmanager.screen.DeleteManyStudent;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsmanager.R;
import com.example.studentsmanager.bean.Student;

import java.util.ArrayList;


public class DeleteStudentAdapter extends RecyclerView.Adapter<DeleteStudentAdapter.ViewHolder> {
    private static final String TAG = "DeleteStudentAdapter";
    Cursor cs;
    Activity parentActivity;
    ArrayList<String> listDelete = new ArrayList<>();

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param cs Student[] containing the data to populate views to be used by RecyclerView.
     */
    public DeleteStudentAdapter(Cursor cs, Activity activity, ArrayList<String> listDelete) {
        this.cs = cs;
        this.parentActivity = activity;
        this.listDelete = listDelete;
    }
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_id;
        private TextView tv_email;
        private CheckBox checkbox_delete;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_id = (TextView) v.findViewById(R.id.tv_id);
            tv_email = v.findViewById(R.id.tv_email);
            checkbox_delete = v.findViewById(R.id.checkbox_delete);
        }

        public TextView getTv_name() {
            return tv_name;
        }


        public TextView getTv_id() {
            return tv_id;
        }


        public TextView getTv_email() {
            return tv_email;
        }

        public CheckBox getCheckbox_delete() {
            return checkbox_delete;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)


    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item_delete, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        if (cs != null && cs.moveToPosition(position)) {
            Student student = new Student(
                    cs.getString(cs.getColumnIndex("id")),
                    cs.getString(cs.getColumnIndex("name")),
                    cs.getString(cs.getColumnIndex("dob")),
                    cs.getString(cs.getColumnIndex("email")),
                    cs.getString(cs.getColumnIndex("address"))
            );
            viewHolder.getTv_name().setText(student.getName());
            viewHolder.getTv_id().setText(student.getId());
            viewHolder.getTv_email().setText(student.getEmail());
            viewHolder.getCheckbox_delete().setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    this.listDelete.remove(student.getId());
                } else {
                    this.listDelete.add(student.getId());
                }
            });
        }


    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (cs != null && cs.getCount() > 0)
            return cs.getCount();
        Toast.makeText(parentActivity.getBaseContext(), "No data!", Toast.LENGTH_SHORT).show();
        return 0;
    }
}
