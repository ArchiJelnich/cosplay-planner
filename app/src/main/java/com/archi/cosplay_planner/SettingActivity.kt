package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat.getApplicationLocales
import androidx.core.os.LocaleListCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.databinding.LSettingScreenBinding
import java.util.Locale

class SettingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = SettingActivity.Handler(this)

    class Handler (private val context: Context) {
        fun onClickToCosplay(view: View) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToMMaterial(view: View) {
            val intent = Intent(context, MaterialBase::class.java)
            context.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {

        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_Cosplayplanner_blue)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_setting_screen)
        val binding = LSettingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sHandlers = handlers

        val spinner_theme: Spinner = findViewById(R.id.s_t)
        val spinner_language: Spinner = findViewById(R.id.s_l)
        val text_language: TextView = findViewById(R.id.t_l)

        ArrayAdapter.createFromResource(
            this,
            R.array.S_theme,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_theme.adapter = adapter
        }

        if (loadTheme(this) =="pink")
        {
            spinner_theme.setSelection(0)
        }
        else {
            spinner_theme.setSelection(1)
        }

        spinner_theme.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTheme = when (position)
                {
                    0 -> "pink"
                    else -> "blue"
                }


                if (loadTheme(this@SettingActivity) !=selectedTheme)
                {

                    saveTheme(this@SettingActivity, selectedTheme)
                    if (selectedTheme=="pink") {
                        setTheme(R.style.Theme_Cosplayplanner)
                    }
                    else setTheme(R.style.Theme_Cosplayplanner_blue)
                    recreate()
                }

                //recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        ArrayAdapter.createFromResource(
            this,
            R.array.S_language,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_language.adapter = adapter
        }

        if (loadLanguage(this)=="ru")
        {
            spinner_language.setSelection(0)
        }
        else {
            spinner_language.setSelection(1)
        }


        spinner_language.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = when (position)
                {
                    0 -> "ru"
                        else -> "en"
                }


                if (loadLanguage(this@SettingActivity)!=selectedLanguage)
                {
                    setAppLocale(this@SettingActivity, selectedLanguage)
                    saveLanguage(this@SettingActivity, selectedLanguage)
                    recreate()
                }

                //recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //spinner_language.setSelection(0)
        //spinner_language.isGone = true
        //text_language.isGone = true


        /*Log.d("MyLog", "s " + loadLanguage(applicationContext) + " and c " +  getResources().getConfiguration().getLocales().get(0))
        saveLanguage(applicationContext, "ru_RU")
        var ruLocale = Locale("ru","RU")
        setAppLocale(applicationContext, ruLocale)
        //restartActivity(this)
        Log.d("MyLog", "s " + loadLanguage(applicationContext) + " and c " +  getResources().getConfiguration().getLocales().get(0))
*/

       // if (savedLanguage != currentLanguage) {
       //     if (savedLanguage != null) {
       //         saveLanguage(applicationContext, savedLanguage)
       //     }
       //     if (savedLanguage != null) {
       //         setAppLocale(applicationContext, savedLanguage)
       //     }
       //     restartActivity(this)
       // }





    }


}



class SettingsViewModel() : ViewModel() {
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
    /*val locale = Locale(myLocate)
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.createConfigurationContext(config)
    } else {
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    saveLanguage(context, myLocate)
*/

    val locale = Locale(myLocate)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.locale = locale
    configuration.setLayoutDirection(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)

}


fun saveTheme(context: Context, themecode: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putString("themecode", themecode).apply()
}

fun loadTheme(context: Context): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString("themecode", "pink")
}