package com.example.antonrooseleer.ankihelper.util

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionUtil {

    companion object {

        val REQUEST_CODE = 200

        fun checkOcrPermissions(context: Context, fragment: Fragment): Boolean {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //request permission
                fragment.requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
                return false
            }
            //has permission
            return true

        }


        fun checkAnkiPermission(activity: Activity): Boolean {
            if (AnkiDroidUtil.shouldRequestPermission(activity)) {
                AnkiDroidUtil.requestPermission(activity, 200)
                return false
            }
            //has permission
            return true
        }
    }
}