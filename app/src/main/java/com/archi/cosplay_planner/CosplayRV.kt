package com.archi.cosplay_planner

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.roomDatabase.Costume


class CosplayRV(private val costumes_a: List<Costume>, private val costumes_f: List<Costume>, private val costumes_p: List<Costume>, private val costumes_h: List<Costume>, private val filter: Int): RecyclerView.Adapter<CosplayRV.MyViewHolder>() {

    var onEventClickListener: ((position: Int, costume : Costume) -> Unit)? = null
    var onEventLongClickListener: ((position: Int, event: Costume) -> Boolean)? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val fandom: TextView = itemView.findViewById(R.id.textView)
            val character: TextView = itemView.findViewById(R.id.textView2)
            val image: ImageView = itemView.findViewById(R.id.imageView)
            val status: ImageView = itemView.findViewById(R.id.i_status)
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
        //holder.status.text = costumes[position].status.toString()

        when(costumes[position].status)
        {
            1 -> holder.status.setBackgroundResource(R.drawable.p_done)
            2 -> holder.status.setBackgroundResource(R.drawable.p_hold)
            else -> holder.status.setBackgroundResource(R.drawable.p_progress)
        }


        costumes[position].progress?.let { holder.progress_bar.setProgress(it) }

        if (costumes[position].status==1)
        {
            holder.character.setBackgroundResource(R.drawable.rounded_green)
        }

        holder.itemView.setOnClickListener {
            Log.v("MyLogCosplay", "clicked " + position)
            Log.v("MyLogCosplay", "clicked " + costumes[position])
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








