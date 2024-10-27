package com.example.app_event.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.app_event.data.ThemePreferences
import com.example.app_event.data.SettingPreferences // Tambahkan SettingPreferences
import com.example.app_event.data.dataStore
import com.example.app_event.data.reminderDataStore // Tambahkan reminderDataStore
import com.example.app_event.databinding.FragmentSettingBinding
import com.example.app_event.viewmodel.SettingsViewModel
import com.example.app_event.viewmodel.SettingsViewModelFactory
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themePreferences = ThemePreferences.getInstance(requireContext().dataStore)
        val settingPreferences = SettingPreferences.getInstance(requireContext().reminderDataStore) // Inisialisasi SettingPreferences
        val factory = SettingsViewModelFactory(themePreferences, settingPreferences)

        settingsViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        settingsViewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchDailyReminder.isChecked = isReminderActive

            if (isReminderActive) {
                val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                    .build()
                WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
            } else {
                WorkManager.getInstance(requireContext()).cancelAllWork()
            }
        }

        // Listener untuk mengubah tema
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        // Listener untuk mengubah Daily Reminder
        binding.switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveReminderSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
