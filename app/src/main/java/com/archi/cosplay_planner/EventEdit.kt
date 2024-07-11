package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.databinding.EventEditBinding
import com.archi.cosplay_planner.infra.checkTheme
import com.archi.cosplay_planner.infra.inputCheckerText
import com.archi.cosplay_planner.infra.sortValueFromDate
import com.archi.cosplay_planner.infra.stringToData
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Events
import com.archi.cosplay_planner.roomDatabase.Repos
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.archi.cosplay_planner.infra.loadTheme



class EditEventActivity : AppCompatActivity() {


    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {
            val e_n = (view.rootView as View).findViewById<View>(R.id.e_n) as EditText
            val e_p = (view.rootView as View).findViewById<View>(R.id.e_p) as EditText
            val e_t = (view.rootView as View).findViewById<View>(R.id.e_t) as Spinner
            val e_d = (view.rootView as View).findViewById<View>(R.id.datePicker1) as DatePicker
            val types = context.resources.getStringArray(R.array.Ev_Types)
            var type = 0
            val e_c = (view.rootView as View).findViewById<View>(R.id.e_c) as Spinner
            val e_id = (view.rootView as View).findViewById<View>(R.id.e_id) as EditText

            when(e_t.getSelectedItem().toString()){
                types[0] ->  type = 0
                types[1] ->  type = 1
                types[2] ->  type = 2
            }

            if (e_n.text.isEmpty()) {
                e_n.setText(R.string.str_Event_name)
            }

            if (e_p.text.isEmpty()) {
                e_p.setText(R.string.str_Event_place)
            }


            if (inputCheckerText(e_n.text.toString()).second != 0)
            {
                Toast.makeText(context, "Name:" + inputCheckerText(e_n.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (inputCheckerText(e_p.text.toString()).second != 0)
            {
                Toast.makeText(context, "Place:" + inputCheckerText(e_p.text.toString()).first, Toast.LENGTH_SHORT).show()
            }


            if ((inputCheckerText(e_p.text.toString()).second == 0) && (inputCheckerText(e_n.text.toString()).second)==0) {

                var db: AppDatabase = AppDatabase.getInstance(context)
                val eventDao = db.EventsDao()

                val date = e_d.dayOfMonth.toString()+"."+(e_d.month).toString()+"."+e_d.year.toString()
                var cosplay_id = e_c.getSelectedItem().toString()
                val event_id = e_id.text.toString()


                GlobalScope.launch {

                    db = AppDatabase.getInstance(context)
                    val costumeDao = db.CostumeDao()
                    val repos = Repos(costumeDao)
                    var names = repos.allCosplay.map { it.character to it.costumeID}.toMap()
                    names = names + Pair(context.getString(R.string.str_no), -1)
                    cosplay_id = names.getValue(cosplay_id).toString()

                    val EventToUpdate = Events(
                        eventID = event_id.toInt(),
                        event = inputCheckerText(e_n.text.toString()).first,
                        place = inputCheckerText(e_p.text.toString()).first,
                        date = date,
                        costumeID = cosplay_id.toInt(),
                        type = type,
                        dateSorted = sortValueFromDate(date)
                    )
                    eventDao.updateEvent(EventToUpdate)

                    val intent = Intent(context, EventScreen::class.java)
                    context.startActivity(intent)


                }}
}

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        fun onClickCalendar(view: View) {

            val e_d = (view.rootView as View).findViewById<View>(R.id.datePicker1) as DatePicker
            val date = e_d.dayOfMonth.toString()+"."+(e_d.month +1).toString()+"."+e_d.year.toString()
            val e_n = (view.rootView as View).findViewById<View>(R.id.e_n) as EditText
            val e_p = (view.rootView as View).findViewById<View>(R.id.e_p) as EditText
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val parsedDate = sdf.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate ?: Date()
            val fullEvent = e_n.text.toString() + " [" + e_p.text.toString() + "]"
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", calendar.timeInMillis)
            intent.putExtra("allDay", true)
            intent.putExtra("endTime", calendar.timeInMillis + 60 * 60 * 1000)
            intent.putExtra("title", fullEvent)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

        }

        fun onClickMap(view: View) {

            val e_p = (view.rootView as View).findViewById<View>(R.id.e_p) as EditText
            val uriString = "geo:0,0?q="+e_p.text.toString()
            val gmmIntentUri = Uri.parse(uriString)
            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            intent.setPackage("com.google.android.apps.maps")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }


        fun hideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        checkTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_edit)

        val event = intent.extras?.get("event") as Events

        val event_id = event.eventID
        val event_type = event.type!!
        val event_name = event.event!!
        val event_date = event.date!!
        val event_place = event.place!!
        val event_costume = event.costumeID!!

        val binding = EventEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val current_vm = EditEViewModel(event_type, event_name, event_place, event_date, event_costume, event_id)

        binding.viewModel = current_vm
        binding.eeHandlers = handlers

        val spinner: Spinner = findViewById(R.id.e_t)
        val spinner_costume: Spinner = findViewById(R.id.e_c)

        ArrayAdapter.createFromResource(
            this,
            R.array.Ev_Types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(event_type)

        val db: AppDatabase = AppDatabase.getInstance(this)
        val costumeDao = db.CostumeDao()
        GlobalScope.launch {
            val repos = Repos(costumeDao)
            var names = repos.allCosplay.map { it.character to it.costumeID}.toMap()
            names = names + Pair(getString(R.string.str_no), -1)
            val names_list = names.keys.toList()

            val adapter_sp = ArrayAdapter(
                this@EditEventActivity,
                android.R.layout.simple_spinner_item,
                names_list
            )

            adapter_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_costume.adapter = adapter_sp

            if (event_costume==-1)
            {
                spinner_costume.setSelection(names_list.size-1)
            }
            else {
               val founded_key = names.filterValues { it == event_costume }.keys
               val founded_key_arr = founded_key.toTypedArray()
               val founded_name = names_list.indexOf(founded_key_arr[0])
                spinner_costume.setSelection(founded_name)
            }

 }


        val datePicker: DatePicker = findViewById(R.id.datePicker1)
        val event_date_obj = stringToData(event_date)
        datePicker.updateDate(event_date_obj.year, event_date_obj.month, event_date_obj.day)

    }




}




class EditEViewModel(var event_type: Int, var event_name : String, var event_place : String, var event_date : String, var event_costume : Int, var event_id : Int) : ViewModel() {
}

