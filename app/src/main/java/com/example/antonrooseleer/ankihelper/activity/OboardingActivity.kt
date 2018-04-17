package com.example.antonrooseleer.ankihelper.activity

import android.app.Activity
import android.os.Bundle
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.util.AnkiDroidUtil

class OboardingActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AnkiDroidUtil.shouldRequestPermission(this)){
            AnkiDroidUtil.requestPermission(this, 200)
        }
        setContentView(R.layout.onboarding_activity)
    }
}