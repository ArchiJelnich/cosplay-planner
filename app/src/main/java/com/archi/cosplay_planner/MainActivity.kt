package com.archi.cosplay_planner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.databinding.LMainScreenBinding
import com.google.android.material.internal.ContextUtils.getActivity
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)



        //setContentView(R.layout.l_main_screen)


       //val binding: LMainScreenBinding = DataBindingUtil.setContentView(this, R.layout.l_main_screen)

        //val binding: LMainScreenBinding = LMainScreenBinding.bind(R.layout.l_main_screen)


        val binding = LMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var current_vm = MyViewModel(getString(R.string.str_My_Cosplays), getString(R.string.str_text_p), getString(R.string.str_text_f), getString(R.string.str_text_h))





        binding.viewModel = current_vm
        binding.handlers = handlers




       // val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
       // val navController = navHostFragment.navController



        val CostumeDao = db.CostumeDao()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            Log.v("MYDEBUG", "Corrrr")
            var repos  =  Repos(CostumeDao)
            recyclerView.adapter = MainRV(repos.allCosplay)

        }









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


}








//val users: List<Events> = userDao.getAll()


//val users: List<User> = userDao.getAll()


// val userDao = getDatabase.userDao()
// val users: List<User> = userDao.getAll()








class MyViewModel(var header: String, var progress: String, var finished: String, var hold: String) : ViewModel() {
}

