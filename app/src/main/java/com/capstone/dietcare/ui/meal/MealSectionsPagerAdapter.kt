package com.capstone.dietcare.ui.meal

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MealSectionsPagerAdapter (activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    var email : String = ""
    var password : String = ""

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = Model1Fragment()
                fragment.arguments = Bundle().apply {
                    putString("USER_EMAIL", email)
                    putString("USER_PASSWORD", password)
                }
            }
            1 -> fragment = Model2Fragment()
        }
        return fragment as Fragment
    }
}