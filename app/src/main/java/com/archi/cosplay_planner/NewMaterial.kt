package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_Infra.IntCheckerNum
import com.archi.cosplay_planner.P_Infra.sort_value_from_date
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Detail
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.P_ROOM.MaterialsPlanned
import com.archi.cosplay_planner.P_ROOM.MaterialsPlannedDao
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial
import com.archi.cosplay_planner.P_ROOM.ReposBPMaterial
import com.archi.cosplay_planner.databinding.LNewDetailBinding
import com.archi.cosplay_planner.databinding.LNewMaterialBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewMaterial : AppCompatActivity() {



    private lateinit var db: AppDatabase
    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {

            val nm_n = (view.rootView as View).findViewById<View>(R.id.nm_n) as Spinner
            val nm_unit = (view.rootView as View).findViewById<View>(R.id.nm_unit) as EditText
            val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as TextView
            val d_id = (view.rootView as View).findViewById<View>(R.id.d_id) as TextView
            val mp_id = (view.rootView as View).findViewById<View>(R.id.mp_id) as TextView


            if (nm_unit.text.isEmpty()) {
                nm_unit.setText("0")
            }

            if (!IntCheckerNum(nm_unit.text.toString()))
            {
                Toast.makeText(context, "How...:", Toast.LENGTH_SHORT).show()
                nm_unit.setText("0")
            }


            val db: AppDatabase = AppDatabase.getInstance(context)
            val MaterialsPlannedDao = db.MaterialsPlannedDao()




            if (mp_id.text.toString().toInt()==-1)
            {



            GlobalScope.launch {

            var checkedMaterial =  MaterialsPlannedDao.getByMaterialandDetail(c_id.text.toString().toInt(), d_id.text.toString().toInt())

                if (checkedMaterial.size!=0)
                {


                    val q = checkedMaterial[0].quantity?.plus((nm_unit.text.toString().toInt()))
                    Log.d("MyLog", "checkedMaterial " + checkedMaterial[0] )
                    Log.d("MyLog", "q " + q )
                    Log.d("MyLog", "checkedMaterial[0].quantity " + checkedMaterial[0].quantity )
                    Log.d("MyLog", "nm_unit.text.toString().toInt() " + nm_unit.text.toString().toInt() )

                    val materialPtoadd = MaterialsPlanned(
                        materialPlannedID = checkedMaterial[0].materialPlannedID,
                        materialID = c_id.text.toString().toInt(),
                        quantity = q,
                        detailID = d_id.text.toString().toInt()
                    )

                    MaterialsPlannedDao.updateMaterialP(materialPtoadd)
                }

                else {
                    val materialPtoadd = MaterialsPlanned(
                        materialPlannedID = 0,
                        materialID = c_id.text.toString().toInt(),
                        quantity = nm_unit.text.toString().toInt(),
                        detailID = d_id.text.toString().toInt()
                    )
                    MaterialsPlannedDao.insertAll(materialPtoadd)

                }

                Log.d("MyLog","Q " + nm_unit.text.toString())

                var DetailDao = db.DetailDao()
                var detail = DetailDao.getByID(d_id.text.toString().toInt())



                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("costume_id", detail[0].costumeID)
                intent.putExtra("detail", detail[0])
                intent.putExtra("edit_flag", 1)
                context.startActivity(intent)



            }}
            else {

            }
                GlobalScope.launch {

                    val materialPtoadd = MaterialsPlanned(
                        materialPlannedID = mp_id.text.toString().toInt(),
                        materialID = c_id.text.toString().toInt(),
                        quantity = nm_unit.text.toString().toInt(),
                        detailID = d_id.text.toString().toInt()
                    )
                    MaterialsPlannedDao.updateMaterialP(materialPtoadd)

                    Log.d("MyLog","Q " + nm_unit.text.toString())


                    var DetailDao = db.DetailDao()
                    var detail = DetailDao.getByID(d_id.text.toString().toInt())

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("costume_id", detail[0].costumeID)
                    intent.putExtra("detail", detail[0])
                    intent.putExtra("edit_flag", 1)
                    context.startActivity(intent)
            }



        }

        fun hideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_Cosplayplanner_blue)
        }
        else {
            setTheme(R.style.Theme_Cosplayplanner_pink)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_new_material)
        val detailID = intent.extras?.get("detail_id") as Int
        val binding = LNewMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var material_id = 0
        var current_vm = NewMaterialViewModel(material_id)
        binding.viewModel = current_vm
        binding.nmHandlers = handlers
        val edit_flag =  intent.extras?.get("edit_flag") as Int
        db = AppDatabase.getInstance(applicationContext)
        val materialDao = db.MaterialsDao()
        var repos = ReposBMaterial(materialDao)
        val spinner_name: Spinner = findViewById(R.id.nm_n)
        val materials_names = mutableListOf<String>()
        val material_unit = mutableListOf<String>()
        val d_id: TextView = findViewById(R.id.d_id)
        val mp_id: TextView = findViewById(R.id.mp_id)
        d_id.text = detailID.toString()
        val text_unit: TextView = findViewById(R.id.nm_unit_t)
        val c_id: TextView = findViewById(R.id.c_id)
        val text_unit_number: TextView = findViewById(R.id.nm_unit)



        for (material in repos.allMaterial) {
            material.material?.let { materials_names.add(it) }
            material.unit?.let { material_unit.add(it) }
        }

        lateinit var material_name : List<String>

        if (edit_flag==1)
        {
            var current_material = intent.extras?.get("material") as MaterialsPlanned
            Log.d("MyLog", "material" + current_material)

            //current_material.materialID?.let { spinner_name.setSelection(it) }
            spinner_name.isEnabled = false



            if (current_material.materialID!=null)
            {
                text_unit.text = materialDao.getUnitByID(current_material.materialID!!)[0]
                Log.d("MyLog", "materialDao.getUnitByID(current_material.materialID!!)[0] " + materialDao.getUnitByID(current_material.materialID!!)[0])
                material_name = materialDao.getNameByID(current_material.materialID!!)
            }


            c_id.text = current_material.materialID.toString()
            d_id.text = current_material.detailID.toString()
            mp_id.text = current_material.materialPlannedID.toString()
            text_unit_number.text = current_material.quantity.toString()
        }


                var adapter_name = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    materials_names)

                if (edit_flag==1)
                {
                    adapter_name = ArrayAdapter(this, android.R.layout.simple_spinner_item, material_name )
                }



            adapter_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_name.adapter = adapter_name

        if (edit_flag ==0) {
            spinner_name?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    c_id.text=materialDao.getIDbyName(spinner_name.selectedItem.toString())[0].toString()
                    text_unit.text=materialDao.getUnitByID(c_id.text.toString().toInt())[0].toString()

                    //text_unit.text = material_unit[position]
                    //c_id.text = (position + 1).toString()
                }

            }
        }










    }



}

class NewMaterialViewModel(var material_id : Int) : ViewModel() {}