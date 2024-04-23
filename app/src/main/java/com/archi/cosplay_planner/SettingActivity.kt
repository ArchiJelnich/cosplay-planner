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
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
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
        spinner_theme.setSelection(0)

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
        spinner_language.setSelection(0)

        spinner_language.isGone = true
        text_language.isGone = true


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


// Функция для сохранения выбранного языка в SharedPreferences
fun saveLanguage(context: Context, languageCode: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putString("language", languageCode).apply()
}

// Функция для загрузки выбранного языка из SharedPreferences
fun loadLanguage(context: Context): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString("language", null)
}

// Функция для установки выбранного языка в приложении
fun setAppLocale(context: Context, myLocate: Locale) {
    Locale.setDefault(myLocate)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.locale = myLocate
    configuration.setLayoutDirection(myLocate)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

// Функция для перезапуска активити
fun restartActivity(activity: Activity) {
    val intent = Intent(activity, activity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.startActivity(intent)
    activity.finish()
}
