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
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.P_Infra.sort_value_from_date
import com.archi.cosplay_planner.P_Infra.string_to_data
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.CostumeDao
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.P_ROOM.Repos
import com.archi.cosplay_planner.databinding.LEventsEditScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


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
            val e_s = (view.rootView as View).findViewById<View>(R.id.e_s) as EditText
            val e_id = (view.rootView as View).findViewById<View>(R.id.e_id) as EditText

            when(e_t.getSelectedItem().toString()){
                types[0] ->  type = 0;
                types[1] ->  type = 1;
                types[2] ->  type = 2;
            }



           /* if (e_t.getSelectedItem().toString()==types[0] )
            {
                type = 0
            }
            if (e_t.getSelectedItem().toString()==types[1] )
            {
                type = 1
            }
            if (e_t.getSelectedItem().toString()==types[2] )
            {
                type = 2
            }*/


            if (e_n.text.isEmpty()) {
                e_n.setText(R.string.str_Event_name)
            }

            if (e_p.text.isEmpty()) {
                e_p.setText(R.string.str_Event_place)
            }


            if (InputCheckerText(e_n.text.toString()).second != 0)
            {
                Toast.makeText(context, "Name:" + InputCheckerText(e_n.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (InputCheckerText(e_p.text.toString()).second != 0)
            {
                Toast.makeText(context, "Place:" + InputCheckerText(e_p.text.toString()).first, Toast.LENGTH_SHORT).show()
            }


            if ((InputCheckerText(e_p.text.toString()).second == 0) && (InputCheckerText(e_n.text.toString()).second)==0) {
                //Toast.makeText(context, "Nice" + InputCheckerText(e_f.text.toString()).first.toString() + " " + InputCheckerText(e_c.text.toString()).first.toString(), Toast.LENGTH_SHORT).show()

                val db: AppDatabase = AppDatabase.getInstance(context)
                val eventDao = db.EventsDao()

                val date = e_d.dayOfMonth.toString()+"."+(e_d.month).toString()+"."+e_d.year.toString()
                var cosplay_id = e_c.getSelectedItem().toString()
                val event_id = e_id.text.toString()



                GlobalScope.launch {

                    val db: AppDatabase = AppDatabase.getInstance(context)
                    val costumeDao = db.CostumeDao()
                    val repos = Repos(costumeDao, 0)
                    var names = repos.allCosplay.map { it.character to it.costumeID}.toMap()
                    names = names + Pair(context.getString(R.string.str_no), -1)
                    cosplay_id = names.getValue(cosplay_id).toString()



                    Log.v("MYDEBUG", "In corut")

                    val EventToUpdate = Events(
                        eventID = event_id.toInt(),
                        event = InputCheckerText(e_n.text.toString()).first,
                        place = InputCheckerText(e_p.text.toString()).first,
                        date = date,
                        costumeID = cosplay_id.toInt(),
                        type = type,
                        steps = e_s.text.toString(),
                        date_sorted = sort_value_from_date(date)
                    )
                    eventDao.updateEvent(EventToUpdate)

                    val intent = Intent(context, EventActivity::class.java)
                    context.startActivity(intent)


                }}











                }

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        fun onClickCalendar(view: View) {


            val e_d = (view.rootView as View).findViewById<View>(R.id.datePicker1) as DatePicker
            var date = e_d.dayOfMonth.toString()+"."+(e_d.month +1).toString()+"."+e_d.year.toString()
            val e_n = (view.rootView as View).findViewById<View>(R.id.e_n) as EditText
            val e_p = (view.rootView as View).findViewById<View>(R.id.e_p) as EditText

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val parsedDate = sdf.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate ?: Date()
            var fullEvent = e_n.text.toString() + " [" + e_p.text.toString() + "]"

           // val millis = calendar.timeInMillis

            var intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", calendar.timeInMillis)
            intent.putExtra("allDay", true)
            //intent.putExtra("rule", "FREQ=YEARLY")
            intent.putExtra("endTime", calendar.timeInMillis + 60 * 60 * 1000)
            intent.putExtra("title", fullEvent)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

           // val intentUri = Uri.parse("content://com.android.calendar/time/$millis")
            //val intent = Intent(Intent.ACTION_EDIT, intentUri)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

           // context.startActivity(intent)



/*
            val calendarEvent: Calendar = Calendar.getInstance()
            var intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", calendarEvent.timeInMillis)
            intent.putExtra("allDay", true)
            intent.putExtra("rule", "FREQ=YEARLY")
            intent.putExtra("endTime", calendarEvent.timeInMillis + 60 * 60 * 1000)
            intent.putExtra("title", "Calendar Event")
            context.startActivity(intent)

*/













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


        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_events_edit_screen)

        val event = intent.extras?.get("event") as Events
        //val event_id = event.eventID

        /*var event_id = 0
        var event_type = 0
        var event_name = " "
        var event_date = " "
        var event_place = " "
        var event_steps = " "
        var event_costume = 0
*/

        val event_id = event.eventID
        val event_type = event.type!!
        val event_name = event.event!!
        val event_date = event.date!!
        val event_place = event.place!!
        val event_steps = event.steps!!
        val event_costume = event.costumeID!!
        val event_date_sortes = event.date_sorted!!
        val costume_name = getString(R.string.str_no)

        Log.v("MyLogSpinner", "string event_costume" + event_costume)

        val binding = LEventsEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = EditEViewModel(event_type, event_name, event_place, event_date, event_costume, event_steps, event_id)

        binding.viewModel = current_vm
        binding.eeHandlers = handlers




        val spinner: Spinner = findViewById(R.id.e_t)
        val spinner_costume: Spinner = findViewById(R.id.e_c)

        ArrayAdapter.createFromResource(
            this,
            R.array.Ev_Types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
        spinner.setSelection(event_type)

        val db: AppDatabase = AppDatabase.getInstance(this)
        val costumeDao = db.CostumeDao()
        GlobalScope.launch {
            val repos = Repos(costumeDao, 0)
            var names = repos.allCosplay.map { it.character to it.costumeID}.toMap()
            names = names + Pair(getString(R.string.str_no), -1)
            val names_list = names.keys.toList()


            var adapter_sp = ArrayAdapter(
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
                spinner_costume.setSelection(event_costume-1)
            }







        }







        val datePicker: DatePicker = findViewById(R.id.datePicker1)
        Log.v("MyLog", "string " + event_date)
        val event_date_obj = string_to_data(event_date)
        Log.v("MyLog", "object " + event_date_obj)
        datePicker.updateDate(event_date_obj.year, event_date_obj.month, event_date_obj.day)






    }




}











class EditEViewModel(var event_type: Int, var event_name : String, var event_place : String, var event_date : String, var event_costume : Int, var event_steps : String, var event_id : Int) : ViewModel() {
}

