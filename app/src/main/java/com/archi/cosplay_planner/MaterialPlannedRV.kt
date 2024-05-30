package com.archi.cosplay_planner


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import com.archi.cosplay_planner.P_ROOM.MaterialsPlanned
import com.archi.cosplay_planner.P_ROOM.ReposBPMaterial


class MaterialPlannedRV(private val materials: List<MaterialsPlanned>, private val all_materials: List<Materials>,): RecyclerView.Adapter<MaterialPlannedRV.BMaterialPlannedViewHolder>() {

    var onBMaterialPClickListener: ((position: Int, event : MaterialsPlanned) -> Unit)? = null
    var onBMaterialPLongClickListener: ((position: Int, event: MaterialsPlanned) -> Boolean)? = null

    inner class BMaterialPlannedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val material_unit: TextView = itemView.findViewById(R.id.material)
        val material_name: TextView = itemView.findViewById(R.id.unit)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMaterialPlannedViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.l_bmaterial_rv, parent, false)

        return BMaterialPlannedViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: BMaterialPlannedViewHolder, position: Int) {
        holder.material_unit.text = materials[position].quantity.toString()
        holder.material_name.text = all_materials[materials[position].materialID!!].material.toString()


        holder.itemView.setOnClickListener {
            onBMaterialPClickListener?.invoke(holder.adapterPosition, materials[position])

        }
        holder.itemView.setOnLongClickListener{
            onBMaterialPLongClickListener?.invoke(position, materials[position]) ?: false
        }

    }

    override fun getItemCount(): Int {
        return materials.size
    }



}







