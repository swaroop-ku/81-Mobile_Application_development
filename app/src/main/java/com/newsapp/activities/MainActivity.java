package com.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newsapp.R;
import com.newsapp.fragments.BookmarksFragment;
import com.newsapp.fragments.HomeFragment;
import com.newsapp.fragments.LanguageFragment;
import com.newsapp.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    private FloatingActionButton fabSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottom_nav);
        fabSearch = findViewById(R.id.fab_search);

        // Load default fragment
        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;
            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
                setTitle(R.string.nav_home);
            } else if (id == R.id.nav_language) {
                fragment = new LanguageFragment();
                setTitle(R.string.nav_language);
            } else if (id == R.id.nav_bookmarks) {
                fragment = new BookmarksFragment();
                setTitle(R.string.nav_bookmarks);
            } else if (id == R.id.nav_settings) {
                fragment = new SettingsFragment();
                setTitle(R.string.nav_settings);
            }
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });

        fabSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Profile icon in toolbar
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
