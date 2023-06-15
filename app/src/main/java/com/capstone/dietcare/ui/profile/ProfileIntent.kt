package com.capstone.dietcare.ui.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ProfileIntent (
    val name: String ?=null,
    val date: String?=null,
    val sex: String?=null,
    val height: Double?=null,
    val weight: Double?=null
): Parcelable