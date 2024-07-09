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
import com.archi.cosplay_planner.databinding.EventNewBinding
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Events
import com.archi.cosplay_planner.infra.inputCheckerText
import com.archi.cosplay_planner.infra.sortValueFromDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.archi.cosplay_planner.infra.loadTheme



class EventNew : AppCompatActivity() {

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



           if (e_n.text.isEmpty()) {
               e_n.setText(R.string.str_Event_name)
           }

           if (e_p.text.isEmpty()) {
               e_p.setText(R.string.str_Event_place)
           }


           if (inputCheckerText(e_n.text.toString()).second != 0)
           {
               Toast.makeText(context, "Fandom:" + inputCheckerText(e_n.text.toString()).first, Toast.LENGTH_SHORT).show()
           }

           if (inputCheckerText(e_p.text.toString()).second != 0)
           {
               Toast.makeText(context, "Character:" + inputCheckerText(e_p.text.toString()).first, Toast.LENGTH_SHORT).show()
           }


           if ((inputCheckerText(e_p.text.toString()).second == 0) && (inputCheckerText(e_n.text.toString()).second)==0) {

               val db: AppDatabase = AppDatabase.getInstance(context)
               val eventDao = db.EventsDao()
               val costumeID = -1
               val date = e_d.dayOfMonth.toString()+"."+(e_d.month).toString()+"."+e_d.year.toString()





               GlobalScope.launch {
                   Log.v("MYDEBUG", "In corut")

                   val eventToAd = Events(
                       eventID = 0,
                       event = inputCheckerText(e_n.text.toString()).first,
                       place = inputCheckerText(e_p.text.toString()).first,
                       date = date,
                       costumeID = costumeID,
                       type = type,
                       dateSorted = sortValueFromDate(date)
                   )
                   eventDao.insertAll(eventToAd)

                   val intent = Intent(context, EventScreen::class.java)
                   context.startActivity(intent)


               }
           }













           }

        fun hideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_CosplayPlannerBlue)
        }
        else {
            setTheme(R.style.Theme_CosplayPlannerPink)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_new)

        val binding = EventNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentVm = NewEViewModel("","", "", "")

        binding.viewModel = currentVm
        binding.neHandlers = handlers




        val spinner: Spinner = findViewById(R.id.e_t)
        ArrayAdapter.createFromResource(
            this,
            R.array.Ev_Types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }









    }




}











class NewEViewModel(var event_type: String, var event_name : String, var event_place : String, var event_date : String) : ViewModel() {
}

