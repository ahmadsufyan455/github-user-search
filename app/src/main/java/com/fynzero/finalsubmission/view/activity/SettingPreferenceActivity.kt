package com.fynzero.finalsubmission.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.SwitchPreference
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.view.fragment.PreferenceFragment
import kotlinx.android.synthetic.main.activity_setting_preference.*

class SettingPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, PreferenceFragment())
            .commit()

        btn_back.setOnClickListener { finish() }
    }
}