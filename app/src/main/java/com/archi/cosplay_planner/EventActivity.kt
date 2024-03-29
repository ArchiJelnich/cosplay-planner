package com.archi.cosplay_planner


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.AppDatabase
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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
            val adapter = EventRV(repos.allEvents, 0)
            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, event ->
                //Log.v("MyLog", "clicked " + position)
                Log.v("MyLog", "clicked " + event)
                val intent = Intent(this@EventActivity, EditEventActivity::class.java)
                intent.putExtra("event", event)
                this@EventActivity.startActivity(intent)
            }
            adapter.onEventLongClickListener = { position, event ->
                Log.v("MyLog", "clicked " + position)


                val builder = AlertDialog.Builder(this@EventActivity)
                builder.setTitle(R.string.str_delete_event)
                val message = getString(R.string.str_delete_event_message)
                builder.setMessage(message + " " + event.event)

                builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //Log.v("MyLog", "Yes")
                    eventDao.delete(event)
                    //adapter.notifyItemRemoved(position)
                    repos = ReposEvent(eventDao, 0)
                    val newAdapter = EventRV(repos.allEvents, 0)
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

class EventViewModel() : ViewModel() {
}

