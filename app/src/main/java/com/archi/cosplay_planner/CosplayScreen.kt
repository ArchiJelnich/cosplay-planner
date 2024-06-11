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
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Costume
import com.archi.cosplay_planner.roomDatabase.CostumeDao
import com.archi.cosplay_planner.roomDatabase.DetailDao
import com.archi.cosplay_planner.roomDatabase.EventsDao
import com.archi.cosplay_planner.roomDatabase.MaterialsPlannedDao
import com.archi.cosplay_planner.roomDatabase.PhotoDAO
import com.archi.cosplay_planner.roomDatabase.Repos
import com.archi.cosplay_planner.roomDatabase.ReposEvent
import com.archi.cosplay_planner.databinding.LMainScreenBinding
import kotlinx.coroutines.launch


class CosplayScreen : AppCompatActivity()  {


    private lateinit var db: AppDatabase
    private val handlers = Handler(this)





    //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    //val navController = navHostFragment.navController

    //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    //val navController = navHostFragment.navController
    //val navInflater = navController.navInflater
    //val navGraph = navInflater.inflate(R.navigation.nav_graf)
    //navController.graph = navGraph



    class Handler (private val context: Context) {



        fun onClickFilterIcon(view: View) {
            Log.v("MYDEBUG", "Clicked")
            val t_p = (view.rootView as View).findViewById<View>(R.id.text_p)
            val t_h = (view.rootView as View).findViewById<View>(R.id.text_h)
            val t_f = (view.rootView as View).findViewById<View>(R.id.text_f)
            t_p.visibility = if (t_p.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_h.visibility = if (t_h.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_f.visibility = if (t_f.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        }

        fun onClickNewCosplay(view: View) {



            // val navController = findNavController(Activity(), R.id.nav_graf)
           // navController.navigate(
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
            var editor = sharedPreferences.edit()




           // var sharedPref = getPreferences(Context.MODE_PRIVATE)
           // var editor = sharedPref.edit()
            var filter = sharedPreferences.getInt("filter", -1)
            val t_p = (view.rootView as View).findViewById<View>(R.id.text_p)
            val t_f = (view.rootView as View).findViewById<View>(R.id.text_f)
            val t_h = (view.rootView as View).findViewById<View>(R.id.text_h)


            Log.v("MyDebug", "Filter before " + filter)


            when (view.id) {

                R.id.text_p -> {
                    if (filter==0) {editor.putInt("filter", -1)
                        t_p.setBackgroundResource(0);
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

                else -> { // Note the block
                    Log.v("MyDebug", "Hm")
                }


            }
            editor.apply()
          //  Log.v("MyDebug", "Hm " + sharedPreferences.getInt("filter", -1))
           // com.archi.cosplay_planner.MainActivity.rv()
            filter = sharedPreferences.getInt("filter", -1)
            var db = AppDatabase.getInstance(context)
            val CostumeDao = db.CostumeDao()
            val photoDao = db.PhotoDAO()
            val detailDao = db.DetailDao()
            val eventDao = db.EventsDao()
            val costumeDao = db.CostumeDao()
            val MaterialsPlannedDao = db.MaterialsPlannedDao()
            val repos = Repos(CostumeDao, filter)
            val recycler_view_late = (view.rootView as View).findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = CosplayRV(repos.allCosplay, repos.filteredCosplayFinished, repos.filteredCosplayProgress, repos.filteredCosplayOnHold, filter)


            recycler_view_late.adapter = adapter
            Log.v("MyDebug", "Filter after " + filter)

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
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_CosplayPlannerBlue)
        }
        else {
            setTheme(R.style.Theme_CosplayPlannerPink)
        }


        val language = loadLanguage(this)
        if (language!=null)
        {
            setAppLocale(this, language)
        }
        Log.v("MyLog", "language:" + language);



        //editor.putInt("filter",-1)
       // editor.apply()




        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)



        //setContentView(R.layout.l_main_screen)


       //val binding: LMainScreenBinding = DataBindingUtil.setContentView(this, R.layout.l_main_screen)

        //val binding: LMainScreenBinding = LMainScreenBinding.bind(R.layout.l_main_screen)


        val binding = LMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val currentVm = MyViewModel(getString(R.string.str_My_Cosplays), getString(R.string.str_text_p), getString(R.string.str_text_f), getString(R.string.str_text_h))





        binding.viewModel = currentVm
        binding.handlers = handlers




       // val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
       // val navController = navHostFragment.navController



        rv()


           // val binding: MyViewModel = MyViewModel.bind(R.layout.l_main_screen)
       //

       //

      // val binding: MyLayoutBinding = MyLayoutBinding.inflate(layoutInflater)
    //val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.l_main_screen)

      // val binding: LMainScreenBinding = LMainScreenBinding.inflate(getLayoutInflater())


       // binding.ViewModel = MyViewModel("Hi!")
        //
        //
        /* val EventDao = db.EventsDao()
        val eventToAdd = Events(
            eventID = 0,
            event = "Название мероприятия",
            type = 0,
            place = "Место проведения",
            date = "Дата",
            costumeID = 123,
            steps = "Шаг 1"
        )


        lifecycleScope.launch {
            val events = withContext(Dispatchers.IO) {
                EventDao.insertAll(eventToAdd)
                Log.v("MYDEBUG", "DB:" + EventDao.getAll());
            }

        }







*/
/*
    val CostumeDao = db.CostumeDao()
    val costumeToAdd = Costume(
        costumeID = 0,
        fandom = "HH",
        character = "AngelDust",
        status = 1,
        progress = 75
)


lifecycleScope.launch {
    val costumes = withContext(Dispatchers.IO) {
        CostumeDao.insertAll(costumeToAdd)
        Log.v("MYDEBUG", "DB:" + CostumeDao.getAll());
    }

}*/








}

   // override fun onSupportNavigateUp(): Boolean {
   //     return navController.navigateUp() || super.onSupportNavigateUp()
  //  }




    fun rv () {

        val CostumeDao = db.CostumeDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
       // var editor = sharedPref.edit()
        val filter = sharedPref.getInt("filter", -1)
        Log.v("MYDEBUG", "Filter in main" + filter)
        var db = AppDatabase.getInstance(applicationContext)
        val eventDao = db.EventsDao()
        val costumeDao = db.CostumeDao()
        val photoDao = db.PhotoDAO()
        val detailDao = db.DetailDao()
        val MaterialsPlannedDao = db.MaterialsPlannedDao()

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            var repos = Repos(CostumeDao, filter)
            var adapter = CosplayRV(repos.allCosplay, repos.filteredCosplayFinished, repos.filteredCosplayProgress, repos.filteredCosplayOnHold, filter)
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













//val users: List<Events> = userDao.getAll()


//val users: List<User> = userDao.getAll()


// val userDao = getDatabase.userDao()
// val users: List<User> = userDao.getAll()








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
    var repos_e = ReposEvent(eventDao, costume.costumeID)


    if (repos_e.filteredEvents.size == 0)
    {
        builder.setMessage(message + " " + costume.fandom + " " + costume.character)

        builder.setPositiveButton(R.string.str_yes) { dialog, which ->
            costumeDao.delete(costume)
            var detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            var repos = Repos(costumeDao, filter)
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
            var detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            eventDao.deleteByCostumeID(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            var repos = Repos(costumeDao, filter)
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
            var detailID = detailDao.getIDByCostume(costume.costumeID)
            for (ID in detailID)
            {
                MaterialsPlannedDao.deleteByDetail(ID)
            }
            detailDao.deleteByCostumeID(costume.costumeID)
            eventDao.updateWhenDelete(costume.costumeID)
            photoDao.deleteByID(costume.costumeID)
            var repos = Repos(costumeDao, filter)
            var adapter = CosplayRV(
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
        Log.v("MyLog", "No")
    }

    builder.show()
}