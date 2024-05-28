package com.archi.cosplay_planner.P_Infra

import android.util.Log
import com.archi.cosplay_planner.P_ROOM.AppDatabase
import com.archi.cosplay_planner.P_ROOM.MaterialQuanPair
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun CreateReport(db : AppDatabase): String {



        var MaterialsPlannedDao = db.MaterialsPlannedDao()
        var CostumeDao = db.CostumeDao()
        var MaterialsDao = db.MaterialsDao()
        var DetailDao = db.DetailDao()
        var result_string = ""


        var costumesInProgress = CostumeDao.getIDByInProgress()
        var detailOfProgress = ArrayList<Int>()

        for (ID in costumesInProgress) {
            detailOfProgress.addAll(DetailDao.getIDByCostume(ID))
        }


        var allRes = MaterialsPlannedDao.getAllRes(detailOfProgress)



        Log.d("MyLog", "All res " + allRes)


        for (plannedmaterial in allRes) {
            result_string =
                result_string + MaterialsDao.getNameByID(plannedmaterial.M)[0] + " " + MaterialsDao.getUnitByID(
                    plannedmaterial.M
                )[0] + " " + plannedmaterial.Q + "\n"
        }


        return result_string

}