package com.newsapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.newsapp.fragments.NewsFeedFragment;
import com.newsapp.utils.Constants;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    private final String language;
    private final String country;

    public CategoryPagerAdapter(@NonNull Fragment fragment, String language, String country) {
        super(fragment);
        this.language = language;
        this.country = country;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String category = Constants.CATEGORIES[position];
        return NewsFeedFragment.newInstance(category, language, country);
    }

    @Override
    public int getItemCount() {
        return Constants.CATEGORIES.length;
    }
}
