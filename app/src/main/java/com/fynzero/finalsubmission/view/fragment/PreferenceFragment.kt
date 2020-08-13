package com.fynzero.finalsubmission.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.notification.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var switchPreference: SwitchPreference
    private lateinit var language: Preference

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val alarmReceiver = AlarmReceiver()

        switchPreference =
            findPreference(resources.getString(R.string.notification))!!
        language = findPreference(resources.getString(R.string.language))!!

        switchPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                if (switchPreference.isChecked) {
                    activity?.let { alarmReceiver.cancelAlarm(it) }
                    Toast.makeText(activity, R.string.reminder_off, Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = false
                } else {
                    activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                    Toast.makeText(activity, R.string.reminder_on, Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = true
                }

                false
            }

        language.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            false
        }

    }
}