package com.example.johnseremba.journalapp.ui.dashboard;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnseremba.journalapp.R;
import com.example.johnseremba.journalapp.data.AppDatabase;
import com.example.johnseremba.journalapp.data.Entry;
import com.example.johnseremba.journalapp.data.JournalDatabase;
import com.example.johnseremba.journalapp.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private ImageView profilePic;
    private TextView userName;
    private TextView userEmail;
    private RecyclerView mRecyclerView;
    private EntriesAdapter mEntriesAdapter;
    private int mEntriesCount = 0;

    private FirebaseUser mCurrentUser;
    private MainActivityViewModel mViewModel;
    private AppDatabase mDatabase;
    private TextView noContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        profilePic = (ImageView) header.findViewById(R.id.img_profile);
        userName = (TextView) header.findViewById(R.id.txt_name);
        userEmail = (TextView) header.findViewById(R.id.txt_email);

        noContent = (TextView) findViewById(R.id.txt_no_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.entries_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Entry> dummyEntries = new ArrayList<Entry>(){{
            add(new Entry("Watch a Movie", "Today I'm planning to go out and watch a movie", "Entertainment", new Date().getTime()));
            add(new Entry("Go Swimming", "I'm going to go swimming one of these days", "Other", new Date().getTime()));
            add(new Entry("Lean new stuff", "Identify some new languages to learn", "Business", new Date().getTime()));
        }};
        mEntriesCount = dummyEntries.size();

        mEntriesAdapter = new EntriesAdapter(dummyEntries);
        mRecyclerView.setAdapter(mEntriesAdapter);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        initializeUserProfile();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getApplicationContext(), NewEntryActivity.class);
                startActivity(newActivity);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mDatabase = JournalDatabase.getInstance(getApplicationContext());
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getEntries().observe(this, new Observer<List<Entry>>() {
            @Override
            public void onChanged(@Nullable List<Entry> entries) {
                // Do something with the data
                if (entries != null) {
                    mEntriesCount = entries.size();
                    toggleNoContentVisibility();
                    mEntriesAdapter.setEntries(entries);
                }
            }
        });
        new GetEntries().execute();
        mEntriesAdapter.notifyDataSetChanged();
    }

    private void toggleNoContentVisibility() {
        if (mEntriesCount > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
        }
    }

    private class GetEntries extends AsyncTask<Void, Void, Void> {
        List<Entry> mylist;
        @Override
        protected Void doInBackground(Void... voids) {
            mylist = mDatabase.entryDao().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mViewModel.setEntries(mylist);
        }
    }

    private void initializeUserProfile() {
        if (mCurrentUser != null) {
            Log.d("User", mCurrentUser.getDisplayName());
            userEmail.setText(mCurrentUser.getEmail());
            userName.setText(mCurrentUser.getDisplayName());
            Uri url = mCurrentUser.getPhotoUrl();
            Picasso.get().load(url)
                    .fit()
                    .into(profilePic);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser == null) {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
