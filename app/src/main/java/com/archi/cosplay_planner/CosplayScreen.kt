package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.archi.cosplay_planner.databinding.CosplayScreenBinding
import com.archi.cosplay_planner.infra.checkTheme
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Costume
import com.archi.cosplay_planner.roomDatabase.CostumeDao
import com.archi.cosplay_planner.roomDatabase.DetailDao
import com.archi.cosplay_planner.roomDatabase.EventsDao
import com.archi.cosplay_planner.roomDatabase.MaterialsPlannedDao
import com.archi.cosplay_planner.roomDatabase.PhotoDAO
import com.archi.cosplay_planner.roomDatabase.Repos
import com.archi.cosplay_planner.roomDatabase.ReposEvent
import kotlinx.coroutines.launch
import com.archi.cosplay_planner.infra.localeChecker


class CosplayScreen : AppCompatActivity()  {


    private lateinit var db: AppDatabase
    private val handlers = CosplayScreen.Handler(this)

    class Handler (private val context: Context) {

        fun onClickFilterIcon(view: View) {
            val t_p = (view.rootView as View).findViewById<View>(R.id.text_p)
            val t_h = (view.rootView as View).findViewById<View>(R.id.text_h)
            val t_f = (view.rootView as View).findViewById<View>(R.id.text_f)
            t_p.visibility = if (t_p.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_h.visibility = if (t_h.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_f.visibility = if (t_f.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        }

        fun onClickNewCosplay(view: View) {
            val intent = Intent(context, CosplayNewActivity::class.java)
            context.startActivity(intent)
        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventScreen::class.java)
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

        @SuppressLint("CommitPrefEdits", "ResourceAsColor")
        fun onClickFilter(view: View) {

            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()

            var filter = sharedPreferences.getInt("filter", -1)
            val t_p = (view.rootView as View).findViewById<View>(R.id.text_p)
            val t_f = (view.rootView as View).findViewById<View>(R.id.text_f)
            val t_h = (view.rootView as View).findViewById<View>(R.id.text_h)

            when (view.id) {

                R.id.text_p -> {
                    if (filter==0) {editor.putInt("filter", -1)
                        t_p.setBackgroundResource(0)
                    }
                    else {editor.putInt("filter",0)
                        t_p.setBackgroundResource(R.drawable.rounded_green)
                    }
                    t_h.setBackgroundResource(0)
                    t_f.setBackgroundResource(0)
                }

                R.id.text_f -> {
                    if (filter==1) {editor.putInt("filter", -1)
                        t_f.setBackgroundResource(0);}
                    else {editor.putInt("filter",1)
                        t_f.setBackgroundResource(R.drawable.rounded_green)
                    }
                    t_h.setBackgroundResource(0)
                    t_p.setBackgroundResource(0)
                }

                R.id.text_h -> {
                    if (filter==2) {editor.putInt("filter", -1)
                        t_h.setBackgroundResource(0);}
                    else {editor.putInt("filter",2)
                        t_h.setBackgroundResource(R.drawable.rounded_green)
                    }
                    t_p.setBackgroundResource(0)
                    t_f.setBackgroundResource(0)
                }

            }
            editor.apply()
            filter = sharedPreferences.getInt("filter", -1)
            val db = AppDatabase.getInstance(context)
            val CostumeDao = db.CostumeDao()
            val photoDao = db.PhotoDAO()
            val detailDao = db.DetailDao()
            val eventDao = db.EventsDao()
            val costumeDao = db.CostumeDao()
            val MaterialsPlannedDao = db.MaterialsPlannedDao()
            val repos = Repos(CostumeDao)
            val recycler_view_late = (view.rootView as View).findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = CosplayRV(repos.allCosplay, repos.filteredCosplayFinished, repos.filteredCosplayProgress, repos.filteredCosplayOnHold, filter)
            recycler_view_late.adapter = adapter

            adapter.onEventClickListener = { position, costume ->
                onCostume(context, costume)
            }

            adapter.onEventLongClickListener = { position, costume ->
                onCostumeLong(context, eventDao, costume, costumeDao, detailDao, MaterialsPlannedDao, photoDao, filter, recycler_view_late)
                true
            }


        }

    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme(this)
        localeChecker(this)
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)
        val binding = CosplayScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentVm = MyViewModel(getString(R.string.str_My_Cosplays), getString(R.string.str_text_p), getString(R.string.str_text_f), getString(R.string.str_text_h))
        binding.viewModel = currentVm
        binding.handlers = handlers
        val CostumeDao = db.CostumeDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val filter = sharedPref.getInt("filter", -1)
        Log.v("MYDEBUG", "Filter in main" + filter)
        val db = AppDatabase.getInstance(applicationContext)
        val eventDao = db.EventsDao()
        val costumeDao = db.CostumeDao()
        val photoDao = db.PhotoDAO()
        val detailDao = db.DetailDao()
        val MaterialsPlannedDao = db.MaterialsPlannedDao()

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            val repos = Repos(CostumeDao)
            val adapter = CosplayRV(repos.allCosplay, repos.filteredCosplayFinished, repos.filteredCosplayProgress, repos.filteredCosplayOnHold, filter)
            val divider = DividerItemDecoration(this@CosplayScreen, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@CosplayScreen,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)

            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, costume ->
                onCostume(this@CosplayScreen, costume)
            }

            adapter.onEventLongClickListener = { position, costume ->
                onCostumeLong(this@CosplayScreen, eventDao, costume, costumeDao, detailDao, MaterialsPlannedDao, photoDao, filter, recyclerView)
                true
            }

        }
    }
}


class MyViewModel(var header: String, var progress: String, var finished: String, var hold: String) : ViewModel() {
}

fun onCostume(context: Context, costume : Costume)
{
    val intent = Intent(context, CosplayEditActivity::class.java)
    intent.putExtra("costume", costume)
    context.startActivity(intent)
}

fun onCostumeLong(context: Context, eventDao : EventsDao, costume: Costume, costumeDao : CostumeDao, detailDao : DetailDao, MaterialsPlannedDao : MaterialsPlannedDao, photoDao : PhotoDAO, filter : Int, recyclerView : RecyclerView){
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.str_delete_event)
    val message = context.getString(R.string.str_delete_costume_message)
    val repos_e = ReposEvent(eventDao, costume.costumeID)

    if (repos_e.filteredEvents.size == 0)
    {
        builder.setMessage(message + " " + costume.fandom + " " + costume.character)

        builder.setPositiveButton(R.string.str_yes) { dialog, which ->
            costumeDao.delete(costume)
            val detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            val repos = Repos(costumeDao)
            val adapter = CosplayRV(
                repos.allCosplay,
                repos.filteredCosplayFinished,
                repos.filteredCosplayProgress,
                repos.filteredCosplayOnHold,
                filter
            )
            recyclerView.adapter = adapter
            adapter.onEventClickListener = { position, costume ->
                onCostume(context, costume)
            }
            adapter.onEventLongClickListener = { position, costume ->
                onCostumeLong(context, eventDao, costume, costumeDao, detailDao, MaterialsPlannedDao, photoDao, filter, recyclerView)
                true
            }

        }


    }
    else
    {
        if (repos_e.filteredEvents.size == 1) {
            builder.setMessage(
                message + " " + costume.fandom + " " + costume.character + ("\n") + context.getString(
                    R.string.str_delete_costume_message_full
                ) + " " + "1" + " " + context.getString(R.string.str_event)
            )
        }
        else {
            builder.setMessage(
                message + " " + costume.fandom + " " + costume.character + ("\n") + context.getString(
                    R.string.str_delete_costume_message_full
                ) + " " + repos_e.filteredEvents.size + " " + context.getString(R.string.str_events))
        }

        builder.setPositiveButton(R.string.str_del_del) { dialog, which ->
            costumeDao.delete(costume)
            val detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            eventDao.deleteByCostumeID(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            val repos = Repos(costumeDao)
            val adapter = CosplayRV(
                repos.allCosplay,
                repos.filteredCosplayFinished,
                repos.filteredCosplayProgress,
                repos.filteredCosplayOnHold,
                filter
            )
            recyclerView.adapter = adapter
            adapter.onEventClickListener = { position, costume ->
                onCostume(context, costume)
            }
            adapter.onEventLongClickListener = { position, costume ->
                onCostumeLong(context, eventDao, costume, costumeDao, detailDao, MaterialsPlannedDao, photoDao, filter, recyclerView)
                true
            }

        }

        builder.setNeutralButton(R.string.str_del_update) { dialog, which ->
            costumeDao.delete(costume)
            val detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            eventDao.updateWhenDelete(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            val repos = Repos(costumeDao)
            val adapter = CosplayRV(
                repos.allCosplay,
                repos.filteredCosplayFinished,
                repos.filteredCosplayProgress,
                repos.filteredCosplayOnHold,
                filter
            )
            recyclerView.adapter = adapter
            adapter.onEventClickListener = { position, costume ->
                onCostume(context, costume)
            }
            adapter.onEventLongClickListener = { position, costume ->
                onCostumeLong(context, eventDao, costume, costumeDao, detailDao, MaterialsPlannedDao, photoDao, filter, recyclerView)
                true
            }

        }

    }

    builder.setNegativeButton(R.string.str_no) { dialog, which ->
    }

    builder.show()
}