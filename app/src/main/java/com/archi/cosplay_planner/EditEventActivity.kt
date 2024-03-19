package com.archi.cosplay_planner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.archi.cosplay_planner.databinding.LEditEventScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EditEventActivity : AppCompatActivity() {

    private val handlers = Handlers(this)
    class Handlers  (private val context: Context) {
        fun onClickAdd(view: View) {

                }


        fun HideKeyboard(view: View) {

            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.l_edit_event_screen)
        val binding = LEditEventScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_vm = EditEViewModel("","", "", "", "", "")

        binding.viewModel = current_vm
        binding.eeHandlers = handlers




        val spinner: Spinner = findViewById(R.id.e_t)
        ArrayAdapter.createFromResource(
            this,
            R.array.Ev_Types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }









    }




}











class EditEViewModel(var event_type: String, var event_name : String, var event_place : String, var event_date : String, var event_costume : String, var event_steps : String) : ViewModel() {
}

