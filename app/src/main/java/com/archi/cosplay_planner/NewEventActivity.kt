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
import com.archi.cosplay_planner.databinding.LNewEventScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewEventActivity : AppCompatActivity() {

    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
       fun onClickAdd(view: View) {




           val e_n = (view.rootView as View).findViewById<View>(R.id.e_n) as EditText
           val e_p = (view.rootView as View).findViewById<View>(R.id.e_p) as EditText
           val e_t = (view.rootView as View).findViewById<View>(R.id.e_t) as Spinner
           val e_d = (view.rootView as View).findViewById<View>(R.id.datePicker1) as DatePicker
           val types = context.resources.getStringArray(R.array.Ev_Types)
           var type = 0


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



           if (e_n.text.length==0) {
               e_n.setText(R.string.str_Event_name)
           }

           if (e_p.text.length==0) {
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

               var db: AppDatabase
               db = AppDatabase.getInstance(context)
               val EventDao = db.EventsDao()
               val costumeID = -1
               val steps = ""
               val date = e_d.getDayOfMonth().toString()+"."+(e_d.getMonth()+1).toString()+"."+e_d.getYear().toString()





               GlobalScope.launch {
                   Log.v("MYDEBUG", "In corut")

                   val EventToAdd = Events(
                       eventID = 0,
                       event = InputCheckerText(e_n.text.toString()).first,
                       place = InputCheckerText(e_p.text.toString()).first,
                       date = date,
                       costumeID = costumeID,
                       type = type,
                       steps = steps
                   )
                   EventDao.insertAll(EventToAdd)

                   val intent = Intent(context, EventActivity::class.java)
                   context.startActivity(intent)


               }
           }













           }

        fun HideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_new_event_screen)

        val binding = LNewEventScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = NewEViewModel("","", "", "")

        binding.viewModel = current_vm
        binding.neHandlers = handlers




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









    }




}











class NewEViewModel(var event_type: String, var event_name : String, var event_place : String, var event_date : String) : ViewModel() {
}

