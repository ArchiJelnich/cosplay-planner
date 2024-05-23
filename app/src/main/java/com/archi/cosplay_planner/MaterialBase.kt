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
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat.getApplicationLocales
import androidx.core.os.LocaleListCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial_filter
import com.archi.cosplay_planner.P_ROOM.ReposEvent
import com.archi.cosplay_planner.databinding.LBasematerialScreenBinding
import com.archi.cosplay_planner.databinding.LMymaterialScreenBinding
import com.archi.cosplay_planner.databinding.LSettingScreenBinding
import kotlinx.coroutines.launch
import java.util.Locale

class MaterialBase : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = MaterialBase.Handler(this)

    class Handler (private val context: Context) {

        fun onClickNewMaterial(view: View) {
            val intent = Intent(context, NewBMaterial::class.java)
            intent.putExtra("edit_flag", 0)
            context.startActivity(intent)
        }

        fun onClickFilterIcon(view: View) {
            val t_filter = (view.rootView as View).findViewById<EditText>(R.id.text_filter)
            var t_filter_text = t_filter.text.toString()

            if (InputCheckerText(t_filter_text).second==1)
            {
                t_filter_text = ""
            }
            else
            {
                t_filter_text = InputCheckerText(t_filter_text).first
            }



            var db = AppDatabase.getInstance(context)
            val materialDao = db.MaterialsDao()
            val recyclerView: RecyclerView = (view.rootView as View).findViewById(R.id.recyclerViewEvent)
            recyclerView.layoutManager = LinearLayoutManager(context)

            if (t_filter_text=="")
            {
                var repos = ReposBMaterial(materialDao)
                var adapter = MaterialBaseRV(repos.allMaterial)
                recyclerView.adapter = adapter

                adapter.onBMaterialClickListener = { position, material ->
                    val intent = Intent(context, NewBMaterial::class.java)
                    intent.putExtra("material", material)
                    intent.putExtra("edit_flag", 1)
                    context.startActivity(intent)
                }
                adapter.onBMaterialLongClickListener = { position, material ->
                    Log.v("MyLog", "clicked " + position)
                    true


                }
            }
            else
            {
                var repos = ReposBMaterial_filter(materialDao, t_filter_text)
                var adapter = MaterialBaseRV(repos.filtereMaterial)
                recyclerView.adapter = adapter

                adapter.onBMaterialClickListener = { position, material ->
                    val intent = Intent(context, NewBMaterial::class.java)
                    intent.putExtra("material", material)
                    intent.putExtra("edit_flag", 1)
                    context.startActivity(intent)
                }
                adapter.onBMaterialLongClickListener = { position, material ->
                    Log.v("MyLog", "clicked " + position)
                    true


                }
            }



            }


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_basematerial_screen)
        db = AppDatabase.getInstance(applicationContext)
        val binding = LBasematerialScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mbHandlers = handlers

        val materialDao = db.MaterialsDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvent)
        recyclerView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {

            var repos = ReposBMaterial(materialDao)
            val adapter = MaterialBaseRV(repos.allMaterial)
            recyclerView.adapter = adapter

            adapter.onBMaterialClickListener = { position, material ->
                val intent = Intent(this@MaterialBase, NewBMaterial::class.java)
                intent.putExtra("material", material)
                intent.putExtra("edit_flag", 1)
                this@MaterialBase.startActivity(intent)
            }
            adapter.onBMaterialLongClickListener = { position, material ->
                Log.v("MyLog", "clicked " + position)

               /* val builder = AlertDialog.Builder(this@MaterialBase)
                builder.setTitle(R.string.str_delete_event)
                val message = getString(R.string.str_delete_event_message)
                builder.setMessage(message + " " + event.event)

                builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //Log.v("MyLog", "Yes")
                    eventDao.delete(event)
                    //adapter.notifyItemRemoved(position)
                    repos = ReposEvent(eventDao, 0)
                    val newAdapter = EventRV(repos.allEvents, 0)
                    recyclerView.adapter = newAdapter

                }

                builder.setNegativeButton(R.string.str_no) { dialog, which ->
                    //Log.v("MyLog", "No")
                }

                builder.show()
                */
                true


            }





        }



    }


}



class MaterialBaseViewModel() : ViewModel() {
}

