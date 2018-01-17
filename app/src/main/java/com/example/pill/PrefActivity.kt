package com.example.pill

import android.os.Bundle
import android.preference.PreferenceActivity



/**
 * Created by Дом on 17.01.2018.
 */
class PrefActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
    }
}