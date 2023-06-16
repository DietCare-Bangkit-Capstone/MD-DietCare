package com.capstone.dietcare.data.local.user

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataUserRepository (context: Context){
    private val mDataUserDao: DataUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = DataUserRoomDatabase.getDatabase(context)
        mDataUserDao = db.dataUserDao()
    }

    fun insert(data: DataUser){
        executorService.execute { mDataUserDao.insert(data) }
    }

    fun getLatestMeal(): LiveData<DataUser> = mDataUserDao.getLatestMeal()

    fun getAllMeal(): LiveData<List<DataUser>> = mDataUserDao.getAllMeal()

    fun getAllCalData(): LiveData<List<DataUser>> = mDataUserDao.getAllCalData()

    fun delete(id: Int){
        executorService.execute { mDataUserDao.delete(id) }
    }

    fun updateBodyWeight(weight: Double, dateTime: Long, date : String){
        executorService.execute { mDataUserDao.updateBodyWeight(weight, dateTime, date) }
    }

    fun getLatestBodyWeight(): LiveData<DataUser> = mDataUserDao.getLatestBodyWeight()

    fun getAllBodyWeight(): LiveData<List<DataUser>> = mDataUserDao.getAllBodyWeight()

    fun updateMeal1(m1name: String, m1img: String, m1cal: Double, m1time:String, m1port:Double){
        executorService.execute { mDataUserDao.updateMeal1(m1name, m1img, m1cal, m1time, m1port) }
    }
    
    fun updateMeal2(m2name: String?, m2img: String?, m2cal: Double?, m2time:String?, m2port:Double?){
        executorService.execute { mDataUserDao.updateMeal2(m2name, m2img, m2cal, m2time, m2port) }
    }
    
    fun updateMeal3(m3name: String?, m3img: String?, m3cal: Double?, m3time:String?, m3port:Double?){
        executorService.execute { mDataUserDao.updateMeal3(m3name, m3img, m3cal, m3time, m3port) }
    }
    
    fun updateMeal4(m4name: String?, m4img: String?, m4cal: Double?, m4time:String?, m4port:Double?){
        executorService.execute { mDataUserDao.updateMeal4(m4name, m4img, m4cal, m4time, m4port) }
    }

    fun updateMeal5(m5name: String?, m5img: String?, m5cal: Double?, m5time:String?, m5port:Double?){
        executorService.execute { mDataUserDao.updateMeal5(m5name, m5img, m5cal, m5time, m5port) }
    }

}