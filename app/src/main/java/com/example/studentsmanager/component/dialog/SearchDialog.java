package com.example.studentsmanager.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentsmanager.R;

public class SearchDialog extends Dialog {
    private EditText editTextSearchId;
    private EditText editTextSearchName;
    private Button buttonOK;
    private Button buttonCancel;
    public Context context;
    private SearchDialog.SearchListener listener;

   public interface SearchListener {
        public void search(String id, String name);
    }

    public SearchDialog(Context context, SearchDialog.SearchListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_search_dialog);

        this.editTextSearchId = findViewById(R.id.et_search_id);
        this.editTextSearchName = findViewById(R.id.et_search_name);
        buttonOK = findViewById(R.id.btn_search_ok);
        buttonCancel = findViewById(R.id.btn_search_cancel);

        buttonOK.setOnClickListener(v -> {
            buttonOKClick();
        });

        buttonCancel.setOnClickListener(v -> {
            buttonCancelClick();
        });
    }

    private void buttonCancelClick() {
        this.dismiss();
    }

    private void buttonOKClick() {
        String id = editTextSearchId.getText().toString();
        String name = editTextSearchName.getText().toString();
        if ((id == null || "".equals(id)) && (name == null || "".equals(name))) {
            Toast.makeText(this.context, "Please enter params!", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss();
        if (this.listener != null) {
            this.listener.search(id, name);
        }
    }
}
