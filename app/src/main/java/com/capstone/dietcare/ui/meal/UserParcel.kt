package com.capstone.dietcare.ui.meal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserParcel (
    val email : String,
    val password: String
    ):Parcelable