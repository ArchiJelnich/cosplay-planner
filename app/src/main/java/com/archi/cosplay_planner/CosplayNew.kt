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
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.Costume
import com.archi.cosplay_planner.infra.inputCheckerText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.databinding.DataBindingUtil
import com.archi.cosplay_planner.databinding.CosplayNewBinding


class CosplayNewActivity : AppCompatActivity() {

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



         if (inputCheckerText(e_f.text.toString()).second != 0)
          {
                   Toast.makeText(context, "Fandom:" + inputCheckerText(e_f.text.toString()).first, Toast.LENGTH_SHORT).show()
                 }

           if (inputCheckerText(e_c.text.toString()).second != 0)
           {
               Toast.makeText(context, "Character:" + inputCheckerText(e_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }


            if ((inputCheckerText(e_f.text.toString()).second == 0) && (inputCheckerText(e_c.text.toString()).second)==0)
            {
                //Toast.makeText(context, "Nice" + InputCheckerText(e_f.text.toString()).first.toString() + " " + InputCheckerText(e_c.text.toString()).first.toString(), Toast.LENGTH_SHORT).show()

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()

                Log.v("MYDEBUG", "Before corut")

                GlobalScope.launch  {
                    Log.v("MYDEBUG", "In corut")
                    val character = inputCheckerText(e_c.text.toString()).first

                    if (costumeDao.getCostumeIDByCharacter(character).size!=0)
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                        // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val CostumeToAdd = Costume(
                        costumeID = 0,
                        fandom = inputCheckerText(e_f.text.toString()).first,
                        character = character,
                        status = 0,
                        progress = 0
                    )
                    costumeDao.insertAll(CostumeToAdd)

                    val intent = Intent(context, CosplayScreen::class.java)
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
        if (loadTheme(this)=="blue")
        {
            setTheme(R.style.Theme_CosplayPlannerBlue)
        }
        else {
            setTheme(R.style.Theme_CosplayPlannerPink)
        }

        super.onCreate(savedInstanceState)

        val binding = CosplayNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentVm = NewCViewModel("","")

        binding.viewModel = currentVm
        binding.ncHandlers = handlers













    }




}











class NewCViewModel(var fandom: String, var character : String) : ViewModel() {
}

