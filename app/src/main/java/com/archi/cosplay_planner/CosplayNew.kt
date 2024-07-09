package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.archi.cosplay_planner.databinding.CosplayNewBinding
import com.archi.cosplay_planner.infra.checkTheme


class CosplayNewActivity : AppCompatActivity() {

    private val handlers = Handlers(this)

    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {
          val edittextFandom = (view.rootView as View).findViewById<View>(R.id.e_f) as EditText
          val edittextCharacter = (view.rootView as View).findViewById<View>(R.id.e_c) as EditText

          if (edittextFandom.text.isEmpty()) {
              edittextFandom.setText(R.string.str_New_fandom)
          }

          if (edittextCharacter.text.isEmpty()) {
              edittextCharacter.setText(R.string.str_New_Char)
          }

          if (inputCheckerText(edittextFandom.text.toString()).second != 0) {
              Toast.makeText(context, "Fandom:" + inputCheckerText(edittextFandom.text.toString()).first, Toast.LENGTH_SHORT).show()
          }

          if (inputCheckerText(edittextCharacter.text.toString()).second != 0) {
              Toast.makeText(context, "Character:" + inputCheckerText(edittextCharacter.text.toString()).first, Toast.LENGTH_SHORT).show()
          }

          if ((inputCheckerText(edittextFandom.text.toString()).second == 0) && (inputCheckerText(edittextCharacter.text.toString()).second)==0) {
                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()
                GlobalScope.launch  {

                    val character = inputCheckerText(edittextCharacter.text.toString()).first
                    if (costumeDao.getCostumeIDByCharacter(character).isNotEmpty())
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    val costumeToAdd = Costume(
                        costumeID = 0,
                        fandom = inputCheckerText(edittextFandom.text.toString()).first,
                        character = character,
                        status = 0,
                        progress = 0
                    )
                    costumeDao.insertAll(costumeToAdd)
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

        checkTheme(this)
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