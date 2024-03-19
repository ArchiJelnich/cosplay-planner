package com.archi.cosplay_planner


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.P_ROOM.Events


class EventRV(private val events: List<Events>, val filter: Int,): RecyclerView.Adapter<EventRV.EventViewHolder>() {

    var onEventClickListener: ((position: Int, event : Events) -> Unit)? = null

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.event_name)
        val type: TextView = itemView.findViewById(R.id.event_type)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val image: ImageView = itemView.findViewById(R.id.imageView)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.l_events_rv, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.name.text = events[position].event
        holder.type.text = events[position].type.toString()
        holder.date.text = events[position].date

        holder.itemView.setOnClickListener {
                onEventClickListener?.invoke(holder.adapterPosition, events[position])

        }

    }

    override fun getItemCount(): Int {
        return events.size
    }



}







