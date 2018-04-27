package com.example.antonrooseleer.ankihelper.activity

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.adapter.SlidingImageAdapter
import com.example.antonrooseleer.ankihelper.util.AnkiDroidUtil
import kotlinx.android.synthetic.main.onboarding_activity.*



class OboardingActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AnkiDroidUtil.shouldRequestPermission(this)){
            AnkiDroidUtil.requestPermission(this, 200)
        }
        setContentView(R.layout.onboarding_activity)
        initViewPager()
    }

    private fun initViewPager () {
        val imageList = arrayListOf<Int>(R.drawable.onboarding_1, R.drawable.onboarding_2, R.drawable.onboarding_3, R.drawable.onboarding_4, R.drawable.onboarding_5, R.drawable.onboarding_6, R.drawable.onboarding_7, R.drawable.onboarding_8)
        onboardingTutorial.adapter = SlidingImageAdapter(this, imageList)

    }
}