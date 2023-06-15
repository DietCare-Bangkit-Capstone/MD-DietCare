package com.capstone.dietcare.ui.recommendation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecIntent(
    var name : String?= null,
    var img : String?= null,
    var cal : Double?= null,
    var time : String?= null,
    var port : Double?= null,
):Parcelable
