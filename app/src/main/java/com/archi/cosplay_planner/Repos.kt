package com.archi.cosplay_planner

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.reactivex.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class Repos (private val CostumeDao: CostumeDao) {


      var allCosplay: List<Costume> = CostumeDao.getAll()

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

