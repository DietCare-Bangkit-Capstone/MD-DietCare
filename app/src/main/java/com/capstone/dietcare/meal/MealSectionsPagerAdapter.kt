package com.capstone.dietcare.meal

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MealSectionsPagerAdapter (activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = Model1Fragment()
            1 -> fragment = Model2Fragment()
        }
        return fragment as Fragment
    }
}