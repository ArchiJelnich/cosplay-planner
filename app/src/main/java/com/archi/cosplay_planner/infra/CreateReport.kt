package com.archi.cosplay_planner.infra

import com.archi.cosplay_planner.roomDatabase.AppDatabase

fun CreateReport(db : AppDatabase): String {

        val materialsPlannedDao = db.MaterialsPlannedDao()
        val costumeDao = db.CostumeDao()
        val materialsDao = db.MaterialsDao()
        val detailDao = db.DetailDao()
        var resultString = ""
        val costumesInProgress = costumeDao.getCostumeIDByInProgress()
        val detailOfProgress = ArrayList<Int>()

        for (ID in costumesInProgress) {
            detailOfProgress.addAll(detailDao.getIDByCostume(ID))
        }

        val resourcesForReport = materialsPlannedDao.getAllForReport(detailOfProgress)

        for (material in resourcesForReport) {
            resultString =
                resultString + materialsDao.getNameByID(material.M)[0] + " " + materialsDao.getUnitByID(
                    material.M
                )[0] + " " + material.Q + "\n"
        }

        return resultString

}