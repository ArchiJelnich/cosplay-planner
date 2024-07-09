package com.archi.cosplay_planner

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.databinding.MaterialBaseScreenBinding
import com.archi.cosplay_planner.infra.CreateReport
import com.archi.cosplay_planner.infra.inputCheckerText
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Materials
import com.archi.cosplay_planner.roomDatabase.MaterialsDao
import com.archi.cosplay_planner.roomDatabase.MaterialsPlannedDao
import com.archi.cosplay_planner.roomDatabase.ReposBMaterial
import com.archi.cosplay_planner.roomDatabase.ReposBMaterialFilter
import kotlinx.coroutines.launch
import com.archi.cosplay_planner.infra.loadTheme



class MaterialBase : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val handlers = MaterialBase.Handler(this)

    class Handler (private val context: Context) {

        fun onClickToCosplay(view: View) {
            val intent = Intent(context, CosplayScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickToSettings(view: View) {
            val intent = Intent(context, SettingScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickNewMaterial(view: View) {
            val intent = Intent(context, MaterialBaseEditNew::class.java)
            intent.putExtra("edit_flag", 0)
            context.startActivity(intent)
        }

        fun onClickLoad(view: View) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.str_material_planned)
            val db = AppDatabase.getInstance(context)
            val message = CreateReport(db)



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

            t_filter_text = if (inputCheckerText(t_filter_text).second==1) {
                ""
            } else {
                inputCheckerText(t_filter_text).first
            }



            val db = AppDatabase.getInstance(context)
            val materialDao = db.MaterialsDao()
            val recyclerView: RecyclerView = (view.rootView as View).findViewById(R.id.recyclerViewEvent)
            recyclerView.layoutManager = LinearLayoutManager(context)

            if (t_filter_text=="")
            {
                val repos = ReposBMaterial(materialDao)
                val adapter = MaterialBaseRV(repos.allMaterial)
                recyclerView.adapter = adapter

                adapter.onBMaterialClickListener = { position, material ->
                    val intent = Intent(context, MaterialBaseEditNew::class.java)
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
                val repos = ReposBMaterialFilter(materialDao, t_filter_text)
                val adapter = MaterialBaseRV(repos.filterMaterial)
                recyclerView.adapter = adapter

                adapter.onBMaterialClickListener = { position, material ->
                    val intent = Intent(context, MaterialBaseEditNew::class.java)
                    intent.putExtra("material", material)
                    intent.putExtra("edit_flag", 1)
                    context.startActivity(intent)
                }
                adapter.onBMaterialLongClickListener = { position, material ->
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
            setTheme(R.style.Theme_CosplayPlannerBlue)
        }
        else {
            setTheme(R.style.Theme_CosplayPlannerPink)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.material_base_screen)
        db = AppDatabase.getInstance(applicationContext)
        val binding = MaterialBaseScreenBinding.inflate(layoutInflater)
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

            val repos = ReposBMaterial(materialDao)
            val adapter = MaterialBaseRV(repos.allMaterial)
            recyclerView.adapter = adapter

            adapter.onBMaterialClickListener = { position, material ->
                onMaterialBaseonBMaterialClickListener(material, this@MaterialBase)
            }
            adapter.onBMaterialLongClickListener = { position, material ->
                onMaterialBaseonBMaterialClickListenerLong(this@MaterialBase, materialPlannedDao, material, materialDao, recyclerView)
                true
            }





        }



    }


}



class MaterialBaseViewModel() : ViewModel() {
}

fun onMaterialBaseonBMaterialClickListener(material : Materials, context: Context){
    val intent = Intent(context, MaterialBaseEditNew::class.java)
    intent.putExtra("material", material)
    intent.putExtra("edit_flag", 1)
    context.startActivity(intent)
}

fun onMaterialBaseonBMaterialClickListenerLong(context: Context, materialPlannedDao : MaterialsPlannedDao, material : Materials, materialDao : MaterialsDao, recyclerView : RecyclerView){
    if (materialPlannedDao.getByMaterial(material.materialID).size!=0)
    {
        val message_used = context.getString(R.string.str_delete_material_used)
        Toast.makeText(context, message_used, Toast.LENGTH_SHORT).show()
    }
    else {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.str_delete_material_base)
        val message = context.getString(R.string.str_delete_material_base)
        builder.setMessage(message + " " + material.material)

        builder.setPositiveButton(R.string.str_yes) { dialog, which ->
            materialDao.delete(material)
            val repos = ReposBMaterial(materialDao)
            val adapter = MaterialBaseRV(repos.allMaterial)
            recyclerView.adapter = adapter
            adapter.onBMaterialClickListener = { position, material ->
                onMaterialBaseonBMaterialClickListener(material, context)
            }
            adapter.onBMaterialClickListener = { position, material ->
                onMaterialBaseonBMaterialClickListener(material, context)
            }
            adapter.onBMaterialLongClickListener = { position, material ->
                onMaterialBaseonBMaterialClickListenerLong(context, materialPlannedDao, material, materialDao, recyclerView)
                true
            }

        }

        builder.setNegativeButton(R.string.str_no) { dialog, which ->
        }

        builder.show()

    }
}