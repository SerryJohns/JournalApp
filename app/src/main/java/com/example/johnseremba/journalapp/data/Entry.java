package com.example.johnseremba.journalapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "entry", indices = {@Index(value = {"date"}, unique = true)})
public class Entry {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String category;
    public Long dateAdded;

}
