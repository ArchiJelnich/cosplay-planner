package com.archi.cosplay_planner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Costume
import com.archi.cosplay_planner.P_ROOM.ReposDetail
import com.archi.cosplay_planner.P_ROOM.ReposEvent
import com.archi.cosplay_planner.databinding.LMainEditScreenBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class EditMainActivity : AppCompatActivity() {


    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
        @OptIn(DelicateCoroutinesApi::class)
        fun onClickAdd(view: View) {
            val c_f = (view.rootView as View).findViewById<View>(R.id.c_f) as EditText
            val c_c = (view.rootView as View).findViewById<View>(R.id.c_c) as EditText
            val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as EditText
            val c_p = (view.rootView as View).findViewById<View>(R.id.c_p) as TextView
            val c_s = (view.rootView as View).findViewById<View>(R.id.c_s) as Spinner
            val statuses = context.resources.getStringArray(R.array.C_status)
            var status = 0

            when(c_s.getSelectedItem().toString()){
                statuses[0] ->  status = 0;
                statuses[1] ->  status = 1;
                statuses[2] ->  status = 2;
            }

            if (c_f.text.isEmpty()) {
                c_f.setText(R.string.str_New_fandom)
            }

            if (c_c.text.isEmpty()) {
                c_c.setText(R.string.str_New_Char)
            }

            if (InputCheckerText(c_f.text.toString()).second != 0)
            {
                Toast.makeText(context, "Fandom:" + InputCheckerText(c_f.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (InputCheckerText(c_c.text.toString()).second != 0)
            {
                Toast.makeText(context, "Character:" + InputCheckerText(c_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }




            if ((InputCheckerText(c_f.text.toString()).second == 0) && (InputCheckerText(c_c.text.toString()).second)==0) {

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()
                val costume_id = c_id.text.toString()
                var character = InputCheckerText(c_c.text.toString()).first
                val detailDao = db.DetailDao()
                var repos = ReposDetail(detailDao, costume_id.toInt())




                GlobalScope.launch {

                withContext(Dispatchers.Main){
                if (repos.costume_progress == 100 && status == 0) {
                    //val builder = AlertDialog.Builder(context)
                    //builder.setTitle(R.string.str_change_status)
                    //builder.setCancelable(false)
                    //builder.setMessage(R.string.str_change_all_finished)
                    //builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //        status = 1

                    //}
                    //builder.setNegativeButton(R.string.str_no) { dialog, which ->
                        //Log.v("MyLog", "No")
                    //}
                    //builder.show()
                    var d_result = showAlertDialog(context, context.getString(R.string.str_change_status), context.getString(R.string.str_change_all_finished))
                    if (d_result)
                    {
                        status = 1
                    }

                }

                if (repos.costume_progress != 100 && status == 1) {
                    var d_result = showAlertDialog(context, context.getString(R.string.str_change_status), context.getString(R.string.str_change_not_all_finished))
                    if (d_result)
                    {
                        status = 0
                    }
                }}




                    if (costumeDao.getByCharacter(character).size!=0 && !costumeDao.getByCharacter(character).contains(c_id.text.toString().toInt()))
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                       // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    Log.v("MYDEBUG", "In corut")



                    val CostumeToUpdate = Costume(
                        costumeID = costume_id.toInt(),
                        fandom = InputCheckerText(c_f.text.toString()).first,
                        character = character,
                        status = status,
                        progress = repos.costume_progress
                    )

                    costumeDao.updateCostume(CostumeToUpdate)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }}







        }

        fun onClickAddPhoto(view: View){

         val REQUST_GALLERY = 101
         val activity = context as Activity
         val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activity.startActivityForResult(intent, REQUST_GALLERY)
        }

        fun onClickAddDetail(view: View){


            val c_f = (view.rootView as View).findViewById<View>(R.id.c_f) as EditText
            val c_c = (view.rootView as View).findViewById<View>(R.id.c_c) as EditText
            val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as EditText
            val c_p = (view.rootView as View).findViewById<View>(R.id.c_p) as TextView
            val c_s = (view.rootView as View).findViewById<View>(R.id.c_s) as Spinner
            val statuses = context.resources.getStringArray(R.array.C_status)
            var status = 0

            when(c_s.getSelectedItem().toString()){
                statuses[0] ->  status = 0;
                statuses[1] ->  status = 1;
                statuses[2] ->  status = 2;
            }

            if (c_f.text.isEmpty()) {
                c_f.setText(R.string.str_New_fandom)
            }

            if (c_c.text.isEmpty()) {
                c_c.setText(R.string.str_New_Char)
            }

            if (InputCheckerText(c_f.text.toString()).second != 0)
            {
                Toast.makeText(context, "Fandom:" + InputCheckerText(c_f.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (InputCheckerText(c_c.text.toString()).second != 0)
            {
                Toast.makeText(context, "Character:" + InputCheckerText(c_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }




            if ((InputCheckerText(c_f.text.toString()).second == 0) && (InputCheckerText(c_c.text.toString()).second)==0) {

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()
                val costume_id = c_id.text.toString()
                var character = InputCheckerText(c_c.text.toString()).first


                GlobalScope.launch {

                    if (costumeDao.getByCharacter(character).size!=0 && !costumeDao.getByCharacter(character).contains(c_id.text.toString().toInt()))
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                        // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }


                    Log.v("MYDEBUG", "In corut")

                    val CostumeToUpdate = Costume(
                        costumeID = costume_id.toInt(),
                        fandom = InputCheckerText(c_f.text.toString()).first,
                        character = character,
                        status = status,
                        progress = c_p.text.toString().toInt()
                    )
                    costumeDao.updateCostume(CostumeToUpdate)



                }}


            val costume_id = c_id.text.toString().toInt()
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("costume_id", costume_id)
            intent.putExtra("edit_flag", 0)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_Cosplayplanner_blue)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_main_edit_screen)
        val costume = intent.extras?.get("costume") as Costume

        val costume_id = costume.costumeID
        val costume_fandom = costume.fandom!!
        val costume_character = costume.character!!
        val costume_status = costume.status!!
        val costume_progress = costume.progress!!


        val binding = LMainEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = EditMViewModel(costume_id, costume_fandom, costume_character, costume_status, costume_progress)

        binding.viewModel = current_vm
        binding.ecHandlers = handlers

        val spinner: Spinner = findViewById(R.id.c_s)


        ArrayAdapter.createFromResource(
            this,
            R.array.C_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
        spinner.setSelection(costume_status)

        var db = AppDatabase.getInstance(applicationContext)
        val eventDao = db.EventsDao()
        val detailDao = db.DetailDao()
        val MaterialsPlannedDao = db.MaterialsPlannedDao()



        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            var repos = ReposEvent(eventDao, costume_id)
            //recyclerView.adapter = EventRV(repos.allEvents, 0)
            val adapter = EventRV(repos.filteredEvents, costume_id)
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@EditMainActivity)
            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, event ->

                val intent = Intent(this@EditMainActivity, EditEventActivity::class.java)
                intent.putExtra("event", event)
                this@EditMainActivity.startActivity(intent)
            }


        }

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            var repos = ReposDetail(detailDao, costume_id)
            //recyclerView.adapter = EventRV(repos.allEvents, 0)
            val adapter = DetailRV(repos.filteredDetails)
            val recyclerView: RecyclerView = findViewById(R.id.recyclerViewD)
            recyclerView.layoutManager = LinearLayoutManager(this@EditMainActivity)
            recyclerView.adapter = adapter

            adapter.onDetailClickListener = { position, detail ->


                val intent = Intent(this@EditMainActivity, DetailActivity::class.java)

                intent.putExtra("costume_id", costume_id)


                intent.putExtra("edit_flag", 1)
                intent.putExtra("detail", detail)
                this@EditMainActivity.startActivity(intent)
            }

            adapter.onDetailLongClickListener = {position, detail ->
                val builder = AlertDialog.Builder(this@EditMainActivity)


                builder.setTitle(R.string.str_delete_detail)
                val message = getString(R.string.str_delete_detail_message)
                builder.setMessage(message + " " + detail.detail)

                builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //Log.v("MyLog", "Yes")
                    detailDao.delete(detail)
                    MaterialsPlannedDao.deleteByDetail(detail.detailID)



                    //adapter.notifyItemRemoved(position)
                    repos = ReposDetail(detailDao, costume_id)
                    val newAdapter = DetailRV(repos.filteredDetails)
                    recyclerView.adapter = newAdapter

                }

                builder.setNegativeButton(R.string.str_no) { dialog, which ->
                    //Log.v("MyLog", "No")
                }

                builder.show()
                true
            }


        }



    }




}











class EditMViewModel(var costume_id : Int, var costume_fandom : String, var costume_character: String, var costume_status: Int, var costume_progress: Int) : ViewModel() {
}

suspend fun showAlertDialog(context: Context, mess : String, title : String): Boolean {
    return suspendCoroutine { continuation ->
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(mess)
        builder.setPositiveButton(R.string.str_yes) { dialog, which ->
            continuation.resume(true)
        }
        builder.setNegativeButton(R.string.str_no) { dialog, which ->
            continuation.resume(false)
        }
        builder.setOnCancelListener {
            continuation.resume(false)
        }
        builder.show()
        Log.v("MYDEBUG", "Corrrr")
    }
}