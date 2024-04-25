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
import com.archi.cosplay_planner.databinding.LMymaterialScreenBinding
import com.archi.cosplay_planner.databinding.LSettingScreenBinding
import java.util.Locale

class MyMaterial : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = MyMaterial.Handler(this)

    class Handler (private val context: Context) {
        fun onClickToCosplay(view: View) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToSettings(view: View) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickNewMaterial(view: View) {
            //
        }

        fun onClickMaterialBase(view: View) {
            val intent = Intent(context, MaterialBase::class.java)
            context.startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_mymaterial_screen)
        val binding = LMymaterialScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mmHandlers = handlers

    }


}



class MyMaterialViewModel() : ViewModel() {
}

