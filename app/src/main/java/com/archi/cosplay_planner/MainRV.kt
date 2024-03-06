package com.archi.cosplay_planner

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow


class MainRV(private val costumes: List<Costume>): RecyclerView.Adapter<MainRV.MyViewHolder>() {
        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fandom: TextView = itemView.findViewById(R.id.textView)
            val character: TextView = itemView.findViewById(R.id.textView2)
            val image: ImageView = itemView.findViewById(R.id.imageView)
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
    }

    override fun getItemCount(): Int {
        return  costumes.size
    }

    }




