package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_Infra.sort_value_from_date
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Detail
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.P_ROOM.Materials
import com.archi.cosplay_planner.P_ROOM.MaterialsDao
import com.archi.cosplay_planner.P_ROOM.ReposBMaterial
import com.archi.cosplay_planner.databinding.LBmaterialEditScreenBinding
import com.archi.cosplay_planner.databinding.LBmaterialRvBinding
import com.archi.cosplay_planner.databinding.LNewDetailBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewBMaterial : AppCompatActivity() {




    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {
            val m_n = (view.rootView as View).findViewById<View>(R.id.m_n) as EditText
            val m_u = (view.rootView as View).findViewById<View>(R.id.m_u) as EditText
            val m_id = (view.rootView as View).findViewById<View>(R.id.m_id) as EditText
            val db: AppDatabase = AppDatabase.getInstance(context)
            val materialDao = db.MaterialsDao()
            var getByName = materialDao.getByName(InputCheckerText(m_n.text.toString()).first)



            if (getByName.size!=0 && getByName[0]!=m_id.text.toString().toInt()) {
                Toast.makeText(context, "Material name not uniq", Toast.LENGTH_SHORT).show()
            } else {


                if (InputCheckerText(m_n.text.toString()).second != 0) {
                    Toast.makeText(
                        context,
                        "Material name:" + InputCheckerText(m_n.text.toString()).first,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (InputCheckerText(m_u.text.toString()).second != 0) {
                    Toast.makeText(
                        context,
                        "Material unit:" + InputCheckerText(m_u.text.toString()).first,
                        Toast.LENGTH_SHORT
                    ).show()
                }



                if (InputCheckerText(m_n.text.toString()).second == 0 && InputCheckerText(m_u.text.toString()).second == 0) {


                    if (m_id.text.toString().toInt() == -1) {


                        GlobalScope.launch {

                            val materialToAd = Materials(
                                materialID = 0,
                                material = InputCheckerText(m_n.text.toString()).first,
                                unit = InputCheckerText(m_u.text.toString()).first
                            )
                            materialDao.insertAll(materialToAd)
                            Log.d("MyLog", "insertAll")
                            val intent = Intent(context, MaterialBase::class.java)
                            context.startActivity(intent)


                        }


                    } else {


                        GlobalScope.launch {

                            val materialToAd = Materials(
                                materialID = m_id.text.toString().toInt(),
                                material = InputCheckerText(m_n.text.toString()).first,
                                unit = InputCheckerText(m_u.text.toString()).first
                            )

                            materialDao.updateMaterial(materialToAd)
                            Log.d("MyLog", "updateMaterial")
                            val intent = Intent(context, MaterialBase::class.java)
                            context.startActivity(intent)


                        }


                    }
                }


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
        setContentView(R.layout.l_bmaterial_edit_screen)
        val binding = LBmaterialEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var current_vm = NewBMaterialViewModel(getString(R.string.str_material_name), getString(R.string.str_material_unit), -1)

        binding.viewModel = current_vm
        binding.nbmHandlers = handlers

        val edit_flag =  intent.extras?.get("edit_flag") as Int


        Log.d ("MyLog", "My flag " + edit_flag)
        if (edit_flag==1)
        {
            var material =  intent.extras?.get("material") as Materials
            current_vm = NewBMaterialViewModel(material.material.toString(), material.unit.toString(), material.materialID)
            binding.viewModel = current_vm

        }


    }


}

class NewBMaterialViewModel(var material_name: String, var material_unit: String, var material_id: Int ) : ViewModel() {
}
