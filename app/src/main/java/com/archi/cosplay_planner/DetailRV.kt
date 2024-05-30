package com.archi.cosplay_planner


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.Events
import com.archi.cosplay_planner.P_Infra.check_if_in_future
import com.archi.cosplay_planner.P_Infra.fulldata_to_string
import com.archi.cosplay_planner.P_Infra.string_to_data
import com.archi.cosplay_planner.P_ROOM.Detail


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
            4 -> holder.type.setBackgroundResource(R.drawable.t_top)
            6 -> holder.type.setBackgroundResource(R.drawable.t_lan)
            else -> holder.type.setBackgroundResource(R.drawable.t_shoe)
        }

        //holder.type.text = details[position].type.toString()
        //holder.progress.text = details[position].progress.toString()

        //holder.type.text = events[position].type.toString()
        //var date = events[position].date.toString()
        //val ob_date = string_to_data(date)
       // date = fulldata_to_string(ob_date)
        //holder.date.text = date

        //when (check_if_in_future(date))
        //{
        //    2 -> holder.name.setBackgroundResource(R.color.white_t)
        //    1 -> holder.name.setBackgroundResource(R.color.red_t)

        //}



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







