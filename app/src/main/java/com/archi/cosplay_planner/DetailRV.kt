package com.archi.cosplay_planner


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.roomDatabase.Detail


class DetailRV(private val details: List<Detail>): RecyclerView.Adapter<DetailRV.DetailViewHolder>() {

    var onDetailClickListener: ((position: Int, event : Detail) -> Unit)? = null
    var onDetailLongClickListener: ((position: Int, event: Detail) -> Boolean)? = null

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.detail_name)
        val type: ImageView = itemView.findViewById(R.id.detail_type)
        val progress: ImageView = itemView.findViewById(R.id.detail_progress)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.l_detail_rv, parent, false)
        return DetailViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.name.text = details[position].detail

        when (details[position].progress)
        {
            1 -> holder.progress.setBackgroundResource(R.drawable.p_done)
            2 -> holder.progress.setBackgroundResource(R.drawable.p_hold)
            else -> holder.progress.setBackgroundResource(R.drawable.p_progress)
        }

        when (details[position].type)
        {
            0 -> holder.type.setBackgroundResource(R.drawable.t_shoe)
            1 -> holder.type.setBackgroundResource(R.drawable.t_hat)
            2 -> holder.type.setBackgroundResource(R.drawable.t_access)
            3 -> holder.type.setBackgroundResource(R.drawable.t_props)
            4 -> holder.type.setBackgroundResource(R.drawable.t_top)
            5 -> holder.type.setBackgroundResource(R.drawable.t_bot)
            6 -> holder.type.setBackgroundResource(R.drawable.t_lan)
            else -> holder.type.setBackgroundResource(R.drawable.t_other)
        }




        holder.itemView.setOnClickListener {
            onDetailClickListener?.invoke(holder.adapterPosition, details[position])

        }
        holder.itemView.setOnLongClickListener{
            onDetailLongClickListener?.invoke(position, details[position]) ?: false
        }

    }

    override fun getItemCount(): Int {
        return details.size
    }



}







