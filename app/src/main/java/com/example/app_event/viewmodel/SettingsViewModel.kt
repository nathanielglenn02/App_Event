package com.example.app_event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_event.data.ThemePreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: ThemePreferences) : ViewModel() {

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
}
