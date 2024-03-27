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


class EventRV(private val events: List<Events>, val filter: Int): RecyclerView.Adapter<EventRV.EventViewHolder>() {

    var onEventClickListener: ((position: Int, event : Events) -> Unit)? = null
    var onEventLongClickListener: ((position: Int, event: Events) -> Boolean)? = null

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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.name.text = events[position].event
        holder.type.text = events[position].type.toString()
        var date = events[position].date.toString()
        val ob_date = string_to_data(date)
        date = fulldata_to_string(ob_date)
        holder.date.text = date

        when (check_if_in_future(date))
        {
            2 -> holder.name.setBackgroundResource(R.color.white_t)
            1 -> holder.name.setBackgroundResource(R.color.red_t)

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







