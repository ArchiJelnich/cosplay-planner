package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
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
import com.archi.cosplay_planner.databinding.DetailEditNewBinding
import com.archi.cosplay_planner.infra.inputCheckerText
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Detail
import com.archi.cosplay_planner.roomDatabase.MaterialsPlanned
import com.archi.cosplay_planner.roomDatabase.MaterialsPlannedDao
import com.archi.cosplay_planner.roomDatabase.ReposBMaterial
import com.archi.cosplay_planner.roomDatabase.ReposBPMaterial
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailEditNew : AppCompatActivity() {



    private lateinit var db: AppDatabase
        private val handlers = Handlers(this)
        class Handlers  (private val context: Context) {
            fun onClickAdd(view: View) {
                val d_n = (view.rootView as View).findViewById<View>(R.id.d_n) as EditText
                val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as EditText
                val d_t = (view.rootView as View).findViewById<View>(R.id.d_t) as Spinner
                val d_p = (view.rootView as View).findViewById<View>(R.id.d_p) as Spinner
                val d_id = (view.rootView as View).findViewById<View>(R.id.d_id) as TextView
                val types = context.resources.getStringArray(R.array.D_type)
                val progresses = context.resources.getStringArray(R.array.C_status)
                var type = 0
                var progress = 0
                when (d_t.getSelectedItem().toString())
                {
                    types[0] -> type = 0
                    types[1] -> type = 1
                    types[2] -> type = 2
                    types[3] -> type = 3
                    types[4] -> type = 4
                    types[5] -> type = 5
                    types[6] -> type = 6

                }

                when (d_p.getSelectedItem().toString())
                {
                    progresses[0] -> progress = 0
                    progresses[1] -> progress = 1
                    progresses[2] -> progress = 2

                }

                if (d_n.text.isEmpty()) {
                    d_n.setText(R.string.str_Event_name)
                }

                if (inputCheckerText(d_n.text.toString()).second != 0)
                {
                    Toast.makeText(context, "Detail:" + inputCheckerText(d_n.text.toString()).first, Toast.LENGTH_SHORT).show()
                }



                if (inputCheckerText(d_n.text.toString()).second == 0)
                    {


                    if (d_id.text.toString().toInt()==-1) {

                        val db: AppDatabase = AppDatabase.getInstance(context)
                        val detailDao = db.DetailDao()
                        val costumeDao = db.CostumeDao()

                        GlobalScope.launch {
                            Log.v("MYDEBUG", "In corut")

                            val detailToAd = Detail(
                                detailID = 0,
                                detail = inputCheckerText(d_n.text.toString()).first,
                                type = type,
                                progress = progress,
                                costumeID = c_id.text.toString().toInt()
                            )
                            detailDao.insertAll(detailToAd)
                            val costume = detailToAd.costumeID?.let { costumeDao.getByID(it) }
                            Log.d("MyDebug", "Here:" + detailDao.getAll())


                            val intent = Intent(context, CosplayEditActivity::class.java)
                            intent.putExtra("costume", costume)
                            context.startActivity(intent)


                        }


                    }
                        else {

                        val db: AppDatabase = AppDatabase.getInstance(context)
                        val detailDao = db.DetailDao()
                        val costumeDao = db.CostumeDao()

                        GlobalScope.launch {
                            Log.v("MYDEBUG", "In corut")

                            val detailToUp = Detail(
                                detailID = d_id.text.toString().toInt(),
                                detail = inputCheckerText(d_n.text.toString()).first,
                                type = type,
                                progress = progress,
                                costumeID = c_id.text.toString().toInt()
                            )
                            detailDao.updateDetail(detailToUp)
                            val costume = detailToUp.costumeID?.let { costumeDao.getByID(it) }
                            Log.d("MyDebug", "Here:" + detailDao.getAll())
                            val intent = Intent(context, CosplayEditActivity::class.java)
                            intent.putExtra("costume", costume)
                            context.startActivity(intent)


                        }


                    }
                    }



            }

            fun hideKeyboard(view: View) {

                val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)

            }

            fun addMaterial(view: View){

                val d_id = (view.rootView as View).findViewById<View>(R.id.d_id) as TextView
                val detail_id = d_id.text.toString().toInt()
                val intent = Intent(context, MaterialPlanned::class.java)
                intent.putExtra("detail_id", detail_id)
                intent.putExtra("edit_flag", 0)
                intent.putExtra("material_id", -1)
                context.startActivity(intent)


            }


            }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_CosplayPlannerBlue)
        }
        else {
            setTheme(R.style.Theme_CosplayPlannerPink)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_edit_new)
        val costume_id = intent.extras?.get("costume_id") as Int


        val binding = DetailEditNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = DetailViewModel(getString(R.string.str_Cosplay_details), costume_id)
        db = AppDatabase.getInstance(applicationContext)

        binding.viewModel = current_vm
        binding.dHandlers = handlers


        val spinner: Spinner = findViewById(R.id.d_t)
        ArrayAdapter.createFromResource(
            this,
            R.array.D_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        val spinner_p: Spinner = findViewById(R.id.d_p)
        ArrayAdapter.createFromResource(
            this,
            R.array.C_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_p.adapter = adapter
        }

        val edit_flag =  intent.extras?.get("edit_flag") as Int
        Log.d ("MyLog", "My flag " + edit_flag)
        if (edit_flag==1)
        {
            Log.d ("MyLog", "We are editing")
            val lay_id : ImageView = findViewById(R.id.lay_id)

            val materialDao = db.MaterialsDao()
            if (materialDao.getAll().size!=0){
            lay_id.visibility = View.VISIBLE}

            val detail =  intent.extras?.get("detail") as Detail
            current_vm = DetailViewModel(detail.detail.toString(), detail.costumeID.toString().toInt())
            binding.viewModel = current_vm
            val d_t = findViewById<View>(R.id.d_t) as Spinner
            val d_p = findViewById<View>(R.id.d_p) as Spinner
            val d_id = findViewById<View>(R.id.d_id) as TextView
            detail.type?.let { d_t.setSelection(it) }
            detail.progress?.let { d_p.setSelection(it) }
            d_id.text = detail.detailID.toString()
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            val divider = DividerItemDecoration(this@DetailEditNew, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@DetailEditNew,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)

            recyclerView.layoutManager = LinearLayoutManager(this)

            lifecycleScope.launch {




                val materialPDao = db.MaterialsPlannedDao()

                val repos = ReposBPMaterial(materialPDao, detail.detailID)
                val repos_all = ReposBMaterial(materialDao)

                val adapter = MaterialPlannedRV(repos.materialsPlannedList, materialDao)

                recyclerView.adapter = adapter

               adapter.onBMaterialPClickListener = { position, material ->
                   onMaterialonBMaterialPClickListener(material, this@DetailEditNew, d_id)
               }
                adapter.onBMaterialPLongClickListener = { position, material ->
                    onMaterialonBMaterialPClickListenerLong(this@DetailEditNew, material, materialPDao, detail, repos_all, recyclerView, d_id)
                    true
                }





            }



        }


    }


        }

class DetailViewModel(var detail_name: String, var costume_id: Int ) : ViewModel() {
}

fun onMaterialonBMaterialPClickListener(material : MaterialsPlanned, context: Context, d_id: TextView)
{
    val intent = Intent(context, MaterialPlanned::class.java)
    intent.putExtra("material", material)
    intent.putExtra("edit_flag", 1)
    val detail_id = d_id.text.toString().toInt()
    intent.putExtra("detail_id", detail_id)
    context.startActivity(intent)
}

fun onMaterialonBMaterialPClickListenerLong(context: Context, material: MaterialsPlanned, materialPDao : MaterialsPlannedDao, detail : Detail, repos_all : ReposBMaterial, recyclerView : RecyclerView, d_id : TextView )
{
    val builder = AlertDialog.Builder(context)
    val db: AppDatabase = AppDatabase.getInstance(context)
    val materialDao = db.MaterialsDao()
    val message = context.getString(R.string.str_delete_material)
    builder.setMessage(message)
    builder.setPositiveButton(R.string.str_yes) { dialog, which ->
        //Log.v("MyLog", "Yes")
        materialPDao.deleteByMaterialPlannedID(material.materialPlannedID)
        val repos = ReposBPMaterial(materialPDao, detail.detailID)
        val adapter = MaterialPlannedRV(repos.materialsPlannedList, materialDao )
        recyclerView.adapter = adapter
        adapter.onBMaterialPClickListener = { position, material ->
            onMaterialonBMaterialPClickListener(material, context, d_id)
        }
        adapter.onBMaterialPLongClickListener = { position, material ->
            onMaterialonBMaterialPClickListenerLong(context, material, materialPDao, detail, repos_all, recyclerView, d_id)
            true
        }
    }
    builder.setNegativeButton(R.string.str_no) { dialog, which ->
    }
    builder.show()
}