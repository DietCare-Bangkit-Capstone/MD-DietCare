package com.capstone.dietcare.ui.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailParcel(
    val name: String,
    val cal:Double
): Parcelable
