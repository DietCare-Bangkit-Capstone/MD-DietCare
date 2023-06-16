package com.capstone.dietcare.data.local.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : DataUser)

    @Query("SELECT * from DataUser WHERE Calories IS NOT NULL ORDER BY id DESC")
    fun getAllMeal(): LiveData<List<DataUser>>

    @Query("SELECT * from DataUser WHERE Calories > 20 ORDER BY id ASC")
    fun getAllCalData(): LiveData<List<DataUser>>

    @Query("SELECT * FROM DATAUSER ORDER BY id DESC LIMIT 1")
    fun getLatestMeal(): LiveData<DataUser>

    @Query("UPDATE DATAUSER SET Meal1Name=:m1name, Meal1Image=:m1img, Meal1Calories =:m1cal, Meal1Time=:m1time, Meal1Portion=:m1port  WHERE ID IN (SELECT ID FROM DataUser ORDER BY ID DESC LIMIT 1)")
    fun updateMeal1(m1name: String, m1img: String, m1cal: Double, m1time:String, m1port:Double)

    @Query("UPDATE DATAUSER SET Meal2Name=:m2name, Meal2Image=:m2img, Meal2Calories =:m2cal, Meal2Time=:m2time, Meal2Portion=:m2port  WHERE ID IN (SELECT ID FROM DataUser ORDER BY ID DESC LIMIT 1)")
    fun updateMeal2(m2name: String?, m2img: String?, m2cal: Double?, m2time:String?, m2port:Double?)

    @Query("UPDATE DATAUSER SET Meal3Name=:m3name, Meal3Image=:m3img, Meal3Calories =:m3cal, Meal3Time=:m3time, Meal3Portion=:m3port  WHERE ID IN (SELECT ID FROM DataUser ORDER BY ID DESC LIMIT 1)")
    fun updateMeal3(m3name: String?, m3img: String?, m3cal: Double?, m3time:String?, m3port:Double?)

    @Query("UPDATE DATAUSER SET Meal4Name=:m4name, Meal4Image=:m4img, Meal4Calories =:m4cal, Meal4Time=:m4time, Meal4Portion=:m4port  WHERE ID IN (SELECT ID FROM DataUser ORDER BY ID DESC LIMIT 1)")
    fun updateMeal4(m4name: String?, m4img: String?, m4cal: Double?, m4time:String?, m4port:Double?)

    @Query("UPDATE DATAUSER SET Meal5Name=:m5name, Meal5Image=:m5img, Meal5Calories =:m5cal, Meal5Time=:m5time, Meal5Portion=:m5port  WHERE ID IN (SELECT ID FROM DataUser ORDER BY ID DESC LIMIT 1)")
    fun updateMeal5(m5name: String?, m5img: String?, m5cal: Double?, m5time:String?, m5port:Double?)

    @Query("SELECT * FROM DataUser WHERE BodyWeight IS NOT NULL ORDER BY id ASC")
    fun getAllBodyWeight(): LiveData<List<DataUser>>

    @Query("SELECT * FROM DataUser WHERE BodyWeight IS NOT NULL ORDER BY id DESC LIMIT 1")
    fun getLatestBodyWeight(): LiveData<DataUser>

    @Query("DELETE FROM DataUser WHERE id = :id")
    fun delete(id: Int)

    @Query("INSERT INTO DataUser (Date, DateInMilliSecond, BodyWeight) VALUES (:date, :dateTime, :weight)")
    fun updateBodyWeight(weight: Double?, dateTime : Long, date: String)

}