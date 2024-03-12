package com.archi.cosplay_planner

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainRV(private val costumes_a: List<Costume>, private val costumes_f: List<Costume>, private val costumes_p: List<Costume>, private val costumes_h: List<Costume>, val filter: Int): RecyclerView.Adapter<MainRV.MyViewHolder>() {
        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fandom: TextView = itemView.findViewById(R.id.textView)
            val character: TextView = itemView.findViewById(R.id.textView2)
            val image: ImageView = itemView.findViewById(R.id.imageView)
            val progress: TextView = itemView.findViewById(R.id.textView3)
            val status: TextView = itemView.findViewById(R.id.textView4)
        }

    var costumes = filtering()



    fun filtering() : List<Costume>
    {
        Log.v("MYDEBUG", "Filter " + filter)
        when (filter)
        {
            -1 -> return  costumes_a
            0 -> return costumes_p
            1 -> return costumes_f
            else -> return costumes_h
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

    }

    override fun getItemCount(): Int {
        return costumes.size
    }



    }




