package com.capstone.dietcare.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "Date")
    var date : String ?= null,

    @ColumnInfo(name = "DateInMilliSecond")
    var ms : Long ?= null,

    @ColumnInfo(name = "BodyWeight")
    var weight : Double?= null,

    @ColumnInfo(name = "Meal")
    var meal : Int ?= null,

    @ColumnInfo(name = "Calories")
    var calor : Double?= null,

    @ColumnInfo(name = "FatContent")
    var fat : Double?= null,

    @ColumnInfo(name = "SaturatedFatContent")
    var satfat : Double?= null,

    @ColumnInfo(name = "CholesterolContent")
    var chol : Double?= null,

    @ColumnInfo(name = "SodiumContent")
    var sodium : Double?= null,

    @ColumnInfo(name = "CarbohydrateContent")
    var carb : Double?= null,

    @ColumnInfo(name = "FiberContent")
    var fiber : Double?= null,

    @ColumnInfo(name = "SugarContent")
    var sugar : Double?= null,

    @ColumnInfo(name = "ProteinContent")
    var protein : Double?= null,

    @ColumnInfo(name = "Meal1Name")
    var m1name : String?= null,

    @ColumnInfo(name = "Meal1Image")
    var m1img : String?= null,

    @ColumnInfo(name = "Meal1Calories")
    var m1cal : Double?= null,

    @ColumnInfo(name = "Meal1Time")
    var m1time : String?= null,

    @ColumnInfo(name = "Meal1Portion")
    var m1port : Double?= null,

    @ColumnInfo(name = "Meal2Name")
    var m2name : String?= null,

    @ColumnInfo(name = "Meal2Image")
    var m2img : String?= null,

    @ColumnInfo(name = "Meal2Calories")
    var m2cal : Double?= null,

    @ColumnInfo(name = "Meal2Time")
    var m2time : String?= null,

    @ColumnInfo(name = "Meal2Portion")
    var m2port : Double?= null,

    @ColumnInfo(name = "Meal3Name")
    var m3name : String?= null,

    @ColumnInfo(name = "Meal3Image")
    var m3img : String?= null,

    @ColumnInfo(name = "Meal3Calories")
    var m3cal : Double?= null,

    @ColumnInfo(name = "Meal3Time")
    var m3time : String?= null,

    @ColumnInfo(name = "Meal3Portion")
    var m3port : Double?= null,

    @ColumnInfo(name = "Meal4Name")
    var m4name : String?= null,

    @ColumnInfo(name = "Meal4Image")
    var m4img : String?= null,

    @ColumnInfo(name = "Meal4Calories")
    var m4cal : Double?= null,

    @ColumnInfo(name = "Meal4Time")
    var m4time : String?= null,

    @ColumnInfo(name = "Meal4Portion")
    var m4port : Double?= null,

    @ColumnInfo(name = "Meal5Name")
    var m5name : String?= null,

    @ColumnInfo(name = "Meal5Image")
    var m5img : String?= null,

    @ColumnInfo(name = "Meal5Calories")
    var m5cal : Double?= null,

    @ColumnInfo(name = "Meal5Time")
    var m5time : String?= null,

    @ColumnInfo(name = "Meal5Portion")
    var m5port : Double?= null

)
