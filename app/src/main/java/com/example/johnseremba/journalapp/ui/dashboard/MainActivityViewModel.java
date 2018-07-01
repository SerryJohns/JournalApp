package com.example.johnseremba.journalapp.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.johnseremba.journalapp.data.Entry;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Entry>> entries = new MutableLiveData<>();

    public MainActivityViewModel() {

    }

    public MutableLiveData<List<Entry>> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries.postValue(entries);
    }
}
