package com.archi.cosplay_planner


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.P_ROOM.EventsDao
import com.archi.cosplay_planner.P_ROOM.ReposEvent
import com.archi.cosplay_planner.databinding.LEventsScreenBinding
import kotlinx.coroutines.launch


class EventActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private val handlers = EventActivity.Handler(this)

    class Handler (private val context: Context) {


        fun onClickNewEvent(view: View) {
            val intent = Intent(context, NewEventActivity::class.java)
            context.startActivity(intent)
            //Log.v("MYDEBUG", "Corrrr")
        }

        fun onClickToCosplay(view: View) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToSettings(view: View) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToMMaterial(view: View) {
            val intent = Intent(context, MaterialBase::class.java)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_Cosplayplanner_blue)
        }
        else {
            setTheme(R.style.Theme_Cosplayplanner_pink)
        }

        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)
        val binding = LEventsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.handlers = handlers




        val eventDao = db.EventsDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvent)
        recyclerView.layoutManager = LinearLayoutManager(this)




        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            var repos = ReposEvent(eventDao, 0)
            //recyclerView.adapter = EventRV(repos.allEvents, 0)
            var adapter = EventRV(repos.allEvents, 0)
            val divider = DividerItemDecoration(this@EventActivity, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@EventActivity,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)
            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, event ->
                //Log.v("MyLog", "clicked " + position)
                EventsonEventClickListener(event, this@EventActivity)
            }

            adapter.onEventLongClickListener = { position, event ->
                EventsonEventClickListenerLong(this@EventActivity, event, eventDao, recyclerView)
                true
            }



        }


    }

}

class EventViewModel() : ViewModel() {
}

fun EventsonEventClickListener(event : Events, context : Context)
{
    val intent = Intent(context, EditEventActivity::class.java)
    intent.putExtra("event", event)
    context.startActivity(intent)
}

fun EventsonEventClickListenerLong(context: Context, event : Events, eventDao : EventsDao, recyclerView : RecyclerView){
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.str_delete_event)
    val message = context.getString(R.string.str_delete_event_message)
    builder.setMessage(message + " " + event.event)
    builder.setPositiveButton(R.string.str_yes) { dialog, which ->
        //Log.v("MyLog", "Yes")
        eventDao.delete(event)
        var repos = ReposEvent(eventDao, 0)
        val adapter = EventRV(repos.allEvents, 0)
        recyclerView.adapter = adapter
        adapter.onEventClickListener = { position, event ->
            EventsonEventClickListener(event, context)
        }
        adapter.onEventLongClickListener = { position, event ->
            EventsonEventClickListenerLong(context, event, eventDao, recyclerView)
            true
        }
    }
    builder.setNegativeButton(R.string.str_no) { dialog, which ->
    }
    builder.show()
}