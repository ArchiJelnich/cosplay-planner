package com.archi.cosplay_planner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Repos
import com.archi.cosplay_planner.databinding.LMainScreenBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity()  {


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
            val intent = Intent(context, NewCosplayActivity::class.java)
            context.startActivity(intent)

        }

        fun onClickToEvents(view: View) {
            val intent = Intent(context, EventActivity::class.java)
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
                        t_p.setBackgroundColor(0);
                    }
                    else {editor.putInt("filter",0)
                        t_p.setBackgroundColor(R.color.purple_500);
                    }
                    t_h.setBackgroundColor(0)
                    t_f.setBackgroundColor(0)
                }

                R.id.text_f -> {
                    if (filter==1) {editor.putInt("filter", -1)
                        t_f.setBackgroundColor(0);}
                    else {editor.putInt("filter",1)
                        t_f.setBackgroundColor(R.color.purple_500);
                    }
                    t_h.setBackgroundColor(0)
                    t_p.setBackgroundColor(0)
                }

                R.id.text_h -> {
                    if (filter==2) {editor.putInt("filter", -1)
                        t_h.setBackgroundColor(0);}
                    else {editor.putInt("filter",2)
                        t_h.setBackgroundColor(R.color.purple_500);
                    }
                    t_p.setBackgroundColor(0)
                    t_f.setBackgroundColor(0)
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
            val repos = Repos(CostumeDao, filter)
            val recycler_view_late = (view.rootView as View).findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = MainRV(repos.allCosplay, repos.filteredCosplay_f, repos.filteredCosplay_p, repos.filteredCosplay_h, filter)
            recycler_view_late.adapter = adapter
            Log.v("MyDebug", "Filter after " + filter)
            adapter.onEventClickListener = { position, costume ->
                //Log.v("MyLog", "clicked " + position)
                val intent = Intent(context, EditMainActivity::class.java)
                intent.putExtra("costume", costume)
                context.startActivity(intent)
            }




        }

    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {


        //var sharedPref = getPreferences(Context.MODE_WORLD_READABLE)
       // var editor = sharedPref.edit()
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

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            val repos = Repos(CostumeDao, filter)
            val adapter = MainRV(repos.allCosplay, repos.filteredCosplay_f, repos.filteredCosplay_p, repos.filteredCosplay_h, filter)
            recyclerView.adapter = adapter

            adapter.onEventClickListener = { position, costume ->
                //Log.v("MyLog", "clicked " + position)
                Log.v("MyLog", "clicked " + costume)
                val intent = Intent(this@MainActivity, EditMainActivity::class.java)
                intent.putExtra("costume", costume)
                this@MainActivity.startActivity(intent)
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

