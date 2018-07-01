package com.example.johnseremba.journalapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "entry", indices = {@Index(value = {"date_added"}, unique = true)})
public class Entry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String category;

    @ColumnInfo(name = "date_added")
    public Long dateAdded;

    public Entry(String title, String description, String category, Long dateAdded) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.dateAdded = dateAdded;
    }
}
