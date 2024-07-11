package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.databinding.EventScreenBinding
import com.archi.cosplay_planner.infra.checkTheme
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Events
import com.archi.cosplay_planner.roomDatabase.EventsDao
import com.archi.cosplay_planner.roomDatabase.ReposEvent
import kotlinx.coroutines.launch
import com.archi.cosplay_planner.infra.loadTheme

class EventScreen : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private val handlers = EventScreen.Handler(this)

    class Handler (private val context: Context) {


        fun onClickNewEvent(view: View) {
            val intent = Intent(context, EventNew::class.java)
            context.startActivity(intent)
        }

        fun onClickToCosplay(view: View) {
            val intent = Intent(context, CosplayScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickToSettings(view: View) {
            val intent = Intent(context, SettingScreen::class.java)
            context.startActivity(intent)
        }

        fun onClickToMMaterial(view: View) {
            val intent = Intent(context, MaterialBase::class.java)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme(this)
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)
        val binding = EventScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.handlers = handlers
        val eventDao = db.EventsDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvent)
        recyclerView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {

            val repos = ReposEvent(eventDao, 0)
            val adapter = EventRV(repos.allEvents, 0)
            val divider = DividerItemDecoration(this@EventScreen, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@EventScreen,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)
            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, event ->
                EventsonEventClickListener(event, this@EventScreen)
            }

            adapter.onEventLongClickListener = { position, event ->
                EventsonEventClickListenerLong(this@EventScreen, event, eventDao, recyclerView)
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