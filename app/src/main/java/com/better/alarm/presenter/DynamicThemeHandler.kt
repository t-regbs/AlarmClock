package com.better.alarm.presenter

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.better.alarm.R
import com.better.alarm.alert.AlarmAlertFullScreen

class DynamicThemeHandler(context: Context) {
    private val themeKey = "theme"
    private val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val synthwave = "synthwave"
    private val light = "light"
    private val dark = "dark"
    private val deusex = "deusex"

    init {
        when (sp.getString(themeKey, dark)) {
            light, dark, synthwave, deusex -> {
            }
            else -> {
                sp.edit().putString(themeKey, dark).apply()
            }
        }
    }

    fun defaultTheme(): Int = when (preference()) {
        light -> R.style.DefaultLightTheme
        dark -> R.style.DefaultDarkTheme
        synthwave -> R.style.DefaultSynthwaveTheme
        deusex -> R.style.DefaultDeusExTheme
        else -> R.style.DefaultDarkTheme
    }

    private fun preference(): String = sp.getString(themeKey, dark) ?: dark

    fun getIdForName(name: String): Int = when {
        preference() == light && name == AlarmAlertFullScreen::class.java.name -> R.style.AlarmAlertFullScreenLightTheme
        preference() == light && name == TimePickerDialogFragment::class.java.name -> R.style.TimePickerDialogFragmentLight
        preference() == dark && name == AlarmAlertFullScreen::class.java.name -> R.style.AlarmAlertFullScreenDarkTheme
        preference() == dark && name == TimePickerDialogFragment::class.java.name -> R.style.TimePickerDialogFragmentDark
        preference() == synthwave && name == AlarmAlertFullScreen::class.java.name -> R.style.AlarmAlertFullScreenSynthwaveTheme
        preference() == synthwave && name == TimePickerDialogFragment::class.java.name -> R.style.TimePickerDialogFragmentSynthwave
        preference() == deusex && name == AlarmAlertFullScreen::class.java.name -> R.style.AlarmAlertFullScreenDeusExTheme
        preference() == deusex && name == TimePickerDialogFragment::class.java.name -> R.style.TimePickerDialogFragmentDeusEx
        else -> defaultTheme()
    }
}
