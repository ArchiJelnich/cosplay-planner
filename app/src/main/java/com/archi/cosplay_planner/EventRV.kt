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
import com.archi.cosplay_planner.infra.checkIfInFuture
import com.archi.cosplay_planner.roomDatabase.Events
import com.archi.cosplay_planner.infra.fullDateToString
import com.archi.cosplay_planner.infra.stringToData


class EventRV(private val events: List<Events>, val filter: Int): RecyclerView.Adapter<EventRV.EventViewHolder>() {

    var onEventClickListener: ((position: Int, event : Events) -> Unit)? = null
    var onEventLongClickListener: ((position: Int, event: Events) -> Boolean)? = null

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.event_name)
        val type: ImageView = itemView.findViewById(R.id.event_type)
        val date: TextView = itemView.findViewById(R.id.event_date)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_rv, parent, false)
        return EventViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.name.text = events[position].event

        when(events[position].type)
        {
            0 -> holder.type.setBackgroundResource(R.drawable.t_conv)
            2 -> holder.type.setBackgroundResource(R.drawable.t_photo)
            else -> holder.type.setBackgroundResource(R.drawable.t_party)
        }

        var date = events[position].date.toString()
        val ob_date = stringToData(date)
        date = fullDateToString(ob_date)
        holder.date.text = date

        when (checkIfInFuture(date))
        {
            2 -> holder.name.setBackgroundResource(R.drawable.rounded_white)
            1 -> holder.name.setBackgroundResource(R.drawable.rounded_red)

        }



        holder.itemView.setOnClickListener {
                onEventClickListener?.invoke(holder.adapterPosition, events[position])

        }
        holder.itemView.setOnLongClickListener{
            onEventLongClickListener?.invoke(position, events[position]) ?: false
        }

    }

    override fun getItemCount(): Int {
        return events.size
    }



}







