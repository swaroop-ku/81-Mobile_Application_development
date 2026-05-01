package com.newsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.newsapp.R;
import com.newsapp.adapters.CategoryPagerAdapter;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CategoryPagerAdapter pagerAdapter;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(requireContext());
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        setupCategoryTabs();
        return view;
    }

    private void setupCategoryTabs() {
        String language = sessionManager.getPreferredLanguage();
        String country = sessionManager.getPreferredCountry();

        pagerAdapter = new CategoryPagerAdapter(this, language, country);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String category = Constants.CATEGORIES[position];
            tab.setText(capitalize(category));
        }).attach();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
