package com.capstone.dietcare.data.local.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "Name")
    var name : String ?= null,

    @ColumnInfo(name = "Image")
    var img : String ?= null,

    @ColumnInfo(name = "Calories")
    var cal : Double?= null,

    @ColumnInfo(name = "Time")
    var time : String?= null,

    @ColumnInfo(name = "Portion")
    var port : Double?= null,


)
