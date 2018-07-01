package com.example.johnseremba.journalapp.ui.dashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnseremba.journalapp.R;
import com.example.johnseremba.journalapp.data.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {
    private List<Entry> entries;
    private HashMap<String, Integer> imgTags = new HashMap<String, Integer>(){{
        put("Business", R.drawable.tag_business);
        put("Entertainment", R.drawable.tag_entertainment);
        put("Personal", R.drawable.tag_personal);
        put("Travel", R.drawable.tag_travel);
        put("Other", R.drawable.tag_other);
    }};

    public EntriesAdapter(List<Entry> entries) {
        this.entries = entries;
    }

    public void setEntries(List<Entry> ents) {
        this.entries = ents;
    }

    @NonNull
    @Override
    public EntriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_card_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntriesAdapter.ViewHolder viewHolder, int i) {
        Entry entry = entries.get(i);
        viewHolder.title.setText(entry.title);
        viewHolder.desc.setText(entry.description);
        viewHolder.date.setText(getDatePublishedString(entry.dateAdded));

/*        if(entry.category != null) {
            viewHolder.imgTag.setImageResource(imgTags.get(entry.category));
        }*/
    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Integer position;

        TextView title;
        TextView desc;
        TextView date;
        ImageView imgTag;

        private ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.txt_title);
            desc = v.findViewById(R.id.txt_desc);
            date = v.findViewById(R.id.txt_time);
            imgTag = v.findViewWithTag(R.id.img_tag);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private  String getDatePublishedString(long dateTime) {

        DateFormat utcFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        utcFormat.setTimeZone(TimeZone.getDefault());

        Date publishedAt = new Date(dateTime);
        Date currentDate = new Date();

        long diff = currentDate.getTime() - publishedAt.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays =
                (int) ((currentDate.getTime() - publishedAt.getTime()) / (1000 * 60 * 60 * 24));
        String publishedDate;

        if (diffInDays > 1) {
            if (diffInDays > 2) {
                publishedDate = utcFormat.format(publishedAt);
            } else {
                publishedDate = diffInDays + " days ago";
            }
        } else if (diffHours > 24) {
            publishedDate = diffHours + " hrs ago";
        } else {
            publishedDate = diffMinutes + " mins ago";
        }
        return publishedDate;
    }
}
