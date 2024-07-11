package com.archi.cosplay_planner.infra

import android.content.Context
import android.preference.PreferenceManager
import java.util.Locale

fun localeChecker(context: Context) {
    val language = loadLanguage(context)
    if (language!=null)
    {
        setAppLocale(context, language)
    }
}

fun saveLanguage(context: Context, languageCode: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putString("language", languageCode).apply()
}

fun loadLanguage(context: Context): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString("language", "eng")
}

fun setAppLocale(context: Context, myLocate: String) {
    val locale = Locale(myLocate)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.locale = locale
    configuration.setLayoutDirection(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)

}