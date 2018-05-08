package com.example.antonrooseleer.ankihelper.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.antonrooseleer.ankihelper.fragment.OcrFragment
import com.example.antonrooseleer.ankihelper.fragment.OnboardingFragment

/**
 * Created by a_176 on 7/05/2018.
 */
class OnboardingPager(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val NUM_ITEMS = 2

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
//            0 // Fragment # 0 - This will show FirstFragment
//            -> return OnboardingFragment.newInstance()
//            1 // Fragment # 0 - This will show FirstFragment different title
//            -> return OcrFragment.newInstance()
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Tutorial"
            1 -> return "OCR"
        }
        return ""
    }
}