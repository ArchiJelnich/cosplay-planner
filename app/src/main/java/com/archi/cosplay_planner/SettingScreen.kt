package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.databinding.SettingScreenBinding
import com.archi.cosplay_planner.infra.checkTheme
import com.archi.cosplay_planner.infra.loadLanguage
import java.util.Locale
import com.archi.cosplay_planner.infra.loadTheme
import com.archi.cosplay_planner.infra.saveLanguage
import com.archi.cosplay_planner.infra.saveTheme
import com.archi.cosplay_planner.infra.setAppLocale

class SettingScreen : AppCompatActivity() {

    private val handlers = Handler(this)

    class Handler (private val context: Context) {
        fun onClickToCosplay(view: View) {
            val intent = Intent(context, CosplayScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventScreen::class.java)
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

        checkTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_screen)
        val binding = SettingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sHandlers = handlers

        val spinner_theme: Spinner = findViewById(R.id.s_t)
        val spinner_language: Spinner = findViewById(R.id.s_l)

        ArrayAdapter.createFromResource(
            this,
            R.array.S_theme,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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


                if (loadTheme(this@SettingScreen) !=selectedTheme)
                {
                    saveTheme(this@SettingScreen, selectedTheme)
                    if (selectedTheme=="pink") {
                        setTheme(R.style.Theme_CosplayPlannerPink)
                    }
                    else setTheme(R.style.Theme_CosplayPlannerBlue)
                    recreate()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        ArrayAdapter.createFromResource(
            this,
            R.array.S_language,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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


                if (loadLanguage(this@SettingScreen)!=selectedLanguage)
                {
                    setAppLocale(this@SettingScreen, selectedLanguage)
                    saveLanguage(this@SettingScreen, selectedLanguage)
                    recreate()
                }

                //recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }



    }


}



class SettingsViewModel() : ViewModel() {
}