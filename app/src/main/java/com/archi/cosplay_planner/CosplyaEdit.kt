package com.archi.cosplay_planner

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.databinding.CosplayEditBinding
import com.archi.cosplay_planner.infra.inputCheckerText
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.CosplayPhoto
import com.archi.cosplay_planner.roomDatabase.Costume
import com.archi.cosplay_planner.roomDatabase.Detail
import com.archi.cosplay_planner.roomDatabase.DetailDao
import com.archi.cosplay_planner.roomDatabase.MaterialsPlannedDao
import com.archi.cosplay_planner.roomDatabase.ReposDetail
import com.archi.cosplay_planner.roomDatabase.ReposEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CosplayEditActivity : AppCompatActivity() {
companion object{
    lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
}
    private val handlers = CosplayEditActivity.Handlers(this)

    class Handlers  (private val context: Context) {
        @OptIn(DelicateCoroutinesApi::class)
        fun onClickAdd(view: View) {
            val c_f = (view.rootView as View).findViewById<View>(R.id.c_f) as EditText
            val c_c = (view.rootView as View).findViewById<View>(R.id.c_c) as EditText
            val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as EditText
            val c_s = (view.rootView as View).findViewById<View>(R.id.c_s) as Spinner
            val avatar = (view.rootView as View).findViewById<View>(R.id.image_avatar) as ImageView

            val statuses = context.resources.getStringArray(R.array.C_status)
            var status = 0

            when(c_s.getSelectedItem().toString()){
                statuses[0] ->  status = 0
                statuses[1] ->  status = 1
                statuses[2] ->  status = 2
            }

            if (c_f.text.isEmpty()) {
                c_f.setText(R.string.str_New_fandom)
            }

            if (c_c.text.isEmpty()) {
                c_c.setText(R.string.str_New_Char)
            }

            if (inputCheckerText(c_f.text.toString()).second != 0)
            {
                Toast.makeText(context, "Fandom:" + inputCheckerText(c_f.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (inputCheckerText(c_c.text.toString()).second != 0)
            {
                Toast.makeText(context, "Character:" + inputCheckerText(c_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }




            if ((inputCheckerText(c_f.text.toString()).second == 0) && (inputCheckerText(c_c.text.toString()).second)==0) {

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()
                val costume_id = c_id.text.toString()
                val character = inputCheckerText(c_c.text.toString()).first
                val detailDao = db.DetailDao()
                val repos = ReposDetail(detailDao, costume_id.toInt())




                GlobalScope.launch {

                withContext(Dispatchers.Main){
                if (repos.costumeProgress == 100 && status == 0) {
                    //val builder = AlertDialog.Builder(context)
                    //builder.setTitle(R.string.str_change_status)
                    //builder.setCancelable(false)
                    //builder.setMessage(R.string.str_change_all_finished)
                    //builder.setPositiveButton(R.string.str_yes) { dialog, which ->
                    //        status = 1

                    //}
                    //builder.setNegativeButton(R.string.str_no) { dialog, which ->
                        //Log.v("MyLog", "No")
                    //}
                    //builder.show()
                    val d_result = showAlertDialog(context, context.getString(R.string.str_change_status), context.getString(R.string.str_change_all_finished))
                    if (d_result)
                    {
                        status = 1
                    }

                }

                if (repos.costumeProgress != 100 && status == 1) {
                    val d_result = showAlertDialog(context, context.getString(R.string.str_change_status), context.getString(R.string.str_change_not_all_finished))
                    if (d_result)
                    {
                        status = 0
                    }
                }}




                    if (costumeDao.getCostumeIDByCharacter(character).size!=0 && !costumeDao.getCostumeIDByCharacter(character).contains(c_id.text.toString().toInt()))
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                       // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    Log.v("MYDEBUG", "In corut")



                    val CostumeToUpdate = Costume(
                        costumeID = costume_id.toInt(),
                        fandom = inputCheckerText(c_f.text.toString()).first,
                        character = character,
                        status = status,
                        progress = repos.costumeProgress
                    )

                    costumeDao.updateCostume(CostumeToUpdate)
                    val intent = Intent(context, CosplayScreen::class.java)
                    context.startActivity(intent)
                }}



            if (avatar.contentDescription!=null)
            {
                Log.d("MyLog", " " + avatar.contentDescription)
            }


        }

        fun onClickImage(view: View){
            if (hasStoragePermission(context)) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                selectImageLauncher.launch(intent)
            }
            else {
                val activity = context as Activity
                requestPermissions(activity,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    200)
            }

        }


        fun onClickAddDetail(view: View){


            val c_f = (view.rootView as View).findViewById<View>(R.id.c_f) as EditText
            val c_c = (view.rootView as View).findViewById<View>(R.id.c_c) as EditText
            val c_id = (view.rootView as View).findViewById<View>(R.id.c_id) as EditText
            val c_p = (view.rootView as View).findViewById<View>(R.id.c_p) as TextView
            val c_s = (view.rootView as View).findViewById<View>(R.id.c_s) as Spinner
            val statuses = context.resources.getStringArray(R.array.C_status)
            var status = 0

            when(c_s.getSelectedItem().toString()){
                statuses[0] ->  status = 0
                statuses[1] ->  status = 1
                statuses[2] ->  status = 2
            }

            if (c_f.text.isEmpty()) {
                c_f.setText(R.string.str_New_fandom)
            }

            if (c_c.text.isEmpty()) {
                c_c.setText(R.string.str_New_Char)
            }

            if (inputCheckerText(c_f.text.toString()).second != 0)
            {
                Toast.makeText(context, "Fandom:" + inputCheckerText(c_f.text.toString()).first, Toast.LENGTH_SHORT).show()
            }

            if (inputCheckerText(c_c.text.toString()).second != 0)
            {
                Toast.makeText(context, "Character:" + inputCheckerText(c_c.text.toString()).first, Toast.LENGTH_SHORT).show()
            }




            if ((inputCheckerText(c_f.text.toString()).second == 0) && (inputCheckerText(c_c.text.toString()).second)==0) {

                val db: AppDatabase = AppDatabase.getInstance(context)
                val costumeDao = db.CostumeDao()
                val costume_id = c_id.text.toString()
                val character = inputCheckerText(c_c.text.toString()).first


                GlobalScope.launch {

                    if (costumeDao.getCostumeIDByCharacter(character).size!=0 && !costumeDao.getCostumeIDByCharacter(character).contains(c_id.text.toString().toInt()))
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        }
                        // Toast.makeText(context, "Character name is not unique", Toast.LENGTH_SHORT).show()
                        return@launch
                    }


                    Log.v("MYDEBUG", "In corut")

                    val CostumeToUpdate = Costume(
                        costumeID = costume_id.toInt(),
                        fandom = inputCheckerText(c_f.text.toString()).first,
                        character = character,
                        status = status,
                        progress = c_p.text.toString().toInt()
                    )
                    costumeDao.updateCostume(CostumeToUpdate)



                }}


            val costume_id = c_id.text.toString().toInt()
            val intent = Intent(context, DetailEditNew::class.java)
            intent.putExtra("costume_id", costume_id)
            intent.putExtra("edit_flag", 0)
            context.startActivity(intent)
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

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    findViewById<ImageView>(R.id.image_avatar).setImageURI(uri)
                    val c_id = findViewById<View>(R.id.c_id) as EditText
                    val db: AppDatabase = AppDatabase.getInstance(this)
                    val PhotoDAO = db.PhotoDAO()
                    val PhotoToUpdate = CosplayPhoto(
                        photoID = 0,
                        costumeID = c_id.text.toString().toInt(),
                        photo = uri.toString()
                    )


                    if (PhotoDAO.getByID(c_id.text.toString().toInt()).size==0)
                    {
                        PhotoDAO.insertPhoto(PhotoToUpdate)
                    }
                    else {
                        PhotoToUpdate.photoID=PhotoDAO.getByID(c_id.text.toString().toInt())[0].photoID
                        PhotoDAO.update(PhotoToUpdate)
                    }


                }
            }
        }




        super.onCreate(savedInstanceState)
        setContentView(R.layout.cosplay_edit)
        val costume = intent.extras?.get("costume") as Costume

        val costume_id = costume.costumeID
        val costume_fandom = costume.fandom!!
        val costume_character = costume.character!!
        val costume_status = costume.status!!
        val costume_progress = costume.progress!!








        val binding = CosplayEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val current_vm = EditMViewModel(costume_id, costume_fandom, costume_character, costume_status, costume_progress)

        binding.viewModel = current_vm
        binding.ecHandlers = handlers

        val spinner: Spinner = findViewById(R.id.c_s)


        ArrayAdapter.createFromResource(
            this,
            R.array.C_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
        spinner.setSelection(costume_status)

        val db = AppDatabase.getInstance(applicationContext)
        val eventDao = db.EventsDao()
        val detailDao = db.DetailDao()
        val MaterialsPlannedDao = db.MaterialsPlannedDao()



        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            val repos = ReposEvent(eventDao, costume_id)
            //recyclerView.adapter = EventRV(repos.allEvents, 0)
            val adapter = EventRV(repos.filteredEvents, costume_id)
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@CosplayEditActivity)
            val divider = DividerItemDecoration(this@CosplayEditActivity,DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@CosplayEditActivity,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)

            recyclerView.adapter = adapter

            //adapter.onEventClickListener = { position, event ->
            //    EventsonEventClickListener( event, this@EditMainActivity)
            //}


        }

        lifecycleScope.launch {
            //Log.v("MYDEBUG", "Corrrr")

            val repos = ReposDetail(detailDao, costume_id)
            //recyclerView.adapter = EventRV(repos.allEvents, 0)
            val adapter = DetailRV(repos.filteredDetails)
            val recyclerView: RecyclerView = findViewById(R.id.recyclerViewD)
            recyclerView.layoutManager = LinearLayoutManager(this@CosplayEditActivity)
            //recyclerView.addItemDecoration(DividerItemDecoration(this@EditMainActivity, LinearLayoutManager.VERTICAL))

            val divider = DividerItemDecoration(this@CosplayEditActivity,DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(this@CosplayEditActivity,R.drawable.divider)!!)
            recyclerView.addItemDecoration(divider)

            recyclerView.adapter = adapter



            adapter.onDetailClickListener = { position, detail ->
                omnCostume(this@CosplayEditActivity, detail, costume_id)
            }

            adapter.onDetailLongClickListener = {position, detail ->
                onCostumeLong(this@CosplayEditActivity, detail, detailDao, MaterialsPlannedDao, costume_id, recyclerView)
                true
            }


        }



        val PhotoDAO = db.PhotoDAO()


      if (PhotoDAO.getByID(costume_id).size!=0 && hasStoragePermission(this))
        {
            val avatar = findViewById<ImageView>(R.id.image_avatar)
            val uri = PhotoDAO.getByID(costume_id)[0].photo?.toUri()
            avatar.setImageURI(uri)
            //Log.d("MyLogs", "There are saved photo..." + PhotoDAO.getByID(costume_id)[0].photo)
            //Log.d("MyLogs", "There are saved photo..." + uri)

        }






    }




}







class EditMViewModel(var costume_id : Int, var costume_fandom : String, var costume_character: String, var costume_status: Int, var costume_progress: Int) : ViewModel() {
}

suspend fun showAlertDialog(context: Context, mess : String, title : String): Boolean {
    return suspendCoroutine { continuation ->
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(mess)
        builder.setPositiveButton(R.string.str_yes) { dialog, which ->
            continuation.resume(true)
        }
        builder.setNegativeButton(R.string.str_no) { dialog, which ->
            continuation.resume(false)
        }
        builder.setOnCancelListener {
            continuation.resume(false)
        }
        builder.show()
        Log.v("MYDEBUG", "Corrrr")
    }
}

fun omnCostume(context: Context, detail :Detail, costume_id : Int)
{
    val intent = Intent(context, DetailEditNew::class.java)
    intent.putExtra("costume_id", costume_id)
    intent.putExtra("edit_flag", 1)
    intent.putExtra("detail", detail)
    context.startActivity(intent)
}

fun onCostumeLong(context: Context, detail : Detail, detailDao : DetailDao, MaterialsPlannedDao : MaterialsPlannedDao, costume_id : Int, recyclerView : RecyclerView){
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.str_delete_detail)
    val message = context.getString(R.string.str_delete_detail_message)
    builder.setMessage(message + " " + detail.detail)
    builder.setPositiveButton(R.string.str_yes) { dialog, which ->
        detailDao.delete(detail)
        MaterialsPlannedDao.deleteByDetail(detail.detailID)
        val repos = ReposDetail(detailDao, costume_id)
        val adapter = DetailRV(repos.filteredDetails)
        recyclerView.adapter = adapter

        adapter.onDetailClickListener = { position, detail ->
            omnCostume(context, detail, costume_id)
        }
        adapter.onDetailLongClickListener = {position, detail ->
            onCostumeLong(context, detail, detailDao, MaterialsPlannedDao, costume_id, recyclerView)
            true
        }
    }

    builder.setNegativeButton(R.string.str_no) { dialog, which ->
    }

    builder.show()
}

fun hasStoragePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

