package com.archi.cosplay_planner.roomDatabase

class Repos (costumeDao: CostumeDao) {

      var allCosplay: List<Costume> = costumeDao.getAll()
      var filteredCosplayProgress: List<Costume> = costumeDao.getInProgress()
      var filteredCosplayFinished: List<Costume> = costumeDao.getFinished()
      var filteredCosplayOnHold: List<Costume> = costumeDao.getOnHold()

}

class ReposEvent (eventsDao: EventsDao, filter: Int) {

      var allEvents: List<Events> = eventsDao.getAll().sortedByDescending { it.dateSorted}
      var filteredEvents: List<Events> = eventsDao.getByCostume(filter).sortedByDescending { it.dateSorted}
// NEED NORMAL SORTER

}

class ReposDetail (detailDao: DetailDao, costumeID: Int) {

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


class ReposBMaterial ( materialsDao: MaterialsDao) {

      var allMaterial: List<Materials> = materialsDao.getAll()
}

class ReposBMaterialFilter (materialsDao: MaterialsDao, filter : String) {

      private var newFilter = "%$filter%"
      var filterMaterial: List<Materials> = materialsDao.getFiltered(newFilter)
}

class ReposBPMaterial (materialsPlannedDao: MaterialsPlannedDao, detailID: Int) {
      var materialsPlannedList: List<MaterialsPlanned> = materialsPlannedDao.getByDetail(detailID)
}

