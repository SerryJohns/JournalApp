package com.example.johnseremba.journalapp.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.johnseremba.journalapp.R;
import com.example.johnseremba.journalapp.data.AppDatabase;
import com.example.johnseremba.journalapp.data.Entry;
import com.example.johnseremba.journalapp.data.JournalDatabase;

import java.util.Date;

public class NewEntryActivity extends AppCompatActivity {

    private AppDatabase mDatabase;
    private Spinner mSpinner;
    private TextInputLayout mTitle;
    private EditText mDesc;
    private Button btbSubmit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        mSpinner = (Spinner) findViewById(R.id.tag_spinner);
        mTitle = (TextInputLayout) findViewById(R.id.txt_title);
        mDesc = (EditText) findViewById(R.id.txt_description);
        btbSubmit = (Button) findViewById(R.id.btn_submit);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tag_entries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mDatabase = JournalDatabase.getInstance(getApplicationContext());

        btbSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEntrySubmission();
            }
        });
    }

    private void handleEntrySubmission() {
        progressBar.setVisibility(View.VISIBLE);
        String title = mTitle.getEditText().toString();
        String tag = mSpinner.getSelectedItem().toString();
        String desc = mDesc.getText().toString();

        Entry entry = new Entry(title, desc, tag, new Date().getTime());
        new InsertEntries().execute(entry);
    }

    private class InsertEntries extends AsyncTask<Entry, Void, Void> {
        @Override
        protected Void doInBackground(Entry... entries) {
            for(Entry entry : entries) {
                mDatabase.entryDao().insert(entry);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            finish();
        }
    }
}
