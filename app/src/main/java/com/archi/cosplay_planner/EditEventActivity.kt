package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.R_Infra.InputCheckerText
import com.archi.cosplay_planner.R_Infra.sort_value_from_date
import com.archi.cosplay_planner.R_Infra.string_to_data
import com.archi.cosplay_planner.databinding.LEventsEditScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            val e_c = (view.rootView as View).findViewById<View>(R.id.e_c) as EditText
            val e_s = (view.rootView as View).findViewById<View>(R.id.e_s) as EditText
            val e_id = (view.rootView as View).findViewById<View>(R.id.e_id) as EditText



            if (e_t.getSelectedItem().toString()==types[0] )
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
            }


            if (e_n.text.isEmpty()) {
                e_n.setText(R.string.str_Event_name)
            }

            if (e_p.text.isEmpty()) {
                e_p.setText(R.string.str_Event_place)
            }


            if (InputCheckerText(e_n.text.toString()).second != 0)
            {
                Toast.makeText(context, "Fandom:" + InputCheckerText(e_n.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (InputCheckerText(e_p.text.toString()).second != 0)
            {
                Toast.makeText(context, "Character:" + InputCheckerText(e_p.text.toString()).first, Toast.LENGTH_SHORT).show()
            }


            if ((InputCheckerText(e_p.text.toString()).second == 0) && (InputCheckerText(e_n.text.toString()).second)==0) {
                //Toast.makeText(context, "Nice" + InputCheckerText(e_f.text.toString()).first.toString() + " " + InputCheckerText(e_c.text.toString()).first.toString(), Toast.LENGTH_SHORT).show()

                val db: AppDatabase = AppDatabase.getInstance(context)
                val eventDao = db.EventsDao()

                val date = e_d.dayOfMonth.toString()+"."+(e_d.month +1).toString()+"."+e_d.year.toString()
                val cosplay_id = e_c.text.toString()
                val event_id = e_id.text.toString()



                GlobalScope.launch {
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


        val binding = LEventsEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = EditEViewModel(event_type, event_name, event_place, event_date, event_costume, event_steps, event_id)

        binding.viewModel = current_vm
        binding.eeHandlers = handlers




        val spinner: Spinner = findViewById(R.id.e_t)


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


        val datePicker: DatePicker = findViewById(R.id.datePicker1)
        Log.v("MyLog", "string " + event_date)
        val event_date_obj = string_to_data(event_date)
        Log.v("MyLog", "object " + event_date_obj)
        datePicker.updateDate(event_date_obj.year, event_date_obj.month, event_date_obj.day)






    }




}











class EditEViewModel(var event_type: Int, var event_name : String, var event_place : String, var event_date : String, var event_costume : Int, var event_steps : String, var event_id : Int) : ViewModel() {
}

