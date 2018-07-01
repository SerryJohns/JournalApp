package com.example.johnseremba.journalapp.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.johnseremba.journalapp.R;
import com.example.johnseremba.journalapp.data.AppDatabase;
import com.example.johnseremba.journalapp.data.Entry;
import com.example.johnseremba.journalapp.data.JournalDatabase;

import java.util.Date;

public class NewEntryActivity extends AppCompatActivity {

    private AppDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        mDatabase = JournalDatabase.getInstance(getApplicationContext());
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

        }
    }
}
