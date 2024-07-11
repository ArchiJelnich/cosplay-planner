package com.archi.cosplay_planner.infra

import android.content.Context
import android.preference.PreferenceManager
import com.archi.cosplay_planner.R

fun loadTheme(context: Context): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString("themecode", "pink")
}

fun checkTheme(context: Context){
    if (loadTheme(context)=="blue")
    {
        context.setTheme(R.style.Theme_CosplayPlannerBlue)
    }
    else {
        context.setTheme(R.style.Theme_CosplayPlannerPink)
    }
}

fun saveTheme(context: Context, themecode: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putString("themecode", themecode).apply()
}