package com.archi.cosplay_planner

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


      var allEvents: List<Events> = EventsDao.getAll()

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