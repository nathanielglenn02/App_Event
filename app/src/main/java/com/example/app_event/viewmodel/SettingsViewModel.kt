package com.example.app_event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.app_event.data.SettingPreferences
import com.example.app_event.data.ThemePreferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: ThemePreferences,
    private val settingPreferences: SettingPreferences
) : ViewModel() {

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
    fun getReminderSettings() = settingPreferences.getReminderSetting().asLiveData()

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveReminderSetting(isReminderActive)
        }
    }
}
