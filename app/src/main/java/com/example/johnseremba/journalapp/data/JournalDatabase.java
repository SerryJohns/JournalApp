package com.example.johnseremba.journalapp.data;

import android.arch.persistence.room.Room;
import android.content.Context;

public class JournalDatabase {

    private static final String DATABASE_NAME = "journal_db";
    private static final Object LOCK = new Object();
    private static volatile AppDatabase journalInstance;

    public static AppDatabase getInstance(Context context) {
        if (journalInstance == null) {
            synchronized (LOCK) {
                if (journalInstance == null) {
                    journalInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return journalInstance;
    }
}
