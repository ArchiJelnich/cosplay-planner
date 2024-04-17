package com.archi.cosplay_planner

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.Costume
import com.archi.cosplay_planner.P_ROOM.Events


class MainRV(private val costumes_a: List<Costume>, private val costumes_f: List<Costume>, private val costumes_p: List<Costume>, private val costumes_h: List<Costume>, private val filter: Int): RecyclerView.Adapter<MainRV.MyViewHolder>() {

    var onEventClickListener: ((position: Int, costume : Costume) -> Unit)? = null
    var onEventLongClickListener: ((position: Int, event: Costume) -> Boolean)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fandom: TextView = itemView.findViewById(R.id.textView)
            val character: TextView = itemView.findViewById(R.id.textView2)
            val image: ImageView = itemView.findViewById(R.id.imageView)
            val progress: TextView = itemView.findViewById(R.id.textView3)
            val status: TextView = itemView.findViewById(R.id.textView4)
            val progress_bar: ProgressBar = itemView.findViewById(R.id.progressBar)
        }

    private var costumes = filtering()



    private fun filtering() : List<Costume>
    {
        Log.v("MYDEBUG", "Filter " + filter)
        return when (filter) {
            -1 -> costumes_a
            0 -> costumes_p
            1 -> costumes_f
            2 -> costumes_h
            else -> costumes_a
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.l_main_rv, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fandom.text = costumes[position].fandom
        holder.character.text = costumes[position].character
        holder.progress.text = costumes[position].progress.toString()
        holder.status.text = costumes[position].status.toString()
        costumes[position].progress?.let { holder.progress_bar.setProgress(it) }

        holder.itemView.setOnClickListener {
            onEventClickListener?.invoke(holder.adapterPosition, costumes[position])

        }

        holder.itemView.setOnLongClickListener{
            onEventLongClickListener?.invoke(position, costumes[position]) ?: false
        }

    }

    override fun getItemCount(): Int {
        return costumes.size
    }



    }








