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
import com.archi.cosplay_planner.P_ROOM.Materials


class MaterialBaseRV(private val materials: List<Materials>): RecyclerView.Adapter<MaterialBaseRV.BMaterialViewHolder>() {

    var onBMaterialClickListener: ((position: Int, event : Materials) -> Unit)? = null
    var onBMaterialLongClickListener: ((position: Int, event: Materials) -> Boolean)? = null

    inner class BMaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val material_unit: TextView = itemView.findViewById(R.id.unit)
        val material_name: TextView = itemView.findViewById(R.id.material)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMaterialViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.l_bmaterial_rv, parent, false)
        return BMaterialViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: BMaterialViewHolder, position: Int) {
        holder.material_unit.text = materials[position].material
        holder.material_name.text = materials[position].unit.toString()


        holder.itemView.setOnClickListener {
            onBMaterialClickListener?.invoke(holder.adapterPosition, materials[position])

        }
        holder.itemView.setOnLongClickListener{
            onBMaterialLongClickListener?.invoke(position, materials[position]) ?: false
        }

    }

    override fun getItemCount(): Int {
        return materials.size
    }



}







