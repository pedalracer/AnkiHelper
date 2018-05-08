package com.example.antonrooseleer.ankihelper.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.TabLayout
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.util.AnkiDroidUtil
import com.example.antonrooseleer.ankihelper.adapter.OnboardingPager
import kotlinx.android.synthetic.main.onboarding_activity.*


class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AnkiDroidUtil.shouldRequestPermission(this)) {
            AnkiDroidUtil.requestPermission(this, 200)
        }
        setContentView(R.layout.onboarding_activity)
        val onboardingAdapter = OnboardingPager(supportFragmentManager)
        pager.adapter = onboardingAdapter
        (itemTabs as TabLayout)
        itemTabs.setupWithViewPager(pager)
        itemTabs.setTabGravity(TabLayout.GRAVITY_FILL)
    }
}