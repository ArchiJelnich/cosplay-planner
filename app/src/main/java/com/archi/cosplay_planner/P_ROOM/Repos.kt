package com.archi.cosplay_planner.P_ROOM

import android.util.Log

class Repos (private val CostumeDao: CostumeDao, val filter: Int) {


      var allCosplay: List<Costume> = CostumeDao.getAll()
      var filteredCosplay_p: List<Costume> = CostumeDao.getP()
      var filteredCosplay_f: List<Costume> = CostumeDao.getF()
      var filteredCosplay_h: List<Costume> = CostumeDao.getH()




/*
      suspend fun getAllCostumes(): List<Costume> {
            return CostumeDao.getAll()
      }


      */


   // @Suppress("RedundantSuspendModifier")
   // @WorkerThread
   // suspend fun getAll() {
   //     CostumeDao.getAll()
   // }
}

class ReposEvent (private val EventsDao: EventsDao, val filter: Int) {


      var allEvents: List<Events> = EventsDao.getAll().sortedByDescending { it.date_sorted}
      var filteredEvents: List<Events> = EventsDao.getByCostume(filter).sortedByDescending { it.date_sorted}
// NEED NORMAL SORTER

      /*
            suspend fun getAllCostumes(): List<Costume> {
                  return CostumeDao.getAll()
            }


            */


      // @Suppress("RedundantSuspendModifier")
      // @WorkerThread
      // suspend fun getAll() {
      //     CostumeDao.getAll()
      // }
}

class ReposDetail (private val DetailDao: DetailDao, val costume_id: Int) {


      var filteredDetails: List<Detail> = DetailDao.getByCostume(costume_id).sortedByDescending { it.progress}
      var costume_progress = progression()



private fun progression(): Int {
      Log.d("MyLog", "I'm calculating")
      var sum = 0.0
      for (filter in filteredDetails)
      {
            Log.d("MyLog", "Count: " + filter.progress)
            if (filter.progress==1) {
                  sum += filter.progress
            }
      }


      return if (filteredDetails.isNotEmpty()) {
            Log.d("MyLog", "Size: " + filteredDetails.size)
            Log.d("MyLog", "Sum: " + sum)
            Log.d("MyLog", "I will return: " + sum / filteredDetails.size * 100)
            (sum / filteredDetails.size * 100).toInt()
      } else {
            Log.d("MyLog", "I will return: " + 0)
            0
      }
}
      /*
            suspend fun getAllCostumes(): List<Costume> {
                  return CostumeDao.getAll()
            }


            */


      // @Suppress("RedundantSuspendModifier")
      // @WorkerThread
      // suspend fun getAll() {
      //     CostumeDao.getAll()
      // }
}


class ReposBMaterial (private val MaterialsDao: MaterialsDao) {


      var allMaterial: List<Materials> = MaterialsDao.getAll()
}

class ReposBPMaterial (private val MaterialsPlannedDao: MaterialsPlannedDao, val detail_id: Int) {

      var MaterialP: List<MaterialsPlanned> = MaterialsPlannedDao.getByDetail(detail_id)

}