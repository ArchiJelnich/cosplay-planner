package com.archi.cosplay_planner

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.databinding.LMainScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize



class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private val handlers = Handler()

    class Handler {
        fun onClickFilterIcon(view: View) {
            Log.v("MYDEBUG", "Clicked")
            val t_p = (view.rootView as View).findViewById<View>(R.id.text_p)
            val t_h = (view.rootView as View).findViewById<View>(R.id.text_h)
            val t_f = (view.rootView as View).findViewById<View>(R.id.text_f)
            t_p.visibility = if (t_p.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_h.visibility = if (t_h.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            t_f.visibility = if (t_f.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE

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




}








//val users: List<Events> = userDao.getAll()


//val users: List<User> = userDao.getAll()


// val userDao = getDatabase.userDao()
// val users: List<User> = userDao.getAll()








class MyViewModel(var header: String, var progress: String, var finished: String, var hold: String) : ViewModel() {
}

