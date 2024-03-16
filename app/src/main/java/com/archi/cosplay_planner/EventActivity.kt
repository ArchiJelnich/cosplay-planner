package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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




        rv()




    }

    fun rv () {

        val EventDao = db.EventsDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvent)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            var repos = ReposEvent(EventDao, 0)
            recyclerView.adapter = EventRV(repos.allEvents, 0)

        }
    }

}

class EventViewModel() : ViewModel() {
}
