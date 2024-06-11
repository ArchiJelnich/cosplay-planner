package com.archi.cosplay_planner.roomDatabase

class Repos (costumeDao: CostumeDao, val filter: Int) {

      var allCosplay: List<Costume> = costumeDao.getAll()
      var filteredCosplayProgress: List<Costume> = costumeDao.getInProgress()
      var filteredCosplayFinished: List<Costume> = costumeDao.getFinished()
      var filteredCosplayOnHold: List<Costume> = costumeDao.getOnHold()

}

class ReposEvent (private val eventsDao: EventsDao, private val filter: Int) {

      var allEvents: List<Events> = eventsDao.getAll().sortedByDescending { it.dateSorted}
      var filteredEvents: List<Events> = eventsDao.getByCostume(filter).sortedByDescending { it.dateSorted}
// NEED NORMAL SORTER

}

class ReposDetail (private val detailDao: DetailDao, private val costumeID: Int) {

      var filteredDetails: List<Detail> = detailDao.getByCostume(costumeID).sortedByDescending { it.progress}
      var costumeProgress = progression()

private fun progression(): Int {
      var sum = 0.0
      for (filter in filteredDetails)
      {
            if (filter.progress==1) {
                  sum += filter.progress
            }
      }

      return if (filteredDetails.isNotEmpty()) {
            (sum / filteredDetails.size * 100).toInt()
      } else {
            0
      }
}

}


class ReposBMaterial (private val materialsDao: MaterialsDao) {

      var allMaterial: List<Materials> = materialsDao.getAll()
}

class ReposBMaterialFilter (private val materialsDao: MaterialsDao, private val filter : String) {

      private var newFilter = "%$filter%"
      var filterMaterial: List<Materials> = materialsDao.getFiltered(newFilter)
}

class ReposBPMaterial (private val materialsPlannedDao: MaterialsPlannedDao, private val detailID: Int) {
      var materialsPlannedList: List<MaterialsPlanned> = materialsPlannedDao.getByDetail(detailID)
}

