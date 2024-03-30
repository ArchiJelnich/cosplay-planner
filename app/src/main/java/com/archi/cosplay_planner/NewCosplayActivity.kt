package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.Costume
import com.archi.cosplay_planner.P_Infra.InputCheckerText
import com.archi.cosplay_planner.databinding.LNewCosplayScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewCosplayActivity : AppCompatActivity() {

    private val handlers = Handlers(this)

    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {
          val e_f = (view.rootView as View).findViewById<View>(R.id.e_f) as EditText
          val e_c = (view.rootView as View).findViewById<View>(R.id.e_c) as EditText

          if (e_f.text.isEmpty()) {
              e_f.setText(R.string.str_New_fandom)
          }

            if (e_c.text.isEmpty()) {
                e_c.setText(R.string.str_New_Char)
            }


            Log.v("MYLOG", "Clicked");

         if (InputCheckerText(e_f.text.toString()).second != 0)
          {
                   Toast.makeText(context, "Fandom:" + InputCheckerText(e_f.text.toString()).first, Toast.LENGTH_SHORT).show()
                 }

           if (InputCheckerText(e_c.text.toString()).second != 0)
           {
               Toast.makeText(context, "Character:" + InputCheckerText(e_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }


            if ((InputCheckerText(e_f.text.toString()).second == 0) && (InputCheckerText(e_c.text.toString()).second)==0)
            {
                //Toast.makeText(context, "Nice" + InputCheckerText(e_f.text.toString()).first.toString() + " " + InputCheckerText(e_c.text.toString()).first.toString(), Toast.LENGTH_SHORT).show()

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()

                Log.v("MYDEBUG", "Before corut")

                GlobalScope.launch  {
                    Log.v("MYDEBUG", "In corut")
                    var character = InputCheckerText(e_c.text.toString()).first

                    if (costumeDao.getByCharacter(character).size!=0)
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                        // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val CostumeToAdd = Costume(
                        costumeID = 0,
                        fandom = InputCheckerText(e_f.text.toString()).first,
                        character = character,
                        status = 0,
                        progress = 0
                    )
                    costumeDao.insertAll(CostumeToAdd)

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)


                }



                }
            }



        fun hideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("MYLOG", "OnCreate");

        super.onCreate(savedInstanceState)


        val binding = LNewCosplayScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentVm = NewCViewModel("","")

        binding.viewModel = currentVm
        binding.ncHandlers = handlers













    }




}











class NewCViewModel(var fandom: String, var character : String) : ViewModel() {
}

