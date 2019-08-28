package com.example.newstoday;

import android.content.SearchRecentSuggestionsProvider;

public class SearchProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.SearchProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }

}
