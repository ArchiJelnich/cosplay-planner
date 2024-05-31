package com.archi.cosplay_planner

import android.R.attr.label
import android.R.attr.text
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_Infra.CreateReport
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial_filter
import com.archi.cosplay_planner.databinding.LBasematerialScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MaterialBase : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = MaterialBase.Handler(this)

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
            val intent = Intent(context, NewBMaterial::class.java)
            intent.putExtra("edit_flag", 0)
            context.startActivity(intent)
        }

        fun onClickLoad(view: View) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.str_material_planned)
            var db = AppDatabase.getInstance(context)
            var message = CreateReport(db)



            builder.setMessage(message)

            builder.setPositiveButton(R.string.str_copy) { dialog, which ->
                val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                val clip = ClipData.newPlainText("cosplay-planner",message)
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip)
                }

            }

            builder.show()
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
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_Cosplayplanner_blue)
        }
        else {
            setTheme(R.style.Theme_Cosplayplanner_pink)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_basematerial_screen)
        db = AppDatabase.getInstance(applicationContext)
        val binding = LBasematerialScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mbHandlers = handlers

        val materialDao = db.MaterialsDao()
        val materialPlannedDao = db.MaterialsPlannedDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvent)

        val divider = DividerItemDecoration(this@MaterialBase, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this@MaterialBase,R.drawable.divider)!!)
        recyclerView.addItemDecoration(divider)
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
                Log.v("MyLog", "clicked " + material.materialID)
                Log.v("MyLog", "find all " + materialPlannedDao.getAll())
                Log.v("MyLog", "find all in base" + materialDao.getAll())
                Log.v("MyLog", "find rewsult " + materialPlannedDao.getByMaterial(material.materialID))

                if (materialPlannedDao.getByMaterial(material.materialID).size!=0)
                {
                    val message_used = getString(R.string.str_delete_material_used)
                    Toast.makeText(this@MaterialBase, message_used, Toast.LENGTH_SHORT).show()
                }
                else {

                val builder = AlertDialog.Builder(this@MaterialBase)
                builder.setTitle(R.string.str_delete_material_base)
                val message = getString(R.string.str_delete_material_base)
                builder.setMessage(message + " " + material.material)

                builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //Log.v("MyLog", "Yes")
                    materialDao.delete(material)
                    //adapter.notifyItemRemoved(position)
                    repos = ReposBMaterial(materialDao)
                    val newAdapter = MaterialBaseRV(repos.allMaterial)
                    recyclerView.adapter = newAdapter

                }

                builder.setNegativeButton(R.string.str_no) { dialog, which ->
                    //Log.v("MyLog", "No")
                }

                builder.show()

                }
                true


            }





        }



    }


}



class MaterialBaseViewModel() : ViewModel() {
}

