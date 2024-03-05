package com.archi.cosplay_planner

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.databinding.LMainScreenBinding
import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)


        //setContentView(R.layout.l_main_screen)


       //val binding: LMainScreenBinding = DataBindingUtil.setContentView(this, R.layout.l_main_screen)

        //val binding: LMainScreenBinding = LMainScreenBinding.bind(R.layout.l_main_screen)


        var binding = LMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = MyViewModel("Hi!")

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



        }





    }








    //val users: List<Events> = userDao.getAll()


        //val users: List<User> = userDao.getAll()


       // val userDao = getDatabase.userDao()
       // val users: List<User> = userDao.getAll()








data class MyViewModel(val header: String)


