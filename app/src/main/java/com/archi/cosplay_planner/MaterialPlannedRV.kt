package com.archi.cosplay_planner


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.archi.cosplay_planner.roomDatabase.Materials
import com.archi.cosplay_planner.roomDatabase.MaterialsDao
import com.archi.cosplay_planner.roomDatabase.MaterialsPlanned


class MaterialPlannedRV(private val materials: List<MaterialsPlanned>, private val all_materials: List<Materials>, var materialDao : MaterialsDao): RecyclerView.Adapter<MaterialPlannedRV.BMaterialPlannedViewHolder>() {

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

        Log.d("MyLogMaterials", "materials " + materials[position].materialID)
        Log.d("MyLogMaterials", "all " + materialDao.getAll())


        holder.material_unit.text = materials[position].quantity.toString()
        holder.material_name.text =
            materials[position].materialID?.let { materialDao.getNameByID(it)[0].toString() }


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







