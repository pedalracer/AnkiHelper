package com.example.antonrooseleer.ankihelper.activity

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.util.AnkiDroidUtil
import kotlinx.android.synthetic.main.onboarding_activity.*

class OboardingActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AnkiDroidUtil.shouldRequestPermission(this)){
            AnkiDroidUtil.requestPermission(this, 200)
        }
        setContentView(R.layout.onboarding_activity)
        onboardingVideo.setVideoPath("file:///android_asset/tutorial.264")

    }
}