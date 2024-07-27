package com.archi.cosplay_planner.infra

import android.util.Log
import com.archi.cosplay_planner.roomDatabase.AppDatabase
import com.archi.cosplay_planner.roomDatabase.MaterialQuanPair

fun createReport(db : AppDatabase): String {

        val materialsPlannedDao = db.MaterialsPlannedDao()
        val costumeDao = db.CostumeDao()
        val materialsDao = db.MaterialsDao()
        val detailDao = db.DetailDao()
        var resultString = ""
        val costumesInProgress = costumeDao.getCostumeIDByInProgress()
        val detailOfProgress = ArrayList<Int>()
        var resourcesForReport = ArrayList<List<MaterialQuanPair>>()
        var materialsPrepared:MutableMap<String, Int> = mutableMapOf()
        var pairArray = ArrayList<MaterialQuanPair>()

        for (CreateReport in costumesInProgress) {
            detailOfProgress.addAll(detailDao.getIDByCostume(CreateReport))
        }


        for (detail in detailOfProgress) {
            resourcesForReport.add(materialsPlannedDao.getAllForReport(detail))
        }







       for (detail in resourcesForReport) {


           for (material in detail) {


               if (materialsPrepared.containsKey(material.M.toString()))
               {
                   var new_value = 0
                   new_value = new_value + materialsPrepared[material.M.toString()]!! + material.Q
                   materialsPrepared.replace(material.M.toString(), new_value)
               }
               else {
                   materialsPrepared.put(material.M.toString(), material.Q)
               }



           }
        }

    for ((key, value) in materialsPrepared) {
        var pair = MaterialQuanPair(key.toInt(), value)
        pairArray.add(pair)
    }


    for (material in pairArray)
    {
              resultString =
                  resultString + materialsDao.getNameByID(material.M)[0] + " " + materialsDao.getUnitByID(
                      material.M
                 )[0] + " " + material.Q + "\n"
    }



        return resultString

}