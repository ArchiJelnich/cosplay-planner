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
import com.archi.cosplay_planner.databinding.LNewDetailBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {




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

                if (InputCheckerText(d_n.text.toString()).second != 0)
                {
                    Toast.makeText(context, "Detail:" + InputCheckerText(d_n.text.toString()).first, Toast.LENGTH_SHORT).show()
                }



                if (InputCheckerText(d_n.text.toString()).second == 0)
                    {


                    if (d_id.text.toString().toInt()==-1) {

                        val db: AppDatabase = AppDatabase.getInstance(context)
                        val detailDao = db.DetailDao()
                        val costumeDao = db.CostumeDao()

                        GlobalScope.launch {
                            Log.v("MYDEBUG", "In corut")

                            val detailToAd = Detail(
                                detailID = 0,
                                detail = InputCheckerText(d_n.text.toString()).first,
                                type = type,
                                progress = progress,
                                costumeID = c_id.text.toString().toInt()
                            )
                            detailDao.insertAll(detailToAd)
                            val costume = detailToAd.costumeID?.let { costumeDao.getByID(it) }
                            Log.d("MyDebug", "Here:" + detailDao.getAll())


                            val intent = Intent(context, EditMainActivity::class.java)
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
                                detail = InputCheckerText(d_n.text.toString()).first,
                                type = type,
                                progress = progress,
                                costumeID = c_id.text.toString().toInt()
                            )
                            detailDao.updateDetail(detailToUp)
                            val costume = detailToUp.costumeID?.let { costumeDao.getByID(it) }
                            Log.d("MyDebug", "Here:" + detailDao.getAll())
                            val intent = Intent(context, EditMainActivity::class.java)
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




            }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_new_detail)
        val costume_id = intent.extras?.get("costume_id") as Int
        Log.d("MyLog", "Costume_id " + costume_id + " " + costume_id::class.java.typeName)
        val binding = LNewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = DetailViewModel(getString(R.string.str_Cosplay_details), costume_id)


        binding.viewModel = current_vm
        binding.dHandlers = handlers


        val spinner: Spinner = findViewById(R.id.d_t)
        ArrayAdapter.createFromResource(
            this,
            R.array.D_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
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
            var detail =  intent.extras?.get("detail") as Detail
            current_vm = DetailViewModel(detail.detail.toString(), detail.costumeID.toString().toInt())
            binding.viewModel = current_vm
            val d_t = findViewById<View>(R.id.d_t) as Spinner
            val d_p = findViewById<View>(R.id.d_p) as Spinner
            val d_id = findViewById<View>(R.id.d_id) as TextView
            detail.type?.let { d_t.setSelection(it) }
            detail.progress?.let { d_p.setSelection(it) }
            d_id.text = detail.detailID.toString()


        }


    }


        }

class DetailViewModel(var detail_name: String, var costume_id: Int ) : ViewModel() {
}