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
import com.archi.cosplay_planner.databinding.LBasematerialScreenBinding
import com.archi.cosplay_planner.databinding.LMymaterialScreenBinding
import com.archi.cosplay_planner.databinding.LSettingScreenBinding
import java.util.Locale

class MaterialBase : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = MaterialBase.Handler(this)

    class Handler (private val context: Context) {

        fun onClickNewMaterial(view: View) {
            //
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_basematerial_screen)
        val binding = LBasematerialScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mbHandlers = handlers

    }


}



class MaterialBaseViewModel() : ViewModel() {
}

