package com.e.vidihub.provider

import android.content.SearchRecentSuggestionsProvider

class VideoSuggestionProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.e.vidihub.provider.VideoSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }

}