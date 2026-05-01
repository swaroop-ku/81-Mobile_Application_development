package com.newsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newsapp.R;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageFragment extends Fragment {

    private Spinner spinnerLanguage, spinnerCountry;
    private Button btnApply;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        sessionManager = new SessionManager(requireContext());
        spinnerLanguage = view.findViewById(R.id.spinner_language);
        spinnerCountry = view.findViewById(R.id.spinner_country);
        btnApply = view.findViewById(R.id.btn_apply);

        setupLanguageSpinner();
        setupCountrySpinner();

        btnApply.setOnClickListener(v -> applySettings());

        return view;
    }

    private void setupLanguageSpinner() {
        List<String> languageNames = new ArrayList<>(Constants.LANGUAGES.values());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, languageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set current selection
        String currentLang = sessionManager.getPreferredLanguage();
        List<String> langKeys = new ArrayList<>(Constants.LANGUAGES.keySet());
        int pos = langKeys.indexOf(currentLang);
        if (pos >= 0) spinnerLanguage.setSelection(pos);
    }

    private void setupCountrySpinner() {
        List<String> countryNames = new ArrayList<>(Constants.COUNTRIES.values());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, countryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        String currentCountry = sessionManager.getPreferredCountry();
        List<String> countryKeys = new ArrayList<>(Constants.COUNTRIES.keySet());
        int pos = countryKeys.indexOf(currentCountry);
        if (pos >= 0) spinnerCountry.setSelection(pos);
    }

    private void applySettings() {
        int langPos = spinnerLanguage.getSelectedItemPosition();
        int countryPos = spinnerCountry.getSelectedItemPosition();

        List<String> langKeys = new ArrayList<>(Constants.LANGUAGES.keySet());
        List<String> countryKeys = new ArrayList<>(Constants.COUNTRIES.keySet());

        String selectedLang = langKeys.get(langPos);
        String selectedCountry = countryKeys.get(countryPos);

        sessionManager.setPreferredLanguage(selectedLang);
        sessionManager.setPreferredCountry(selectedCountry);

        Toast.makeText(requireContext(), "Preferences saved! Refreshing news...", Toast.LENGTH_SHORT).show();

        // Navigate back to home to reload
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}
