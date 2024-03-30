package com.archi.cosplay_planner

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Costume
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_ROOM.ReposEvent
import com.archi.cosplay_planner.databinding.LMainEditScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditMainActivity : AppCompatActivity() {


    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
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


                GlobalScope.launch {

                    if (costumeDao.getByCharacter(character).size!=0)
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

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)





                }}



        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

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





    }




}











class EditMViewModel(var costume_id : Int, var costume_fandom : String, var costume_character: String, var costume_status: Int, var costume_progress: Int) : ViewModel() {
}

